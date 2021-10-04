package deco.combatevolved.mainmenu;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import deco.combatevolved.managers.SoundManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;

public class ErrorScreen implements Screen {
    // Setup a logger for the main menu
    private static final Logger logger = LoggerFactory.getLogger(ErrorScreen.class);

    final CombatEvolvedGame game;
    private Stage stage;
    private Skin skin;

    private TextButton mainMenuButton;

    private Label titleLabel;
    private Label errorLabel;

    /**
     * The constructor of the ErrorScreen
     *
     * @param game the Iguana Chase Game to run
     * @param title The title for the error message
     * @param errorMsg The error message to display
     */
    public ErrorScreen(final CombatEvolvedGame game, String title, String errorMsg) {
        this.game = game;

        game.create();

        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        skin = GameManager.get().getSkin();

        //initialize background image, fill screen and add to stage
        Image background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("background"));
        background.setFillParent(true);
        stage.addActor(background);

        mainMenuButton = new TextButton("Back to Main Menu", skin);
        mainMenuButton.setPosition(stage.getWidth()/2 - mainMenuButton.getWidth()/2,stage.getHeight()/2 - 200);
        mainMenuButton.setHeight(50);
        mainMenuButton.setWidth(175);
        stage.addActor(mainMenuButton);

        // Labels for error message info
        titleLabel = new Label(title, skin);
        errorLabel = new Label(errorMsg, skin);
        titleLabel.setPosition(stage.getWidth()/2 - titleLabel.getWidth()/2,
                stage.getHeight()/10 * 7);
        errorLabel.setPosition(stage.getWidth()/2 - errorLabel.getWidth()/2,
                stage.getHeight()/10 * 5);
        titleLabel.setAlignment(1);
        titleLabel.setFontScale(3.0f);
        errorLabel.setAlignment(1);
        errorLabel.setFontScale(2.0f);
        stage.addActor(titleLabel);
        stage.addActor(errorLabel);

        //--------------listeners--------------//
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                //insert
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
     * Disposes of the stage that the menu is on
     */
    public void dispose() {
        stage.dispose();
    }
}
