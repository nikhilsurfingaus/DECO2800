package deco.combatevolved.entities.staticentities;


import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.worlds.Tile;

/**
 * Class for the plants of the world
 */
public class Plant extends StaticEntity {
    /**
     * Plant constructor
     * @param tile The tile to set the plant on
     * @param texture The texture to render the plant as
     * @param obstructed determine if the tile will become obstructed or not
     */
    public Plant(Tile tile, String texture, boolean obstructed) {
        super(tile, RenderConstants.PLANT_RENDER, "plant", obstructed);
        setTexture(texture);
    }

    @Override
    public void onTick(long i) {
        // Adding this in here to stop sonarqube smell
    }
}
