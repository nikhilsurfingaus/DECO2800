package deco.combatevolved.entities.enemyentities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.staticentities.defensivetowers.SimpleTower;
import deco.combatevolved.entities.staticentities.defensivetowers.Tower;
import deco.combatevolved.exceptions.EnemyValueException;
import deco.combatevolved.factories.EnemyEntityFactory;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.managers.EnemyManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.InputManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.managers.TextureManager;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GameManager.class)
public class BasicEnemyTest extends BaseGDXTest {

    private CombatEvolvedWorld gameWorld;

    private PlayerAttributes player;

    private BasicEnemy enemy;

    private String textureAtlas = "testFrames";

    @Before
    public void setup() {
        gameWorld = new CombatEvolvedWorld();

        mockStatic(GameManager.class);
        GameManager mockGM = mock(GameManager.class);
        NetworkManager mockNM = mock(NetworkManager.class);
        TextureManager mockTM = mock(TextureManager.class);
        when(GameManager.get()).thenReturn(mockGM);
        when(mockGM.getWorld()).thenReturn(gameWorld);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);
        when(GameManager.get().getManager(EnemyManager.class)).thenReturn(new EnemyManager());
        when(GameManager.getManagerFromInstance(EnemyManager.class)).thenReturn(new EnemyManager());

        KeyboardManager mockKM = mock(KeyboardManager.class);
        when(GameManager.getManagerFromInstance(KeyboardManager.class)).thenReturn(mockKM);
        InputManager mockIM = mock(InputManager.class);
        when(GameManager.getManagerFromInstance(InputManager.class)).thenReturn(mockIM);
        SoundManager mockSM = mock(SoundManager.class);
        when(GameManager.get().getManager(SoundManager.class)).thenReturn(mockSM);

        when(GameManager.getManagerFromInstance(TextureManager.class)).thenReturn(mockTM);
        TextureAtlas atlas = new TextureAtlas("resources/PlayerCharacter/Soldier/Soldier_Frames.atlas");
        when(mockTM.getAtlas(anyString())).thenReturn(atlas);

        for (AbstractEntity entity : gameWorld.getEntities()) {
            if (entity instanceof PlayerPeon) {
                player = (PlayerAttributes) entity;
            }
        }
        for (Tile tile : gameWorld.getTileListFromMap()) {
            tile.setObstructed(false);
        }

        enemy = new BasicEnemy(0,0);
    }

    @After
    public void teardown() {
        gameWorld = null;
        enemy = null;
        player = null;
    }

    @Test
    public void checkDamaged() throws EnemyValueException{
        assertFalse(enemy.isDamaged());
        enemy.loseHealth(1);
        assertTrue(enemy.isDamaged());
    }

    @Test
    public void checkAtacked() {
        assertFalse(enemy.hasAttacked());
        enemy.setLastAttack();
        assertTrue(enemy.hasAttacked());
    }

    @Test
    public void checkHealed() {
        HealerEnemy healer = new HealerEnemy(0,0);
        assertFalse(healer.hasHealed());
        healer.setLastHeal(0);
        assertTrue(healer.hasHealed());
    }

    @Test
    public void testConstructor() {
        assertThat("", enemy.getCol(), is(equalTo(0f)));
        assertThat("", enemy.getRow(), is(equalTo(0f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
        assertThat("", enemy.getHealth(), is(equalTo(70)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getDamage(), is(equalTo(5)));
    }

    @Test
    public void testHealthLossAndGain() throws EnemyValueException {
        enemy.gainHealth(10);
        assertThat("", enemy.getHealth(), is(equalTo(70)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        enemy.gainFullHealth();
        assertThat("", enemy.getHealth(), is(equalTo(70)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
    }

    @Test(expected = EnemyValueException.class)
    public void testEnemyValueException() throws EnemyValueException {
        enemy.gainHealth(-10);
        enemy.loseHealth(-10);
    }

    @Test
    public void testEnemySpeed() throws EnemyValueException {
        enemy.loseSpeed(0.05f);
        assertThat("", enemy.getSpeed(), is(equalTo(0.0f)));
        enemy.gainSpeed(0.05f);
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
    }

    @Test
    public void testBasicEnemy() {
        enemy = EnemyEntityFactory.createEnemy("basic", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
        assertThat("", enemy.getDamage(), is(equalTo(5)));
        assertThat("", enemy.getHealth(), is(equalTo(70)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(5f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        System.out.println(enemy.getAngle());
        enemy.setTextureByAngle(enemy.getAngle());
        System.out.println(enemy.getTexture());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Basic_Lizard_SnowBR")));
                break;
            case "Desert":
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Basic_Lizard_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Basic_Lizard_ForestBR")));
                break;
        }
    }

    @Test
    public void testFastEnemy() {
        enemy = EnemyEntityFactory.createEnemy("fast", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.07f)));
        assertThat("", enemy.getDamage(), is(equalTo(2)));
        assertThat("", enemy.getHealth(), is(equalTo(50)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(5f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_SnowBR")));
                break;
            case "Desert":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_DesertBR")));
                break;
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_ForestBR")));
                break;
        }
    }

    @Test
    public void testExplosiveEnemy() {
        enemy = EnemyEntityFactory.createEnemy("explosive", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
        assertThat("", enemy.getDamage(), is(equalTo(25)));
        assertThat("", enemy.getHealth(), is(equalTo(30)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(10f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Explode_SnowBR")));
                break;
            case "Desert":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Explode_DesertBR")));
                break;
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Explode_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Explode_ForestBR")));
                break;
        }

        PlayerPeon player = new PlayerPeon(1,2,0.1f, textureAtlas);
        player.setAttributes(textureAtlas, "Soldier");
        Tower tower = new SimpleTower(1,0);
        GameManager.get().getWorld().addEntity(player);
        GameManager.get().getWorld().addEntity(tower);
        enemy.death();
        GameManager.get().getManager(EnemyManager.class).resetExp();
        assertThat("", player.getHealth(), is(equalTo(125)));
        assertThat("", tower.getHealth(), is(equalTo(125)));
    }

    @Test
    public void testFlyingEnemy() {
        enemy = EnemyEntityFactory.createEnemy("flying", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
        assertThat("", enemy.getDamage(), is(equalTo(2)));
        assertThat("", enemy.getHealth(), is(equalTo(70)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(10f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Wasp_SnowBR")));
                break;
            case "Desert":
                assertThat("", enemy.getTextureString(), is(equalTo("Wasp_DesertBR")));
                break;
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Wasp_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Wasp_ForestBR")));
                break;
        }
    }

    @Test
    public void testHealerEnemy() {
        enemy = EnemyEntityFactory.createEnemy("healer", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.035f)));
        assertThat("", enemy.getDamage(), is(equalTo(0)));
        assertThat("", enemy.getHealth(), is(equalTo(75)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(15f)));
        assertThat("", ((HealerEnemy) enemy).getHealingAbility(), is(equalTo(5)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Wizard_Lizard_SnowBR")));
                break;
            case "Desert":
                assertThat("", enemy.getTextureString(), is(equalTo("Wizard_Lizard_DesertBR")));
                break;
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Wizard_Lizard_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Wizard_Lizard_ForestBR")));
                break;
        }

        BasicEnemy enemyHealed = EnemyEntityFactory.createEnemy("fast", 1, 2);
        GameManager.get().getManager(EnemyManager.class).addEnemyToGame(enemyHealed);
        ((HealerEnemy) enemy).setLastHeal(5000);
        GameManager.get().getManager(EnemyManager.class).enemyHeal();
        assertThat("", enemyHealed.getHealth(), is(equalTo(50)));
        GameManager.get().getManager(EnemyManager.class).resetDificulty();

    }

    @Test
    public void testHeavyEnemy() {
        enemy = EnemyEntityFactory.createEnemy("heavy", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.035f)));
        assertThat("", enemy.getDamage(), is(equalTo(5)));
        assertThat("", enemy.getHealth(), is(equalTo(200)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(15)));
        assertThat("", enemy.getExp(), is(equalTo(20f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Heavy_Lizard_SnowBR")));
                break;
            case "Desert":
                assertThat("", enemy.getTextureString(), is(equalTo("Heavy_Lizard_DesertBR")));
                break;
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Heavy_Lizard_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Heavy_Lizard_ForestBR")));
                break;
        }
    }

    @Test
    public void testRangeEnemy() {
        enemy = EnemyEntityFactory.createEnemy("ranged", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
        assertThat("", enemy.getDamage(), is(equalTo(2)));
        assertThat("", enemy.getHealth(), is(equalTo(50)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(7f)));
        assertThat("", enemy.getRange(), is(equalTo(10f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Gun_Lizard_SnowBR")));
                break;
            case "Desert":
                assertThat("", enemy.getTextureString(), is(equalTo("Gun_Lizard_DesertBR")));
                break;
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Gun_Lizard_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Gun_Lizard_ForestBR")));
                break;
        }
    }

    @Test
    public void testSmallEnemy() {
        enemy = EnemyEntityFactory.createEnemy("small", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
        assertThat("", enemy.getDamage(), is(equalTo(1)));
        assertThat("", enemy.getHealth(), is(equalTo(30)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(3f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());

        switch (enemy.getBiomeSuffix()) {
            case "Snow":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Small_SnowBR")));
                break;
            case "Desert":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Small_DesertBR")));
                break;
            case "plains":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Small_PlainsBR")));
                break;
            case "Forest":
                assertThat("", enemy.getTextureString(), is(equalTo("Spider_Small_ForestBR")));
                break;
        }
    }

    @Test
    public void testRangerEnemy() {
        enemy = EnemyEntityFactory.createEnemy("ranger", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.05f)));
        assertThat("", enemy.getDamage(), is(equalTo(15)));
        assertThat("", enemy.getHealth(), is(equalTo(50)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(20f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());
        ((RangerEnemy) enemy).setStealth(true);
        assertThat("", enemy.getTextureString(), is(equalTo("Ranger_Lizard_ForestBR")));
        enemy.setTextureByAngle(enemy.getAngle());
        ((RangerEnemy) enemy).setStealth(false);
        assertThat("", enemy.getTextureString(), is(equalTo("STRanger_Lizard_ForestBR")));
    }

    @Test
    public void testVehicleEnemy() {
        enemy = EnemyEntityFactory.createEnemy("vehicle", 1, 1);
        assertThat("", enemy.getRow(), is(equalTo(1f)));
        assertThat("", enemy.getCol(), is(equalTo(1f)));
        assertThat("", enemy.getSpeed(), is(equalTo(0.03f)));
        assertThat("", enemy.getDamage(), is(equalTo(3)));
        assertThat("", enemy.getHealth(), is(equalTo(150)));
        assertThat("", enemy.getArmourHealth(), is(equalTo(0)));
        assertThat("", enemy.getExp(), is(equalTo(15f)));
        enemy.setAngle(new HexVector(1,1), new HexVector(2,2));
        assertThat("", enemy.getAngle(), is(equalTo(-135d)));
        enemy.setTextureByAngle(enemy.getAngle());
        assertThat("", enemy.getTextureString(), is(equalTo("Spider_VehicleBR")));
    }

    @Test
    public void testGivePlayerExpWhenEnemyDies() {
        player = new PlayerAttributes(0f, 0f, 0.05f, textureAtlas, "Soldier");
        player.setAttributes("soldierAtlas", "Soldier");
        gameWorld.addEntity(player);
        enemy.death();
        assertEquals(5, player.getExp());
    }
}