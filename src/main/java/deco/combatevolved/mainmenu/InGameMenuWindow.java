package deco.combatevolved.mainmenu;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import deco.combatevolved.CombatEvolvedGame;
import deco.combatevolved.managers.DatabaseManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.TextureManager;
import org.slf4j.Logger;



public class InGameMenuWindow implements Screen {
    private CombatEvolvedGame game;
    private int mode;
    // Display status of the menu
    private boolean displayMenu = false;

    // Menu buttons
    private ImageButton resumeGame;
    private ImageButton saveGame;
    private ImageButton loadGame;
    private ImageButton exitGame;
    private ImageButton gameSettings;
    private ImageButton help;

    private boolean pressBack = false;

    // Stage
    private Stage stage;
    private SettingsScreen settings;
    private HelpScreen helpScreen;

    /**
     * Constructor for the in-game menu window
     * @param logger - Logger to keep track of click events
     */
    public InGameMenuWindow(Logger logger, Stage stage, CombatEvolvedGame game) {
        // Stage
        this.stage = stage;

        this.game = game;

        mode = 1;

        settings = new SettingsScreen(this.game, this.mode, this.stage);
        helpScreen = new HelpScreen(this.game, this.stage, 1);

        // Get button images
        Drawable resumeGameDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("resumeGameButton")));
        Drawable saveGameDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("saveGameButton")));
        Drawable loadGameDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("loadGameButton")));
        Drawable exitGameDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("exitGameButton")));
        Drawable gameSettingsDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gameSettingsButton")));
        Drawable helpDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("tutorialButton")));

        // Get images of buttons for when mouse clicks and holds on to them.
        Drawable resumeGameHoverDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("resumeGameHoverButton")));
        Drawable saveGameHoverDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("saveGameHoverButton")));
        Drawable loadGameHoverDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("loadGameHoverButton")));
        Drawable exitGameHoverDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("exitGameHoverButton")));
        Drawable gameSettingsHoverDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("gameSettingsHoverButton")));
        Drawable helpHoverDrawable =
                new TextureRegionDrawable(new TextureRegion(GameManager.get().getManager(TextureManager.class).getTexture("tutorialHoverButton")));

        // Create buttons
        resumeGame = new ImageButton(resumeGameDrawable);
        saveGame = new ImageButton(saveGameDrawable);
        loadGame = new ImageButton(loadGameDrawable);
        exitGame = new ImageButton(exitGameDrawable);
        gameSettings = new ImageButton(gameSettingsDrawable);
        help = new ImageButton(helpDrawable);

        // Set the button coordinates in game
        setButtonPositions();

        // Button Listeners
        // Resume game listener
        resumeGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Close window
                closeMenu();
                pressBack = true;
                logger.info("In-Game Menu closed");
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                resumeGame.getStyle().imageUp = resumeGameHoverDrawable;
                resumeGame.getStyle().imageDown = resumeGameHoverDrawable;
            }

            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                resumeGame.getStyle().imageUp = resumeGameDrawable ;
                resumeGame.getStyle().imageDown = resumeGameDrawable ;
            }
        });

        // Save game listener
        saveGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Save game
                DatabaseManager.saveWorld(null);
                // Close window
                closeMenu();
                logger.info("Game saved.");
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                saveGame.getStyle().imageUp = saveGameHoverDrawable;
                saveGame.getStyle().imageDown = saveGameHoverDrawable;
            }
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                saveGame.getStyle().imageUp = saveGameDrawable ;
                saveGame.getStyle().imageDown = saveGameDrawable ;
            }
        });

        // Load game listener
        loadGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Load game
                DatabaseManager.loadWorld(null);
                // Close window
                closeMenu();
                logger.info("Game loaded.");
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                loadGame.getStyle().imageUp = loadGameHoverDrawable;
                loadGame.getStyle().imageDown = loadGameHoverDrawable;
            }
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                loadGame.getStyle().imageUp = loadGameDrawable ;
                loadGame.getStyle().imageDown = loadGameDrawable ;
            }
        });

        // Exit game listener
        exitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Close window
                closeMenu();
                logger.info("Game exiting");
                Gdx.app.exit();
                System.exit(0);
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitGame.getStyle().imageUp = exitGameHoverDrawable;
                exitGame.getStyle().imageDown = exitGameHoverDrawable;
            }
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                exitGame.getStyle().imageUp = exitGameDrawable ;
                exitGame.getStyle().imageDown = exitGameDrawable ;
            }
        });

        // Setting menu game listener
        gameSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Open settings menu
                renderSettingsMenu();
                logger.info("Opened game settings");
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                gameSettings.getStyle().imageUp = gameSettingsHoverDrawable;
                gameSettings.getStyle().imageDown = gameSettingsHoverDrawable;
            }
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                gameSettings.getStyle().imageUp = gameSettingsDrawable ;
                gameSettings.getStyle().imageDown = gameSettingsDrawable ;
            }
        });

        help.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                renderHelpMenu();
                logger.info("Help Screen opened");
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                help.getStyle().imageUp = helpHoverDrawable;
                help.getStyle().imageDown = helpHoverDrawable;
            }
            public void exit(InputEvent event, float x, float y, int pointer,
                             Actor actor) {
                help.getStyle().imageUp = helpDrawable ;
                help.getStyle().imageDown = helpDrawable ;
            }
        });
    }

    /**
     * Function to render the in-game menu window to be added to the stage
     */

    public void renderSettingsMenu() {
        closeMenu();
        Group settingsGroup = settings.getSettingsGroup();

        this.stage.addActor(settingsGroup);
    }

    /** displays the in-game help menu screen
     *
     */

    public void renderHelpMenu() {

        closeMenu();
        Group helpGroup = helpScreen.getHelpGroup();

        this.stage.addActor(helpGroup);
    }

    /**
     * Closes the in-game menu window
     */
    public void closeMenu() {
        // Clear stage
        stage.clear();
        // Toggle display menu status
        displayMenu = !displayMenu;
    }

    /**
     * Get the display status of the menu (whether it is showing or not)
     * @return status of the menu
     */
    public boolean displayStatus() {
        return displayMenu;
    }

    /**
     * Boolean describing if the back button was clicked.
     *
     * @return status of back button
     */
    public boolean backButtonPressed() {
        return pressBack;
    }

    /**
     * Set boolean describing if the back button was clicked.
     *
     */
    public void setButtonPressed() {
        this.pressBack = false;
    }

    /**
     * Set the button positions relative to the screen size
     */
    public void setButtonPositions() {
        resumeGame.setPosition((stage.getWidth() - resumeGame.getWidth())/2,
                stage.getHeight()/2 + 240);
        saveGame.setPosition((stage.getWidth() - saveGame.getWidth())/2,
                stage.getHeight()/2 + 140);
        loadGame.setPosition((stage.getWidth() - loadGame.getWidth())/2,
                stage.getHeight()/2 + 40);
        gameSettings.setPosition((stage.getWidth() - gameSettings.getWidth())/2,
                stage.getHeight()/2 - 60);
        help.setPosition((stage.getWidth() - resumeGame.getWidth())/2,
                stage.getHeight()/2 - 160);
        exitGame.setPosition((stage.getWidth() - exitGame.getWidth())/2,
                stage.getHeight()/2 - 260);

    }

    // Screen method overrides
    @Override
    public void show() {
        // do nothing
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    /**
     * Renders the menu
     */
    public void render() {
        // Open window
        if (!displayMenu) {
            settings.getSettingsGroup().remove();
            helpScreen.getHelpGroup().remove();
            displayMenu = true;
            stage.addActor(resumeGame);
            stage.addActor(saveGame);
            stage.addActor(loadGame);
            stage.addActor(gameSettings);
            stage.addActor(exitGame);
            stage.addActor(help);
        } else {
            // Close window
            displayMenu = false;
            resumeGame.remove();
            saveGame.remove();
            loadGame.remove();
            gameSettings.remove();
            exitGame.remove();
            help.remove();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // Position buttons
        setButtonPositions();
        settings.repositionElements(stage);
        helpScreen.repositionElements(stage);
    }

    @Override
    public void pause() {
        // do nothing
    }

    @Override
    public void resume() {
        // do nothing
    }

    @Override
    public void hide() {
        // do nothing
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
