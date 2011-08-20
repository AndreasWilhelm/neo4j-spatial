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
package org.neo4j.gis.spatial.geometry;

import java.io.File;
import java.util.List;

import org.neo4j.gis.spatial.Layer;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.operation.Search;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_AsBinary;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_AsText;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_Boundary;
import org.neo4j.gis.spatial.query.geometry.accessors.ST_Box2D;
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
		Search select = new ST_AsText();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		if (debug) {
			printTestResults("textAsText", select.getResults());
		}
	}
	public void testAsBinary() throws Exception {
		Search select = new ST_AsBinary();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		if (debug) {
			printTestResults("testAsBinary", select.getResults());
		}
	}
	
	
	public void testGetStartPoint() throws Exception {
		Search select = new ST_StartPoint();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		if (debug) {
			printTestResults("testGetStartPoint", select.getResults());
		}
	}
	
	public void testGetEndPoint() throws Exception {
		Search select = new ST_EndPoint();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		if (debug) {
			printTestResults("testGetEndPoint", select.getResults());
		}
	}
	
	
	public void testMinX() throws Exception {
		Search select = new ST_MinX();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("12.9710302", select.getResults().get(0).getResult().toString());
		assertEquals("12.9639158", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testMinX", select.getResults());
		}
	}
	
	
	public void testMinY() throws Exception {
		Search select = new ST_MinY();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("56.0538436", select.getResults().get(0).getResult().toString());
		assertEquals("56.0704885", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testMinY", select.getResults());
		}
	}
	
	public void testMaxX() throws Exception {
		Search select = new ST_MaxX();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("12.9763764", select.getResults().get(0).getResult().toString());
		assertEquals("12.9680173", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testMaxX", select.getResults());
		}
	}
	
	public void testMaxY() throws Exception {
		Search select = new ST_MaxY();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("56.0583531", select.getResults().get(0).getResult().toString());
		assertEquals("56.0711966", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testMaxY", select.getResults());
		}
	}
	
	public void testBoundary() throws Exception {
		Search select = new ST_Boundary();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("MULTIPOINT ((12.9710302 56.0538436), (12.9762034 56.0583531))", select.getResults().get(0).getResult().toString());
		assertEquals("MULTIPOINT ((12.9639158 56.070904), (12.9680173 56.0704885))", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testBoundary", select.getResults());
		}
	}

	public void testBox2D() throws Exception {
		Search select = new ST_Box2D();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("BOX(12.9710302 12.9763764,56.0538436 56.0583531)", select.getResults().get(0).getResult().toString());
		assertEquals("BOX(12.9639158 12.9680173,56.0704885 56.0711966)", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testBox2D", select.getResults());
		}
	}

	public void testDimension() throws Exception {
		Search select = new ST_Dimension();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals(1, select.getResults().get(0).getResult());
		assertEquals(1, select.getResults().get(1).getResult());
		if (debug) {
			printTestResults("testDimension", select.getResults());
		}
	}
	
	public void testEnvelope() throws Exception {
		Search select = new ST_Envelope();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("Env[12.9710302 : 12.9763764, 56.0538436 : 56.0583531]", select.getResults().get(0).getResult().toString());
		assertEquals("Env[12.9639158 : 12.9680173, 56.0704885 : 56.0711966]", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testEnvelope", select.getResults());
		}
	}
	
	public void testIsSimple() throws Exception {
		Search select = new ST_IsSimple();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals(true, select.getResults().get(0).getResult());
		assertEquals(true, select.getResults().get(1).getResult());
		if (debug) {
			printTestResults("testIsSimple", select.getResults());
		}
	}
	
	public void testIsNotSimple() throws Exception {
		Search select = new ST_IsNotSimple();
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("testIsNotSimple", select.getResults());
		}
	}
	
	public void testNumGeometries() throws Exception {
		Search select = new ST_NumGeometries();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals(1, select.getResults().get(0).getResult());
		assertEquals(1, select.getResults().get(1).getResult());
		if (debug) {
			printTestResults("testNumGeometries", select.getResults());
		}
	}
	
	public void testNumPoints() throws Exception {
		Search select = new ST_NumPoints();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals(16, select.getResults().get(0).getResult());
		assertEquals(8, select.getResults().get(1).getResult());
		if (debug) {
			printTestResults("testNumPoints", select.getResults());
		}
	}
	
	public void testPointN() throws Exception {
		Search select = new ST_PointN(0);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("POINT (12.9710302 56.0538436)", select.getResults().get(0).getResult().toString());
		assertEquals("POINT (12.9639158 56.070904)", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testPointN", select.getResults());
		}
	}
	
	public void testSRID() throws Exception {
		Search select = new ST_SRID();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		//TODO: GeometryEncoder need deliver a geometry with srid.
		//assertEquals(4326, results.get(0).getResult());
		//assertEquals(4326, results.get(1).getResult());
		if (debug) {
			printTestResults("testSRID", select.getResults());
		}
	}
		
	public void testExteriorRing() throws Exception {
		Search select = new ST_ExteriorRing();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		if (debug) {
			printTestResults("testExteriorRing", select.getResults());
		}
	}
		
	public void testGeometryType() throws Exception {
		Search select = new ST_GeometryType();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("LineString", select.getResults().get(0).getResult().toString());
		assertEquals("LineString", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testGeometryType", select.getResults());
		}
	}
	
	public void testGeometryN() throws Exception {
		Search select = new ST_GeometryN(0);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531)", select.getResults().get(0).getResult().toString());
		assertEquals("LINESTRING (12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9678958 56.0705812, 12.9680173 56.0704885)", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testGeometryN", select.getResults());
		}
	}
		
	public void testNumInteriorRings() throws Exception {
		Search select = new ST_NumInteriorRings();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		if (debug) {
			printTestResults("testNumInteriorRings", select.getResults());
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