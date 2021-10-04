package deco.combatevolved.handlers;

import deco.combatevolved.managers.AbstractManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.observers.KeyDownObserver;
import deco.combatevolved.observers.KeyUpObserver;
import deco.combatevolved.observers.MouseMovedObserver;
import deco.combatevolved.observers.TouchDownObserver;
import deco.combatevolved.util.WorldUtil;

/**
 * Wrapper around NetworkManager.sendInput that can be added to list of observers
 */
public class InputTransmissionManager extends AbstractManager implements TouchDownObserver, KeyDownObserver,
        KeyUpObserver, MouseMovedObserver {
    @Override
    public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {
        float[] mouse = WorldUtil.screenToWorldCoordinates(screenX, screenY);
        float[] clickedPosition = WorldUtil.worldCoordinatesToColRow(mouse[0], mouse[1]);
        GameManager.get().getManager(NetworkManager.class).sendInput(clickedPosition,
                pointer, button);
    }

    @Override
    public void notifyKeyDown(int keycode) {
        GameManager.get().getManager(NetworkManager.class).sendKeyboardInput(keycode, 0);
    }

    @Override
    public void notifyKeyUp(int keycode) {
        GameManager.get().getManager(NetworkManager.class).sendKeyboardInput(keycode, 1);
    }

    @Override
    public void notifyMouseMoved(int screenX, int screenY) {
        float[] mouse = WorldUtil.screenToWorldCoordinates(screenX, screenY);
        float[] position = WorldUtil.worldCoordinatesToColRowUnrounded(mouse[0], mouse[1]);
        GameManager.get().getManager(NetworkManager.class).sendMouseMovement(position);
    }
}
