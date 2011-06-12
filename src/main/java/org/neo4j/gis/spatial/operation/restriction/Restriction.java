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
package org.neo4j.gis.spatial.operation.restriction;

import org.neo4j.graphdb.Node;

/**
 * The <code>Restriction</code> class provides filtering for spatial type
 * queries.
 * 
 * <p>
 * These are the current standard restrictions that RestrictionType defines for
 * launching filtered spatial type queries (usually through
 * {@link Layer#execute}. The most important, and by far most frequently used,
 * are {@link #HAS_PROPERTY} and {@link #HAS_RELATIONSHIP}.
 * </p>
 * 
 * <h3>Available {@link RestrictionType}:</h3>
 * 
 * <ul>
 * <li>{@link #HAS_PROPERTY}</li>
 * <li>{@link #HAS_NOT_PROPERTY}</li>
 * <li>{@link #HAS_RELATIONSHIP}</li>
 * <li>{@link #HAS_NOT_RELATIONSHIP}</li>
 * <li>{@link #IN_BBOX}</li>
 * <li>.......TODO</li>
 * </ul>
 * 
 * <p>
 * The restriction value could be a key=value pair or just a value.
 * </p>
 * 
 * <h3>Example restriction values:</h3>
 * <ul>
 * <li>highway=residential</li>
 * <li>residential</li>
 * </ul>
 * 
 * 
 * @author Andreas Wilhelm
 * 
 */
public class Restriction {

	// Restriction value which could be a property value or a key=value pair.
	private String property;
	// The type of the restriction.
	private RestrictionType restrictionType;

	/**
	 * Create a restriction with a given {@link RestrictionType} and value. The
	 * value could be a simple property value or a key=value pair.
	 * 
	 * @param restrictionType
	 *            The {@link RestrictionType}
	 * @param property
	 *            The restriction value which could be just a property or a
	 *            key=value pair, such as highway=residential.
	 */
	public Restriction(RestrictionType restrictionType, String property) {
		this.restrictionType = restrictionType;
		this.property = property;
	}

	/**
	 * Determine the provided node for restrictions.
	 * 
	 * @param node
	 *            The node to determine for restrictions.
	 * @return Returns true if a restriction found on the node.
	 */
	public boolean hasRestriction(Node node) {

		boolean keyValuePair = isKeyValuePair();

		switch (this.restrictionType) {
		case HAS_PROPERTY:
			return hasProperty(node, keyValuePair);
		case HAS_NOT_PROPERTY:
			return hasNotProperty(node, keyValuePair);
		case HAS_RELATIONSHIP:
			return hasRelationship(node, keyValuePair);
		case HAS_NOT_RELATIONSHIP:
			return hasNotRelationship(node, keyValuePair);
		}
		return false;
	}

	private boolean isKeyValuePair() {
		if (this.property.contains("="))
			return true;
		else
			return false;
	}

	private String[] getKeyValuePair() {
		return this.property.split("=");
	}

	private boolean hasProperty(Node node, boolean iskeyValuePair) {
		
		// Determine if the property value is a key=value pair.
		if (iskeyValuePair) {
			//Split key and value.
			String[] keyValue = getKeyValuePair();
			// Determine if node has the property and the value.
			if (node.hasProperty(keyValue[0])
					&& node.getProperty(keyValue[0]).toString().equals(keyValue[1])) {
				return true;
			}
		} else if (node.hasProperty(property)) {
			return true;
		}

		return false;
	}

	private boolean hasNotProperty(Node node, boolean iskeyValuePair) {
		return !hasProperty(node, iskeyValuePair);
	}

	private boolean hasRelationship(Node node, boolean iskeyValuePair)
			throws UnsupportedOperationException {
		return false;
	}

	private boolean hasNotRelationship(Node node, boolean iskeyValuePair)
			throws UnsupportedOperationException {
		return false;
	}
}
