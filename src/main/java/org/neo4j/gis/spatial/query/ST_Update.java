package org.neo4j.gis.spatial.query;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractUpdateOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The simplest spatial type operation to update a single node {@link Node} of
 * the layer.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Update extends AbstractUpdateOperation {

	private Geometry geometry;

	/**
	 * Update a single node.
	 * 
	 * @param node
	 *            The node to update.
	 * @param geometry
	 *            The new geometry of the node.
	 */
	public ST_Update(Node node, Geometry geometry) {
		this(node.getId(), geometry);
	}

	/**
	 * Update a single node.
	 * 
	 * @param nodeId
	 *            The node id to update.
	 * @param geometry
	 *            The new geometry of the node.
	 */
	public ST_Update(long nodeId, Geometry geometry) {
		this.geometry = geometry;
		this.addRestriction(RestrictionType.EQUAL_TO, "id=" + nodeId);
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(org.neo4j.gis.spatial.operation.OperationType, Node, Layer)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type, Node node,
			Layer layer) {
		return new SpatialDatabaseRecordImpl(layer, node, this.geometry);
	}

}