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
package org.neo4j.gis.spatial.query.geometry.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * <p>
 * The <code>ST_Union</code> class returns a {@link Geometry} which represents
 * all points of the node geometry and the other geometry.
 * </p>
 * 
 * <h3>For example:</h3>
 * <ul>
 * <li>Input: LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985,
 * 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521,
 * 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416,
 * 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183,
 * 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325,
 * 12.9762427 56.058262, 12.9762034 56.0583531)</li>
 * <li>Output: MULTILINESTRING ((12.9710302 56.0538436, 12.9726158 56.0546985,
 * 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521,
 * 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416,
 * 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183,
 * 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325,
 * 12.9762427 56.058262, 12.9762034 56.0583531), (13.9639158 56.070904,
 * 13.9639658 56.0710206, 13.9654342 56.0711966))</li>
 * </ul>
 * <p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Union extends AbstractFullOperation {

	// The geometry from which to union with the geometry from the query.
	private Geometry other;

	/**
	 * Construct a {@link Geometry} which represents all points of the node
	 * geometry and the provided other geometry.
	 * 
	 * @param other
	 *            the geometry from which to union with the geometry from the
	 *            query.
	 * 
	 */
	public ST_Union(Geometry other) {
		this.other = other;
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		SpatialDatabaseRecord record = null;
		GeometryFactory geometryFactory = new GeometryFactory();
		Collection<Geometry> geometries = new ArrayList<Geometry>();

		Geometry geom = this.decodeGeometry(node);
		geometries.add(geom);
		geometries.add(other);

		GeometryCollection geometryCollection = (GeometryCollection) geometryFactory
				.buildGeometry(geometries);

		Geometry unionGeom = geometryCollection.union();

		record = new SpatialDatabaseRecordImpl(layer, node, unionGeom);
		record.setResult(unionGeom);
		records.add(record);
		return record;
	}

}
