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
import org.neo4j.gis.spatial.query.geometry.processing.ST_Closest;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Contain;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Cover;
import org.neo4j.gis.spatial.query.geometry.processing.ST_CoveredBy;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Cross;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Disjoint;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Empty;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Equal;
import org.neo4j.gis.spatial.query.geometry.processing.ST_InRelation;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Intersect;
import org.neo4j.gis.spatial.query.geometry.processing.ST_IntersectWindow;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Invalid;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Overlap;
import org.neo4j.gis.spatial.query.geometry.processing.ST_PointsWithinOrthodromicDistance;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Simplify;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Touch;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Union;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Within;
import org.neo4j.gis.spatial.query.geometry.processing.ST_WithinDistance;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author Andreas Wilhelm
 * 
 */
public class TestSearchGeoprocessing extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private Layer layer = null;
	private boolean debug = true;
	private String wkt = "LINESTRING (12.9639158 56.070904, 12.9639658 56.0710206, 12.9654342 56.0711966, 12.9666335 56.0710678, 12.9674023 56.0708619, 12.9677867 56.0706645, 12.9678958 56.0705812, 12.9680173 56.0704885)";
	private String wkt2 = "LINESTRING (13.9639158 56.070904, 13.9639658 56.0710206, 13.9654342 56.0711966)";

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

	public void testSimplify() throws Exception {

		Select select = new ST_Simplify();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(wkt, results.get(1).getGeometry().toText());
		if (debug) {
			printTestResults("testSimplify", results);
		}
	}

	public void testContain() throws Exception {
		Select select = new ST_Contain(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("ST_Contain", results);
		}
	}

	public void testCover() throws Exception {

		Select select = new ST_Cover(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(results.size(), 1);
		if (debug) {
			printTestResults("ST_Contain", results);
		}
	}

	public void testCoverBy() throws Exception {

		Select select = new ST_CoveredBy(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("ST_CoveredBy", results);
		}
	}

	public void testDisjoint() throws Exception {

		Select select = new ST_Disjoint(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("ST_Disjoint", results);
		}
	}

	public void testEmpty() throws Exception {

		Select select = new ST_Empty();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("ST_Empty", results);
		}
	}

	public void testEqual() throws Exception {

		Select select = new ST_Equal(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("ST_Equal", results);
		}
	}

	public void testClosest() throws Exception {
		Select select = new ST_Closest(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		if (debug) {
			printTestResults("testClosest", results);
		}
	}

	public void testIntersect() throws Exception {
		Select select = new ST_Intersect(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		if (debug) {
			printTestResults("testIntersect", results);
		}
	}

	public void testInvalid() throws Exception {
		Select select = new ST_Invalid();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("testInvalid", results);
		}
	}

	public void testOverlap() throws Exception {
		Select select = new ST_Overlap(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("testOverlap", results);
		}
	}

	public void testTouch() throws Exception {
		Select select = new ST_Touch(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("testTouch", results);
		}
	}

	public void testWithin() throws Exception {
		Select select = new ST_Within(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("testWithin", results);
		}
	}

	public void testWithinDistance() throws Exception {

		Coordinate coordinate = new Coordinate(12.9639158, 56.070904);
		Point point = new GeometryFactory().createPoint(coordinate);
		Select select = new ST_WithinDistance(point, 20d);
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testWithinDistance", results);
		}
	}

	public void testCross() throws Exception {

		Select select = new ST_Cross(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("testCross", results);
		}
	}

	public void testPointsWithinOrthodromicDistance() throws Exception {
		Coordinate coord = new Coordinate(12, 56);
		Select select = new ST_PointsWithinOrthodromicDistance(coord, 32);
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("testPointsWithinOrthodromicDistance", results);
		}
	}

	public void testIntersectWindow() throws Exception {
		Envelope envelope = new Envelope(20, 23, 70, 72);
		Select select = new ST_IntersectWindow(envelope);
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("testIntersectWindow", results);
		}
	}

	public void testInRelation() throws Exception {
		//T*F**FFF* -> Equals
		Select select = new ST_InRelation(wktReader.read(wkt), "T*F**FFF*");
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("testInRelation", results);
		}
	}

	public void testUnion() throws Exception {
		Select select = new ST_Union(wktReader.read(wkt2));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());

		String expected = "MULTILINESTRING ((12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531), (13.9639158 56.070904, 13.9639658 56.0710206, 13.9654342 56.0711966))";
		assertEquals(expected, results.get(0).getResult().toString());
		if (debug) {
			printTestResults("testUnion", results);
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
			System.out.println(spatialDatabaseRecord.getGeometry().toText());
		}
		System.out.println("------------------------------------------------");
	}

}