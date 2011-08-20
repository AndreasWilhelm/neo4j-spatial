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

import java.util.HashMap;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * This interface provides the public APIs to execute the spatial type insert
 * operations.
 * 
 * @author Andreas Wilhelm
 */
public interface Insert extends SpatialQuery {

	/**
	 * Add a property to a single node.
	 * 
	 * @param index
	 *            The index number of the concrete geometry.
	 * @param key
	 *            The property key.
	 * @param value
	 *            The property value.
	 */
	public abstract void addProperty(int index, String key, Object value);

	/**
	 * Add a property to all nodes in the insert list.
	 * 
	 * @param index
	 *            The index number of the concrete geometry.
	 * @param key
	 *            The property key.
	 * @param value
	 *            The property value.
	 */
	public abstract void addProperty(String key, Object value);

	/**
	 * Get a map of added properties.
	 * 
	 * @return Returns a HashMap with node properties.
	 */
	public abstract List<NodeProperty> getProperties();

	/**
	 * Add a relationship to all insert node.
	 * 
	 * @param node
	 *            - The node to which the {@link Relationship} should done.
	 * @param relationshipType
	 *            - The {@link RelationshipType} of the new {@link Relationship}
	 */
	public abstract void addRelationship(Node node,
			RelationshipType relationshipType);

	/**
	 * Add a relationship to a single node.
	 * 
	 * @param node
	 *            - The node to which the {@link Relationship} should done.
	 * @param relationshipType
	 *            - The {@link RelationshipType} of the new {@link Relationship}
	 */
	public abstract void addRelationship(int index, Node node,
			RelationshipType relationshipType);

	/**
	 * Get a list of added relationships.
	 * 
	 * @return Returns a list with node relationships.
	 */
	public abstract List<NodeRelation> getRelationships();

	/**
	 * 
	 * @return
	 */
	public abstract List<Geometry> getGeometries();

	/**
	 * Add a single geometry to the insert list and returns the current index of
	 * the added geometry or -1 when it was not successful added.
	 * 
	 * @param geometry the geometry to add.
	 * @return Returns the index number of the added geometry or -1 when it was
	 *         not successful added.
	 */
	public abstract int add(Geometry geometry);
	
	/**
	 * 
	 * @param geometry
	 * @param properties
	 * @return
	 */
	public abstract int add(Geometry geometry, HashMap<String, Object> properties);
	
	/**
	 * 
	 * @param geometry
	 * @param properties
	 * @param relations
	 * @return
	 */
	public abstract int add(Geometry geometry, HashMap<String, Object> properties, HashMap<String, Object> relations);

}
