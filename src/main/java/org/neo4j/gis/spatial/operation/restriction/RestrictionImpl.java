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
 * <li>{@link #EQUAL_TO}</li>
 * <li>{@link #NOT_EQUAL_TO}</li>
 * </ul>
 * 
 * <p>
 * The restriction value could be a key=value pair or just a single value or a
 * key.
 * </p>
 * <h3>Special restriction values:</h3>
 * <ul>
 * <li>node_id=?</li>
 * </ul>
 * 
 * <h3>Example restriction values:</h3>
 * <ul>
 * <li>roadclass=residential</li>
 * <li>id=55</li>
 * <li>residential</li>
 * <li>roadclass</li>
 * </ul>
 * 
 * 
 * @author Andreas Wilhelm
 * 
 */
public class RestrictionImpl {

	// Restriction input value which could be a property value or a key=value
	// pair.
	private String property = null;
	// The type of the restriction.
	private RestrictionType restrictionType = null;
	// Boolean to identify the property as a key=value or as a single value or
	// key.
	private boolean isKeyValuePair = false;
	// The restriction property key, could be a generated key.
	private String key = null;
	// The restriction property value.
	private String value = null;

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
	public RestrictionImpl(RestrictionType restrictionType, String property) {
		this.restrictionType = restrictionType;
		determineProperty(property);
	}

	/**
	 * @return the key of the restriction.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return the value of the restriction.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Determine the provided node for restrictions.
	 * 
	 * @param node
	 *            The node to determine for restrictions.
	 * @return Returns true if a restriction found on the node.
	 */
	public boolean hasRestriction(Node node) {

		switch (this.restrictionType) {
		case EQUAL_TO:
			return equalTo(node);
		case NOT_EQUAL_TO:
			return notEqualTo(node);
		case HAS_PROPERTY:
			return hasProperty(node);
		case HAS_NOT_PROPERTY:
			return hasNotProperty(node);
		case HAS_RELATIONSHIP:
			return hasRelationship(node);
		case HAS_NOT_RELATIONSHIP:
			return hasNotRelationship(node);
		}
		return false;
	}

	private void determineProperty(Object property) {

		this.property = property.toString();

		// Test if input property is a key=value pair or single key or property
		// value.
		if (this.property.contains("=")) {
			// Split key and value.
			String[] keyValue = this.property.split("=");
			this.key = keyValue[0];
			this.value = keyValue[1];
			this.isKeyValuePair = true;
		} else {
			this.key = this.property;
			this.value = this.property;
			this.isKeyValuePair = false;
		}

	}

	private boolean hasProperty(Node node) {
		// Determine if the property value is a key=value pair.
		if (this.isKeyValuePair) {
			// Determine if node has the property and the value.
			if (node.hasProperty(key)
					&& node.getProperty(key).toString().equals(value)) {
				return true;
			}
		} else if (node.hasProperty(key)) {
			return true;
		}
		return false;
	}

	private boolean hasNotProperty(Node node) {
		return !hasProperty(node);
	}

	private boolean hasRelationship(Node node)
			throws UnsupportedOperationException {
		return false;
	}

	private boolean hasNotRelationship(Node node)
			throws UnsupportedOperationException {
		return false;
	}

	private boolean equalTo(Node node) {

		if (this.isKeyValuePair) {

			// TODO: Remove this workaround for id...
			if (("" + node.getId()).equals(this.value)) {
				return true;
			}
			// Determine if node has the property and the value.
			if (node.hasProperty(key)
					&& node.getProperty(key).toString().equals(value)) {
				return true;
			}
		} else {
			throw new IllegalArgumentException(
					"The restriction property must be a key=value pair");
		}
		return false;
	}

	private boolean notEqualTo(Node node) {
		return !equalTo(node);
	}
}
