package deco.combatevolved.factories;

import deco.combatevolved.entities.enemyentities.*;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.biomes.BiomeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnemyEntityFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnemyEntityFactory.class);
    private static final String SNOW = "Snow";
    private static final String DESERT = "Desert";
    private static final String PLAINS = "Plains";
    private static final String FOREST = "Forest";

    // The enemy that will be created
    private static BasicEnemy enemy;


    /**
     *  This class is used a way of spawning all types of enemies from the
     *  same location
     */
    private EnemyEntityFactory(){
        // Hiding this constructor as it's not called anywhere. If the
        // constructor is needed then change it back to public
    }

    /**
     *  This creates an enemy of the type parsed in and its coordinates
     * @param enemyType - The type of enemy to be created
     * @param row - the row the enemy will be spawned on
     * @param col - the column the enemy will be spawned
     * */

    public static BasicEnemy createEnemy(String enemyType, float col, float row) {

        switch(enemyType) {
            case "fast":
                enemy = new FastEnemy(col, row);
                break;
            case "healer":
                enemy = new HealerEnemy(col, row);
                break;
            case "flying":
                enemy = new FlyingEnemy(col, row);
                break;
            case "heavy":
                enemy = new HeavyEnemy(col, row);
                break;
            case "vehicle":
                enemy = new VehicleEnemy(col, row);
                break;
            case "small":
                enemy = new SmallEnemy(col, row);
                break;
            case "explosive":
                enemy = new ExplosiveEnemy(col, row);
                break;
            case "ranged":
                enemy = new RangeEnemy(col, row);
                break;
            case "ranger":
                enemy = new RangerEnemy(col, row);
                break;
            case "biome":
                enemy = createBiomeEnemy(col, row);
                break;
            case "boss":
                enemy = createBossEnemy(col, row);
                break;
            case "snowman":
                enemy = new SnowEnemy(col, row);
                break;
            case "rainforest":
                enemy = new RainforestEnemy(col, row);
                break;
            default:
                enemy = new BasicEnemy(col, row);
        }
        return enemy;
    }

    public static String getHomeBiome(float col, float row) {
        HexVector test = new HexVector(col,row);
        float error = 1f;
        LOGGER.trace("Position Row: {} Col {}", row, col);
        String biome = GameManager.get().getWorld().getTile(test, error).getBiome();
        if (biome.contains(BiomeType.SNOW.asLowerCase())
                || biome.contains(BiomeType.ICE.asLowerCase())
                || biome.contains(BiomeType.TUNDRA.asLowerCase())) {
            return SNOW;
        } else if (biome.contains(BiomeType.TEMPERATE_DESERT.asLowerCase())
                || biome.contains(BiomeType.SUBTROPICAL_DESERT.asLowerCase())) {
            return DESERT;
        } else if (biome.contains(BiomeType.GRASSLAND.asLowerCase())) {
            return PLAINS;
        } else if (biome.contains(BiomeType.TROPICAL_RAINFOREST.asLowerCase())
                || biome.contains(BiomeType.TEMPERATE_RAINFOREST.asLowerCase())
                || biome.contains(BiomeType.SHRUBLAND.asLowerCase())) {
            return FOREST;
        } else {
            return PLAINS;
        }
    }

    // TODO: THIS LOOKS BAD
    private static BasicEnemy createBiomeEnemy(float col, float row) {
        String biome = getHomeBiome(col, row);
        LOGGER.trace("biome: {}", biome);
        switch(biome) {
            case SNOW:
                enemy = new SnowEnemy(col, row);
                break;
            case DESERT:
                enemy = new DesertEnemy(col, row);
                break;
            case FOREST:
                enemy = new RainforestEnemy(col, row);
                break;
            case PLAINS:
                enemy = new BasicEnemy(col, row);
                break;
            default:
                break;
        }
        return enemy;
    }

    private static BasicEnemy createBossEnemy(float col, float row) {
        String biome = getHomeBiome(col, row);
        switch(biome) {
            case PLAINS:
                enemy = new BasicBossEnemy(col, row);
                break;
            case SNOW:
                enemy = new SnowBossEnemy(col, row);
                break;
            case DESERT:
                enemy = new DesertBossEnemy(col, row);
                break;
            case FOREST:
                enemy = new RainforestBossEnemy(col, row);
                break;
            default:
                break;
        }
        return enemy;
    }
}

