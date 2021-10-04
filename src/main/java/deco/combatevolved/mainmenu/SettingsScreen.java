package deco.combatevolved.mainmenu;

import com.badlogic.gdx.*;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import com.esotericsoftware.minlog.Log;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.managers.*;

import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.util.Keybindings;
import org.lwjgl.Sys;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;


public class SettingsScreen implements Screen {

    final CombatEvolvedGame game;
    private Stage stage;
    private Skin skin;

    private int mode;
    private float displacement = 0;

    private Image background;
    private Image backdrop;

    private ImageButton backButton;
    private ImageButton onButton;
    private ImageButton saveButton;
    private ImageButton offButton;
    private ImageButton keybindingsButton;
    private ImageButton exitButton;
    private ImageButton goButton;

    private Drawable offDrawable;
    private Drawable onButtonDrawable;

    private Label title;
    private Label musicLevel;
    private Label sfxLevel;
    private Label fullScreen;
    private Label newPass;
    private Label keybindings;
    private Label mLeft;
    private Label mRight;
    private Label mUp;
    private Label mDown;

    private TextField newPassField;
    private TextField leftKey;
    private TextField rightKey;
    private TextField upKey;
    private TextField downKey;

    private Table tb;
    private Table tb2;

    private Group settings;

    private boolean bindingShowStatus;
    private boolean isHost = false;

    private Keybindings playersKeys;

    /**
     * The constructor of the Settings Screen
     *
     * @param game the Iguana Chase Game to run
     * @param mode the game mode settings is being loaded from: 0 = main menu
     *             1 = multiplayer host
     *             2 = multiplayer guest
     */
    public SettingsScreen(final CombatEvolvedGame game, int mode, Stage stage) {

        this.game = game;
        this.bindingShowStatus = false;
        this.mode = mode;

        this.stage = stage;
        skin = GameManager.get().getSkin();
        settings = new Group();

        //initialize background music class
        //note: this may not be the correct track, will need to discuss with
        // sound team.
        Music mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Background " +
                        "Music/BGM_SafeSituation_01_Inactive.mp3"));
        //set to loop endlessly and play.
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        mainMenuMusic.play();

        //initialize background image, fill screen and add to stage
        background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("settingsBackground"));
        background.setWidth(stage.getViewport().getScreenWidth());

        if (mode == 0) {
            stage.addActor(background);
        }

        backdrop =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("settingsBackdrop"));
        backdrop.setWidth(700);
        backdrop.setHeight(800);
        backdrop.setPosition(stage.getWidth()/2 - backdrop.getWidth()/2,
                stage.getHeight()/2 - backdrop.getHeight()/2);
        if (this.mode != 0) {
            settings.addActor(backdrop);
            isHost =
                    GameManager.get().getManager(NetworkManager.class).isHost();
        }

        //initialize title
        title = new Label("SETTINGS", skin, "label-font", "white");
        title.setFontScale(3);
        title.setPosition(stage.getWidth()/2 - (title.getWidth()*3)/2,
                stage.getHeight()/2 + 300);
        settings.addActor(title);

        //initialize all labels
        musicLevel = new Label("MUSIC VOLUME", skin);
        sfxLevel = new Label("SOUND EFFECTS", skin);
        fullScreen = new Label("FULL SCREEN", skin);
        newPass = new Label ("NEW SERVER PASSWORD", skin);
        keybindings = new Label("PLAYER CONTROLS", skin);
        mLeft = new Label("MOVE LEFT", skin);
        mRight = new Label("MOVE RIGHT", skin);
        mUp = new Label("MOVE UP", skin);
        mDown = new Label("MOVE DOWN", skin);

        newPassField = new TextField("", skin);
        newPassField.setPasswordMode(true);
        newPassField.setPasswordCharacter('*');

        if (mode != 0) {
            playersKeys = GameManager.get().getKeybindings();
            leftKey = new TextField(Input.Keys.toString(this.playersKeys.getKey(
                    "MoveLeft")), skin);
            rightKey = new TextField(Input.Keys.toString(this.playersKeys.getKey(
                    "MoveRight")), skin);
            upKey = new TextField(Input.Keys.toString(this.playersKeys.getKey(
                    "MoveUp")), skin);
            downKey = new TextField(Input.Keys.toString(this.playersKeys.getKey(
                    "MoveDown")), skin);
        } else {
            leftKey = new TextField("", skin);
            rightKey = new TextField("", skin);
            upKey = new TextField("", skin);
            downKey = new TextField("", skin);
        }
        leftKey.setMaxLength(1);
        rightKey.setMaxLength(1);
        upKey.setMaxLength(1);
        downKey.setMaxLength(1);

        //initalize sliders with inital values determined by the current
        // game's sound level (from SoundManager)
        Slider musicVolume = new Slider(0, 1, 0.05f, false, skin);
        musicVolume.setHeight(musicLevel.getHeight());
        musicVolume.setValue(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        tb = new Table();
        tb.add(musicVolume).width(300).height(musicVolume.getHeight());
        tb.setPosition(stage.getWidth()/2 + 200,
                stage.getHeight()/2 + 220);
        settings.addActor(tb);

        Slider sfxVolume = new Slider(0, 1, 0.05f, false, skin);
        sfxVolume.getStyle().knob.setMinHeight(40);
        sfxVolume.getStyle().knob.setMinWidth(20);
        sfxVolume.getStyle().background.setMinHeight(15);
        sfxVolume.setHeight(musicLevel.getHeight());
        sfxVolume.setValue(GameManager.get().getManager(SoundManager.class).getSfxVolume());
        tb2 = new Table();
        tb2.add(sfxVolume).width(300).height(sfxVolume.getHeight());
        tb2.setPosition(stage.getWidth()/2 + 200,
                stage.getHeight()/2 + 150);
        settings.addActor(tb2);

        if (mode == 1 && isHost) {
            displacement = 50;
        }
        //position labels
        musicLevel.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 + 200);
        sfxLevel.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 + 130);
        fullScreen.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 + 60);

        newPass.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2);
        newPassField.setPosition(stage.getWidth()/2 + 75,
                stage.getHeight()/2);

        keybindings.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 - displacement);
        mLeft.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 50 - displacement);
        mRight.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 100 - displacement);
        mUp.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 150 - displacement);
        mDown.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 200 - displacement);
        leftKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 50 - displacement);
        rightKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 100 - displacement);
        upKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 150 - displacement);
        downKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 200 - displacement);


        //add labels to stage
        settings.addActor(musicLevel);
        settings.addActor(sfxLevel);
        settings.addActor(fullScreen);
        if (mode == 1 && isHost) {
            settings.addActor(newPass);
            settings.addActor(newPassField);
        }
        settings.addActor(keybindings);

        //intializing all ImageButtons
        Drawable saveDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("saveButton")));
        saveDrawable.setMinWidth(169 * 1.5f);
        saveDrawable.setMinHeight(45 * 1.5f);
        saveButton = new ImageButton(saveDrawable);
        saveButton.setPosition(stage.getWidth()/2 - saveButton.getWidth() - 100,
                stage.getHeight()/2 - displacement - 300);
        settings.addActor(saveButton);

        Drawable backDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("goBackButton")));
        backButton = new ImageButton(backDrawable);
        backButton.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - displacement - 300);
        backButton.setSize(saveButton.getWidth(),saveButton.getHeight());
        settings.addActor(backButton);

        Drawable keybindingsDropDown =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("keybindingsButton")));
        keybindingsButton = new ImageButton(keybindingsDropDown);
        keybindingsButton.setPosition(stage.getWidth()/2,
                stage.getHeight()/2 - displacement);
        keybindingsButton.setTransform(true);
        settings.addActor(keybindingsButton);

        Drawable goDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("go")));
        goButton = new ImageButton(goDrawable);
        goButton.setWidth(72 * 1.3f);
        goButton.setHeight(37 * 1.3f);
        goButton.setPosition(stage.getWidth()/2 + 250,
                stage.getHeight()/2);
        if (mode == 1 && isHost) {
            settings.addActor(goButton);
        }

        offDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("offButton")));
        onButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("onButton")));
        if (GameManager.get().getManager(ScreenManager.class).getFullscreen()) {
            onButton = new ImageButton(offDrawable);
        } else {
            onButton = new ImageButton(onButtonDrawable);
        }

        onButton.setPosition(stage.getWidth()/2 + 75,
                stage.getHeight()/2 + 55);

        onButton.setWidth(72 * 1.3f);
        onButton.setHeight(37 * 1.3f);
        onButton.setTransform(true);
        settings.addActor(onButton);

        if (mode == 0) {
            stage.addActor(settings);
        }


        //BUTTON LISTENERS
        keybindings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleShow();
            }
        });

        keybindingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleShow();
            }
        });

        //go button only seen by host
        goButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameManager.get().getManager(NetworkManager.class).setServerPassword(newPassField.getText());
                Log.info("password set to: " + newPassField.getText());
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

        onButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toggleFullScreen();
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable;
                if (GameManager.get().getManager(ScreenManager.class).getFullscreen()) {
                    hoverDrawable =
                            new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("offButtonHover")));
                } else {
                    hoverDrawable =
                            new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("onButtonHover")));
                }
                onButton.getStyle().imageUp = hoverDrawable;
                onButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                Drawable btnDrawable;
                if (GameManager.get().getManager(ScreenManager.class).getFullscreen()) {
                    btnDrawable =
                            new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("offButton")));
                } else {
                    btnDrawable =
                            new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("onButton")));
                }
                onButton.getStyle().imageUp = btnDrawable;
                onButton.getStyle().imageDown = btnDrawable;
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and switch to MainMenuScreen
                if (mode == 0) {
                    game.setScreen(new MainMenuScreen(game));
                    mainMenuMusic.dispose();
                } else {
                    getSettingsGroup().remove();
                    GameManager.get().getManager(ScreenManager.class).setSettingsOpen(false);
                }
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("goBackButtonHover")));
                backButton.getStyle().imageUp = hoverDrawable;
                backButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                backButton.getStyle().imageUp = backDrawable;
                backButton.getStyle().imageDown = backDrawable;
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (mode != 0) {
                    if (!leftKey.getText().isEmpty() && leftKey.getText().matches("[a-zA-Z]+")) {
                        playersKeys.setKey("MoveLeft",
                                Input.Keys.valueOf(leftKey.getText().toUpperCase()));
                    } else {
                        Log.info("invalid character\n");
                    }
                    if (!rightKey.getText().isEmpty() && rightKey.getText().matches("[a-zA-Z]+")) {
                        playersKeys.setKey("MoveRight",
                                Input.Keys.valueOf(rightKey.getText().toUpperCase()));
                    } else {
                        Log.info("invalid character\n");
                    }
                    if (!upKey.getText().isEmpty() && upKey.getText().matches("[a-zA-Z]+")) {
                        playersKeys.setKey("MoveUp",
                                Input.Keys.valueOf(upKey.getText().toUpperCase()));
                    } else {
                        Log.info("invalid character\n");
                    }
                    if (!downKey.getText().isEmpty() && downKey.getText().matches("[a-zA-Z]+")) {
                        playersKeys.setKey("MoveDown",
                                Input.Keys.valueOf(downKey.getText().toUpperCase()));
                    } else {
                        Log.info("invalid character\n");
                    }

                    if (isHost) {
                        GameManager.get().getManager(NetworkManager.class).setServerPassword(newPassField.getText());
                        Log.info("password set to: " + newPassField.getText());
                    }
                } else {
                    Log.info("Keybindings only tweakable in game\n");
                }
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("saveButtonHover")));
                hoverDrawable.setMinWidth(169 * 1.5f);
                hoverDrawable.setMinHeight(45 * 1.5f);
                saveButton.getStyle().imageUp = hoverDrawable;
                saveButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                saveButton.getStyle().imageUp = saveDrawable;
                saveButton.getStyle().imageDown = saveDrawable;
            }
        });

        musicVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float Volume = musicVolume.getValue();
                //System.out.println(Volume);
                mainMenuMusic.setVolume(Volume);
                GameManager.get().getManager(SoundManager.class).setMusicVolume(Volume);
            }
        });

        sfxVolume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float Volume = sfxVolume.getValue();
                //System.out.println(Volume);
                GameManager.get().getManager(SoundManager.class).setSfxVolume(Volume);
            }
        });

    }

    public Group getSettingsGroup() {
        GameManager.get().getManager(ScreenManager.class).setSettingsOpen(true);
        return this.settings;
    }

    /** this method is created to toggle whether the keybindings settings are
     * to be displayed or not.
     */
    private void toggleShow() {
        if (!bindingShowStatus) {
            //reset condition
            bindingShowStatus = true;
            //add actors to stage
            settings.addActor(mLeft);
            settings.addActor(mRight);
            settings.addActor(mDown);
            settings.addActor(mUp);
            settings.addActor(leftKey);
            settings.addActor(rightKey);
            settings.addActor(downKey);
            settings.addActor(upKey);
            //rotate drop down icon/button
            keybindingsButton.setRotation(270);
            keybindingsButton.setPosition(keybindingsButton.getX(),
                    keybindingsButton.getY() + 20);
        }
        else {
            bindingShowStatus = false;
            mLeft.remove();
            mRight.remove();
            mDown.remove();
            mUp.remove();
            leftKey.remove();
            rightKey.remove();
            downKey.remove();
            upKey.remove();
            keybindingsButton.setRotation(0);
            keybindingsButton.setPosition(keybindingsButton.getX(),
                    keybindingsButton.getY() - 20);
        }
    }

    /** this method is created to toggle Fullscreen mode */
    private void toggleFullScreen() {
        //if already fullscreen according to ScreenManager
        if (GameManager.get().getManager(ScreenManager.class).getFullscreen()) {
            //toggle ScreenManger boolean for fullscreen status
            GameManager.get().getManager(ScreenManager.class).setFullscreen(false);
            //toggle button to display On
            onButton.getStyle().imageUp = onButtonDrawable;
            onButton.getStyle().imageDown = onButtonDrawable;
            //set to windowed mode
            Gdx.graphics.setWindowedMode(1280, 720);
        }
        else {
            GameManager.get().getManager(ScreenManager.class).setFullscreen(true);
            //toggle button to display Off
            onButton.getStyle().imageUp = offDrawable;
            onButton.getStyle().imageDown = offDrawable;
            //determine the monitor size and set to fullscreen with that
            // display mode
            Graphics.Monitor currentMonitor = Gdx.graphics.getMonitor();
            Graphics.DisplayMode displayMode =
                    Gdx.graphics.getDisplayMode(currentMonitor);
            Gdx.graphics.setFullscreenMode(displayMode);

        }
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

        //repositioning elements

        repositionElements(stage);

        if (((float) width/height) > (5334f/3000)) {
            background.setWidth(stage.getWidth());
            background.setHeight(stage.getWidth() * (3000f/5334));
        } else {
            background.setHeight(stage.getHeight());
            background.setWidth(stage.getHeight() * (5334f/3000));
        }
    }

    public void repositionElements(Stage stage) {
        title.setPosition(stage.getWidth()/2 - (title.getWidth()*3)/2,
                stage.getHeight()/2 + 300);

        saveButton.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - displacement - 300);
        backButton.setPosition(stage.getWidth()/2 - backButton.getWidth() - 100,
                stage.getHeight()/2 - displacement - 300);
        keybindingsButton.setPosition(stage.getWidth()/2,
                stage.getHeight()/2 - displacement);
        onButton.setPosition(stage.getWidth()/2 + 75,
                stage.getHeight()/2 + 55);

        musicLevel.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 + 200);
        tb.setPosition(stage.getWidth()/2 + 200,
                stage.getHeight()/2 + 220);
        sfxLevel.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 + 130);
        tb2.setPosition(stage.getWidth()/2 + 200,
                stage.getHeight()/2 + 150);
        fullScreen.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 + 60);

        newPass.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2);
        goButton.setPosition(stage.getWidth()/2 + 250,
                stage.getHeight()/2);

        keybindings.setPosition(stage.getWidth()/2 - 300,
                stage.getHeight()/2 - displacement);
        mLeft.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 50 - displacement);
        mRight.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 100 - displacement);
        mUp.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 150 - displacement);
        mDown.setPosition(stage.getWidth()/2 - 250,
                stage.getHeight()/2 - 200 - displacement);
        leftKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 50 - displacement);
        rightKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 100 - displacement);
        upKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 150 - displacement);
        downKey.setPosition(stage.getWidth()/2 + 100,
                stage.getHeight()/2 - 200 - displacement);
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