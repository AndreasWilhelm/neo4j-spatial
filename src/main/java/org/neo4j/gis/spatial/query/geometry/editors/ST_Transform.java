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

import java.util.List;

import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseRecordImpl;
import org.neo4j.gis.spatial.operation.AbstractFullOperation;
import org.neo4j.gis.spatial.operation.OperationType;
import org.neo4j.gis.spatial.operation.SpatialTypeOperation;
import org.neo4j.graphdb.Node;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

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
public class ST_Transform  extends AbstractFullOperation {

	private static Logger logger = Logger.getLogger(ST_Transform.class);

	private static String EPSG = "EPSG:";
	private CoordinateReferenceSystem targetCRS;

	/**
	 * Transformation from the geometry coordinate reference system(CRS) to
	 * another CRS.
	 * 
	 * @param targetSRID
	 * @throws NoSuchAuthorityCodeException
	 * @throws FactoryException
	 */
	public ST_Transform(int targetSRID) throws NoSuchAuthorityCodeException,
			FactoryException, Exception {
		this.targetCRS = CRS.decode(EPSG + targetSRID);
		this.setCoordinateReferenceSystem(this.targetCRS);
	}

	/**
	 * Transformation from the geometry coordinate reference system(CRS) to
	 * another CRS.
	 * 
	 * @param crs
	 * @throws NoSuchAuthorityCodeException
	 * @throws FactoryException
	 */
	public ST_Transform(CoordinateReferenceSystem crs) {
		this.targetCRS = crs;
	}

	/**
	 * @see SpatialTypeOperation#onIndexReference(OperationType, Node, Layer,
	 *      List)
	 */
	public SpatialDatabaseRecord onIndexReference(OperationType type,
			Node node, Layer layer, List<SpatialDatabaseRecord> records) {
		SpatialDatabaseRecord spatialDatabaseRecord = null;

		Geometry geom = this.decodeGeometry(node);
		try {
			MathTransform transform = CRS.findMathTransform(this
					.getCoordinateReferenceSystem(), this.targetCRS);
			Geometry targetGeometry = JTS.transform(geom, transform);
			spatialDatabaseRecord = new SpatialDatabaseRecordImpl(layer, node,
					targetGeometry);
			records.add(spatialDatabaseRecord);
		} catch (FactoryException e) {
			logger.error(e.getMessage());
		} catch (MismatchedDimensionException e) {
			logger.error("Mismatched dimension at Node " + node.getId() + " :"
					+ e.getMessage());
		} catch (TransformException e) {
			logger.error("Could not transform Node " + node.getId() + " :"
					+ e.getMessage());
		}

		return spatialDatabaseRecord;
	}

}
