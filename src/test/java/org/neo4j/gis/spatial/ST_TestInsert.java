/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.gis.spatial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.geometry.editors.Dataset;
import org.neo4j.gis.spatial.operation.Insert;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.osm.OSMLayer;
import org.neo4j.gis.spatial.query.ST_Insert;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * A simple testcase to test the insert operation with a Relationship and Properties.
 * 
 * 
 * @author Andreas Wilhelm
 *
 */
public class ST_TestInsert extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private OSMLayer layer = null;
	private boolean debug = true;
	private String wellKnownText = "LINESTRING (30 10, 10 30, 40 40)";
	private WKTReader wktReader = new WKTReader();
	
	protected void setUp(boolean deleteDb, boolean useBatchInserter,
			boolean autoTx) throws Exception {
		super.setUp(false, true, false);
		try {
			this.loadTestOsmData(Dataset.LAYER_NAME, Dataset.COMMIT_INTERVAL);
			this.spatialService = new SpatialDatabaseService(graphDb());
			this.layer = (OSMLayer) spatialService.getLayer(Dataset.LAYER_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testInsertWithGeometryAndRelation() throws Exception {
		// Example data.
		Geometry geometry = wktReader.read(wellKnownText);
		String propertyKey = "networklevel";
		int propertyValue = 15;
		Node relnode = layer.getIndex().get(55l).getGeomNode();
		
		Insert insert = new ST_Insert(geometry);
		insert.addProperty(propertyKey, propertyValue);
		insert.addRelationship(relnode, SpatialRelationshipTypes.NEXT_GEOM);
		List<SpatialDatabaseRecord> records = layer.execute(insert);
		
		Node nNode = layer.getIndex().get(73l).getGeomNode();
		assertEquals(nNode.hasRelationship(Direction.OUTGOING, SpatialRelationshipTypes.NEXT_GEOM), true);
		assertEquals(nNode.getProperty(propertyKey), propertyValue);
		assertEquals(1, records.size());
	}

	public void testBatchInsert() throws ParseException{
		// Add 5000 geometries at one transaction.
		int size = 5000;
		String propertyKey = "roadtype";
		String propertyValue = "highway";
		List<Geometry> geometries = new ArrayList<Geometry>();
		
		Geometry sampleGeomerty = this.wktReader.read(this.wellKnownText);
		for (int i = 0; i < size; i++) {
			geometries.add(sampleGeomerty);
		}
		
		Insert insert = new ST_Insert(geometries);
		insert.addProperty(propertyKey, propertyValue);
		List<SpatialDatabaseRecord> records;
		try {
			records = this.layer.execute(insert);
			assertEquals(size, records.size());
		} catch (SpatialExecuteException e) {
			e.printStackTrace();
		}

	}

	private void loadTestOsmData(String layerName, int commitInterval)
			throws Exception {
		String osmPath = Dataset.OSM_DIR + File.separator + layerName;
		if (this.debug) {
			System.out.println("\n=== Loading layer " + layerName + " from "
					+ osmPath + " ===");
		}
		reActivateDatabase(false, true, false);
		OSMImporter importer = new OSMImporter(layerName);
		importer.importFile(getBatchInserter(), osmPath);
		reActivateDatabase(false, false, false);
		importer.reIndex(graphDb(), commitInterval);
	}

}
