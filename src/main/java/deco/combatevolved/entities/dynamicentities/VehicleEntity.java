package deco.combatevolved.entities.dynamicentities;

import com.google.gson.annotations.Expose;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.entities.items.Inventory;
import deco.combatevolved.util.HexVector;

public class VehicleEntity extends DynamicEntity{
    @Expose
    int driverID =-1;
    
    AgentEntity driver;
    
    private transient Inventory stash;

    public VehicleEntity() {
        stash = new Inventory(64);
    }

    public VehicleEntity(float row, float col, int ferretRender, float speed) {
        super(row, col, RenderConstants.FERRET_RENDER, speed);
        stash = new Inventory(64);
	}

	public boolean enterVehicle(AgentEntity agentPeon) {
        if (this.driver == null) {
            this.driver = agentPeon;
            driver.vehicle = this;
            driver.currentSpeed = this.speed;
            driverID = driver.getEntityID();
            return true;
        }
		return false;
    }

    private void setDriver(int driverID) {
        if (driverID != -1) {
            AbstractEntity newDriver = GameManager.get().getWorld().getEntityById(driverID);
            if (newDriver instanceof AgentEntity) {
                this.driver = (AgentEntity) newDriver;
                this.driver.setVehicle(this);
            }
        }
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

	public void move(HexVector velocity) {
        if (driver != null) {  
            this.position = this.position.add(velocity);
             driver.setPosition(this.position);
        } else if (this.driverID != -1) {
            setDriver(this.driverID);
        }
	}

    @Override
    public void onTick(long i) {
        if (driver == null && this.driverID != -1) {  
            setDriver(this.driverID);
        }
    }
    
    /**
     * Get the inventory of the vehicle
     * 
     * @return vehicle stash
     */
    public Inventory getInventory() {
    	return stash;
    }

}