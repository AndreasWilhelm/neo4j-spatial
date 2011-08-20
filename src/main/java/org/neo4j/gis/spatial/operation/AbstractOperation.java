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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.neo4j.gis.spatial.GeometryEncoder;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.operation.restriction.RestrictionMap;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * The <code>AbstractOperation</code> is a abstract superclass for spatial type
 * operation. It contains the basic implementation which a necessary to execute
 * spatial type operations.
 * </p>
 * <p>
 * This class <b>should not</b> extend directly by spatial type operations.
 * </p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public abstract class AbstractOperation {

	// The layer which execute this operation.
	private Layer layer = null;
	// The CoordinateReferenceSystem of the layer.
	private CoordinateReferenceSystem crs = null;
	// Contains the delete restrictions.
	protected RestrictionMap restrictions;
	//
	private int threadPoolSize = 8;
	//
	private List<SpatialDatabaseRecord> record = null;
	
	
	
	/**
	 * Default constructor.
	 */
	public AbstractOperation() {
		this.restrictions = new RestrictionMap();
		this.record = Collections.synchronizedList(new ArrayList<SpatialDatabaseRecord>());
	}

	/**
	 * Sets the {@link Layer} and the CoordinateReferenceSystem of layer which
	 * execute this operation.
	 * 
	 * @param layer
	 *            The {@link Layer} which execute this operation.
	 */
	public void setLayer(Layer layer) {
		this.layer = layer;
		this.crs = layer.getCoordinateReferenceSystem();
	}

	/**
	 * Gets the {@link Layer} which execute this operation.
	 * 
	 * @return Returns the {@link Layer} which execute this operation.
	 */
	public Layer getLayer() {
		return this.layer;
	}

	/**
	 * Gets the {@link CoordinateReferenceSystem} of the {@link Layer} which
	 * execute this operation.
	 * 
	 * @return Return the {@link CoordinateReferenceSystem} of the {@link Layer}
	 *         which execute this operation.
	 */
	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return this.crs;
	}

	/**
	 * Sets the {@link CoordinateReferenceSystem} of the {@link Layer} which
	 * execute this operation.
	 * 
	 * @param crs
	 *            The {@link CoordinateReferenceSystem} of the {@link Layer}
	 *            which execute this operation.
	 * @throws Exception
	 */
	protected void setCoordinateReferenceSystem(CoordinateReferenceSystem crs)
			throws Exception {

		// if(this.layer instanceof EditableLayer) {
		this.crs = crs;
		// EditableLayer editLayer = (EditableLayer) this.layer;
		// editLayer.setCoordinateReferenceSystem(this.crs);
		// } else {
		// TODO: Exception which extends from SpatialTypeException.
		// throw new Exception();
		// }

	}

	/**
	 * @see SpatialQuery#getThreadPoolSize()
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	/**
	 * @see SpatialQuery#setThreadPoolSize(int)
	 */
	public void setThreadPoolSize(int nThreads) {
		this.threadPoolSize = nThreads;
	}

	/**
	 * @see GeometryEncoder#encodeGeometry(Geometry,
	 *      org.neo4j.graphdb.PropertyContainer)
	 */
	protected void encodeGeometry(Geometry geometry, Node node) {
		this.layer.getGeometryEncoder().encodeGeometry(geometry, node);
	}

	/**
	 * @see GeometryEncoder#decodeEnvelope(org.neo4j.graphdb.PropertyContainer)
	 */
	protected Envelope getEnvelope(Node node) {
		return this.layer.getGeometryEncoder().decodeEnvelope(node);
	}

	/**
	 * @see GeometryEncoder#decodeGeometry(org.neo4j.graphdb.PropertyContainer)
	 */
	protected Geometry decodeGeometry(Node node) {
		return this.layer.getGeometryEncoder().decodeGeometry(node);
	}
	

	/**
	 * @see Search#getResults()
	 */
	public List<SpatialDatabaseRecord> getResults()  {
		return record;
	}
	
}
