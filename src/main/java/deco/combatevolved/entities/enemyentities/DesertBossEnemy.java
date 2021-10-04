package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.BossEnemyArtificialIntelligence;
import deco.combatevolved.tasks.AbstractTask;

public class DesertBossEnemy extends DesertEnemy {
    // The damage of the poison
    private int poisonDamage;

    /**
     *  Constructs a desert boss enemy that resembles a scorpion
     *  Abilities: Poisons towers and the players
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public DesertBossEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.035f;
        this.damage = 30;
        this.health = 600;
        this.maxHealth = this.health;
        this.armourHealth = 35;
        this.exp = 150;
        this.poisonDamage = 4;
        this.ai = new BossEnemyArtificialIntelligence(getPlayer(), "desert");
        enemyType = "Scorpion";
        includeBiome = false;

        moveSound = "Scorpion_Movement.mp3";
        attackSound = "Wasp_Attack.mp3";
        damageSound = "Wasp_Damage.mp3";
        deathSound = "Wasp_Death.mp3";
    }

    /**
     *  Gets the poison damage of the enemy
     * @return poison damage
     */
    public int getPoisonDamage() {
        return this.poisonDamage;
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
}