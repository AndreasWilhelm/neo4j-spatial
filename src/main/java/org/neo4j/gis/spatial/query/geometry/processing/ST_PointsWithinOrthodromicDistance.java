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

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * This search assume Layer contains Points with Latitude / Longitude
 * coordinates in degrees. Algorithm reference:
 * http://www.movable-type.co.uk/scripts/latlong-db.html
 * 
 * @author Davide Savazzi, Andreas Wilhelm
 */
public class ST_PointsWithinOrthodromicDistance extends AbstractReadOperation {
	//TODO some srid has different earthRadiusInKm
	private static final double earthRadiusInKm = 6371;
	
	private Coordinate reference;
	private double maxDistanceInKm;
	private Envelope bbox;

	public ST_PointsWithinOrthodromicDistance(Coordinate reference, double maxDistanceInKm) {
		this.reference = reference;
		this.maxDistanceInKm = maxDistanceInKm;

		double lat = reference.y;
		double lon = reference.x;

		// first-cut bounding box (in degrees)
		double maxLat = lat + Math.toDegrees(maxDistanceInKm / earthRadiusInKm);
		double minLat = lat - Math.toDegrees(maxDistanceInKm / earthRadiusInKm);
		// compensate for degrees longitude getting smaller with increasing
		// latitude
		double maxLon = lon + Math.toDegrees(maxDistanceInKm / earthRadiusInKm / Math.cos(Math.toRadians(lat)));
		double minLon = lon - Math.toDegrees(maxDistanceInKm / earthRadiusInKm / Math.cos(Math.toRadians(lat)));
		this.bbox = new Envelope(minLon, maxLon, minLat, maxLat);
	}

	public ST_PointsWithinOrthodromicDistance(Coordinate reference, Envelope bbox) {
		this.reference = reference;
		this.bbox = bbox;
		this.maxDistanceInKm = calculateDistance(bbox.centre(), new Coordinate(bbox.getMinX(),
				(bbox.getMinY() + bbox.getMaxY()) / 2));
	}

	public static double calculateDistance(Coordinate reference, Coordinate point) {
		double distanceInKm = Math.acos(Math.sin(Math.toRadians(reference.y)) * Math.sin(Math.toRadians(point.y))
				+ Math.cos(Math.toRadians(reference.y)) * Math.cos(Math.toRadians(point.y))
				* Math.cos(Math.toRadians(point.x) - Math.toRadians(reference.x)))
				* earthRadiusInKm;
		return distanceInKm;
	}



	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		Geometry geometry = decodeGeometry(node);
		Coordinate point = geometry.getCoordinate();

		// d = acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 -
		// lon1)) * R
		double distanceInKm = calculateDistance(reference, point);

		if (distanceInKm < maxDistanceInKm) {
			SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(layer, node);
			databaseRecord.setProperty(ST_PointsWithinOrthodromicDistance.class.getName(), distanceInKm);
			return databaseRecord;
		}
		return null;
	}
}