package deco.combatevolved.handlers;

import com.badlogic.gdx.InputProcessor;

import deco.combatevolved.managers.TickableManager;
import deco.combatevolved.observers.KeyDownObserver;
import deco.combatevolved.observers.KeyTypedObserver;
import deco.combatevolved.observers.KeyUpObserver;

import java.util.ArrayList;
import java.util.List;

public class KeyboardManager extends TickableManager implements InputProcessor {
    List<KeyTypedObserver> keyTypedObserverList = new ArrayList<>();
    List<KeyDownObserver> keyDownObserverList = new ArrayList<>();
    List<KeyUpObserver> keyUpObserverList = new ArrayList<>();

    public void registerForKeyTyped(KeyTypedObserver observer) {
        keyTypedObserverList.add(observer);
    }

    public void registerForKeyDown(KeyDownObserver observer) {
        keyDownObserverList.add(observer);
    }

    public void registerForKeyUp(KeyUpObserver observer) {
        keyUpObserverList.add(observer);
    }

    @Override
    public void onTick(long i) {
        // Do nothing
    }

    @Override
    public boolean keyDown(int keycode) {
        for (KeyDownObserver o : keyDownObserverList) {
            o.notifyKeyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        for (KeyUpObserver o : keyUpObserverList) {
            o.notifyKeyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (KeyTypedObserver o : keyTypedObserverList) {
            o.notifyKeyTyped(character);
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
