package deco.combatevolved.entities.staticentities;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.managers.GameManager;

public class Death extends AbstractEntity {

    private long timeOfDeath;

    public Death() {}

    /**
     * Constructs a death entity that displays when a entity dies
     * @param row - the row the enemy will be spawned on
     * @param col - the column the enemy will be spawned om
     */
    public Death(float col, float row, long timeOfDeath) {
        super(col, row, RenderConstants.PEON_RENDER);
        this.setTexture("enemyHit");
        this.timeOfDeath = timeOfDeath;
    }

    @Override
    public void onTick(long i) {
        if(System.currentTimeMillis() - timeOfDeath > 300) {
            GameManager.get().getWorld().removeEntity(this);
        }
    }

}
