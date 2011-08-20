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
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_MaxDistance;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * The <code>ST_LongestLine</code> class returns a {@link LineString} with
 * points of the given geometry and this geometry, that have the longest distance
 * to between it.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_LongestLine extends ST_MaxDistance {

	private Geometry other = null;

	/**
	 * 
	 * @param other
	 * @throws FactoryException
	 * @throws NoSuchAuthorityCodeException
	 */
	public ST_LongestLine(Geometry other) throws NoSuchAuthorityCodeException,
			FactoryException {
		super(other);
		this.other = other;
	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		super.clear();

		Geometry geometry = decodeGeometry(node);

		GeometryFactory geometryFactory = new GeometryFactory();
		Coordinate[] farthestPoints = super.getFarthestPoints(geometry,
				this.other);
		LineString lineString = geometryFactory
				.createLineString(farthestPoints);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, geometry);
		record.setResult(lineString);
		records.add(record);
		return record;

	}

}