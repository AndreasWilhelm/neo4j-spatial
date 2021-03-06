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
package org.neo4j.gis.spatial;

import java.io.File;

import org.neo4j.gis.spatial.SpatialDatabaseService;
import org.neo4j.gis.spatial.geometry.Dataset;
import org.neo4j.gis.spatial.operation.Delete;
import org.neo4j.gis.spatial.operation.restriction.RestrictionType;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.osm.OSMLayer;
import org.neo4j.gis.spatial.query.ST_Delete;

/**
 * Testcase to test the delete operation with and without restrictions.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_TestDelete extends Neo4jTestCase {

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

	public void testDelete() throws Exception {
		Delete delete = new ST_Delete();
		int count = layer.execute(delete);
		assertEquals(2, count);
	}

	public void testDeleteASingleNode() throws Exception {
		Delete delete = new ST_Delete(55l);
		int count = layer.execute(delete);
		assertEquals(1, count);
	}

	public void testDeleteWithKeyRestriction() throws Exception {
		Delete delete = new ST_Delete();
		delete.addRestriction(RestrictionType.HAS_PROPERTY, "vertices");
		int count = layer.execute(delete);
		assertEquals(0, count);
	}

	public void testDeleteWithKeyValueRestriction() throws Exception {
		Delete delete = new ST_Delete();
		delete.addRestriction(RestrictionType.EQUAL_TO, "vertices=8");
		int count = layer.execute(delete);
		assertEquals(1, count);
	}

	private void loadTestOsmData(String layerName, int commitInterval)
			throws Exception {
		String osmPath = Dataset.OSM_DIR + File.separator + layerName;
		if (this.debug) {
			System.out.println("\n=== Loading layer " + layerName + " from "
					+ osmPath + " ===");
		}
		reActivateDatabase(false, true, false);
		OSMImporter importer = new OSMImporter(layerName);
		importer.importFile(getBatchInserter(), osmPath);
		reActivateDatabase(false, false, false);
		importer.reIndex(graphDb(), commitInterval);
	}

}
