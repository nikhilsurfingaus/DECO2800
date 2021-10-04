package deco.combatevolved.entities.dynamicentities;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.managers.*;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.Tile;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameManager.class})

public class PlayerPeonTest extends BaseGDXTest {
    private GameManager mockGM;
    private NetworkManager mockNM;
    private TextureManager mockTM;
    private OnScreenMessageManager mockOSMM;
    private TextureAtlas atlas;
    private CombatEvolvedWorld mockedWorld;
    private String textureAtlas = "soldierAtlas";
    private PlayerPeon testPlayer;
    private String[] statesArray = new String[] {"N_Walking", "NE_Walking", "NW_Walking", "S_Walking", "SE_Walking", "SW_Walking",
            "N_Running", "NE_Running", "NW_Running", "S_Running", "SE_Running", "SW_Running",
            "N_Walking_Gun", "NE_Walking_Gun", "NW_Walking_Gun", "S_Walking_Gun", "SE_Walking_Gun", "SW_Walking_Gun",
            "N_Idle", "NE_Idle", "NW_Idle", "S_Idle", "SE_Idle", "SW_Idle", "Death"};
    private Map<Integer, Integer> userEntities;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        atlas = new TextureAtlas("resources/PlayerCharacter/Soldier/Soldier_Frames.atlas");
        testPlayer = new PlayerPeon(0, 0, 1, textureAtlas);
        testPlayer.setAttributes(textureAtlas, "Soldier");
        userEntities = new HashMap<Integer, Integer>(){{put(0, testPlayer.getEntityID());}};

        mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        when(GameManager.get()).thenReturn(mockGM);

        mockNM = mock(NetworkManager.class);
        when(mockGM.getManager(NetworkManager.class)).thenReturn(mockNM);
        doReturn(userEntities).when(mockNM).getUserEntities();

        mockTM = mock(TextureManager.class);
        when(GameManager.getManagerFromInstance(TextureManager.class)).thenReturn(mockTM);
        doReturn(atlas).when(mockTM).getAtlas(textureAtlas);

        mockOSMM = mock(OnScreenMessageManager.class);
        when(GameManager.getManagerFromInstance(OnScreenMessageManager.class)).thenReturn(mockOSMM);
        doReturn(false).when(mockOSMM).isTyping();

        mockedWorld = mock(CombatEvolvedWorld.class);
        when(mockGM.getWorld()).thenReturn(mockedWorld);

        InputManager mockIM = mock(InputManager.class);
        when(GameManager.getManagerFromInstance(InputManager.class)).thenReturn(mockIM);

        KeyboardManager mockKBM = mock(KeyboardManager.class);
        when(GameManager.getManagerFromInstance(KeyboardManager.class)).thenReturn(mockKBM);
    }

    @Test
    public void playerState() {
        assertEquals("S_Idle", testPlayer.getCurrentState());
        assertEquals("S_Idle", testPlayer.getPreviousState());
    }

    @Test
    public void getInventory() {
        PlayerPeon peon = new PlayerPeon(1f, 1f, 1f, textureAtlas);
        Assert.assertEquals(8, peon.getInventory().capacity());
    }

    @Test
    public void testUpdate() {
        PlayerPeon mockPlayer = mock(PlayerPeon.class);
        doCallRealMethod().when(mockPlayer).update(anyFloat());
        mockPlayer.update(0.1f);
        verify(mockPlayer).setPosition(anyFloat(), anyFloat());
        verify(mockPlayer).setRegion(any(TextureRegion.class));
    }

    @Test
    public void testSprint() {
        PlayerPeon p = new PlayerPeon(1, 1, 1, textureAtlas);
        HexVector vel = p.getVelocity();

        vel.setRow(-1);
        vel.setCol(-1);
        p.setVelocity(vel);
        p.sprint(2);
        Assert.assertEquals(-2, p.getVelocity().getRow(), 0.01);
        Assert.assertEquals(-2, p.getVelocity().getCol(), 0.01);

        vel.setRow(1);
        vel.setCol(1f);
        p.setVelocity(vel);
        p.sprint(2);
        Assert.assertEquals(2, p.getVelocity().getRow(), 0.01);
        Assert.assertEquals(2, p.getVelocity().getCol(), 0.01);
    }

    @Test
    public void testIsSprinting() {
        PlayerPeon p = new PlayerPeon(0, 0, 0, textureAtlas);
        assertFalse(p.isSprinting());

        p.notifyKeyDown(Keys.SHIFT_LEFT);
        assertTrue(p.isSprinting());
    }

    @Test
    public void testIsMoving() {
        HexVector vel = testPlayer.getVelocity();

        assertFalse(testPlayer.isMoving());

        vel.setRow(0);
        vel.setCol(1);
        testPlayer.setVelocity(vel);

        assertTrue(testPlayer.isMoving());

        vel.setRow(1);
        vel.setCol(0);
        testPlayer.setVelocity(vel);

        assertTrue(testPlayer.isMoving());
    }

    @Test
    public void testIsMovingDiagonal() {
        HexVector vel = testPlayer.getVelocity();
        assertFalse(testPlayer.isMovingDiagonal());

        vel.setRow(1);
        vel.setCol(1);
        testPlayer.setVelocity(vel);
        assertTrue(testPlayer.isMovingDiagonal());
    }

    @Test
    public void testUpdateDamagedState() {
        testPlayer.setDamaged(true);
        testPlayer.updateDamagedState(0.1f);
        assertEquals(0.1f, testPlayer.getDamagedCounter(), 0.001f);
        testPlayer.updateDamagedState(1);
        assertFalse(testPlayer.isDamaged());
        assertEquals(0, testPlayer.getDamagedCounter(), 0.001f);
    }

    @Test
    public void testGetDamagedCounter() {
        assertEquals(0, testPlayer.getDamagedCounter(), 0.001f);
    }

    @Test
    public void testIncrementDamagedCounter() {
        testPlayer.incrementDamagedCounter(1);
        assertEquals(1, testPlayer.getDamagedCounter(), 0.001f);
    }

    @Test
    public void testResetDamagedCounter() {
        testPlayer.incrementDamagedCounter(1);
        testPlayer.resetDamagedCounter();
        assertEquals(0, testPlayer.getDamagedCounter(), 0.001f);
    }

    @Test
    public void testSetDamaged() {
        testPlayer.setDamaged(true);
        assertTrue(testPlayer.isDamaged());
    }

    @Test
    public void testSetShooting() {
        testPlayer.setShooting(true);
        assertTrue(testPlayer.isShooting());
    }

    @Test
    public void testIsShooting() {
        assertFalse(testPlayer.isShooting());
    }

    @Test
    public void testIncrementShootingTimer() {
        testPlayer.incrementShootingTimer(0.1f);
        assertEquals(0.1, testPlayer.getShootingTimer(), 0.001);
    }

    @Test
    public void testResetShootingTimer() {
        testPlayer.incrementShootingTimer(0.1f);
        testPlayer.resetShootingTimer();
        assertEquals(0, testPlayer.getShootingTimer(), 0.001);
    }

    @Test
    public void testUpdateShootingState() {
        testPlayer.setShooting(true);
        testPlayer.updateShootingState(0.1f);
        assertEquals(0.1, testPlayer.getShootingTimer(), 0.001);

        testPlayer.updateShootingState(0.15f);
        assertFalse(testPlayer.isShooting());
        assertEquals(0, testPlayer.getShootingTimer(), 0.001);
    }

    @Test
    public void testGetFrame() {
        PlayerPeon mockPlayer = mock(PlayerPeon.class, CALLS_REAL_METHODS);
        TextureRegion mockTR = mock(TextureRegion.class);
        Animation mockAnimation = mock(Animation.class);
        Tile testTile = new Tile(null, 0, 0);
        HexVector mockVelocity = mock(HexVector.class);
        doReturn(mockVelocity).when(mockPlayer).getVelocity();
        doReturn(0f).when(mockVelocity).getCol();
        doReturn(0f).when(mockVelocity).getRow();
        doReturn(testTile).when(mockedWorld).getTile(anyFloat(), anyFloat());
        doReturn(mockAnimation).when(mockPlayer).getAnimation(anyString());
        doReturn(mockTR).when(mockAnimation).getKeyFrame(anyFloat(), anyBoolean());
        doReturn(true).when(mockPlayer).isSprinting();
        doReturn(true).when(mockPlayer).isMoving();
        doReturn("sIdle").when(mockPlayer).getCurrentState();
        doReturn("sIdle").when(mockPlayer).getPreviousState();
        doReturn(0f).when(mockPlayer).getCol();
        doReturn(0f).when(mockPlayer).getRow();
        doReturn(statesArray).when(mockPlayer).getStatesArray();
        doReturn(true).when(mockPlayer).isAlive();

        mockPlayer.getFrame(.5f);
        verify(mockPlayer).setState();
        verify(mockPlayer).setPreviousState(anyString());
        verify(mockPlayer, atLeast(3)).getCurrentState();
        verify(mockPlayer).getPreviousState();
        verify(mockPlayer, times(2)).isMoving();
        verify(mockPlayer).incrementAnimationTimer(anyFloat());

        doReturn(false).when(mockPlayer).isMoving();

        mockPlayer.getFrame(.5f);
        verify(mockPlayer).resetAnimationTimer();
    }

    @Test
    public void testSetState() {
        Tile testTile = new Tile(null, 0, 0);
        when(mockedWorld.getTile(anyFloat(), anyFloat())).thenReturn(testTile);

        testPlayer.setRotation(0);
        testPlayer.setState();
        assertEquals("N_Idle", testPlayer.getCurrentState());

        testPlayer.setRotation(67.5f);
        testPlayer.setState();
        assertEquals("NW_Idle", testPlayer.getCurrentState());

        testPlayer.setRotation(112.5f);
        testPlayer.setState();
        assertEquals("SW_Idle", testPlayer.getCurrentState());

        testPlayer.setRotation(180);
        testPlayer.setState();
        assertEquals("S_Idle", testPlayer.getCurrentState());

        testPlayer.setRotation(247.5f);
        testPlayer.setState();
        assertEquals("SE_Idle", testPlayer.getCurrentState());

        testPlayer.setRotation(292.5f);
        testPlayer.setState();
        assertEquals("NE_Idle", testPlayer.getCurrentState());

        testPlayer.setRotation(360);
        testPlayer.setState();
        assertEquals("N_Idle", testPlayer.getCurrentState());

        HexVector vel = testPlayer.getVelocity();
        vel.setCol(1);
        vel.setRow(1);
        testPlayer.setVelocity(vel);

        testPlayer.setRotation(0);
        testPlayer.setState();
        assertEquals("N_Walking", testPlayer.getCurrentState());

        testPlayer.setRotation(67.5f);
        testPlayer.setState();
        assertEquals("NW_Walking", testPlayer.getCurrentState());

        testPlayer.setRotation(112.5f);
        testPlayer.setState();
        assertEquals("SW_Walking", testPlayer.getCurrentState());

        testPlayer.setRotation(180);
        testPlayer.setState();
        assertEquals("S_Walking", testPlayer.getCurrentState());

        testPlayer.setRotation(247.5f);
        testPlayer.setState();
        assertEquals("SE_Walking", testPlayer.getCurrentState());

        testPlayer.setRotation(292.5f);
        testPlayer.setState();
        assertEquals("NE_Walking", testPlayer.getCurrentState());

        testPlayer.setRotation(360);
        testPlayer.setState();
        assertEquals("N_Walking", testPlayer.getCurrentState());

        testPlayer.notifyKeyDown(Keys.SHIFT_LEFT);

        testPlayer.setRotation(0);
        testPlayer.setState();
        assertEquals("N_Running", testPlayer.getCurrentState());

        testPlayer.setRotation(67.5f);
        testPlayer.setState();
        assertEquals("NW_Running", testPlayer.getCurrentState());

        testPlayer.setRotation(112.5f);
        testPlayer.setState();
        assertEquals("SW_Running", testPlayer.getCurrentState());

        testPlayer.setRotation(180);
        testPlayer.setState();
        assertEquals("S_Running", testPlayer.getCurrentState());

        testPlayer.setRotation(247.5f);
        testPlayer.setState();
        assertEquals("SE_Running", testPlayer.getCurrentState());

        testPlayer.setRotation(282.5f);
        testPlayer.setState();
        assertEquals("NE_Running", testPlayer.getCurrentState());

        testPlayer.setRotation(360);
        testPlayer.setState();
        assertEquals("N_Running", testPlayer.getCurrentState());

        testPlayer.setShooting(true);

        testPlayer.setRotation(0);
        testPlayer.setState();
        assertEquals("N_Walking_Gun", testPlayer.getCurrentState());

        testPlayer.setRotation(67.5f);
        testPlayer.setState();
        assertEquals("NW_Walking_Gun", testPlayer.getCurrentState());

        testPlayer.setRotation(112.5f);
        testPlayer.setState();
        assertEquals("SW_Walking_Gun", testPlayer.getCurrentState());

        testPlayer.setRotation(180);
        testPlayer.setState();
        assertEquals("S_Walking_Gun", testPlayer.getCurrentState());

        testPlayer.setRotation(247.5f);
        testPlayer.setState();
        assertEquals("SE_Walking_Gun", testPlayer.getCurrentState());

        testPlayer.setRotation(292.5f);
        testPlayer.setState();
        assertEquals("NE_Walking_Gun", testPlayer.getCurrentState());

        testPlayer.setRotation(360);
        testPlayer.setState();
        assertEquals("N_Walking_Gun", testPlayer.getCurrentState());

        testPlayer.setAlive(false);

        testPlayer.setState();
        assertEquals("Death", testPlayer.getCurrentState());
    }

    @Test
    public void testRevive() {
        testPlayer.loseHealth(999);
        assertFalse(testPlayer.isAlive());

        PlayerPeon otherPlayerOne = new PlayerPeon(0, 0, 0, textureAtlas);
        PlayerPeon otherPlayerTwo = new PlayerPeon(0, 0, 0, textureAtlas);
        userEntities.put(1, otherPlayerOne.getEntityID());
        userEntities.put(2, otherPlayerTwo.getEntityID());
        doReturn(otherPlayerOne).when(mockedWorld).getEntityById(otherPlayerOne.getEntityID());
        doReturn(otherPlayerTwo).when(mockedWorld).getEntityById(otherPlayerTwo.getEntityID());

        testPlayer.revive(1);
        assertEquals(1, testPlayer.getReviveCounter(), 0.001f);

        otherPlayerOne.setPosition(0.99f, 0.99f, 0);
        testPlayer.revive(4);
        assertEquals(0, testPlayer.getReviveCounter(), 0.001f);
        assertTrue(testPlayer.isAlive());
        assertEquals(testPlayer.getMaxPlayerHealth(), testPlayer.getHealth());
    }

    @Test
    public void testIncrementReviveCounter() {
        testPlayer.incrementReviveCounter(1);
        assertEquals(1, testPlayer.getReviveCounter(), 0.001f);
    }

    @Test
    public void testResetReviveCounter() {
        testPlayer.incrementReviveCounter(1);
        testPlayer.resetReviveCounter();
        assertEquals(0, testPlayer.getReviveCounter(), 0.001f);
    }

    @Test
    public void testSetAlive() {
        testPlayer.setAlive(false);
        assertFalse(testPlayer.isAlive());
    }

    @Test
    public void testSetCurrentState() {
        testPlayer.setCurrentState("N_Idle");
        assertEquals("N_Idle", testPlayer.getCurrentState());
    }

    @Test
    public void testSetPreviousState() {
        testPlayer.setPreviousState("N_Idle");
        assertEquals("N_Idle", testPlayer.getPreviousState());
    }

    @Test
    public void testGetCurrentState() {
        assertEquals("S_Idle", testPlayer.getCurrentState());
    }

    @Test
    public void testGetPreviousState() {
        assertEquals("S_Idle", testPlayer.getPreviousState());
    }

    @Test
    public  void testGetEntityAtlas() {
        assertEquals(atlas, testPlayer.getEntityAtlas());
    }

    @Test
    public void testPosTileCoord() {
        when(GameManager.get()).thenCallRealMethod();

        GameManager gm = GameManager.get();
        PlayerPeon player = new PlayerPeon(0f, 0f, 0.05f, textureAtlas);
        CombatEvolvedWorld world = new CombatEvolvedWorld();
        gm.setWorld(world);

        Assert.assertEquals(0f, player.getRow(), 0.01f);
        Assert.assertEquals(0f, player.getCol(), 0.01f);

        Tile posTile, posTileN, posTileS;

        // assert that current tile is not null
        float[] coords = player.getPosTile(player.getCol(), player.getRow(), 0);
        posTile = gm.getWorld().getTile(coords[0], coords[1]);
        assertNotNull(posTile);

        // test for position half a unit north of even column
        player.getPosition().setRow(0.5f);
        coords = player.getPosTile(player.getCol(), player.getRow(), 0);
        posTile = gm.getWorld().getTile(coords[0], coords[1]);
        posTileN = gm.getWorld().getTile(coords[0], coords[2]);
        posTileS = gm.getWorld().getTile(coords[0], coords[3]);

        assertEquals(1f, posTile.getRow(), 0.01f);
        assertEquals(1f, posTileN.getRow(), 0.01f);
        assertEquals(0f, posTileS.getRow(), 0.01f);

        // test for odd column
        player.getPosition().setCol(1);
        player.getPosition().setRow(0);
        coords = player.getPosTile(player.getCol(), player.getRow(), 0);
        posTile = gm.getWorld().getTile(coords[0], coords[1]);
        posTileN = gm.getWorld().getTile(coords[0], coords[2]);
        posTileS = gm.getWorld().getTile(coords[0], coords[3]);

        assertEquals(0.5f, posTile.getRow(), 0.01f);
        assertEquals(0.5f, posTileN.getRow(), 0.01f);
        assertEquals(-0.5f, posTileS.getRow(), 0.01f);
    }

    @Test
    public void testDecreaseEnergy() {
        PlayerPeon p = new PlayerPeon(1,1,1, textureAtlas);
        // initial energy = 100
        // test initial energy
        assertEquals(100, p.getPlayerEnergy(), 0.0001);
        // test decreasing energy by 50
        p.decreaseEnergy(50);
        assertEquals(50, p.getPlayerEnergy(), 0.0001);
        // test decreasing energy by 0
        p.decreaseEnergy(0);
        assertEquals(50, p.getPlayerEnergy(), 0.0001);
        // test decreasing energy by negative value
        p.decreaseEnergy(-10);
        assertEquals(60, p.getPlayerEnergy(), 0.0001);
        // test decreasing energy when it is <= 0
        p.decreaseEnergy(110);
        assertEquals(0, p.getPlayerEnergy(), 0.0001);
    }

    @Test
    public void testIncreaseEnergy() {
        PlayerPeon p = new PlayerPeon(1,1,1, textureAtlas);
        // initial energy = 100
        // test initial energy
        assertEquals(100, p.getPlayerEnergy(), 0.0001);
        // test increasing energy when energy is max
        p.increaseEnergy(20);
        assertEquals(100, p.getPlayerEnergy(), 0.0001);
        p.decreaseEnergy(50);
        // test increasing energy
        p.increaseEnergy(20);
        assertEquals(70, p.getPlayerEnergy(), 0.0001);
        // test increasing energy by negative value
        p.increaseEnergy(-20);
        assertEquals(50, p.getPlayerEnergy(), 0.0001);
        // test increasing energy by 0
        p.increaseEnergy(0);
        assertEquals(50, p.getPlayerEnergy(), 0.0001);
    }

    @Test
    public void testGetMaxPlayerEnergy() {
        PlayerPeon p = new PlayerPeon(1,1,1, textureAtlas);
        p.decreaseEnergy(20);
        assertEquals(100, p.getMaxPlayerEnergy());
    }

    @Test
    public void testGetPlayerEnergy() {
        PlayerPeon p = new PlayerPeon(1,1,1, textureAtlas);
        p.decreaseEnergy(20);
        assertEquals(80, p.getPlayerEnergy());
        p.increaseEnergy(200);
        assertEquals(100, p.getPlayerEnergy());
        p.decreaseEnergy(200);
        assertEquals(0, p.getPlayerEnergy());
    }

    @Test
    public void testGetMaxPlayerHealth() {
        // test getting max health of different player types
        // explorer
        PlayerPeon p = new PlayerPeon(1,1,1, textureAtlas);
        p.setAttributes(textureAtlas,"Explorer");
        p.decreaseEnergy(20);
        assertEquals(100, p.getMaxPlayerHealth());
        // soldier
        PlayerPeon p2 = new PlayerPeon(1,1,1, textureAtlas);
        p2.setAttributes(textureAtlas,"Soldier");
        p2.decreaseEnergy(20);
        assertEquals(150, p2.getMaxPlayerHealth());
        // engineer
        PlayerPeon p3 = new PlayerPeon(1,1,1, textureAtlas);
        p3.setAttributes(textureAtlas,"Engineer");
        p3.decreaseEnergy(20);
        assertEquals(75, p3.getMaxPlayerHealth());
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testTileCollisionObstruction(){
        when(GameManager.get()).thenCallRealMethod();

        GameManager gm = GameManager.get();
        PlayerPeon player = new PlayerPeon(0f, 0f, 0.05f, textureAtlas);
        CombatEvolvedWorld world = new CombatEvolvedWorld();
        gm.setWorld(world);

        if (gm.getWorld().getTile(player.getPosition()).isObstructed()) {
            gm.getWorld().getTile(player.getPosition()).setObstructed(false);
        }
        assertFalse(gm.getWorld().getTile(player.getPosition()).isObstructed());

        //set the tile above as obstructed
        Tile nextTile= gm.getWorld().getTile(0,1);
        assertNotNull(nextTile);
        nextTile.setObstructed(true);

        //start moving to tile above
        HexVector vel= player.getVelocity();
        vel.setRow(0.05f);

        //check obstruction to 0.25 (the row offset for moving onto obstructed tiles)
        HexVector nextPos= player.getPosition().add(vel); //row 0.05
        assertFalse(player.tileCollision(nextPos));
        nextPos= nextPos.add(vel);			//row 0.1
        assertFalse(player.tileCollision(nextPos));
        nextPos= nextPos.add(vel);			//row 0.15
        assertFalse(player.tileCollision(nextPos));
        nextPos= nextPos.add(vel);			//row 0.2
        assertFalse(player.tileCollision(nextPos));
        nextPos= nextPos.add(vel);			//row 0.25
        assertFalse(player.tileCollision(nextPos));

        //check that moving to 0.3 is obstructed
        nextPos= nextPos.add(vel);			//row 0.3
        assertTrue(player.tileCollision(nextPos));
    }

    @Test
    public void testTileCollisionNull(){
        when(GameManager.get()).thenCallRealMethod();

        GameManager gm = GameManager.get();
        PlayerPeon player = new PlayerPeon(0f, 0f, 0.05f, textureAtlas);
        CombatEvolvedWorld world = new CombatEvolvedWorld();
        gm.setWorld(world);

        //get the tile at edge of map
        Tile notNullTile= gm.getWorld().getTile(0,24);
        assertNotNull(notNullTile);
        Tile nullTile= gm.getWorld().getTile(0,25);

        //set player's position to map edge
        player.getPosition().setRow(25);

        //start moving to tile above
        HexVector vel= player.getVelocity();
        vel.setRow(0.05f);

        //check collision with null tile (no offset- cannot walk on next row at all)
        HexVector nextPos= player.getPosition().add(vel); //row 25.05

    }

    @Test
    public void testMove() {
        HexVector vel = new HexVector(1, 0);
        testPlayer.move(vel);
        assertEquals(1, testPlayer.getPosition().getCol(), 0.001);

        PlayerPeon mockPlayer = mock(PlayerPeon.class);
        VehicleEntity mockVehicle = mock(VehicleEntity.class);
        doCallRealMethod().when(mockPlayer).move(any(HexVector.class));
        doReturn(mockVehicle).when(mockPlayer).getVehicle();
        doReturn(true).when(mockPlayer).isInVehicle();
        mockPlayer.move(vel);
        verify(mockPlayer, times(2)).getVehicle();
        verify(mockVehicle).move(any(HexVector.class));
    }

    @Test
    public void testSimpleTileCollisionCheck() {
        when(mockedWorld.getTile(0, 0)).thenReturn(null);
        assertTrue(testPlayer.simpleTileCollisionCheck(0, 0));

        Tile testTile = new Tile(null, 0, 0);
        when(mockedWorld.getTile(0, 0)).thenReturn(testTile);
        assertFalse(testPlayer.simpleTileCollisionCheck(0, 0));
    }

    @Test
    public void testOriginalGetPosTile() {
        Tile testTile = new Tile(null, 0, 0);
        PlayerPeon mockPlayer = mock(PlayerPeon.class);

        doCallRealMethod().when(mockPlayer).originalGetPosTile(0, 0);
        doReturn(testTile).when(mockedWorld).getTile(0, 0);
        doReturn(0f).when(mockPlayer).getCol();
        doReturn(0f).when(mockPlayer).getRow();

        assertEquals(testTile, mockPlayer.originalGetPosTile(0, 0));
        verify(mockPlayer, times(1)).originalGetPosTile(0, 0);

        testTile = new Tile(null, 1, 0.5f);

        doReturn(testTile).when(mockedWorld).getTile(1, 0.5f);
        doReturn(1f).when(mockPlayer).getCol();
        doReturn(0.5f).when(mockPlayer).getRow();

        assertEquals(testTile, mockPlayer.originalGetPosTile(0, 0));
        verify(mockPlayer, times(2)).originalGetPosTile(0, 0);
    }

    @Test
    public void playerInventory() {
        PlayerPeon peon = new PlayerPeon(1,1,1, textureAtlas);
        assertEquals(3, peon.getInventory().stackCount());
    }

    @Test
    public void testGetHealth() {
        assertEquals(150, testPlayer.getHealth());
    }

    @Test
    public void testIsAlive() {
        assertTrue(testPlayer.isAlive());
    }

    @Test
    public void testDeath() {
        testPlayer.death();
        assertEquals(0, testPlayer.getHealth());
        assertFalse(testPlayer.isAlive());
    }

    @Test
    public void testGainFullHealth() {
        testPlayer.gainFullHealth();
        assertEquals(testPlayer.getHealth(), testPlayer.getMaxPlayerHealth());
    }

    @Test
    public void testLoseHealth() {
        testPlayer.loseHealth(10);
        assertEquals(140, testPlayer.getHealth());
        testPlayer.loseHealth(999);
        assertEquals(0, testPlayer.getHealth());
        assertFalse(testPlayer.isAlive());
    }

    @Test
    public void testGetReviveCounter() {
        assertEquals(0, testPlayer.getReviveCounter(), 0.1f);
    }

}