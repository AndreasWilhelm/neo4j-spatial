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

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.List;

import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseException;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.graphdb.Node;
import org.geotools.xml.Encoder;
import com.vividsolutions.jts.geom.Geometry;
/**
 * The <code>ST_AsKML</code> class represent the {@link Geometry} as KML.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_AsKML extends AbstractReadOperation {

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = decodeGeometry(node);
	

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(
				layer, node);
		try {
			Encoder encoder = new Encoder(new KMLConfiguration());
			encoder.setIndenting(true);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			encoder.encode(geom, KML.Geometry, out);
			String kml = new String(out.toByteArray());
			record.setResult(kml);
			records.add(record);
		} catch (IOException e) {
			throw new SpatialDatabaseException(e.getMessage());
		}
		return record;
	}

}
