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

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_InRelation</code> class return geometries have the specified
 * relation with the given geometry. For more information, see the
 * OpenGIS Simple Features Specification at DE-9IM.
 * 
 * @author Davide Savazzi, Andreas Wilhelm
 */
public class ST_InRelation extends AbstractReadOperation {

	private Geometry other;
	private String intersectionPattern;

	/**
	 * 
	 * @param other
	 *            geometry
	 * @param intersectionPattern
	 *            a 9-character string (for more information on the DE-9IM, see
	 *            the OpenGIS Simple Features Specification)
	 */
	public ST_InRelation(Geometry other, String intersectionPattern) {
		this.other = other;
		this.intersectionPattern = intersectionPattern;
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		SpatialDatabaseRecord record = null;
		Geometry geometry = decodeGeometry(node);
		if (geometry.relate(other, intersectionPattern)) {
			record = new SpatialDatabaseRecordImpl(layer, node);
			record.setResult(geometry);
			records.add(record);
		}
		return record;
	}
}
