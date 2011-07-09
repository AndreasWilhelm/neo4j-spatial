package org.neo4j.gis.spatial.query.geometry.processing;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * BOX(12.9710302 12.9763764,56.0538436 56.0583531)
 * @author Andreas
 *
 */
public class ST_Box2D extends AbstractReadOperation {

	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = decodeGeometry(node);

		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(
				layer, node);
		
		Envelope envelope = geom.getEnvelopeInternal();
		String env = envelope.toString();
		String bbox = env.replace("Env[", "BOX(").replace("]", ")").replace(" :", "").replace(", ", ",");
		databaseRecord.setProperty(ST_Box2D.class.getName(), bbox);
		records.add(databaseRecord);
		return databaseRecord;
	}
	
}