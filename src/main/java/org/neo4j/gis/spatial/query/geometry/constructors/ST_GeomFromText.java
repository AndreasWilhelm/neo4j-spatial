package org.neo4j.gis.spatial.query.geometry.constructors;

import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 * @author Andreas Wilhelm
 *
 */
public class ST_GeomFromText extends AbstractFullOperation {

	private Geometry geom = null;

	/**
	 * 
	 * @param wellKnownText
	 * @throws ParseException
	 */
	public ST_GeomFromText(String wellKnownText) throws ParseException {
		WKTReader wktReader = new WKTReader(null);
		this.geom = wktReader.read(wellKnownText);
	}
	
	public ST_GeomFromText(String wellKnownText, Relationship relationship) throws ParseException {
		this(wellKnownText);
	}
	
	
	public ST_GeomFromText(String wellKnownText, List<Relationship> relationship) throws ParseException {
		this(wellKnownText);
	}
	
	
	public SpatialDatabaseRecord onIndexReference(int mode, Node node,
			Layer layer) {
		// The node should be carete in RThreeIndex by executing insert...
		this.encodeGeometry(this.geom, node);
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer, node);
		return record;
	}

}
