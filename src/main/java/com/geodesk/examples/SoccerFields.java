package com.geodesk.examples;

import com.geodesk.feature.Feature;
import com.geodesk.feature.FeatureLibrary;
import com.geodesk.core.Box;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class SoccerFields
{
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
        features = new FeatureLibrary("c:\\geodesk\\tests\\france.gol");
        double totalArea = 0;
        long totalCount = 0;
        double smallestArea = Double.MAX_VALUE;
        Feature smallest = null;
        double largestArea = Double.MIN_VALUE;
        Feature largest = null;
        List<AreaFeature> tooSmall = new ArrayList<>();
        List<AreaFeature> tooBig = new ArrayList<>();
        final double MIN_AREA = 300;
        final double MAX_AREA = 5_000;

        Box box = Box.ofWorld();
        for (Feature f: features.features("a[leisure=pitch][sport=tennis]").in(box))
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
    }
}
