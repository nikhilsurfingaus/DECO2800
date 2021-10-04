package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SkillTreeTest extends BaseGDXTest {

    private CombatEvolvedWorld gameWorld;
    private PlayerAttributes player;
    String textureAtlas = "testFrames";

    @Before
    public void setup() {
        gameWorld = new CombatEvolvedWorld();
    }

    @After
    public void teardown() {
        gameWorld = null;
    }


    @Test
    public void testIncreaseSkillPoint() {
        player = new PlayerAttributes(0f, 0f, 0.05f, textureAtlas, "Soldier");
        player.setAttributes(textureAtlas,"Soldier");
        gameWorld.addEntity(player);
        player.increaseExp(1);
        assertEquals(1, player.getExp());
        player.increaseExp(99);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(2, player.getSkillPoint());
    }


    @Test
    public void explorerAddSkillsDefence() {
        player = new PlayerAttributes(1,1,1, textureAtlas, "Explorer");
        player.setAttributes(textureAtlas,"Explorer");
        gameWorld.addEntity(player);
        player.increaseExp(100);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(2, player.getSkillPoint());
        player.increaseExp(120);
        assertEquals(3, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(3, player.getSkillPoint());

        SkillTree s = new SkillTree(1,1,1, textureAtlas, "Explorer");
        s.setSkillPoint(player.getSkillPoint());
        s.addSkills(10,"defence");

        assertEquals(16,s.getStats("defence"));
        assertEquals(105,s.getStats("health"));
    }
    @Test
    public void explorerAddSkillsOffence() {
        player = new PlayerAttributes(1,1,1,textureAtlas,"Explorer");
        player.setAttributes(textureAtlas,"Explorer");
        gameWorld.addEntity(player);
        player.increaseExp(100);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(2, player.getSkillPoint());

        SkillTree s = new SkillTree(1,1,1,textureAtlas,"Explorer");
        s.setSkillPoint(player.getSkillPoint());
        s.addSkills(15,"offensive");

        assertEquals(46,s.getStats("attack"));
        assertEquals(16,s.getStats("defence"));
    }
    @Test
    public void explorerAddSkillsSpeed() {
        player = new PlayerAttributes(1,1,1,textureAtlas,"Explorer");
        player.setAttributes(textureAtlas,"Explorer");
        gameWorld.addEntity(player);
        player.increaseExp(100);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(2, player.getSkillPoint());

        SkillTree s = new SkillTree(1,1,1,textureAtlas,"Explorer");
        s.setSkillPoint(player.getSkillPoint());
        s.addSkills(10,"speed");
        assertEquals(55,s.getStats("speed"));
        assertEquals(42,s.getStats("attack"));
    }


}