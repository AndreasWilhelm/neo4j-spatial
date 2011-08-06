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
package org.neo4j.gis.spatial.query.geometry.accessors;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * <p>
 * The <code>ST_InteriorRingN</code> class returns the interior
 * {@link LineString} ring on the provided index of a polygonal Geometry or
 * null.
 * </p>
 * 
 * <h3>For example:</h3>
 * 
 * <code></code>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_InteriorRingN extends AbstractReadOperation {

	// The index value.
	int index = -1;

	/**
	 * Construct a LineString with the provided index value for a interior LineString ring
	 * of a polygonal Geometry.
	 * 
	 * @param index
	 *            the index value
	 */
	public ST_InteriorRingN(int index) {
		this.index = index;
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geometry = decodeGeometry(node);
		LineString lineString = null;

		if (geometry instanceof Polygon) {
			Polygon polygon = (Polygon) geometry;
			polygon.getInteriorRingN(index);
		}
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node);
		record.setResult(lineString);
		records.add(record);
		return record;
	}

}