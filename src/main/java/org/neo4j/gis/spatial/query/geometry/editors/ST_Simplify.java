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
package org.neo4j.gis.spatial.query.geometry.editors;

import java.util.List;

import org.geotools.geometry.jts.Geometries;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;
import com.vividsolutions.jts.simplify.DouglasPeuckerLineSimplifier;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * <p>
 * The <code>ST_Simplify</code> class returns the simplified {@link Geometry}.
 * Which simplified means to using the Douglas-Peucker algorithm to reducing the
 * number of coordinate points in the {@link Geometry}. It works only with
 * LINESTRING and MULTIPOINT Geometries.
 * </p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Simplify extends AbstractFullOperation {

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = this.decodeGeometry(node);
		Geometry simplifiedGeometry = simplify(geom);
		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(
				layer, node, simplifiedGeometry);
		databaseRecord.setResult(simplifiedGeometry);
		records.add(databaseRecord);
		return databaseRecord;
	}

	/**
	 * Using the Douglas-Peucker algorithm to reducing the number of coordinate
	 * points in the {@link Geometry}.
	 * 
	 * @return Returns the simplified {@link Geometry}.
	 */
	private Geometry simplify(Geometry geometry) {

		DouglasPeuckerLineSimplifier simplifier = new DouglasPeuckerLineSimplifier(
				geometry.getCoordinates());

		GeometryFactory geometryFactory = new GeometryFactory(
				geometry.getPrecisionModel(), geometry.getSRID());

		switch (Geometries.get(geometry)) {

		case POINT:
			throw new IllegalArgumentException("unsupported geometry type:"
					+ Geometries.POINT);
		case MULTIPOINT:
			return geometryFactory.createMultiPoint(simplifier.simplify());
		case LINESTRING:
			return geometryFactory.createLineString(simplifier.simplify());
		case MULTILINESTRING:

		case POLYGON:

		case MULTIPOLYGON:

		default:
			throw new IllegalArgumentException("unknown type:"
					+ geometry.getGeometryType());
		}
	}

}
