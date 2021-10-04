package deco.combatevolved.entities.staticentities.defensivetowers;
import deco.combatevolved.worlds.Tile;

/**
 * A Multi tower which shoots in multiple directions
 */
public class MultiTower extends Tower {

    /**
     * A sniper tower which has a tile to be created
     */
    public MultiTower(Tile tile, boolean obstructed) {
        //Inherit from the constants
        super("multitower",
                TowerConstants.MULTI_TOWER_MAX_HEALTH,
                TowerConstants.MULTI_TOWER_DAMAGE,
                TowerConstants.MULTI_TOWER_RANGE,
                TowerConstants.MULTI_TOWER_ATTACK_SPEED,
                tile, obstructed, TowerConstants.MULTI_TOWER_ROTATE);
    }

    /**
     * A sniper tower which has a coloumn and row to be created
     */
    public MultiTower(float col, float row) {
        //Inherit from the constants
        super("multitower",
                TowerConstants.MULTI_TOWER_MAX_HEALTH,
                TowerConstants.MULTI_TOWER_DAMAGE,
                TowerConstants.MULTI_TOWER_RANGE,
                TowerConstants.MULTI_TOWER_ATTACK_SPEED,
                col, row, TowerConstants.MULTI_TOWER_ROTATE);
    }

}