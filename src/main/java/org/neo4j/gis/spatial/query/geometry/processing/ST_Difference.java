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

import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * The <code>ST_Difference</code> class returns a the part of {@link Geometry}
 * that the node geometry does not intersect with the provided other geometry
 * <p>
 * 
 * @author Andreas Wilhelm
 */
public class ST_Difference extends AbstractReadOperation {

	// The geometry to determine the difference between it and the node
	// geometry.
	private Geometry other = null;

	/**
	 * Construct a {@link Geometry} to determine the difference between it and
	 * the node geometry
	 * 
	 * @param other
	 *            the {@link Geometry} to determine.
	 */
	public ST_Difference(Geometry other) {
		this.other = other;
	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {

		Geometry geometry = this.decodeGeometry(node);
		Geometry targetGeometry = geometry.difference(this.other);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, targetGeometry);
		record.setResult(targetGeometry);
		records.add(record);

		return record;
	}
}