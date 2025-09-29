/*
 * Copyright (c) Clarisma / GeoDesk contributors
 *
 * This source code is licensed under the Apache 2.0 license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.geodesk.examples;

import com.geodesk.feature.Features;
import com.geodesk.geom.Box;
import com.geodesk.geom.Mercator;
import com.geodesk.feature.Feature;
import com.geodesk.feature.FeatureLibrary;

import static java.lang.System.out;

/**
 * An example that retrieves a list of hotels in a given bounding box,
 * and finds nearby pubs.
 */
public class HotelFinder
{
    static final String GEODESK_PATH =  "d:\\geodesk\\tests\\";
    static final String GOL_FILE =      "de.gol";

    static FeatureLibrary features;

    public static void main(String[] args)
    {
        features = Features.open(GEODESK_PATH + GOL_FILE);

        // City center of Heidelberg, Germany
        Box bbox = Box.ofWSEN(8.67, 49.40, 8.73, 49.42);

        int mostChoices = 0;
        int bestShortestDistance = 10000;
        Feature bestHotel = null;
        Feature secondBestHotel = null;
        Feature topClosestPub = null;
        int hotelCount = 0;

        // "na[tourism=hotel,guest_house,hostel][phone][website]"
        for (Feature hotel : features.select(
            "na[tourism=hotel][phone][website]").in(bbox))
        {
            hotelCount++;
            out.printf("- %s, tel. %s\n", hotel.tag("name"), hotel.tag("phone"));
            Box pubSearchBox = hotel.bounds();
            double maxPubDistance = 250;
            pubSearchBox.bufferMeters(maxPubDistance);
            int pubCount = 0;
            int shortestDistance = 10000;
            Feature closestPub = null;
            for (Feature pub : features.select("na[amenity=pub]").in(pubSearchBox))
            {
                int dist = (int) (Mercator.distance(hotel.x(), hotel.y(), pub.x(), pub.y()) + 0.5);
                if (dist <= maxPubDistance)
                {
                    out.printf("  - %d meters from %s\n", dist, pub.tag("name"));
                    if (dist < shortestDistance)
                    {
                        shortestDistance = dist;
                        closestPub = pub;
                    }
                    pubCount++;
                }
            }
            if (pubCount < mostChoices) continue;
            if (pubCount == mostChoices)
            {
                if (shortestDistance > bestShortestDistance) continue;
            }

            mostChoices = pubCount;
            bestShortestDistance = shortestDistance;
            topClosestPub = closestPub;
            secondBestHotel = bestHotel;
            bestHotel = hotel;
        }
        if (bestHotel != null)
        {
            out.printf("\nFound %d hotels.\n", hotelCount);
            out.printf("I recommend %s, since there are %d pubs nearby.\n",
                bestHotel.tag("name"), mostChoices);
            out.printf("(%s is only %d meters away.)\n",
                topClosestPub.tag("name"), bestShortestDistance);
            if (secondBestHotel != null)
            {
                out.printf("If that is booked, try %s\n", secondBestHotel.tag("name"));
            }
        }

        features.close();
    }

}
