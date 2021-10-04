package deco.combatevolved.tasks;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.factories.EnemyEntityFactory;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.Tile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Task that spawns enemies around the given entity
 */
public class SpawnTask extends AbstractTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnemyManager.class);
    private boolean complete;
    private boolean alive;
    private int amount;
    private float radius;
    private String type;

    /**
     * Creates a new spawn task
     * @param entity the entity enemies should be spawned around
     * @param amount the number of enemies to spawn
     * @param radius the maximum distance enemies should be spawned from the
     *               entity
     * @param type the type of enemies to spawn
     */
    public SpawnTask(DynamicEntity entity, int amount, float radius, String type) {
        super(entity);
        this.amount = amount;
        this.radius = radius;
        this.type = type;
        complete = false;
        alive = true;
    }

    public void onTick(long tick) {
        // get a list of possible tiles to spawn enemies at
        List<Tile> tiles = new ArrayList<>();
        for (Tile tile : GameManager.get().getWorld().getTileListFromMap()) {
            if (entity.getPosition().distance(tile.getCoordinates()) < radius) {
                tiles.add(tile);
            }
        }

        // spawn the enemies at random positions from the list
        LOGGER.info("Spawning enemies");
        for (int i = 0; i < amount; i++) {
            int index = ThreadLocalRandom.current().nextInt(tiles.size());
            Tile tile = tiles.remove(index);
            HexVector position = tile.getCoordinates();
            if (!tile.isObstructed()) {
                BasicEnemy enemy = EnemyEntityFactory.createEnemy(type, position.getCol(), position.getRow());
                GameManager.getManagerFromInstance(EnemyManager.class)
                        .addEnemyToGame(enemy);
            } else {
                LOGGER.info("Failed to spawn enemy at obstructed tile");
            }
        }

        complete = true;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isAlive() {
        return alive;
    }

    public void reset(DynamicEntity entity, int amount, float radius, String type) {
        this.entity = entity;
        this.amount = amount;
        this.radius = radius;
        this.type = type;
        complete = false;
        alive = true;
    }
}
