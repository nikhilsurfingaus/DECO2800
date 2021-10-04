package deco.combatevolved.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.managers.TextureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndGameScreen implements Screen {
    // Setup a logger for the screen
    private static final Logger logger = LoggerFactory.getLogger(EndGameScreen.class);
    private Stage stage;
    private Skin skin;

    private ImageButton goodButton;
    private ImageButton gladButton;
    private ImageButton reassureButton;
    private ImageButton endButton;
    private ImageButton menuButton;

    private Image e1Text;
    private Image e2Text;
    private Image e3Text;

    private Label playerName;

    private int screen;

    /**
     * The constructor of the MainMenuScreen
     *
     * @param
     */
    public EndGameScreen(final CombatEvolvedGame game, String username) {
        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        skin = GameManager.get().getSkin();
        screen = 1;

        //initialize background images, fill screen and add to stage
        Image e1Background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("e1Background"));
        e1Background.setFillParent(true);
        stage.addActor(e1Background);

        Image e2Background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("e2Background"));
        e2Background.setFillParent(true);

        Image e3Background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("e3Background"));
        e3Background.setFillParent(true);

        //Initialises buttons and text images
        Drawable e1TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("e1Text")));
        e1TextDrawable.setMinHeight(157);
        e1TextDrawable.setMinWidth(1157);
        e1Text = new Image(e1TextDrawable);
        e1Text.setPosition(stage.getWidth()/2 - e1Text.getWidth()/2,
                stage.getHeight() - e1Text.getHeight() * 12/10);
        stage.addActor(e1Text);

        Drawable gladButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gladButton")));
        gladButtonDrawable.setMinHeight(58);
        gladButtonDrawable.setMinWidth(265);
        gladButton = new ImageButton(gladButtonDrawable);
        gladButton.setPosition(stage.getWidth()/2 + 15,
                stage.getHeight() - e1Text.getHeight() * 13/10 - gladButton.getHeight());
        stage.addActor(gladButton);

        ImageButton.ImageButtonStyle gladButtonStyle = gladButton.getStyle();

        Drawable goodButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("goodButton")));
        goodButtonDrawable.setMinHeight(58);
        goodButtonDrawable.setMinWidth(265);
        goodButton = new ImageButton(goodButtonDrawable);
        goodButton.setPosition(stage.getWidth()/2 - goodButton.getWidth() - 15,
                stage.getHeight() - e1Text.getHeight() * 13/10 - goodButton.getHeight());
        stage.addActor(goodButton);

        ImageButton.ImageButtonStyle goodButtonStyle = goodButton.getStyle();

        Drawable e2TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("e2Text")));
        e2TextDrawable.setMinHeight(157);
        e2TextDrawable.setMinWidth(1157);
        e2Text = new Image(e2TextDrawable);
        e2Text.setPosition(stage.getWidth()/2 - e2Text.getWidth()/2,
                stage.getHeight() - e2Text.getHeight() * 12/10);

        Drawable reassureButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("reassureButton")));
        reassureButtonDrawable.setMinHeight(59);
        reassureButtonDrawable.setMinWidth(265);
        reassureButton = new ImageButton(reassureButtonDrawable);
        reassureButton.setPosition(stage.getWidth()/2 - reassureButton.getWidth()/2,
                stage.getHeight() - e2Text.getHeight() * 13/10 - reassureButton.getHeight());

        ImageButton.ImageButtonStyle reassureButtonStyle = reassureButton.getStyle();

        Drawable e3TextDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("e3Text")));
        e3TextDrawable.setMinHeight(157);
        e3TextDrawable.setMinWidth(1157);
        e3Text = new Image(e3TextDrawable);
        e3Text.setPosition(stage.getWidth()/2 - e3Text.getWidth()/2,
                stage.getHeight() - e3Text.getHeight() * 12/10);

        Drawable endButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("endButton")));
        endButtonDrawable.setMinHeight(58);
        endButtonDrawable.setMinWidth(226);
        endButton = new ImageButton(endButtonDrawable);
        endButton.setPosition(stage.getWidth()/2 + 30,
                stage.getHeight() - e3Text.getHeight() * 13/10 - endButton.getHeight());

        ImageButton.ImageButtonStyle endButtonStyle = endButton.getStyle();

        Drawable menuButtonDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("mainMenuButton")));
        menuButtonDrawable.setMinHeight(58);
        menuButtonDrawable.setMinWidth(226);
        menuButton = new ImageButton(menuButtonDrawable);
        menuButton.setPosition(stage.getWidth()/2 - menuButton.getWidth() - 30,
                stage.getHeight() - e3Text.getHeight() * 13/10 - menuButton.getHeight());

        ImageButton.ImageButtonStyle menuButtonStyle = menuButton.getStyle();

        playerName = new Label("", skin);
        playerName.setFontScale((float) 0.9, (float) 0.9);
        playerName.setHeight(157);
        playerName.setWidth(1157);
        playerName.setAlignment(Align.topLeft);
        playerName.setPosition((float)(stage.getWidth()/2 - playerName.getWidth()/4.5),
                (float)(stage.getHeight() - playerName.getHeight() * 1.397));
        playerName.setText(username);

        Label.LabelStyle playerNameStyle =  playerName.getStyle();
        BitmapFont playerNameFont =
                new BitmapFont(Gdx.files.internal("resources/fonts/Red Hat Display/RedHatDisplay-Regular.fnt"));
        playerNameStyle.font = playerNameFont;
        stage.addActor(playerName);

        //Initialize the BGM sound
        Music storyBGM = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Background " +
                        "Music/BGM_NormalPlay_01_Inactive.wav"));
        storyBGM.setLooping(true);
        storyBGM.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        storyBGM.play();

        //Initialize the selected sound
        Music selectedSound = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Sound " +
                        "Effect/EFFECT_select_02_Inactive.wav"));
        selectedSound.setLooping(false);
        selectedSound.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());


        //Listener for the skip button, skips the backstory and starts the game when clicked.
        gladButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Clicked glad I could help button");
                stage.clear();
                stage.addActor(e2Background);
                stage.addActor(e2Text);
                stage.addActor(reassureButton);
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable skipButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gladButtonHover")));
                gladButtonStyle.imageUp = skipButtonHoverDrawable;
                gladButtonStyle.imageDown = skipButtonHoverDrawable;
                selectedSound.play();

            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                gladButtonStyle.imageUp = gladButtonDrawable;
                gladButtonStyle.imageDown = gladButtonDrawable;
            }


        });
        //Listener for the pick up call button, loads next backstory slide when clicked and changes size and colour when hovered.
        goodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Clicked good to hear button");
                stage.clear();
                stage.addActor(e2Background);
                stage.addActor(e2Text);
                stage.addActor(reassureButton);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable goodButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("goodButtonHover")));
                goodButtonStyle.imageUp = goodButtonHoverDrawable;
                goodButtonStyle.imageDown = goodButtonHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                goodButtonStyle.imageUp = goodButtonDrawable;
                goodButtonStyle.imageDown = goodButtonDrawable;
            }
        });
        //Listener for the pick up call button, loads next backstory slide when clicked and changes size and colour when hovered.
        reassureButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Clicked that's reassuring button");
                stage.clear();
                screen = 3;
                stage.addActor(e3Background);
                stage.addActor(e3Text);
                playerName.setText(username + ".");
                playerName.setAlignment(Align.bottomLeft);
                playerName.setPosition((float)(stage.getWidth()/2 + playerName.getWidth() * 0.005),
                        (float)(stage.getHeight() - playerName.getHeight() * 1.09));
                stage.addActor(playerName);
                stage.addActor(endButton);
                stage.addActor(menuButton);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable reassureButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("reassureButtonHover")));
                reassureButtonStyle.imageUp = reassureButtonHoverDrawable;
                reassureButtonStyle.imageDown = reassureButtonHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                reassureButtonStyle.imageUp = reassureButtonDrawable;
                reassureButtonStyle.imageDown = reassureButtonDrawable;
            }
        });
        //Listener for the introduce button, loads next backstory slide when clicked and changes size and colour when hovered.
        endButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Exited game.");
                // Exit game
                Gdx.app.exit();
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable endButtonStyleHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("endButtonHover")));
                endButtonStyle.imageUp = endButtonStyleHoverDrawable;
                endButtonStyle.imageDown = endButtonStyleHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                endButtonStyle.imageUp = endButtonDrawable;
                endButtonStyle.imageDown = endButtonDrawable;
            }
        });
        //Listener for the introduce button, loads next backstory slide when clicked and changes size and colour when hovered.
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logger.info("Clicked to return to main menu");
                try {
                    //game.setScreen(new MainMenuScreen(game));
                    stage.clear();
                    storyBGM.dispose();
                    game.create();
                } catch (IllegalArgumentException e) {
                    logger.error("Can't return to main menu");
                    // Exit game
                    Gdx.app.exit();
                }
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                Drawable menuButtonHoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("mainMenuButtonHover")));
                menuButtonStyle.imageUp = menuButtonHoverDrawable;
                menuButtonStyle.imageDown = menuButtonHoverDrawable;
                selectedSound.play();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                menuButtonStyle.imageUp = menuButtonDrawable;
                menuButtonStyle.imageDown = menuButtonDrawable;
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
        e1Text.setPosition(stage.getWidth()/2 - e1Text.getWidth()/2,
                stage.getHeight() - e1Text.getHeight() * 12/10);
        gladButton.setPosition(stage.getWidth()/2 + 15,
                stage.getHeight() - e1Text.getHeight() * 13/10 - gladButton.getHeight());
        goodButton.setPosition(stage.getWidth()/2 - goodButton.getWidth() - 15,
                stage.getHeight() - e1Text.getHeight() * 13/10 - goodButton.getHeight());
        e2Text.setPosition(stage.getWidth()/2 - e2Text.getWidth()/2,
                stage.getHeight() - e2Text.getHeight() * 12/10);
        reassureButton.setPosition(stage.getWidth()/2 - reassureButton.getWidth()/2,
                stage.getHeight() - e2Text.getHeight() * 13/10 - reassureButton.getHeight());
        e3Text.setPosition(stage.getWidth()/2 - e3Text.getWidth()/2,
                stage.getHeight() - e3Text.getHeight() * 12/10);
        endButton.setPosition(stage.getWidth()/2 + 30,
                stage.getHeight() - e3Text.getHeight() * 13/10 - endButton.getHeight());
        menuButton.setPosition(stage.getWidth()/2 - menuButton.getWidth() - 30,
                stage.getHeight() - e3Text.getHeight() * 13/10 - menuButton.getHeight());
        if(screen == 1) {
            playerName.setAlignment(Align.topLeft);
            playerName.setPosition((float)(stage.getWidth()/2 - playerName.getWidth()/4.5),
                    (float)(stage.getHeight() - playerName.getHeight() * 1.397));
        } else if(screen == 3) {
            playerName.setAlignment(Align.bottomLeft);
            playerName.setPosition((float)(stage.getWidth()/2 + playerName.getWidth() * 0.005),
                    (float)(stage.getHeight() - playerName.getHeight() * 1.09));
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
