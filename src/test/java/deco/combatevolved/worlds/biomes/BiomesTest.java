package deco.combatevolved.worlds.biomes;

import deco.combatevolved.worlds.Tile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class BiomesTest extends AbstractBiome {
    private final Logger logger = LoggerFactory.getLogger(BiomesTest.class);
    private Grassland grassland;
    private TemperateDesert temperateDesert;
    private Snow snow;
    private TropicalRainforest tropicalRainforest;
    private Tile tile1;

    /**
     * Set's the texture type for the biome when constructing tiles for this
     * particular biome
     *
     * @param tile The Tile to set the texture for
     */
    @Override
    public void setBiomeTexture(Tile tile) {
        // Do nothing as method is not used in test cases
    }

    @Before
    public void setUp() throws Exception {
            this.grassland = new Grassland(BiomeType.GRASSLAND);
            this.temperateDesert =
                    new TemperateDesert(BiomeType.TEMPERATE_DESERT);
            this.snow = new Snow(BiomeType.SNOW);
            this.tropicalRainforest =
                    new TropicalRainforest(BiomeType.TROPICAL_RAINFOREST);
            this.tile1 =  new Tile(null, 6, 6);
    }

    @After
    public void tearDown() throws Exception {
        this.grassland = null;
        this.temperateDesert = null;
        this.snow = null;
        this.tropicalRainforest = null;
        this.tile1 = null;
    }

    @Test
    public void testEmptyConstructor() {
        Snow newSnow = new Snow();
        assertNotEquals(BiomeType.SNOW, newSnow.getBiomeType());
        assertEquals(BiomeType.OCEAN, newSnow.getBiomeType());
    }

    @Test
    public void testConstructor() {
        TemperateRainforest temperateRainforest =
                new TemperateRainforest(BiomeType.TEMPERATE_RAINFOREST);
        assertEquals(BiomeType.TEMPERATE_RAINFOREST, temperateRainforest.getBiomeType());
        assertNotEquals(BiomeType.OCEAN, temperateRainforest.getBiomeType());
    }

    @Test
    public void testGetTileList() {
        this.temperateDesert.addTileToBiome(tile1);
        List<Tile> temperateDesertTestList = new CopyOnWriteArrayList<>();
        temperateDesertTestList.add(tile1);
        assertEquals(temperateDesertTestList, this.temperateDesert.getTileList());
    }

    @Test
    public void testAddTileToBiome() {
        MountainRocks mountainRocks = new MountainRocks();
        Lake lake = new Lake();
        Tile t1 =  new Tile(null, 6, 6);
        Tile t2 =  new Tile(null, 6, 6);
        Tile t3 =  new Tile(null, 6, 6);
        Tile t4 =  new Tile(null, 6, 6);
        List<Tile> mountainTestList = new CopyOnWriteArrayList<>();
        List<Tile> lakeTestList = new CopyOnWriteArrayList<>();

        mountainRocks.addTileToBiome(t1);
        mountainRocks.addTileToBiome(t2);
        lake.addTileToBiome(t3);
        lake.addTileToBiome(t4);

        mountainTestList.add(t1);
        mountainTestList.add(t2);
        lakeTestList.add(t3);
        lakeTestList.add(t4);

        assertEquals(mountainTestList, mountainRocks.getTileList());
        assertEquals(lakeTestList, lake.getTileList());
    }

    @Test
    public void testGetBiomeType() {
        assertEquals(BiomeType.TEMPERATE_DESERT,
                this.temperateDesert.getBiomeType());
        assertEquals(BiomeType.SNOW, this.snow.getBiomeType());
        assertEquals(BiomeType.TROPICAL_RAINFOREST,
                this.tropicalRainforest.getBiomeType());
        assertEquals(BiomeType.GRASSLAND, this.grassland.getBiomeType());
    }

    @Test
    public void testSetBiomeType() {
        temperateDesert.setBiomeType(BiomeType.SNOW);
        assertNotEquals(BiomeType.TEMPERATE_DESERT, temperateDesert.getBiomeType());
        assertEquals(BiomeType.SNOW, temperateDesert.getBiomeType());
    }

    @Test
    public void testToString() {
        String testString = "grassland";
        assertEquals(testString, grassland.toString());
    }

    @Test
    public void testSetBiomeTexture() {
        Snow snow = new Snow(BiomeType.SNOW);
        Tile tile1 = new Tile(null, 5, 5);
        Tile tile2 = new Tile(null, 6, 6);

        assertNull(tile1.getTextureName());
        assertNull(tile2.getTextureName());


        snow.setBiomeTexture(tile1);
        snow.setBiomeTexture(tile2);

        String textureName1 = tile1.getTextureName();
        String textureName2 = tile2.getTextureName();

        assertNotNull(tile1.getTextureName());
        assertNotNull(tile2.getTextureName());

        assertTrue(textureName1.equals("snow_0") ||
                textureName1.equals("snow_1") || textureName1.equals("snow_2"));
        assertTrue(textureName2.equals("snow_0") ||
                textureName2.equals("snow_1") || textureName2.equals("snow_2"));
    }
}
