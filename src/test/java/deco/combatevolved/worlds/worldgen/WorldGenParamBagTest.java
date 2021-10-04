package deco.combatevolved.worlds.worldgen;

import deco.combatevolved.exceptions.IllegalWorldSizeException;
import deco.combatevolved.exceptions.InvalidScaleException;
import deco.combatevolved.exceptions.WorldException;
import deco.combatevolved.exceptions.WorldGenException;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.worlds.biomes.BiomeType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.Assert.*;

public class WorldGenParamBagTest {
    private CombatEvolvedWorld combatEvolvedWorld;

    @Before
    public void setUp() throws Exception {
        this.combatEvolvedWorld = new CombatEvolvedWorld();
    }

    @After
    public void tearDown() throws Exception {
        this.combatEvolvedWorld = null;
    }

    @Test
    public void testGetSeed() {
        assertNotEquals(0, this.combatEvolvedWorld.getWorldGenParamBag()
                .getSeed());
    }

    @Test(expected = WorldGenException.class)
    public void testSetSeedWithZeroParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setSeed(0);
    }

    @Test
    public void testSetSeedWithNonZeroParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setSeed(20);
        assertEquals(20,
                this.combatEvolvedWorld.getWorldGenParamBag().getSeed());
    }

    @Test(expected = IllegalWorldSizeException.class)
    public void testSetWorldSizeWithIncorrectParam() throws IllegalWorldSizeException {
        this.combatEvolvedWorld.getWorldGenParamBag().setWorldSize(13);
    }

    @Test(expected = IllegalWorldSizeException.class)
    public void testSetWorldSizeWithNegativeParam() throws IllegalWorldSizeException {
        this.combatEvolvedWorld.getWorldGenParamBag().setWorldSize(-14);
    }

    @Test
    public void testSetWorldSizeWithCorrectParam() throws IllegalWorldSizeException {
        this.combatEvolvedWorld.getWorldGenParamBag().setWorldSize(50);
        assertEquals(50, this.combatEvolvedWorld.getWorldGenParamBag()
                .getWorldSize());
    }

    @Test
    public void testGetBiomeSize() {
        assertEquals(10, this.combatEvolvedWorld.getWorldGenParamBag()
                .getBiomeSize());
    }

    @Test(expected = WorldGenException.class)
    public void testSetBiomeSizeWithZeroParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setBiomeSize(0);
    }

    @Test(expected = WorldGenException.class)
    public void testSetBiomeSizeWithNegativeParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setBiomeSize(-100);
    }

    @Test
    public void testSetBiomeSizeWithCorrectParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setBiomeSize(10);
        assertEquals(10, this.combatEvolvedWorld.getWorldGenParamBag()
                .getBiomeSize());
    }

    @Test
    public void testInitialiseHeightMap() {
        float[][] testHeightMap = new float[25][25];
        this.combatEvolvedWorld.getWorldGenParamBag().initialiseHeightMap(25,
                25);

        assertArrayEquals(testHeightMap,
                this.combatEvolvedWorld.getWorldGenParamBag().getHeightMap());
    }

    @Test
    public void testGetHeightMap() {

    }

    @Test
    public void testSetHeightMap() {
    }

    @Test
    public void testSetVoronoiPoints() {
    }

    @Test
    public void testGetVoronoiPoints() {
    }

    @Test(expected = WorldException.class)
    public void testSetMapWidthWithZeroWidth() throws WorldException {
        this.combatEvolvedWorld.getWorldGenParamBag().setMapWidth(0);
    }

    @Test(expected = WorldException.class)
    public void testSetMapWidthWithWidthLessThanWorldSize() throws WorldException {
        this.combatEvolvedWorld.getWorldGenParamBag().setWorldSize(20);
        this.combatEvolvedWorld.getWorldGenParamBag().setMapWidth(15);
    }

    @Test
    public void testSetMapWidthWithCorrectParam() throws WorldException {
        this.combatEvolvedWorld.getWorldGenParamBag().setWorldSize(20);
        this.combatEvolvedWorld.getWorldGenParamBag().setMapWidth(25);
        assertEquals(25, this.combatEvolvedWorld.getWorldGenParamBag()
                .getMapWidth());
    }

    @Test(expected = WorldException.class)
    public void testSetMapHeightWithZeroHeight() throws WorldException {
        this.combatEvolvedWorld.getWorldGenParamBag().setMapHeight(0);
    }

    @Test(expected = WorldException.class)
    public void testSetMapHeightWithHeightLessThanWorldSize() throws WorldException {
        this.combatEvolvedWorld.getWorldGenParamBag().setWorldSize(20);
        this.combatEvolvedWorld.getWorldGenParamBag().setMapHeight(17);
    }

    @Test
    public void testSetMapHeightWithCorrectParam() throws WorldException {
        this.combatEvolvedWorld.getWorldGenParamBag().setWorldSize(20);
        this.combatEvolvedWorld.getWorldGenParamBag().setMapHeight(25);
        assertEquals(25, this.combatEvolvedWorld.getWorldGenParamBag()
                .getMapHeight());
    }

    @Test
    public void testGetNoiseScale() {
        assertEquals(1f, this.combatEvolvedWorld.getWorldGenParamBag()
                .getNoiseScale(), 0.01);
    }

    @Test(expected = InvalidScaleException.class)
    public void testSetNoiseScaleWithZeroParam() throws InvalidScaleException {
        this.combatEvolvedWorld.getWorldGenParamBag().setNoiseScale(0f);
    }

    @Test(expected = InvalidScaleException.class)
    public void testSetNoiseScaleWithNegativeParam() throws InvalidScaleException {
        this.combatEvolvedWorld.getWorldGenParamBag().setNoiseScale(-10f);
    }

    @Test
    public void testSetNoiseScaleWithCorrectParam() throws InvalidScaleException {
        this.combatEvolvedWorld.getWorldGenParamBag().setNoiseScale(0.3f);
        assertEquals(0.3f, this.combatEvolvedWorld.getWorldGenParamBag()
                .getNoiseScale(), 0.01);
    }

    @Test
    public void testGetOctaves() {
        assertEquals(2, this.combatEvolvedWorld.getWorldGenParamBag()
                .getOctaves());
    }

    @Test(expected = WorldGenException.class)
    public void testSetOctavesWithZeroParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setOctaves(0);
    }

    @Test(expected = WorldGenException.class)
    public void testSetOctavesWithNegativeParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setOctaves(-16);
    }

    @Test
    public void testSetOctavesWithCorrectParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setOctaves(4);
        assertEquals(4, this.combatEvolvedWorld.getWorldGenParamBag()
                .getOctaves());
    }

    @Test
    public void testGetPersistence() {
        assertEquals(0.5f, this.combatEvolvedWorld.getWorldGenParamBag()
                .getPersistence(), 0.01);
    }

    @Test(expected = WorldGenException.class)
    public void testSetPersistenceLessThanZero() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setPersistence(-1f);
        this.combatEvolvedWorld.getWorldGenParamBag().setPersistence(0f);
    }

    @Test(expected = WorldGenException.class)
    public void testSetPersistenceGreaterThanOne() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setPersistence(1f);
        this.combatEvolvedWorld.getWorldGenParamBag().setPersistence(2f);
    }

    @Test
    public void testSetPersistenceWithCorrectParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setPersistence(0.5f);
        assertEquals(0.5f,
                this.combatEvolvedWorld.getWorldGenParamBag()
                        .getPersistence(), 0.01);
    }

    @Test
    public void testGetLacunarity() {
        assertEquals(2f, this.combatEvolvedWorld.getWorldGenParamBag()
                .getLacunarity(), 0.01);
    }

    @Test(expected = WorldGenException.class)
    public void testSetLacunarityLessThanOne() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setLacunarity(0.5f);
        this.combatEvolvedWorld.getWorldGenParamBag().setLacunarity(1f);
    }

    @Test
    public void testSetLacunarityWithCorrectParam() throws WorldGenException {
        this.combatEvolvedWorld.getWorldGenParamBag().setLacunarity(2f);
        assertEquals(2f, this.combatEvolvedWorld.getWorldGenParamBag()
                .getLacunarity(), 0.01);
    }

    @Test
    public void testInitialiseBiomeHeightMap() {
        Map<BiomeType, Float> testBiomeHeightMap = new EnumMap<>(BiomeType.class);
        testBiomeHeightMap = new EnumMap<>(BiomeType.class);
        testBiomeHeightMap.put(BiomeType.SNOW, 1.0f);
        testBiomeHeightMap.put(BiomeType.MOUNTAIN_ROCKS, 0.85f);
        testBiomeHeightMap.put(BiomeType.TUNDRA, 0.82f);
        testBiomeHeightMap.put(BiomeType.SHRUBLAND, 0.78f);
        testBiomeHeightMap.put(BiomeType.TEMPERATE_RAINFOREST, 0.65f);
        testBiomeHeightMap.put(BiomeType.GRASSLAND, 0.58f);
        testBiomeHeightMap.put(BiomeType.TEMPERATE_DESERT, 0.50f);
        testBiomeHeightMap.put(BiomeType.TROPICAL_RAINFOREST, 0.47f);
        testBiomeHeightMap.put(BiomeType.SUBTROPICAL_DESERT, 0.41f);
        testBiomeHeightMap.put(BiomeType.BEACH, 0.37f);
        testBiomeHeightMap.put(BiomeType.OCEAN, 0.32f);

        this.combatEvolvedWorld.getWorldGenParamBag().initialiseBiomeHeightMap();

        assertEquals(testBiomeHeightMap,
                this.combatEvolvedWorld.getWorldGenParamBag().getBiomeHeightMap());
    }

    @Test
    public void testInitialiseBiomeMoistureMap() {
        Map<BiomeType, Float> testBiomeMoistureMap = new EnumMap<>(BiomeType.class);
        testBiomeMoistureMap = new EnumMap<>(BiomeType.class);
        testBiomeMoistureMap.put(BiomeType.OCEAN, 0.92f);
        testBiomeMoistureMap.put(BiomeType.SNOW, 0.75f);
        testBiomeMoistureMap.put(BiomeType.TEMPERATE_RAINFOREST, 0.70f);
        testBiomeMoistureMap.put(BiomeType.TROPICAL_RAINFOREST, 0.67f);
        testBiomeMoistureMap.put(BiomeType.BEACH, 0.43f);
        testBiomeMoistureMap.put(BiomeType.SHRUBLAND, 0.44f);
        testBiomeMoistureMap.put(BiomeType.TUNDRA, 0.45f);
        testBiomeMoistureMap.put(BiomeType.GRASSLAND, 0.36f);
        testBiomeMoistureMap.put(BiomeType.TEMPERATE_DESERT, 0.21f);
        testBiomeMoistureMap.put(BiomeType.SUBTROPICAL_DESERT, 0.15f);
        testBiomeMoistureMap.put(BiomeType.MOUNTAIN_ROCKS, 0.1f);

        this.combatEvolvedWorld.getWorldGenParamBag().initialiseBiomeMoistureMap();

        assertEquals(testBiomeMoistureMap,
                this.combatEvolvedWorld.getWorldGenParamBag().getBiomeMoistureMap());
    }

    @Test
    public void testGetMapOfTilesInBiomes() {
    }

    @Test
    public void testSetMapOfTilesInBiomes() {
    }

    @Test
    public void testInitialiseTileMap() {
        Map<BiomeType, CopyOnWriteArrayList<Tile>> testMap =
                new ConcurrentHashMap<>();

        this.combatEvolvedWorld.getWorldGenParamBag().initialiseTileMap();

        assertEquals(testMap,
                this.combatEvolvedWorld.getWorldGenParamBag().getMapOfTilesInBiomes());
    }
}
