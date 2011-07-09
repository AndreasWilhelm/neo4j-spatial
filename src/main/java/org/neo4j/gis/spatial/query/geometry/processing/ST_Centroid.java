package org.neo4j.gis.spatial.query.geometry.processing;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;

public class ST_Centroid extends AbstractReadOperation {

	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		Geometry geom = decodeGeometry(node);

		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(
				layer, node);
		
		databaseRecord.setProperty(ST_Centroid.class.getName(), geom.getCentroid());
		records.add(databaseRecord);
		return databaseRecord;
	}
	
}