package deco.combatevolved.entities.staticentities;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.managers.GameManager;

public class PauseTower extends AbstractEntity {

    private long timeFrozen;

    public PauseTower() {}

    /**
     * Constructs a frozen tower that displays when a tower froze by snow enemy
     * @param row - the row the tower will be spawned on
     * @param col - the column the tower will be spawned on
     */
    public PauseTower(float col, float row, long timeFrozen) {
        super(col, row, RenderConstants.TOWER_RENDER);
        this.setTexture("towerEffect_Pause");
        this.timeFrozen = timeFrozen;
    }

    @Override
    public void onTick(long i) {
        if(System.currentTimeMillis() - timeFrozen > 300) {
            GameManager.get().getWorld().removeEntity(this);
        }
    }

}
