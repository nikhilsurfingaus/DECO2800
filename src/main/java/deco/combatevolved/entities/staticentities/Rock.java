package deco.combatevolved.entities.staticentities;
import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.worlds.Tile;

public class Rock extends StaticEntity implements HasHealth {
    private int health = 100;
    private final int MAX_HEALTH = 100;
    
    public Rock() {
    }

    public Rock(Tile tile, boolean obstructed) {
        super(tile, RenderConstants.ROCK_RENDER, "rock", obstructed);
    }

    /**
     * Rock constructor
     * @param tile The tile to render the rock on
     * @param texture The texture to render the rock as
     * @param obstructed Determine if the tile the rock is rendered on will
     *                   be marked as obstructed or not
     */
    public Rock(Tile tile, String texture, boolean obstructed) {
        super(tile, RenderConstants.ROCK_RENDER, texture, obstructed);
    }

    @Override
    public void onTick(long i) {
        // Adding this in here to stop sonarqube smell
    }

    @Override
    public int getHealth() { return health; }

    @Override
    public void loseHealth(int lossHealth) {
        this.health -= lossHealth;
    }

    @Override
    public void gainHealth(int gainHealth) {
        this.health += gainHealth;
    }

    @Override
    public void gainFullHealth() { this.health = this.MAX_HEALTH; }

    public void death() { this.health = 0; }
}
