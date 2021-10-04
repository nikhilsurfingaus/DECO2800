package deco.combatevolved.entities.staticentities.defensivetowers;
import deco.combatevolved.worlds.Tile;

/**
 * A splash tower which has attacks which splash onto nearby enemies
 * -Splash function not finished as of sprint 1
 */
public class SplashTower extends Tower {
    /**
     * A splash tower which has a tile to be created
     */
    public SplashTower(Tile tile, boolean obstructed) {
        //Inherit from the constants
        super("splashtower",
                TowerConstants.SPLASH_TOWER_MAX_HEALTH,
                TowerConstants.SPLASH_TOWER_DAMAGE,
                TowerConstants.SPLASH_TOWER_RANGE,
                TowerConstants.SPLASH_TOWER_ATTACK_SPEED,
                tile, obstructed, TowerConstants.SPLASH_TOWER_ROTATE);
    }

    /**
     * A splash tower which has a coloumn and row to be created
     */
    public SplashTower(float col, float row) {
        //Inherit from the constants
        super("splashtower",
                TowerConstants.SPLASH_TOWER_MAX_HEALTH,
                TowerConstants.SPLASH_TOWER_DAMAGE,
                TowerConstants.SPLASH_TOWER_RANGE,
                TowerConstants.SPLASH_TOWER_ATTACK_SPEED,
                col, row, TowerConstants.SPLASH_TOWER_ROTATE);
    }
}
