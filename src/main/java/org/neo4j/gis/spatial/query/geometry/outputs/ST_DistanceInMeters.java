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

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseException;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.operation.distance.DistanceOp;

/**
 * The <code>ST_DistanceInMeter</code> class returns the minimum distance in
 * meters. The Spatial Reference System Identifier(SRID) of the other geometry
 * must be the same as the SRID of layer geometry, else a
 * SpatialDatabaseException will be thrown.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_DistanceInMeters extends AbstractReadOperation {

	// The geometry from which to calculate the distance.
	private Geometry other;
	// The CoordinateReferenceSystem from the given geometry.
	private CoordinateReferenceSystem crs = null;

	/**
	 * Calculate the minimum distance between the given geometry and the
	 * geometry from the layer query.
	 * 
	 * @param other
	 *            the geometry from which to calculate the distance.
	 * @throws FactoryException
	 *             if the creation of the CoordinateReferenceSystem failed for
	 *             an other reason.
	 * @throws NoSuchAuthorityCodeException
	 *             if the SRID of the given geometry could not be found.
	 * 
	 * 
	 */
	public ST_DistanceInMeters(Geometry other)
			throws NoSuchAuthorityCodeException, FactoryException {
		this.other = other;
		this.crs = CRS.decode("EPSG:" + other.getSRID());
	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = this.decodeGeometry(node);
		SpatialDatabaseRecord record = null;
		// TODO: Every impl. of the GeometryEncoder should be generated geometries with SRID.
		geom.setSRID(4326);
		if (geom.getSRID() == other.getSRID()) {

			DistanceOp distanceOp = new DistanceOp(geom, other);
			Coordinate[] coords = distanceOp.nearestPoints();

			Coordinate coord = coords[0];
			Coordinate otherCoord = coords[1];

			GeodeticCalculator gc = new GeodeticCalculator(this.crs);
			gc.setStartingGeographicPoint(coord.x, coord.y);
			gc.setDestinationGeographicPoint(otherCoord.x, otherCoord.y);
			double distanceInMeter = gc.getOrthodromicDistance();

			record = new SpatialDatabaseRecordImpl(layer, node);
			record.setResult(distanceInMeter);
			records.add(record);
			return record;

		} else {
			throw new SpatialDatabaseException(
					"The SRID of the two geometries to determine the distance are not the same.");
		}

	}

}
