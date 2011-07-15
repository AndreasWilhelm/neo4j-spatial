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
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.gis.spatial.query.ST_Insert;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Returns the start {@link Point} of a {@link Geometry}
 * 
 * @author Andreas Wilhelm
 *
 */
public class ST_StartPoint extends ST_Insert {

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geometry = this.decodeGeometry(node);

		Point point = getStartPoint(geometry);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(
				layer, node, point);
		record.setResult(point);
		records.add(record);
		return record;
	}

	private Point getStartPoint(Geometry geometry) {

		Coordinate[] coords = geometry.getCoordinates();
		GeometryFactory geometryFactory = new GeometryFactory(geometry
				.getPrecisionModel(), geometry.getSRID());
		Point startPoint = geometryFactory.createPoint(coords[0]);
		
		return startPoint;
	}

}
