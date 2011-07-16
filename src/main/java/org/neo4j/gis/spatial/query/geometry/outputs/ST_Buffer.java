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
import com.vividsolutions.jts.operation.buffer.BufferOp;

/**
 * The <code>ST_Buffer</code> class computes a buffer area around the given
 * {@link Geometry}. This operation will always return a polygonal geometry.
 * 
 * Following end cap styles are supported:
 * <ul
 * <li>{@link BufferOp#CAP_ROUND} - the usual round end caps
 * <li>{@link BufferOp#CAP_BUTT} - end caps are truncated flat at the line ends
 * <li>{@link BufferOp#CAP_SQUARE} - end caps are squared off at the buffer distance beyond the line ends
 * </ul>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Buffer extends AbstractReadOperation {

	// The width of the buffer.
	private float bufferDistance;
	// The number of line segments used to represent a quadrant of a circle.
	private int quadrantSegments;
	// The cap styles.
	private int endCapStyle;

	/**
	 * Construct a new polygonal geometry with a buffer area around the geometry
	 * from the layer with a given buffer distance.
	 * 
	 * @param bufferDistance
	 *            the width of the buffer could be positive, negative or 0.
	 */
	public ST_Buffer(float bufferDistance) {
		this(bufferDistance, -1, -1);
	}

	/**
	 * Construct a new polygonal geometry with a buffer area around the geometry
	 * from the layer with a given buffer width and the number of line segments
	 * used to represent a quadrant of a circle.
	 * 
	 * @param bufferDistance
	 *            the width of the buffer could be positive, negative or 0.
	 * @param quadrantSegments
	 *            the number of line segments used to represent a quadrant of a
	 *            circle.
	 */
	public ST_Buffer(float bufferDistance, int quadrantSegments) {
		this(bufferDistance, quadrantSegments, -1);
	}

	/**
	 * Construct a new polygonal geometry with a buffer area around the geometry
	 * from the layer with a given buffer width, the number of line segments
	 * used to represent a quadrant of a circle and the end cap style.
	 * 
	 * @param bufferDistance
	 *            the width of the buffer could be positive, negative or 0.
	 * @param quadrantSegments
	 *            the number of line segments used to represent a quadrant of a
	 *            circle.
	 * @param endCapStyle
	 *            the end cap style specifies the buffer geometry that will be
	 *            created at the ends of linestrings.
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

		Geometry bufferGeometry = null;
		if (bufferDistance != -1 && quadrantSegments != -1 && endCapStyle != -1) {
			bufferGeometry = decodeGeometry(node).buffer(bufferDistance,
					quadrantSegments, endCapStyle);
		} else if (bufferDistance != -1 && quadrantSegments != -1) {
			bufferGeometry = decodeGeometry(node).buffer(bufferDistance,
					quadrantSegments);
		} else {
			bufferGeometry = decodeGeometry(node).buffer(bufferDistance);
		}

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node);
		record.setResult(bufferGeometry);
		records.add(record);
		return record;
	}

}
