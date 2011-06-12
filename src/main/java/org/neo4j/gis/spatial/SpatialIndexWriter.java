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

import org.neo4j.gis.spatial.operation.Delete;
import org.neo4j.gis.spatial.operation.Insert;
import org.neo4j.gis.spatial.operation.Update;
import org.neo4j.gis.spatial.query.ST_Delete;
import org.neo4j.gis.spatial.query.geometry.constructors.ST_GeomFromText;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Transform;
import org.neo4j.graphdb.Node;


/**
 * @author Davide Savazzi
 * @author Andreas Wilhelm
 */
public interface SpatialIndexWriter extends SpatialIndexReader {

	void add(Node geomNode);
	
	void remove(long geomNodeId, boolean deleteGeomNode);
	
	void removeAll(boolean deleteGeomNodes, Listener monitor);
	
	void clear(Listener monitor);
	
	/**
	 * Execute a spatial type insert query on the layer.
	 * 
	 * @param insert A spatial type insert query, such as {@link ST_GeomFromText} 
	 * @return Returns the number of records in a query.
	 */
	int execute(Insert insert);
	
	/**
	 * Execute a spatial type delete query on the layer.
	 * 
	 * @param delete A spatial type delete query, such as {@link ST_Delete} 
	 * @return Returns the number of records in a query.
	 */
	int execute(Delete delete);
	
	/**
	 * Execute a spatial type update query on the layer.
	 * 
	 * @param update A spatial type query, such as {@link ST_Transform} 
	 * @return Returns the number of records in a query.
	 */
	int execute(Update update);
}