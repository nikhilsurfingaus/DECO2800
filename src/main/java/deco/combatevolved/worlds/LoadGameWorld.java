package deco.combatevolved.worlds;

import deco.combatevolved.entities.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class LoadGameWorld extends AbstractWorld {
	private final Logger logger = LoggerFactory.getLogger(LoadGameWorld.class);


	/**
	 * LoadGameWorld constructor
	 */
	public LoadGameWorld() {
		super();
	}

	/**
	 * Main method to generate the game world
	 */
	@Override
	protected void generateWorld() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTick(long i) {
		super.onTick(i);
		for (AbstractEntity e : this.getEntities()) {
			e.onTick(0);
		}
	}

}
