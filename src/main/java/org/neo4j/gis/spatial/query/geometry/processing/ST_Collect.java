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

import java.util.ArrayList;
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
 * The <code>ST_Collect</code> spatial type function returns a
 * {@link GeometryCollection} of all collected geometries.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Collect extends AbstractFullOperation {

	private ArrayList<Geometry> geometries = new ArrayList<Geometry>();
	private GeometryFactory geometryFactory = new GeometryFactory();

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {

		Geometry geometry = decodeGeometry(node);
		this.geometries.add(geometry);

		GeometryCollection collect = new GeometryCollection(
				(Geometry[]) geometries.toArray(new Geometry[0]),
				geometryFactory);

		// TODO Handle null for node on update we should create a new node
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				null, collect);
		record.setResult(collect);
		records.clear();
		records.add(record);
		return record;
	}

}
