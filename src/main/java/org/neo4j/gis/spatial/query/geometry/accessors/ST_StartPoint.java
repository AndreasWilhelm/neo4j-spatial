package org.neo4j.gis.spatial.query.geometry.accessors;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.gis.spatial.query.ST_Insert;
import org.neo4j.graphdb.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Andreas
 *
 */
public class ST_StartPoint extends ST_Insert {

	/**
	 * @see SpatialTypeOperation#onIndexReference(org.neo4j.gis.spatial.operation.OperationType,
	 *      Node, Layer)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer) {
		Geometry geometry = this.decodeGeometry(node);

		Point point = getStartPoint(geometry);

		SpatialDatabaseRecord databaseRecord = new SpatialDatabaseRecordImpl(
				layer, node, point);
		databaseRecord.setProperty(ST_AsBinary.class.getName(), point);
		return databaseRecord;
	}

	private Point getStartPoint(Geometry geometry) {

		Coordinate[] coords = geometry.getCoordinates();
		GeometryFactory geometryFactory = new GeometryFactory(geometry
				.getPrecisionModel(), geometry.getSRID());
		Point startPoint = geometryFactory.createPoint(coords[0]);
		
		return startPoint;
	}

}
