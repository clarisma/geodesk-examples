/*
 * Copyright (c) Clarisma / GeoDesk contributors
 *
 * This source code is licensed under the Apache 2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.geodesk.examples;

import com.clarisma.common.text.Format;
import com.geodesk.feature.*;

import java.util.List;

import static java.lang.System.out;

/**
 * This example displays a hierarchy of administrative areas, such as
 * country -> state -> county -> city.
 *
 */
public class AdminAreas
{
    static final String GEODESK_PATH =  "/home/md/geodesk/tests/"; // "c:\\geodesk\\tests\\";
    static final String GOL_FILE =      "de3.gol";
    static final String TILESET_URL =   null;

    static FeatureLibrary features;

    static final int MAX_LEVEL = 8;

    // Typical levels:
    //   2 = country
    //   4 = state
    //   6 = county (French département, German landkreis, Italian provincia)
    //   8 = city
    //  10 = village/suburb
    //
    // See https://wiki.openstreetmap.org/wiki/Tag:boundary%3Dadministrative

    static Features adminAreas(int level)
    {
        return features.select(String.format(
            "a[boundary=administrative][admin_level=%d][name]", level));
    }

    static void printAdminArea(Feature adminArea, String indent)
    {
        String name = adminArea.stringValue("name");
        int level = adminArea.intValue("admin_level");

        List<? extends Feature> subAreas = null;
        if(level < MAX_LEVEL)
        {
            Filter filter = Filters.within(adminArea);
                // Filter that select features that lie within an area

            for (int childLevel = level + 1; childLevel <= MAX_LEVEL; childLevel++)
            {
                subAreas = adminAreas(childLevel).select(filter).toList();
                if (!subAreas.isEmpty()) break;
            }
        }

        String subAreasString = (subAreas == null || subAreas.isEmpty()) ? "" :
            String.format (" (%d sub-areas)", subAreas.size());
        out.format("%s%d: %s%s\n", indent, level, name, subAreasString);

        if(subAreas != null)
        {
            String childIndent = indent + "  ";
            for (Feature subArea : subAreas) printAdminArea(subArea, childIndent);
        }
    }

    public static void main(String[] args)
    {
        features = new FeatureLibrary(GEODESK_PATH + GOL_FILE, TILESET_URL);

        long start = System.currentTimeMillis();

        Feature country = features.select(
            "a[boundary=administrative][admin_level=2][name:en=Germany]")
                .first();

        printAdminArea(country, "");

        out.format("\nExecuted in %s ms\n", Format.formatTimespan(
            System.currentTimeMillis() - start));

        features.close();
    }
}

