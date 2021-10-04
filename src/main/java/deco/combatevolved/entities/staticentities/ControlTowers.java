package deco.combatevolved.entities.staticentities;


import deco.combatevolved.entities.HasHealth;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.ControlTowerManager;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.Tile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Similar to the other static elements, the control towers are entities which the player must protect.
 * As such, it will have health (like rocks). However, it is a separate class since it will have other
 * methods to it (e.g. when its health reaches 0, you lose). This is just a simple skeleton used for
 * spawning, real tower class will replace this.
 * @author Chris
 *
 */
public class ControlTowers extends StaticEntity implements HasHealth{
	private static final Logger LOGGER = LoggerFactory.getLogger(ControlTowers.class);

	private int health = 500;

	private int conquestStarted = 0;

	private int conquestCompletion = 0;

	boolean towerConquered = false;

	private long lastConquestChange = 0;

	private int singleConquestTime;
	private int singleDeconquestTime;

	private String texture;
	private HexVector coord;

	public ControlTowers() {
		this.singleConquestTime = 350;
		this.singleDeconquestTime = 35;
	}


	public ControlTowers(Tile t, boolean obstruct, String textureName) {
		super(t, RenderConstants.TOWER_RENDER, textureName, obstruct);
		this.texture = textureName;
		this.coord = t.getCoordinates();
		this.singleConquestTime = 350;
		this.singleDeconquestTime = 35;
	}

	public ControlTowers(Tile t, boolean obstruct, String textureName, int conquestRate, int deconquestRate) {
		super(t, RenderConstants.TOWER_RENDER, textureName, obstruct);
		this.texture = textureName;
		this.singleConquestTime = conquestRate;
		this.singleDeconquestTime = deconquestRate;
	}
	
    @Override
    public void onTick(long i) {
		PlayerPeon player = (PlayerPeon)(GameManager.get().getWorld().
				getEntityById(GameManager.get().getPlayerEntityID()));
		//checks if the tower has already been conquered or another tower is being conquered (can't conquer two towers at once).
		if (!towerConquered && ((conquestStarted == 1 ) ||
				GameManager.get().getManager(ControlTowerManager.class).getMaxConquestCompletion() == 0)) {
			if (this.distance(player) < 3.5) { //checks if the player is close to the tower
				conquestStarted = 1;
				if (System.currentTimeMillis() - lastConquestChange >= this.singleConquestTime) { //controls rate of conquest
					conquestCompletion += 1;
					lastConquestChange = System.currentTimeMillis();
					if (conquestCompletion == 100) {
						LOGGER.info("tower has been conquered");
						towerConquered = true;
						this.texture = this.texture.concat("Con");
						Map<HexVector, String> child = new HashMap<>();
						child.put(this.coord, this.texture);
						this.setChildren(child);
						this.conquestCompletion = -1;
					}
				}
			} else if (conquestStarted == 1) {
				if (System.currentTimeMillis() - lastConquestChange >= this.singleDeconquestTime) { //controls rate of de-conquest
					conquestCompletion -= 1;
					lastConquestChange = System.currentTimeMillis();
				}
				if (conquestCompletion <= 0) {
					conquestStarted = 0;
					conquestCompletion = 0;
				}
			}
		}

    }
	
	@Override
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public void loseHealth(int lossHealth) {

	}

	@Override
	public void gainHealth(int gainHealth) {

	}

	@Override
	public void gainFullHealth() {

	}

	@Override
	public void death() {

	}

	public int getConquestCompletion() {
		return this.conquestCompletion;
	}

}
