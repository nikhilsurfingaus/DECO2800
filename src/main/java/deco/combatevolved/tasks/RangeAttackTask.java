package deco.combatevolved.tasks;

import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.SkillTreev2;
import deco.combatevolved.entities.enemyentities.RangeEnemy;
import deco.combatevolved.entities.dynamicentities.Bullet;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.managers.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines the action for a range enemy attacking a object(a player or the tower). 
 * The object's health will reduced by 5 (TBD) and the task terminates within one tick.
 */
public class RangeAttackTask extends AbstractAttackTask{
	
	public RangeAttackTask(RangeEnemy entity, HasHealth target, HexVector targetPosition) {
		super(entity, target, targetPosition);
	}
	
	/**
	 * Fire a bullet in the direction of the target if it is in the entities
	 * range
	 * The bullet will damage a tower if it hits it
     */
	@Override
	protected void performAttack() {
		if (entity.getPosition().distance(targetPosition) <= entity.getRange()) {
			entity.play_attack_sound();
			getLogger().info(String.format("%s range attacked %s", entity, target));
			HexVector position = entity.getPosition();
			HexVector direction = targetPosition.subtract(position);
			direction = direction.normalise().multiply(0.1f);
			// fire bullet in the direction of the target
			Bullet bullet = new Bullet(position, direction, entity.getRange(),
					entity.getDamage());
			GameManager.get().getWorld().addEntity(bullet);

			// For implementing a skill
			if (target instanceof PlayerAttributes) {
				int attack = entity.getDamage();
				int damageAmount = bullet.getDamageDone();
				PlayerAttributes player = (PlayerAttributes) target;
				SkillTreev2 playerSkills = player.getPlayerSkills();
				// If player has learnt thorn armour, enemy takes damage that has been mitigated
				if (playerSkills.hasLearnt(playerSkills.getSkill("defenceThornArmour"))) {
					int damageMitigated = attack - damageAmount;
					if (damageMitigated > 0) {
						try {
							entity.loseHealth(damageMitigated);
						} catch (EnemyValueException e) {
							getLogger().info("Found negative value in loseHealth");
						}
					}
				}
			}

	        taskComplete = true;
	        taskAlive = false;
		}
	}
	
	public static Logger getLogger() {
        return LoggerFactory.getLogger(RangeAttackTask.class);
    }
}