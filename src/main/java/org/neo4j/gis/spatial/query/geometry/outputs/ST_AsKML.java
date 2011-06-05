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

import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.graphdb.Node;
import org.geotools.xml.Encoder;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_AsKML extends AbstractReadOperation {

	public boolean needsToVisit(Envelope indexNodeEnvelope) {
		return true;
	}

	public SpatialDatabaseRecord onIndexReference(int mode, Node node,
			Layer layer) {
		Geometry geom = decodeGeometry(node);
	

		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(
				layer, node);
		try {
			Encoder encoder = new Encoder(new KMLConfiguration());
			encoder.setIndenting(true);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			encoder.encode(geom, KML.Geometry, out);
			String kml = new String(out.toByteArray());
			databaseRecord.setProperty(ST_AsKML.class.getName(), kml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return databaseRecord;
	}

}
