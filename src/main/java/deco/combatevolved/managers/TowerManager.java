package deco.combatevolved.managers;

import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//CURRENTLY NOT USED, WILL BE FIXED NEXT SPRINT
public class TowerManager extends AbstractManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(TowerManager.class);
    private Tower tower;

    public TowerManager(Tower tower) {}


//    public static AbstractTask getTask(Tower tower, BasicEnemy enemy) {
//        return GameManager.getManagerFromInstance(TaskPool.class).getTask(new TowerTaskParameters(tower, enemy));

//    }


}
