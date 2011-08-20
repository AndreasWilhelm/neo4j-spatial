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
import org.neo4j.gis.spatial.query.geometry.outputs.ST_DistanceInMeters;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_DistanceInMiles;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_MaxDistance;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_MaxDistanceInMeter;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_MaxDistanceInMiles;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Centroid;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Closest;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Collect;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Contain;
import org.neo4j.gis.spatial.query.geometry.processing.ST_ConvexHull;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Cover;
import org.neo4j.gis.spatial.query.geometry.processing.ST_CoveredBy;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Cross;
import org.neo4j.gis.spatial.query.geometry.processing.ST_DelaunayTriangle;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Difference;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Disjoint;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Empty;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Equal;
import org.neo4j.gis.spatial.query.geometry.processing.ST_InRelation;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Intersect;
import org.neo4j.gis.spatial.query.geometry.processing.ST_IntersectWindow;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Invalid;
import org.neo4j.gis.spatial.query.geometry.processing.ST_LongestLine;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Overlap;
import org.neo4j.gis.spatial.query.geometry.processing.ST_PointsWithinOrthodromicDistance;
import org.neo4j.gis.spatial.query.geometry.processing.ST_ShortestLine;
import org.neo4j.gis.spatial.query.geometry.processing.ST_SymDifference;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Touch;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Within;
import org.neo4j.gis.spatial.query.geometry.processing.ST_WithinDistance;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * This unit test testing all available geoprocessing queries: 
 * 
 * 
 * @author Andreas Wilhelm
 * 
 */
public class TestSearchGeoprocessing extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private Layer layer = null;
	private boolean debug = true;

	private WKTReader wktReader = new WKTReader();

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
	
	public void testContain() throws Exception {
		Search select = new ST_Contain(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		if (debug) {
			printTestResults("ST_Contain", select.getResults());
		}
	}

	public void testCover() throws Exception {

		Search select = new ST_Cover(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(select.getResults().size(), 1);
		if (debug) {
			printTestResults("ST_Contain", select.getResults());
		}
	}

	public void testCoverBy() throws Exception {

		Search select = new ST_CoveredBy(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		if (debug) {
			printTestResults("ST_CoveredBy", select.getResults());
		}
	}

	public void testDisjoint() throws Exception {

		Search select = new ST_Disjoint(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		if (debug) {
			printTestResults("ST_Disjoint", select.getResults());
		}
	}

	public void testEmpty() throws Exception {

		Search select = new ST_Empty();
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("ST_Empty", select.getResults());
		}
	}

	public void testEqual() throws Exception {

		Search select = new ST_Equal(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		if (debug) {
			printTestResults("ST_Equal", select.getResults());
		}
	}

	public void testClosest() throws Exception {
		Search select = new ST_Closest(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		if (debug) {
			printTestResults("testClosest", select.getResults());
		}
	}

	public void testIntersect() throws Exception {
		Search select = new ST_Intersect(wktReader.read(Dataset.wkt));
		layer.execute(select);
		if (debug) {
			printTestResults("testIntersect", select.getResults());
		}
	}

	public void testInvalid() throws Exception {
		Search select = new ST_Invalid();
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("testInvalid", select.getResults());
		}
	}

	public void testOverlap() throws Exception {
		Search select = new ST_Overlap(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("testOverlap", select.getResults());
		}
	}

	public void testTouch() throws Exception {
		Search select = new ST_Touch(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("testTouch", select.getResults());
		}
	}

	public void testWithin() throws Exception {
		Search select = new ST_Within(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		if (debug) {
			printTestResults("testWithin", select.getResults());
		}
	}

	public void testWithinDistance() throws Exception {

		Coordinate coordinate = new Coordinate(12.9639158, 56.070904);
		Point point = new GeometryFactory().createPoint(coordinate);
		Search select = new ST_WithinDistance(point, 20d);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		if (debug) {
			printTestResults("testWithinDistance", select.getResults());
		}
	}

	public void testCross() throws Exception {

		Search select = new ST_Cross(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("testCross", select.getResults());
		}
	}

	public void testPointsWithinOrthodromicDistance() throws Exception {
		Coordinate coord = new Coordinate(12, 56);
		Search select = new ST_PointsWithinOrthodromicDistance(coord, 32);
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("testPointsWithinOrthodromicDistance", select.getResults());
		}
	}

	public void testIntersectWindow() throws Exception {
		Envelope envelope = new Envelope(20, 23, 70, 72);
		Search select = new ST_IntersectWindow(envelope);
		layer.execute(select);
		assertEquals(0, select.getResults().size());
		if (debug) {
			printTestResults("testIntersectWindow", select.getResults());
		}
	}

	public void testInRelation() throws Exception {
		// T*F**FFF* -> Equals
		Search select = new ST_InRelation(wktReader.read(Dataset.wkt),
				"T*F**FFF*");
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		assertEquals("LINESTRING (12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9678958 56.0705812, 12.9680173 56.0704885)", select.getResults().get(0).getResult().toString());
		if (debug) {
			printTestResults("testInRelation", select.getResults());
		}
	}
	
	public void testCentroid() throws Exception {
		Search select = new ST_Centroid();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("POINT (12.974217017261186 56.055761171718636)", select.getResults().get(0).getResult().toString());
		assertEquals("POINT (12.965978732527317 56.07101434783782)", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testCentroid", select.getResults());
		}
	}
	
	public void testDifference() throws Exception {
		Search select = new ST_Difference(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531)", select.getResults().get(0).getResult().toString());
		assertEquals("GEOMETRYCOLLECTION EMPTY", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testDifference", select.getResults());
		}
	}
	
	public void testSymDifference() throws Exception {
		Search select = new ST_SymDifference(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("MULTILINESTRING ((12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531), (12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9678958 56.0705812, 12.9680173 56.0704885))", select.getResults().get(0).getResult().toString());
		assertEquals("GEOMETRYCOLLECTION EMPTY", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testSymDifference", select.getResults());
		}
	}
	
	public void testDelaunayTriangle() throws Exception {
		Search select = new ST_DelaunayTriangle(0.5);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("GEOMETRYCOLLECTION EMPTY", select.getResults().get(0).getResult().toString());
		assertEquals("GEOMETRYCOLLECTION EMPTY", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testDelaunayTriangle", select.getResults());
		}
	}
	
	public void testConvexHull() throws Exception {
		Search select = new ST_ConvexHull();
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("POLYGON ((12.9710302 56.0538436, 12.9762034 56.0583531, 12.9762427 56.058262, 12.9762985 56.0581325, 12.9763764 56.0577353, 12.9763358 56.057183, 12.9759293 56.0564416, 12.9757125 56.056313, 12.9710302 56.0538436))", select.getResults().get(0).getResult().toString());
		assertEquals("POLYGON ((12.9680173 56.0704885, 12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9680173 56.0704885))", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testConvexHull", select.getResults());
		}
	}

	public void testShortestLine() throws Exception {
		Search select = new ST_ShortestLine(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("LINESTRING (12.9762034 56.0583531, 12.9680173 56.0704885)", select.getResults().get(0).getResult().toString());
		assertEquals("LINESTRING (12.9639158 56.070904, 12.9639158 56.070904)", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testShortestLine", select.getResults());
		}
	}
	
	public void testDistanceInMeter() throws Exception {
		Geometry geom = wktReader.read(Dataset.wkt);
		geom.setSRID(4326);
		Search select = new ST_DistanceInMeters(geom);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("1444.2025387102333", select.getResults().get(0).getResult().toString());
		if (debug) {
			printTestResults("testDistanceInMeter", select.getResults());
		}
	}
	
	public void testDistanceInMiles() throws Exception {
		Geometry geom = wktReader.read(Dataset.wkt);
		geom.setSRID(4326);
		Search select = new ST_DistanceInMiles(geom);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("0.8973855756809164", select.getResults().get(0).getResult().toString());
		if (debug) {
			printTestResults("testDistanceInMiles", select.getResults());
		}
	}
	
	public void testMaxDistance() throws Exception {
		Search select = new ST_MaxDistance(wktReader.read(Dataset.wkt));
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("0.018859283476578264", select.getResults().get(0).getResult().toString());
		assertEquals("0.004122492268032626", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testMaxDistance", select.getResults());
		}
	}
	
	public void testMaxDistanceInMeters() throws Exception {
		Geometry geom = wktReader.read(Dataset.wkt);
		geom.setSRID(4326);
		Search select = new ST_MaxDistanceInMeter(geom);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("2062.4682606010447", select.getResults().get(0).getResult().toString());
		assertEquals("455.98375501011816", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testMaxDistanceInMeters", select.getResults());
		}
	}
	
	
	public void testMaxDistanceInMiles() throws Exception {
		Geometry geom = wktReader.read(Dataset.wkt);
		geom.setSRID(4326);
		Search select = new ST_MaxDistanceInMiles(geom);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("1.2815579655579317", select.getResults().get(0).getResult().toString());
		assertEquals("0.28333508183439216", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testMaxDistanceInMiles", select.getResults());
		}
	}
	
	public void testLongestLine() throws Exception {
		Geometry geom = wktReader.read(Dataset.wkt);
		geom.setSRID(4326);
		Search select = new ST_LongestLine(geom);
		layer.execute(select);
		assertEquals(2, select.getResults().size());
		assertEquals("LINESTRING (12.9759293 56.0564416, 12.9639658 56.0710206)", select.getResults().get(0).getResult().toString());
		assertEquals("LINESTRING (12.9639158 56.070904, 12.9680173 56.0704885)", select.getResults().get(1).getResult().toString());
		if (debug) {
			printTestResults("testLongestLine", select.getResults());
		}
	}
	
	public void testCollect() throws Exception {
		Search select = new ST_Collect();
		layer.execute(select);
		assertEquals(1, select.getResults().size());
		assertEquals("GEOMETRYCOLLECTION (LINESTRING (12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531), LINESTRING (12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9678958 56.0705812, 12.9680173 56.0704885))", select.getResults().get(0).getResult().toString());
		if (debug) {
			printTestResults("testCollect", select.getResults());
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
	

	private void printTestResults(String mode,
			List<SpatialDatabaseRecord> results) {
		System.out.println("----------------------  " + mode
				+ "  -------------------");
		for (SpatialDatabaseRecord spatialDatabaseRecord : results) {
			System.out.println(spatialDatabaseRecord.getResult());
		}
		System.out.println("------------------------------------------------");
	}

}