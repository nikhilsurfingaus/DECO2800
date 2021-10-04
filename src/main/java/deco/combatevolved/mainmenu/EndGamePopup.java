package deco.combatevolved.mainmenu;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;

public class EndGamePopup implements Screen {
    // Setup a logger for the screen
    private static final Logger logger = LoggerFactory.getLogger(EndGameScreen.class);

    private Stage stage;

    private ImageButton callProfButton;

    private Image winText;

    /**
     * The constructor of the EndGamePopup
     *
     * @param
     */
    public EndGamePopup(final CombatEvolvedGame game, Skin skin, Logger logger, Stage stage, String username) {
        this.stage = stage;

        //initialize background image, fill screen and add to stage
        Drawable winTextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("winText")));
        winTextDrawable.setMinHeight(416);
        winTextDrawable.setMinWidth(469);
        winText = new Image(winTextDrawable);
        winText.setPosition(stage.getWidth()/2 - winText.getWidth()/2,
                (float) ((double)stage.getHeight() - winText.getHeight() * 1.2));

        //Initialises buttons and text images
        Drawable callProfButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("callProfButton")));
        callProfButtonDrawable.setMinHeight(95);
        callProfButtonDrawable.setMinWidth(376);
        callProfButton = new ImageButton(callProfButtonDrawable);
        callProfButton.setPosition(stage.getWidth()/2 - callProfButton.getWidth()/2,
                (float) ((double) stage.getHeight() - winText.getHeight() * 1.25 - callProfButton.getHeight()));

        ImageButton.ImageButtonStyle callProfButtonStyle = callProfButton.getStyle();

        //Listener for the skip button, skips the backstory and starts the game when clicked.
        callProfButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Clicked call professor");
                game.setScreen(new EndGameScreen(game, username));
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable callProfButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("callProfButtonHover")));
                callProfButtonStyle.imageUp = callProfButtonHoverDrawable;
                callProfButtonStyle.imageDown = callProfButtonHoverDrawable;

            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                callProfButtonStyle.imageUp = callProfButtonDrawable;
                callProfButtonStyle.imageDown = callProfButtonDrawable;
            }
        });
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

        //repositioning
        winText.setPosition(stage.getWidth()/2 - winText.getWidth()/2,
                (float) ((double)stage.getHeight() - winText.getHeight() * 1.2));
        callProfButton.setPosition(stage.getWidth()/2 - callProfButton.getWidth()/2,
                (float) ((double) stage.getHeight() - winText.getHeight() * 1.25 - callProfButton.getHeight()));
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
        logger.info("Popup rendered");
        stage.addActor(winText);
        stage.addActor(callProfButton);
    }

    /**
     * Disposes of the stage that the menu is on
     */
    public void dispose() {
        stage.dispose();
    }
}
