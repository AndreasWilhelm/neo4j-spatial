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

import org.neo4j.gis.spatial.operation.restriction.Restriction;
import org.neo4j.gis.spatial.operation.restriction.RestrictionMap;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;

/**
 * The <code>Delete</code> interface provides the public APIs to execute spatial
 * type delete operations.
 * 
 * @author Andreas Wilhelm
 */
public interface Delete extends SpatialTypeOperation {

	/**
	 * Add a restriction to filter the delete operation.
	 * 
	 * @param type
	 *            The {@link RestrictionType}.
	 * @param value
	 *            The property value for the restriction type.
	 */
	public abstract void addRestriction(RestrictionType type, String value);

	/**
	 * Returns a list with restrictions of this delete query.
	 * 
	 * @return Returns a list with {@link Restriction}.
	 */
	public abstract RestrictionMap getRestrictions();
}
