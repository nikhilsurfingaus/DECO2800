package deco.combatevolved.entities.dynamicentities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deco.combatevolved.entities.items.Equipment;
import deco.combatevolved.entities.items.EquipmentSlots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlayerAttributes extends PlayerPeon {
    private static final Logger logger = LoggerFactory.getLogger(PlayerAttributes.class);

    private static final String SOLDIER = "Soldier";
    private static final String ENGINEER = "Engineer";
    private static final String EXPLORER = "Explorer";
    private static final String BULLET = "bullet";
    private static final String PROJECTILE = "projectile";
    private static final String GUNLOW = "gun_low";
    private static final String GRENADELOW = "grenade_low";
    private static final String GUN = "gun";
    private static final String GRENADE = "grenade";
    private static final String HEALTH = "health";
    private static final String ATTACK = "attack";
    private static final String DEFENCE = "defence";
    private static final String SPEED = "speed";

    //the maximum defence number the player can get
    //used to calculate damage that player get
    //the actual damage that player get equals [damage*(player defence/player MAXDEFENCE)]
    private int MAXDEFENCE = 300;

    private String playerClassType;
    private int playerAttack = 0;
    private int playerDefence = 0;
    private int playerSpeed = 0;
    private int playerHealth = 0;
    private transient EquipmentSlots equipmentSlots;
    private int experience;
    private int level = 0;
    private int skillPoint = 0;
    private String playerTexture;
    private String attackPattern;
    private String defaultAttackPattern;

    /**
     * enumclass for classtype
     * */
    public enum ClassType {
        SOLDIER,
        ENGINEER,
        EXPLORER
    }

    /**
     * enumclass for attributes
     * */
    public enum Attributes {
        ATTACK,
        DEFENCE,
        SPEED,
        HEALTH
    }
    /** Initialisation of player classtype class used in character selection screens
     * @param row
     * @param column
     * @param speed
     * @param textureAtlas path to texture atlas
     * @param strClassType to initialised
     * change string to upper class to match enum
     * **/
    public PlayerAttributes(float row, float column, float speed, String textureAtlas, String strClassType) {
        super(row, column, speed, textureAtlas);
        logger.info("Player created at ({}, {})", row, column);
        ClassType classType = ClassType.valueOf(strClassType.toUpperCase());
        equipmentSlots = new EquipmentSlots(this);
        this.playerClassType = strClassType;

        if (classType.equals(ClassType.SOLDIER)) {
            this.playerSpeed = 10;
            this.playerHealth = 150;
            this.playerAttack = 50;
            this.playerDefence = 30;
            defaultAttackPattern = BULLET;
            attackPattern = BULLET;
            playerTexture = SOLDIER;
            try {
                Equipment defaultGun = new Equipment(GUNLOW, 1, GUN, "401", GUNLOW);
                equipmentSlots.uncheck_equip(this.equipmentSlots, defaultGun);
            } catch (Exception exception) {
                logger.info("CAUGHT AN EXECEPTION (could be anything): " + exception);
            }
        } else if (classType.equals(ClassType.ENGINEER)) {
            this.playerSpeed = 20;
            this.playerHealth = 75;
            this.playerAttack = 40;
            this.playerDefence = 50;
            defaultAttackPattern = PROJECTILE;
            attackPattern = PROJECTILE;
            playerTexture = ENGINEER;
            try {
                Equipment defaultGrenade = new Equipment(GRENADELOW, 1, GRENADE, "404", GRENADELOW);
                equipmentSlots.uncheck_equip(this.equipmentSlots, defaultGrenade);
            } catch (Exception exception) {
                logger.info("CAUGHT AN EXECEPTION (could be anything): " + exception);
            }
        } else if (classType.equals(ClassType.EXPLORER)) {
            this.playerSpeed = 50;
            this.playerHealth = 100;
            this.playerAttack = 40;
            this.playerDefence = 15;
            defaultAttackPattern = BULLET;
            attackPattern = BULLET;
            playerTexture = EXPLORER;
            try {
                Equipment defaultGun = new Equipment(GUNLOW, 1, GUN, "401", GUNLOW);
                equipmentSlots.uncheck_equip(this.equipmentSlots, defaultGun);
            } catch (Exception exception) {
                logger.info("CAUGHT AN EXECEPTION (could be anything): " + exception);
            }
        }
        // Leveling system including experience
        this.skillPoint = 1;
        this.level = 1;
        this.experience = 0;
    }

    /**
     * get the equipmentSlots of the player
     *
     * @return the equipmentSlots of the player
     */
    public EquipmentSlots getEquipmentSlots() {
        return this.equipmentSlots;
    }

    /**
     * get the Texture of the player
     *
     * @return the Texture of the player
     */
    public String getPlayerTexture() {
        return this.playerTexture;
    }

    /**
     * set the AttackPattern of the player
     */
    public void setAttackPattern(String attackPattern) {
        this.attackPattern = attackPattern;
    }

    /**
     * get the AttackPattern of the player
     *
     * @return the AttackPattern of the player
     */
    public String getAttackPattern() {
        return this.attackPattern;
    }

    /**
     * get the max defence of the player
     *
     * @return the max defence of the player
     */
    public int getMaxDefence() {
        return this.MAXDEFENCE;
    }

    /**
     * get the actual damage rate of the player
     * calculated by 1-(player defence/player max defence)
     *
     * @return the actual damage rate  of the player
     */
    public float getActualDamageRate() {
        if((float) this.playerDefence/(float) this.MAXDEFENCE > 1){
            return 0;
        } else {
            return 1 - (float) this.playerDefence/(float) this.MAXDEFENCE;
        }
    }

    /**
     * get the default AttackPattern of the player
     *
     * @return the default AttackPattern of the player
     */
    public String getDefaultAttackPattern() {
        return this.defaultAttackPattern;
    }

    /**for other team to implement the stats change. Initialisation of classType would be required from the character
     selection screen first.
     @param value the integer value to change the property
     @param strProperty the property to add on the value to
     change string to upper class to match enum
     **/


    public void statsChange(int value, String strProperty) {
        Attributes property = Attributes.valueOf(strProperty.toUpperCase());
        if (property.equals(Attributes.ATTACK)) {
            this.playerAttack += value;
        } else if (property.equals(Attributes.DEFENCE)) {
            if((this.playerDefence + value) <= this.MAXDEFENCE){
                this.playerDefence += value;
            }
        } else if (property.equals(Attributes.SPEED)) {
            this.playerSpeed += value;
        } else if (property.equals(Attributes.HEALTH)) {
            this.playerHealth += value;
        }
    }


    /**for other team to implement the stats change. Initialisation of classType would be required from the character
     selection screen first.
     @param strProperty that they wish to retrieved
     change string to upper class to match enum
     **/

    public int getStats(String strProperty) {
        Attributes property = Attributes.valueOf(strProperty.toUpperCase());
        if (property.equals(Attributes.ATTACK)) {
            return this.playerAttack;
        } else if (property.equals(Attributes.DEFENCE)) {
            return this.playerDefence;
        } else if (property.equals(Attributes.SPEED)) {
            return this.playerSpeed;
        } else if (property.equals(Attributes.HEALTH)) {
            return this.playerHealth;
        }
        return 0;
    }

    /**
     * Method to get the player class type
     * **/
    public String getPlayerClassType() {
        return playerClassType;
    }

    /**
     * Returns the player's current experience points
     *
     * @return player's exp
     */
    public int getExp() {
        return this.experience;
    }

    /**
     * Returns the player's current level
     *
     * @return level
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Checks whether a player has enough experience points to level up
     *
     * @return  true if possible to level up
     *          false if not possible
     */
    private boolean canLevelUp() {
        return this.experience >= 80 + (20 * this.level);
    }

    /**
     * Increases level by 1 if canLevelUp() returns true. Also increases the player's
     * attributes after leveling up.
     * Increase skillpoint by 1 and the user could choose to increase their skills level by using the skillpoint
     */
    public void increaseLevel() {
        if (canLevelUp()) {
            this.level++; // increases level by 1
            this.experience = 0; // resets experience
            int defaultValue = 5; // the default value for increasing the attributes
            if (this.level < 30) {
                addSkillPoint();
            }
            String classType = getPlayerClassType();
            gainFullHealth();
            // based on the class, attributes increase at different rates
            switch (classType) {
                case SOLDIER:
                    statsChange((int)(defaultValue * 1.5), HEALTH);
                    statsChange((int)(defaultValue * 1.5), ATTACK);
                    statsChange((int)(defaultValue * 1.0), DEFENCE);
                    statsChange((int)(defaultValue * 0.5), SPEED);
                    break;
                case ENGINEER:
                    statsChange((int)(defaultValue * 0.5), HEALTH);
                    statsChange((int)(defaultValue * 1.0), ATTACK);
                    statsChange((int)(defaultValue * 1.5), DEFENCE);
                    statsChange((int)(defaultValue * 1.0), SPEED);
                    break;
                case EXPLORER:
                    statsChange((int)(defaultValue * 1.0), HEALTH);
                    statsChange((int)(defaultValue * 0.5), ATTACK);
                    statsChange((int)(defaultValue * 1.0), DEFENCE);
                    statsChange((int)(defaultValue * 1.5), SPEED);
                    break;
                default:
                    break;
            }

            // Applies the basic increase stat skills after leveling up
            SkillTreev2 playerSkills = getPlayerSkills();
            List learntSkills = playerSkills.getLearntSkills();
            // Checks if the player has learnt the basic skills
            for (Object learntSkill : learntSkills) {
                SkillNode skill = (SkillNode) learntSkill;
                if (skill == (playerSkills.getSkill("attackDamage1")) ||
                        skill == (playerSkills.getSkill("attackDamage2")) ||
                        skill == (playerSkills.getSkill("attackDamage3")) ||
                        skill == (playerSkills.getSkill("attackDamage4")) ||
                        skill == (playerSkills.getSkill("defenceHealth1")) ||
                        skill == (playerSkills.getSkill("defenceHealth2")) ||
                        skill == (playerSkills.getSkill("defenceHealth3")) ||
                        skill == (playerSkills.getSkill("defenceHealth4"))) {
                    statsChange((int) (getStats(skill.getType()) * skill.getValue()), skill.getType());
                }
            }
        }
    }

    /**
     * Adding the skill point value upon decreasing a certain skill
     */
    public void addSkillPoint() {
        this.skillPoint++;
    }

    /**
     * Removes a skill point. Called whenever a skill is unlocked
     */
    public void removeSkillPoint() {
        if (this.skillPoint > 0) {
            this.skillPoint--;
        }
    }

    /**
     * return the current skill point that the player acquired.
     * */
    public int getSkillPoint() {
        return this.skillPoint;
    }

    public void setSkillPoint(int point) {
        this.skillPoint = point;
    }

    /**
     * Increases experience points based on the amount given. Also checks if
     * the player can level up after adding the experience points.
     *
     * @param amount the number of experience points to be added
     */
    public void increaseExp(int amount) {
        this.experience += amount;
        if (canLevelUp()) { // checks if player can level up after gaining the experience.
            increaseLevel();
        }
    }

    @Override
    public void loseHealth(int lossHealth) {
        lossHealth = Math.round(lossHealth * this.getActualDamageRate());
        super.loseHealth(lossHealth);
    }
}