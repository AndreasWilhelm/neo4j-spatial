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
package org.neo4j.gis.spatial.query.geometry.outputs;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialQuery;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.vividsolutions.jts.geom.Geometry;

/**
 * The <code>ST_AsGeoJSON</code> class represent the {@link Geometry} and/or the
 * relationship(with the relationship properties) and the node properties as
 * GeoJSON.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_AsGeoJSON extends AbstractReadOperation {

	// Identifier to add node properties to the GeoJSON.
	private boolean enableProperties = false;
	// Identifier to add relationships and the relation properties to the
	// GeoJSON.
	private boolean enableRelationships = false;

	/**
	 * Create a valid GeoJSON String that contains only the geometry.
	 */
	public ST_AsGeoJSON() {
	}

	/**
	 * Create a valid GeoJSON String that contains the geometry and the node
	 * properties/and or relationships with its property values.
	 * 
	 * @param enableProperties
	 *            add node properties to the GeoJSON.
	 * @param enableRelationships
	 *            add relationships and its properties to the GeoJSON.
	 */
	public ST_AsGeoJSON(boolean enableProperties, boolean enableRelationships) {
		this.enableProperties = enableProperties;
		this.enableRelationships = enableRelationships;
	}

	/**
	 * @see SpatialQuery#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {

		Geometry geom = decodeGeometry(node);
		StringBuilder relations = new StringBuilder();
		StringBuilder nodeProperties = new StringBuilder();

		// Determine if the properties should be added.
		if (this.enableProperties) {
			// Add every property key-value pair.
			for (String key : node.getPropertyKeys()) {
				nodeProperties.append(key + ":" + node.getProperty(key));
			}
		}

		// Determine if the relations should be added.
		if (this.enableRelationships) {
			// Add every Relationship.
			for (Relationship rel : node.getRelationships()) {
				relations.append("RelationshipType" + ":" + rel.getType());
				for (String relkey : rel.getPropertyKeys()) {
					relations.append(relkey + ":" + node.getProperty(relkey));
				}
			}
		}

		
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node);

		record.setResult(this.convert2json(geom, nodeProperties, relations));
		records.add(record);
		return record;
	}

	/**
	 * Generate a GeoJSON from a given {@link Geometry} and/or relationships and node properties.
	 * 
	 * @param geometry
	 *            the Geometry to convert.
	 * @param properties
	 * @param relations
	 * @return the converted geometry as a valid GeoJSON.
	 */
	private String convert2json(Geometry geometry,
			StringBuilder nodeProperties, StringBuilder relations) {

		
		
		return null;
	}

}
