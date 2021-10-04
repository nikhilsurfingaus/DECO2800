package deco.combatevolved.worlds.biomes;

import org.junit.Test;
import static org.junit.Assert.*;

public class BiomeTypeTest {

    @Test
    public void asLowerCase() {
    	assertEquals("snow", BiomeType.SNOW.asLowerCase());
    }
}