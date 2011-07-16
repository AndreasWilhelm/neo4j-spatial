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

import org.geotools.referencing.GeodeticCalculator;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;


public class ST_LengthInMeters extends AbstractReadOperation {
	

	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = decodeGeometry(node);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(
				layer, node);
		
		GeodeticCalculator geodeticCalculator = new GeodeticCalculator(layer.getCoordinateReferenceSystem());
		Coordinate[] coords = geom.getCoordinates();
		
		double totalLength = 0;
		
		for (int i = 0; i < (coords.length - 1); i++) {
			Coordinate c1 = coords[i];
			Coordinate c2 = coords[i+1];
			geodeticCalculator.setStartingGeographicPoint(c1.x, c1.y);
			geodeticCalculator.setDestinationGeographicPoint(c2.x, c2.y);
			totalLength += geodeticCalculator.getOrthodromicDistance();
		}

		record.setResult(totalLength);
		records.add(record);
		return record;
	}
}
