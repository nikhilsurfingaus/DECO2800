package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.EnemyArtificialIntelligence;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.tasks.MeleeAttackTask;
import deco.combatevolved.worlds.biomes.BiomeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SnowEnemy extends BasicEnemy {
    private static final Logger LOGGER = LoggerFactory.getLogger(SnowEnemy.class);

    /**
     *  Constructs a snow enemy that resembles a snowman
     *  Abilities: Can freeze towers
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public SnowEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.05f;
        this.damage = 5;
        this.health = 70;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 6;
        enemyType = "Snowman";
        homeBiome = BiomeType.SNOW.asLowerCase();

        moveSound = "Snowman_Movement.mp3";
        attackSound = "Snowman_Attack.mp3";
        damageSound = "Snowman_Damage.mp3";
        deathSound = "Snowman_Death.mp3";
    }

    /**
     *  Determines what the enemy will do on every frame
     * @param i
     */
    @Override
    public void onTick(long i) {
        AbstractTask newTask = getTask(i);
        LOGGER.trace("task: {}", this.task);
        freezeAbility(newTask);
        play_move_sound(newTask);
        setEnemyTexture(newTask);
    }

    /**
     * Freezes the target of the enemy
     * @param task
     */
    public void freezeAbility(AbstractTask task) {
        if(task instanceof MeleeAttackTask) {
            LOGGER.trace("Finding freeze target");
            AbstractEntity entity = ((EnemyArtificialIntelligence) ai).getTarget(this);
            if (entity instanceof Tower) {
                LOGGER.trace("freezing Tower");
                ((Tower)entity).setFreezeTime();
            }
        }
    }
}