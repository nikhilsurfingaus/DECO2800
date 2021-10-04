package deco.combatevolved.entities.dynamicentities;

import java.util.ArrayList;
import java.util.List;

public class SkillNode {

    private String skillName;
    private String type;
    private double value;
    private String description;
    private transient List<SkillNode> parents;
    private transient ArrayList<SkillNode> children;
    private int levelRequirement;

    public SkillNode(String skillName, String type, double value, String skillDesc, int levelRequirement) {
        this.skillName = skillName;
        this.type = type;
        this.value = value;
        this.description = skillDesc;
        this.levelRequirement = levelRequirement;
        children = new ArrayList<>();
        this.parents = null;
    }

    public boolean isRootNode() {
        return this.parents == null;
    }

    public void addParent(SkillNode skill) {
        if (this.parents == null) {
            this.parents = new ArrayList<>();
        }
        this.parents.add(skill);
    }

    public void addChild(SkillNode skill) {
        this.children.add(skill);
        skill.addParent(this);
    }

    public List<SkillNode> getParents() {
        return this.parents;
    }

    public List<SkillNode> getChildren() {
        return this.children;
    }

    // Getter methods below
    public String getName() {
        return this.skillName;
    }

    public String getType() {
        return this.type;
    }

    public double getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

    public int getLevelRequirement(){
        return this.levelRequirement;
    }

/*    for (AbstractEntity entity : entities) { // iterates through list of entities
        if (entity instanceof Tower) { // checks if instance is a tower
            int origHP = ((Tower) entity).getHealth();
            int origDmg = ((Tower) entity).getDamage();
            ((Tower) entity).setDamage((int) Math.round(origDmg * (1 + value)));    // Sets new damage and health
            ((Tower) entity).setHealth((int) Math.round(origHP * (1 + value)));     // values for the towers
        }
    }*/
}
