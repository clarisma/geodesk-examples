/*
 * Copyright (c) Clarisma / GeoDesk contributors
 *
 * This source code is licensed under the Apache 2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.geodesk.examples;

import com.clarisma.common.text.Format;
import com.clarisma.common.util.Log;
import com.geodesk.feature.Feature;
import com.geodesk.feature.FeatureLibrary;
import com.geodesk.feature.Features;

import java.util.HashSet;
import java.util.Set;

/**
 * This example finds features within a certain radius from a series of point
 * features (such as movie theaters near a bus stop).
 */
public class NearbyPOIs
{
    static final String GEODESK_PATH =  "c:\\geodesk\\tests\\";
    static final String GOL_FILE =      "de.gol";

    static FeatureLibrary features;

    /**
     * Finds all features within a given distance from other features.
     * (If a feature is within the radius of more than one feature
     * specified in `nearWhat`, it is only returned once).
     *
     * @param what          the GOQL query for the POIs to find
     * @param maxDistance   maximum distance (in meters) from ...
     * @param nearWhat      ... these points (GOQL query)
     */
    static void findNearby(String what, int maxDistance, String nearWhat)
    {
        long start = System.currentTimeMillis();
        Set<Feature> results = new HashSet<>();
        Features amenities = features.select(what);
        long totalCount = amenities.count();
        long pointCount = 0;
        for(Feature point: features.select(nearWhat))
        {
            for(Feature f: amenities
                .maxMetersFromXY(maxDistance, point.x(), point.y()))
            {
                results.add(f);
            }
            pointCount++;
        }
        long end = System.currentTimeMillis();

        Log.debug("Found %,d (of %,d) %s within %,d meters from %,d %s in %s",
            results.size(), totalCount, what, maxDistance, pointCount, nearWhat,
            Format.formatTimespan(end - start));
    }

    public static void main(String[] args)
    {
        features = new FeatureLibrary(GEODESK_PATH + GOL_FILE);

        for(int i=0; i<10; i++)
        {
            findNearby("na[amenity=restaurant,bar,pub,cafe]", 100,
                "n[amenity=telephone]");
        }

        features.close();
    }
}

