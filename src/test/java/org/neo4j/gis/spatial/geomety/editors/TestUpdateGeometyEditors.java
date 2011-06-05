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

import org.geotools.referencing.CRS;
import org.junit.Test;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.operation.Update;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Reverse;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Transform;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This unit test testing all available geometry editors queries:
 * 	- ST_Transform SEARCH | UPDATE
 *  -
 *  
 * @author Andreas Wilhelm
 * 
 */
public class TestUpdateGeometyEditors extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private Layer layer = null;

    protected void setUp(boolean deleteDb, boolean useBatchInserter, boolean autoTx) throws Exception {
        super.setUp(false, true, false);
		try {
			this.loadTestOsmData(Dataset.LAYER_NAME, Dataset.COMMIT_INTERVAL);
			this.spatialService = new SpatialDatabaseService(graphDb());
			this.layer = spatialService.getLayer(Dataset.LAYER_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Test
	public void testTransformUpdate() throws Exception {
		CoordinateReferenceSystem crs = CRS.decode("EPSG:2002");
		Update update = new ST_Transform(crs);
		int results = layer.execute(update);
		assertEquals(2, results);
	}
	
	@Test
	public void testTransformUpdate2() throws Exception {
		Update update = new ST_Transform(Dataset.WORLD_MERCATOR_SRID);
		int results = layer.execute(update);
		assertEquals(2, results);
	}
	
	@Test
	public void testReverseUpdate() throws Exception {
		Update update = new ST_Reverse();
		int results = layer.execute(update);
		assertEquals(2, results);
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