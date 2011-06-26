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

import org.neo4j.gis.spatial.operation.Delete;
import org.neo4j.gis.spatial.operation.Insert;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.NodeProperty;
import org.neo4j.gis.spatial.operation.NodeRelation;
import org.neo4j.gis.spatial.operation.Update;
import org.neo4j.gis.spatial.operation.restriction.RestrictionMap;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser;

import org.neo4j.graphdb.Traverser.Order;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>EditableLayerImpl</code> is the concrete implementation of the
 * {@link EditableLayer} interface.
 * 
 * @author Craig Taverner, Andreas Wilhelm
 * 
 */
public class EditableLayerImpl extends DefaultLayer implements EditableLayer {

	@Deprecated
	private Node previousGeomNode;

	/**
	 * Add a geometry to this layer.
	 */
	@Deprecated
	public SpatialDatabaseRecordImpl add(Geometry geometry) {
		return add(geometry, null, null);
	}

	/**
	 * Add a geometry to this layer, including properties.
	 * 
	 */
	@Deprecated
	public SpatialDatabaseRecordImpl add(Geometry geometry,
			String[] fieldsName, Object[] fields) {
		Transaction tx = getDatabase().beginTx();
		try {
			Node geomNode = addGeomNode(geometry, fieldsName, fields);
			index.add(geomNode);
			tx.success();
			return new SpatialDatabaseRecordImpl(this, geomNode, geometry);
		} finally {
			tx.finish();
		}
	}

	/**
	 * Should be overwride by a conrecte layer.
	 */
	public void update(long geomNodeId, Geometry geometry) {
		Transaction tx = this.getDatabase().beginTx();
		try {
			Node geomNode = this.getDatabase().getNodeById(geomNodeId);
			this.getGeometryEncoder().encodeGeometry(geometry, geomNode);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	/**
	 * Should be overwride by a conrecte layer.
	 */
	public void delete(long geomNodeId) {
		Transaction tx = getDatabase().beginTx();
		try {
			index.remove(geomNodeId, true);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	@Deprecated
	private Node addGeomNode(Geometry geom, String[] fieldsName, Object[] fields) {
		Node geomNode = getDatabase().createNode();
		if (previousGeomNode == null) {
			for (Node node : layerNode.traverse(Order.DEPTH_FIRST,
					StopEvaluator.END_OF_GRAPH,
					ReturnableEvaluator.ALL_BUT_START_NODE,
					SpatialRelationshipTypes.GEOMETRIES, Direction.OUTGOING,
					SpatialRelationshipTypes.NEXT_GEOM, Direction.OUTGOING)) {
				previousGeomNode = node;
			}
		}
		if (previousGeomNode != null) {
			previousGeomNode.createRelationshipTo(geomNode,
					SpatialRelationshipTypes.NEXT_GEOM);
		} else {
			layerNode.createRelationshipTo(geomNode,
					SpatialRelationshipTypes.GEOMETRIES);
		}
		previousGeomNode = geomNode;
		// other properties
		if (fieldsName != null) {
			for (int i = 0; i < fieldsName.length; i++) {
				geomNode.setProperty(fieldsName[i], fields[i]);
			}
		}
		getGeometryEncoder().encodeGeometry(geom, geomNode);

		return geomNode;
	}

	/**
	 * @see EditableLayer#execute(Insert)
	 */
	public List<SpatialDatabaseRecord> execute(Insert insert)
			throws SpatialDatabaseException {

		// Container for the inserted records.
		List<SpatialDatabaseRecord> records = new ArrayList<SpatialDatabaseRecord>();

		// TODO: replace this
		List<Geometry> geometries = insert.getGeometries();

		Transaction tx = getDatabase().beginTx();
		try {
			// TODO: replace this
			for (Geometry geometry : geometries) {

				// Get properties and relationships.
				List<NodeProperty> properties = insert.getProperties();
				List<NodeRelation> relationships = insert.getRelationships();

				Node spatialNode = this.getDatabase().createNode();

				// Execute spatial type query.
				SpatialDatabaseRecord record = insert.onIndexReference(
						OperationType.INSERT, spatialNode, this);

				// TODO spatialtype props and realtions..
				// TODO mehrere geoms ST_Network(List<Geometry>);

				// Create relationships.
				for (NodeRelation relation : relationships) {
					record.getGeomNode().createRelationshipTo(
							relation.getNode(), relation.getRelationshipType());
				}

				// Add properties.
				for (NodeProperty property : properties) {
					record.getGeomNode().setProperty(property.getKey(),
							property.getValue());
				}

				// Add node to the layer.
				getGeometryEncoder().encodeGeometry(record.getGeometry(),
						record.getGeomNode());

				// Add node to the index.
				index.add(record.getGeomNode());

				// Add record to the return list.
				records.add(record);
			}
			tx.success();
		} catch (Exception e) {
			throw new SpatialDatabaseException("" + e.getMessage());
		} finally {
			tx.finish();
		}

		return records;
	}

	/**
	 * workaround...
	 * 
	 * @return
	 */
	private Traverser getGeomNodes() {
		List<Node> node = new ArrayList<Node>();

		Node indexNode = getLayerNode().getSingleRelationship(
				SpatialRelationshipTypes.RTREE_ROOT, Direction.OUTGOING)
				.getEndNode();

		return indexNode.traverse(Order.DEPTH_FIRST,
				StopEvaluator.END_OF_GRAPH,
				ReturnableEvaluator.ALL_BUT_START_NODE, 
				SpatialRelationshipTypes.RTREE_REFERENCE, Direction.OUTGOING);
	}

	/**
	 * @see EditableLayer#execute(Delete)
	 */
	public int execute(Delete delete) throws SpatialDatabaseException {
		int count = 0;
		
		//
		RestrictionMap restrictions = delete.getRestrictions();

		// Iterable<Node> nodes = getIndex().getAllGeometryNodes();
		Transaction tx = getDatabase().beginTx();
		try {

			for (Node node : getGeomNodes()) {
				if (restrictions.determineNode(node)) {
					SpatialDatabaseRecord record = delete.onIndexReference(
							OperationType.DELETE, node, this);
					if (record != null) {
						index.remove(record.getId(), true);
						count++;
					}
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}
		return count;
	}

	/**
	 * @see EditableLayer#execute(Update)
	 */
	public List<SpatialDatabaseRecord> execute(Update update)
			throws SpatialDatabaseException {
		RestrictionMap restrictions = update.getRestrictions();
		List<SpatialDatabaseRecord> records = new ArrayList<SpatialDatabaseRecord>();
		Iterable<Node> nodes = getGeomNodes();

		Transaction tx = this.getDatabase().beginTx();
		try {

			for (Node node : nodes) {

				if (!restrictions.determineNode(node)) {
					SpatialDatabaseRecord record = update.onIndexReference(
							OperationType.UPDATE, node, this);
					if (record != null) {

						// Update node and subgraph.
						this.update(record.getGeomNode().getId(), record.getGeometry());
						// Add updated node to record list.
						records.add(record);
					}
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}

		return records;
	}

}
