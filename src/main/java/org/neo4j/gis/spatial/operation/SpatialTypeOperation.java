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

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.RTreeIndex;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.graphdb.Node;

/**
 * This <code>SpatialTypeQuery</code> interface provides the base APIs to
 * execute spatial type operations.
 * 
 * @author Andreas Wilhelm
 */
public interface SpatialTypeOperation {

	/**
	 * Sets the data layer of the spatial type operation.
	 * 
	 * @param layer
	 *            The layer of the spatial type operation..
	 */
	public abstract void setLayer(Layer layer);

	/**
	 * Gets the data layer of the spatial type operation.
	 * 
	 * @return Returns the layer of the spatial type operation..
	 */
	public abstract Layer getLayer();

	/**
	 * The base method which must be implement by any spatial type class. It
	 * will be called from the {@link RTreeIndex} on any node on which the
	 * spatial type operation should be done.
	 * 
	 * @param type
	 *            The operation type, such as DELETE or INSERT
	 * @param node
	 *            The {@link Node} on which the spatial type operation should
	 *            done.
	 * @param layer
	 *            The {@link Layer} which has execute this spatial type
	 *            operation.
	 * @return Returns a {@link SpatialDatabaseRecord} of spatial type
	 *         operation.
	 */
	public abstract SpatialDatabaseRecord onIndexReference(OperationType type, Node node,
			Layer layer, List<SpatialDatabaseRecord> records);

}
