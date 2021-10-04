package deco.combatevolved.entities.staticentities.defensivetowers;

/**
 * A class to store all the constant values required for the defensive towers
 */
public class TowerConstants {

    public static final int SIMPLE_TOWER_DAMAGE = 20;
    public static final int SIMPLE_TOWER_RANGE = 4;
    public static final int SIMPLE_TOWER_MAX_HEALTH = 150;
    public static final float SIMPLE_TOWER_ATTACK_SPEED = 0.5f;
    public static final boolean SIMPLE_TOWER_ROTATE = true;

    public static final int SPLASH_TOWER_DAMAGE = 15;
    public static final int SPLASH_TOWER_RANGE = 4;
    public static final int SPLASH_TOWER_MAX_HEALTH = 250;
    public static final float SPLASH_TOWER_ATTACK_SPEED = 2f;
    public static final boolean SPLASH_TOWER_ROTATE = true;

    public static final int SNIPER_TOWER_DAMAGE = 160;
    public static final int SNIPER_TOWER_RANGE = 8;
    public static final int SNIPER_TOWER_MAX_HEALTH = 100;
    public static final float SNIPER_TOWER_ATTACK_SPEED = 2f;
    public static final boolean SNIPER_TOWER_ROTATE = true;

    public static final int SLOW_TOWER_DAMAGE = 10;
    public static final int SLOW_TOWER_RANGE = 6;
    public static final int SLOW_TOWER_MAX_HEALTH = 150;
    public static final float SLOW_TOWER_ATTACK_SPEED = 0.75f;
    public static final boolean SLOW_TOWER_ROTATE = true;

    public static final int ZAP_TOWER_DAMAGE = 5;
    public static final int ZAP_TOWER_RANGE = 5;
    public static final int ZAP_TOWER_MAX_HEALTH = 125;
    public static final float ZAP_TOWER_ATTACK_SPEED = 0.1f;
    public static final boolean ZAP_TOWER_ROTATE = true;

    public static final int MULTI_TOWER_DAMAGE = 20;
    public static final int MULTI_TOWER_RANGE = 4;
    public static final int MULTI_TOWER_MAX_HEALTH = 200;
    public static final float MULTI_TOWER_ATTACK_SPEED = 0.5f;
    public static final boolean MULTI_TOWER_ROTATE = false;
    
    /**
     * Returns sum of all simple tower components.
     */
    public static float simpleTotal(){
        return SIMPLE_TOWER_DAMAGE + SIMPLE_TOWER_RANGE + SIMPLE_TOWER_MAX_HEALTH +
        SIMPLE_TOWER_ATTACK_SPEED;

    }

    /**
     * Returns sum of all splash tower components.
     */
    public static float splashTotal(){
        return SPLASH_TOWER_DAMAGE + SPLASH_TOWER_RANGE + SPLASH_TOWER_MAX_HEALTH +
                SPLASH_TOWER_ATTACK_SPEED;

    }

    /**
     * Returns sum of all sniper tower components.
     */
    public static float sniperTotal(){
        return SNIPER_TOWER_DAMAGE + SNIPER_TOWER_RANGE + SNIPER_TOWER_MAX_HEALTH +
                SNIPER_TOWER_ATTACK_SPEED;

    }

    /**
     * Returns sum of all slow tower components.
     */
    public static float slowTotal(){
        return SLOW_TOWER_DAMAGE + SLOW_TOWER_RANGE + SLOW_TOWER_MAX_HEALTH +
                SLOW_TOWER_ATTACK_SPEED;

    }

    /**
     * Returns sum of all zap tower components.
     */
    public static float zapTotal(){
        return ZAP_TOWER_DAMAGE + ZAP_TOWER_RANGE + ZAP_TOWER_MAX_HEALTH +
                ZAP_TOWER_ATTACK_SPEED;

    }

    /**
     * Returns sum of all multi tower components.
     */
    public static float multiTotal(){
        return MULTI_TOWER_DAMAGE + MULTI_TOWER_RANGE + MULTI_TOWER_MAX_HEALTH +
                MULTI_TOWER_ATTACK_SPEED;

    }
}
