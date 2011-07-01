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
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * 
 * @author Davide Savazzi, Andreas Wilhelm
 */
public class ST_IntersectWindow extends AbstractReadOperation {

	private Envelope envelope;
	private Geometry windowGeom;

	public ST_IntersectWindow(Envelope envelope) {
		this.envelope = envelope;
	}

	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		//TODO: create the geom just one time...
		this.windowGeom = layer.getGeometryFactory().toGeometry(envelope);
		Envelope geomEnvelope = getEnvelope(node);
		
		if (envelope.covers(geomEnvelope)) {
			return new SpatialDatabaseRecordImpl(layer, node);
		} else if (envelope.intersects(geomEnvelope)) {
			Geometry geometry = decodeGeometry(node);
			if (geometry.intersects(windowGeom)) {
				return new SpatialDatabaseRecordImpl(layer, node);
			}
		}
		return null;
	}	

}