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

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

/**
 * The <code>RelationshipItem</code> class provides a simple container to
 * store {@link Node} and {@link RelationshipType} for relationships.
 * 
 * @author Andreas Wilhelm
 *
 */
public class NodeRelation {
	
	private RelationshipType relationshipType = null;
	private Node node = null;
	
	public NodeRelation(Node node, RelationshipType type) {
		this.node = node;
		this.relationshipType = type;
	}
	

	/**
	 * @return the relationshipType
	 */
	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime
				* result
				+ ((relationshipType == null) ? 0 : relationshipType.hashCode());
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeRelation other = (NodeRelation) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (relationshipType == null) {
			if (other.relationshipType != null)
				return false;
		} else if (!relationshipType.equals(other.relationshipType))
			return false;
		return true;
	}
	
	

	
}
