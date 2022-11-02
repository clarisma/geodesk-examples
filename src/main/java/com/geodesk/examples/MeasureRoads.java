/*
 * Copyright (c) Clarisma / GeoDesk contributors
 *
 * This source code is licensed under the Apache 2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.geodesk.examples;

import com.clarisma.common.text.Format;
import com.geodesk.core.Box;
import com.geodesk.feature.*;
import static com.geodesk.feature.Filters.*;

import static java.lang.System.out;

/**
 * This example measures the total length of the road network in a given
 * country and reports the percentage that is one-way.
 */
public class MeasureRoads
{
    static final String GEODESK_PATH =  "c:\\geodesk\\tests\\";
    static final String GOL_FILE =      "de.gol";
    static final String TILESET_URL =   null;

    static FeatureLibrary features;

    public static void main(String[] args)
    {
        features = new FeatureLibrary(GEODESK_PATH + GOL_FILE, TILESET_URL);

        long start = System.currentTimeMillis();

        final int adminLevel = 2;
        final String countryName = "Germany";
        final Box location = Box.atLonLat(9.9,50.7);
        final String roadType = "motorway";

        // Try:
        // - motorway
        // - primary
        // - residential
        // - cycleway
        // - path,pedestrian

        Feature country = features.select(String.format(
            "a[boundary=administrative][admin_level=%d][name:en=%s]",
            adminLevel, countryName))
            .in(location)
            .first();

        Features<Way> roads = features.ways(
            String.format("w[highway=%s]", roadType));

        long segmentCount = 0;
        double totalLength = 0;
        double totalOnewayLength = 0;
        for(Way road: roads.select(within(country)))
        {
            double roadLength = road.length();
            totalLength += roadLength;
            if(road.booleanValue("oneway")) totalOnewayLength += roadLength;
            segmentCount++;
        }

        out.format(
            "There are %.1f km of roads of type %s (%d segments) in %s.\n" +
            "%.1f%% of the total length is one-way.",
            totalLength / 1000, roadType, segmentCount, countryName,
            totalLength==0 ? 0 : (totalOnewayLength / totalLength * 100));
                // guard against division by zero if no road were found

        // Remember, major roads such as motorways are mapped as bi-directional
        // features, so the reported length will be double the official length

        out.format("\nExecuted in %s ms\n", Format.formatTimespan(
            System.currentTimeMillis() - start));

        features.close();
    }
}

