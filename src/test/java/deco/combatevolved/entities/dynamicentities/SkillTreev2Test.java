package deco.combatevolved.entities.dynamicentities;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SkillTreev2Test extends BaseGDXTest {
    private PlayerAttributes player;
    String textureAtlas = "testFrames";
    private CombatEvolvedWorld gameWorld;
    private SkillTreev2 playerSkills;

    @Before
    public void setup() {
        gameWorld = new CombatEvolvedWorld();

        for (AbstractEntity entity : gameWorld.getEntities()) {
            if (entity instanceof PlayerPeon) {
                player = (PlayerAttributes) entity;
            }
        }

        player = new PlayerAttributes(0f, 0f, 0.05f, textureAtlas, "Explorer");
        player.setAttributes(textureAtlas,"Explorer");

        for (Tile tile : gameWorld.getTileListFromMap()) {
            tile.setObstructed(false);
        }
        GameManager.get().setWorld(gameWorld);
        gameWorld.addEntity(player);
        playerSkills = player.getPlayerSkills();
    }

    @Test
    public void testSkillTreeStructure() {
        assertTrue(playerSkills.getSkill("attackDamage1").getParents() == null);
        assertEquals(3, playerSkills.getSkill("attackDamage1").getChildren().size());
        assertTrue(playerSkills.getSkill("attackDamage1").getChildren().contains(playerSkills.getSkill("attackDamage2")));
        assertTrue(playerSkills.getSkill("attackDamage1").getChildren().contains(playerSkills.getSkill("attackExplosion")));
        assertTrue(playerSkills.getSkill("attackDamage1").getChildren().contains(playerSkills.getSkill("attackMortalReminder")));
        assertTrue(playerSkills.getSkill("attackDamage1").getChildren().contains(playerSkills.getSkill("attackDamage2")));
        assertEquals(1, playerSkills.getSkill("attackDamage2").getChildren().size());
        assertTrue(playerSkills.getSkill("attackDamage2").getChildren().contains(playerSkills.getSkill("attackDamage3")));
        assertEquals(1, playerSkills.getSkill("attackMortalReminder").getChildren().size());
        assertTrue(playerSkills.getSkill("attackMortalReminder").getChildren().contains(playerSkills.getSkill("attackDoubleEdged")));
        assertEquals(1, playerSkills.getSkill("attackDoubleEdged").getChildren().size());
        assertTrue(playerSkills.getSkill("attackDoubleEdged").getChildren().contains(playerSkills.getSkill("attackDamage4")));
        assertEquals(2, playerSkills.getSkill("attackExplosion").getChildren().size());
        assertTrue(playerSkills.getSkill("attackExplosion").getChildren().contains(playerSkills.getSkill("attackFireTouch")));
        assertTrue(playerSkills.getSkill("attackExplosion").getChildren().contains(playerSkills.getSkill("attackFlameTrail")));
        assertEquals(1, playerSkills.getSkill("attackFireTouch").getChildren().size());
        assertTrue(playerSkills.getSkill("attackFireTouch").getChildren().contains(playerSkills.getSkill("attackMoreExplosion")));
        assertEquals(1, playerSkills.getSkill("attackFlameTrail").getChildren().size());
        assertTrue(playerSkills.getSkill("attackFlameTrail").getChildren().contains(playerSkills.getSkill("attackMoreExplosion")));
        assertEquals(1, playerSkills.getSkill("attackMoreExplosion").getChildren().size());
        assertTrue(playerSkills.getSkill("attackMoreExplosion").getChildren().contains(playerSkills.getSkill("attackDamage4")));
        assertEquals(3, playerSkills.getSkill("attackDamage4").getParents().size());
        assertEquals(0, playerSkills.getSkill("attackDamage4").getChildren().size());
    }

    @Test
    public void testIncreaseSkillPoint() {
        player.increaseExp(1);
        assertEquals(1, player.getExp());
        player.increaseExp(99);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(2, player.getSkillPoint());
    }

    @Test
    public void testLearnBasicSkills() {
        player.increaseExp(100);
        assertEquals(2, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(2, player.getSkillPoint());
        player.increaseExp(120);
        assertEquals(3, player.getLevel());
        assertEquals(0, player.getExp());
        assertEquals(3, player.getSkillPoint());

        SkillNode skillToLearn = playerSkills.getSkill("attackDamage1");
        playerSkills.learnSkill(skillToLearn);
        assertEquals(46, player.getStats("attack"));
    }

    @Test
    // Tests if basic skills are applied even after leveling up
    public void testBasicSkillLevelUp() {
        SkillNode skillToLearn = playerSkills.getSkill("attackDamage1");
        playerSkills.learnSkill(skillToLearn);
        assertEquals(42, player.getStats("attack"));
        player.increaseExp(100);
        assertEquals((int) ((42 + 5 / 2) * 1.05), player.getStats("attack"));
    }

    @Test
    public void hasLearnt() {
        SkillNode skillToLearn = playerSkills.getSkill("attackDamage1");
        playerSkills.learnSkill(skillToLearn);
        assertTrue(playerSkills.hasLearnt(skillToLearn));
    }

    @Test
    public void getSkill() {
        assertEquals("Damage I", playerSkills.getSkill("attackDamage1").getName());
    }
}