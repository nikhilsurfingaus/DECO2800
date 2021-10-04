package deco.combatevolved.entities.dynamicentities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.util.WorldUtil;
import deco.combatevolved.worlds.AbstractWorld;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.entities.HasHealth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class Bullet extends DynamicEntity {

    // the delay between the player's bullets
    private static final long FIRE_DELAY = 500;

    // the velocity of the bullet
    public static final long VELOCITY = 20;

    // the last time the player fired a bullet
    private static long lastFire = 0;

    private HexVector bulletPosition;
    private HexVector initialPosition;

    private HexVector bulletVelocity;

    private int damage;

    private final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private float distance;

    private int damageDone;

    public Bullet(HexVector position, HexVector velocity, float distance, int damage) {
        super(position.getCol(), position.getRow(), RenderConstants.BULLET_RENDER, 0);
        this.bulletPosition = new HexVector(position.getCol(), position.getRow());
        this.bulletVelocity = new HexVector(velocity.getCol(), velocity.getRow());
        initialPosition = new HexVector(position);
        this.distance = distance;
        this.damage = damage;
        setTexture("enemyBullet");

        damageDone = 0;
    }

    public void update() {
        bulletPosition.setCol(bulletPosition.getCol() + bulletVelocity.getCol());
        bulletPosition.setRow(bulletPosition.getRow() + bulletVelocity.getRow());
    }

    public HexVector getBulletPosition() {
        return bulletPosition;
    }

    public static void keyUpdate(List<Bullet> bullets, PlayerPeon player) {
        // check if the enemy can fire
        if (!canFire()) {
            return;
        }

        // get the direction to fire the bullet and change the magnitude to 20
        HexVector direction = getDirection(player);
        if (direction.getRow() == 0 && direction.getCol() == 0) {
            return;
        }
        direction = direction.multiply(VELOCITY);

        // get the starting position of the bullet
        float[] position = WorldUtil.colRowToWorldCords(player.getCol(), player.getRow());
        HexVector playerPosition = new HexVector(position[0], position[1]);

        // create the bullet and add it to the list of bullets
        Bullet bullet = new Bullet(playerPosition, direction, 10, 10);
        bullets.add(bullet);
        updateFire();
        GameManager.get().getManager(SoundManager.class).playWeaponSound("Sound Effect/EFFECT_fireball_01_active.wav");
    }

    /**
     * Checks the left, right, up and down keys to determine if the player
     * should fire a bullet and returns a unit vector in the direction the
     * bullet should be fired
     * If right shift is pressed, the vector is rotated anticlockwise by 45
     * degrees
     * The zero vector is returned if none of the left, right, up or down keys
     * have been pressed
     * @return a unit vector in the direction a bullet should be fired
     */
    private static HexVector getDirection(PlayerPeon player) {
        int up = player.getControls().getKey("ShootUp");
        int down = player.getControls().getKey("ShootDown");
        int left = player.getControls().getKey("ShootLeft");
        int right = player.getControls().getKey("ShootRight");
        int row = 0;
        int col = 0;

        // check if a bullet should be fired
        if (Gdx.input.isKeyJustPressed(up)) {
            row = 1;
        } else if (Gdx.input.isKeyJustPressed(left)) {
            col = -1;
        } else if (Gdx.input.isKeyJustPressed(down)) {
            row = -1;
        } else if (Gdx.input.isKeyJustPressed(right)) {
            col = 1;
        }

        // check if the direction should be rotated
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
            // rotate the vector anticlockwise by 45 degrees
            return new HexVector(col - row, col + row).normalise();
        } else {
            return new HexVector(col, row);
        }
    }

    public double distanceToEnemy(BasicEnemy enemy) {
        float posX = WorldUtil.colRowToWorldCords(enemy.getCol(), enemy.getRow())[0];
        float posY = WorldUtil.colRowToWorldCords(enemy.getCol(), enemy.getRow())[1];
        return Math.sqrt((this.getBulletPosition().getCol() - posX) * (this.getBulletPosition().getCol() - posX) +
                (this.getBulletPosition().getRow() - posY) * (this.getBulletPosition().getRow() - posY));
    }

    public boolean hitTarget(BasicEnemy enemy, int distance) {
        if(distance < 40){
            distance = 40;
        }
        return this.distanceToEnemy(enemy) < distance;
    }

    /**
     * Check whether the player can fire a bullet at this time
     * A player can only fire a bullet every one second
     * @return true if more than one second has passed since last firing a bullet
     */
    public static boolean canFire() {
        return System.currentTimeMillis() - lastFire > FIRE_DELAY;
    }

    /**
     * Updates the last time the player fired a bullet to be the current time
     */
    public static void updateFire() {
        lastFire = System.currentTimeMillis();
    }

    @Override
    public void onTick(long i) {
        AbstractWorld world = GameManager.get().getWorld();
        // if the bullet as travelled a far enough distance then the bullet is
        // removed
        if (initialPosition.distance(position) > distance) {
            world.removeEntity(this);
            logger.info("Removed bullet {} after travelling distance {}", this, distance);
        }
        // if the bullet has collided with a tower then the tower is damaged
        // and the bullet is removed
        for (AbstractEntity entity : world.getEntities()) {
            if ((entity instanceof Tower || entity instanceof PlayerPeon) &&
                    position.distance(entity.getPosition()) < 0.2f) {
                try {
                    if (entity instanceof PlayerAttributes) { // Now utilising player's defence when being attacked
                        int attack = this.damage;
                        PlayerAttributes player = (PlayerAttributes) entity;
                        int armour = player.getStats("defence");
                        int damageAmount = attack * (100 / (100 + armour));
                        player.loseHealth(damageAmount);
                        this.damageDone = damageAmount;
                    }
                    ((HasHealth) entity).loseHealth(damage);
                } catch (EnemyValueException e) {
                    logger.warn("Bullet {} does negative damage", this);
                }
                logger.info("Bullet {} hit tower {} and caused {} damage",
                        this, entity, damage);
                world.removeEntity(this);
                return;
            }
        }
        position = position.add(bulletVelocity);
    }

    // Added by (@blee01) used to implement a skill
    public int getDamageDone() {
        return this.damageDone;
    }
}
