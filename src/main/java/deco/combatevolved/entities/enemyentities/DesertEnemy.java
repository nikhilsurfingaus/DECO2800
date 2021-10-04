package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.EnemyArtificialIntelligence;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.tasks.MeleeAttackTask;
import deco.combatevolved.worlds.biomes.BiomeType;

public class DesertEnemy extends BasicEnemy {
    // The damage of the poison
    private int poisonDamage;

    /**
     *  Constructs a desert enemy that resembles a snake
     *  Abilities: Poisons towers and the players
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public DesertEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.05f;
        this.damage = 5;
        this.health = 50;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 7;
        this.poisonDamage = 2;
        enemyType = "Snake";
        homeBiome = BiomeType.TEMPERATE_DESERT.asLowerCase();

        moveSound = "Snake_Movement.mp3";
        attackSound = "Snake_Attack.mp3";
        damageSound = "Snake_Damage.mp3";
        deathSound = "Snake_Death.mp3";
    }

    /**
     *  Determines what the enemy will do on every frame
     * @param i
     */
    @Override
    public void onTick(long i) {
        AbstractTask newTask = getTask(i);
        poison(newTask);
        play_move_sound(newTask);
        setEnemyTexture(newTask);
    }

    public int getPoisonDamage() {
        return this.poisonDamage;
    }

    /**
     * Adds new entities to be poisoned and poisons the entities it has poisoned
     * @param task - The task of the enemy
     */
    public void poison(AbstractTask task) {
        if(task instanceof MeleeAttackTask) {
            AbstractEntity entity = ((EnemyArtificialIntelligence) ai).getTarget(this);
            if (entity instanceof Tower) {
                ((Tower)entity).poison(this);
            }
            if (entity instanceof PlayerPeon) {
                ((PlayerPeon)entity).poison(this);
            }
        }
    }

}