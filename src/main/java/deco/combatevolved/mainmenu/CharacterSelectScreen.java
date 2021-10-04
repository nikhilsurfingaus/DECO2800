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

import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.GameScreen;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.SoundManager;
import deco.combatevolved.managers.TextureManager;


public class CharacterSelectScreen implements Screen {
    final CombatEvolvedGame game;
    private Stage stage;
    private Skin skin;

    private ImageButton startButton;
    private ImageButton backButton;
    private ImageButton helpButton;

    private ImageButton panel1;
    private ImageButton panel2;
    private ImageButton panel3;

    private Button backdrop1;
    private Button backdrop2;
    private Button backdrop3;

    private Label tempTitle;
    private String textureAtlas;
    private String classType;

    private Image background;

    /**
     * The constructor of the CharacterSelectScreen
     *
     * @param game the Game to run
     */
    public CharacterSelectScreen(final CombatEvolvedGame game, String hostname, String username, String password,
                                 int gameType) {
        this.game = game;
        stage = new Stage(new ExtendViewport(1280, 720), game.getBatch());
        skin = GameManager.get().getSkin();

        //initialize background music class
        //note: this may not be the correct track, will need to discuss with
        // sound team.
        Music mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal(
                "resources/sounds/Background " +
                        "Music/BGM_BattlePlay_02_Inactive.mp3"));
        mainMenuMusic.setLooping(true);
        mainMenuMusic.setVolume(GameManager.get().getManager(SoundManager.class).getMusicVolume());
        mainMenuMusic.play();

        //initialize background image, fill screen and add to stage
        background =
                new Image(GameManager.get().getManager(TextureManager.class).getTexture("characterSelectBackground"));
        //background.setFillParent(true);
        stage.addActor(background);

        //note, temporary textButton to display text.
        tempTitle = new Label("Select a Character", skin,
                "label-font", "white");
        tempTitle.setFontScale(2f);
        tempTitle.setPosition(stage.getWidth() / 2 - tempTitle.getWidth(),
                stage.getHeight() - 100);
        stage.addActor(tempTitle);

        //create drawable class for the button.
        Drawable startDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("startButton")));
        //initialize button with drawable class, set position and add to stage
        startButton = new ImageButton(startDrawable);
        startButton.setPosition(stage.getWidth() / 2 - (354 / 2), 30);
        stage.addActor(startButton);

        //same process as above ^
        Drawable backDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("backButton")));
        backButton = new ImageButton(backDrawable);
        backDrawable.setMinWidth(120);
        backDrawable.setMinHeight(100);
        backButton.setPosition(20, stage.getHeight() - 170);
        stage.addActor(backButton);

        Drawable helpDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("helpButton")));
        helpButton = new ImageButton(helpDrawable);
        helpButton.setPosition(stage.getWidth() - helpButton.getWidth() - 60,
                stage.getHeight() - 135);
        stage.addActor(helpButton);

        Drawable Engineer =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("Engineer")));
        Engineer.setMinHeight(400);
        Engineer.setMinWidth(400);

        Drawable Soldier =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("Soldier")));
        Soldier.setMinHeight(400);
        Soldier.setMinWidth(400);

        Drawable Explorer =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).
                        getTexture("Explorer")));
        Explorer.setMinHeight(400);
        Explorer.setMinWidth(400);

        //initialize all panels with same drawable class, set position and
        // add to stage
        panel1 = new ImageButton(Soldier);
        backdrop1 = new Button(skin, "panel-button");
        panel2 = new ImageButton(Engineer);
        backdrop2 = new Button(skin, "panel-button");
        panel3 = new ImageButton(Explorer);
        backdrop3 = new Button(skin, "panel-button");
        panel1.setPosition(stage.getWidth() / 5 - panel1.getWidth() / 2,
                stage.getHeight() / 2 - panel1.getHeight() / 2);
        panel2.setPosition(stage.getWidth() / 2 - panel1.getWidth() / 2,
                stage.getHeight() / 2 - panel1.getHeight() / 2);
        panel3.setPosition(stage.getWidth() / 5 * 4 - panel1.getWidth() / 2,
                stage.getHeight() / 2 - panel1.getHeight() / 2);
        backdrop1.setPosition(panel1.getX() + 20, panel1.getY());
        backdrop2.setPosition(panel2.getX() + 20, panel2.getY());
        backdrop3.setPosition(panel3.getX() + 20, panel3.getY());
        backdrop1.setSize(panel1.getWidth() - 40, panel1.getHeight());
        backdrop2.setSize(panel2.getWidth() - 40, panel2.getHeight());
        backdrop3.setSize(panel3.getWidth() - 40, panel3.getHeight());
        backdrop1.setChecked(true);
        stage.addActor(backdrop1);
        stage.addActor(backdrop2);
        stage.addActor(backdrop3);
        stage.addActor(panel1);
        stage.addActor(panel2);
        stage.addActor(panel3);

        // added default to be soldier so the game does not crash
        textureAtlas = "soldierAtlas";
        classType = "Soldier";
        //character 1 click
        panel1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //code for selecting character 1
                textureAtlas = "soldierAtlas";
                classType = "Soldier";
                System.out.println("Click 2");
                backdrop1.setChecked(true);
                backdrop2.setChecked(false);
                backdrop3.setChecked(false);
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                backdrop1.setChecked(true);
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                if (backdrop2.isChecked() || backdrop3.isChecked()) {
                    backdrop1.setChecked(false);
                }
            }
        });
        //character 2 click
        panel2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //code for selecting character 2
                textureAtlas = "engineerAtlas";
                classType = "Engineer";
                System.out.println("Click 2");
                backdrop1.setChecked(false);
                backdrop2.setChecked(true);
                backdrop3.setChecked(false);
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                backdrop2.setChecked(true);
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                if (backdrop1.isChecked() || backdrop3.isChecked()) {
                    backdrop2.setChecked(false);
                }
            }
        });
        //character 3 click
        panel3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //code for selecting character 3
                textureAtlas = "explorerAtlas";
                classType = "Explorer";
                System.out.println("Click 3");
                backdrop1.setChecked(false);
                backdrop2.setChecked(false);
                backdrop3.setChecked(true);
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                backdrop3.setChecked(true);
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                if (backdrop1.isChecked() || backdrop2.isChecked()) {
                    backdrop3.setChecked(false);
                }
            }
        });

        //listen for a user click on start.
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and switch screen to GameScreen with a GameType
                // of 'NEW_GAME'
                mainMenuMusic.dispose();

                if (gameType == 0) {
                    game.setScreen(new BackstoryScreen(game, hostname, username, password, gameType, textureAtlas, classType));
                } else if (gameType == 1) {
                    game.setScreen(new GameScreen(new CombatEvolvedGame(),
                            GameScreen.gameType.CONNECT_TO_SERVER, hostname, username, password, textureAtlas, classType));
                }
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("startButtonHover")));
                startButton.getStyle().imageUp = hoverDrawable;
                startButton.getStyle().imageDown = hoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                startButton.getStyle().imageUp = startDrawable;
                startButton.getStyle().imageDown = startDrawable;
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and switch to MainMenuScreen
                mainMenuMusic.dispose();
                game.setScreen(new MainMenuScreen(game));
            }

            public void enter(InputEvent event, float x, float y, int pointer
                    , Actor fromActor) {
                Drawable hoverDrawable =
                        new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("backButtonHover")));
                hoverDrawable.setMinWidth(120);
                hoverDrawable.setMinHeight(100);
                backButton.getStyle().imageUp = hoverDrawable;
                backButton.getStyle().imageDown = hoverDrawable;

            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                backButton.getStyle().imageUp = backDrawable;
                backButton.getStyle().imageDown = backDrawable;
            }
        });

        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //stop music and switch to character info screen
                mainMenuMusic.dispose();
                game.setScreen(new CharacterInfoScreen(game, hostname,
                        username, password, gameType));
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
     * Resizes the character select stage to a new width and height
     *
     * @param width  the new width for the character select stage
     * @param height the new width for the character stage
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        //repositioning
        startButton.setPosition(stage.getWidth()/2 - startButton.getWidth()/2, 30);
        backButton.setPosition(20, stage.getHeight() - 170);
        helpButton.setPosition(stage.getWidth() - helpButton.getWidth() - 60,
                stage.getHeight() - 135);

        panel1.setPosition(stage.getWidth()/5 - panel1.getWidth()/2,
                stage.getHeight()/2 - panel1.getHeight()/2);
        panel2.setPosition(stage.getWidth()/2 - panel1.getWidth()/2,
                stage.getHeight()/2 - panel1.getHeight()/2);
        panel3.setPosition(stage.getWidth()/5 * 4 - panel1.getWidth()/2,
                stage.getHeight()/2 - panel1.getHeight()/2);
        backdrop1.setPosition(panel1.getX() + 20, panel1.getY());
        backdrop2.setPosition(panel2.getX() + 20, panel2.getY());
        backdrop3.setPosition(panel3.getX() + 20, panel3.getY());

        tempTitle.setPosition(stage.getWidth() / 2 - tempTitle.getWidth(),
                stage.getHeight() - 100);

        if (((float) width / height) > (5334f / 3000)) {
            background.setWidth(stage.getWidth());
            background.setHeight(stage.getWidth() * (3000f / 5334));
        } else {
            background.setHeight(stage.getHeight());
            background.setWidth(stage.getHeight() * (5334f / 3000));
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
     * Disposes of the stage that the screen is on
     */
    public void dispose() {
        stage.dispose();
    }
}