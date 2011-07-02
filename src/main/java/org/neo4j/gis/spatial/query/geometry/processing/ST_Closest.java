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

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Search graph for the closest geometry to the given geometry.
 * 
 * @author Davide Savazzi, Andreas Wilhelm
 */
public class ST_Closest extends AbstractReadOperation {
	
	//
	private Geometry other;
	//
	private Envelope envelope;
	//
	private double minDistance = Double.MAX_VALUE;

	/**
	 * 
	 * @param other
	 */
	public ST_Closest(Geometry other) {
		this(other, null);
	}

	/**
	 * 
	 * @param other
	 * @param envelope
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
	 * @see SpatialTypeOperation#onIndexReference(org.neo4j.gis.spatial.operation.OperationType, Node, Layer)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		Envelope geomEnvelope = getEnvelope(node);
		if (geomEnvelope.intersects(this.envelope)) {
			Geometry geometry = decodeGeometry(node);
			double distance = geometry.distance(other);
			if (distance <= minDistance) {
				return new SpatialDatabaseRecordImpl(layer, node);
			}
		}
		return null;
	}

}