package com.geodesk.examples;

import com.geodesk.feature.Feature;
import com.geodesk.feature.FeatureLibrary;
import com.geodesk.core.Box;

import java.nio.file.Path;

import static java.lang.System.out;

public class NamedAmenities
{
    static FeatureLibrary features;

    public static void main(String[] args)
    {
        out.println("Opening store...");
        long time = System.currentTimeMillis();
        features = new FeatureLibrary("c:\\geodesk\\tests\\de.gol");
        out.format("Store opened (%d ms).\n", System.currentTimeMillis()-time);

        Box box = Box.ofWorld();
        for (Feature f: features.features("n[amenity=waste_basket,drinking_water,bench][name]").in(box))
        {
            out.format("node/%d: %s %s\n", f.id(), f.tag("amenity"), f.tag("name"));
        }
    }

}
