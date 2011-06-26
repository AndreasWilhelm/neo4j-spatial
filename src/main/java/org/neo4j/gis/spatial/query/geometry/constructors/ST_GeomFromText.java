package org.neo4j.gis.spatial.query.geometry.constructors;

import java.util.ArrayList;
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
 * Creates a geometry node from a Well-known Text(WKT).
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_GeomFromText extends AbstractFullOperation {

	private int index = 0;

	/**
	 * Creates a geometry node from a Well-known Text(WKT).
	 * 
	 * @param wellKnownText
	 *            the well known text, such as POINT (48 7)
	 * @throws ParseException
	 */
	public ST_GeomFromText(String wellKnownText) throws ParseException {
		WKTReader wktReader = new WKTReader();
		Geometry geom = wktReader.read(wellKnownText);
		List<Geometry> geometies = new ArrayList<Geometry>();
		geometies.add(geom);
		this.setGeometries(geometies);
	}
	
	/**
	 * 
	 * @param wktList
	 * @throws ParseException
	 */
	public ST_GeomFromText(List<String> wktList) throws ParseException {
		List<Geometry> geometries = new ArrayList<Geometry>();
		WKTReader wktReader = new WKTReader();
		
		for (String wkt : wktList) {
			Geometry geom = wktReader.read(wkt);
			geometries.add(geom);
		}
		this.setGeometries(geometries);
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
