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
	private HashMap<String, Object> properties = new HashMap<String, Object>();
	// The added relationships for the insert operations.
	private List<RelationshipItem> relationships = new ArrayList<RelationshipItem>();
	
	/**
	 * @see Insert#addRelationship(Node, RelationshipType)
	 */
	public void addRelationship(Node node, RelationshipType relationshipType) {
		// TODO Determine if it is a valid relation or throw a exception.
		relationships.add(new RelationshipItem(node, relationshipType));
	}

	/**
	 * @see Insert#getRelationship()
	 */
	public List<RelationshipItem> getRelationship() {
		return relationships;
	}

	/**
	 * @see Insert#addProperty(String, Object)
	 */
	public void addProperty(String key, Object value) {
		// TODO Determine if it is a valid property or throw a exception.
		properties.put(key, value);
	}

	/**
	 * @see Insert#getProperties()
	 */
	public HashMap<String, Object> getProperties() {
		return properties;
	}
}
