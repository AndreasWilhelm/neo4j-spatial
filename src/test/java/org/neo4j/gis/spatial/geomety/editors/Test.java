package org.neo4j.gis.spatial.geomety.editors;

import java.io.File;

import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.osm.OSMRelation;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;

public class Test extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;

	protected void setUp(boolean deleteDb, boolean useBatchInserter,
			boolean autoTx) throws Exception {
		super.setUp(false, true, false);
		try {
			this.loadTestOsmData(Dataset.LAYER_NAME, Dataset.COMMIT_INTERVAL);
			this.spatialService = new SpatialDatabaseService(graphDb());
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public void testDelete() {

		Transaction tx = spatialService.getDatabase().beginTx();
		try {

			Node startNode = spatialService.getDatabase().getNodeById(39l);

			// Get all coordinate nodes and proxy nodes for them.
			Traverser traverser = startNode.traverse(Order.BREADTH_FIRST,
					StopEvaluator.END_OF_GRAPH,
					ReturnableEvaluator.ALL_BUT_START_NODE, OSMRelation.NODE,
					Direction.OUTGOING, OSMRelation.NEXT, Direction.OUTGOING);

			for (Node node : traverser.getAllNodes()) {
				System.out.println("------------------------------");
				// Delete relationship of the subnode.
				for (Relationship rel : node.getRelationships()) {
					rel.delete();
					System.out.println("Deleted rel: " + rel.getId());
				}

				// Delete subnode.
				if (!node.hasRelationship()) {
					System.out.println("Node has no relations!");
					node.delete();
					System.out.println("Deleted Node: " + node);
				} else {
					System.out.println("node hasRelationship");
				}

			}

			tx.success();
		} finally {
			tx.finish();
		}

	}
}
