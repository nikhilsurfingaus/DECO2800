package deco.combatevolved.entities.dynamicentities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {

    private float x;

    private float y;

    private float stateTime;

    private static Animation<TextureRegion> animation = null;

    private boolean remove = false;

    public Explosion(float x, float y) {
        this.x = x - 150;
        this.y = y - 150;

        this.stateTime = 0;

        if (this.getAnimation() == null) {
            animation = new Animation<>(0.3f, TextureRegion.split(new Texture("resources/explosion.png"), 400, 400)[0]);
        }
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void checkAnimation(float stateTime) {
        if (animation.isAnimationFinished(stateTime)) {
            remove = true;
        }
    }

    public void update(float deltaTime) {
        this.setStateTime(this.getStateTime() + deltaTime);
        checkAnimation(this.getStateTime());
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime), this.getX(), this.getY());
    }

    /**
     * Whether the explosion is to be removed.
     * @return  true : Explosion to be removed.
     *          false : Explosion not to be removed.
     */
    public boolean getRemove() {
        return this.remove;
    }
}
