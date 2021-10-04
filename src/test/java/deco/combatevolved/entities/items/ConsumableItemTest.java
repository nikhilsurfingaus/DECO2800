package deco.combatevolved.entities.items;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Coconut;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Cola;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Medkit;
import deco.combatevolved.entities.items.consumableitems.ConcreteConsumableItems.Mentos;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ConsumableItemTest extends BaseGDXTest {

    private PlayerPeon playerPeon;

    private Coconut coconut;
    private Cola cola;
    private Mentos mentos;
    private String textureAtlas = "testFrames";
    private Medkit medkit;

    @Before
    public void setup() {
        playerPeon = new PlayerPeon(4,4,4,textureAtlas);
        coconut = new Coconut("coconut", 1, "coconut", "coconut", "yum");
        cola = new Cola("cola", 2, "cola", "cola", "thirsty");
        mentos = new Mentos("mentos", 1, "mentos", "mentos", "minty fresh");
        medkit = new Medkit("medkit", 1, "medkit", "medkit", "flex tape TM");
    }

    @Test
    public void testCoconutTimeUsed() {
        long timeUsed = 10;
        coconut.setTimeUsed(timeUsed);
        assertEquals(10, coconut.getTimeUsed());
    }

    @Test
    public void testCoconutConsume() {
        playerPeon.setDefenceMax(10);
        coconut.setPlayer(playerPeon);
        coconut.consume();
        assertEquals(20, playerPeon.getDefenceMax());
        coconut.revertNormal();
        assertEquals(10, playerPeon.getDefenceMax());
    }

    @Test
    public void testColaConsume() {
        playerPeon.setSpeed(10.0f);
        cola.setPlayer(playerPeon);
        cola.consume();
        assertEquals((int)20f, (int)playerPeon.getCurrentSpeed());
        cola.revertNormal();
        assertEquals(10, (int)playerPeon.getSpeed());
    }

    @Test
    public void testMentosConsume() {
        playerPeon.setOffenceMax(20);
        mentos.setPlayer(playerPeon);
        mentos.consume();
        assertEquals(40, playerPeon.getOffenceMax());
        mentos.revertNormal();
        assertEquals(20, playerPeon.getOffenceMax());
    }

    @Test
    public void testMedkitConsume() {
        playerPeon.setAttributes("soldierAtlas", "Explorer");
        int maxHealth = playerPeon.getMaxPlayerHealth();
        assertEquals(maxHealth, playerPeon.getHealth());
        playerPeon.loseHealth(5);
        assertEquals(maxHealth - 5, playerPeon.getHealth());
        medkit.setPlayer(playerPeon);
        medkit.consume();
        assertEquals(maxHealth, playerPeon.getMaxPlayerHealth());
    }
}
