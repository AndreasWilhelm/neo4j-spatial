package org.neo4j.gis.spatial.query.geometry.processing;

import org.geotools.geometry.jts.Geometries;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;
import com.vividsolutions.jts.simplify.DouglasPeuckerLineSimplifier;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Simplify extends AbstractFullOperation {

	/**
	 * @see SpatialTypeOperation#onIndexReference(org.neo4j.gis.spatial.operation.OperationType,
	 *      Node, Layer)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		Geometry geom = this.decodeGeometry(node);
		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(
				layer, node, simplify(geom));
		return databaseRecord;
	}

	/**
	 * Douglas-Peuker
	 * 
	 * @return
	 */
	private Geometry simplify(Geometry geometry) {
		
		DouglasPeuckerLineSimplifier simplifier = new DouglasPeuckerLineSimplifier(
				geometry.getCoordinates());

		GeometryFactory geometryFactory = new GeometryFactory(geometry
				.getPrecisionModel(), geometry.getSRID());

		switch (Geometries.get(geometry)) {

		case POINT:
			throw new IllegalArgumentException("unsupported geometry type:"
					+ Geometries.POINT);
		case MULTIPOINT:
			return geometryFactory.createMultiPoint(simplifier.simplify());
		case LINESTRING:
			return geometryFactory.createLineString(simplifier.simplify());
		case MULTILINESTRING:

		case POLYGON:

		case MULTIPOLYGON:

		default:
			throw new IllegalArgumentException("unknown type:"
					+ geometry.getGeometryType());
		}
	}

}
