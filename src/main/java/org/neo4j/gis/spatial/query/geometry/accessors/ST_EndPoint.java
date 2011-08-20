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
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.gis.spatial.query.ST_Insert;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * <p>
 * The <code>ST_EndPoint</code> class returns the end {@link Point} of this Geometry.
 * </p>
 * 
 * <h3>For example:</h3>
 * <code>Input: LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531)</code>
 * <code>Output: POINT (12.9762034 56.0583531)</code>
 * 
 * @author Andreas Wilhelm
 *
 */
public class ST_EndPoint extends ST_Insert {

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geometry = this.decodeGeometry(node);

		Point point = getEndPoint(geometry);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(
				layer, node, point);
		record.setResult(point);
		records.add(record);
		return record;
	}

	private Point getEndPoint(Geometry geometry) {

		Coordinate[] coords = geometry.getCoordinates();
		GeometryFactory geometryFactory = new GeometryFactory(geometry
				.getPrecisionModel(), geometry.getSRID());
		Point endPoint = geometryFactory.createPoint(coords[coords.length - 1]);

		return endPoint;
	}

}
