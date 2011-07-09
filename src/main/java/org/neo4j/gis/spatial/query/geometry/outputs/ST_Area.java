package org.neo4j.gis.spatial.query.geometry.outputs;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

public class ST_Area extends AbstractReadOperation {

	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = decodeGeometry(node);

		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(
				layer, node);
		
		databaseRecord.setProperty(ST_Area.class.getName(), geom.getArea());
		records.add(databaseRecord);
		return databaseRecord;
	}
	
}