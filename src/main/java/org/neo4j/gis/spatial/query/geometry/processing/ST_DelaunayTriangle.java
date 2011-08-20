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
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.triangulate.DelaunayTriangulationBuilder;

/**
 * <p>
 * The <code>ST_DelaunayTriangle</code> class returns the computed Delaunay triangulation as a polygon Geometry. 
 * </p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_DelaunayTriangle extends AbstractReadOperation {

	private double tolerance = 0.0d;

	/**
	 * DelaunayTriangle with no tolerance.
	 */
	public ST_DelaunayTriangle() {
	}

	/**
	 * 
	 * @param tolerance
	 *            the snapping tolerance which will be used to improved the
	 *            robustness of the triangulation computation.
	 */
	public ST_DelaunayTriangle(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {

		Geometry geometry = decodeGeometry(node);
		GeometryFactory geometryFactory = new GeometryFactory();
		DelaunayTriangulationBuilder builder = new DelaunayTriangulationBuilder();
		builder.setSites(geometry);
		builder.setTolerance(this.tolerance);
		Geometry trianglesGeometry = builder.getTriangles(geometryFactory);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, trianglesGeometry);
		record.setResult(trianglesGeometry);
		records.add(record);
		return record;
	}

}