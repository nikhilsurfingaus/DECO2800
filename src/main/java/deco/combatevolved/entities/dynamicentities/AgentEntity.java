package deco.combatevolved.entities.dynamicentities;

import com.google.gson.annotations.Expose;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TaskPool;
import deco.combatevolved.Tickable;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.tasks.AbstractTask;
import deco.combatevolved.util.HexVector;

/**
 * An AgentEntity is an entity that interacts with the world and has agency.
 * ("...Agency is the capacity of individuals to act independently and to make
 * their own free choices." - https://en.wikipedia.org/wiki/Agency_(sociology))
 */
public class AgentEntity extends DynamicEntity implements Tickable {
	private transient AbstractTask task;

	@Expose
	boolean inVehicle = false;
	HexVector velocity;
	protected float currentSpeed;

	VehicleEntity vehicle;
	
	public AgentEntity() {
		super();
		this.setTexture("spacman_ded");
		this.setZ(1);
		this.currentSpeed = this.speed;
	}

	/**
	 * Peon constructor
     */
	public AgentEntity(float row, float col, float speed) {
		super(row, col, RenderConstants.PEON_RENDER, speed);
		this.currentSpeed = this.speed;
		this.setTexture("spacman_ded");
		if (this instanceof Projectile) {
			this.task = null;
		}
	}

	/**
	 * Getter method for velocity.
	 *
	 * @return velocity
	 */
	public HexVector getVelocity() {
		return velocity;
	}

	/**
	 * Setter method for velocity.
	 *
	 * @param newVelocity The new velocity to be set.
	 */
	public void setVelocity(HexVector newVelocity) {
		velocity = newVelocity;
	}

	@Override
	public void onTick(long i) {
		if(task != null && task.isAlive()) {
			if(task.isComplete()) {
				this.task = GameManager.getManagerFromInstance(TaskPool.class).getTask(this);
			}
			task.onTick(i);
		}
//		else {
//			this.task = GameManager.getManagerFromInstance(TaskPool.class).getTask(this);
//		}
	}
	
	public AbstractTask getTask() {
		return task;
	}

	public void setTask(AbstractTask task) {
		this.task = task;
	}

	public void eject() {
		this.vehicle.driver.currentSpeed = this.vehicle.driver.speed;
		this.vehicle.driver = null;
		this.inVehicle = false;
		this.vehicle.driverID = -1;
		this.vehicle = null;
	}

	public void setInVehicle(Boolean b) {
		this.inVehicle = b;
	}

	public void setVehicle(VehicleEntity vehicle) {
		this.vehicle = vehicle;
		inVehicle = true;
	}

	/**
	 * Gets the player's vehicle.
	 * @return The player's vehicle
	 */
	public VehicleEntity getVehicle() {
		return vehicle;
	}

	/**
	 * Gets if the player is in a vehicle or not.
	 * @return True if the player is in a vehicle, false otherwise
	 */
	public boolean isInVehicle() {
		return inVehicle;
	}

	public float getCurrentSpeed() {
		return this.currentSpeed;
	}

	public void setCurrentSpeed(float newSpeed) {
		this.currentSpeed = newSpeed;
	}
}
