package org.neo4j.gis.spatial.operation;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

public class InsertOperation {

	List<NodeProperty> properties = new ArrayList<NodeProperty>();
	List<NodeRelation> relationships = new ArrayList<NodeRelation>();

	/**
	 * Add a property to the new node.
	 * 
	 * @param key
	 *            The property key.
	 * @param value
	 *            The property value.
	 */

	public void addProperty(String key, Object value) {
		// TODO Determine if it is a valid property or throw a exception.
		NodeProperty property = new NodeProperty(key, value);
		this.properties.add(property);
	}

	/**
	 * @see Insert#getProperties()
	 */
	public List<NodeProperty> getProperties() {
		return this.properties;
	}

	/**
	 * @see Insert#addRelationship(Node, RelationshipType)
	 */
	public void addRelationship(Node node, RelationshipType relationshipType) {
		// TODO Determine if it is a valid relation or throw a exception.
		relationships.add(new NodeRelation(node, relationshipType));
	}

	/**
	 * @see Insert#getRelationships()
	 */
	public List<NodeRelation> getRelationships() {
		return relationships;
	}

}
