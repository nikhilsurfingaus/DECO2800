package deco.combatevolved.tasks;

import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.SkillTreev2;
import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.util.HexVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the action for a melee enemy attacking a object(a player or the tower). 
 * The object's health will reduced by 10 (TBD) and the task terminates within one tick.
 */
public class MeleeAttackTask extends AbstractAttackTask{
	
	public MeleeAttackTask(BasicEnemy entity, HasHealth target, HexVector targetPosition) {
		super(entity, target, targetPosition);
	}
	
	/**
     * Reduce the target's health by enemy's damage unless health is already 0 when the object 
     * is close to the enemy.
     */
	@Override
	protected void performAttack() {
		if (entity.getPosition().distance(targetPosition) <= entity.getRange()) {
			entity.play_attack_sound();
			getLogger().info(String.format("%s melee attacked %s", entity, target));
			try {
				if (target instanceof PlayerAttributes) { // Now utilising player's defence when being attacked
					int attack = entity.getDamage();
					PlayerAttributes player = (PlayerAttributes) target;
					int armour = player.getStats("defence");
					int damageAmount = (int) (attack * ((float) 100 / (100 + armour)));
					target.loseHealth(damageAmount);
					SkillTreev2 playerSkills = player.getPlayerSkills();
					// If player has learnt thorn armour, enemy takes damage that has been mitigated
					if (playerSkills.hasLearnt(playerSkills.getSkill("defenceThornArmour"))) {
						int damageMitigated = attack - damageAmount;
						entity.loseHealth(damageMitigated);
					}
				} else {
					target.loseHealth(entity.getDamage());
				}
			} catch (EnemyValueException e) {
				getLogger().warn("Enemy {} does negative damage", entity);
			}
	        taskComplete = true;
	        taskAlive = false;
		}
	}
	
	public static Logger getLogger() {
        return LoggerFactory.getLogger(MeleeAttackTask.class);
    }
}
