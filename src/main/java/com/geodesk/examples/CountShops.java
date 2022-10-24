package com.geodesk.examples;

import com.clarisma.common.text.Format;
import com.geodesk.feature.Feature;
import com.geodesk.feature.FeatureLibrary;
import com.geodesk.feature.Features;

import static com.geodesk.feature.Filters.*;

import static java.lang.System.out;

/**
 * This example retrieves all administrative areas of level 6
 * (US counties; French départements; German landkreise; Italian provinces)
 * and tallies the number of shops in each.
 */
public class CountShops
{
    static final String GEODESK_PATH =  "c:\\geodesk\\tests\\";
    static final String GOL_FILE =      "de.gol";
    static final String TILESET_URL =   null;

    static FeatureLibrary features;

    public static void main(String[] args)
    {
        features = new FeatureLibrary(GEODESK_PATH + GOL_FILE, TILESET_URL);

        long start = System.currentTimeMillis();

        Features<?> counties = features.features("a[boundary=administrative][admin_level=6][name]");
        Features<?> shops = features.features("na[shop]");

        int countyCount = 0;
        long totalShopCount = 0;
        for(Feature county: counties)
        {
            countyCount++;
            long shopCount = shops.select(within(county)).count();
            totalShopCount += shopCount;
            out.format("%d. %s - %d shops\n", countyCount, county.stringValue("name"), shopCount);
        }
        out.format("\nTotal of %d shops in %d counties\n", totalShopCount, countyCount);
        out.format("Executed in %s ms\n", Format.formatTimespan(
            System.currentTimeMillis() - start));

        features.close();
    }
}
