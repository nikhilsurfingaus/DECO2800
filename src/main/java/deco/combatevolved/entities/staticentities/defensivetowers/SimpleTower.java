package deco.combatevolved.entities.staticentities.defensivetowers;
import deco.combatevolved.worlds.Tile;

/**
 * A simple tower which shoots slowly with little damage
 */
public class SimpleTower extends Tower {

    /**
     * A simple tower which has a tile to be created
     */
    public SimpleTower(Tile tile, boolean obstructed) {
        //Inherit from the constants
        super("simpletower",
                TowerConstants.SIMPLE_TOWER_MAX_HEALTH,
                TowerConstants.SIMPLE_TOWER_DAMAGE,
                TowerConstants.SIMPLE_TOWER_RANGE,
                TowerConstants.SIMPLE_TOWER_ATTACK_SPEED,
                tile, obstructed, TowerConstants.SIMPLE_TOWER_ROTATE);
    }

    /**
     * A simple tower which has a coloumn and row to be created
     */
    public SimpleTower(float col, float row) {
        //Inherit from the constants
        super("simpletower",
                TowerConstants.SIMPLE_TOWER_MAX_HEALTH,
                TowerConstants.SIMPLE_TOWER_DAMAGE,
                TowerConstants.SIMPLE_TOWER_RANGE,
                TowerConstants.SIMPLE_TOWER_ATTACK_SPEED,
                col, row, TowerConstants.SIMPLE_TOWER_ROTATE);
    }

    @Override
    protected void updateImage() {
        // Since there are no damaged images for this tower yet, its texture will be the standard one
        // This override should be deleted when these images are created
    }

}
