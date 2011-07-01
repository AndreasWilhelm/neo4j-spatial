package org.neo4j.gis.spatial.query.geometry.constructors;

import java.util.List;

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
 * The <code>ST_GeomFromText</code> creates a geometry node from a Well-known
 * Text(WKT).
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_GeomFromText extends AbstractFullOperation {

	private WKTReader wktReader = new WKTReader();
	private int index = 0;
	/**
	 * Creates a geometry node from a Well-known Text(WKT).
	 * 
	 * @param wellKnownText
	 *            the well known text, such as POINT (48 7)
	 * @throws ParseException
	 */
	public ST_GeomFromText(String wellKnownText) throws ParseException {
		this.add(wktReader.read(wellKnownText));
	}

	/**
	 * 
	 * @param wktList
	 * @throws ParseException
	 */
	public ST_GeomFromText(List<String> wktList) throws ParseException {
		for (String wkt : wktList) {
			Geometry geom = wktReader.read(wkt);
			this.add(geom);
		}
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(org.neo4j.gis.spatial.operation.OperationType,
	 *      Node, Layer)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		SpatialDatabaseRecord record = new SpatialDatabaseRecordImpl(layer,
				node, this.getGeometries().get(this.index++));
		return record;
	}
}
