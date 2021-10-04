package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.GameScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillTree extends PlayerAttributes{

    private static final String ATTACK_STR = "attack";
    private static final String DEFENCE_STR = "defence";
    private static final String SPEED_STR = "speed";
    private static final String HEALTH_STR = "health";

    private final Logger logger = LoggerFactory.getLogger(GameScreen.class);
    private int attack = 0;
    private int defence = 0;
    private int speed = 0;
    private int health = 0;
    private int skillPoint = 0;

    /**
     * enumclass for skills area
     * */
    public enum SkillsArea{
        OFFENSIVE,
        DEFENCE,
        SPEED
    }


    /**
     * Constructor for the initialisation of skilltree
     * extends from attributes as each skill is adding to the attributes itself
     *
     * @param row
     * @param column
     * @param speed
     * @param strClassType to initialised
     *                     change string to upper class to match enum
     **/

    public SkillTree(float row, float column, float speed, String textureAtlas, String strClassType) {
        super(row, column, speed, textureAtlas, strClassType);
    }
    /**
     * Retrieve the current skill point of the player and set it in the skill tree branch
     * @param skillValue the skill value retrieve from the player
     * */
    public void setSkillPoint(int skillValue){
        skillPoint = skillValue;
    }

    /**
     * Return the current skill point value as int
     * **/
    public int getSkillPointValue(){
        return this.skillPoint;
    }

    /**
     * in this method it will get the percentage value that the user wish to change and set the end status change
     * based on the value change by having different area of skills
     * get the available skillpoint by getSkillPoint()
     * the value is being checked by the enum that is being predefined
     * @param percentageValue percentage value that the user wish to put
     * @param strProperty the property string that the skill is to added
     * */
    public void addSkills(int percentageValue, String strProperty){
        SkillsArea targetedSkill = SkillsArea.valueOf(strProperty.toUpperCase());
        if (getSkillPointValue() > 0){
            if(targetedSkill.equals(SkillsArea.DEFENCE)){
                this.defence = getStats(DEFENCE_STR) * percentageValue/100;
                this.health = getStats(HEALTH_STR) * percentageValue/200;
            } else if (targetedSkill.equals(SkillsArea.OFFENSIVE)){
                this.attack = getStats(ATTACK_STR) * percentageValue/100;
                this.defence = getStats(DEFENCE_STR) * percentageValue/200;
            }else if (targetedSkill.equals(SkillsArea.SPEED)){
                this.speed = getStats(SPEED_STR) * percentageValue/100;
                this.attack = getStats(ATTACK_STR) * percentageValue/200;
            }
        } else {
            logger.info("not enough skill point available");
        }
        statsChange(attack, ATTACK_STR);
        statsChange(speed, SPEED_STR);
        statsChange(health, HEALTH_STR);
        statsChange(defence, DEFENCE_STR);
    }

    /**
     * in this method it will get the percentage value that the user wish to change and set the end status change
     * based on the value change by having different area of skills
     * the value is being checked by the enum that is being predefined
     * addSkillPoint() to add back the value to the skill point value
     * @param percentageValue percentage value that the user wish to put
     * @param strProperty the property string that the skill is to be removed
     * */
    public void removeSkills(int percentageValue, String strProperty){
        SkillsArea targetedSkill = SkillsArea.valueOf(strProperty.toUpperCase());
        if(targetedSkill.equals(SkillsArea.DEFENCE)){
            this.defence = getStats(DEFENCE_STR) * percentageValue/100;
            this.health = getStats(HEALTH_STR) * percentageValue/200;
        } else if (targetedSkill.equals(SkillsArea.OFFENSIVE)){
            this.attack = getStats(ATTACK_STR) * percentageValue/100;
            this.defence = getStats(DEFENCE_STR) * percentageValue/200;
        } else if (targetedSkill.equals(SkillsArea.SPEED)){
            this.speed = getStats(SPEED_STR) * percentageValue/100;
            this.attack = getStats(ATTACK_STR) * percentageValue/200;
        }

        int existingSkillPoint = getSkillPointValue();
        setSkillPoint(existingSkillPoint + 1);
        attack = this.attack * -1;
        speed = this.speed * -1;
        health = this.health * -1;
        defence = this.defence * -1;
        statsChange(attack, ATTACK_STR);
        statsChange(speed, SPEED_STR);
        statsChange(health, HEALTH_STR);
        statsChange(defence, DEFENCE_STR);
    }
}
