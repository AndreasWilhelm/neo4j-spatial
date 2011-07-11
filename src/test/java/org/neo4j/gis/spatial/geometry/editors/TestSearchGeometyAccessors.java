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
package org.neo4j.gis.spatial.geometry.editors;

import java.io.File;
import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.operation.Select;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_AsBinary;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_AsText;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_Boundary;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_Box2D;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_CoordDim;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_Dimension;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_EndPoint;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_Envelope;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_ExteriorRing;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_GeometryN;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_GeometryType;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_IsNotSimple;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_IsSimple;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_MaxX;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_MaxY;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_MinX;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_MinY;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_NDims;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_NumGeometries;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_NumInteriorRings;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_NumPoints;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_PointN;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_SRID;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_StartPoint;

/**
 * This unit test testing all available geometry accessor queries:
 *   - ST_AsText
 *   - ST_AsBinary
 * 	 - ST_StartPoint
 *   - ST_EndPoint
 *   
 * @author Andreas Wilhelm
 * 
 */
public class TestSearchGeometyAccessors extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private Layer layer = null;
	private boolean debug = true;

	protected void setUp(boolean deleteDb, boolean useBatchInserter,
			boolean autoTx) throws Exception {
		super.setUp(false, true, false);
		try {
			this.loadTestOsmData(Dataset.LAYER_NAME, Dataset.COMMIT_INTERVAL);
			this.spatialService = new SpatialDatabaseService(graphDb());
			this.layer = spatialService.getLayer(Dataset.LAYER_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testAsText() throws Exception {
		Select select = new ST_AsText();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("textAsText", results);
		}
	}
	public void testAsBinary() throws Exception {
		Select select = new ST_AsBinary();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testAsBinary", results);
		}
	}
	
	
	public void testGetStartPoint() throws Exception {
		Select select = new ST_StartPoint();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testGetStartPoint", results);
		}
	}
	
	public void testGetEndPoint() throws Exception {
		Select select = new ST_EndPoint();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testGetEndPoint", results);
		}
	}
	
	
	public void testMinX() throws Exception {
		Select select = new ST_MinX();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("12.9710302", results.get(0).getResult().toString());
		assertEquals("12.9639158", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testMinX", results);
		}
	}
	
	
	public void testMinY() throws Exception {
		Select select = new ST_MinY();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("56.0538436", results.get(0).getResult().toString());
		assertEquals("56.0704885", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testMinY", results);
		}
	}
	
	public void testMaxX() throws Exception {
		Select select = new ST_MaxX();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("12.9763764", results.get(0).getResult().toString());
		assertEquals("12.9680173", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testMaxX", results);
		}
	}
	
	public void testMaxY() throws Exception {
		Select select = new ST_MaxY();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("56.0583531", results.get(0).getResult().toString());
		assertEquals("56.0711966", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testMaxY", results);
		}
	}
	
	public void testBoundary() throws Exception {
		Select select = new ST_Boundary();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("MULTIPOINT ((12.9710302 56.0538436), (12.9762034 56.0583531))", results.get(0).getResult().toString());
		assertEquals("MULTIPOINT ((12.9639158 56.070904), (12.9680173 56.0704885))", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testBoundary", results);
		}
	}

	public void testBox2D() throws Exception {
		Select select = new ST_Box2D();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("BOX(12.9710302 12.9763764,56.0538436 56.0583531)", results.get(0).getResult().toString());
		assertEquals("BOX(12.9639158 12.9680173,56.0704885 56.0711966)", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testBox2D", results);
		}
	}

	public void testDimension() throws Exception {
		Select select = new ST_Dimension();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(1, results.get(0).getResult());
		assertEquals(1, results.get(1).getResult());
		if (debug) {
			printTestResults("testDimension", results);
		}
	}
	
	public void testEnvelope() throws Exception {
		Select select = new ST_Envelope();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("Env[12.9710302 : 12.9763764, 56.0538436 : 56.0583531]", results.get(0).getResult().toString());
		assertEquals("Env[12.9639158 : 12.9680173, 56.0704885 : 56.0711966]", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testEnvelope", results);
		}
	}
	
	public void testIsSimple() throws Exception {
		Select select = new ST_IsSimple();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(true, results.get(0).getResult());
		assertEquals(true, results.get(1).getResult());
		if (debug) {
			printTestResults("testIsSimple", results);
		}
	}
	
	public void testIsNotSimple() throws Exception {
		Select select = new ST_IsNotSimple();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("testIsNotSimple", results);
		}
	}
	
	public void testNumGeometries() throws Exception {
		Select select = new ST_NumGeometries();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(1, results.get(0).getResult());
		assertEquals(1, results.get(1).getResult());
		if (debug) {
			printTestResults("testNumGeometries", results);
		}
	}
	
	public void testNumPoints() throws Exception {
		Select select = new ST_NumPoints();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(16, results.get(0).getResult());
		assertEquals(8, results.get(1).getResult());
		if (debug) {
			printTestResults("testNumPoints", results);
		}
	}
	
	public void testPointN() throws Exception {
		Select select = new ST_PointN(0);
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("[POINT (12.9710302 56.0538436), POINT (12.9726158 56.0546985), POINT (12.9726773 56.0547317), POINT (12.9735859 56.0552154), POINT (12.9738426 56.0553521), POINT (12.9747403 56.0559176), POINT (12.9757125 56.056313), POINT (12.9759293 56.0564416), POINT (12.9760919 56.0567821), POINT (12.9761463 56.0568715), POINT (12.9763358 56.057183), POINT (12.9763358 56.0575008), POINT (12.9763764 56.0577353), POINT (12.9762985 56.0581325), POINT (12.9762427 56.058262), POINT (12.9762034 56.0583531)]", results.get(0).getResult().toString());
		assertEquals("[POINT (12.9639158 56.070904), POINT (12.9639658 56.0710206), POINT (12.9654342 56.0711966), POINT (12.9666335 56.0710678), POINT (12.9674023 56.0708619), POINT (12.9677867 56.0706645), POINT (12.9678958 56.0705812), POINT (12.9680173 56.0704885)]", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testPointN", results);
		}
	}
	
	public void testSRID() throws Exception {
		Select select = new ST_SRID();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(4326, results.get(0).getResult());
		assertEquals(4326, results.get(1).getResult());
		if (debug) {
			printTestResults("testSRID", results);
		}
	}
	
	public void testNDims() throws Exception {
		Select select = new ST_NDims();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(32424, results.get(0).getResult());
		assertEquals(32424, results.get(1).getResult());
		if (debug) {
			printTestResults("testNDims", results);
		}
	}
	
	public void testCoordDim() throws Exception {
		Select select = new ST_CoordDim();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(32424, results.get(0).getResult());
		assertEquals(32424, results.get(1).getResult());
		if (debug) {
			printTestResults("testCoordDim", results);
		}
	}
	
	public void testExteriorRing() throws Exception {
		Select select = new ST_ExteriorRing();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(4326, results.get(0).getResult());
		assertEquals(4326, results.get(1).getResult());
		if (debug) {
			printTestResults("testExteriorRing", results);
		}
	}
		
	public void testGeometryType() throws Exception {
		Select select = new ST_GeometryType();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("LineString", results.get(0).getResult().toString());
		assertEquals("LineString", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testGeometryType", results);
		}
	}
	
	public void testGeometryN() throws Exception {
		Select select = new ST_GeometryN(0);
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("[LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531)]", results.get(0).getResult().toString());
		assertEquals("[LINESTRING (12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9678958 56.0705812, 12.9680173 56.0704885)]", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testGeometryN", results);
		}
	}
		
	public void testNumInteriorRings() throws Exception {
		Select select = new ST_NumInteriorRings();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("[LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531)]", results.get(0).getResult().toString());
		assertEquals("[LINESTRING (12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9678958 56.0705812, 12.9680173 56.0704885)]", results.get(1).getResult().toString());
		if (debug) {
			printTestResults("testNumInteriorRings", results);
		}
	}
	
	private void loadTestOsmData(String layerName, int commitInterval)
			throws Exception {
		String osmPath = Dataset.OSM_DIR + File.separator + layerName;
		System.out.println("\n=== Loading layer " + layerName + " from "
				+ osmPath + " ===");
		reActivateDatabase(false, true, false);
		OSMImporter importer = new OSMImporter(layerName);
		importer.importFile(getBatchInserter(), osmPath);
		reActivateDatabase(false, false, false);
		importer.reIndex(graphDb(), commitInterval);
	}

	private void printTestResults(String function,
			List<SpatialDatabaseRecord> results) {
		System.out.println("----------------------  " + function
				+ "  -------------------");
		for (SpatialDatabaseRecord spatialDatabaseRecord : results) {
			System.out.println("" + spatialDatabaseRecord.getResult());
		}
		System.out.println("------------------------------------------------");
	}

}