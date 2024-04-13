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

import static java.lang.System.out;

public class Railways
{
    static FeatureLibrary features;

    public static void main(String[] args)
    {
        features = new FeatureLibrary("c:\\geodesk\\tests\\switzerland.gol");

        Features stations = features.select("na[railway=station]");

        for(Feature station: stations)
        {
            out.format("%s: %s - %s\n", station, station.tag("name"), station.belongsToRelation());
        }
    }
}
