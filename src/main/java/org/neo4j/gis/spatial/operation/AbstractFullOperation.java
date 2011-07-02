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
import java.util.HashMap;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * The <code>AbstractWriteOperation</code> is the abstract implementation of the
 * {@link Insert}, {@link Select}, {@link Update} and {@link Delete} interface.
 * </p>
 * 
 * <p>
 * This class should be extend by spatial type implementation which should be
 * capable to do all operations.
 * </p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public abstract class AbstractFullOperation extends AbstractUpdateOperation
		implements Insert {

	// The added properties for the insert operations.
	private List<NodeProperty> properties = new ArrayList<NodeProperty>();
	// The added relationships for the insert operations.
	private List<NodeRelation> relationships = new ArrayList<NodeRelation>();
	//
	private List<Geometry> geometries = new ArrayList<Geometry>();

	/**
	 * @see Insert#addRelationship(Node, RelationshipType)
	 */
	public void addRelationship(Node node, RelationshipType relationshipType) {
		// TODO Determine if it is a valid relation or throw a exception.
		relationships.add(new NodeRelation(node, relationshipType));
	}
	
	/**
	 * @see Insert#addRelationship(int, Node, RelationshipType)
	 */
	public void addRelationship(int index, Node node, RelationshipType relationshipType) {
		// TODO Determine if it is a valid relation or throw a exception.
		relationships.add(new NodeRelation(node, relationshipType));
	}

	/**
	 * @see Insert#getRelationships()
	 */
	public List<NodeRelation> getRelationships() {
		return relationships;
	}

	/**
	 * @see Insert#addProperty(String, Object)
	 */
	public void addProperty(String key, Object value) {
		// TODO Determine if it is a valid property or throw a exception.
		NodeProperty property = new NodeProperty(key, value);
		this.properties.add(property);
	}
	
	/**
	 * @see Insert#addProperty(int, String, Object)
	 */
	public void addProperty(int index, String key, Object value) {
		// TODO Determine if it is a valid property or throw a exception.
		//NodeProperty property = new NodeProperty(key, value);
		//this.properties.add(property);
	}

	/**
	 * @see Insert#getProperties()
	 */
	public List<NodeProperty> getProperties() {
		return this.properties;
	}

	/**
	 * @return the geometies
	 */
	public List<Geometry> getGeometries() {
		return this.geometries;
	}

	/**
	 * @param geometies
	 *            the geometies to set
	 */
	public void setGeometries(List<Geometry> geometies) {
		if (geometies != null) {
			this.geometries = geometies;
		}
	}

	/**
	 * @see Insert#add(Geometry)
	 */
	public int add(Geometry geom) {
		if (geom != null) {
			this.geometries.add(geom);
			return geometries.size();
		} else {
			return -1;
		}
	}
	
	/**
	 * @see Insert#add(Geometry, HashMap)
	 */
	public int add(Geometry geom, HashMap<String, Object> properties) {
		if (geom != null) {
			this.geometries.add(geom);
			return geometries.size();
		} else {
			return -1;
		}
	}
	
	/**
	 * @see Insert#add(Geometry, HashMap, HashMap)
	 */
	public int add(Geometry geom, HashMap<String, Object> properties, HashMap<String, Object> relations) {
		if (geom != null) {
			this.geometries.add(geom);
			return geometries.size();
		} else {
			return -1;
		}
	}

}