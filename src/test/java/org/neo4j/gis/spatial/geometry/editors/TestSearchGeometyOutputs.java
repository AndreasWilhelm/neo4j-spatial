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
import org.neo4j.gis.spatial.query.geometry.outputs.ST_Area;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_AsGML;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_AsGeoJSON;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_AsKML;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_Buffer;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_Distance;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_Length;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_LengthInMeters;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_LengthInMiles;
import com.vividsolutions.jts.io.WKTReader;

/**
 * This unit test testing all available geometry output queries: 
 * 	- ST_AsGML
 *  - ST_AsGeoJSON
 *  - ST_AsKML
 * 
 * @author Andreas Wilhelm
 * 
 */
public class TestSearchGeometyOutputs extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private Layer layer = null;
	private boolean debug = true;
	private String wkt = "LINESTRING (22.9639158 86.070904, 22.9639658 86.0710206, 22.9654342 86.0711966)";
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
	
	public void testLengthInMeters() throws Exception {

		Select select = new ST_LengthInMeters();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(results.size(), 2);
		assertEquals(639.2762070150728, results.get(0).getResult());
		assertEquals(292.8106275851124, results.get(1).getResult());
		if (debug) {
			printTestResults("testLengthInMeters", results);
		}
	}
	
	public void testLengthInMiles() throws Exception {

		Select select = new ST_LengthInMiles();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(results.size(), 2);
		assertEquals(0.3972276960291628, results.get(0).getResult());
		assertEquals(0.18194403247318888, results.get(1).getResult());
		if (debug) {
			printTestResults("testLengthInMiles", results);
		}
	}
	
	public void testAsGML() throws Exception {
		Select select = new ST_AsGML();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testAsGML", results);
		}
	}
	
	public void testAsKML() throws Exception {
		Select select = new ST_AsKML();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testAsKML", results);
		}
	}
	
	public void testAsGeoJSON() throws Exception {
		Select select = new ST_AsGeoJSON();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testAsGeoJSON", results);
		}
	}
	
	public void testAsDistance() throws Exception {
		Select select = new ST_Distance(wktReader.read(wkt));
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(31.63080161030075, results.get(0).getResult());
		assertEquals(31.62171418752397, results.get(1).getResult());
		if (debug) {
			printTestResults("testAsDistance", results);
		}
	}
	
	
	public void testAsLength() throws Exception {
		Select select = new ST_Length();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals(0.007601378785773344, results.get(0).getResult());
		assertEquals(0.004330082368743335, results.get(1).getResult());
		if (debug) {
			printTestResults("testAsLength", results);
		}
	}
	
	public void testAsArea() throws Exception {
		Select select = new ST_Area();
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		if (debug) {
			printTestResults("testAsArea", results);
		}
	}
	
	public void testBuffer()  {
		Select select = new ST_Buffer(1);
		List<SpatialDatabaseRecord> results = layer.execute(select);
		assertEquals(2, results.size());
		assertEquals("POLYGON ((12.262049686207407 56.75223776280409, 12.258997876500109 56.755233623501816, 12.262585476500108 56.75888822350181, 12.298326885040375 56.78877802963586, 12.333848415474367 56.824557148258194, 12.371138708989665 56.849669063061654, 12.412965067626965 56.88464758802811, 12.466622633041327 56.913969553614294, 12.496450439108079 56.9340561030649, 12.498036039108081 56.9349110030649, 12.499608234716872 56.93199502066076, 12.584989591999221 56.97865291877078, 12.772048243617716 57.03729164358817, 12.966952463404452 57.05831030917052, 13.16221219149657 57.04090117998692, 13.350323706046407 56.98573327910873, 13.524057987043964 56.894926678028185, 13.676738523501815 56.77197102349989, 13.802497888028112 56.62159143237303, 13.894302111058764 56.45359481263406, 13.894446725041327 56.45436977371557, 13.894671469129928 56.453848420290655, 13.895335475504641 56.451703813199224, 13.896503218770778 56.449566908000776, 13.955141943588169 56.26250825638228, 13.95579844487282 56.256420578257696, 13.957604016636276 56.25058894447306, 13.957681916636275 56.25019174447306, 13.95921400214205 56.224748418953006, 13.976160609170524 56.06760403659555, 13.97169351204537 56.01750134847026, 13.97368538456873 55.9844223454934, 13.97364478456873 55.983870045493404, 13.963961858579168 55.93078359610074, 13.95875147998692 55.87234430850343, 13.944338502530664 55.823198936370176, 13.935304900646635 55.773672393487686, 13.916725372849319 55.72904368371179, 13.90358357910873 55.68423279395359, 13.876489179736891 55.63239486655719, 13.853185200748753 55.5764178510899, 13.852778700748754 55.5756764510899, 13.825829993001344 55.53547198563563, 13.812776978028188 55.51049851295603, 13.790789578973738 55.48319559715419, 13.753823499945169 55.42804633132616, 13.709220793357689 55.38190732380189, 13.68982132349989 55.35781797649818, 13.690751324705651 55.36280169057234, 13.630297123051644 55.300265197197945, 13.48610137105682 55.19636924379176, 13.48588457105682 55.19624064379176, 13.447106106075811 55.17465191055824, 13.447195560891918 55.174485996935104, 13.445609960891918 55.173631096935104, 13.444251141017638 55.17306249542636, 13.438015245340392 55.16959084980258, 13.434918545340391 55.16797634980258, 13.434413133722336 55.16894575714704, 13.264770103194525 55.097958214992325, 13.072641985748602 55.05901947232869, 12.876608984360907 55.058311262987665, 12.684204537338193 55.095860803057064, 12.502822639677888 55.17022508477003, 12.339433695986937 55.27854633048432, 12.200316651741808 55.41666181547437, 12.090817696935105 55.57926383910808, 12.015144814992325 55.76010369680547, 11.97620607232869 55.9522318142514, 11.975497862987668 56.148264815639095, 12.01304740305706 56.340669262661805, 12.087411684770029 56.522051160322114, 12.195732930484313 56.68544010401306, 12.262049686207407 56.75223776280409))", results.get(0).getResult().toString());
		if (debug) {
			printTestResults("testBuffer", results);
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