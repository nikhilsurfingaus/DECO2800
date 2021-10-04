package deco.combatevolved.entities.staticentities.defensivetowers;
import deco.combatevolved.worlds.Tile;

/**
 * A sniper tower which shoots slowly with high damage and range
 */
public class SniperTower extends Tower {

    /**
     * A sniper tower which has a tile to be created
     */
    public SniperTower(Tile tile, boolean obstructed) {
        //Inherit from the constants
        super("snipertower",
                TowerConstants.SNIPER_TOWER_MAX_HEALTH,
                TowerConstants.SNIPER_TOWER_DAMAGE,
                TowerConstants.SNIPER_TOWER_RANGE,
                TowerConstants.SNIPER_TOWER_ATTACK_SPEED,
                tile, obstructed, TowerConstants.SNIPER_TOWER_ROTATE);
    }

    /**
     * A sniper tower which has a coloumn and row to be created
     */
    public SniperTower(float col, float row) {
        //Inherit from the constants
        super("snipertower",
                TowerConstants.SNIPER_TOWER_MAX_HEALTH,
                TowerConstants.SNIPER_TOWER_DAMAGE,
                TowerConstants.SNIPER_TOWER_RANGE,
                TowerConstants.SNIPER_TOWER_ATTACK_SPEED,
                col, row, TowerConstants.SNIPER_TOWER_ROTATE);
    }

}
