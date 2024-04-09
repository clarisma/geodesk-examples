/*
 * Copyright (c) Clarisma / GeoDesk contributors
 *
 * This source code is licensed under the Apache 2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.geodesk.examples;

import com.geodesk.feature.Feature;
import com.geodesk.feature.FeatureLibrary;
import com.geodesk.geom.Box;

import static java.lang.System.out;

/**
 * A very simple quality-assurance application that retrieves generic objects
 * that have "name" tag. Some OSM contributors have a habit to assign a
 * "name" to things like waste baskets, park benches, water fountains, etc.
 * which often is simply a description of the object itself.
 *
 * This application lists such objects so these names can be identified and
 * removed if necessary.
 *
 */
public class NamedAmenities
{
    static final String GEODESK_PATH =  "c:\\geodesk\\tests\\";
    static final String GOL_FILE =      "switzerland.gol";
    static final String TILESET_URL =   "http://data.geodesk.com/switzerland";

    static FeatureLibrary features;

    public static void main(String[] args)
    {
        features = new FeatureLibrary(GEODESK_PATH + GOL_FILE, TILESET_URL);

        Box box = Box.ofWorld();
        for (Feature f: features.select("n[amenity=waste_basket,drinking_water,bench][name]").in(box))
        {
            out.format("%s: %s %s\n", f, f.id(), f.tag("amenity"), f.tag("name"));
        }
    }

}
