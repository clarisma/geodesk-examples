/*
 * Copyright (c) Clarisma / GeoDesk contributors
 *
 * This source code is licensed under the Apache 2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.geodesk.examples;

import com.geodesk.feature.Feature;
import com.geodesk.feature.FeatureLibrary;
import com.geodesk.feature.Features;
import com.geodesk.geom.Box;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * A simple quality-assurance application that retrieves all the soccer fields
 * in a GOL and measures their areas. Its output is a list of all areas that are
 * tagged as soccer fields, but are suspiciously small or large.
 *
 * An area less than 300 m2 is likely a smaller kind of pitch, or another
 * kind of object altogether.
 *
 * Areas larger than 5,000 m2 are often entire sports complexes that include
 * multiple individual soccer fields; these should be re-tagged as
 * "leisure=sports_centre".
 *
 */
public class SoccerFields
{
    static final String GEODESK_PATH =  "d:\\geodesk\\tests\\";
    static final String GOL_FILE =      "de.gol";

    static FeatureLibrary features;

    private static class AreaFeature
    {
        public final Feature feature;
        public final double area;

        public AreaFeature(Feature f, double a)
        {
            feature = f;
            area = a;
        }
    }

    public static void main(String[] args)
    {
        features = Features.open(GEODESK_PATH + GOL_FILE);

        double totalArea = 0;
        long totalCount = 0;
        double smallestArea = Double.MAX_VALUE;
        Feature smallest = null;
        double largestArea = Double.MIN_VALUE;
        Feature largest = null;
        List<AreaFeature> tooSmall = new ArrayList<>();
        List<AreaFeature> tooBig = new ArrayList<>();

        final double MIN_AREA = 300;        // square meters
        final double MAX_AREA = 5_000;      // square meters

        Box box = Box.ofWorld();
        for (Feature f: features.select("a[leisure=pitch][sport=soccer]").in(box))
        {
            double area = f.area();
            totalArea += area;
            totalCount++;
            if(area < smallestArea)
            {
                smallestArea = area;
                smallest = f;
            }
            if(area > largestArea)
            {
                largestArea = area;
                largest = f;
            }
            if(area < MIN_AREA) tooSmall.add(new AreaFeature(f, area));
            if(area > MAX_AREA) tooBig.add(new AreaFeature(f, area));
        }
        out.format("%d soccer fields (avg: %f m2)\n", totalCount, totalArea / totalCount);
        out.format("Smallest: %s (%f m2)\n", smallest, smallestArea);
        out.format("Largest:  %s (%f m2)\n", largest, largestArea);

        tooSmall.sort((a,b) -> Double.compare(a.area, b.area));
        tooBig.sort((a,b) -> Double.compare(b.area, a.area));

        out.println("\nToo small:");
        tooSmall.forEach(af -> out.format("https://www.openstreetmap.org/%s: %f m2\n", af.feature, af.area));
        out.println("\nToo big:");
        tooBig.forEach(af -> out.format("https://www.openstreetmap.org/%s: %f m2\n", af.feature, af.area));

        features.close();
    }
}
