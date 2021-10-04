package deco.combatevolved.tasks;

import deco.combatevolved.entities.dynamicentities.Bullet;
import deco.combatevolved.entities.dynamicentities.TowerBullet;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.enemyentities.FlyingEnemy;
import deco.combatevolved.entities.staticentities.defensivetowers.*;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.managers.EnemyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A task which deals damage to the enemy that is passed to it
 */
public class TowerAttackTask extends AbstractTask {
    private boolean taskAlive = true;
    private boolean taskComplete = false;
    private BasicEnemy target;
    private Tower tower;
    private int range;

    /** Simple constructor for attacking task
     *
     * @param tower the tower attacking
     * @param enemy the enemy being shot
     */
    public TowerAttackTask(Tower tower, BasicEnemy enemy) {
        super(tower);
        this.target = enemy;
        this.tower = tower;
        this.range = tower.getRange();
    }

    /** Does damage every game tick - will be made slower in the future
     *
     * @param tick Current game tick
     */
    @Override
    public void onTick(long tick) {
        if (tower.distance(target) <= range && target.getHealth() != 0) {
            //Try to damage the enemy
//                TowerTargetTask.aim(target, tower);
                if (target instanceof FlyingEnemy) {

                    //Makes sure that only the ZapTower can damage flying enemies
                    if (tower instanceof ZapTower) {
                        //Aims and shoots bullet
                        shootEnemy(tower, target);

                    }
                } else if (tower instanceof SlowTower) {
                    //Slow towers slow enemies to half speed before damaging them
                    //Does so using slowEnemy method
                    GameManager.getManagerFromInstance(EnemyManager.class).slowEnemy(target);
                    shootEnemy(tower, target);

                } else if (tower instanceof MultiTower) {


                    //Aims and shoots bullet
                    multiShoot(tower);

                } else if (tower instanceof SplashTower) {
                    shootEnemy(tower, target);
                    splashHit(target);
                } else {
                    //For all normal towers/enemies
                    //Aims and shoots bullet
                    shootEnemy(tower, target);
                }

            taskComplete = true;
            taskAlive = false;
        } else {
            getLogger().info(String.format("%s killed %s", tower, target));
            taskComplete = true;

        }

    }

    /**A method used to handle the firing of projectiles at enemies from towers
     * Aims the tower in the right direction before firing the bullet
     *
     * @param tower the tower that is attacking
     * @param target the enemy being shot
     */
    public void shootEnemy(Tower tower, BasicEnemy target) {

        //Aim rotates the tower to face the enemy
        if (tower.isRotatable()) {
            TowerTargetTask.aim(target, tower);
        }

        //Logs the attacking of the enemy
        getLogger().info(String.format("%s range attacked %s", tower, target));

        HexVector position = tower.getPosition();
        HexVector direction = target.getPosition().subtract(position);

        direction = direction.normalise().multiply(0.2f);
        // fire bullet in the direction of the target
        TowerBullet bullet = new TowerBullet(position, direction, tower.getRange(),
                tower.getDamage(), tower.getTowerName(), tower.getSuffix());

        //Adds the bullet to the world
        GameManager.get().getWorld().addEntity(bullet);

        GameManager.get().getManager(SoundManager.class).playTowerSound(tower.getTowerName() + "_sound.wav");
    }

    /**A method used to handle the firing of projectiles at enemies from the multitower
     * Fires 6 bullets in spread out directions
     *
     * @param tower the tower that is attacking
     */
    public void multiShoot(Tower tower) {

        HexVector position = tower.getPosition();
        ArrayList<HexVector> directions = new ArrayList<HexVector>();
        directions.add(new HexVector(0,1));
        directions.add(new HexVector(1,1));
        directions.add(new HexVector(1,-1));
        directions.add(new HexVector(0,-1));
        directions.add(new HexVector(-1,-1));
        directions.add(new HexVector(-1,1));

        // fire bullet in the direction of the target
        for (int i = 0; i < 6; i++) {
            HexVector direction = directions.get(i);
            direction = direction.normalise().multiply(0.2f);
            TowerBullet bullet = new TowerBullet(position, direction, tower.getRange(),
                    tower.getDamage(), tower.getTowerName(), tower.getSuffix());
            GameManager.get().getWorld().addEntity(bullet);

        }

        GameManager.get().getManager(SoundManager.class).playTowerSound("simpletower_sound.wav");

    }

    /**Deals damage to all enemies in a set range around the target enemy
     * used for the splash tower
     *
     * @param target the enemy being shot at
     */
    public void splashHit(BasicEnemy target) {
        List<BasicEnemy> enemyList = GameManager.get().getManager(EnemyManager.class).getEnemyList();
        int size = enemyList.size();

        for (BasicEnemy enemy: enemyList) {
            if (target.distance(enemy) <= 2f) {
                try {
                    enemy.loseHealth(15);
                } catch (EnemyValueException ignored) {};
            }
        }
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

    /** Getter method
     *
     * @return which enemy is being targetted
     */
    public BasicEnemy getTarget() {
        return target;
    }



    public static Logger getLogger() {return LoggerFactory.getLogger(TowerAttackTask.class);}
}
