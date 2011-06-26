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
package org.neo4j.gis.spatial.geomety.editors;

import java.io.File;
import java.util.List;

import org.geotools.referencing.CRS;
import org.junit.Test;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.operation.Update;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.osm.OSMLayer;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Reverse;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Transform;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This unit test testing all available geometry output queries: 
 * 	- ST_Transform
 *  - ST_Reverse
 * 
 * @author Andreas Wilhelm
 * 
 */
public class TestUpdateGeometyEditors extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private OSMLayer layer = null;

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

	@Test
	public void testTransformUpdate() throws Exception {
		CoordinateReferenceSystem crs = CRS.decode("EPSG:2002");
		Update update = new ST_Transform(crs);
		List<SpatialDatabaseRecord> records = layer.execute(update);
		assertEquals(2, records.size());
	}
	
	@Test
	public void testTransformUpdate2() throws Exception {
		Update update = new ST_Transform(Dataset.WORLD_MERCATOR_SRID);
		List<SpatialDatabaseRecord> records = layer.execute(update);
		assertEquals(2, records.size());
	}
	
	@Test
	public void testReverseUpdate() throws Exception {
		Update update = new ST_Reverse();
		List<SpatialDatabaseRecord> records = layer.execute(update);
		assertEquals(2, records.size());
	}
	
	private void loadTestOsmData(String layerName, int commitInterval)
			throws Exception {
		String osmPath = Dataset.OSM_DIR + File.separator + layerName;
		System.out.println("\n=== Loading layer " + layerName + " from "
				+ osmPath + " ===");
		reActivateDatabase(false, true, false);
		OSMImporter importer = new OSMImporter(layerName);
		importer.importFile(getBatchInserter(), osmPath);
		reActivateDatabase(false, false, false);
		importer.reIndex(graphDb(), commitInterval);
	}


}