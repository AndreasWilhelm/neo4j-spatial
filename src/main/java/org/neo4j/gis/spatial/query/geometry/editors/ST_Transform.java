/**
 * Copyright (c) 2002-2011 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.gis.spatial.query.geometry.editors;

import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.graphdb.Node;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * <p>
 * This class offers coordinate transformation from the layer coordinate
 * reference system(CRS) to spatial reference system identifier(SRID) referenced
 * by a integer parameter or CRS. The target SRID must exist in GeoTools.
 * </p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Transform extends AbstractFullOperation {

	private static Logger logger = Logger.getLogger(ST_Transform.class);

	private static String EPSG = "EPSG:";
	private CoordinateReferenceSystem targetCRS;

	/**
	 * 
	 * @param targetSRID
	 * @throws NoSuchAuthorityCodeException
	 * @throws FactoryException
	 */
	public ST_Transform(int targetSRID) throws NoSuchAuthorityCodeException,
			FactoryException {
		this.targetCRS = CRS.decode(EPSG + targetSRID);
		this.setCoordinateReferenceSystem(this.targetCRS);
	}

	/**
	 * 
	 * @param crs
	 * @throws NoSuchAuthorityCodeException
	 * @throws FactoryException
	 */
	public ST_Transform(CoordinateReferenceSystem crs) {
		this.targetCRS = crs;
	}

	public boolean needsToVisit(Envelope indexNodeEnvelope) {
		return true;
	}

	public SpatialDatabaseRecord onIndexReference(int mode, Node geomNode, Layer layer) {
		SpatialDatabaseRecord spatialDatabaseRecord = null;
		
		Geometry geom = this.decodeGeometry(geomNode);
		try {
			MathTransform transform = CRS.findMathTransform(this
					.getCoordinateReferenceSystem(), this.targetCRS);
			Geometry targetGeometry = JTS.transform(geom, transform);
			spatialDatabaseRecord = new SpatialDatabaseRecordImpl(layer, geomNode, targetGeometry);
		} catch (FactoryException e) {
			logger.error(e.getMessage());
		} catch (MismatchedDimensionException e) {
			logger.error("Mismatched dimension at Node " + geomNode.getId()
					+ " :" + e.getMessage());
		} catch (TransformException e) {
			logger.error("Could not transform Node " + geomNode.getId() + " :"
					+ e.getMessage());
		}
		
		return spatialDatabaseRecord;
	}



}
