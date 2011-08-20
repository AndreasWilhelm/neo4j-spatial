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
package org.neo4j.gis.spatial.query.geometry.processing;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * <code>ST_Closest</code> class returns the closest {@link Geometry} to the
 * given geometry. If another geometry has the same distance as the closest
 * Geometry then both will be returned.
 * </p>
 * 
 * @author Davide Savazzi, Andreas Wilhelm
 */
public class ST_Closest extends AbstractReadOperation {

	// The geometry to determine which one is close to this.
	private Geometry other;
	// The max extent of the geometry to determine which one is close to this.
	private Envelope envelope;
	// Contains the tmp. min. distance of the current closest Geometry.
	private double minDistance = Double.MAX_VALUE;

	/**
	 * Construct a {@link Geometry} to find the close geometry node.
	 * 
	 * @param other
	 *            the geometry to use for finding the closest geometry to it.
	 */
	public ST_Closest(Geometry other) {
		this(other, null);
	}

	/**
	 * Construct a {@link Geometry} to find the close geometry node.
	 * 
	 * @param other
	 *            the geometry to use for finding the closest geometry to it.
	 * @param envelope
	 *            the max extent of the other geometry.
	 */
	public ST_Closest(Geometry other, Envelope envelope) {
		this.other = other;
		if (envelope == null) {
			this.envelope = other.getEnvelopeInternal();
		} else {
			this.envelope = envelope;
		}

	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Envelope geomEnvelope = getEnvelope(node);
		SpatialDatabaseRecord record = null;
		if (geomEnvelope.intersects(this.envelope)) {
			Geometry geometry = decodeGeometry(node);
			double distance = geometry.distance(other);
			if (distance < minDistance) {
				record = new SpatialDatabaseRecordImpl(layer, node);
				record.setResult(geometry);
				minDistance = distance;
				// Remove old entries.
				records.clear();
				records.add(record);
			} else if (distance == minDistance) {
				record = new SpatialDatabaseRecordImpl(layer, node);
				record.setResult(geometry);
				records.add(record);
			}
		}
		return record;
	}

}