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
package org.neo4j.gis.spatial.query.geometry.accessors;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ByteOrderValues;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * Represent the geometry as Well-Known Binary (WKB).
 * 
 * <h2>For example:</h2> <code>
 * 01020000000800000003322A5F86ED2940EA93DC6113094C40CAECE2E
 * C8CED2940D02BF93317094C403EE136644DEE29400F4B5EF81C094C40
 * A06F0B96EAEE29403B66EABF18094C407E26B15A4FEF29402684B3001
 * 2094C40ED0104BD81EF29407D3ECA880B094C401CDFCD0990EF294031
 * DB04CE08094C40A8ECAAF69FEF29403F1F65C405094C40
 * <code>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_AsBinary extends AbstractReadOperation {

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geometry = decodeGeometry(node);
		WKBWriter wkbWriter = new WKBWriter(2, ByteOrderValues.LITTLE_ENDIAN);
		byte[] wkb = wkbWriter.write(geometry);
		String hex = WKBWriter.toHex(wkb);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(
				layer, node);
		record.setResult(hex);
		records.add(record);
		return record;
	}

}
