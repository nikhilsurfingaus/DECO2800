package deco.combatevolved.entities.staticentities.defensivetowers;
import deco.combatevolved.worlds.Tile;

/**
 * A zapping tower which shoots at flying enemies first
 */
public class ZapTower extends Tower {

    /**
     * A zap tower which has a tile to be created
     */
    public ZapTower(Tile tile, boolean obstructed) {
        //Inherit from the constants
        super("zaptower",
                TowerConstants.ZAP_TOWER_MAX_HEALTH,
                TowerConstants.ZAP_TOWER_DAMAGE,
                TowerConstants.ZAP_TOWER_RANGE,
                TowerConstants.ZAP_TOWER_ATTACK_SPEED,
                tile, obstructed, TowerConstants.ZAP_TOWER_ROTATE);
    }

    /**
     * A zap tower which has a column and row to be created
     */
    public ZapTower(float col, float row) {
        //Inherit from the constants
        super("zaptower",
                TowerConstants.ZAP_TOWER_MAX_HEALTH,
                TowerConstants.ZAP_TOWER_DAMAGE,
                TowerConstants.ZAP_TOWER_RANGE,
                TowerConstants.ZAP_TOWER_ATTACK_SPEED,
                col, row, TowerConstants.ZAP_TOWER_ROTATE);
    }

}
