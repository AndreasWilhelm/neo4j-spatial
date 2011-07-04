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
package org.neo4j.gis.spatial.geometry.editors;

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
		reActivateDatabase(false, false, false);
		OSMImporter importer = new OSMImporter(layerName);
		importer.importFile(graphDb(), osmPath);
		reActivateDatabase(false, false, false);
		importer.reIndex(graphDb(), commitInterval);
	}

	public void testDelete() {

		Transaction tx = spatialService.getDatabase().beginTx();
		try {

//			Node startNode = spatialService.getDatabase().getNodeById(39l);
            Node startNode = spatialService.getDatabase().getNodeById(18l);

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
					System.out.println(node + " has no relations!");
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
