package com.geodesk.examples;

import com.geodesk.feature.*;
import com.geodesk.core.Box;

import static java.lang.System.out;

import java.nio.file.Path;

// leisure=marina or harbour=yes
// Port Saint-Saveur (Toulouse)
// Port Sud (Ramonville-Saint-Agne)
// Port Lauragais
// Petit Bassin (Castelnaudary)
// Port de Plaisance du Canal du Midi (Castelnaudary) -- this is a relation area
// Port de Bram
// Port de Carcassonne (node in large area that covers much more than just port)
//   harbour:visitor_berth=48
// Port de Trèbes
// Port La Fabrique
// Port Minervois
// Port Occitanie (way/202809646: leisure=marina, overlaps natural=water adjacent to canal water area)
// Base fluviale Le Somail
// Port de Béziers
// Port Cassafières
//
// Photos:
// https://www.flickr.com/photos/130538128@N06/20004487119/in/photolist-wtJe5k-ebs2XV-a7TCD3-8DQ1sS-6iLv6g-2itBkXL-6iQH5w-21g9az-XV7sVM-BQgE7G-YTESQQ-dkcP8c-29JjBEU-vQjMyZ-QYXLNb-955yWE-np2zyA-952wsR-BQgEBu-955AC7-6iLwtc-YTESoY-952w96-BQgK5s-mMQapD-L3J9VV-XRFHef-2itxD6T-YTERvL-952uAR-6iQG9Q-KC7mnU-YTESAw-SQZQAm-6iLvY6-47rU6L-YxE3P7-955AVd-ywwJQ6-a8PZwf-28qKsQi-952vrP-273MA7o-bvYcvJ-3S8Qfv-6MRD7i-AGDTNw-2bsWcwS-28qKZ1i-28qLDUa
// https://www.flickr.com/photos/mdicaire/48870663002/in/photolist-2hswVtY-jqcEUM-2kGCJtf-jfVuXD-jfVFz1-jqbe8F-jqdcPd-jqcBFc-jqesLY-jqd6ju-2kGDkMx-gDPQvV-gDPzux-itkk7g-8xx6v8-itkjxF-itkwt3-aZhzZv-itktEC-gDNwtA-itkVQA-gDRDEL-CyLsdt-gDNCbE-gDSLbZ-BQgFLd-gDWEDQ-aZhnrn-2m3ft5V-gDRyvg-w7buHC-itm2E8-iQMCDw-gDT5jx-8xx8K8-itkWjb-2191FGq-itkTYE-itkXAj-3UVCYG-gDRbdu-oBoSG2-nWrcDZ-nWr73C-qXpmkX-HLpUG-gDPSsJ-mMUpty-gDXMdg-gDUSEr (non-comm)
// https://www.flickr.com/photos/130538128@N06/16379525725/in/photolist-nWrcDZ-nWr73C-qXpmkX-HLpUG-gDPSsJ-mMUpty-gDXMdg-gDUSEr-gDNVbR-BBdVk7-gDUvpG-gDU6QY-6VAEUu-6ofWbC-7fRRds-gDQGPR-itkWNY-qEPum3-8xAb1Y-8y2RdB-jqb2eF-jqb1bP-27mqXg-jqcWid-gDP55m-8FRbA9-aw79KG-gDWUBP-LTpWex-jqcAb8-gDS3tu-gDYvtZ-gDQWR1-3jbgN1-7fMSpv-gDQEeG-LtqcVm-gDQ8VV-gDTEnR-KWgugL-5DReDJ-aw4APx-jqex4f-gDRmNu-gDTsbr-jqetjb-jqb6EV-gDST7S-H28jiB-2mfiSGt
// https://www.flickr.com/photos/33497395@N08/49553274998/in/photolist-2iuRufj-KXJX1C-LKjojq-2atooBq-gDRJUk-jwrVmu-dFhmrF-gDQ2qJ-2WjgYV-iQKELT-gDXgX3-6ZafnW-gDPYhu-itkxsN-iwmKwQ-2dNdhdn-2iuTUKA-jwpWL8-5DLGp2-iQKKmp-iwmtxV-4ovEP5-2dW9ScW-7UAQJe-gDPEnt-gDUTy9-gDVbbh-gDX7Gu-jfXUw1-5DLjQ8-fBjLsj-2dzbC9R-2hUjSRN-jfTE8t-itkmV6-iwmu8x-rQTmfc-iQPY1A-gDVWSx-jwtkYA-6ZafoC-gDX6KP-gDXbsL-gDQTD6-EeA7b-gDRdpc-dFnMAL-iQMdqR-gDXzvD-2hUnkjS
// https://www.flickr.com/photos/130538128@N06/49163020986/in/photolist-jfXUw1-5DLjQ8-fBjLsj-2dzbC9R-2hUjSRN-jfTE8t-itkmV6-iwmu8x-rQTmfc-iQPY1A-gDVWSx-jwtkYA-6ZafoC-gDX6KP-gDXbsL-gDQTD6-EeA7b-gDRdpc-dFnMAL-iQMdqR-gDXzvD-2hUnkjS-iQKQCB-6iLkQM-gDPPZA-8oyLRk-jwrUys-2tfbE6-gDSwiM-bVxfmH-dodVuk-5DLM6F-wLHFeM-dFnMfW-8FRdM3-gDWqWP-5DQVuo-2cb7CFA-gDSmd8-6Zafnd-gDSVF8-iQPyEN-od3ADJ-gDPnWy-iQLc8p-gDXkdJ-qEYqsc-gDRQwT-gDV6RN-2gsYzw9
// https://www.flickr.com/photos/130538128@N06/49163260552/in/photolist-2hUoyxj-2hUnk58-fBjLJQ-2hUoxVn-2kGkLsb-2j9cru-gDSqdF-iwmEf5-iQM6Vr-gDWQUh-iQPTFJ-dJ9o5F-n1Y914-AXhHXB-iQQ2nA-iQKXsP-jwtimb-pUCFgw-GfNPk9-iQMXuC-iQMZej-5DQGRj-iQMuT1-2WspBo-iQMk1z-iQPN2C-gDNP1p-GfNHLo-gDQ8F7-jwrZfb-iQLQur-5DQNUW-gDQ2JN-iQPFKj-iQMNcj-qEWHYa-bwsMSp-5i5wxu-Crsevt-2Wnqhx-BBmYKX-feYAms-2WnVBT-iQPJ9C-2hUjTem-2hUoy1n-qER7bQ-qER7d3-2hUnkoK-26RMyQn
// Nice album: https://www.flickr.com/photos/130538128@N06/albums/72157650510255885


// DistanceOp(g1,g2, maxDist)
// Coordinate[] c = dop.nearestPoints()

//public static double getLengthAloneLineString(LineString line, Point point) {
//    return getLengthAloneLineString(line, point.getCoordinate());
//    }
//
//public static double getLengthAloneLineString(LineString line, Coordinate coordinate) {
//    LocationIndexedLine locationIndexedLine = new LocationIndexedLine(line);
//    LinearLocation location = locationIndexedLine.project(coordinate);
//    double length = new LengthLocationMap(line).getLength(location);
//
//    return length;
//    }

// must sort segments, flow reverses at Rigole

public class CanalDuMidi
{
    static FeatureLibrary features;

    static int countLockChambers(Way canal)
    {
        int gates = 0;
        out.format("Checking %s...\n", canal);
        for(Node node: canal)
        {
            out.format("  Checking %s...\n", node);
            if(node.hasTag("waterway", "lock_gate")) gates++;
        }

        // canal.nodes("waterway=lock_gate").count()
        // canal.nodes("n < w[waterway=lock_gate]").count()
        // if(node.parentWays("waterway=lock_gate").first() != null)
        // lockGates = features.ways("waterway=lock_gate")
        // if(lockGates.with(node).count() > 0)

        return gates - 1;
    }

    public static void main(String[] args)
    {
        features = new FeatureLibrary("c:\\geodesk\\tests\\france.gol");

        Box box = Box.ofWSEN(1.857849,43.276901,2.039908,43.348275);
        Relation canal = (Relation)features.features(
            "r[waterway=canal][name='Canal du Midi']").in(box).first();
        double dist = 0;
        // For (Way member: canal.memberWays("role=main_stream"))
        for(Feature member: canal)
        {
            if(!(member instanceof Way) || !"main_stream".equals(member.role())) continue;
            Way way = (Way)member;
            String lockName = way.tag("lock_name");
            if(!lockName.isEmpty())
            {
                out.format("%.1f %s (%s): %d chambers\n", dist / 1000,
                    lockName, way.tag("lock_ref"),
                    countLockChambers(way));
            }
            dist += way.length();
        }

    }

}
