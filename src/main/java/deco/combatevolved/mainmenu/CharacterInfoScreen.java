package deco.combatevolved.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.managers.TextureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * Screen containing info and stats on the playable characters
 */
public class CharacterInfoScreen implements Screen {
    final CombatEvolvedGame game;
    private static final Logger logger = LoggerFactory.getLogger(CharacterInfoScreen.class);
    private Stage stage;
    private Skin skin;

    private static final String ENGINEER = "Engineer";
    private static final String SOLDIER = "Soldier";
    private static final String EXPLORER = "Explorer";

    // Buttons
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton exitButton;

    // Character info pages
    private Image engineerStats;
    private Image explorerStats;
    private Image soldierStats;

    // Background to contain current character page
    // Order of characters - Engineer <--> Explorer <--> Soldier
    private Image background;
    private String current = ENGINEER;

    /***
     * Constructor for Character Info Screen
     * Sets up the buttons, backgrounds, button listeners and adds them to
     * the stage to be rendered on the screen
     * @param game - game to run
     * @param hostname - hostname
     * @param username - username of player
     * @param password - password of player
     * @param gameType - game type
     */
    public CharacterInfoScreen(final CombatEvolvedGame game, String hostname, String username, String password,
                               int gameType) {
        this.game = game;
        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        skin = GameManager.get().getSkin();

        // initialize background music class
        Music mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Background " +
                        "Music/BGM_BattlePlay_02_Inactive.mp3"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        mainMenuMusic.play();

        // Set images
        engineerStats =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("engineerStats"));
        explorerStats =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("explorerStats"));
        soldierStats =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("soldierStats"));

        // Set initial character screen
        background = engineerStats;
        current = ENGINEER;
        background.setFillParent(true);
        stage.addActor(background);

        // Buttons
        // Exit Button
        Drawable exitDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("exitCross")));
        exitButton = new ImageButton(exitDrawable);
        exitButton.setPosition(stage.getWidth() - exitButton.getWidth() - 50,
                stage.getHeight() - exitButton.getHeight() - 50);
        //exitButton.setPosition(20, 20);
        stage.addActor(exitButton);

        // Left Button
        Drawable leftDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("leftButton")));
        leftButton = new ImageButton(leftDrawable);
        leftButton.setPosition(0,
                (stage.getHeight() - leftButton.getHeight())/2);
        stage.addActor(leftButton);

        // Right Button
        Drawable rightDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("rightButton")));
        rightButton = new ImageButton(rightDrawable);
        rightButton.setPosition(stage.getWidth() - rightButton.getWidth(),
                (stage.getHeight() - rightButton.getHeight())/2);
        stage.addActor(rightButton);

        // Button listeners
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // dispose music
                mainMenuMusic.dispose();
                game.setScreen(new CharacterSelectScreen(game, hostname,
                        username, password, gameType));
            }
        });

        leftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Clear stage
                stage.clear();
                // Change background
                if (current == ENGINEER) {
                    // Change to soldier
                    background = soldierStats;
                    background.setFillParent(true);
                    stage.addActor(background);
                    current = SOLDIER;
                } else if (current == EXPLORER) {
                    // Change to Engineer
                    background = engineerStats;
                    background.setFillParent(true);
                    stage.addActor(background);
                    current = ENGINEER;
                } else if (current == SOLDIER) {
                    // Change to Explorer
                    background = explorerStats;
                    background.setFillParent(true);
                    stage.addActor(background);
                    current = EXPLORER;
                }
                // Re-add buttons
                stage.addActor(exitButton);
                stage.addActor(leftButton);
                stage.addActor(rightButton);
            }
        });

        rightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Clear stage
                stage.clear();
                // Change background
                if (current == ENGINEER) {
                    // Change to soldier
                    background = explorerStats;
                    background.setFillParent(true);
                    stage.addActor(background);
                    current = EXPLORER;
                } else if (current == EXPLORER) {
                    // Change to Engineer
                    background = soldierStats;
                    background.setFillParent(true);
                    stage.addActor(background);
                    current = SOLDIER;
                } else if (current == SOLDIER) {
                    // Change to Explorer
                    background = engineerStats;
                    background.setFillParent(true);
                    stage.addActor(background);
                    current = ENGINEER;
                }
                // Re-add buttons
                stage.addActor(exitButton);
                stage.addActor(leftButton);
                stage.addActor(rightButton);
            }
        });

    }

    /**
     * Begins things that need to begin when shown
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the screen
     * @param delta
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Resizes the stage to a new width and height
     *
     * @param width  the new width for the character select stage
     * @param height the new width for the character stage
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // exit button
        exitButton.setPosition(stage.getWidth() - exitButton.getWidth() - 50,
                stage.getHeight() - exitButton.getHeight() - 50);
        // left button
        leftButton.setPosition(0,
                (stage.getHeight() - leftButton.getHeight())/2);
        // right button
        rightButton.setPosition(stage.getWidth() - rightButton.getWidth(),
                (stage.getHeight() - rightButton.getHeight())/2);
    }

    /**
     * Pauses the screen
     */
    @Override
    public void pause() {
        // Unused
    }

    /**
     * Resumes the screen
     */
    @Override
    public void resume() {
        // Unused
    }

    /**
     * Hides the screen
     */
    @Override
    public void hide() {
        // Unused
    }

    /**
     * Disposes of the stage that the screen is on
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
