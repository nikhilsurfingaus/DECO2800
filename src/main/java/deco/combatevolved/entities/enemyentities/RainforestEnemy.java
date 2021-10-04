package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.worlds.biomes.BiomeType;

public class RainforestEnemy extends BasicEnemy {

    /**
     *  Constructs a rainforest enemy that resembles a tree lizard
     *  Abilities: Camouflaged as a tree
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public RainforestEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.05f;
        this.damage = 7;
        this.health = 140;
        this.maxHealth = this.health;
        this.armourHealth = 10;
        this.exp = 20;
        enemyType = "Tree_Lizard";
        homeBiome = BiomeType.TEMPERATE_RAINFOREST.asLowerCase();

        moveSound = "Forest_Movement.mp3";
        attackSound = "Lizard_Attack.mp3";
        damageSound = "Forest_Damage.mp3";
        deathSound = "Forest_Death.mp3";
    }
}