package deco.combatevolved.tasks;

import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.enemyentities.FlyingEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.ZapTower;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.managers.EnemyManager;

import java.util.List;

/**
 * A class that finds a list of all the enemies alive and
 * determines the closest one within the towers range
 * and sets it to be the target
 */
public class TowerTargetTask extends AbstractTask {
    private boolean taskAlive = true;
    private boolean taskComplete = false;
    private int range;
    private BasicEnemy target;
    private Tower tower;

    /**Simple constructor for the targetting task
     *
     * @param tower tower that is finding a target
     */
    public TowerTargetTask(Tower tower) {
        super(tower);

        this.tower = tower;
        this.range = tower.getRange();
        
    }

    /** Getter method
     * @return boolean of if the task is finished
     */
    @Override
    public boolean isComplete() {
        return taskComplete;
    }

    /** Getter method
     * @return boolean of if the task is alive
     */
    @Override
    public boolean isAlive() {
        return taskAlive;
    }

    /**
     * On tick is called periodically (time dependant on the world settings)
     * Each tick searches for enemies within the towers range, then finds the closest of those
     * If none are found target should be null
     * @param tick Current game tick
     */
    @Override
    public void onTick(long tick) {

        //Gets a list of enemies
        List<BasicEnemy> enemyList = GameManager.get().getManager(EnemyManager.class).getEnemyList();
        int size = enemyList.size();
        float closestDist = 0;
        float currentDist;
        
        //Finds the closest within the range
        for (int i = 0; i < size; i++) {
            currentDist = tower.distance(enemyList.get(i));
            if (currentDist < range && currentDist > closestDist) {

                //Ensures that only ZapTowers can target Flying Enemies
                if (enemyList.get(i) instanceof FlyingEnemy) {
                    if (tower instanceof ZapTower) {
                        target = enemyList.get(i);
                        closestDist = currentDist;
                        taskComplete = true;
                    }
                } else {
                    target = enemyList.get(i);
                    closestDist = currentDist;
                    taskComplete = true;
                }


            }
        }
    }

    /**Aims the tower towards the enemy that is given
     *
     * @param target the enemy being shot
     * @param tower the tower being rotated
     */
    public static void aim(BasicEnemy target, Tower tower) {

        //Calculates angle from the enemy to the tower
        float enemy_x = target.getCol();
        float enemy_y = target.getRow();
        float tower_x = tower.getCol();
        float tower_y = tower.getRow();

        float x_diff = enemy_x-tower_x;
        float y_diff = enemy_y-tower_y;

        float angle = (float) Math.toDegrees(Math.atan2(y_diff, x_diff));
        if(angle < 0){
            angle += 360;
        }

        //Sets the tower texture depending on the angle it is shooting at
        String suffix = "";

        if(angle >= 0 && angle <= 30) {
            suffix = "_UR";
        } else if(angle >= 30 && angle <= 90) {
            suffix = "_U";
        } else if(angle >= 90 && angle <= 130) {
            suffix = "_UL";
        } else if(angle >= 130 && angle <= 210) {
            suffix = "_DL";
        } else if(angle >= 210 && angle <= 270) {
            suffix = "_D";
        } else if(angle >= 270 && angle <= 350) {
            suffix = "_DR";
        } else if(angle >= 350 && angle <= 360) {
            suffix = "_UR";
        }

        if (!(tower instanceof ZapTower)) {
            tower.setTexture(tower.getTowerName() + suffix);
        }
        tower.setSuffix(suffix);


    }

    /**Called when the task has been completed - returning the target found and
     * setting the task to dead
     * @return target - BasicEnemy to be attacked
     */
    public BasicEnemy getTarget() {
        taskAlive = false;
        return target;
    }
}
