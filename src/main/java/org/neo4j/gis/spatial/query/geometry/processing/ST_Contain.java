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
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_Contain</code> class returns geometries that contain the given
 * {@link Geometry}.
 * 
 * @author Davide Savazzi, Andreas Wilhelm
 */
public class ST_Contain extends AbstractReadOperation {

	// The geometry to determine if it is in this geometry.
	private Geometry other = null;

	/**
	 * Construct a Geometry to determine if it is in this geometry.
	 * 
	 * @param other
	 *            the geometry to determine if it is in this geometry.
	 */
	public ST_Contain(Geometry other) {
		this.other = other;
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {

		Geometry geom = this.decodeGeometry(node);
		if (geom.contains(this.other)) {
			SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
					node);
			record.setResult(geom);
			records.add(record);
			return record;
		} else {
			return null;
		}
	}

}
