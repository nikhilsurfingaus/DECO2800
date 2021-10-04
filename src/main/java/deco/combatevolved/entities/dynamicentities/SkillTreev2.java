package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.managers.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used for the initialisation of the skill nodes tree
 * The skills attributes would first being define in a private skill nodes indicating the skill name, skill type
 * value, skill descriptions, and the level requirements of the skills
 *
 * **/
public class SkillTreev2{

    private static final String ATTACK = "attack";
    private static final String HEALTH = "health";
    private static final String DEFENCE = "defence";
    private static final String TOWER = "tower";
    private static final String SPECIAL = "special";
    private static final String INCREASE_ATK_DESC = "Increases attack by 5%";
    private static final String INCREASES_HEALTH_BY_5 = "Increases health by 5%";
    private transient SkillNode damage1 = new SkillNode("Damage I", ATTACK, 0.05, INCREASE_ATK_DESC, 1);
    private transient SkillNode damage2 = new SkillNode("Damage II", ATTACK, 0.05, INCREASE_ATK_DESC, 2);
    private transient SkillNode damage3 = new SkillNode("Damage III", ATTACK, 0.05, INCREASE_ATK_DESC, 3);
    private transient SkillNode damage4 = new SkillNode("Damage IV", ATTACK, 0.1, "Increases attack by 10%", 4);
    private transient SkillNode mortalReminder = new SkillNode("Mortal Reminder", SPECIAL, 0.4, "Attacks ignore 50% of enemy armour", 1);
    private transient SkillNode doubleEdged = new SkillNode("Double Edged", SPECIAL, 0.1, "Deal 10% more damage but take 5% more damage", 1);
    private transient SkillNode fireTouch = new SkillNode("Fire Touch", SPECIAL, 0.2, "Deal extra 20% bullet damage to enemy max health", 3);
    private transient SkillNode explosion = new SkillNode("Explosion", SPECIAL, 0.25, "Explosion range are 25% larger", 2);
    private transient SkillNode flameTrail = new SkillNode("Flame Trail", SPECIAL, 20, "Explosions leave behind fire that deals 5 dps for 5 second", 3);
    private transient SkillNode moreExplosion = new SkillNode("More Explosion", SPECIAL, 0.25, "Explosions have a 25% chance to deal extra 10 dps for 5 second", 4);


    private transient SkillNode health1 = new SkillNode("Health I", HEALTH, 0.05, INCREASES_HEALTH_BY_5, 1);
    private transient SkillNode health2 = new SkillNode("Health II", HEALTH, 0.05, INCREASES_HEALTH_BY_5, 2);
    private transient SkillNode health3 = new SkillNode("Health III", HEALTH, 0.05, INCREASES_HEALTH_BY_5, 3);
    private transient SkillNode health4 = new SkillNode("Health IV", HEALTH, 0.1, "Increases health by 10%", 4);
    private transient SkillNode thickArmour = new SkillNode("Thick Armour", DEFENCE, 0.1, "Increases defence by 10%", 2);
    private transient SkillNode thornArmour = new SkillNode("Thick Armour", DEFENCE, 0, "Return 10% of resistance when attacked by enemy", 3);
    private transient SkillNode guardian = new SkillNode("Guardian", "defence", 50, "Standing near an ally will increase both player's resistance by 50", 4);
    private transient SkillNode advancedHealing = new SkillNode("Advanced Healing", null, 0.25, "Healing items are 25% more effective", 2);
    private transient SkillNode apothecary = new SkillNode("Apothecary", null, 0, "Healing yourself also heals an ally", 3);
    private transient SkillNode unkillable = new SkillNode("Unkillable", HEALTH, 0.01, "Gain 1% health per second", 4);

    private String towerUpgradeDesc = "Increases tower HP and attack by 5%";
    private transient SkillNode towerUpgrade1 = new SkillNode("Tower Upgrade I", TOWER, 0.05, towerUpgradeDesc, 1);
    private transient SkillNode towerUpgrade2 = new SkillNode("Tower Upgrade II", TOWER, 0.05, towerUpgradeDesc, 2);
    private transient SkillNode towerUpgrade3 = new SkillNode("Tower Upgrade III", TOWER, 0.05, towerUpgradeDesc, 3);
    private transient SkillNode towerUpgrade4 = new SkillNode("Tower Upgrade IV", TOWER, 0.1, "Increases tower HP and attack by 10%", 4);
    private transient SkillNode shootFaster = new SkillNode("Shoot Faster", TOWER, 0.25, "Towers attack 25% faster", 2);
    private transient SkillNode laserBullets = new SkillNode("Laser Bullets", SPECIAL, 0.05, "Allow bullet to shoot through enemies", 3);
    private transient SkillNode stickyBullets = new SkillNode("Sticky Bullets", TOWER, 0.05, "Towers attacks slow enemies by 20%", 4);
    private transient SkillNode efficiency = new SkillNode("Efficiency", null, 0.25, "Energy depletes 25% slower", 2);
    private transient SkillNode endlessItems = new SkillNode("Endless Items", null, 0.25, "When using a consumable, 25% chance to retain it", 3);
    private transient SkillNode fortune = new SkillNode("Fortune", null, 0.5, "50% chance to gather double the resources", 3);

    private ArrayList<SkillNode> skillsLearnt;
    private transient HashMap<String, SkillNode> skillsMap;

    public SkillTreev2() {
        skillsMap = new HashMap<>();
        skillsLearnt = new ArrayList<>();
        setUp();
    }

    /**
     * This method is used to setup the entire skill map required by putting in the skill nodes that have been
     * initialised earlier to allow easier iterating through the map to search for the node
    **/
    public void setUp() {
        skillsMap.put("attackDamage1", damage1);
        skillsMap.put("attackDamage2", damage2);
        skillsMap.put("attackDamage3", damage3);
        skillsMap.put("attackDamage4", damage4);
        skillsMap.put("attackMortalReminder", mortalReminder);
        skillsMap.put("attackDoubleEdged", doubleEdged);
        skillsMap.put("attackExplosion", explosion);
        skillsMap.put("attackMoreExplosion", moreExplosion);
        skillsMap.put("attackFireTouch", fireTouch);
        skillsMap.put("attackFlameTrail", flameTrail);
        skillsMap.put("defenceHealth1", health1);
        skillsMap.put("defenceHealth2", health2);
        skillsMap.put("defenceHealth3", health3);
        skillsMap.put("defenceHealth4", health4);
        skillsMap.put("defenceThickArmour", thickArmour);
        skillsMap.put("defenceThornArmour", thornArmour);
        skillsMap.put("defenceGuardian", guardian);
        skillsMap.put("defenceAdvancedHealing", advancedHealing);
        skillsMap.put("defenceApothecary", apothecary);
        skillsMap.put("defenceUnkillable", unkillable);
        skillsMap.put("utilityTowerUpgrade1", towerUpgrade1);
        skillsMap.put("utilityTowerUpgrade2", towerUpgrade2);
        skillsMap.put("utilityTowerUpgrade3", towerUpgrade3);
        skillsMap.put("utilityTowerUpgrade4", towerUpgrade4);
        skillsMap.put("utilityShootFaster", shootFaster);
        skillsMap.put("utilityLaserBullets", laserBullets);
        skillsMap.put("utilityStickyBullets", stickyBullets);
        skillsMap.put("utilityEfficiency", efficiency);
        skillsMap.put("utilityEndlessItems", endlessItems);
        skillsMap.put("utilityFortune", fortune);

        // Sets up the children and parent nodes
        damage1.addChild(damage2);
        damage2.addChild(damage3);
        damage3.addChild(damage4);
        damage1.addChild(mortalReminder);
        mortalReminder.addChild(doubleEdged);
        doubleEdged.addChild(damage4);
        damage1.addChild(explosion);
        explosion.addChild(fireTouch);
        explosion.addChild(flameTrail);
        fireTouch.addChild(moreExplosion);
        flameTrail.addChild(moreExplosion);
        moreExplosion.addChild(damage4);

        health1.addChild(health2);
        health2.addChild(health3);
        health3.addChild(health4);
        health1.addChild(advancedHealing);
        advancedHealing.addChild(apothecary);
        apothecary.addChild(unkillable);
        unkillable.addChild(health4);
        health1.addChild(thickArmour);
        thickArmour.addChild(thornArmour);
        thornArmour.addChild(guardian);
        guardian.addChild(health4);

        towerUpgrade1.addChild(towerUpgrade2);
        towerUpgrade2.addChild(towerUpgrade3);
        towerUpgrade3.addChild(towerUpgrade4);
        towerUpgrade1.addChild(shootFaster);
        shootFaster.addChild(laserBullets);
        laserBullets.addChild(stickyBullets);
        stickyBullets.addChild(towerUpgrade4);
        towerUpgrade1.addChild(efficiency);
        efficiency.addChild(endlessItems);
        endlessItems.addChild(towerUpgrade4);
        efficiency.addChild(fortune);
        fortune.addChild(towerUpgrade4);
    }

    /**
     * Indicating if the skill has been learnt
     * @param skill parsing in the skills to be search for and indicate if the skills has been learnt
     * @return if the skill exist in the skillslearnt list
     * */
    public boolean hasLearnt(SkillNode skill) {
        return skillsLearnt.contains(skill);
    }

    /**
     * iterate through the skill type to add the skill skills to the skillslearnt list and to add the attributes
     * accordingly if required
     * @param skill indicating the skill which the user wish to learn
     *
    **/
    public void learnSkill(SkillNode skill) {
        PlayerAttributes player = (PlayerAttributes) GameManager.get().getWorld().getEntityById(
                                    GameManager.get().getPlayerEntityID());
        if (player == null) { // If above doesn't work (usually does) run below
            for (AbstractEntity entity : GameManager.get().getWorld().getEntities()) {
                if (entity instanceof PlayerPeon) {
                    player = (PlayerAttributes) entity;
                }
            }
        }
        // Makes sure player isn't null, has a skill point and checks if skill to be learnt is a base skill
        // or if all required skills have been learnt
        if (player != null && player.getSkillPoint() > 0 &&
                (skill.isRootNode() || skillsLearnt.containsAll(skill.getParents()))) {
            skillsLearnt.add(skill);
            player.removeSkillPoint();
            // Increases player attributes
            if (skill.getType() != null && !skill.getType().equals("tower") && !skill.getType().equals("special")) {
                int statToAdd = (int) Math.round(player.getStats(skill.getType()) * (skill.getValue()));
                player.statsChange(statToAdd,skill.getType());
            } else if ((skill.getType() != null && skill.getType().equals(SPECIAL))
                    && (skill.equals(skillsMap.get("attackDoubleEdged")))) {
                int statToChange1 = (int) Math.round(player.getStats(ATTACK) * (skill.getValue()));
                int statToChange2 = (int) Math.round(player.getStats(DEFENCE) * 0.05);
                player.statsChange(statToChange1, ATTACK);
                player.statsChange(statToChange2, DEFENCE);
            }
        }
    }

    /**
     * Getting the skill node from the skillsMap
     * @param skillName skillname to be gotten from the skill map
     * @return SkillNode of the particular skill
     * */
    public SkillNode getSkill(String skillName) {
        return skillsMap.get(skillName);
    }

    /**
     * Gets a list of learnt skills
     * @return list of skills
     */
    public List<SkillNode> getLearntSkills() {
        return this.skillsLearnt;
    }
}
