package deco.combatevolved.managers;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import deco.combatevolved.BaseGDXTest;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.renderers.PotateCamera;
import deco.combatevolved.worlds.CombatEvolvedWorld;
import org.junit.*;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class CameraManagerTest {

    private class DummyEntity extends AbstractEntity {

        private DummyEntity(float col, float row, int renderOrder) {
            super(col, row, renderOrder, 1f, 1f);
        }

        @Override
        public void onTick(long i) {

        }
    }

    private final float BASE_CAMERA_SPEED = 5.0f;
    private int screenWidth = 1000;
    private int screenHeight = 1000;

    private PotateCamera camera;
    private CameraManager cameraManager;
    private AbstractEntity player;
    private static Application game;

    private String textureAtlas = "testFrames";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        game = new HeadlessApplication(
                new ApplicationListener() {
                    @Override public void create() {}
                    @Override public void resize(int width, int height) {}
                    @Override public void render() {}
                    @Override public void pause() {}
                    @Override public void resume() {}
                    @Override public void dispose() {}
                });

        Gdx.gl20 = Mockito.mock(GL20.class);
        Gdx.gl = Gdx.gl20;
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        game.exit();
        game = null;
    }

    @Before
    public void setup() {
        camera = new PotateCamera(100, 100);
//        player = new PlayerPeon(0, 0, 1, "resources/PlayerCharacter/test/frames.atlas");
        player = new DummyEntity(0, 0, 1);
        player.setPosition(0, 0, 0);
        cameraManager = new CameraManager(player, camera);
        cameraManager.setScreenWidth(screenWidth);
        cameraManager.setScreenHeight(screenHeight);
    }

    @Test
    public void moveCameraTest() {
        int BOUNDARY = 50;
        float ZOOM_LEVEL = 2.0f;

        //create player for CameraManager to refer to for the keybindings
        GameManager gm = GameManager.get();
        CombatEvolvedWorld world = new CombatEvolvedWorld();
        PlayerPeon otherPeon= new PlayerPeon(0f, 0f, 0.05f, textureAtlas);
        gm.setWorld(world);
        gm.getWorld().addEntity(otherPeon);
        cameraManager.setPlayerControls(otherPeon);

        // if mouseX < boundary, x = -base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(BOUNDARY - 1, 0);
        cameraManager.moveCamera(camera);
        assertEquals(-BASE_CAMERA_SPEED, camera.position.x, 0.1);

        // if mouseX > screenWidth - BOUNDARY, x = base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(screenWidth - BOUNDARY + 1, 0);
        cameraManager.moveCamera(camera);
        assertEquals(BASE_CAMERA_SPEED, camera.position.x, 0.1);

        // mouseY < BOUNDARY, y = base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(0, BOUNDARY - 1);
        cameraManager.moveCamera(camera);
        assertEquals(BASE_CAMERA_SPEED, camera.position.y, 0.1);

        // mouseY > screenHeight - BOUNDARY, y = -base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(0, screenHeight - BOUNDARY + 1);
        cameraManager.moveCamera(camera);
        assertEquals(-BASE_CAMERA_SPEED, camera.position.y, 0.1);

        // if mouseX < boundary, x = -base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(BOUNDARY - 1, 0);
        cameraManager.moveCamera(camera);
        assertEquals(-BASE_CAMERA_SPEED, camera.position.x, 0.1);

        // test camera moving at different zoom level
        camera.zoom = ZOOM_LEVEL;

        // if mouseX < boundary, x = -base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(BOUNDARY - 1, 0);
        cameraManager.moveCamera(camera);
        assertEquals(-BASE_CAMERA_SPEED * ZOOM_LEVEL, camera.position.x, 0.1);

        // if mouseX > screenWidth - BOUNDARY, x = base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(screenWidth - BOUNDARY + 1, 0);
        cameraManager.moveCamera(camera);
        assertEquals(BASE_CAMERA_SPEED * ZOOM_LEVEL, camera.position.x, 0.1);

        // mouseY < BOUNDARY, y = base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(0, BOUNDARY - 1);
        cameraManager.moveCamera(camera);
        assertEquals(BASE_CAMERA_SPEED * ZOOM_LEVEL, camera.position.y, 0.1);

        // mouseY > screenHeight - BOUNDARY, y = -base_camera_speed
        camera.position.set(0, 0, 0);
        cameraManager.setMouseXY(0, screenHeight - BOUNDARY + 1);
        cameraManager.moveCamera(camera);
        assertEquals(-BASE_CAMERA_SPEED * ZOOM_LEVEL, camera.position.y, 0.1);
    }

    @Test
    public void lockCameraTest() {
        camera.position.set(0, 0, 0);
        cameraManager.lockCamera(player, camera);
        assertEquals(0, 0, 0.1);

        camera.position.set(BASE_CAMERA_SPEED, 0, 0);
        cameraManager.lockCamera(player, camera);
        assertEquals(0, camera.position.x, 0.1);

        camera.position.set(0, BASE_CAMERA_SPEED, 0);
        cameraManager.lockCamera(player, camera);
        assertEquals(0, camera.position.y, 0.1);

        camera.position.set(0, 0, BASE_CAMERA_SPEED);
        cameraManager.lockCamera(player, camera);
        assertEquals(0, camera.position.z, 0.1);

        camera.position.set(-BASE_CAMERA_SPEED, 0, 0);
        cameraManager.lockCamera(player, camera);
        assertEquals(0, camera.position.x, 0.1);

        camera.position.set(0, -BASE_CAMERA_SPEED, 0);
        cameraManager.lockCamera(player, camera);
        assertEquals(0, camera.position.y, 0.1);

        camera.position.set(0, 0, -BASE_CAMERA_SPEED);
        cameraManager.lockCamera(player, camera);
        assertEquals(0, camera.position.z, 0.1);
    }

    @Test
    public void setCameraLockedTest() {
        assertEquals(cameraManager.isCameraLocked(), true);
        cameraManager.setCameraLocked();
        assertEquals(cameraManager.isCameraLocked(), false);
    }
}