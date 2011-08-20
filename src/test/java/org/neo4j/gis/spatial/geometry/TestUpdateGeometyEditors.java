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

import org.geotools.referencing.CRS;
import org.junit.Test;
import org.neo4j.gis.spatial.Neo4jTestCase;
import org.neo4j.gis.spatial.SpatialDatabaseRecord;
import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.operation.Update;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.osm.OSMLayer;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Reverse;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Simplify;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Transform;
import org.neo4j.gis.spatial.query.geometry.editors.ST_Union;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

/**
 * This unit test testing all available geometry editor queries: 
 * 	- ST_Transform
 *  - ST_Reverse
 *  - ST_Union
 *  - ST_Simplify
 * 
 * @author Andreas Wilhelm
 * 
 */
public class TestUpdateGeometyEditors extends Neo4jTestCase {

	private WKTReader wktReader = new WKTReader();
	private SpatialDatabaseService spatialService = null;
	private OSMLayer layer = null;
	private boolean debug = true;
	
	protected void setUp(boolean deleteDb, boolean useBatchInserter,
			boolean autoTx) throws Exception {
		super.setUp(false, true, false);
		try {
			this.loadTestOsmData(Dataset.LAYER_NAME, Dataset.COMMIT_INTERVAL);
			this.spatialService = new SpatialDatabaseService(graphDb());
			this.layer = (OSMLayer) spatialService.getLayer(Dataset.LAYER_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testTransformUpdate() throws Exception {
		CoordinateReferenceSystem crs = CRS.decode("EPSG:2002");
		Update update = new ST_Transform(crs);
		List<SpatialDatabaseRecord> records = layer.execute(update);
		
		String updatedWKT = "LINESTRING (4214724.387523849 8880923.057915369, 4214648.5268391855 8881065.598204594, 4214645.579287704 8881071.128362853, 4214602.863938585 8881152.560950926, 4214590.79019744 8881175.568857124, 4214537.852061339 8881259.48607473, 4214507.109029424 8881341.783257904, 4214495.300119692 8881361.733703097, 4214456.54268533 8881386.346124297, 4214446.579885175 8881393.61100593, 4214411.8652233295 8881418.92081886, 4214372.929042781 8881431.486282624, 4214344.937774361 8881443.541860292, 4214294.855928422 8881453.90549495, 4214277.974253805 8881455.199932678, 4214266.0975965895 8881456.107396355)";
		Node node = layer.getIndex().get(55l).getGeomNode();
		Geometry updatedGeometry = layer.getGeometryEncoder().decodeGeometry(node);
	
		assertEquals(2, records.size());
		assertEquals(updatedWKT, updatedGeometry.toString());
		if (debug) {
			printTestResults("testTransformUpdate", records);
		}
	}
	
	@Test
	public void testTransformUpdate2() throws Exception {
		Update update = new ST_Transform(Dataset.WORLD_MERCATOR_SRID);
		List<SpatialDatabaseRecord> records = layer.execute(update);
		assertEquals(2, records.size());
		if (debug) {
			printTestResults("testTransformUpdate2", records);
		}
	}
	
	@Test
	public void testReverseUpdate() throws Exception {
		Update update = new ST_Reverse();
		List<SpatialDatabaseRecord> records = layer.execute(update);
		assertEquals(2, records.size());
		if (debug) {
			printTestResults("testReverseUpdate", records);
		}
	}
	
	@Test
	public void testSimplify() throws Exception {
		Update update = new ST_Simplify();
		List<SpatialDatabaseRecord> records = layer.execute(update);
		assertEquals(2, records.size());
		assertEquals(Dataset.wkt, records.get(1).getGeometry().toText());
		if (debug) {
			printTestResults("testSimplify", records);
		}
	}
	/*
	@Test
	public void testUnion() throws Exception {
		Update update = new ST_Union(wktReader.read(Dataset.wkt2));
		List<SpatialDatabaseRecord> records = layer.execute(update);
		assertEquals(2, records.size());
		String expected = "MULTILINESTRING ((12.9710302 56.0538436, 12.9726158 56.0546985, 12.9726773 56.0547317, 12.9735859 56.0552154, 12.9738426 56.0553521, 12.9747403 56.0559176, 12.9757125 56.056313, 12.9759293 56.0564416, 12.9760919 56.0567821, 12.9761463 56.0568715, 12.9763358 56.057183, 12.9763358 56.0575008, 12.9763764 56.0577353, 12.9762985 56.0581325, 12.9762427 56.058262, 12.9762034 56.0583531), (13.9639158 56.070904, 13.9639658 56.0710206, 13.9654342 56.0711966))";
		assertEquals(expected, records.get(0).getGeometry().toString());
		if (debug) {
			printTestResults("testUnion", records);
		}
	}
	*/
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