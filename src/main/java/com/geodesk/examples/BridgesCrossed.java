package com.geodesk.examples;

import com.clarisma.common.text.Format;
import com.geodesk.feature.*;

import java.util.*;

import static com.geodesk.feature.Filters.*;

import static java.lang.System.out;


/**
 * This example demonstrates how a spatial operation performs much faster when
 * applied to each relation member individually.
 *
 * Here, we'll query all bridges that cross a river relation. We first apply
 * the "crosses" predicate against the entire relation. Then, we iterate the
 * individual segments and query bridges that cross just this segment. Unless
 * a river runs straight vertically or horizontally, the total area of the
 * bounding boxes of its members is significantly smaller than the relation's
 * bbox, so there are far fewer candidates to consider.
 *
 * Future versions of the query engine might choose this strategy automatically.
 *
 */
public class BridgesCrossed
{
    static final String GEODESK_PATH =  "c:\\geodesk\\tests\\";
    static final String GOL_FILE =      "de.gol";

    static FeatureLibrary features;

    /**
     * Gets the name of a feature, using a list of keys. Tries all keys
     * and returns the first that returns a value.
     *
     * @param f         the feature
     * @param keys      list of keys for name tags
     * @return          the name of the feature, or "(unnamed)" if the
     *                  feature has none of the listed tags
     */
    static String name(Feature f, String... keys)
    {
        for(String k: keys)
        {
            String v = f.stringValue(k);
            if(!v.isEmpty()) return v;
        }
        return "(unnamed)";
    }

    static void findBridges(Relation river, Features<Way> bridges)
    {
        long start = System.currentTimeMillis();
        List<Way> results = bridges.select(crosses(river)).toList();
        long end = System.currentTimeMillis();

        printBridges(results);
        out.format("\nFound %d bridges that cross river %s in %s\n\n",
            results.size(), name(river, "name:en", "name"),
            Format.formatTimespan(end - start));
    }

    static void printBridges(List<Way> bridges)
    {
        bridges.sort(Comparator.comparing(Feature::id));
        for(Way bridge: bridges)
        {
            out.format("%s: %s\n", bridge, name(bridge, "bridge:name", "name"));
        }
    }


    static void findBridgesFaster(Relation river, Features<Way> bridges)
    {
        long start = System.currentTimeMillis();

        // We'll use a Set instead of a List here, since bridges may cross
        // more than one segment; the Set de-duplicates the results for us

        Set<Way> results = new HashSet<>();
        for(Way segment: river.memberWays())
        {
            for (Way bridge : bridges.select(crosses(segment)))
            {
                results.add(bridge);
            }
        }
        long end = System.currentTimeMillis();

        printBridges(new ArrayList<>(results));
        out.format("\nFound %d bridges that cross segments of river %s in %s\n\n",
            results.size(), name(river, "name:en", "name"), Format.formatTimespan(
                end - start));
    }

    public static void main(String[] args)
    {
        features = new FeatureLibrary(GEODESK_PATH + GOL_FILE);

        final String RIVER_NAME_EN = "Rhine";

        Relation river = features.relations(
            String.format("r[waterway=river][name:en=%s]", RIVER_NAME_EN))
            .first();

        // Collection of road bridges
        Features<Way> bridges = features.ways(
            "w[highway][highway != path,pedestrian,cycleway][bridge]");

        // Perform multiple runs, so cache warmup and compiler optimizations
        // have a chance to take effect

        for(int run=0; run<3; run++)
        {
            findBridges(river, bridges);
            findBridgesFaster(river, bridges);
        }

        features.close();
    }

}
