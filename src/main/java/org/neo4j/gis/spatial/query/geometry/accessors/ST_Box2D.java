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
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * The <code>ST_Box2D</code> class returns the maximal extends of the
 * Geometry longitude and latitude in following order: BOX(minX, maxX, minY, maxY). 
 * </p>
 * 
 * <h3>For example:</h3>
 * 
 * <code>BOX(12.9710302 12.9763764,56.0538436 56.0583531)</code>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Box2D extends AbstractReadOperation {

	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = decodeGeometry(node);

		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node);

		Envelope envelope = geom.getEnvelopeInternal();
		String env = envelope.toString();
		String bbox = env.replace("Env[", "BOX(").replace("]", ")")
				.replace(" :", "").replace(", ", ",");
		record.setResult(bbox);
		records.add(record);
		return record;
	}

}