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

import org.neo4j.gis.spatial.operation.Delete;
import org.neo4j.gis.spatial.operation.Insert;
import org.neo4j.gis.spatial.operation.Update;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Traverser.Order;

import com.vividsolutions.jts.geom.Geometry;

public class EditableLayerImpl extends DefaultLayer implements EditableLayer {
	private Node previousGeomNode;

	/**
	 * Add a geometry to this layer.
	 */
	public SpatialDatabaseRecordImpl add(Geometry geometry) {
		return add(geometry, null, null);
	}

	/**
	 * Add a geometry to this layer, including properties.
	 */
	public SpatialDatabaseRecordImpl add(Geometry geometry, String[] fieldsName, Object[] fields) {
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

	public void delete(long geomNodeId) {
		Transaction tx = getDatabase().beginTx();
		try {
			index.remove(geomNodeId, true);
			tx.success();
		} finally {
			tx.finish();
		}
	}

	private Node addGeomNode(Geometry geom, String[] fieldsName, Object[] fields) {
		Node geomNode = getDatabase().createNode();
		if (previousGeomNode == null) {
			for (Node node : layerNode.traverse(Order.DEPTH_FIRST, StopEvaluator.END_OF_GRAPH,
					ReturnableEvaluator.ALL_BUT_START_NODE, SpatialRelationshipTypes.GEOMETRIES, Direction.OUTGOING,
					SpatialRelationshipTypes.NEXT_GEOM, Direction.OUTGOING)) {
				previousGeomNode = node;
			}
		}
		if (previousGeomNode != null) {
			previousGeomNode.createRelationshipTo(geomNode, SpatialRelationshipTypes.NEXT_GEOM);
		} else {
			layerNode.createRelationshipTo(geomNode, SpatialRelationshipTypes.GEOMETRIES);
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
	public int execute(Insert insert) {
		return index.execute(insert);
	}

	/**
	 * @see EditableLayer#execute(Delete)
	 */
	public int execute(Delete delete) {
		return index.execute(delete);
	}

	/**
	 * @see EditableLayer#execute(Update)
	 */
	public int execute(Update update) {
		return index.execute(update);
	}

}
