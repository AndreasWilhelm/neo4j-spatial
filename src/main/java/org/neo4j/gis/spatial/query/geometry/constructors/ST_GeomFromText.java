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
package org.neo4j.gis.spatial.query.geometry.constructors;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * The <code>ST_GeomFromText</code> creates a geometry node from a Well-known
 * Text(WKT).
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_GeomFromText extends AbstractFullOperation {

	private WKTReader wktReader = new WKTReader();
	private int index = 0;
	/**
	 * Creates a geometry node from a Well-known Text(WKT).
	 * 
	 * @param wellKnownText
	 *            the well known text, such as POINT (48 7)
	 * @throws ParseException
	 */
	public ST_GeomFromText(String wellKnownText) throws ParseException {
		this.add(wktReader.read(wellKnownText));
	}

	/**
	 * 
	 * @param wktList
	 * @throws ParseException
	 */
	public ST_GeomFromText(List<String> wktList) throws ParseException {
		for (String wkt : wktList) {
			Geometry geom = wktReader.read(wkt);
			this.add(geom);
		}
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		
		Geometry geometry = this.getGeometries().get(this.index++);
		
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, geometry);
		record.setProperty(ST_GeomFromText.class.getName(), geometry);
		records.add(record);
		return record;
	}
}
