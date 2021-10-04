package deco.combatevolved.entities.staticentities;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.staticentities.ControlTowers;
import deco.combatevolved.managers.*;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import org.junit.*;

import static org.junit.Assert.assertEquals;


public class ControlTowersTest extends BaseGDXTest {
    private static ControlTowers controlTower;
    private static CombatEvolvedWorld combatEvolvedWorld;
    private static PlayerPeon player;
    private static String textureAtlas = "testFrames";

    @BeforeClass
    public static void classSetup() {
        combatEvolvedWorld = new CombatEvolvedWorld();
        player = new PlayerPeon(0,0,0.1f, textureAtlas);
        combatEvolvedWorld.addEntity(player);
        GameManager.get().setPlayerEntityID(player.getEntityID());
        GameManager.get().setWorld(combatEvolvedWorld);
    }

    @AfterClass
    public static void classTearDown() {
        combatEvolvedWorld = null;
        player = null;
    }

    @Before
    public void setup() {

        Tile tile = new Tile(null,1,1);
        float tileCol = tile.getCol();
        //System.out.print(tileCol);
        controlTower = new ControlTowers(tile,true,"PcomTower1", 0, 0);
        //conSeedWorld.addEntity(controlTower);
    }

    @After
    public void tearDown() {
        controlTower = null;
    }

    @Test
    public void NoConquer() {
        assertEquals("initialised tower has initial conquestcompletion",
                0, controlTower.getConquestCompletion());
    }

    @Test
    public void singleConquer() {
        controlTower.onTick(1);
        assertEquals("tower has not been conquered at all",
                1, controlTower.getConquestCompletion());
    }


    @Test
    public void MultipleConquer() {
        controlTower.onTick(1);
        controlTower.onTick(1);
        assertEquals("tower has not been conquered at all",
                2, controlTower.getConquestCompletion());
    }

    @Test
    public void SingleDeConquer() {
        controlTower.onTick(1);
        player.setCol(40);
        controlTower.onTick(1);
        player.setCol(0);
        assertEquals("tower did not deconquer",
                0, controlTower.getConquestCompletion());
    }
}
