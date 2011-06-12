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

import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.operation.restriction.Restriction;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;
import org.neo4j.graphdb.Node;

/**
 * <p>The <code>AbstractUpdateOperation</code> is the abstract implementation of the
 * {@link Update}, {@link Delete} and {@link Select} interface.</p>
 * 
 * <p>
 * This class should be extend by spatial type implementation which should be
 * capable to do search, delete and update operations.
 * </p>
 * 
 * @author Andreas Wilhelm
 *
 */
public abstract class AbstractUpdateOperation extends AbstractDeleteOperation implements Update {
		
	/**
	 * @see Update#update(SpatialDatabaseRecord)
	 */
	public void update(SpatialDatabaseRecord record) {
		this.encodeGeometry(record.getGeometry(), record.getGeomNode());
	}
	
	/**
	 * @see Update#addRestriction(RestrictionType, String)
	 */
	public void addRestriction(RestrictionType restrictionType, String value) {
		this.restrictions.add(new Restriction(restrictionType, value));
	}
	
	/**
	 * @see Update#isRestricted(Node)
	 */
	public boolean isRestricted(Node node) {
		for (Restriction res : this.restrictions) {
			if(res.hasRestriction(node)) return true;
		}
		return false;
	}
}
