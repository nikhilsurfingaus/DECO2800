package deco.combatevolved.util.worldgen;

import java.util.Random;

import deco.combatevolved.worlds.biomes.BiomeType;
import deco.combatevolved.worlds.worldgen.WorldGenParamBag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoronoiNoise {
    private static final Logger logger = LoggerFactory.getLogger(VoronoiNoise.class);
    // Array of data points for noise function
    private static VoronoiPoint[] points;
    private Random rand;
    private int width;
    private int height;
    private static int pointCount; // Number of points to try
    private int biomeSize;
    // The index of the nearest VoronoiPoint, set after findNearestPoint is run
    private int nearestI;
    private WorldGenParamBag worldGenParamBag;

    /**
     * VoronoiNoise constructor, places points randomly through the map region to be used for biome region generation
     * @param worldGenParamBag
     * @param seed Param to determine/affect island shape and biome shape.
     * @param width of Map
     * @param height of Map
     * @param biomeSize Rough NTS size of biomes
     */
    public VoronoiNoise(WorldGenParamBag worldGenParamBag, int seed, int width, int height, int biomeSize) {
        this.worldGenParamBag = worldGenParamBag;
        rand = new Random(seed);
        this.width = width;
        this.height = height;
        this.biomeSize = biomeSize;
        pointCount = width * height / biomeSize;
        generatePoints();
//        relax();
        generateElevation();
        generateMoisture();
    }

    /**       ~~~~~ WIP ~~~~~
     *    ~~~ May be removed ~~~
     * Returns nearest and next nearest points
     * @param x Coordinate to search from
     * @param y Coordinate to search from
     * @return Nearest and next nearest points to the give coordinates
     */
    public VoronoiPoint noise (float x, float y) {
        //VoronoiPoint point = ;
        //point.nearPoint = findNearestPoint(point.x, point.y, false);
        return findNearestPoint(x, y, true);
    }

    /**
     * Generates the biome shapes
     */
    private void generatePoints() {
        short x;
        short y;
        short attempts = 0;
        points = new VoronoiPoint[pointCount]; // creates a new array of points

        // Handles the first point in array, so don't have to deal with nulls
        x = (short)((rand.nextInt(width))-width/2);
        y = (short)((rand.nextInt(height))-height/2);
        points[0] = new VoronoiPoint(x, y);

        // Loops over points (after first one) array and adds point if distance to next point is large enough
        for (int next = 1; next < pointCount; next++) {
            // new x and y
            x = (short)((rand.nextInt(width))-width/2);
            y = (short)((rand.nextInt(height))-height/2);
            // if x and y not near other x and y
            if (distance(x, y, findNearestPoint(x, y, true))  < this.biomeSize) {
                // If too many points fail to generate, stop generating points
                if (attempts++ == 50) {
                    logger.info("Finish generating VoronoiPoints");
                    pointCount = next;
                    break;
                }
                // Next decremented to try the same point again
                next--;
            } else {
                // Point successfully generated
                attempts = 0;
                points[next] = new VoronoiPoint(x, y);
            }
        }
    }

    /**
     *  Finds the nearest point to the specified x and y coordinates
     *  Sets nearestI the index of the nearest VoronoiPoint
     * @param x x coordinate to search with
     * @param y Y coordinate to search with
     * @param includeInput Bool, where to ignore the x and y coordinates when looking for point
     * @return The nearest point to coordinates
     */
    private VoronoiPoint findNearestPoint(float x, float y, boolean includeInput) {
        // Distance from x and y to the current nearest found point
        float distance = 2147483647;
        VoronoiPoint nearest = points[0];
        // Loop to find nearest point
        for (int i = 0; i < pointCount; i++) {
            if (points[i] != null && distance(x, y, points[i]) < distance &&
                    !(!includeInput && floatEqual(points[i].getX(), x) && floatEqual(points[i].getY(),y))) {
                distance = distance(x, y, points[i]);
                nearest = points[i];
                nearestI = i;
            }
        }
        return nearest;
    }

    // Returns the nearest Voronoi Point to the x and y coords passed in
    public static VoronoiPoint findNearestPoint(float x, float y, VoronoiPoint[] points) {
        // Distance from x and y to the current nearest found point
        float distance = 2147483647;
        VoronoiPoint nearest = points[0];
        // Loop to find nearest point
        for (int i = 0; i < pointCount; i++) {
            if (distance(x, y, points[i]) == 0) {
                nearest = points[i];
                return nearest;
            }
            if (points[i] != null && distance(x, y, points[i]) < distance &&
                    !(floatEqual(points[i].getX(), x) && floatEqual(points[i].getY(),
                            y))) {
                distance = distance(x, y, points[i]);
                nearest = points[i];
            }
        }
        return nearest;
    }

    /**
     * Distance between x,y and a given point
     * @param x Coordinate to check
     * @param y Coordinate to check
     * @param point Point to find distance to
     * @return Float distance between x,y and point or -1 if point is null
     */
    public static float distance(float x, float y, VoronoiPoint point) {
        if (point != null) {
            return (float) Math.sqrt((x-point.getX())*(x-point.getX()) + (y-point.getY())*(y-point.getY()));
        }
        return -1;
    }

    /** Moves the coordinates of the VoronoiPoint points to the rough centre of the biome.
     * Move point to the average of the x and y coordinates
     */
    private void relax() {
        final int sumX = 0;
        final int sumY = 1;
        final int divisor = 2;
        int [][] avgArray = new int [pointCount][3]; // Array stores the values for averaging the point coordinates

        // Determines the new coordinates
        for (int q = -this.width/2; q < this.width/2; q++) { // q not g
            for (int r = -this.height/2; r < this.height/2; r++) {
                float oddCol = (q % 2 != 0 ? 0.5f : 0);
                findNearestPoint(q, r + oddCol, true);
                avgArray[nearestI][sumX] += q + this.width/2;
                avgArray[nearestI][sumY] += r + this.height/2;
                avgArray[nearestI][divisor] += 1;
            }
        }

        // Sets the new coordinates
        for (int i = 0; i < pointCount; i++) {
            points[i].setX((short) (avgArray[i][sumX] / avgArray[i][divisor] - this.width/2));
            points[i].setY((short) (avgArray[i][sumY] / avgArray[i][divisor] - this.height/2));
        }
    }

    /**
     *  Equals method for float variables
     * @param f1 Float 1
     * @param f2 Float 2
     * @return True if float ~ equal, false otherwise
     */
    private static boolean floatEqual(float f1, float f2) {
        return (Math.abs(f1 - f2) < 0.001);
    }

    public VoronoiPoint[] getPoints() {
        return points;
    }

    private void generateElevation() {
        // call our perlin algorithm or heightMap from param bag to get a
        // Perlin value to encode into VoronoiPoint
        int mapHeight = worldGenParamBag.getMapHeight();
        int mapWidth = worldGenParamBag.getMapWidth();
        float oceanQuantity = worldGenParamBag.getOceanQuantity();

        double centerX = (double)mapWidth / 2;
        double centerY = (double)mapHeight / 2;
        float[][] perlin = worldGenParamBag.getHeightMap();
        for (int i = 0; i < pointCount; i++) {
            short x1 = (short) (points[i].getX() + Math.ceil(centerX));
            short y1 = (short) (points[i].getY() + Math.ceil(centerY));

            // elliptic paraboloid not cone
            float coneX = (float) Math.pow((double) x1 - centerX, 2) / (float) Math.pow((centerX * oceanQuantity), 2);
            float coneY = (float) Math.pow((double) y1 - centerY, 2) / (float) Math.pow((centerY * oceanQuantity), 2);
            float cone = - coneX - coneY + 1.8f;
            if (cone < 0) {
                cone = 0;
            }

            float perlinboi = perlin[(int) x1][(int) y1];
            if (cone * perlinboi > 1) {
                points[i].setElevation(1);
            } else {
                points[i].setElevation(cone * perlinboi);
            }

        }
    }

    /**
     * Generates the moisture for each Voronoi point within the world
     */
    private void generateMoisture() {
        float oceanHeight = this.worldGenParamBag.getBiomeHeightMap().get(BiomeType.OCEAN);
        for (int i = 0; i < pointCount; i++) {
            float distance = 2147483647;

            // find the nearest ocean tile
            for (int j = 0; j < pointCount; j++) {
                if (points[i].getElevation() <= oceanHeight) { // If you are an ocean biome
                    distance = 0;
                } else if (points[j].getElevation() <= oceanHeight && distance(points[i].getX(), points[i].getY(), points[j]) < distance) {
                    distance = distance(points[i].getX(), points[i].getY(), points[j]);
                }
            }

            float moisture = 1 - (distance/50); // This may need tweaking
            if (moisture < 0) {
                moisture = 0;
            }
            points[i].setMoisture(moisture);
        }
    }

    public static int getVoronoiCount() {
        return pointCount;
    }
}
