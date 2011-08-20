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

import java.util.ArrayList;
import java.util.List;

import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.gis.spatial.operation.restriction.RestrictionMap;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>GeoprocessingRunnable</code> is a class to provide a
 * 
 * @author Andreas Wilhelm
 * 
 */
public class GeoprocessingRunnable implements Runnable {

	private Node node;
	private List<SpatialDatabaseRecord> records;
	private SpatialQuery query;
	private Layer layer;
	private RestrictionMap reMap;

	/**
	 * The
	 * 
	 * @param layer
	 *            the
	 * @param node
	 * @param results
	 * @param query
	 * @param restrictionMap
	 */
	public GeoprocessingRunnable(Layer layer, Node node,
			List<SpatialDatabaseRecord> results, SpatialQuery query,
			RestrictionMap restrictionMap) {
		this.node = node;
		this.records = results;
		this.query = query;
		this.layer = layer;
		this.reMap = restrictionMap;
	}
	
	
	public GeoprocessingRunnable(Layer layer, List<Node> node, SpatialQuery query) {
		
	}
	
	
	

	public void run() {

		determineNode(this.node);

	}

	/**
	 * Determine if node is a leaf which mean it has a R_TREE_REFERENCE OUTGOING
	 * relation. If it is a leaf the geoprocessing function will be execute and
	 * the result will be added.
	 * 
	 * @param node
	 *            the node to determine if it is a leaf or not.
	 */
	private void determineNode(Node node) {

		if (node.hasRelationship(SpatialRelationshipTypes.RTREE_CHILD,
				Direction.OUTGOING)) {
			// Node is not a leaf

			// collect children
			List<Node> children = new ArrayList<Node>();
			for (Relationship rel : node.getRelationships(
					SpatialRelationshipTypes.RTREE_CHILD, Direction.OUTGOING)) {
				children.add(rel.getEndNode());
			}

			// Call method again to visit the children to finally get the node
			// which represent the geometry on the graph.
			for (Node child : children) {
				determineNode(child);
			}

		} else if (node.hasRelationship(
				SpatialRelationshipTypes.RTREE_REFERENCE, Direction.OUTGOING)) {
			
			for (Relationship rel : node.getRelationships(
					SpatialRelationshipTypes.RTREE_REFERENCE,
					Direction.OUTGOING)) {
				geoprocessing(rel.getEndNode());
			}

		} else {
			// TODO: Logging
		}
	}

	/**
	 * Call the spatial type function to execute the geoprocessing.
	 * 
	 * @param geomNode
	 *            the node which represent the {@link Geometry} in the graph.
	 */
	private void geoprocessing(Node geomNode) {
		if (reMap.determineNode(node)) {
			query.onIndexReference(null, geomNode, layer, records);
		}
	}

}
