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

import javax.swing.text.Style;

public class WorldSetUpScreen implements Screen {
    // Setup a logger for the main menu
    private static final Logger logger = LoggerFactory.getLogger(BackstoryScreen.class);

    final CombatEvolvedGame game;
    private Stage stage;
    private Skin skin;

    private TextButton singlePlayerButton;
    private TextButton multiPlayerButton;

    private Label usernameLabel;
    private Label serverLabel;
    private Label passwordLabel;

    private TextField inputUsernameField;
    private TextField inputIPAddressField;
    private TextField passwordField;

    private ImageButton createWorldButton;
    private ImageButton goBackButton;

    private TextButton.TextButtonStyle pressed;
    private TextButton.TextButtonStyle defaultStyle;

    private Image background;

    // Used to pass into CharacterSelectScreen so that it knows if the game is a host or a client
    // 0 == host, 1 == client
    private int gameType;

    /**
     * The constructor of the MainMenuScreen
     *
     * @param game the Iguana Chase Game to run
     */
    public WorldSetUpScreen(final CombatEvolvedGame game) {
        this.game = game;

        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        skin = GameManager.get().getSkin();

        //initialize background music class
        Music mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Background " +
                        "Music/BGM_StartPage_01_Inactive.mp3"));
        //set to loop endlessly and play.
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        mainMenuMusic.play();

        //initialize background image, fill screen and add to stage
        background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("worldSetUpBackground"));
        stage.addActor(background);

        singlePlayerButton = new TextButton("Single Player", skin);
        singlePlayerButton.setChecked(true);
        singlePlayerButton.setPosition(0,stage.getHeight() - 100);
        singlePlayerButton.setHeight(100);
        singlePlayerButton.setWidth(stage.getWidth()/2);
        stage.addActor(singlePlayerButton);

        multiPlayerButton = new TextButton("CO-OP", skin);
        defaultStyle = multiPlayerButton.getStyle();
        multiPlayerButton.setPosition(stage.getWidth()/2,
                stage.getHeight() - 100);
        multiPlayerButton.setHeight(100);
        multiPlayerButton.setWidth(stage.getWidth()/2);
        stage.addActor(multiPlayerButton);

        //text's
        usernameLabel = new Label("User Name", skin, "label-font", "white");
        serverLabel = new Label("Server Address", skin, "label-font",
                "white");
        passwordLabel = new Label("Password", skin, "label-font", "white");
        usernameLabel.setPosition(stage.getWidth()/2 - 20 - usernameLabel.getWidth(),
                stage.getHeight()/10 * 6);
        serverLabel.setPosition(stage.getWidth()/2 - 20 - serverLabel.getWidth(),
                stage.getHeight()/10 * 5);
        passwordLabel.setPosition(stage.getWidth()/2 - 20 - passwordLabel.getWidth(),
                stage.getHeight()/10 * 4);
        usernameLabel.setFontScale(1);
        serverLabel.setFontScale(1);
        passwordLabel.setFontScale(1);
        stage.addActor(usernameLabel);
        stage.addActor(serverLabel);
        stage.addActor(passwordLabel);

        // The entry field for the username. Defaults to "".
        inputUsernameField = new TextField("", skin);
        inputUsernameField.setMessageText("Username");
        inputUsernameField.setHeight(60);
        inputUsernameField.setWidth(300);
        inputUsernameField.setPosition(stage.getWidth()/4 * 2,
                stage.getHeight()/10 * 6 - 30);
        stage.addActor(inputUsernameField);

        // The entry field for the IP Address. Defaults to "localhost".
        inputIPAddressField = new TextField("localhost", skin);
        inputIPAddressField.setMessageText("IP Address");
        inputIPAddressField.setHeight(60);
        inputIPAddressField.setWidth(300);
        inputIPAddressField.setPosition(stage.getWidth()/4 * 2,
                stage.getHeight()/10 * 5 - 30);
        inputIPAddressField.setDisabled(true);
        stage.addActor(inputIPAddressField);

        // The entry field for the password. Defaults to "". Hidden with '*'.
        passwordField = new TextField("", skin);
        passwordField.setMessageText("Password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setWidth(300);
        passwordField.setHeight(60);
        passwordField.setPosition(stage.getWidth()/4 * 2,
                stage.getHeight()/10 * 4 - 30);
        stage.addActor(passwordField);

        Drawable goBackDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("goBackButton")));
        goBackDrawable.setMinHeight(95);
        goBackDrawable.setMinWidth(359);
        goBackButton = new ImageButton(goBackDrawable);
        goBackButton.setPosition(stage.getWidth()/4 - goBackButton.getWidth()/2, 50);
        stage.addActor(goBackButton);

        Drawable createWorldDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("createWorldButton")));
        createWorldDrawable.setMinHeight(95);
        createWorldDrawable.setMinWidth(359);
        createWorldButton = new ImageButton(createWorldDrawable);
        createWorldButton.setPosition(stage.getWidth()/4 * 3 - createWorldButton.getWidth()/2
                , 50);
        stage.addActor(createWorldButton);

        //--------------listeners--------------//
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //remove old actors
                inputIPAddressField.setDisabled(true);
                //add new actors
                inputIPAddressField.setText("localhost");
                //set it to being a host
                gameType = 0;
                //style
                singlePlayerButton.setChecked(true);
                multiPlayerButton.setChecked(false);

            }
        });
        multiPlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //remove old actors
                inputIPAddressField.setText("");
                //add new actors
                inputIPAddressField.setDisabled(false);
                //set it to being a client
                gameType = 1;
                //style
                multiPlayerButton.setChecked(true);
                singlePlayerButton.setChecked(false);
            }
        });
        goBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music
                mainMenuMusic.dispose();
                game.setScreen(new MainMenuScreen(game));
            }
            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("goBackButtonHover")));
                goBackButton.getStyle().imageUp = hoverDrawable;
                goBackButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                goBackButton.getStyle().imageUp = goBackDrawable;
                goBackButton.getStyle().imageDown = goBackDrawable;
            }
        });
        createWorldButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music
                mainMenuMusic.dispose();
                game.setScreen(new CharacterSelectScreen(game, inputIPAddressField.getText(),
                        inputUsernameField.getText(), passwordField.getText(), gameType));
            }
            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("createWorldButtonHover")));
                createWorldButton.getStyle().imageUp = hoverDrawable;
                createWorldButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                createWorldButton.getStyle().imageUp = createWorldDrawable;
                createWorldButton.getStyle().imageDown = createWorldDrawable;
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
        singlePlayerButton.setPosition(0,stage.getHeight() - 100);
        singlePlayerButton.setWidth(stage.getWidth()/2);
        multiPlayerButton.setPosition(stage.getWidth()/2,
                stage.getHeight() - 100);
        multiPlayerButton.setWidth(stage.getWidth()/2);

        usernameLabel.setPosition(stage.getWidth()/2 - 20 - usernameLabel.getWidth(),
                stage.getHeight()/10 * 6);
        serverLabel.setPosition(stage.getWidth()/2 - 20 - serverLabel.getWidth(),
                stage.getHeight()/10 * 5);
        passwordLabel.setPosition(stage.getWidth()/2 - 20 - passwordLabel.getWidth(),
                stage.getHeight()/10 * 4);
        inputUsernameField.setPosition(stage.getWidth()/4 * 2,
                stage.getHeight()/10 * 6 - 10);
        inputIPAddressField.setPosition(stage.getWidth()/4 * 2,
                stage.getHeight()/10 * 5 - 10);
        passwordField.setPosition(stage.getWidth()/4 * 2,
                stage.getHeight()/10 * 4 - 10);

        createWorldButton.setPosition(stage.getWidth()/4 * 3 - createWorldButton.getWidth()/2, 50);
        goBackButton.setPosition(stage.getWidth()/4 - goBackButton.getWidth()/2, 50);

        if (((float) width/height) > (5334f/3000)) {
            background.setWidth(stage.getWidth());
            background.setHeight(stage.getWidth() * (3000f/5334));
        } else {
            background.setHeight(stage.getHeight());
            background.setWidth(stage.getHeight() * (5334f/3000));
        }
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
