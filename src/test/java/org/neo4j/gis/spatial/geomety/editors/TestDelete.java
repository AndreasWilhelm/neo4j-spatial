package org.neo4j.gis.spatial.geomety.editors;

import java.io.File;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.operation.Delete;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.query.ST_Delete;

public class TestDelete extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private Layer layer = null;

	protected void setUp(boolean deleteDb, boolean useBatchInserter,
			boolean autoTx) throws Exception {
		super.setUp(false, true, false);
		try {
			this.loadTestOsmData(Dataset.LAYER_NAME, Dataset.COMMIT_INTERVAL);
			this.spatialService = new SpatialDatabaseService(graphDb());
			this.layer = spatialService.getLayer(Dataset.LAYER_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void testDelete() throws Exception {
		System.out.println(layer.getIndex().get(55l));
		Delete delete = new ST_Delete();
		System.out.println("Still exist: " + layer.getIndex().get(55l));
		int count = layer.execute(delete);
		assertEquals(2, count);
	}
	
	
	public void testDeleteWithKeyRestriction() throws Exception {
		Delete delete = new ST_Delete();
		delete.addRestriction(RestrictionType.HAS_PROPERTY, "vertices");
		int count = layer.execute(delete);
		assertEquals(0, count);
	}

	public void testDeleteWithKeyValueRestriction() throws Exception {
		Delete delete = new ST_Delete();
		delete.addRestriction(RestrictionType.HAS_NOT_PROPERTY, "vertices=8");
		int count = layer.execute(delete);
		assertEquals(1, count);
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
