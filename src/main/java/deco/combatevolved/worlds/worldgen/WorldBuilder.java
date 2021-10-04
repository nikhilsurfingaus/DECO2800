package deco.combatevolved.worlds.worldgen;

import deco.combatevolved.exceptions.IllegalWorldSizeException;
import deco.combatevolved.exceptions.InvalidScaleException;
import deco.combatevolved.exceptions.WorldException;
import deco.combatevolved.exceptions.WorldGenException;
import deco.combatevolved.util.worldgen.NoiseMap;
import deco.combatevolved.util.worldgen.VoronoiNoise;
import deco.combatevolved.util.worldgen.VoronoiPoint;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.worlds.biomes.*;
import org.lwjgl.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
/**
 * A generator class used to build the world
 */
public class WorldBuilder implements WorldBuilderInterface {
    private static final Logger logger = LoggerFactory.getLogger(WorldBuilder.class);
    private WorldGenParamBag worldGenParamBag;
    private Random random;

    // New up the biomes used in the game
    private TemperateDesert temperateDesert = new TemperateDesert(BiomeType.TEMPERATE_DESERT);
    private TropicalRainforest tropicalRainforest = new TropicalRainforest(BiomeType.TROPICAL_RAINFOREST);
    private Snow snow = new Snow(BiomeType.SNOW);
    private Grassland grassland = new Grassland(BiomeType.GRASSLAND);
    private Beach beach = new Beach(BiomeType.BEACH);
    private Ice ice = new Ice(BiomeType.ICE);
    private Lake lake = new Lake(BiomeType.LAKE);
    private Ocean ocean = new Ocean(BiomeType.OCEAN);
    private MountainRocks mountainRocks = new MountainRocks(BiomeType.MOUNTAIN_ROCKS);
    private Shrubland shrubland = new Shrubland(BiomeType.SHRUBLAND);
    private TemperateRainforest temperateRainforest = new TemperateRainforest(BiomeType.TEMPERATE_RAINFOREST);
    private Tundra tundra = new Tundra(BiomeType.TUNDRA);
    private SubtropicalDesert subtropicalDesert = new SubtropicalDesert(BiomeType.SUBTROPICAL_DESERT);

    /**
     * WorldBuilder constructor
     * @param worldGenParamBag The param bag used for the world
     */
    public WorldBuilder(WorldGenParamBag worldGenParamBag) {
        this.worldGenParamBag = worldGenParamBag;
        this.random = new Random(System.currentTimeMillis());
        logger.info("Setting world parameters");
        // SET WORLD PARAMS HERE
        try {
            this.worldGenParamBag.setWorldSize(70);
        } catch (IllegalWorldSizeException e ) {
            logger.error(e.getMessage());
            logger.error(e.getMessage());
            return;
        }

        try {
            this.worldGenParamBag.setMapWidth(this.worldGenParamBag.getWorldSize());
            this.worldGenParamBag.setMapHeight(this.worldGenParamBag.getWorldSize());
        } catch (WorldException e ) {
            logger.error(e.getMessage());
            logger.error(String.valueOf(e.getCause()));
            return;
        }

        try {
            this.worldGenParamBag.setNoiseScale(1f);
        } catch (InvalidScaleException e) {
            logger.error(e.getMessage());
            logger.error(String.valueOf(e.getCause()));
            return;
        }

        try {
            this.worldGenParamBag.setSeed(getNonZeroRandomNumber());
            this.worldGenParamBag.setOctaves(2);
            this.worldGenParamBag.setPersistence(0.5f);
            this.worldGenParamBag.setLacunarity(2f);
            this.worldGenParamBag.setBiomeSize(10);
        } catch (WorldGenException e) {
            logger.error(e.getMessage());
            logger.error(String.valueOf(e.getCause()));
            return;
        }

        try {
            this.worldGenParamBag.setOceanQuantity(0.8f); // CAREFUL OF HIGHER VALUES
        } catch (WorldGenException e) {
            logger.error(e.getMessage());
            logger.error(String.valueOf(e.getCause()));
            return;
        }

        this.worldGenParamBag.initialiseHeightMap(this.worldGenParamBag.getMapWidth(),
                this.worldGenParamBag.getMapHeight());
        this.worldGenParamBag.initialiseTileMap();
        this.worldGenParamBag.initialiseBiomeHeightMap();
        this.worldGenParamBag.initialiseBiomeMoistureMap();
    }

    /**
     * Generates the heightMap used to assign biomes types
     */
    @Override
    public void generateWorldNoise() {
        NoiseMap heightMap = new NoiseMap(
                this.worldGenParamBag.getMapWidth(),
                this.worldGenParamBag.getMapHeight(),
                this.worldGenParamBag.getSeed(),
                this.worldGenParamBag.getNoiseScale(),
                this.worldGenParamBag.getOctaves(),
                this.worldGenParamBag.getPersistence(),
                this.worldGenParamBag.getLacunarity()
        );
        this.worldGenParamBag.setHeightMap(heightMap.generateHeightMap());
    }

    /**
     * Set's the biome type for a VoronoiPoint
     *
     * @param voronoiPoint The VoronoiPoint to set the biome for
     */
    @Override
    public void setBiomeTypeForVoronoiPoint(VoronoiPoint voronoiPoint) {
        // Set any point below the Ocean height
        if (voronoiPoint.getElevation() <= this.worldGenParamBag.getBiomeHeightMap()
                .get(BiomeType.OCEAN)) {  //0.32f
            voronoiPoint.setBiome(BiomeType.OCEAN);
        } else if (voronoiPoint.getElevation() > 0.8f) {
            if (voronoiPoint.getMoisture() > 0.50f) {
                voronoiPoint.setBiome(BiomeType.SNOW);
            } else if (voronoiPoint.getMoisture() > 0.33f) {
                voronoiPoint.setBiome(BiomeType.TUNDRA);
            } else if (voronoiPoint.getMoisture() > 0.16f) {
                voronoiPoint.setBiome(BiomeType.MOUNTAIN_ROCKS);
            }
        } else if (voronoiPoint.getElevation() > 0.6f) {
            if (voronoiPoint.getMoisture() > 0.33) {
                voronoiPoint.setBiome(BiomeType.SHRUBLAND);
            } else {
                voronoiPoint.setBiome(BiomeType.TEMPERATE_DESERT);
            }
        } else if (voronoiPoint.getElevation() > 0.3f) {
            if (voronoiPoint.getMoisture() > 0.83f) {
                voronoiPoint.setBiome(BiomeType.TEMPERATE_RAINFOREST);
            } else if (voronoiPoint.getMoisture() > 0.16f) {
                voronoiPoint.setBiome(BiomeType.GRASSLAND);
            } else {
                voronoiPoint.setBiome(BiomeType.TEMPERATE_DESERT);
            }
        } else {
            if (voronoiPoint.getMoisture() > 0.66f) {
                voronoiPoint.setBiome(BiomeType.TROPICAL_RAINFOREST);
            } else if (voronoiPoint.getMoisture() > 0.16f) {
                voronoiPoint.setBiome(BiomeType.GRASSLAND);
            } else {
                voronoiPoint.setBiome(BiomeType.SUBTROPICAL_DESERT);
            }
        }
    }

    /**
     * Generates the Tile objects in the world
     */
    @Override
    public void generateTiles() {
        float halfHeight = Math.floorDiv(this.worldGenParamBag.getMapHeight(), 2);
        float halfWidth = Math.floorDiv(this.worldGenParamBag.getMapWidth(), 2);
        float oddCol;

        for (int height = 0; height < this.worldGenParamBag.getMapHeight(); height++) {
            for (int width = 0; width < this.worldGenParamBag.getMapWidth(); width++) {
                if (this.worldGenParamBag.getMapHeight() % 2 == 0) {
                    oddCol = (height % 2 == 0 ? 0.5f : 0);
                } else {
                    oddCol = (height % 2 != 0 ? 0.5f : 0);
                }

                BiomeType biome = VoronoiNoise.findNearestPoint(width -25, height-25,
                        this.worldGenParamBag.getVoronoiPoints()).getBiome();
                Tile tile = new Tile(null, height - halfHeight,
                        width + oddCol - halfWidth);
                switch (biome) {
                    case SNOW:
                        //set the tile's biome
                        tile.setBiome(snow);
                        //update its texture
                        snow.setBiomeTexture(tile);
                        //add to the biome's list of tiles
                        snow.addTileToBiome(tile);
                        break;
                    case BEACH:
                        tile.setBiome(beach);
                        beach.setBiomeTexture(tile);
                        beach.addTileToBiome(tile);
                        break;
                    case TUNDRA:
                        tile.setBiome(tundra);
                        tundra.setBiomeTexture(tile);
                        tundra.addTileToBiome(tile);
                        break;
                    case OCEAN:
                        tile.setBiome(ocean);
                        tile.setObstructed(true);
                        ocean.setBiomeTexture(tile);
                        ocean.addTileToBiome(tile);
                        break;
                    case GRASSLAND:
                        tile.setBiome(grassland);
                        grassland.setBiomeTexture(tile);
                        grassland.addTileToBiome(tile);
                        break;
                    case SHRUBLAND:
                        tile.setBiome(shrubland);
                        shrubland.setBiomeTexture(tile);
                        shrubland.addTileToBiome(tile);
                        break;
                    case MOUNTAIN_ROCKS:
                        tile.setBiome(mountainRocks);
                        mountainRocks.setBiomeTexture(tile);
                        mountainRocks.addTileToBiome(tile);
                        break;
                    case TEMPERATE_DESERT:
                        tile.setBiome(temperateDesert);
                        temperateDesert.setBiomeTexture(tile);
                        temperateDesert.addTileToBiome(tile);
                        break;
                    case SUBTROPICAL_DESERT:
                        tile.setBiome(subtropicalDesert);
                        subtropicalDesert.setBiomeTexture(tile);
                        subtropicalDesert.addTileToBiome(tile);
                        break;
                    case TROPICAL_RAINFOREST:
                        tile.setBiome(tropicalRainforest);
                        tropicalRainforest.setBiomeTexture(tile);
                        tropicalRainforest.addTileToBiome(tile);
                        break;
                    case TEMPERATE_RAINFOREST:
                        tile.setBiome(temperateRainforest);
                        temperateRainforest.setBiomeTexture(tile);
                        temperateRainforest.addTileToBiome(tile);
                        break;
                    case LAKE:
                        tile.setBiome(lake);
                        lake.setBiomeTexture(tile);
                        lake.addTileToBiome(tile);
                        break;
                    case ICE:
                        tile.setBiome(ice);
                        ice.setBiomeTexture(tile);
                        ice.addTileToBiome(tile);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + biome);
                }
            }
        }
        logger.info("FINISHED ADDING TILES IN WORLD BUILDER");
        setWorldBiomeMapList();
    }

    /**
     * Generates the biomes within the game
     */
    @Override
    public void generateBiomes() {
        // Generate Voronoi points
        generateVoronoi();

        VoronoiPoint[] voronoi = this.worldGenParamBag.getVoronoiPoints();
        int pointCount = VoronoiNoise.getVoronoiCount();
        for (int i = 0; i < pointCount; i++) {
            setBiomeTypeForVoronoiPoint(voronoi[i]);
        }
    }

    /**
     * Generates the VoronoiPoint to create the map and biomes
     */
    @Override
    public void generateVoronoi() {
        VoronoiNoise voronoi = new VoronoiNoise(this.worldGenParamBag,
                this.worldGenParamBag.getSeed(),
                this.worldGenParamBag.getMapWidth(),
                this.worldGenParamBag.getMapHeight(),
                this.worldGenParamBag.getBiomeSize());
        WorldGenParamBag.setVoronoiPoints(voronoi.getPoints());
    }

    /**
     * Get's a biome instance within the world
     *
     * @param biomeType The type of biome to get within the world
     * @return A biome used in the world, else null if the biome does now
     * exist in the world
     */
    @Override
    public AbstractBiome getBiomeInWorld(BiomeType biomeType) {
        if (biomeType.equals(snow.getBiomeType())) {
            return snow;
        } else if (biomeType.equals(tropicalRainforest.getBiomeType())) {
            return tropicalRainforest;
        } else if (biomeType.equals(grassland.getBiomeType())) {
            return grassland;
        } else if (biomeType.equals(temperateDesert.getBiomeType())) {
            return temperateDesert;
        } else if (biomeType.equals(beach.getBiomeType())) {
            return beach;
        } else if (biomeType.equals(temperateRainforest.getBiomeType())) {
            return temperateRainforest;
        } else if (biomeType.equals(ocean.getBiomeType())) {
            return ocean;
        } else if (biomeType.equals(ice.getBiomeType())) {
            return ice;
        } else if (biomeType.equals(lake.getBiomeType())) {
            return lake;
        } else if (biomeType.equals(mountainRocks.getBiomeType())) {
            return mountainRocks;
        } else if (biomeType.equals(shrubland.getBiomeType())) {
            return shrubland;
        } else if (biomeType.equals(tundra.getBiomeType())) {
            return tundra;
        } else if (biomeType.equals(subtropicalDesert.getBiomeType())) {
            return subtropicalDesert;
        }
        return null;
    }

    /**
     * Get's a non-zero random int
     * @return A non-zero random int
     */
    private int getNonZeroRandomNumber() {
        int num = 0;
        while (num <= 0) {
            num = this.random.nextInt();
        }
        return num;
    }

    /**
     * Private helper function to set the map of biomes and their tiles for
     * the world
     */
    private void setWorldBiomeMapList() {
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.SNOW,
                (CopyOnWriteArrayList<Tile>)snow.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.BEACH,
                (CopyOnWriteArrayList<Tile>)beach.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.TUNDRA
                , (CopyOnWriteArrayList<Tile>)tundra.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.OCEAN,
                (CopyOnWriteArrayList<Tile>)ocean.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.GRASSLAND,
                (CopyOnWriteArrayList<Tile>)grassland.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.SHRUBLAND,
                (CopyOnWriteArrayList<Tile>)shrubland.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.MOUNTAIN_ROCKS,
                (CopyOnWriteArrayList<Tile>)mountainRocks.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.TEMPERATE_DESERT,
                (CopyOnWriteArrayList<Tile>)temperateDesert.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.SUBTROPICAL_DESERT,
                (CopyOnWriteArrayList<Tile>)subtropicalDesert.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.TROPICAL_RAINFOREST,
                (CopyOnWriteArrayList<Tile>)tropicalRainforest.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.TEMPERATE_RAINFOREST,
                (CopyOnWriteArrayList<Tile>)temperateRainforest.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.LAKE,
                (CopyOnWriteArrayList<Tile>)lake.getTileList());
        this.worldGenParamBag.getMapOfTilesInBiomes().put(BiomeType.ICE,
                (CopyOnWriteArrayList<Tile>)ice.getTileList());
    }
}
