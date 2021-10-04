package deco.combatevolved.managers;

import deco.combatevolved.Tickable;

public abstract class TickableManager extends AbstractManager implements Tickable{

	 public abstract void onTick(long i);
}
