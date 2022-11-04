# GeoDesk Examples

Examples using the [GeoDesk](http://www.github.com/clarisma/geodesk) geospatial database.

Each file contains a single self-contained mini-application.

You will need to [build a GOL](http://docs.geodesk.com/gol/build) (or [download a tile set](http://docs.geodesk.com/gol/load)) using the [GOL command-line utility](http://www.github.com/clarisma/gol-tool).

In the example source code:

- Change `GEODESK_PATH` to the folder where you keep your GOLs

- Change `GOL_FILE` to the name of the GOL you wish to use 

- Change `TILESET_URL` to the URL of the tile set if you want the program to download tiles as-needed (or use `null` if the GOL already contains all required tiles)
