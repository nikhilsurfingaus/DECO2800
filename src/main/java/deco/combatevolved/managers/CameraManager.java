package deco.combatevolved.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import deco.combatevolved.GameScreen;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.observers.KeyDownObserver;
import deco.combatevolved.observers.ScrollObserver;
import deco.combatevolved.renderers.PotateCamera;
import deco.combatevolved.renderers.Renderer3D;
import deco.combatevolved.util.WorldUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager for camera and camera inputs.
 */
public class CameraManager extends AbstractManager implements ScrollObserver, KeyDownObserver {

    private final float BASE_CAMERA_SPEED = 5.0f;
    private static final int BOUNDARY = 50;

    private int screenWidth;
    private int screenHeight;

    private boolean isCameraLocked;

    private AbstractEntity entity;
    private PlayerPeon player;

    private Logger LOG = LoggerFactory.getLogger(CameraManager.class);

    private PotateCamera camera;

    //Whether the camera can be moved by mouse
    private boolean mouseCameraMove = true;

    private int mouseX;
    private int mouseY;

    public CameraManager() {
    }

    public CameraManager(AbstractEntity entity, PotateCamera camera) {
        this.entity = entity;
        this.camera = camera;
        isCameraLocked = true;
        GameManager.get().getManager(InputManager.class).addScrollListener(this);
        GameManager.get().getManager(InputManager.class).addKeyDownListener(this);

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    /**
     * Assigns button inputs to camera movements.
     */
    public void moveCamera(PotateCamera camera) {
        // time to fix hack.  // fps is not updated cycle by cycle
        float normilisedGameSpeed = (60.0f/ Gdx.graphics.getFramesPerSecond());

        if(this.player==null){
            this.player= (PlayerPeon) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        }

        if (Gdx.input.isKeyPressed(this.player.getControls().getKey("ZoomIn"))) {
            camera.zoom *=1-0.015*normilisedGameSpeed;
            if (camera.zoom < 0.6) {
                camera.zoom = 0.6f;
            }
        }

        if (Gdx.input.isKeyPressed(this.player.getControls().getKey("ZoomOut"))) {
            camera.zoom *=1+0.015*normilisedGameSpeed;
            if (camera.zoom > 5.0) {
                camera.zoom = 5.0f;
            }
        }

        if (mouseX < BOUNDARY && mouseCameraMove) {
            camera.translate(-BASE_CAMERA_SPEED * camera.zoom, 0, 0);
        }
        if (mouseX > screenWidth - BOUNDARY && mouseCameraMove) {
            camera.translate(BASE_CAMERA_SPEED * camera.zoom, 0, 0);
        }
        if (mouseY < BOUNDARY && mouseCameraMove) {
            camera.translate(0, BASE_CAMERA_SPEED * camera.zoom, 0);
        }
        if (mouseY > screenHeight - BOUNDARY && mouseCameraMove) {
            // Mouse not in the player inventory
            if (!playerInvBoundaries().contains(mouseX, mouseY)) {
                camera.translate(0, -BASE_CAMERA_SPEED * camera.zoom, 0);
            }
        }

        if (Gdx.input.isButtonPressed(this.player.getControls().getKey("DragCamera"))) {
            float x = Gdx.input.getDeltaX() * camera.zoom;
            float y = Gdx.input.getDeltaY() * camera.zoom;
            camera.translate(-x, y);
        }

        if (Gdx.input.isKeyPressed(this.player.getControls().getKey("CentreCamera"))) {
            lockCamera(entity, camera);
        }
    }


    /**
     * Locks the camera at the entity's position.
     */
    public void lockCamera(AbstractEntity entity, PotateCamera camera) {
        float[] entityWorldCoordinates = WorldUtil.colRowToWorldCords(entity.getCol(), entity.getRow());
        camera.position.set(entityWorldCoordinates[0], entityWorldCoordinates[1], 0);
    }

    /**
     * Set whether the camera can be moved by mouse
     * @param status True the camera can be moved by mouse
     */
    public void setMouseCameraMove(boolean status) {
        this.mouseCameraMove = status;
    }

    @Override
    public void notifyScrolled(int amount) {
        float normilisedGameSpeed = (60.0f/ Gdx.graphics.getFramesPerSecond());
        if (amount == -1) {
            camera.zoom -= 0.4f;
            if (camera.zoom < 0.6) {
                camera.zoom = 0.6f;
            }
        }
        if (amount == 1) {
            camera.zoom += 0.4f;
            if (camera.zoom > 5.0) {
                camera.zoom = 5.0f;
            }
        }
    }

    @Override
    public void notifyKeyDown(int keycode) {

        if(this.player==null){
            this.player= (PlayerPeon) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        }

        this.player= (PlayerPeon) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());

        if (keycode == this.player.getControls().getKey("LockCamera")) {
            setCameraLocked();
        }
    }

    /**
     * Checks if camera is currently locked.
     *
     * @return true if locked, false otherwise
     */
    public boolean isCameraLocked() {
        return isCameraLocked;
    }

    /**
     * Locks or unlocks the camera, used in event handling
     */
    public void setCameraLocked() {
        isCameraLocked = !isCameraLocked;
        if (isCameraLocked) {
            LOG.info("Camera was locked");
        } else {
            LOG.info("Camera was unlocked");
        }
    }

    /**
     * Sets the mouse X and Y positions
     *
     * @param x value of mouse X
     * @param y value of mouse Y
     */
    public void setMouseXY(int x, int y) {
        mouseX = x;
        mouseY = y;
    }

    /**
     * Creates a rectangle which matches the position and dimensions 
     * of the player inventory at the bottom of the screen
     * 
     * @return Rectangle matching the player inventory on the screen
     */
    private Rectangle playerInvBoundaries() {
    	return new Rectangle(screenWidth / 2 - (4 * 55),
    			screenHeight - 56, 8 * 55, 56);
    }

    public void setPlayerControls(PlayerPeon playerEntity){
        this.player= playerEntity;
    }


}