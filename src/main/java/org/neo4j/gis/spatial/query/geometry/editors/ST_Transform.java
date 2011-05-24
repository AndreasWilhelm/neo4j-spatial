package org.neo4j.gis.spatial.query.geometry.editors;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.neo4j.gis.spatial.AbstractSearch;
import org.neo4j.graphdb.Node;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


public class ST_Transform extends AbstractSearch {

	private static final String EPSG = "EPSG:";
	private CoordinateReferenceSystem targetCRS;

	/**
	 * Create a search result list with new geometry with its coordinates transformed to the 
	 * SRID referenced by the integer parameter.
	 * 
	 * @param targetSRID
	 * @throws NoSuchAuthorityCodeException
	 * @throws FactoryException
	 */
	public ST_Transform(int targetSRID) throws NoSuchAuthorityCodeException, FactoryException {
		this.targetCRS = CRS.decode(EPSG + targetSRID);
	}	

	public boolean needsToVisit(Envelope indexNodeEnvelope) {
		return true;
	}

	public void onIndexReference(Node geomNode) {
		
		Geometry geom = decode(geomNode);
		geom.setSRID(4326);
		try {
			CoordinateReferenceSystem sourceCRS = CRS.decode(EPSG + geom.getSRID());
			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			Geometry targetGeometry = JTS.transform(geom, transform);
			add(geomNode, targetGeometry);
		} catch (NoSuchAuthorityCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MismatchedDimensionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
