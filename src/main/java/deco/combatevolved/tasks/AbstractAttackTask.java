package deco.combatevolved.tasks;

import deco.combatevolved.entities.enemyentities.BasicEnemy;
import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.util.HexVector;

public abstract class AbstractAttackTask extends AbstractTask {
	protected HasHealth target;
	protected HexVector targetPosition;
	protected BasicEnemy entity;
	protected boolean taskAlive = true;
	protected boolean taskComplete = false;
	
	public AbstractAttackTask(BasicEnemy entity, HasHealth target, HexVector targetPosition) {
		super(entity);
		this.entity = entity;
		this.target = target;
		this.targetPosition = targetPosition;
	}
	
	/** 
	 * Simple attack logic.
	 * @param tick Current game tick
	 */
	public void onTick(long tick) {
		performAttack();
	}

	@Override
	public boolean isComplete() {
		return taskComplete;
	}

	@Override
	public boolean isAlive() {
		return taskAlive;
	}
	
	/**
	 * Define custom attack behaviours by overriding functions below.
	 */
	protected abstract void performAttack();

	/**
	 * Resets an old attack task so that it can be reused
	 * @param entity the new entity the task is for
	 * @param target the new target the entity should attack
	 */
	public void reset(BasicEnemy entity, HasHealth target, HexVector targetPosition) {
		this.entity = entity;
		this.target = target;
		this.targetPosition = targetPosition;
		taskAlive = true;
		taskComplete = false;
	}
	public HasHealth getTarget() {
		return target;
	}

}
	
