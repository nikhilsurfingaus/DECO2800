package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerAttributesTest extends BaseGDXTest {
    private String textureAtlas = "testFrames";
    @Test
    public void setPlayerAttributes() {
        PlayerAttributes p = new PlayerAttributes(1,1,1,textureAtlas,"Explorer");
        assertEquals(15,p.getStats("defence"));
    }

    @Test
    public void explorerIncreaseStats() {
        PlayerAttributes p = new PlayerAttributes(1,1,1,textureAtlas,"Explorer");
        p.statsChange(10,"speed");
        assertEquals(60,p.getStats("speed"));
    }

    @Test
    public void explorerDecreaseStats() {
        PlayerAttributes p = new PlayerAttributes(1,1,1,textureAtlas,"Explorer");
        p.statsChange(-10,"speed");
        assertEquals(40,p.getStats("speed"));
        p.statsChange(-20, "health");
        assertEquals(80, p.getStats("health"));
    }

    @Test
    public void attackPattern() {
        PlayerAttributes p = new PlayerAttributes(1,1,1,textureAtlas,"Explorer");
        assertEquals("bullet",p.getAttackPattern());
        assertEquals("bullet",p.getDefaultAttackPattern());
        assertEquals("Explorer", p.getPlayerTexture());
        p = new PlayerAttributes(1,1,1,textureAtlas,"Engineer");
        assertEquals("projectile",p.getAttackPattern());
        assertEquals("projectile",p.getDefaultAttackPattern());
        assertEquals("Engineer", p.getPlayerTexture());
        p = new PlayerAttributes(1,1,1,textureAtlas,"Soldier");
        assertEquals("bullet",p.getAttackPattern());
        assertEquals("bullet",p.getDefaultAttackPattern());
        assertEquals("Soldier", p.getPlayerTexture());
    }

    @Test
    public void getStats() {
        PlayerAttributes p = new PlayerAttributes(1, 1, 1, textureAtlas, "Explorer");
        assertEquals(50, p.getStats("speed"));
    }

    @Test
    public void testGivePlayerExp() { // Simple test of whether player is gaining experience properly
        CombatEvolvedWorld gameWorld = new CombatEvolvedWorld();
        GameManager.get().setWorld(gameWorld);
        PlayerAttributes player = new PlayerAttributes(0f, 0f, 0.05f, textureAtlas, "Soldier");
        gameWorld.addEntity(player);
        player.increaseExp(1);
        assertEquals(1, player.getExp());
        player.increaseLevel();
        assertEquals(1, player.getLevel());
        player.increaseExp(99);
        assertEquals(2, player.getLevel()); // Checks if player has successfully leveled up
        assertEquals(0, player.getExp());   // and currently has zero experience

        player = new PlayerAttributes(0f, 0f, 0.05f, textureAtlas, "Engineer");
        player.increaseExp(100);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp());
    }

    @Test
    public void testLevelUpOnAttributes() {
        CombatEvolvedWorld gameWorld = new CombatEvolvedWorld();
        GameManager.get().setWorld(gameWorld);
        PlayerAttributes player = new PlayerAttributes(0f, 0f, 0.05f, textureAtlas,"Soldier");
        gameWorld.addEntity(player);
        assertEquals(50, player.getStats("attack"));
        player.increaseExp(100);
        assertEquals(57, player.getStats("attack"));
    }
}