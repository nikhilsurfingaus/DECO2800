package deco.combatevolved.entities.enemyentities;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.worlds.biomes.BiomeType;

public class RangerEnemy extends BasicEnemy {

    private boolean stealth;
    private long timeSpawned;

    /**
     *  Constructs a ranger enemy that resembles a lizard
     *  Abilities: Can be camouflaged
     * @param row the row coordinate of the enemy
     * @param col the row coordinate of the enemy
     */
    public RangerEnemy(float col, float row) {
        super(col, row);
        this.speed = 0.05f;
        this.damage = 15;
        this.health = 50;
        this.maxHealth = this.health;
        this.armourHealth = 0;
        this.exp = 20;
        this.timeSpawned = System.currentTimeMillis();
        homeBiome = BiomeType.TEMPERATE_RAINFOREST.asLowerCase();

        moveSound = "Lizard_Movement.mp3";
        attackSound = "Lizard_Attack.mp3";
        damageSound = "Lizard_Damage.mp3";
        deathSound = "Lizard_Death.mp3";
    }

    /**
     *  Determines what the enemy will do on every frame
     * @param i
     */
    @Override
    public void onTick(long i) {
        AbstractTask newTask = getTask(i);
        checkStealth();
        play_move_sound(newTask);
        setEnemyTexture(newTask);
    }

    /**
     * checks if the enemy should be stealth
     */
    public void checkStealth() {
        if (System.currentTimeMillis() > timeSpawned + 2000) {
            this.stealth = true;
        } else {
            for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
                if ((entity instanceof PlayerPeon) && (entity.distance(this) < 2)) {
                    this.stealth = false;
                    break;
                } else {
                    this.stealth = true;
                }
            }
        }
    }

    /**
     * Sets the enemy to be stealth
     * @param stealth
     */
    public void setStealth(boolean stealth) {
        this.stealth = stealth;
    }

    /**
     *  Takes an angle and sets the appropriate texture
     * @param angle - direction that enemy will move
     */
    @Override
    public void setTextureByAngle(double angle) {
        enemyType = stealth ? "STRanger_Lizard" : "Ranger_Lizard";
        super.setTextureByAngle(angle);
    }

}
