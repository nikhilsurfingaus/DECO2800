package deco.combatevolved.entities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import deco.combatevolved.entities.dynamicentities.VehicleEntity;
import deco.combatevolved.entities.items.Inventory;
import org.junit.Assert;
import org.junit.Test;

public class VehicleEntityTest {

	@Test
	public void testConstructor() {
		VehicleEntity vehicle = new VehicleEntity(1,1,1,1);
		assertThat("", vehicle.getCol(), is(equalTo(1f)));
		assertThat("", vehicle.getRow(), is(equalTo(1f)));
		assertThat("", vehicle.getSpeed(), is(equalTo(1f)));
		assertThat("", vehicle.getTextureString(), is(equalTo("error_box")));
		assertTrue(vehicle.getInventory() instanceof Inventory);
	}
	
	@Test
	public void testGetInventory() {
		VehicleEntity vehicle = new VehicleEntity(1,1,1,1);
		Assert.assertEquals(64, vehicle.getInventory().capacity());
	}
}
