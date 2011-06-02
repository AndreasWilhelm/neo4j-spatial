package org.neo4j.gis.spatial.geomety.editors;

import java.io.File;
import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.SpatialIndexReader;
import org.neo4j.gis.spatial.Update;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Transform;


/**
 * 
 * @author Andreas Wilhelm
 *
 */
public class TestUpdate extends Neo4jTestCase {

	private SpatialDatabaseService spatialService;
	
	private void loadTestOsmData(String layerName, int commitInterval)
			throws Exception {
		
		String osmPath = Default.OSM_DIR + File.separator + layerName;
		System.out.println("\n=== Loading layer " + layerName + " from "
				+ osmPath + " ===");
		reActivateDatabase(false, true, false);
		OSMImporter importer = new OSMImporter(layerName);
		importer.importFile(getBatchInserter(), osmPath);
		reActivateDatabase(false, false, false);
		importer.reIndex(graphDb(), commitInterval);
	}

	public void testTransform() throws Exception {

		loadTestOsmData(Default.LAYER_NAME, Default.COMMIT_INTERVAL);
		spatialService = new SpatialDatabaseService(graphDb());
		Layer layer = spatialService.getLayer(Default.LAYER_NAME);
		SpatialIndexReader spatialIndex = layer.getIndex();
		
		Update update = new ST_Transform(Default.WORLD_MERCATOR_SRID);
		spatialIndex.execute(update);
		
		List<SpatialDatabaseRecord> results = update.getResults();
		
		printTestResults(results);

		assertEquals(2, results.size());
		deleteDatabase(true);
		
	}
	
	public void printTestResults(List<SpatialDatabaseRecord> results) {
		for (SpatialDatabaseRecord spatialDatabaseRecord : results) {
			System.out.println("ID: " + spatialDatabaseRecord.getId() + ";"
					+ "Geometry:" + spatialDatabaseRecord.getGeometry());
		}
	}

}