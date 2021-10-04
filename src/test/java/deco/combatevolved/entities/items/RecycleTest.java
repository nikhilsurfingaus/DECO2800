package deco.combatevolved.entities.items;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecycleTest extends BaseGDXTest {

    static Item item = new Item("item", 1);
    static Item item2 = new Item("item2", 2);
    static Item item3 = new Item("item3", 3);
    Recycle recycle;
    PlayerPeon player;
    private String textureAtlas = "testFrames";

    @Before
    public void setUp() {
        recycle = new Recycle();
        player = new PlayerPeon(5, 5, 5, textureAtlas);
    }

    @Test
    public void convert() {
        assertEquals(0, recycle.addItem(item, 5));
        assertEquals(5, recycle.calculate());
        assertEquals(0, player.getScrap());
        assertTrue(recycle.convert(player));
        assertEquals(5, player.getScrap());
    }
    
    @Test
    public void convertNull() {
    	assertFalse(recycle.convert(player));
    	assertEquals(0, player.getScrap());
    }

    @Test
    public void calculateRarityOne() {
        assertEquals(0, recycle.addItem(item));
        assertEquals(1, recycle.calculate());
        assertEquals(0, recycle.addItem(item, 5));
        assertEquals(6, recycle.calculate());
    }

    @Test
    public void calculateHigherRarity() {
        assertEquals(0, recycle.addItem(item2));
        assertEquals(2, recycle.calculate());
        assertEquals(0, recycle.addItem(item2, 5));
        assertEquals(12, recycle.calculate());
    }
    
    @Test
    public void calculateNull() {
    	assertEquals(0, recycle.calculate());
    }
}