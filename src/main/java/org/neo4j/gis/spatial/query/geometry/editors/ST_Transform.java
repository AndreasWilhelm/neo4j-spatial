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

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.SpatialDatabaseException;
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
 * The <code>ST_Transform</code> class offers coordinate transformation from the
 * layer coordinate reference system(CRS) to a spatial reference system
 * identifier(SRID) referenced by a integer parameter or CRS. The target SRID
 * must exist in GeoTools.
 * </p>
 * 
 * <h3>For example:</h3>
 * <ul>
 * <li>Input: LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985,
 * 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521,
 * 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416,
 * 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183,
 * 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325,
 * 12.9762427 56.058262, 12.9762034 56.0583531)</li>
 * <li>Output(EPSG:3395): LINESTRING (1443928.4769281733 7533666.928761364,
 * 1444104.9851127751 7533836.997176612, 1444111.831261459 7533843.6018516235,
 * 1444212.9761507937 7533939.82783964, 1444241.5518640804 7533967.022792728,
 * 1444341.4833709656 7534079.523791746, 1444449.7081799149 7534158.185951799,
 * 1444473.8422455187 7534183.770227966, 1444491.9427947218 7534251.511279608,
 * 1444497.998575021 7534269.297135449, 1444519.0936185261 7534331.26942866,
 * 1444519.0936185261 7534394.495612137, 1444523.6131898526 7534441.149629269,
 * 1444514.9414015193 7534520.173629358, 1444508.7297739335 7534545.938176674,
 * 1444504.354917945 7534564.062941168)</li>
 * </ul>
 * <p>
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_Transform extends AbstractFullOperation {

	private final static String EPSG = "EPSG:";

	private CoordinateReferenceSystem targetCRS;

	/**
	 * Transformation from the geometry coordinate reference system(CRS) to
	 * another {@link CoordinateReferenceSystem}.
	 * 
	 * @param targetSRID
	 *            the target spatial reference system identifier which to
	 *            transform the geometries.
	 * @throws NoSuchAuthorityCodeException
	 *             if the spatial reference system identifier could not be
	 *             found. You may add the CRS definition by yourself.
	 * @throws FactoryException
	 *             if the CoordinateReferenceSystem creations fails for some
	 *             other reasons.
	 */
	public ST_Transform(int targetSRID) throws NoSuchAuthorityCodeException,
			FactoryException, Exception {
		this.targetCRS = CRS.decode(EPSG + targetSRID);
		this.setCoordinateReferenceSystem(this.targetCRS);
	}

	/**
	 * Transformation from the geometry coordinate reference system(CRS) to
	 * another {@link CoordinateReferenceSystem}.
	 * 
	 * @param crs
	 *            the target CoordinateReferenceSystem which to transform the
	 *            geometries.
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

		SpatialDatabaseRecord record = null;

		Geometry geometry = decodeGeometry(node);
		try {
			MathTransform transform = CRS.findMathTransform(
					this.getCoordinateReferenceSystem(), this.targetCRS);
			Geometry targetGeometry = JTS.transform(geometry, transform);
			record = new SpatialDatabaseRecordImpl(layer, node, targetGeometry);
			record.setResult(targetGeometry);
			records.add(record);
		} catch (FactoryException e) {
			throw new SpatialDatabaseException(e.getMessage());
		} catch (MismatchedDimensionException e) {
			throw new SpatialDatabaseException("Mismatched dimension at Node "
					+ node.getId() + ". " + e.getMessage());
		} catch (TransformException e) {
			throw new SpatialDatabaseException("Could not transform Node "
					+ node.getId() + "." + e.getMessage());
		}

		return record;
	}

}
