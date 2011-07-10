package org.neo4j.gis.spatial.query.geometry.outputs;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

public class ST_Buffer extends AbstractReadOperation {

	private float bufferDistance;
	private int quadrantSegments;
	private int endCapStyle;

	/**
	 * 
	 * @param bufferDistance
	 */
	public ST_Buffer(float bufferDistance) {
		this(bufferDistance, -1, -1);
	}

	/**
	 * 
	 * @param bufferDistance
	 * @param quadrantSegments
	 */
	public ST_Buffer(float bufferDistance, int quadrantSegments) {
		this(bufferDistance, quadrantSegments, -1);
	}

	/**
	 * 
	 * @param bufferDistance
	 * @param quadrantSegments
	 * @param endCapStyle
	 */
	public ST_Buffer(float bufferDistance, int quadrantSegments, int endCapStyle) {
		this.bufferDistance = bufferDistance;
		this.quadrantSegments = quadrantSegments;
		this.endCapStyle = endCapStyle;
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {

		Geometry bufferGeom = null;
		if (bufferDistance != -1 && quadrantSegments != -1 && endCapStyle != -1) {
			bufferGeom = decodeGeometry(node).buffer(bufferDistance,
					quadrantSegments, endCapStyle);
		} else if (bufferDistance != -1 && quadrantSegments != -1) {
			bufferGeom = decodeGeometry(node).buffer(bufferDistance,
					quadrantSegments);
		} else {
			bufferGeom = decodeGeometry(node).buffer(bufferDistance);
		}

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(
				layer, node);

		record.setResult(bufferGeom);
		records.add(record);
		return record;
	}

}
