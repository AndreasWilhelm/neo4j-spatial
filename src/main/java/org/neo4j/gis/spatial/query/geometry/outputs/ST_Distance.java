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
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.graphdb.Node;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_Distance</code> class returns the minimum distance between two
 * geometries in units of spatial reference system.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Distance extends AbstractReadOperation {

	// The geometry from which to calculate the distance.
	private Geometry other;

	/**
	 * Calculate the minimum distance between the given geometry and the
	 * geometry from the layer query.
	 * 
	 * @param other
	 *            the geometry from which to calculate the distance.
	 */
	public ST_Distance(Geometry other) {
		this.other = other;
	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = decodeGeometry(node);
		double distance = geom.distance(other);
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node);
		record.setResult(distance);
		records.add(record);
		return record;
	}

}
