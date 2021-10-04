package deco.combatevolved.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.esotericsoftware.minlog.Log;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.managers.TextureManager;


public class InGameTutorialPopup implements Screen {

    private Stage stage;

    private Image dialgoue1;
    private Image dialgoue2;
    private Image dialgoue3;
    private Image dialgoue4;

    private ImageButton goButton;
    private ImageButton skip;

    private int progress;

    private float ogWidth;
    private float ogHeight;


    /**
     * The constructor of the InGameTutorialPopup
     *
     * @param
     */
    public InGameTutorialPopup(final CombatEvolvedGame game, Stage stage) {
        progress = 1;
        this.stage = stage;
        ogWidth = stage.getWidth();
        ogHeight = stage.getHeight();

        dialgoue1 =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("tutorial1"));
        dialgoue2 =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("tutorial2"));
        dialgoue3 =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("tutorial3"));
        dialgoue4 =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("tutorial4"));

        dialgoue1.setPosition(stage.getWidth() - dialgoue1.getWidth() - 20,
                stage.getHeight() - dialgoue1.getHeight() - 20);
        dialgoue2.setPosition(stage.getWidth() - dialgoue2.getWidth() - 20,
                stage.getHeight() - dialgoue2.getHeight() - 200);
        dialgoue3.setPosition(stage.getWidth() - dialgoue3.getWidth() - 5,
                stage.getHeight() - dialgoue3.getHeight() - 100);
        dialgoue4.setPosition(50, 200);

        Drawable goDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("go")));
        goButton = new ImageButton(goDrawable);
        goButton.setWidth(72 * 1.3f);
        goButton.setHeight(37 * 1.3f);
        goButton.setPosition(dialgoue4.getX() + dialgoue4.getWidth()/2 - goButton.getWidth()/2,
                dialgoue4.getY() - 70);

        Drawable skipButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("skipButton")));
        skipButtonDrawable.setMinHeight(60);
        skipButtonDrawable.setMinWidth(226);
        skip = new ImageButton(skipButtonDrawable);
        skip.setPosition(stage.getWidth() - skip.getWidth() - 5, 10);

        stage.addActor(skip);
        stage.addActor(dialgoue1);

        goButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                progress = 5;
                closeAll();
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("goHover")));
                goButton.getStyle().imageUp = hoverDrawable;
                goButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                goButton.getStyle().imageUp = goDrawable;
                goButton.getStyle().imageDown = goDrawable;
            }
        });

        skip.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                progress = 5;
                closeAll();
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("skipButtonHover")));
                skip.getStyle().imageUp = hoverDrawable;
                skip.getStyle().imageDown = hoverDrawable;

                //selectedSound.play();

            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                skip.getStyle().imageUp = skipButtonDrawable;
                skip.getStyle().imageDown = skipButtonDrawable;
            }
        });

    }

    public boolean show2() {
        if (progress == 1) {
            dialgoue1.remove();
            stage.addActor(dialgoue2);
            progress = 2;
            return true;
        } else {
            return false;
        }
    }
    public boolean show3() {
        if (progress == 2) {
            dialgoue2.remove();
            stage.addActor(dialgoue3);
            progress = 3;
            return true;
        } else {
            return false;
        }
    }
    public boolean show4() {
        if (progress == 3) {
            dialgoue3.remove();
            stage.addActor(dialgoue4);
            stage.addActor(goButton);
            progress = 4;
            return true;
        } else {
            return false;
        }
    }
    public void closeAll() {
        dialgoue1.remove();
        dialgoue2.remove();
        dialgoue3.remove();
        dialgoue4.remove();
        goButton.remove();
        skip.remove();
    }

    /**
     * Begins things that need to begin when shown
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Pauses the screen
     */
    public void pause() {
        //do nothing
    }

    /**
     * Resumes the screen
     */
    public void resume() {
        //do nothing
    }

    /**
     * Hides the screen
     */
    public void hide() {
        //do nothing
    }

    /**
     * Resizes the main menu stage to a new width and height
     *
     * @param width  the new width for the menu stage
     * @param height the new width for the menu stage
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        repositionElements(this.stage);
    }

    public void repositionElements(Stage stage) {
        //repositioning

        dialgoue1.setPosition(stage.getWidth() - dialgoue1.getWidth() - 20 - (stage.getWidth() - ogWidth)/2,
                stage.getHeight() - dialgoue1.getHeight() - 20 - (stage.getHeight() - ogHeight)/2);
        dialgoue2.setPosition(stage.getWidth() - dialgoue2.getWidth() - 20 - (stage.getWidth() - ogWidth)/2,
                stage.getHeight() - dialgoue2.getHeight() - 200 - (stage.getHeight() - ogHeight)/2);
        dialgoue3.setPosition(stage.getWidth() - dialgoue3.getWidth() - 5 - (stage.getWidth() - ogWidth)/2,
                stage.getHeight() - dialgoue3.getHeight() - 100 - (stage.getHeight() - ogHeight)/2);
        dialgoue4.setPosition(50 - (stage.getWidth() - ogWidth)/2, 200 - (stage.getHeight() - ogHeight)/2);

        goButton.setPosition(dialgoue4.getX() + dialgoue4.getWidth()/2 - goButton.getWidth()/2,
                dialgoue4.getY() - 70);
        skip.setPosition(stage.getWidth() - skip.getWidth() - 5 - (stage.getWidth() - ogWidth)/2,
                10 - (stage.getHeight() - ogHeight)/2);
    }

    /**
     * Renders the menu
     *
     * @param delta
     */
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Renders the menu
     */
    public void render() {
    }

    /**
     * Disposes of the stage that the menu is on
     */
    public void dispose() {
        stage.dispose();
    }
}
