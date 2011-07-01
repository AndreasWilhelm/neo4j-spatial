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
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public class InsertOperation {

	List<NodeProperty> properties = new ArrayList<NodeProperty>();
	List<NodeRelation> relationships = new ArrayList<NodeRelation>();

	/**
	 * Add a property to the new node.
	 * 
	 * @param key
	 *            The property key.
	 * @param value
	 *            The property value.
	 */

	public void addProperty(String key, Object value) {
		// TODO Determine if it is a valid property or throw a exception.
		NodeProperty property = new NodeProperty(key, value);
		this.properties.add(property);
	}

	/**
	 * @see Insert#getProperties()
	 */
	public List<NodeProperty> getProperties() {
		return this.properties;
	}

	/**
	 * @see Insert#addRelationship(Node, RelationshipType)
	 */
	public void addRelationship(Node node, RelationshipType relationshipType) {
		// TODO Determine if it is a valid relation or throw a exception.
		relationships.add(new NodeRelation(node, relationshipType));
	}

	/**
	 * @see Insert#getRelationships()
	 */
	public List<NodeRelation> getRelationships() {
		return relationships;
	}

}
