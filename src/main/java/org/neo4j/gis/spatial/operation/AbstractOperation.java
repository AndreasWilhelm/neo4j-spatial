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
package org.neo4j.gis.spatial.operation;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Andreas Wilhelm
 *
 */
public abstract class AbstractOperation {
	
	private Layer layer = null;
	private CoordinateReferenceSystem crs = null;
	
	/**
	 * This should restrrict the operation only on nodes which have this property..
	 */
	public void where() {
		// TODO Auto-generated method stub
		
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
		this.crs = layer.getCoordinateReferenceSystem();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return this.crs;
	}
	
	/**
	 * 
	 * @param crs
	 */
	protected void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
		this.crs = crs;
		//TODO: When ST_Translate was succesfull we have to update the layer
		//layer.setCoordinateReferenceSystem(this.crs);
	}
	
	
	/**
	 * 
	 * @param geometry
	 * @param geomNode
	 */
	protected void encodeGeometry(Geometry geometry, Node node) {
		this.layer.getGeometryEncoder().encodeGeometry(geometry, node);
	}
	
	
	/**
	 * 
	 * @param geomNode
	 * @return
	 */
	protected Envelope getEnvelope(Node node) {
		return this.layer.getGeometryEncoder().decodeEnvelope(node);	
	}

	/**
	 * 
	 * @param geomNode
	 * @return
	 */
	protected Geometry decodeGeometry(Node node) {
		return this.layer.getGeometryEncoder().decodeGeometry(node);
	}
	
}
