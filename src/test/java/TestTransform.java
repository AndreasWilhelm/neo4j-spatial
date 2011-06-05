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


import java.io.File;
import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.SpatialIndexReader;
import org.neo4j.gis.spatial.geomety.editors.Dataset;
import org.neo4j.gis.spatial.operation.Update;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Transform;


/**
 * 
 * @author Andreas Wilhelm
 *
 */
public class TestTransform extends Neo4jTestCase {

	private SpatialDatabaseService spatialService;
	
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

	public void testTransform() throws Exception {

		loadTestOsmData(Dataset.LAYER_NAME, Dataset.COMMIT_INTERVAL);
		spatialService = new SpatialDatabaseService(graphDb());
		Layer layer = spatialService.getLayer(Dataset.LAYER_NAME);
		
		//Would be nice when we could reduce the code for the user:
		/**
		 * Layer layer = spatialService.getLayer(Default.LAYER_NAME);
		 * List<SpatialDatabaseRecord> results = layer.execute(update or search);
		 * 
		 */
		SpatialIndexReader spatialIndex = layer.getIndex();
		Update update = new ST_Transform(Dataset.WORLD_MERCATOR_SRID);
		spatialIndex.execute(update);
		
		//List<SpatialDatabaseRecord> results = update.getResults();
		
		//printTestResults(results);

		//assertEquals(2, results.size());
		deleteDatabase(true);
		
	}
	
	public void printTestResults(List<SpatialDatabaseRecordImpl> results) {
		for (SpatialDatabaseRecordImpl spatialDatabaseRecord : results) {
			System.out.println("ID: " + spatialDatabaseRecord.getId() + ";"
					+ "Geometry:" + spatialDatabaseRecord.getGeometry());
		}
	}

}