package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.entities.RenderConstants;

public class Ferret extends VehicleEntity  {
	PlayerPeon rider;
	public Ferret() {
		this(0,0,0.145f);
	}

	/**
	 * Peon constructor
     */
	public Ferret(float row, float col, float speed) {
		super(row, col, RenderConstants.FERRET_RENDER, speed);
       
		this.setTexture("ferret");
	}
}
