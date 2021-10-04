package deco.combatevolved.entities.dynamicentities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AgentEntityTest {
    private AgentEntity testEntity;

    @Before
    public void setup() {
        testEntity = new AgentEntity(0, 0, 0);
    }

    @Test
    public void testGetVehicle() {
        assertNull(testEntity.getVehicle());
    }

    @Test
    public void testIsInVehicle() {
        assertFalse(testEntity.isInVehicle());
    }
}
