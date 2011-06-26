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
package org.neo4j.gis.spatial.query;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The simplest spatial type operation to add a new spatial {@link Node} to the
 * layer.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Insert extends AbstractFullOperation {
	
	private int index = 0;

	/**
	 * Add a new Node with the provided geometry.
	 * 
	 * @param geometry
	 *            The geometry of the new Node.
	 */
	public ST_Insert(Geometry geometry) {
		List<Geometry> geometies = new ArrayList<Geometry>();
		geometies.add(geometry);
		this.setGeometries(geometies);
	}

	/**
	 * Add a list with new geometries.
	 * 
	 * @param geometries
	 */
	public ST_Insert(List<Geometry> geometries) {
		this.setGeometries(geometries);
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(org.neo4j.gis.spatial.operation.OperationType,
	 *      Node, Layer)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, this.getGeometries().get(this.index++));
		return record;
	}

}
