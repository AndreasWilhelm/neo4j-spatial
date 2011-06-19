package org.neo4j.gis.spatial.query.geometry.constructors;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Creates a geometry node from a Well-known Text(WKT).
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_GeomFromText extends AbstractFullOperation {

	private Geometry geom = null;

	/**
	 * Creates a geometry node from a Well-known Text(WKT).
	 * 
	 * @param wellKnownText
	 *            the well known text, such as POINT (48 7)
	 * @throws ParseException
	 */
	public ST_GeomFromText(String wellKnownText) throws ParseException {
		WKTReader wktReader = new WKTReader();
		this.geom = wktReader.read(wellKnownText);
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(org.neo4j.gis.spatial.operation.OperationType,
	 *      Node, Layer)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, this.geom);
		return record;
	}

}
