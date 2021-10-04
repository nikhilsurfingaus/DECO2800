package deco.combatevolved.mainmenu;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.ScreenManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.managers.TextureManager;

import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.observers.KeyDownObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.Text;


public class HelpScreen implements Screen, KeyDownObserver {

    final CombatEvolvedGame game;
    private Stage stage;
    private Skin skin;

    private Group inGameHelp;

    private Image background;
    private Image craftPage;
    private Image recipePage;
    private Image recyclePage;
    private Image movementPage;

    private ImageButton backButton;

    private TextButton craftButton;
    private TextButton recipeButton;
    private TextButton recycleButton;

    private ImageButton exit;

    private Label title;

    private float ratio;

    private final Logger logger = LoggerFactory.getLogger(HelpScreen.class);


    /**
     * The constructor of the Help Screen
     *
     * @param game the Iguana Chase Game to run
     * @param mode 0 = main menu, 1 = in game
     */
    public HelpScreen(final CombatEvolvedGame game, Stage stage, int mode) {

        this.game = game;
        this.stage = stage;
        skin = GameManager.get().getSkin();

        inGameHelp = new Group();

        //main menu help screen stuff
        background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("settingsBackground"));
        background.setWidth(stage.getViewport().getScreenWidth());


        title = new Label("MOVEMENT CONTROLS", skin, "label-font", "white");
        title.setFontScale(2);
        title.setPosition(stage.getWidth()/2 - (title.getWidth()*3)/2,
                stage.getHeight()/2 + 300);

        Drawable backDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("backButton")));
        backDrawable.setMinWidth(100);
        backDrawable.setMinHeight(85);
        backButton = new ImageButton(backDrawable);
        backButton.setPosition(30, stage.getHeight() - 150);

        movementPage =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("movementPage"));
        if (stage.getWidth() > 1280) {
            movementPage.setHeight(stage.getHeight() * 8/10);
            movementPage.setWidth(movementPage.getHeight()/(720f/1280));
        } else {
            movementPage.setWidth(stage.getWidth() * 9 /10);
            movementPage.setHeight(movementPage.getWidth() * (720f/1280));
        }
        movementPage.setPosition(stage.getWidth()/2 - movementPage.getWidth()/2,
                stage.getHeight()/2 - movementPage.getHeight()/2);

        if (mode == 0) {
            stage.addActor(background);
            stage.addActor(title);
            stage.addActor(backButton);
            stage.addActor(movementPage);
        }

        //pages
        craftPage =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("craftPage"));
        recipePage =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("recipePage"));
        recyclePage =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("recyclePage"));

        ratio =
                craftPage.getDrawable().getMinHeight()/craftPage.getDrawable().getMinWidth();

        if (stage.getWidth() > 1280) {
            craftPage.setHeight(stage.getHeight() * 8/10);
            craftPage.setWidth(craftPage.getHeight()/ratio);

            recipePage.setHeight(stage.getHeight() * 8/10);
            recipePage.setWidth(recipePage.getHeight()/ratio);

            recyclePage.setHeight(stage.getHeight() * 8/10);
            recyclePage.setWidth(recyclePage.getHeight()/ratio);
        } else {
            craftPage.setWidth(stage.getWidth() * 9 / 10);
            craftPage.setHeight(craftPage.getWidth() * ratio);

            recipePage.setWidth(stage.getWidth() * 9/10);
            recipePage.setHeight(recipePage.getWidth() * ratio);

            recyclePage.setWidth(stage.getWidth() * 9/10);
            recyclePage.setHeight(recyclePage.getWidth() * ratio);
        }

        craftPage.setPosition(stage.getWidth()/2 - craftPage.getWidth()/2,
                stage.getHeight()/2 - craftPage.getHeight()/2 - 50);
        recipePage.setPosition(stage.getWidth()/2 - recipePage.getWidth()/2,
                stage.getHeight()/2 - recipePage.getHeight()/2 - 50);
        recyclePage.setPosition(stage.getWidth()/2 - recyclePage.getWidth()/2,
                stage.getHeight()/2 - recyclePage.getHeight()/2 - 50);
        inGameHelp.addActor(craftPage);

        //intializing MAIN BUTTONS
        craftButton = new TextButton("how to craft", skin);
        craftButton.setChecked(true);
        craftButton.setPosition(craftPage.getX(),
                craftPage.getY() + craftPage.getHeight());
        craftButton.setHeight(100);
        craftButton.setWidth(craftPage.getWidth()/3);
        inGameHelp.addActor(craftButton);

        recipeButton = new TextButton("recipe book", skin);
        recipeButton.setPosition(craftButton.getX() + craftButton.getWidth(),
                craftButton.getY());
        recipeButton.setHeight(100);
        recipeButton.setWidth(craftPage.getWidth()/3);
        inGameHelp.addActor(recipeButton);

        recycleButton = new TextButton("recycling", skin);
        recycleButton.setPosition(recipeButton.getX() + recipeButton.getWidth(),
                craftButton.getY());
        recycleButton.setHeight(100);
        recycleButton.setWidth(craftPage.getWidth()/3);
        inGameHelp.addActor(recycleButton);

        Drawable exitDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("exitIcon")));
        exit = new ImageButton(exitDrawable);
        exit.setSize(50, 50);
        exit.setPosition(recycleButton.getX() + recycleButton.getWidth() - exit.getWidth() - 10,
                recycleButton.getY() + recycleButton.getHeight() - exit.getHeight() - 10);
        inGameHelp.addActor(exit);

        //BUTTON LISTENERS

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and switch to MainMenuScreen
                //mainMenuMusic.dispose();
                game.setScreen(new MainMenuScreen(game));
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("backButtonHover")));
                hoverDrawable.setMinWidth(100);
                hoverDrawable.setMinHeight(85);
                backButton.getStyle().imageUp = hoverDrawable;
                backButton.getStyle().imageDown = hoverDrawable;

            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                backButton.getStyle().imageUp = backDrawable;
                backButton.getStyle().imageDown = backDrawable;
            }
        });

        exit.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                getHelpGroup().remove();
            }
        });
        craftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //remove old actors
                recipePage.remove();
                recyclePage.remove();

                //add new actors
                inGameHelp.addActor(craftPage);

                //style
                craftButton.setChecked(true);
                recipeButton.setChecked(false);
                recycleButton.setChecked(false);

            }
            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                //discuss with UI team
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                //discuss with UI team
            }
        });

        recipeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //remove old actors
                craftPage.remove();
                recyclePage.remove();

                //add new actors
                inGameHelp.addActor(recipePage);

                //style
                craftButton.setChecked(false);
                recipeButton.setChecked(true);
                recycleButton.setChecked(false);

            }
            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                //discuss with UI team
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                //discuss with UI team
            }
        });

        recycleButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //remove old actors
                recipePage.remove();
                craftPage.remove();

                //add new actors
                inGameHelp.addActor(recyclePage);

                //style
                craftButton.setChecked(false);
                recipeButton.setChecked(false);
                recycleButton.setChecked(true);

            }
            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                //discuss with UI team
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                //discuss with UI team
            }
        });

    }

    /** returns the in-game help page in group form to be used in game menu
     *
     * @return Group version of help screen
     */
    public Group getHelpGroup() {
        return this.inGameHelp;
    }


    @Override
    public void notifyKeyDown(int keycode) {
        //System.out.println(keycode);
        switch (keycode) {
            case Input.Keys.E:
                logger.info("pressed E\n");
                //System.out.println("bruh");
                break;

            case Input.Keys.R:
                logger.info("pressed R\n");
                break;

            case Input.Keys.I:
                logger.info("pressed I\n");
                break;

            default:
                break;
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
        repositionElements(this.stage);

        //main menu help resizing:
        title.setFontScale(2);
        title.setPosition(stage.getWidth()/2 - (title.getWidth()),
                stage.getHeight() - 100);
        backButton.setPosition(35, stage.getHeight() - 130);

        if (((float) width/height) > (5334f/3000)) {
            background.setWidth(stage.getWidth());
            background.setHeight(stage.getWidth() * (3000f/5334));
        } else {
            background.setHeight(stage.getHeight());
            background.setWidth(stage.getHeight() * (5334f/3000));
        }
    }

    public void repositionElements(Stage stage) {

        if (stage.getWidth() > 1280) {
            craftPage.setHeight(stage.getHeight() * 8/10);
            craftPage.setWidth(craftPage.getHeight()/ratio);

            recipePage.setHeight(stage.getHeight() * 8/10);
            recipePage.setWidth(recipePage.getHeight()/ratio);

            recyclePage.setHeight(stage.getHeight() * 8/10);
            recyclePage.setWidth(recyclePage.getHeight()/ratio);

            movementPage.setHeight(stage.getHeight() * 7/10);
            movementPage.setWidth(movementPage.getHeight()/(720f/1280));
        } else {
            craftPage.setWidth(stage.getWidth() * 9 / 10);
            craftPage.setHeight(craftPage.getWidth() * ratio);

            recipePage.setWidth(stage.getWidth() * 9/10);
            recipePage.setHeight(recipePage.getWidth() * ratio);

            recyclePage.setWidth(stage.getWidth() * 9/10);
            recyclePage.setHeight(recyclePage.getWidth() * ratio);

            movementPage.setWidth(stage.getWidth() * 7 /10);
            movementPage.setHeight(movementPage.getWidth() * (720f/1280));
        }

        craftPage.setPosition(stage.getWidth()/2 - craftPage.getWidth()/2,
                stage.getHeight()/2 - craftPage.getHeight()/2 - 50);
        recipePage.setPosition(stage.getWidth()/2 - recipePage.getWidth()/2,
                stage.getHeight()/2 - recipePage.getHeight()/2 - 50);
        recyclePage.setPosition(stage.getWidth()/2 - recyclePage.getWidth()/2,
                stage.getHeight()/2 - recyclePage.getHeight()/2 - 50);
        movementPage.setPosition(stage.getWidth()/2 - movementPage.getWidth()/2,
                stage.getHeight()/2 - movementPage.getHeight()/2);


        craftButton.setPosition(craftPage.getX(),
                craftPage.getY() + craftPage.getHeight());
        craftButton.setWidth(craftPage.getWidth()/3);

        recipeButton.setPosition(craftButton.getX() + craftButton.getWidth(),
                craftButton.getY());
        recipeButton.setWidth(craftButton.getWidth());

        recycleButton.setPosition(recipeButton.getX() + recipeButton.getWidth(),
                recipeButton.getY());
        recycleButton.setWidth(craftButton.getWidth());
    }

    /**
     * Renders the menu
     *
     * @param delta
     */
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //stage.addActor(inGameHelp);
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