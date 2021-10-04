package deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems;

import deco.combatevolved.entities.dynamicentities.SkillTreev2;
import deco.combatevolved.entities.items.consumableitems.Consumable;
import deco.combatevolved.entities.items.consumableitems.PassiveItem;

public class Medkit extends PassiveItem implements Consumable {

    public Medkit(String name, int rarity, String id, String texture, String description) {
        super(name, rarity, id, texture, description);
    }

    /**
     * Sets the player's health back to the maximum
     */
    public void consume() {
        this.updateStats();
    }

    private void updateStats() {
        // If player has unlocked the advanced healing skill, then healing is 25% more effective
        SkillTreev2 playerSkills = this.getPlayer().getPlayerSkills();
        if (playerSkills.hasLearnt(playerSkills.getSkill("defenceAdvancedHealing"))) {
            int healAmount = (int) Math.round(this.getPlayer().getMaxPlayerHealth() * 0.5);
            double healModifier = playerSkills.getSkill("defenceAdvancedHealing").getValue();
            this.getPlayer().gainHealth((int) Math.round(healAmount * (1 + healModifier)));
        } else {
            // heals players for 50% of their max health
            this.getPlayer().gainHealth((int) Math.round(this.getPlayer().getMaxPlayerHealth() * 0.5));
        }

    }
}
