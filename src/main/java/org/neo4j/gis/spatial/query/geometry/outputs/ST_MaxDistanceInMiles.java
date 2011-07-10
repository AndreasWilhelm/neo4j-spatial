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
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_MaxDistanceInMeter</code> function returns the maximal distance in
 * miles. The Spatial Reference System Identifier(SRID) of the other geometry
 * must be the same as the SRID of layer geometry, else a
 * SpatialDatabaseException will be thrown.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_MaxDistanceInMiles extends ST_MaxDistanceInMeter {

	/**
	 * 
	 * @param other
	 * @throws FactoryException 
	 * @throws NoSuchAuthorityCodeException 
	 */
	public ST_MaxDistanceInMiles(Geometry other) throws NoSuchAuthorityCodeException, FactoryException {
		super(other);
	}


	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {

		SpatialDatabaseRecord record = super.onIndexReference(type, node,
				layer, records);

		double miles = (Double) record.getResult() * 0.000621371;
		record.setResult(miles);
		return record;

	}
	
}
