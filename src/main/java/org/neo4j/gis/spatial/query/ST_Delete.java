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
package org.neo4j.gis.spatial.query;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractDeleteOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;
import org.neo4j.graphdb.Node;

/**
 * The simplest spatial type operation to delete a single node {@link Node} of
 * the layer or all nodes which have no restriction.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Delete extends AbstractDeleteOperation {

	/**
	 * Delete all nodes which have no restriction.
	 */
	public ST_Delete() {
	}

	/**
	 * Delete a single node.
	 * 
	 * @param nodeid
	 *            The id of the node to delete.
	 */
	public ST_Delete(long nodeid) {
		this.addRestriction(RestrictionType.EQUAL_TO, "id=" + nodeid);
	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node);
		return record;
	}





}
