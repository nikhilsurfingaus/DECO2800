package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.managers.EnemyManager;

public class ExplosiveEnemy extends BasicEnemy {

    /**
     *  Constructs a explosive enemy that resembles a spider
     *  Abilities: On death the spider explodes and damages everything in its
     *  range
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public ExplosiveEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.05f;
        this.damage = 25;
        this.health = 30;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 10;
        enemyType = "Spider_Explode";

        moveSound = "Spider_Movement.mp3";
        attackSound = "Spider_Attack.mp3";
        damageSound = "Explosive_Damage.mp3";
        deathSound = "Explosive_Death.mp3";
    }

    /**
     * On death the enemy will deal damage to any player within a distance of 5
     * Removes Enemy from the manager and the world and spawns 2 more weaker
     * enemies
     */
    @Override
    public void death() {
        GameManager.get().getManager(EnemyManager.class).removeEnemyFromGame(this);
        for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
            if ((entity instanceof PlayerPeon) && (entity.distance(this) < 5)) {
                ((PlayerPeon) entity).loseHealth(this.damage);
            } else if ((entity instanceof Tower) && (entity.distance(this) < 5)) {
                ((Tower) entity).takeDamage(this.damage);
            }
        }
    }
}
