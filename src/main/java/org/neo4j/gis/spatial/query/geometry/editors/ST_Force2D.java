package org.neo4j.gis.spatial.query.geometry.editors;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 * @author Andreas Wilhelm
 *
 */
public class ST_Force2D extends AbstractFullOperation {

	public boolean needsToVisit(Envelope indexNodeEnvelope) {
		return true;
	}

	public SpatialDatabaseRecord onIndexReference(int mode, Node node,
			Layer layer) {
		
		Geometry geom = decodeGeometry(node);
		Geometry targetGeometry = geom;
		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(layer, node, targetGeometry);
		return databaseRecord;
	}


}
