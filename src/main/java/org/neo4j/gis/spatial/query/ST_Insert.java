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

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_Insert</code> is the base class for spatial type insert
 * operations, to add a new spatial {@link Node} to the layer. It has has a bunch
 * of overloaded constructors which over several ways to insert new geometry
 * nodes.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Insert extends AbstractFullOperation {

	private int index = 0;

	/**
	 * Empty constructor.
	 */
	public ST_Insert() {
	}

	/**
	 * Add a new Node with the provided geometry.
	 * 
	 * @param geometry
	 *            The geometry of the new Node.
	 */
	public ST_Insert(Geometry geometry) {
		this.add(geometry);
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
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, this.getGeometries().get(this.index++));
		records.add(record);
		return record;
	}

}
