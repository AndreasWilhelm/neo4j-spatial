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
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Instances of Layer provide the ability for developers to add/remove and edit geometries
 * associated with a single dataset (or layer). This includes support for several storage
 * mechanisms, like in-node (geometries in properties) and sub-graph (geometries describe by the
 * graph). A Layer can be associated with a dataset. In cases where the dataset contains only one
 * layer, the layer itself is the dataset. See the class DefaultLayer for the standard
 * implementation of that pattern.
 * 
 * @author Davide Savazzi
 * @author Craig Taverner
 * @author Andreas Wilhelm
 */
public interface EditableLayer extends Layer {

    /**
     * @deprecated replaced by {@link EditableLayer#execute(Insert)}
     * Add a new geometry to the layer. This will add the geometry to the index.
     * 
     * @param geometry
     * @return
     */
    SpatialDatabaseRecordImpl add(Geometry geometry);

    /**
     * @deprecated replaced by {@link EditableLayer#execute(Insert)}
     * Add a new geometry to the layer. This will add the geometry to the index.
     * @TODO: Rather use a HashMap of properties
     * 
     * @param geometry
     * @return
     */
    SpatialDatabaseRecordImpl add(Geometry geometry, String[] fieldsName, Object[] fields);

    /**
     * @deprecated replaced by {@link EditableLayer#execute(Delete)}
     * Delete the geometry identified by the passed node id. This might be as simple as deleting the
     * geometry node, or it might require extracting and deleting an entire sub-graph.
     * 
     * @param geoemtryNodeId
     */
    void delete(long geometryNodeId);

    /**
     * @deprecated replaced by  {@link EditableLayer#execute(Update)}
     * Update the geometry identified by the passed node id. This might be as simple as changing
     * node properties or it might require editing an entire sub-graph.
     * 
     * @param geoemtryNodeId
     */
    void update(long geometryNodeId, Geometry geometry);

    /**
     * 
     * @param coordinateReferenceSystem
     */
	void setCoordinateReferenceSystem(
			CoordinateReferenceSystem coordinateReferenceSystem);

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