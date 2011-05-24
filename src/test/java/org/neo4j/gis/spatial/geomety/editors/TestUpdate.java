package org.neo4j.gis.spatial.geomety.editors;

import java.io.File;
import java.util.List;

import org.neo4j.gis.spatial.EditableLayer;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.Search;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.SpatialIndexReader;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Transform;
import org.neo4j.graphdb.Node;


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
		
		List<SpatialDatabaseRecord> results = null;
		Search searchQuery = new ST_Transform(Default.WORLD_MERCATOR_SRID);
		spatialIndex.executeSearch(searchQuery);
		results = searchQuery.getResults();
		update(results);
		System.out.println("Search results: " + results.size());
		printTestResults(results);
		assertEquals(2, results.size());
		deleteDatabase(true);
		
	}
	
	

	
	private void update(List<SpatialDatabaseRecord> results) {
		
		
		
		//TODO: It would be nice when editlayer would offer update method for List<SpatialDatabaseRecord>
		EditableLayer editlayer = (EditableLayer) spatialService.getLayer(Default.LAYER_NAME);
		
		
		SpatialIndexReader spatialIndex = editlayer.getIndex();
		SpatialDatabaseRecord oldnode = spatialIndex.get(55L);
		System.out.println("OldGeomValue: " +oldnode.getGeometry());
		
		for (SpatialDatabaseRecord spatialDatabaseRecord : results) {
			//TODO:  Failed to decode OSM geometry: More than one relationship[GEOM, INCOMING] found for NodeImpl#55
			editlayer.update(spatialDatabaseRecord.getId(), spatialDatabaseRecord.getGeometry());
		}
		
		System.out.println("Layer updated!");
		SpatialDatabaseRecord node = spatialIndex.get(55L);
		System.out.println("Updated: " +node.getGeometry());
	}

	public void printTestResults(List<SpatialDatabaseRecord> results) {
		for (SpatialDatabaseRecord spatialDatabaseRecord : results) {
			System.out.println("ID: " + spatialDatabaseRecord.getId() + ";"
					+ "Geometry:" + spatialDatabaseRecord.getGeometry());
		}
	}

}