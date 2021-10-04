package deco.combatevolved.util.worldgen;

import deco.combatevolved.util.Vector2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * A class to generate perlin noise for a map
 */
public class NoiseMap {
    private static final Logger logger = LoggerFactory.getLogger(NoiseMap.class);

    private float[][] heightMapArr; // the map we save our perlin noise to
    private int mapWidth; // the width of the map
    private int mapHeight; // the height of the map
    private int seed; // seed value to generate unique noise maps
    private float scale; // the scale of the noise
    private int octaves; // the number of octaves to use over the noise
    private float persistence; // home much each successive octave effects the overall noise
    private float lacunarity; // How much the frequency changes for each successive octave
    protected static float[][][] vector; // Vectors for perlin noise
    private final float frequencyStart = 1f;

    /**
     * NoiseMap constructor
     * @param mapWidth The width of the map
     * @param mapHeight The height of map
     * @param seed A value used to generate unique noise maps
     * @param scale A scaling factor to use when generating sample points to
     *              pass through the perlin noise function
     * @param octaves The number of octaves to use
     * @param persistence A multiplier that determines how quickly the amplitudes
     *                    diminish for each successive octave in a Perlin-noise function.
     * @param lacunarity A multiplier that determines how quickly the frequency
     *                   increases for each successive octave in a Perlin-noise function.
     */
    public NoiseMap(int mapWidth, int mapHeight,
                    int seed, Float scale,
                    int octaves, Float persistence,
                    Float lacunarity) {
        this.heightMapArr = new float[mapWidth][mapHeight];
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.seed = seed;
        this.scale = scale;
        this.octaves = octaves;
        this.persistence = persistence;
        this.lacunarity = lacunarity;
    }

    /**
     * Generates a noise map using perlin noise
     */
    public float[][] generateHeightMap() {
        Random random = new Random(seed);
        Vector2[] octaveOffsets = new Vector2[octaves];

        final int MAX_BOUNDS = 100;
        final int MIN_BOUNDS = 0;
        float frequencyEnd = frequencyStart; // Largest value frequency can be

        // For starting each octave in a different location
        for (int i = 0; i < octaves; i++) {
            frequencyEnd *= this.lacunarity;
            float offsetX =
                    (MIN_BOUNDS + random.nextFloat() * (MAX_BOUNDS - MIN_BOUNDS));
            float offsetY =
                    (MIN_BOUNDS + random.nextFloat() * (MAX_BOUNDS - MIN_BOUNDS));
            octaveOffsets[i] = new Vector2(offsetX, offsetY);
        }

        generateVectors(random,
                (int) (frequencyEnd * (float) mapWidth / scale) + 1 + MAX_BOUNDS,
                (int) (frequencyEnd * (float) mapHeight / scale) + 1 + MAX_BOUNDS);

        // Params to normalise the noiseMap
        float rangeStart = 0f;
        float rangeEnd = 1f;

        // Loop through and create noise within the bounds of the map
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                // Set initial values for frequency, amplitude and noiseHeightValue
                float frequency = frequencyStart; // frequency of 1 is probably boring (all 0.5)
                float amplitude = 1f;
                float noiseHeightValue = 0f;
                // Apply octave modification to our perlin value to create
                // interesting noise maps
                for (int i = 0; i < this.octaves; i++) {
                    // The sample values to put through the perlin generator
                    float sampleX =
                            x  / this.scale * frequency + octaveOffsets[i].getX();
                    float sampleY =
                            y  / this.scale * frequency + octaveOffsets[i].getY();

                    // Apply perlin noise to our sample values and sum value
                    noiseHeightValue += PerlinNoise.perlin(sampleX, sampleY) * amplitude;

                    // Update frequency and amplitude noiseHeightValue
                    amplitude *= this.persistence;
                    frequency *= this.lacunarity;

                }
                // For scaling noise between 0 and 1
                if (noiseHeightValue > rangeStart) {
                    rangeStart = noiseHeightValue;
                } else if (noiseHeightValue < rangeEnd) {
                    rangeEnd = noiseHeightValue;
                }
                heightMapArr[x][y] = noiseHeightValue;
            }
        }
        // normalise the heightMapArr to be within the bounds
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                heightMapArr[x][y] = normalise(rangeStart, rangeEnd,
                        heightMapArr[x][y]);
            }
        }
        return heightMapArr;
    }

    /**
     * Private function to help normalise the noiseMap within the bounds of 0
     * and 1
     * @param rangeStart The maximum value the noise value can be
     * @param rangeEnd The minimum value the noise value can be
     * @param noiseValue The noise value to be normalised
     * @return noiseValue scaled between maxNoiseHeight and minNoiseHeight
     */
    private float normalise(float rangeStart,
                            float rangeEnd, float noiseValue) {
        return (noiseValue - rangeStart) / (rangeEnd - rangeStart);
    }

    /**
     *
     * @param rand Random number generator based off the map seed
     * @param sizeX The size of the array needed
     * @param sizeY The size of the array needed
     */
    private static void generateVectors(Random rand, int sizeX, int sizeY) {
        vector = new float[sizeY][sizeX][2];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                vector[x][y][0] = rand.nextFloat() * 2 - 1;
                vector[x][y][1] = rand.nextFloat() * 2 - 1;
            }
        }
    }
}
