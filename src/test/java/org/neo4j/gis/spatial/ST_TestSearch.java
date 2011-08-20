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
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.neo4j.gis.spatial.geometry.Dataset;
import org.neo4j.gis.spatial.operation.Search;
import org.neo4j.gis.spatial.osm.OSMImporter;
import org.neo4j.gis.spatial.osm.OSMLayer;
import org.neo4j.gis.spatial.query.SearchAll;
import org.neo4j.gis.spatial.query.geometry.outputs.ST_Geometry;

/**
 * A simple testcase to test the search operation.
 * 
 * @author Andreas Wilhelm
 * 
 */
public class ST_TestSearch extends Neo4jTestCase {

	private SpatialDatabaseService spatialService = null;
	private OSMLayer layer = null;
	private boolean debug = false;

	protected void setUp(boolean deleteDb, boolean useBatchInserter,
			boolean autoTx) throws Exception {
		super.setUp(false, true, false);
		try {
			this.loadTestOsmData(Dataset.KARLSRUHE, Dataset.COMMIT_INTERVAL);
			this.spatialService = new SpatialDatabaseService(graphDb());
			this.layer = (OSMLayer) spatialService.getLayer(Dataset.KARLSRUHE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testSearch() throws Exception {
		Search search = new ST_Geometry();
		search.setThreadPoolSize(8);
		layer.execute(search);
		assertEquals(21912, search.getResults().size());
	}

	private void loadTestOsmData(String layerName, int commitInterval)
			throws Exception {
		String osmPath = Dataset.OSM_DIR + File.separator + layerName;
		if (debug) {
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
