package deco.combatevolved.mainmenu;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.managers.TextureManager;
import org.lwjgl.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import deco.combatevolved.CombatEvolvedGame;

public class MainMenuScreen implements Screen {
    // Setup a logger for the main menu
    private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);

    final CombatEvolvedGame game;
    private Stage stage;
    private Skin skin;

    private Image logo;
    private Image icon;
    private Image background;

    private ImageButton settingsButton;
    private ImageButton playButton;
    private ImageButton loadGameButton;
    private ImageButton exitButton;
    private ImageButton helpButton;

    /**
     * The constructor of the MainMenuScreen
     * @param game the Iguana Chase Game to run
     */
    public MainMenuScreen(final CombatEvolvedGame game) {
        this.game = game;

        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        skin = GameManager.get().getSkin();

        //initialize background music class
        //note: this may not be the correct track, will need to discuss with
        // sound team.
        Music mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Background " +
                        "Music/BGM_StartPage_01_Inactive.mp3"));
        //set to loop endlessly and play.
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        mainMenuMusic.play();

        //initialize background image, fill screen and add to stage
        background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("mainMenuBackground"));
        background.setPosition(0,0);
        background.setWidth(stage.getViewport().getScreenWidth());
        stage.addActor(background);

        //initialize logo, set position & scale, and add to stage
        Drawable logoDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("menuLogo")));
        logo =
                new Image(logoDrawable);
        logo.setPosition(stage.getWidth()/2 - 732/2, stage.getHeight()/10 * 8);
        logo.setWidth(732);
        logo.setHeight(205);
        stage.addActor(logo);

        // add the game icon
        Drawable iconDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gameIcon")));
        icon = new Image(iconDrawable);
        icon.setPosition(10, stage.getHeight() - 110);
        icon.setWidth(120);
        icon.setHeight(100);
        stage.addActor(icon);


        //create drawable class for the button.
        Drawable singlePlayerDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get()
                        .getManager(TextureManager.class)
                        .getTexture("playButton")));
        singlePlayerDrawable.setMinWidth(354);
        //initialize button with drawable class, set position and add to stage
        playButton = new ImageButton(singlePlayerDrawable);
        playButton.setPosition(stage.getWidth()/2 -(354/2),
                stage.getHeight()/10 * 6);
        playButton.setWidth(354);
        stage.addActor(playButton);

        //same process as above ^
        Drawable loadGameDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("loadButton")));
        loadGameButton = new ImageButton(loadGameDrawable);
        loadGameDrawable.setMinWidth(354);
        loadGameButton.setPosition(stage.getWidth()/2 -(354/2),
                stage.getHeight()/10 * 4);
        loadGameButton.setWidth(354);
        stage.addActor(loadGameButton);

        Drawable exitDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("exitButton")));
        exitButton = new ImageButton(exitDrawable);
        exitDrawable.setMinWidth(354);
        exitButton.setPosition(stage.getWidth()/2 -(354/2),
                stage.getHeight()/10 * 2);
        exitButton.setWidth(354);
        stage.addActor(exitButton);

        Drawable helpDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("helpButton")));
        helpButton = new ImageButton(helpDrawable);
        helpButton.setPosition(20,20);
        stage.addActor(helpButton);

        Drawable settingsDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("settingsButton")));
        settingsButton = new ImageButton(settingsDrawable);
        settingsButton.setPosition(stage.getWidth() - settingsButton.getWidth(), 20);
        stage.addActor(settingsButton);


        //listen for a user click.
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //log the exit and exit app with status of 0. (clean exit)
               logger.info("Game exiting");
               Gdx.app.exit();
               System.exit(0);
           }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable exitHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("exitButtonHover")));
                exitButton.getStyle().imageUp = exitHoverDrawable;
                exitButton.getStyle().imageDown = exitHoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                exitButton.getStyle().imageUp = exitDrawable;
                exitButton.getStyle().imageDown = exitDrawable;
            }
        });

        loadGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and change screen to gameScreen with a gameType
                // of 'LOAD_GAME'
                mainMenuMusic.dispose();
                //game.setScreen(new GameScreen(new CombatEvolvedGame(),
                       // GameScreen.gameType.LOAD_GAME,
                       // "localhost",
                       // "temporary",
                        // "temporary"));
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("loadButtonHover")));
                loadGameButton.getStyle().imageUp = hoverDrawable;
                loadGameButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                loadGameButton.getStyle().imageUp = loadGameDrawable;
                loadGameButton.getStyle().imageDown = loadGameDrawable;
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and change screen to the character selection screen
                mainMenuMusic.dispose();
                game.setScreen(new WorldSetUpScreen(game));
            }
            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("playButtonHover")));
                playButton.getStyle().imageUp = hoverDrawable;
                playButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                playButton.getStyle().imageUp = singlePlayerDrawable;
                playButton.getStyle().imageDown = singlePlayerDrawable;
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and change screen to the character selection screen
                mainMenuMusic.dispose();
                game.setScreen(new SettingsScreen(game, 0, stage));
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("settingsButtonHover")));
                settingsButton.getStyle().imageUp = hoverDrawable;
                settingsButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                settingsButton.getStyle().imageUp = settingsDrawable;
                settingsButton.getStyle().imageDown = settingsDrawable;
            }
        });

        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenuMusic.dispose();
                game.setScreen(new HelpScreen(game, stage, 0));
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("helpButtonHover")));
                helpButton.getStyle().imageUp = hoverDrawable;
                helpButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                helpButton.getStyle().imageUp = helpDrawable;
                helpButton.getStyle().imageDown = helpDrawable;
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
     * @param width the new width for the menu stage
     * @param height the new width for the menu stage
     */
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);

        //repositioning
        //background
        if (((float) width/height) > (1265f/713)) {
            background.setWidth(stage.getWidth());
            background.setHeight(stage.getWidth() * (713f/1265));
        } else {
            background.setHeight(stage.getHeight());
            background.setWidth(stage.getHeight() * (1265f/713));
        }


        logo.setPosition(stage.getWidth()/2 - 732/2, stage.getHeight()/10 * 6);

        icon.setPosition(10, stage.getHeight() - 110);

        playButton.setPosition(stage.getWidth()/2 -(354/2),
                stage.getHeight()/20 * 9);
        loadGameButton.setPosition(stage.getWidth()/2 -(354/2),
                stage.getHeight()/20 * 9 - 130);
        exitButton.setPosition(stage.getWidth()/2 -(354/2),
                stage.getHeight()/20 * 9 - 260);
        settingsButton.setPosition(stage.getWidth() - settingsButton.getWidth() - 20, 20);
    }

    /**
     * Renders the menu
     * @param delta
     */
    public void render (float delta) {
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