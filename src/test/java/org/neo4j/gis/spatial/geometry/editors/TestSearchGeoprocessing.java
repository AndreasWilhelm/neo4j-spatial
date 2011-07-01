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
import org.neo4j.gis.spatial.query.geometry.processing.ST_Disjoint;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Empty;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Equal;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Intersect;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Invalid;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Overlap;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Simplify;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Touch;
import org.neo4j.gis.spatial.query.geometry.processing.ST_Within;
import org.neo4j.gis.spatial.query.geometry.processing.ST_WithinDistance;

import com.vividsolutions.jts.geom.Coordinate;
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
		assertEquals(results.size(), 32423);
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
			printTestResults("ST_Invalid", results);
		}
	}
	
	public void testOverlap() throws Exception {
		Select select = new ST_Overlap(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("ST_Overlap", results);
		}
	}
	
	public void testTouch() throws Exception {
		Select select = new ST_Touch(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(0, results.size());
		if (debug) {
			printTestResults("ST_Touch", results);
		}
	}

	public void testWithin() throws Exception {
		Select select = new ST_Within(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("ST_Within", results);
		}
	}
	
	public void testWithinDistance() throws Exception {

		Coordinate coordinate = new Coordinate(12.9639158, 56.070904);
		Point point = new GeometryFactory().createPoint(coordinate);
		Select select = new ST_WithinDistance(point, 20d);
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(1, results.size());
		if (debug) {
			printTestResults("ST_WithinDistance", results);
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