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

import org.neo4j.graphdb.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * TODO: Clean the complete interface
 *
 */
public interface SpatialDatabaseRecord extends Constants,
		Comparable<SpatialDatabaseRecordImpl> {

	public long getId();

	public Node getGeomNode();
	
	/**
	 * This method returns a simple integer representation of the geometry. Some
	 * geometry encoders store this directly as a property of the geometry node,
	 * while others might store this information elsewhere in the graph, or
	 * deduce it from other factors of the data model. See the GeometryEncoder
	 * for information about mapping from the data model to the geometry.
	 * 
	 * @return integer representation of a geometry
	 * @deprecated This method is of questionable value, since it is better to
	 *             query the geometry object directly, outside the result
	 */
	public int getType();

	public Geometry getGeometry();

	public CoordinateReferenceSystem getCoordinateReferenceSystem();

	public String getLayerName();
	
	
	/**
	 * Sets the spatial type function result for this record.
	 * @param value the spatial type result, could be a String, Geometry, ...
	 */
	public void setResult(Object value);
	
	/**
	 * Returns the spatial type function result for this record.
	 * @return the spatial type function result.
	 */
	public Object getResult();

	/**
	 * Not all geometry records have the same attribute set, so we should test
	 * for each specific record if it contains that property.
	 * 
	 * @param name
	 * @return
	 */
	public boolean hasProperty(String name);

	public String[] getPropertyNames();

	public Object[] getPropertyValues();

	public Object getProperty(String name);

	//@Deprecated
	public void setProperty(String name, Object value);
	


	public int hashcode();

	public boolean equals(Object anotherObject);

	public String toString();

	@SuppressWarnings("rawtypes")
	public Comparable getUserData();

	@SuppressWarnings("rawtypes")
	public void setUserData(Comparable object);

	@SuppressWarnings("unchecked")
	public int compareTo(SpatialDatabaseRecordImpl other);



}