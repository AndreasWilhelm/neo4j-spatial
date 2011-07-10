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

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseException;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_MaxDistanceInMeter</code> function returns the maximal distance
 * in meter. The Spatial Reference System Identifier(SRID) of the other geometry
 * must be the same as the SRID of layer geometry, else a
 * SpatialDatabaseException will be thrown.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_MaxDistanceInMeter extends ST_MaxDistance {

	private Geometry other = null;
	private CoordinateReferenceSystem crs = null;

	/**
	 * 
	 * @param other
	 * @throws FactoryException
	 * @throws NoSuchAuthorityCodeException
	 */
	public ST_MaxDistanceInMeter(Geometry other)
			throws NoSuchAuthorityCodeException, FactoryException {
		super(other);
		this.other = other;
		this.crs = CRS.decode("EPSG:" + other.getSRID());
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		super.clear();
		
		Geometry geometry = decodeGeometry(node);
		double distanceInMeter = 0;
		Coordinate[] farthestPoints = super.getFarthestPoints(geometry, this.other);
	
		if (farthestPoints != null) {
			try {
				distanceInMeter = JTS.orthodromicDistance(farthestPoints[0],
						farthestPoints[1], this.crs);
			} catch (TransformException e) {
				throw new SpatialDatabaseException(e.getMessage());
			}
		}

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, geometry);
		record.setResult(distanceInMeter);
		records.add(record);
		return record;

	}
}
