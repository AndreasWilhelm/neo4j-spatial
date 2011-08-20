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
package org.neo4j.gis.spatial;

import java.util.List;

import org.neo4j.gis.spatial.operation.Search;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Intersect;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;


/**
 * Creates a Network of LineStrings.
 * If a LineString start point or end point is equal to some other LineString start point or end point, 
 * the two LineStrings are connected together with a Relationship. 
 * 
 * @author Davide Savazzi
 */
public class LineStringNetworkGenerator {

	// Constructor

	public LineStringNetworkGenerator(EditableLayer pointsLayer, EditableLayer edgesLayer) {
		this(pointsLayer, edgesLayer, null);
	}
	
	public LineStringNetworkGenerator(EditableLayer pointsLayer, EditableLayer edgesLayer, Double buffer) {
		this.pointsLayer = pointsLayer;
		this.edgesLayer = edgesLayer;
		this.buffer = buffer;
	}
	
	
	// Public methods
	
	public void add(SpatialDatabaseRecordImpl record) {
		Geometry geometry = record.getGeometry();
		if (geometry instanceof MultiLineString) {
			add((MultiLineString) geometry, record);
		} else if (geometry instanceof LineString) {
			add((LineString) geometry, record);
		} else {
			// TODO better handling?
			throw new IllegalArgumentException("geometry type not supported: " + geometry.getGeometryType());
		}
	}
	
	public void add(MultiLineString lines) {
		add(lines, null);
	}
	
	public void add(LineString line) {
		add(line, null);
	}
	

	// Private methods

	protected void add(MultiLineString line, SpatialDatabaseRecordImpl record) {
		for (int i = 0; i < line.getNumGeometries(); i++) {
			add((LineString) line.getGeometryN(i), record);
		}
	}
	
	protected void add(LineString line, SpatialDatabaseRecordImpl edge) {
		if (edge == null) {
			edge = edgesLayer.add(line);
		}
		
		// TODO reserved property?
		edge.setProperty("_network_length", edge.getGeometry().getLength());
		
		addEdgePoint(edge.getGeomNode(), line.getStartPoint());
		addEdgePoint(edge.getGeomNode(), line.getEndPoint());
	}
	
	protected void addEdgePoint(Node edge, Geometry edgePoint) {
		if (buffer != null) edgePoint = edgePoint.buffer(buffer.doubleValue());
		
		Search search = new ST_Intersect(edgePoint);
		pointsLayer.execute(search);
		List<SpatialDatabaseRecord> results = search.getResults();
		if (results.size() == 0) {
			SpatialDatabaseRecord point = pointsLayer.add(edgePoint);
			edge.createRelationshipTo(point.getGeomNode(), SpatialRelationshipTypes.NETWORK);
		} else {
			for (SpatialDatabaseRecord point : results) {
				edge.createRelationshipTo(point.getGeomNode(), SpatialRelationshipTypes.NETWORK);
			}
		}
	}
	
	
	// Attributes
	
	private EditableLayer pointsLayer;
	private EditableLayer edgesLayer;
	private Double buffer;
}