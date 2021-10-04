package deco.combatevolved.tasks;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TaskPool;
import deco.combatevolved.Tickable;

public abstract class AbstractTask implements Tickable {
	
	protected DynamicEntity entity;
	
	
	public AbstractTask(DynamicEntity entity) {
		this.entity = entity;
	}
	
	public abstract boolean isComplete();

	public abstract boolean isAlive();

	/**
	 * Returns this task to the task pool so that is can be reused
	 */
	public void returnToTaskPool() {
		GameManager.getManagerFromInstance(TaskPool.class).returnToTaskPool(this);
	}

}
