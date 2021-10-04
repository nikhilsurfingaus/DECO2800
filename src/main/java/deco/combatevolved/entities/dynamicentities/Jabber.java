package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.Tickable;
import deco.combatevolved.entities.RenderConstants;

public class Jabber extends DynamicEntity implements Tickable {

	public Jabber() {
		super();
		this.setTexture("jabber");
		this.setZ(1);
		this.speed = 0.05f;
	}

	/**
	 * Peon constructor
     */
	public Jabber(float row, float col, float speed) {
		super(row, col, RenderConstants.JABBER_RENDER, speed);
		this.setTexture("jabber");
	}

	@Override
	public void onTick(long i) {
		// Inherited method
	}
}
