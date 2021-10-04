package deco.combatevolved.entities.staticentities.defensivetowers;
import deco.combatevolved.worlds.Tile;

/**
 * A slow tower which slows enemies
 */
public class SlowTower extends Tower {

    /**
     * A slow tower which has a tile to be created
     */
    public SlowTower(Tile tile, boolean obstructed) {
        //Inherit from the constants
        super("slowtower",
                TowerConstants.SLOW_TOWER_MAX_HEALTH,
                TowerConstants.SLOW_TOWER_DAMAGE,
                TowerConstants.SLOW_TOWER_RANGE,
                TowerConstants.SLOW_TOWER_ATTACK_SPEED,
                tile, obstructed, TowerConstants.SLOW_TOWER_ROTATE);
    }

    /**
     * A slow tower which has a coloumn and row to be created
     */
    public SlowTower(float col, float row) {
        //Inherit from the constants
        super("slowtower",
                TowerConstants.SLOW_TOWER_MAX_HEALTH,
                TowerConstants.SLOW_TOWER_DAMAGE,
                TowerConstants.SLOW_TOWER_RANGE,
                TowerConstants.SIMPLE_TOWER_ATTACK_SPEED,
                col, row, TowerConstants.SLOW_TOWER_ROTATE);
    }
    

}
