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
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.operation.restriction.Restriction;
import org.neo4j.gis.spatial.operation.restriction.RestrictionMap;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;

/**
 * <p>
 * The <code>AbstractReadOperation</code> is the abstract implementation of the
 * {@link Select} interface.
 * </p>
 * 
 * <p>
 * This class should be extend by spatial type implementation which should be
 * capable to do only search operations.
 * </p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public abstract class AbstractReadOperation extends AbstractOperation
		implements Select {

	// Contains the search results.
	private List<SpatialDatabaseRecord> results;

	/**
	 * Default constructor.
	 */
	public AbstractReadOperation() {
		this.results = new ArrayList<SpatialDatabaseRecord>();
	}

	/**
	 * @see Select#getResults()
	 */
	public List<SpatialDatabaseRecord> getResults() {
		return this.results;
	}

	/**
	 * @see Select#addRestriction(RestrictionType, String)
	 */
	public void addRestriction(RestrictionType restrictionType, String value) {
		Restriction restriction = new Restriction(restrictionType, value);
		this.restrictions.put(restriction.getKey(), restriction);
	}
	
	/**
	 * @see Select#getRestrictions()
	 */
	public RestrictionMap getRestrictions() {
		return this.restrictions;
	}
}
