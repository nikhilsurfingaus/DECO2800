package deco.combatevolved.tasks;

import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;

public class TowerTaskParameters extends AbstractTaskParameters {
    private BasicEnemy target;
    private boolean targetFound = false;

    public TowerTaskParameters(Tower entity, BasicEnemy target) {
        super(entity);
        this.target = target;
        targetFound = true;
    }

    public TowerTaskParameters(Tower entity) {
        super(entity);
    }

    public BasicEnemy getTarget() {
        return target;
    }

    public boolean isTargetFound() {
        return targetFound;
    }

}
