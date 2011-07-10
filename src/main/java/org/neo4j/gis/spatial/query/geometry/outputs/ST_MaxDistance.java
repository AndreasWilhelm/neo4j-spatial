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
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_MaxDistance</code> function returns the maximal distance
 * between the two geometries in the coordinate reference system.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_MaxDistance extends AbstractReadOperation {

	private Geometry other;
	private double maxDistance = 0;

	/**
	 * 
	 * @param other
	 */
	public ST_MaxDistance(Geometry other) {
		this.other = other;
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		clear();
		
		Geometry geometry = decodeGeometry(node);

		getFarthestPoints(geometry, this.other);
		
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, geometry);
		record.setResult(maxDistance);
		records.add(record);
		return record;
	}
	
	
	protected Coordinate[] getFarthestPoints(Geometry geom1, Geometry geom2) {
		
		Coordinate[] farthestPoints = new Coordinate[2];
		
		for (Coordinate coord : geom1.getCoordinates()) {
			double longitude = coord.x;
			double latitude = coord.y;
			double altitude = coord.z;
		
			for (Coordinate otherCoord : geom2.getCoordinates()) {
				
				double otherLongitude = otherCoord.x;
				double otherLatitude = otherCoord.y;
				double otherAltitude = otherCoord.z;
	
				double longitudeDistance = otherLongitude-longitude;
				double latitudeDistance = otherLatitude-latitude;
				Double altitudeDistance = otherAltitude-altitude;
				
				double distance = -1;
				
				if(altitudeDistance.isNaN()) {
					 distance = Math.sqrt(longitudeDistance*longitudeDistance +
								latitudeDistance*latitudeDistance);
				} else {
					 distance = Math.sqrt(longitudeDistance*longitudeDistance +
								latitudeDistance*latitudeDistance +
								altitudeDistance*altitudeDistance);
				}

				if(distance > maxDistance) {
					maxDistance = distance;
					farthestPoints[0] = new Coordinate(longitude, latitude, altitude);
					farthestPoints[1] =  new Coordinate(otherLongitude, otherLatitude, otherAltitude);
				}
			}
		
		}
		return farthestPoints;
	}
	
	
	protected void clear() {
		maxDistance = 0;
	}
}
