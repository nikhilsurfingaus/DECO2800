package deco.combatevolved.managers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Texture manager acts as a cache between the file system and the renderers.
 * This allows all textures to be read into memory at the start of the game
 * saving file reads from being completed during rendering.
 * <p>
 * With this in mind don't load textures you're not going to use. Textures that
 * are not used should probably (at some point) be removed from the list and
 * then read from disk when needed again using some type of reference counting
 *
 */
public class TextureManager extends AbstractManager {

    /**
     * The width of the tile to use then positioning the tile.
     */
    public static final int TILE_WIDTH = 320;

    /**
     * The height of the tile to use when positioning the tile.
     */
    public static final int TILE_HEIGHT = 278;

    private final Logger log = LoggerFactory.getLogger(TextureManager.class);

    /**
     * A HashMap of all textures with string keys
     */
    private Map<String, Texture> textureMap = new HashMap<>();

    /**
     * A HashMap of all the texture atlases with string keys
     */
    private Map<String, TextureAtlas> atlasMap = new HashMap<>();

    /**
     * A check for the getters to see if the texture manager has put all of the textures in.
     */
    private volatile boolean ready = false;

    /**
     * Constructor Currently loads up all the textures but probably
     * shouldn't/doesn't need to.
     */
    public TextureManager() {
        log.info("Setting up texture manager");
        try {
            String resourcesPath = "resources/";

            // backgrounds
            String menuPath = resourcesPath + "main_menu/";

            textureMap.put("background", new Texture(menuPath + "main_menu_background.png"));
            textureMap.put("mainMenuBackground", new Texture(menuPath + "main_menu_background3.png"));
            textureMap.put("characterSelectBackground", new Texture(menuPath + "character_selection_background.png"));
            textureMap.put("worldSetUpBackground", new Texture(menuPath + "coop_background.png"));
            textureMap.put("settingsBackground", new Texture(menuPath + "settings_background.png"));

            textureMap.put("bullet", new Texture("resources/Bullet/bullet.png"));
            textureMap.put("enemyBullet", new Texture("resources/Bullet/enemy-ranged-attack.png"));
            textureMap.put("projectile", new Texture("resources/projectile.png"));

            String playerPath = resourcesPath + "PlayerCharacter/";
            textureMap.put("peon", new Texture(playerPath + "Character1/Character1_Front.png"));
            textureMap.put("spacman_ded", new Texture("resources/entities/spartan.png"));
            textureMap.put("spacman_ded2", new Texture(playerPath + "Character1/Character1_Front.png"));
            textureMap.put("Engineer", new Texture(playerPath + "Engineer/Engineer_Select.png"));
            textureMap.put("Explorer", new Texture(playerPath + "Explorer/Explorer_Select.png"));
            textureMap.put("Soldier", new Texture(playerPath + "Soldier/Soldier_Select.png"));
            textureMap.put("ferret", new Texture("resources/entities/ferret.png"));
            textureMap.put("jabber", new Texture("resources/entities/jabber.png"));

            String spaceManPath = resourcesPath + "spaceman/";
            textureMap.put("spacman_blue", new Texture(spaceManPath + "spacman_blue.png"));
            textureMap.put("spacman_red", new Texture(spaceManPath + "spacman_red.png"));
            textureMap.put("spacman_yellow", new Texture(spaceManPath + "spacman_yellow.png"));
            textureMap.put("spacman_green", new Texture(spaceManPath + "spacman_green.png"));

            // control (comm) towers textures
            String controlTowersPath = resourcesPath + "control_towers/";
            textureMap.put("RFcomTower1", new Texture(controlTowersPath + "control_tower1_rainforest_day.png"));
            textureMap.put("RFcomTower2", new Texture(controlTowersPath + "control_tower2_rainforest_day.png"));
            textureMap.put("ScomTower1", new Texture(controlTowersPath + "control_tower3_snow_day.png"));
            textureMap.put("ScomTower2", new Texture(controlTowersPath + "control_tower4_snow_day.png"));
            textureMap.put("DcomTower1", new Texture(controlTowersPath + "control_tower5_desert_day.png"));
            textureMap.put("DcomTower2", new Texture(controlTowersPath + "control_tower6_desert_day.png"));
            textureMap.put("PcomTower1", new Texture(controlTowersPath + "control_tower7_plains_day.png"));
            textureMap.put("PcomTower2", new Texture(controlTowersPath + "control_tower8_plains-day.png"));

            // textures for conquered control towers
            String conqueredTowersPath = controlTowersPath + "conquered_towers/";
            textureMap.put("RFcomTower1Con", new Texture(conqueredTowersPath + "control_tower1_rainforest_conquered.png"));
            textureMap.put("RFcomTower2Con", new Texture(conqueredTowersPath + "control_tower2_rainforest_conquered.png"));
            textureMap.put("ScomTower1Con", new Texture(conqueredTowersPath + "control_tower3_snow_conquered.png"));
            textureMap.put("ScomTower2Con", new Texture(conqueredTowersPath + "control_tower4_snow_conquered.png"));
            textureMap.put("DcomTower1Con", new Texture(conqueredTowersPath + "control_tower5_desert_conquered.png"));
            textureMap.put("DcomTower2Con", new Texture(conqueredTowersPath + "control_tower6_desert_conquered.png"));
            textureMap.put("PcomTower1Con", new Texture(conqueredTowersPath + "control_tower7_plains_conquered.png"));
            textureMap.put("PcomTower2Con", new Texture(conqueredTowersPath + "control_tower8_plains_conquered.png"));

            // Enemy resource textures
            addEnemyTextures();

            // Biome resource textures
            String biomePath = resourcesPath + "BiomeTextures/";
            // Day
            textureMap.put("beach_0", new Texture(biomePath + "day/beach_0.png"));
            textureMap.put("grassland_0", new Texture(biomePath + "day/grassland_0.png"));
            textureMap.put("grassland_1", new Texture(biomePath + "day/grassland_1.png"));
            textureMap.put("ice_0", new Texture(biomePath + "day/ice_0.png"));
            textureMap.put("mountainRocks_0", new Texture(biomePath + "day/mountainRocks_0.png"));
            textureMap.put("mountainRocks_1", new Texture(biomePath + "day/mountainRocks_1.png"));
            textureMap.put("shrubland_0", new Texture(biomePath + "day/shrubland_0.png"));
            textureMap.put("shrubland_1", new Texture(biomePath + "day/shrubland_1.png"));
            textureMap.put("snow_0", new Texture(biomePath + "day" + "/snow_0.png"));
            textureMap.put("snow_1", new Texture(biomePath + "day/snow_1.png"));
            textureMap.put("temperateDesert_0", new Texture(biomePath + "day/temperateDesert_0.png"));
            textureMap.put("temperateDesert_1", new Texture(biomePath + "day/temperateDesert_1.png"));
            textureMap.put("temperateRainforest_0", new Texture(biomePath + "day/temperateRainforest_0.png"));
            textureMap.put("temperateRainforest_1", new Texture(biomePath + "day/temperateRainforest_1.png"));
            textureMap.put("tropicalRainforest_0", new Texture(biomePath + "day/tropicalRainforest_0.png"));
            textureMap.put("tundra_0", new Texture(biomePath + "day/tundra_0.png"));
            textureMap.put("tundra_1", new Texture(biomePath + "day/tundra_1.png"));
            textureMap.put("ocean_0", new Texture(biomePath + "day/ocean_0.png"));

            // Night
            textureMap.put("plains-night_0", new Texture(biomePath + "night/plains_0.png"));
            textureMap.put("plains-night_1", new Texture(biomePath + "night/plains_1.png"));
            textureMap.put("sand-night_0", new Texture(biomePath + "night/desert_0.png"));
            textureMap.put("sand-night_1", new Texture(biomePath + "night/desert_1.png"));
            textureMap.put("snow-night_0", new Texture(biomePath + "night/snow_0.png"));
            textureMap.put("snow-night_1", new Texture(biomePath + "night/snow_1.png"));
            textureMap.put("snow-night_2", new Texture(biomePath + "night/snow_1.png"));
            textureMap.put("rainforest-night_0", new Texture(biomePath + "night/rainforest_0.png"));
            textureMap.put("rainforest-night_1", new Texture(biomePath + "night/rainforest_1.png"));

            textureMap.put("selection", new Texture("resources/selection/blue_selection.png"));
            textureMap.put("path", new Texture("resources/selection/yellow_selection.png"));

            textureMap.put("buildingB", new Texture("resources/building/building3x2.png"));
            textureMap.put("buildingA", new Texture("resources/building/buildingA.png"));
            textureMap.put("tree", new Texture("resources/tree.png"));
            textureMap.put("craftingtable", new Texture("resources/craftingtable.png"));
            textureMap.put("recipebook", new Texture("resources/book.png"));

            textureMap.put("fenceN-S", new Texture("resources/fence/fence N-S.png"));
            textureMap.put("fenceNE-SW", new Texture("resources/fence/fence NE-SW.png"));
            textureMap.put("fenceNW-SE", new Texture("resources/fence/fence NW-SE.png"));
            textureMap.put("fenceNE-S", new Texture("resources/fence/fence NE-S.png"));
            textureMap.put("fenceNE-SE", new Texture("resources/fence/fence NE-SE.png"));
            textureMap.put("fenceN-SE", new Texture("resources/fence/fence N-SE.png"));
            textureMap.put("fenceN-SW", new Texture("resources/fence/fence N-SW.png"));
            textureMap.put("fenceNW-NE", new Texture("resources/fence/fence NW-NE.png"));
            textureMap.put("fenceSE-SW", new Texture("resources/fence/fence SE-SW.png"));
            textureMap.put("fenceNW-S", new Texture("resources/fence/fence NW-S.png"));
            textureMap.put("rock", new Texture("resources/rocks.png"));
            textureMap.put("testItem1", new Texture("resources/test item 1.png"));
            textureMap.put("recycle", new Texture("resources/recycle.png"));
            textureMap.put("scrap", new Texture("resources/scrap.png"));

            // equipments
            textureMap.put("equipmentPanel", new Texture("resources/Sprites/Equipment/equipmentPanel.png"));
            textureMap.put("testEquipment", new Texture("resources/Sprites/Equipment/test_equipment.png"));
            textureMap.put("helmet_low", new Texture("resources/Sprites/Equipment/Armour/helmet_lowlevel.png"));
            textureMap.put("helmet_mid", new Texture("resources/Sprites/Equipment/Armour/helmet_midlevel.png"));
            textureMap.put("helmet_high", new Texture("resources/Sprites/Equipment/Armour/helmet_highlevel.png"));
            textureMap.put("armour_low", new Texture("resources/Sprites/Equipment/Armour/body armour_lowlevel.png"));
            textureMap.put("armour_mid", new Texture("resources/Sprites/Equipment/Armour/body armour_midlevel.png"));
            textureMap.put("armour_high", new Texture("resources/Sprites/Equipment/Armour/body armour_highlevel.png"));
            textureMap.put("shoes_low", new Texture("resources/Sprites/Equipment/Armour/leg guard_lowlevel.png"));
            textureMap.put("shoes_mid", new Texture("resources/Sprites/Equipment/Armour/leg guard_midlevel.png"));
            textureMap.put("shoes_high", new Texture("resources/Sprites/Equipment/Armour/leg guard_highlevel.png"));
            textureMap.put("gun_low", new Texture("resources/weapons/GUN-BASIC.png"));
            textureMap.put("gun_mid", new Texture("resources/weapons/GUN-INTERMEDIATE.png"));
            textureMap.put("gun_high", new Texture("resources/weapons/GUN-ADVANCED.png"));
            textureMap.put("grenade_low", new Texture("resources/weapons/GRENADE.png"));
            textureMap.put("grenade_mid", new Texture("resources/weapons/GRENADE.png"));
            textureMap.put("grenade_high", new Texture("resources/weapons/GRENADE.png"));

            // In-game menu buttons
            String buttonsPath = resourcesPath + "Buttons/buttons_v2/";
            textureMap.put("resumeGameButton", new Texture(buttonsPath + "resume_game_btn2.png"));
            textureMap.put("saveGameButton", new Texture(buttonsPath + "save_game_btn2.png"));
            textureMap.put("loadGameButton", new Texture(buttonsPath + "load_game_btn2.png"));
            textureMap.put("exitGameButton", new Texture(buttonsPath + "exit_game_btn2.png"));
            textureMap.put("gameSettingsButton", new Texture(buttonsPath + "settings_btn2.png"));
            textureMap.put("tutorialButton", new Texture(buttonsPath + "help_btn2.png"));

            // In-game menu buttons hover render
            textureMap.put("resumeGameHoverButton", new Texture(buttonsPath + "/resume_game_btn_hover.png"));
            textureMap.put("saveGameHoverButton", new Texture(buttonsPath + "/save_game_btn_hover.png"));
            textureMap.put("loadGameHoverButton", new Texture(buttonsPath + "/load_game_btn_hover.png"));
            textureMap.put("exitGameHoverButton", new Texture(buttonsPath + "/exit_game_btn_hover.png"));
            textureMap.put("gameSettingsHoverButton", new Texture(buttonsPath + "/settings_btn_hover.png"));
            textureMap.put("tutorialHoverButton", new Texture(buttonsPath + "help_btn_hover.png"));

            // In-game menu backgrounds
            textureMap.put("modalBackground", new Texture(buttonsPath + "/menu_modal_background.png"));
            textureMap.put("overlayBackground", new Texture(buttonsPath + "/modal_overlay.png"));

            // In game Background for recipe book
            String recipeBookPath = resourcesPath + "RecipeBook/";
            textureMap.put("testRecipeBookPg1", new Texture(recipeBookPath + "recipe-book-pg1-test.png"));
            textureMap.put("testRecipeBookPg2", new Texture(recipeBookPath + "recipe-book-pg2-test.png"));
            textureMap.put("recipeBookSample", new Texture(recipeBookPath + "recipe_book_sample.png"));
            textureMap.put("blankImages", new Texture(recipeBookPath + "blank.png"));

            // In-game recipe book buttons
            String recipeBookButtonsPath = recipeBookPath + "Buttons/";
            textureMap.put("recipeBackButtonNormal", new Texture(recipeBookButtonsPath + "back_normal.png"));
            textureMap.put("recipeBackButtonClicked", new Texture(recipeBookButtonsPath + "back_clicked.png"));
            textureMap.put("equipmentsButtonNormal", new Texture(recipeBookButtonsPath + "equipments_normal.png"));
            textureMap.put("equipmentsButtonClicked", new Texture(recipeBookButtonsPath + "equipments_clicked.png"));
            textureMap.put("itemsButtonNormal", new Texture(recipeBookButtonsPath + "items_normal.png"));
            textureMap.put("itemsButtonClicked", new Texture(recipeBookButtonsPath + "items_clicked.png"));
            textureMap.put("towersButtonNormal", new Texture(recipeBookButtonsPath + "towers_normal.png"));
            textureMap.put("towersButtonClicked", new Texture(recipeBookButtonsPath + "towers_clicked.png"));
            textureMap.put("leftPageButtonNormal", new Texture(recipeBookButtonsPath + "left_normal.png"));
            textureMap.put("leftPageButtonClicked", new Texture(recipeBookButtonsPath + "left_clicked.png"));
            textureMap.put("rightPageButtonNormal", new Texture(recipeBookButtonsPath + "right_normal.png"));
            textureMap.put("rightPageButtonClicked", new Texture(recipeBookButtonsPath + "right_clicked.png"));
            textureMap.put("homePageButtonNormal", new Texture(recipeBookButtonsPath + "home_normal.png"));
            textureMap.put("homePageButtonClicked", new Texture(recipeBookButtonsPath + "home_clicked.png"));
            textureMap.put("craftingEnabled", new Texture(recipeBookButtonsPath + "craftEnabled.png"));
            textureMap.put("craftingDisabled", new Texture(recipeBookButtonsPath + "craftDisabled.png"));

            // In-game recipe book sample tabs for items,weapons and towers
            String recipeBookObjectsPath = recipeBookPath + "ListsOfObjects/";
            textureMap.put("itemsPageOne", new Texture(recipeBookObjectsPath + "items_pg1.png"));
            textureMap.put("itemsPageTwo", new Texture(recipeBookObjectsPath + "items_pg2.png"));
            textureMap.put("equipmentsPageOne", new Texture(recipeBookObjectsPath + "equipments_pg1.png"));
            textureMap.put("equipmentsPageTwo", new Texture(recipeBookObjectsPath + "equipments_pg2.png"));
            textureMap.put("towersPageOne", new Texture(recipeBookObjectsPath + "towers_pg1.png"));
            textureMap.put("towersPageTwo", new Texture(recipeBookObjectsPath + "towers_pg2.png"));

            // Recipe book crafting examples
            String recipeBookDescriptionsPath = recipeBookPath + "Descriptions/";
            // Temporary sample
            textureMap.put("pendingCrafting", new Texture(recipeBookDescriptionsPath + "descPending.png"));

            // Item examples
            textureMap.put("gammaStoneCrafting", new Texture(recipeBookDescriptionsPath + "Items/Diagram/gamma-stone.png"));
            textureMap.put("steelCrafting", new Texture(recipeBookDescriptionsPath + "Items/Diagram/steel.png"));
            textureMap.put("plasmaCrafting", new Texture(recipeBookDescriptionsPath + "Items/Diagram/plasma.png"));
            textureMap.put("reinforcedConcreteCrafting", new Texture(recipeBookDescriptionsPath + "Items/Diagram/reinforced-concrete.png"));

            // Equipment examples
            textureMap.put("grenadeCrafting", new Texture(recipeBookDescriptionsPath + "Equipments/Diagram/grenade.png"));
            textureMap.put("spikedMaceCrafting", new Texture(recipeBookDescriptionsPath + "Equipments/Diagram/spiked-mace.png"));
            textureMap.put("spudGunCrafting", new Texture(recipeBookDescriptionsPath + "Equipments/Diagram/spud-gun.png"));

            // Tower examples
            textureMap.put("simpleTowerCrafting", new Texture(recipeBookDescriptionsPath + "Towers/Diagram/basictowerresourcescrafting.png"));

            ////////////// Not implemented yet ///////////////

            // Recipe book description examples
            // Temporary sample
            textureMap.put("pendingDesc", new Texture(recipeBookDescriptionsPath + "descPending.png"));

            // Item examples
            textureMap.put("gammaStoneDesc",new Texture(recipeBookDescriptionsPath + "Items/Detail/descGammaStone.png"));
            textureMap.put("steelDesc", new Texture(recipeBookDescriptionsPath + "Items/Detail/descSteel.png"));
            textureMap.put("plasmaDesc", new Texture(recipeBookDescriptionsPath + "Items/Detail/descPlasma.png"));
            textureMap.put("reinforcedConcreteDesc", new Texture(recipeBookDescriptionsPath + "Items/Detail/descReinforcedConcrete.png"));

            // Equipment examples
            textureMap.put("grenadeDesc", new Texture(recipeBookDescriptionsPath + "Equipments/Detail/descGrenade.png"));
            textureMap.put("spikedMaceDesc", new Texture(recipeBookDescriptionsPath + "Equipments/Detail/descSpikedMace.png"));
            textureMap.put("spudGunDesc", new Texture(recipeBookDescriptionsPath + "Equipments/Detail/descSpudGun.png"));

            // Tower examples

            //Defense Tower Textures
            textureMap.put("basicTowerDesc", new Texture(recipeBookDescriptionsPath + "Towers/Detail/basicTowerDesc.png"));
            textureMap.put("multiTowerDesc", new Texture(recipeBookDescriptionsPath + "Towers/Detail/multiTowerDesc.png"));
            textureMap.put("sniperTowerDesc", new Texture(recipeBookDescriptionsPath + "Towers/Detail/sniperTowerDesc.png"));
            textureMap.put("slowTowerDesc", new Texture(recipeBookDescriptionsPath + "Towers/Detail/slowTowerDesc.png"));
            textureMap.put("splashTowerDesc", new Texture(recipeBookDescriptionsPath + "Towers/Detail/splashTowerDesc.png"));
            textureMap.put("zapTowerDesc", new Texture(recipeBookDescriptionsPath + "Towers/Detail/zapTowerDesc.png"));

            // main menu screen UI
            textureMap.put("white", new Texture(resourcesPath + "/whiteBackground.png"));
            textureMap.put("exitButton", new Texture(buttonsPath + "exit_btn2.png"));
            textureMap.put("exitButtonHover", new Texture(buttonsPath + "exit_btn_hover.png"));
            textureMap.put("helpButton", new Texture(buttonsPath + "help_btn_symbol.png"));
            textureMap.put("helpButtonHover", new Texture(buttonsPath + "help_btn_symbol_hover.png"));
            textureMap.put("settingsButton", new Texture(buttonsPath + "settings_symbol_btn.png"));
            textureMap.put("settingsButtonHover", new Texture(buttonsPath + "settings_symbol_btn_hover.png"));
            textureMap.put("startButton", new Texture(buttonsPath + "start_btn2.png"));
            textureMap.put("startButtonHover", new Texture(buttonsPath + "start_btn_hover.png"));
            textureMap.put("backButton", new Texture(buttonsPath + "back_btn.png"));
            textureMap.put("backButtonHover", new Texture(buttonsPath + "back_btn_hover.png"));
            textureMap.put("loadButton", new Texture(buttonsPath + "load_game_btn2.png"));
            textureMap.put("loadButtonHover", new Texture(buttonsPath + "load_game_btn_hover.png"));
            textureMap.put("menuLogo", new Texture("resources/combat_logo/combat-evolved" + "-logo.png"));
            textureMap.put("gameIcon", new Texture("resources/combat_logo/game-icon.png"));
            textureMap.put("playButton", new Texture(buttonsPath + "play_btn2.png"));
            textureMap.put("playButtonHover", new Texture(buttonsPath + "play_btn_hover.png"));
            textureMap.put("goBackButton", new Texture(buttonsPath + "go_back_btn2.png"));
            textureMap.put("goBackButtonHover", new Texture(buttonsPath + "go_back_btn_hover.png"));
            textureMap.put("createWorldButton", new Texture(buttonsPath + "create_world_btn2.png"));
            textureMap.put("createWorldButtonHover", new Texture(buttonsPath + "create_world_btn_hover.png"));

            // settings UI
            String settingsUIPath = resourcesPath + "Buttons/settings_ui/";
            textureMap.put("onButton", new Texture(settingsUIPath + "on_btn.png"));
            textureMap.put("onButtonHover", new Texture(settingsUIPath + "on_btn_hover.png"));
            textureMap.put("offButton", new Texture(settingsUIPath + "off_btn.png"));
            textureMap.put("offButtonHover", new Texture(settingsUIPath + "off_btn_hover.png"));
            textureMap.put("keybindingsButton", new Texture(settingsUIPath + "keybindings_btn.png"));
            textureMap.put("saveButton", new Texture(settingsUIPath + "save_settings_btn.png"));
            textureMap.put("saveButtonHover", new Texture(settingsUIPath + "save_settings_btn_hover.png"));
            textureMap.put("settingsBackdrop", new Texture(settingsUIPath + "settings_modal.png"));
            textureMap.put("go", new Texture(settingsUIPath + "go_btn.png"));
            textureMap.put("goHover", new Texture(settingsUIPath + "go_btn_hover.png"));

            // Help screens
            textureMap.put("craftPage", new Texture("resources/helpScreen/UpdatedCraftingPage.png"));
            textureMap.put("recipePage", new Texture("resources/helpScreen/UpdatedRecipePage.png"));
            textureMap.put("recyclePage", new Texture("resources/helpScreen/UpdatedRecyclingPage.png"));
            textureMap.put("exitIcon", new Texture("resources/CharacterStatsScreen/exitCross.png"));
            textureMap.put("movementPage", new Texture("resources/GameMechanics/MainMenu/controlsMainScreenNoBck.png"));

            //In game tutorial
            textureMap.put("tutorial1", new Texture("resources" +
                    "/GameMechanics/InGame/dialogue1.png"));
            textureMap.put("tutorial2", new Texture("resources" +
                    "/GameMechanics/InGame/dialogue2.png"));
            textureMap.put("tutorial3", new Texture("resources" +
                    "/GameMechanics/InGame/dialogue3.png"));
            textureMap.put("tutorial4", new Texture("resources" +
                    "/GameMechanics/InGame/dialogue4.png"));

            // Defense Tower Textures
            textureMap.put("simpletower_U", new Texture("resources/towers/simpletower_up.png"));
            textureMap.put("simpletower_D", new Texture("resources/towers/simpletower_down.png"));
            textureMap.put("simpletower_UL", new Texture("resources/towers/simpletower_upleft.png"));
            textureMap.put("simpletower_UR", new Texture("resources/towers/simpletower_upright.png"));
            textureMap.put("simpletower_DL", new Texture("resources/towers/simpletower_downleft.png"));
            textureMap.put("simpletower_DR", new Texture("resources/towers/simpletower_downright.png"));
            textureMap.put("simpletower_bullet", new Texture("resources/Bullet/bullet.png"));

            textureMap.put("snipertower_U", new Texture("resources/towers/snipertower_U.png"));
            textureMap.put("snipertower_D", new Texture("resources/towers/snipertower_D.png"));
            textureMap.put("snipertower_UL", new Texture("resources/towers/snipertower_UL.png"));
            textureMap.put("snipertower_UR", new Texture("resources/towers/snipertower_UR.png"));
            textureMap.put("snipertower_DL", new Texture("resources/towers/snipertower_DL.png"));
            textureMap.put("snipertower_DR", new Texture("resources/towers/snipertower_DR.png"));
            textureMap.put("snipertower_bullet", new Texture("resources/towers/snipertower_projectile.png"));

            textureMap.put("slowtower_U", new Texture("resources/towers/slimetower_up.png"));
            textureMap.put("slowtower_D", new Texture("resources/towers/slimetower_down.png"));
            textureMap.put("slowtower_UL", new Texture("resources/towers/slimetower_upleft.png"));
            textureMap.put("slowtower_UR", new Texture("resources/towers/slimetower_upright.png"));
            textureMap.put("slowtower_DL", new Texture("resources/towers/slimetower_downleft.png"));
            textureMap.put("slowtower_DR", new Texture("resources/towers/slimetower_downright.png"));
            textureMap.put("slowtower_bullet", new Texture("resources/towers/slowProjectile.png"));

            textureMap.put("splashtower_U", new Texture("resources/towers/splashtower_up.png"));
            textureMap.put("splashtower_D", new Texture("resources/towers/splashtower_down.png"));
            textureMap.put("splashtower_UL", new Texture("resources/towers/splashtower_upleft.png"));
            textureMap.put("splashtower_UR", new Texture("resources/towers/splashtower_upright.png"));
            textureMap.put("splashtower_DL", new Texture("resources/towers/splashtower_downleft.png"));
            textureMap.put("splashtower_DR", new Texture("resources/towers/splashtower_downright.png"));
            textureMap.put("splashtower_bullet", new Texture("resources/towers/splashProjectile.png"));

            textureMap.put("splashtower", new Texture("resources/towers/splashtower.png"));
            textureMap.put("zaptower_D", new Texture("resources/towers/zaptower_n.png"));
            textureMap.put("zaptower_bullet", new Texture("resources/towers/zaptower_projectile.png"));
            textureMap.put("multitower", new Texture("resources/towers/multitower.png"));
            textureMap.put("multitower_bullet", new Texture("resources/towers/multiProjectile.png"));
            textureMap.put("tower_button", new Texture("resources/towers/tower_button1.png"));
            textureMap.put("towerEffect_Pause", new Texture("resources/towers/towerEffect_pause.png"));

            // Character Info Screen
            textureMap.put("exitCross", new Texture("resources" +
                    "/CharacterStatsScreen" + "/exitCross.png"));
            textureMap.put("leftButton", new Texture("resources" +
                    "/CharacterStatsScreen" + "/leftButton.png"));
            textureMap.put("rightButton", new Texture("resources" +
                    "/CharacterStatsScreen" + "/rightButton.png"));
            textureMap.put("engineerStats", new Texture("resources" +
                    "/CharacterStatsScreen" + "/UpdatedEngineerStats.png"));
            textureMap.put("explorerStats", new Texture("resources" +
                    "/CharacterStatsScreen" + "/UpdatedExplorerStats.png"));
            textureMap.put("soldierStats", new Texture("resources" +
                    "/CharacterStatsScreen" + "/UpdatedSoldierStats.png"));

            textureMap.put("snipertower_bullet_D", new Texture("resources/Bullet/sniperprojectile_down.png"));
            textureMap.put("snipertower_bullet_DL", new Texture("resources/Bullet/sniperprojectile_downleft.png"));
            textureMap.put("snipertower_bullet_DR", new Texture("resources/Bullet/sniperprojectile_downright.png"));
            textureMap.put("snipertower_bullet_U", new Texture("resources/Bullet/sniperprojectile_up.png"));
            textureMap.put("snipertower_bullet_UL", new Texture("resources/Bullet/sniperprojectile_upleft.png"));
            textureMap.put("snipertower_bullet_UR", new Texture("resources/Bullet/sniperprojectile_upright.png"));

            textureMap.put("zaptower_bullet_D", new Texture("resources/Bullet/zapprojectile_down.png"));
            textureMap.put("zaptower_bullet_DL", new Texture("resources/Bullet/zapprojectile_downleft.png"));
            textureMap.put("zaptower_bullet_DR", new Texture("resources/Bullet/zapprojectile_downright.png"));
            textureMap.put("zaptower_bullet_U", new Texture("resources/Bullet/zapprojectile_up.png"));
            textureMap.put("zaptower_bullet_UL", new Texture("resources/Bullet/zapprojectile_upleft.png"));
            textureMap.put("zaptower_bullet_UR", new Texture("resources/Bullet/zapprojectile_upright.png"));

            textureMap.put("simpletower_bullet_D", new Texture("resources/Bullet/basicprojectile_down.png"));
            textureMap.put("simpletower_bullet_DL", new Texture("resources/Bullet/basicprojectile_downleft.png"));
            textureMap.put("simpletower_bullet_DR", new Texture("resources/Bullet/basicprojectile_downright.png"));
            textureMap.put("simpletower_bullet_U", new Texture("resources/Bullet/basicprojectile_up.png"));
            textureMap.put("simpletower_bullet_UL", new Texture("resources/Bullet/basicprojectile_upleft.png"));
            textureMap.put("simpletower_bullet_UR", new Texture("resources/Bullet/basicprojectile_upright.png"));

            // Backstory images
            textureMap.put("callButton", new Texture("resources/backstory_ui" + "/call_btn.png"));
            textureMap.put("callButtonHover", new Texture("resources/backstory_ui" + "/call_btn_hover.png"));
            textureMap.put("callScreenBackground",
                    new Texture("resources/backstory_ui" + "/call_screen_background.png"));
            textureMap.put("professorTalkingBackground",
                    new Texture("resources/backstory_ui" + "/d1_d5_background.png"));
            textureMap.put("planetCallBackground", new Texture("resources/backstory_ui" + "/d2_background.png"));
            textureMap.put("towerCallBackground", new Texture("resources/backstory_ui" + "/d3_background.png"));
            textureMap.put("enemyCallBackground", new Texture("resources/backstory_ui" + "/d4_background.png"));
            textureMap.put("d1Button1", new Texture("resources/backstory_ui" + "/d1_btn_1.png"));
            textureMap.put("d1Button1Hover", new Texture("resources/backstory_ui" + "/d1_btn_1_hover.png"));
            textureMap.put("d1Button2", new Texture("resources/backstory_ui" + "/d1_btn_2.png"));
            textureMap.put("d1Button2Hover", new Texture("resources/backstory_ui" + "/d1_btn_2_hover.png"));
            textureMap.put("d1Text", new Texture("resources/backstory_ui" + "/d1_text.png"));
            textureMap.put("d2Button", new Texture("resources/backstory_ui" + "/d2_btn.png"));
            textureMap.put("d2ButtonHover", new Texture("resources/backstory_ui" + "/d2_btn_hover.png"));
            textureMap.put("d2Text", new Texture("resources/backstory_ui" + "/d2_text.png"));
            textureMap.put("d3Button1", new Texture("resources/backstory_ui" + "/d3_btn_1.png"));
            textureMap.put("d3Button1Hover", new Texture("resources/backstory_ui" + "/d3_btn_1_hover.png"));
            textureMap.put("d3Button2", new Texture("resources/backstory_ui" + "/d3_btn_2.png"));
            textureMap.put("d3Button2Hover", new Texture("resources/backstory_ui" + "/d3_btn_2_hover.png"));
            textureMap.put("d3Text", new Texture("resources/backstory_ui" + "/d3_text.png"));
            textureMap.put("d4Button1", new Texture("resources/backstory_ui" + "/d4_btn_1.png"));
            textureMap.put("d4Button1Hover", new Texture("resources/backstory_ui" + "/d4_btn_1_hover.png"));
            textureMap.put("d4Button2", new Texture("resources/backstory_ui" + "/d4_btn_2.png"));
            textureMap.put("d4Button2Hover", new Texture("resources/backstory_ui" + "/d4_btn_2_hover.png"));
            textureMap.put("d4Text", new Texture("resources/backstory_ui" + "/d4_text.png"));
            textureMap.put("d5Button", new Texture("resources/backstory_ui" + "/d5_btn.png"));
            textureMap.put("d5ButtonHover", new Texture("resources/backstory_ui" + "/d5_btn_hover.png"));
            textureMap.put("d5Text", new Texture("resources/backstory_ui" + "/d5_text.png"));
            textureMap.put("skipButton", new Texture("resources/backstory_ui" + "/skip_btn.png"));
            textureMap.put("skipButtonHover", new Texture("resources/backstory_ui" + "/skip_btn_hover.png"));

            // Endgame UI
            String endGamePath = resourcesPath + "end_story_ui/";
            textureMap.put("callProfButton", new Texture(endGamePath + "call_professor_btn.png"));
            textureMap.put("callProfButtonHover", new Texture(endGamePath + "call_professor_btn_hover.png"));
            textureMap.put("e1Text", new Texture(endGamePath + "e1_text_v2.png"));
            textureMap.put("e2Text", new Texture(endGamePath + "e2_text_v2.png"));
            textureMap.put("e3Text", new Texture(endGamePath + "e3_text_v2.png"));
            textureMap.put("e1Background", new Texture(endGamePath + "e1_background.png"));
            textureMap.put("e2Background", new Texture(endGamePath + "e2_background.png"));
            textureMap.put("e3Background", new Texture(endGamePath + "e3_background.png"));
            textureMap.put("endButton", new Texture(endGamePath + "end_btn.png"));
            textureMap.put("endButtonHover", new Texture(endGamePath + "end_btn_hover.png"));
            textureMap.put("endExitButton", new Texture(endGamePath + "exit_game_btn.png"));
            textureMap.put("endExitButtonHover", new Texture(endGamePath + "exit_game_btn_hover.png"));
            textureMap.put("gladButton", new Texture(endGamePath + "glad_hear_btn.png"));
            textureMap.put("gladButtonHover", new Texture(endGamePath + "glad_hear_btn_hover.png"));
            textureMap.put("goodButton", new Texture(endGamePath + "good_hear_btn.png"));
            textureMap.put("goodButtonHover", new Texture(endGamePath + "good_hear_btn_hover.png"));
            textureMap.put("lossText", new Texture(endGamePath + "loss_modal_text.png"));
            textureMap.put("mainMenuButton", new Texture(endGamePath + "main_menu_btn.png"));
            textureMap.put("mainMenuButtonHover", new Texture(endGamePath + "main_menu_btn_hover.png"));
            textureMap.put("playAgainButton", new Texture(endGamePath + "play_again_btn.png"));
            textureMap.put("playAgainButtonHover", new Texture(endGamePath + "play_again_btn_hover.png"));
            textureMap.put("reassureButton", new Texture(endGamePath + "thats_reassuring_btn.png"));
            textureMap.put("reassureButtonHover", new Texture(endGamePath + "thats_reassuring_btn_hover.png"));
            textureMap.put("winText", new Texture(endGamePath + "win_modal_text.png"));

            // Gameplay Interface Textures
            String statusBarPath = resourcesPath + "status_bars/";
            // Health bar
            textureMap.put("healthBarContainer", new Texture(statusBarPath + "healthBackgroundUpdated.png"));
            textureMap.put("healthBar", new Texture(statusBarPath + "healthInnerUpdated.png"));
            // Energy Bar
            textureMap.put("energyBarContainer", new Texture(statusBarPath + "energyOuter.png"));
            textureMap.put("energyBar", new Texture(statusBarPath + "energyInner.png"));
            // Raid Bar
            textureMap.put("raidBarContainer", new Texture(statusBarPath + "enemyRemainingBackground.png"));
            textureMap.put("raidBar", new Texture(statusBarPath + "enemyRemainingInner.png"));
            textureMap.put("placeholderRaidBar", new Texture(statusBarPath + "enemyRemainingUpdated.png"));

            // Exp bar
            textureMap.put("expBarContainer", new Texture(statusBarPath + "expBackground2.png"));
            textureMap.put("expBar", new Texture(statusBarPath + "expInner.png"));

            // Inventory textures
            String inventoryPath = resourcesPath + "Sprites/Inventory/";
            textureMap.put("craftingGrid", new Texture(inventoryPath + "crafting box.png"));
            textureMap.put("testInvSlot", new Texture("resources/test inv slot.png"));
            textureMap.put("invOneLine", new Texture(inventoryPath + "invenOneLine.png"));
            textureMap.put("invGrid", new Texture(inventoryPath + "invenEx1.png"));
            textureMap.put("invDesBackground", new Texture(inventoryPath + "invDesBackground.png"));
            textureMap.put("star", new Texture(inventoryPath + "testStar.png"));

            // Item textures (all textures that have not been created yet will just be
            String placeholderResourcePath = inventoryPath + "Diamond_Glow-01.png";
            Texture placeholderTexture = new Texture(placeholderResourcePath);
            textureMap.put("empty", placeholderTexture);
            textureMap.put("stone", new Texture(inventoryPath + "InventoryC1Stone.png"));
            textureMap.put("copper", new Texture(inventoryPath + "InventoryC2Copper.png"));
            textureMap.put("bronze", new Texture(inventoryPath + "InventoryC3Bronze.png"));
            textureMap.put("nickel", new Texture(inventoryPath + "InventoryC4Nickel.png"));
            textureMap.put("coconut", new Texture(inventoryPath + "InventoryC5Coconut.png"));
            textureMap.put("potato", new Texture(inventoryPath + "InventoryC6Potato.png"));
            textureMap.put("oxygen", new Texture(inventoryPath + "InventoryC7Oxygen.png"));
            textureMap.put("wood", new Texture(inventoryPath + "InventoryC8Wood.png"));
            textureMap.put("roots", new Texture(inventoryPath + "InventoryC9Root.png"));
            textureMap.put("iron", new Texture(inventoryPath + "InventoryU1Iron.png"));
            textureMap.put("concrete", new Texture(inventoryPath + "InventoryU2Concrete.png"));
            textureMap.put("rope", new Texture(inventoryPath + "InventoryU3Rope.png"));
            textureMap.put("cola", new Texture(inventoryPath + "InventoryU4Cola.png"));
            textureMap.put("medkit", new Texture(inventoryPath + "medical_kit.png"));
            textureMap.put("mentos", new Texture(inventoryPath + "InventoryU5Mentos.png"));
            textureMap.put("spudgun", new Texture(inventoryPath + "InventoryU6SpudGun.png"));
            textureMap.put("steel", new Texture(inventoryPath + "InventoryR1Steel.png"));
            textureMap.put("silver", new Texture(inventoryPath + "InventoryR2Silver.png"));
            textureMap.put("moonrocks", new Texture(inventoryPath + "InventoryR3Moonrock.png"));
            textureMap.put("redstone", new Texture(inventoryPath + "InventoryR4RedStone.png"));
            textureMap.put("spikedmace", new Texture(inventoryPath + "InventoryR5SpikedMace.png"));
            textureMap.put("gold", new Texture(inventoryPath + "InventoryE1Gold.png"));
            textureMap.put("titanium", new Texture(inventoryPath + "InventoryE2Titanium.png"));
            textureMap.put("unobtanium", new Texture(inventoryPath + "Unobtainium-01.png"));
            textureMap.put("reinforcedconcrete", new Texture(inventoryPath + "InventoryE4ReinforcedConcrete.png"));
            textureMap.put("grenade", new Texture(inventoryPath + "InventoryE5Grenade.png"));
            textureMap.put("plasma", new Texture(inventoryPath + "InventoryE6Plasma.png"));
            textureMap.put("plutonium", new Texture(inventoryPath + "InventoryL1Plutonium.png"));
            textureMap.put("uranium", new Texture(inventoryPath + "InventoryL2Uranium.png"));
            textureMap.put("diamond", new Texture(inventoryPath + "Diamond-01.png"));
            textureMap.put("alientech", new Texture(inventoryPath + "InventoryL4AlienTech.png"));
            textureMap.put("gammastone", new Texture(inventoryPath + "InventoryL5GammaStone.png"));

            // Item textures with glows
            textureMap.put("stone-glow", new Texture(inventoryPath + "InventoryC1Stone-glow.png"));
            textureMap.put("copper-glow", new Texture(inventoryPath + "InventoryC2Copper-glow.png"));
            textureMap.put("bronze-glow", new Texture(inventoryPath + "InventoryC3Bronze-glow.png"));
            textureMap.put("nickel-glow", new Texture(inventoryPath + "InventoryC4Nickel-glow.png"));
            textureMap.put("coconut-glow", new Texture(inventoryPath + "InventoryC5Coconut-glow.png"));
            textureMap.put("potato-glow", new Texture(inventoryPath + "InventoryC6Potato-glow.png"));
            textureMap.put("oxygen-glow", new Texture(inventoryPath + "InventoryC7Oxygen-glow.png"));
            textureMap.put("wood-glow", new Texture(inventoryPath + "InventoryC8Wood-glow.png"));
            textureMap.put("roots-glow", new Texture(inventoryPath + "InventoryC9Root-glow.png"));
            textureMap.put("iron-glow", new Texture(inventoryPath + "InventoryU1Iron-glow.png"));
            textureMap.put("concrete-glow", new Texture(inventoryPath + "InventoryU2concrete-glow.png"));
            textureMap.put("rope-glow", new Texture(inventoryPath + "InventoryU3Rope-glow.png"));
            textureMap.put("cola-glow", new Texture(inventoryPath + "InventoryU4Cola-glow.png"));
            textureMap.put("mentos-glow", new Texture(inventoryPath + "InventoryU5Mentos-glow.png"));
            textureMap.put("spudgun-glow", new Texture(inventoryPath + "InventoryU6SpudGun-glow.png"));
            textureMap.put("steel-glow", new Texture(inventoryPath + "InventoryR1Steel-glow.png"));
            textureMap.put("silver-glow", new Texture(inventoryPath + "InventoryR2Silver-glow.png"));
            textureMap.put("moonrocks-glow", new Texture(inventoryPath + "InventoryR3Moonrock-glow.png"));
            textureMap.put("redstone-glow", new Texture(inventoryPath + "InventoryR4RedStone-glow.png"));
            textureMap.put("spikedmace-glow", new Texture(inventoryPath + "InventoryR5SpikedMace-glow.png"));
            textureMap.put("gold-glow", new Texture(inventoryPath + "InventoryE1Gold-glow.png"));
            textureMap.put("titanium-glow", new Texture(inventoryPath + "InventoryE2Titanium-glow.png"));
            textureMap.put("unobtanium-glow", new Texture(inventoryPath + "Unobtainium_Glow-01.png"));
            textureMap.put("reinforcedconcrete-glow", new Texture(inventoryPath + "InventoryE4ReinforcedConcrete-glow.png"));
            textureMap.put("grenade-glow", new Texture(inventoryPath + "InventoryE5Grenade-glow.png"));
            textureMap.put("plasma-glow", new Texture(inventoryPath + "InventoryE6Plasma-glow.png"));
            textureMap.put("plutonium-glow", new Texture(inventoryPath + "InventoryL1Plutonium-glow.png"));
            textureMap.put("uranium-glow", new Texture(inventoryPath + "InventoryL2Uranium-glow.png"));
            textureMap.put("diamond-glow", new Texture(inventoryPath + "Diamond_Glow-01.png"));
            textureMap.put("alientech-glow", new Texture(inventoryPath + "InventoryL4AlienTech-glow.png"));
            textureMap.put("gammastone-glow", new Texture(inventoryPath + "InventoryL5GammaStone-glow.png"));

            // textures for the com tower conquer display
            textureMap.put("conquest0", new Texture("resources/conquest_display/conquest0.png"));
            textureMap.put("conquest12", new Texture("resources/conquest_display/conquest12.png"));
            textureMap.put("conquest25", new Texture("resources/conquest_display/conquest25.png"));
            textureMap.put("conquest37", new Texture("resources/conquest_display/conquest37.png"));
            textureMap.put("conquest50", new Texture("resources/conquest_display/conquest50.png"));
            textureMap.put("conquest62", new Texture("resources/conquest_display/conquest62.png"));
            textureMap.put("conquest75", new Texture("resources/conquest_display/conquest75.png"));
            textureMap.put("conquest87", new Texture("resources/conquest_display/conquest87.png"));
            textureMap.put("conquest100", new Texture("resources/conquest_display/conquest100.png"));

            // Textures for the day night cycle
            textureMap.put("dayIcon", new Texture("resources/day_night_icon/sun_icon.png"));
            textureMap.put("nightIcon", new Texture("resources/day_night_icon/moon_icon.png"));
            textureMap.put("nightBackground", new Texture("resources/day_night_icon/night.png"));
            textureMap.put("sunsetIcon", new Texture("resources/day_night_icon/sunset_icon.png"));

            // Texture atlases for animations
            atlasMap.put("testFrames", new TextureAtlas("resources/PlayerCharacter/Soldier/Soldier_Frames.atlas"));
            atlasMap.put("soldierAtlas", new TextureAtlas("resources/PlayerCharacter/Soldier/Soldier_Frames.atlas"));
            atlasMap.put("explorerAtlas", new TextureAtlas("resources/PlayerCharacter/Explorer/Explorer_Frames.atlas"));
            atlasMap.put("engineerAtlas", new TextureAtlas("resources/PlayerCharacter/Engineer/Engineer_Frames.atlas"));

            // Texture for impact indicator
            textureMap.put("enemyHit", new Texture("resources/enemies/hit.png"));

            // texture for frozen icon
            textureMap.put("frozen", new Texture("resources/towers/freezeIcon.png"));

            // texture for heal ring
            textureMap.put("healRing", new Texture("resources/enemies/HealRing.png"));

            // Textures for skill icons
            String skillTreePath = resourcesPath + "SkillTreeIcon/";
            textureMap.put("attackDamage1", new Texture(skillTreePath + "Offence/Damage-I.png"));
            textureMap.put("attackDamage2", new Texture(skillTreePath + "Offence/Damage-II.png"));
            textureMap.put("attackDamage3", new Texture(skillTreePath + "Offence/Damage-III.png"));
            textureMap.put("attackDamage4", new Texture(skillTreePath + "Offence/Damage-IV.png"));

            textureMap.put("attackMortalReminder", new Texture(skillTreePath + "Offence/Mortal-Reminder.png"));
            textureMap.put("attackDoubleEdged", new Texture(skillTreePath + "Offence/Double-Edged.png"));
            textureMap.put("attackExplosion", new Texture(skillTreePath + "Offence/Explosion.png"));
            textureMap.put("attackFireTouch", new Texture(skillTreePath + "Offence/Fire-Touch.png"));
            textureMap.put("attackFlameTrail", new Texture(skillTreePath + "Offence/Flame-Trail.png"));
            textureMap.put("attackMoreExplosion", new Texture(skillTreePath + "Offence/More-Explosion.png"));
            textureMap.put("defenceHealth1", new Texture(skillTreePath + "Defence/Health-I.png"));
            textureMap.put("defenceHealth2", new Texture(skillTreePath + "Defence/Health-II.png"));
            textureMap.put("defenceHealth3", new Texture(skillTreePath + "Defence/Health-III.png"));
            textureMap.put("defenceHealth4", new Texture(skillTreePath + "Defence/Health-IV.png"));
            textureMap.put("defenceAdvancedHealing", new Texture(skillTreePath + "Defence/Advanced-Healing.png"));
            textureMap.put("defenceApothecary", new Texture(skillTreePath + "Defence/Apothecary.png"));
            textureMap.put("defenceUnkillable", new Texture(skillTreePath + "Defence/unkillable.png"));
            textureMap.put("defenceThickArmour", new Texture(skillTreePath + "Defence/Thick-Armour.png"));
            textureMap.put("defenceThornArmour", new Texture(skillTreePath + "Defence/Thorn-Armour.png"));
            textureMap.put("defenceGuardian", new Texture(skillTreePath + "Defence/Guardian.png"));
            textureMap.put("utilityTowerUpgrade1", new Texture(skillTreePath + "Engineering/Tower-Upgrade-I.png"));
            textureMap.put("utilityTowerUpgrade2", new Texture(skillTreePath + "Engineering/Tower-Upgrade-II.png"));
            textureMap.put("utilityTowerUpgrade3", new Texture(skillTreePath + "Engineering/Tower-Upgrade-III.png"));
            textureMap.put("utilityTowerUpgrade4", new Texture(skillTreePath + "Engineering/Tower-Upgrade-IV.png"));
            textureMap.put("utilityEfficiency", new Texture(skillTreePath + "Engineering/Efficiency.png"));
            textureMap.put("utilityEndlessItems", new Texture(skillTreePath + "Engineering/Endless-Items.png"));
            textureMap.put("utilityFortune", new Texture(skillTreePath + "Engineering/Fortune.png"));
            textureMap.put("utilityShootFaster", new Texture(skillTreePath + "Engineering/Shoot-Faster.png"));
            textureMap.put("utilityLaserBullets", new Texture(skillTreePath + "Engineering/Laser-Bullet.png"));
            textureMap.put("utilityStickyBullets", new Texture(skillTreePath + "Engineering/Sticky-Bullets.png"));
            // background for skill tree ui
            textureMap.put("skillsBackground", new Texture("resources/SkillTree_BG/skill_tree_bg_brown_solid_3.jpg"));
            // Locked skill icon
            textureMap.put("lockedSkill", new Texture("resources/SkillTreeIcon/Locked Skill.png"));

            textureMap.put("levelTexture", new Texture(buttonsPath + "levelTexture.png"));
            textureMap.put("levelTexture2", new Texture(buttonsPath + "levelTexture2.png"));

            // Biome resource textures
            String environmentPath = resourcesPath + "Sprites/Environment/day/";
            // Desert
            textureMap.put("environment-desert_0",
                    new Texture(environmentPath + "environment-desert_0.png"));
            textureMap.put("environment-desert_1",
                    new Texture(environmentPath + "environment-desert_1.png"));
            textureMap.put("environment-desert_2",
                    new Texture(environmentPath + "environment-desert_2.png"));
            textureMap.put("environment-desert_3",
                    new Texture(environmentPath + "environment-desert_3.png"));
            textureMap.put("environment-desert-plant_0",
                    new Texture(environmentPath + "environment-desert-plant_0.png"));
            textureMap.put("environment-desert-plant-1",
                    new Texture(environmentPath + "environment-desert-plant_1.png"));

            // Grassland
            textureMap.put("environment-grassland_0",
                    new Texture(environmentPath + "environment-grassland_0.png"));
            textureMap.put("environment-grassland_1",
                    new Texture(environmentPath + "environment-grassland_1.png"));
            textureMap.put("environment-grassland_2",
                    new Texture(environmentPath + "environment-grassland_2.png"));
            textureMap.put("environment-grassland_3",
                    new Texture(environmentPath + "environment-grassland_3.png"));

            // Mountain Rocks
            textureMap.put("environment-mountainrocks_0",
                    new Texture(environmentPath + "environment-mountainrocks_0.png"));

            // Rainforest
            textureMap.put("environment-rainforest-plant_0",
                    new Texture(environmentPath + "environment-rainforest-plant_0.png"));
            textureMap.put("environment-rainforest-plant_1",
                    new Texture(environmentPath + "environment-rainforest-plant_1.png"));
            textureMap.put("environment-rainforest-rocks-0",
                    new Texture(environmentPath + "environment-rainforest-rock_0.png"));
            textureMap.put("environment-rainforest-rock_1",
                    new Texture(environmentPath + "environment-rainforest-rock_1.png"));
            textureMap.put("environment-rainforest-tree_0",
                    new Texture(environmentPath + "environment-rainforest-tree_0.png"));
            textureMap.put("environment-rainforest-tree_1",
                    new Texture(environmentPath + "environment-rainforest-tree_1.png"));

            // Snow
            textureMap.put("environment-snow_0",
                    new Texture(environmentPath + "environment-snow_0.png"));
            textureMap.put("environment-snow_1",
                    new Texture(environmentPath + "environment-snow_1.png"));
            textureMap.put("environment-snow_2",
                    new Texture(environmentPath + "environment-snow_2.png"));
            textureMap.put("environment-snow_3",
                    new Texture(environmentPath + "environment-snow_3.png"));

        } catch (Exception e) {
            log.error(e.toString());
        }
        ready = true;
        log.info("Finished setting up texture manager");
    }

    /**
     * Loads enemies into the textureMap
     * Avoids the redundancies of
     */
    private void addEnemyTextures() {
        String[] enemies = new String[] {
            "Basic_Lizard_Desert",
            "Basic_Lizard_Forest",
            "Basic_Lizard_Plains",
            "Basic_Lizard_Snow",
            "Gun_Lizard_Desert",
            "Gun_Lizard_Forest",
            "Gun_Lizard_Plains",
            "Gun_Lizard_Snow",
            "Heavy_Lizard_Desert",
            "Heavy_Lizard_Forest",
            "Heavy_Lizard_Plains",
            "Heavy_Lizard_Snow",
            "King_Lizard",
            "Ranger_Lizard_Forest",
            "STRanger_Lizard_Forest",
            "Scorpion",
            "Snake_Desert",
            "Snow_Monster",
            "Snowman_Snow",
            "Spider_Desert",
            "Spider_Forest",
            "Spider_Plains",
            "Spider_Snow",
            "Spider_Explode_Desert",
            "Spider_Explode_Forest",
            "Spider_Explode_Plains",
            "Spider_Explode_Snow",
            "Spider_Queen",
            "Spider_Small_Desert",
            "Spider_Small_Forest",
            "Spider_Small_Plains",
            "Spider_Small_Snow",
            "Spider_Vehicle",
            "Tree_Lizard_Forest",
            "Wasp_Desert",
            "Wasp_Forest",
            "Wasp_Plains",
            "Wasp_Snow",
            "Wizard_Lizard_Desert",
            "Wizard_Lizard_Forest",
            "Wizard_Lizard_Plains",
            "Wizard_Lizard_Snow",
            "Guardian_Lizard"
        };

        String[] directions = new String[] {"FL", "FW", "FR", "BL", "BW", "BR"};

        for (String enemy : enemies) {
            for (String direction : directions) {
                textureMap.put(enemy + direction, new Texture("resources/enemies/" + enemy + direction + ".png"));
            }
        }
    }

    /**
     * Gets a texture object for a given string id
     *
     * @param id Texture identifier
     * @return Texture for given id
     */
    public Texture getTexture(String id) {
        if (textureMap.containsKey(id)) {
            return textureMap.get(id);
        } else {
            // log.info("Texture map does not contain {}, returning default texture.", id);
            return textureMap.get("spacman_ded");
        }
    }

    /**
     * Gets a texture object for a given string id
     *
     * @param id Texture identifier
     * @return Texture for given id or null if the id is not exist
     */
    public Texture strictGetTexture(String id) {
        if (id != null && textureMap.containsKey(id)) {
            return textureMap.get(id);
        } else {
            return null;
        }
    }

    /**
     * Checks whether or not a texture is available.
     *
     * @param id Texture identifier
     * @return If texture is available or not.
     */
    public boolean hasTexture(String id) {
        return textureMap.containsKey(id);
    }

    /**
     * Saves a texture with a given id
     *
     * @param id       Texture id
     * @param filename Filename within the assets folder
     */
    public void saveTexture(String id, String filename) {
        if (!textureMap.containsKey(id)) {
            textureMap.put(id, new Texture(filename));
        }
    }

    /**
     * Gets a texture atlas object for a given string id
     *
     * @param id Texture atlas identifier
     * @return Texture atlas for given id or null if the id does not exist
     */
    public TextureAtlas getAtlas(String id) {
        while (!ready) {
            log.info("Waiting for texture manager to finish init");
            // wait for the texture manager to finish init
        }

        if (atlasMap.containsKey(id)) {
            return atlasMap.get(id);
        } else {
            log.warn("Atlas map doesn't have {}, returning null", id);
            return null;
        }
    }

}
