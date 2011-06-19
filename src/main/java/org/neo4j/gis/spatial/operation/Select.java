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

import java.util.List;

import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.operation.restriction.Restriction;
import org.neo4j.gis.spatial.operation.restriction.RestrictionMap;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;

/**
 * The <code>Select</code> interface provides the public APIs to execute search
 * operations.
 * 
 * @author Andreas Wilhelm
 */
public interface Select extends SpatialTypeOperation {

	/**
	 * Return the result list of the search operation. If the search operation
	 * does not find any entries, a empty list is returned.
	 * 
	 * @return Returns a list of {@link #SpatialDatabaseRecord},
	 */
	public abstract List<SpatialDatabaseRecord> getResults();

	/**
	 * Add a restriction to filter search operation.
	 * 
	 * @param type
	 *            the {@link RestrictionType}.
	 * @param value
	 *            the property value for restriction type.
	 */
	public abstract void addRestriction(RestrictionType type, String value);
	
	/**
	 * Returns a list with restrictions of this update query.
	 * 
	 * @return Returns a list with {@link Restriction}.
	 */
	public abstract RestrictionMap getRestrictions();
}
