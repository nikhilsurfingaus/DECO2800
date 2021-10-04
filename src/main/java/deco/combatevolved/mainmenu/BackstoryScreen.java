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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import deco.combatevolved.managers.SoundManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.GameScreen;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;

public class BackstoryScreen implements Screen {
    // Setup a logger for the screen
    private static final Logger logger = LoggerFactory.getLogger(BackstoryScreen.class);

    final CombatEvolvedGame game;
    private Stage stage;
    private Skin skin;

    private ImageButton callButton;
    private ImageButton skipButton;
    private ImageButton d1Button1;
    private ImageButton d1Button2;
    private ImageButton d2Button;
    private ImageButton d3Button1;
    private ImageButton d3Button2;
    private ImageButton d4Button1;
    private ImageButton d4Button2;
    private ImageButton d5Button;

    private Image d1Text;
    private Image d2Text;
    private Image d3Text;
    private Image d4Text;
    private Image d5Text;

    private Label playerName;

    private int screen;

    /**
     * The constructor of the MainMenuScreen
     *
     * @param game the Iguana Chase Game to run
     */
    public BackstoryScreen(final CombatEvolvedGame game, String hostname, String username, String password,
                           int gameType, String textureAtlas, String classType) {
        this.game = game;
        screen = 0;

        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        skin = GameManager.get().getSkin();

        //initialize background images, fill screen and add to stage
        Image background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("callScreenBackground"));
        background.setFillParent(true);
        stage.addActor(background);

        Image profBackground =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("professorTalkingBackground"));
        profBackground.setFillParent(true);

        Image planetBackground =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("planetCallBackground"));
        planetBackground.setFillParent(true);

        Image towerBackground =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("towerCallBackground"));
        towerBackground.setFillParent(true);

        Image enemyBackground =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("enemyCallBackground"));
        enemyBackground.setFillParent(true);

        //Initialises buttons and text images
        Drawable skipButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("skipButton")));
        skipButtonDrawable.setMinHeight(60);
        skipButtonDrawable.setMinWidth(226);
        skipButton = new ImageButton(skipButtonDrawable);
        skipButton.setPosition(stage.getWidth() - skipButton.getWidth() - 5, 10);
        stage.addActor(skipButton);

        ImageButton.ImageButtonStyle skipButtonStyle = skipButton.getStyle();

        Drawable callButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("callButton")));
        callButtonDrawable.setMinHeight(60);
        callButtonDrawable.setMinWidth(226);
        callButton = new ImageButton(callButtonDrawable);
        callButton.setPosition(stage.getWidth()/205 * 100 - callButton.getWidth()/4,
                stage.getHeight()/22 * 10 - callButton.getHeight()/2);
        stage.addActor(callButton);

        ImageButton.ImageButtonStyle callButtonStyle = callButton.getStyle();

        Drawable d1TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d1Text")));
        d1TextDrawable.setMinHeight(157);
        d1TextDrawable.setMinWidth(1157);
        d1Text = new Image(d1TextDrawable);
        d1Text.setPosition(stage.getWidth()/2 - d1Text.getWidth()/2,
                stage.getHeight() - d1Text.getHeight() * 12/10);

        Drawable d1Button1Drawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d1Button1")));
        d1Button1Drawable.setMinHeight(60);
        d1Button1Drawable.setMinWidth(271);
        d1Button1 = new ImageButton(d1Button1Drawable);
        d1Button1.setPosition(stage.getWidth()/2 + 15,
                stage.getHeight() - d1Text.getHeight() * 13/10 - d1Button1.getHeight());

        ImageButton.ImageButtonStyle d1Button1Style = d1Button1.getStyle();

        Drawable d1Button2Drawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d1Button2")));
        d1Button2Drawable.setMinHeight(60);
        d1Button2Drawable.setMinWidth(271);
        d1Button2 = new ImageButton(d1Button2Drawable);
        d1Button2.setPosition(stage.getWidth()/2 - d1Button2.getWidth() - 15,
                stage.getHeight() - d1Text.getHeight() * 13/10 - d1Button2.getHeight());

        ImageButton.ImageButtonStyle d1Button2Style = d1Button2.getStyle();

        Drawable d2TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d2Text")));
        d2TextDrawable.setMinHeight(157);
        d2TextDrawable.setMinWidth(1157);
        d2Text = new Image(d2TextDrawable);
        d2Text.setPosition(stage.getWidth()/2 - d2Text.getWidth()/2,
                stage.getHeight() - d2Text.getHeight() * 12/10);

        Drawable d2ButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d2Button")));
        d2ButtonDrawable.setMinHeight(59);
        d2ButtonDrawable.setMinWidth(226);
        d2Button = new ImageButton(d2ButtonDrawable);
        d2Button.setPosition(stage.getWidth()/2 - d2Button.getWidth()/2,
                stage.getHeight() - d2Text.getHeight() * 13/10 - d2Button.getHeight());

        ImageButton.ImageButtonStyle d2ButtonStyle = d2Button.getStyle();

        Drawable d3TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d3Text")));
        d3TextDrawable.setMinHeight(157);
        d3TextDrawable.setMinWidth(1157);
        d3Text = new Image(d3TextDrawable);
        d3Text.setPosition(stage.getWidth()/2 - d3Text.getWidth()/2,
                stage.getHeight() - d3Text.getHeight() * 12/10);

        Drawable d3Button1Drawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d3Button1")));
        d3Button1Drawable.setMinHeight(60);
        d3Button1Drawable.setMinWidth(304);
        d3Button1 = new ImageButton(d3Button1Drawable);
        d3Button1.setPosition(stage.getWidth()/2 + 15,
                stage.getHeight() - d3Text.getHeight() * 13/10 - d3Button1.getHeight());

        ImageButton.ImageButtonStyle d3Button1Style = d3Button1.getStyle();

        Drawable d3Button2Drawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d3Button2")));
        d3Button2Drawable.setMinHeight(60);
        d3Button2Drawable.setMinWidth(304);
        d3Button2 = new ImageButton(d3Button2Drawable);
        d3Button2.setPosition(stage.getWidth()/2 - d3Button2.getWidth() - 15,
                stage.getHeight() - d3Text.getHeight() * 13/10 - d3Button2.getHeight());

        ImageButton.ImageButtonStyle d3Button2Style = d3Button2.getStyle();

        Drawable d4TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d4Text")));
        d4TextDrawable.setMinHeight(157);
        d4TextDrawable.setMinWidth(1157);
        d4Text = new Image(d4TextDrawable);
        d4Text.setPosition(stage.getWidth()/2 - d4Text.getWidth()/2,
                stage.getHeight() - d4Text.getHeight() * 12/10);

        Drawable d4Button1Drawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d4Button1")));
        d4Button1Drawable.setMinHeight(59);
        d4Button1Drawable.setMinWidth(263);
        d4Button1 = new ImageButton(d4Button1Drawable);
        d4Button1.setPosition(stage.getWidth()/2 + 30,
                stage.getHeight() - d4Text.getHeight() * 13/10 - d4Button1.getHeight());

        ImageButton.ImageButtonStyle d4Button1Style = d4Button1.getStyle();

        Drawable d4Button2Drawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d4Button2")));
        d4Button2Drawable.setMinHeight(60);
        d4Button2Drawable.setMinWidth(263);
        d4Button2 = new ImageButton(d4Button2Drawable);
        d4Button2.setPosition(stage.getWidth()/2 - d4Button2.getWidth() - 30,
                stage.getHeight() - d4Text.getHeight() * 13/10 - d4Button2.getHeight());

        ImageButton.ImageButtonStyle d4Button2Style = d4Button2.getStyle();

        Drawable d5TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d5Text")));
        d5TextDrawable.setMinHeight(157);
        d5TextDrawable.setMinWidth(1157);
        d5Text = new Image(d5TextDrawable);
        d5Text.setPosition(stage.getWidth()/2 - d5Text.getWidth()/2,
                stage.getHeight() - d5Text.getHeight() * 12/10);

        Drawable d5ButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d5Button")));
        d5ButtonDrawable.setMinHeight(60);
        d5ButtonDrawable.setMinWidth(451);
        d5Button = new ImageButton(d5ButtonDrawable);
        d5Button.setPosition(stage.getWidth()/2 - d5Button.getWidth()/2,
                stage.getHeight() - d5Text.getHeight() * 13/10 - d5Button.getHeight());

        ImageButton.ImageButtonStyle d5ButtonStyle = d5Button.getStyle();

        playerName = new Label("", skin);
        playerName.setFontScale((float) 0.85, (float) 0.85);
        playerName.setHeight(157);
        playerName.setWidth(1157);
        playerName.setAlignment(Align.topLeft);
        playerName.setPosition((float)(stage.getWidth()/2 - playerName.getWidth()/2.9),
                (float)(stage.getHeight() - playerName.getHeight() * 1.33));

        Label.LabelStyle playerNameStyle =  playerName.getStyle();
        BitmapFont playerNameFont =
                new BitmapFont(Gdx.files.internal("resources/fonts/Red Hat Display/RedHatDisplay-Regular.fnt"));
        playerNameStyle.font = playerNameFont;

        //Initialize the BGM sound
        Music storyBGM = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Background " +
                        "Music/BGM_NormalPlay_01_Inactive.wav"));
        storyBGM.setLooping(true);
        storyBGM.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        storyBGM.play();

        //Initialize the phone ringing sound
        Music phoneRinging = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Sound " +
                        "Effect/EFFECT_phoneRinging_01_Inactive.wav"));
        phoneRinging.setLooping(true);
        phoneRinging.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        phoneRinging.play();

        //Initialize the selected sound
        Music selectedSound = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Sound " +
                        "Effect/EFFECT_select_02_Inactive.wav"));
        selectedSound.setLooping(false);
        selectedSound.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());

        //Listener for the skip button, skips the backstory and starts the game when clicked.
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                game.setScreen(new GameScreen(game,
                        GameScreen.gameType.NEW_GAME, hostname, username, password, textureAtlas, classType));
                storyBGM.dispose();
                phoneRinging.dispose();
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable skipButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("skipButtonHover")));
                skipButtonStyle.imageUp = skipButtonHoverDrawable;
                skipButtonStyle.imageDown = skipButtonHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                skipButtonStyle.imageUp = skipButtonDrawable;
                skipButtonStyle.imageDown = skipButtonDrawable;
            }


        });
        //Listener for the pick up call button, loads next backstory slide when clicked and changes size and colour when hovered.
        callButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music
                screen = 1;
                stage.clear();
                stage.addActor(profBackground);
                stage.addActor(skipButton);
                stage.addActor(d1Button1);
                stage.addActor(d1Button2);
                stage.addActor(d1Text);
                playerName.setText(username);
                stage.addActor(playerName);
                phoneRinging.dispose();
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable callButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("callButtonHover")));
                callButtonStyle.imageUp = callButtonHoverDrawable;
                callButtonStyle.imageDown = callButtonHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                callButtonStyle.imageUp = callButtonDrawable;
                callButtonStyle.imageDown = callButtonDrawable;
            }
        });
        //Listener for the introduce button, loads next backstory slide when clicked and changes size and colour when hovered.
        d1Button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                stage.addActor(planetBackground);
                stage.addActor(d2Text);
                stage.addActor(d2Button);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d1Button1HoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d1Button1Hover")));
                d1Button1Style.imageUp = d1Button1HoverDrawable;
                d1Button1Style.imageDown = d1Button1HoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d1Button1Style.imageUp = d1Button1Drawable;
                d1Button1Style.imageDown = d1Button1Drawable;
            }
        });
        //Listener for the introduce button, loads next backstory slide when clicked and changes size and colour when hovered.
        d1Button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                stage.addActor(planetBackground);
                stage.addActor(d2Text);
                stage.addActor(d2Button);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d1Button2HoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d1Button2Hover")));
                d1Button2Style.imageUp = d1Button2HoverDrawable;
                d1Button2Style.imageDown = d1Button2HoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d1Button2Style.imageUp = d1Button2Drawable;
                d1Button2Style.imageDown = d1Button2Drawable;
            }
        });
        //Listener for the continue button, loads next backstory slide when clicked and changes size and colour when hovered.
        d2Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                stage.addActor(towerBackground);
                stage.addActor(d3Text);
                stage.addActor(d3Button1);
                stage.addActor(d3Button2);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d2ButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d2ButtonHover")));
                d2ButtonStyle.imageUp = d2ButtonHoverDrawable;
                d2ButtonStyle.imageDown = d2ButtonHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d2ButtonStyle.imageUp = d2ButtonDrawable;
                d2ButtonStyle.imageDown = d2ButtonDrawable;
            }
        });
        //Listener for the left button, loads next backstory slide when clicked and changes size and colour when hovered.
        d3Button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                stage.addActor(enemyBackground);
                stage.addActor(d4Text);
                stage.addActor(d4Button1);
                stage.addActor(d4Button2);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d3Button1HoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d3Button1Hover")));
                d3Button1Style.imageUp = d3Button1HoverDrawable;
                d3Button1Style.imageDown = d3Button1HoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d3Button1Style.imageUp = d3Button1Drawable;
                d3Button1Style.imageDown = d3Button1Drawable;
            }
        });
        //Listener for the right button, loads next backstory slide when clicked and changes size and colour when hovered.
        d3Button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                stage.addActor(enemyBackground);
                stage.addActor(d4Text);
                stage.addActor(d4Button1);
                stage.addActor(d4Button2);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d3Button2HoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d3Button2Hover")));
                d3Button2Style.imageUp = d3Button2HoverDrawable;
                d3Button2Style.imageDown = d3Button2HoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d3Button2Style.imageUp = d3Button2Drawable;
                d3Button2Style.imageDown = d3Button2Drawable;
            }
        });
        //Listener for the left button, loads next backstory slide when clicked and changes size and colour when hovered.
        d4Button1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen = 5;
                stage.clear();
                stage.addActor(profBackground);
                stage.addActor(d5Text);
                stage.addActor(d5Button);
                playerName.setAlignment(Align.bottomLeft);
                playerName.setPosition((float)(stage.getWidth()/2 + playerName.getWidth()/3.35),
                        (float)(stage.getHeight() - playerName.getHeight() * 1.063));
                playerName.setText(username + ".");
                stage.addActor(playerName);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d4Button1HoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d4Button1Hover")));
                d4Button1Style.imageUp = d4Button1HoverDrawable;
                d4Button1Style.imageDown = d4Button1HoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d4Button1Style.imageUp = d4Button1Drawable;
                d4Button1Style.imageDown = d4Button1Drawable;
            }
        });
        //Listener for the right button, loads next backstory slide when clicked and changes size and colour when hovered.
        d4Button2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen = 5;
                stage.clear();
                stage.addActor(profBackground);
                stage.addActor(d5Text);
                stage.addActor(d5Button);
                playerName.setAlignment(Align.bottomLeft);
                playerName.setPosition((float)(stage.getWidth()/2 + playerName.getWidth()/3.35),
                        (float)(stage.getHeight() - playerName.getHeight() * 1.057));
                playerName.setText(username + ".");
                stage.addActor(playerName);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d4Button2HoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d4Button2Hover")));
                d4Button2Style.imageUp = d4Button2HoverDrawable;
                d4Button2Style.imageDown = d4Button2HoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d4Button2Style.imageUp = d4Button2Drawable;
                d4Button2Style.imageDown = d4Button2Drawable;
            }
        });
        //Listener for the "as always" button, loads next backstory slide when clicked and changes size and colour when hovered.
        d5Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                game.setScreen(new GameScreen(game,
                        GameScreen.gameType.NEW_GAME, hostname, username, password, textureAtlas, classType));
                storyBGM.dispose();
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable d5ButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("d5ButtonHover")));
                d5ButtonStyle.imageUp = d5ButtonHoverDrawable;
                d5ButtonStyle.imageDown = d5ButtonHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                d5ButtonStyle.imageUp = d5ButtonDrawable;
                d5ButtonStyle.imageDown = d5ButtonDrawable;
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

    public CombatEvolvedGame getGame() {
        return game;
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
        callButton.setPosition(stage.getWidth()/205 * 100  - callButton.getWidth()/4,
                stage.getHeight()/22 * 10 - callButton.getHeight()/2);
        skipButton.setPosition(stage.getWidth() - skipButton.getWidth() - 5, 10);

        d1Text.setPosition(stage.getWidth()/2 - d1Text.getWidth()/2,
                stage.getHeight() - d1Text.getHeight() * 12/10);
        d1Button1.setPosition(stage.getWidth()/2 + 15,
                stage.getHeight() - d1Text.getHeight() * 13/10 - d1Button1.getHeight());
        d1Button2.setPosition(stage.getWidth()/2 - d1Button2.getWidth() - 15,
                stage.getHeight() - d1Text.getHeight() * 13/10 - d1Button1.getHeight());

        //Position the player name depending on which screen it's on.
        if(screen == 1) {
            playerName.setAlignment(Align.topLeft);
            playerName.setPosition((float)(stage.getWidth()/2 - playerName.getWidth()/2.9),
                    (float)(stage.getHeight() - playerName.getHeight() * 1.33));
        } else if(screen == 5) {
            playerName.setAlignment(Align.bottomLeft);
            playerName.setPosition((float)(stage.getWidth()/2 + playerName.getWidth()/3.35),
                    (float)(stage.getHeight() - playerName.getHeight() * 1.057));
        }

        d2Text.setPosition(stage.getWidth()/2 - d2Text.getWidth()/2,
                stage.getHeight() - d2Text.getHeight() * 12/10);
        d2Button.setPosition(stage.getWidth()/2 - d2Button.getWidth()/2,
                stage.getHeight() - d2Text.getHeight() * 13/10 - d2Button.getHeight());

        d3Text.setPosition(stage.getWidth()/2 - d3Text.getWidth()/2,
                stage.getHeight() - d3Text.getHeight() * 12/10);
        d3Button1.setPosition(stage.getWidth()/2 + 15,
                stage.getHeight() - d3Text.getHeight() * 13/10 - d3Button1.getHeight());
        d3Button2.setPosition(stage.getWidth()/2 - d3Button2.getWidth() - 15,
                stage.getHeight() - d3Text.getHeight() * 13/10 - d3Button1.getHeight());

        d4Text.setPosition(stage.getWidth()/2 - d4Text.getWidth()/2,
                stage.getHeight() - d4Text.getHeight() * 12/10);
        d4Button1.setPosition(stage.getWidth()/2 + 30,
                stage.getHeight() - d4Text.getHeight() * 13/10 - d4Button1.getHeight());
        d4Button2.setPosition(stage.getWidth()/2 - d4Button2.getWidth() - 30,
                stage.getHeight() - d4Text.getHeight() * 13/10 - d4Button2.getHeight());

        d5Text.setPosition(stage.getWidth()/2 - d5Text.getWidth()/2,
                stage.getHeight() - d5Text.getHeight() * 12/10);
        d5Button.setPosition(stage.getWidth()/2 - d5Button.getWidth()/2,
                stage.getHeight() - d5Text.getHeight() * 13/10 - d5Button.getHeight());
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
