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
package org.neo4j.gis.spatial;

import org.neo4j.graphdb.Node;

/**
 * The <code>SpatialIndexWriter</code> interface 
 * 
 * @author Davide Savazzi, Andreas Wilhelm
 */
public interface SpatialIndexWriter extends SpatialIndexReader {

	/**
	 * Add a node to RTreeIndex.
	 * @param geomNode the
	 */
	void add(Node geomNode);
	
	/**
	 * 
	 * @param geomNodeId
	 * @param deleteGeomNode
	 */
	void remove(long geomNodeId, boolean deleteGeomNode);
	
	/**
	 * 
	 * @param deleteGeomNodes
	 * @param monitor
	 */
	void removeAll(boolean deleteGeomNodes, Listener monitor);
	
	/**
	 * 
	 * @param monitor
	 */
	void clear(Listener monitor);
}