package org.neo4j.gis.spatial.query.geometry.outputs;


import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractReadOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.gml2.GMLWriter;

/**
 * 
 * @author Andreas Wilhelm
 *
 */
public class ST_AsGML extends AbstractReadOperation {
	public boolean needsToVisit(Envelope indexNodeEnvelope) {
		return true;
	}



	public SpatialDatabaseRecord onIndexReference(int mode, Node node,
			Layer layer) {
		
		Geometry geometry = this.decodeGeometry(node);
		GMLWriter gmlWriter = new GMLWriter();
		String gml = gmlWriter.write(geometry);
		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(layer, node);
		databaseRecord.setProperty(ST_AsGML.class.getName(), gml);
		return databaseRecord;
	}

}
