package deco.combatevolved.util;


import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Input.*;
import static com.badlogic.gdx.Input.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Keybindings {

    private HashMap<String,Integer> defaultKeyBindings= new HashMap<>();

    private HashMap<String,Integer> currentKeyBindings= new HashMap<>();

    private final Logger logger = LoggerFactory.getLogger(Keybindings.class);

    /**
     * Assign keybindings upon initialisation. These will become the default and current keybinding settings.
     */
    public Keybindings() {
        //Mouse buttons
        setKey("FireBullet", Buttons.LEFT);  //GameScreen
        setKey("PlaceTower", Buttons.LEFT);  //GameScreen
        setKey("FireProjectile", Buttons.LEFT);  //GameScreen
        setKey("DragCamera", Buttons.RIGHT);  //CameraManager
        setKey("SelectItemStack", Buttons.LEFT); //InventoryRenderer
        setKey("SelectSingleItem", Buttons.RIGHT);  //InventoryRenderer
        setKey("CraftingInterface", Buttons.RIGHT); //TODO -to be located

        //Movement key bindings
        setKey("MoveUp", Keys.W); //PlayerPeon
        setKey("MoveDown", Keys.S); //PlayerPeon
        setKey("MoveLeft", Keys.A); //PlayerPeon
        setKey("MoveRight", Keys.D); //PlayerPeon
        setKey("Sprint", Keys.SHIFT_LEFT); //PlayerPeon

        //Interaction Keybindings
        setKey("Vehicle", Keys.Q); //PlayerPeon
        setKey("Inventory", Keys.I); //GameScreen
        setKey("RecipeBook", Keys.R); //GameScreen
        setKey("TowerInventory", Keys.U); //GameScreen
        setKey("Crafting", Keys.E); //GameScreen
        setKey("CraftItem", Keys.ENTER); //GameScreen - not sure what it does though
        setKey("SkillTree", Keys.K);  //GameScreen
        setKey("StashInventory", Keys.P); //InventoryRenderer

        setKey("SwitchAttack", Keys.ALT_LEFT); //GameScreen - not really in use right now
        setKey("ShootUp", Keys.UP); //Bullet
        setKey("ShootDown", Keys.DOWN); //Bullet
        setKey("ShootLeft", Keys.LEFT); //Bullet
        setKey("ShootRight", Keys.RIGHT); //Bullet
        setKey("ShootDiagonally", Keys.SHIFT_RIGHT); //Bullet

        //Gamefunction Keybindings
        setKey("DebugInfo", Keys.F12); //GameScreen
        setKey("ReloadWorld", Keys.F5); //GameScreen
        setKey("TileCoords", Keys.F11); //GameScreen
        setKey("ShowPath", Keys.F10); //GameScreen
        setKey("EndGame", Keys.F9);  //GameScreen
        setKey("SaveWorld", Keys.F3); //GameScreen
        setKey("LoadWorld", Keys.F4); //GameScreen
        setKey("GameMenu", Keys.ESCAPE); //GameScreen
        setKey("Chat", Keys.T); //TODO -to be located

        setKey("ZoomIn", Keys.EQUALS); //CameraManager
        setKey("ZoomOut", Keys.MINUS); //CameraManager
        setKey("LockCamera", Keys.F1); //CameraManager
        setKey("CentreCamera", Keys.F2); //CameraManager

        //Set the current keybindings to default
        setDefaultKeyBindings();
    }

    /**
     * Gets the input value (the keycode) of the key that is assigned to the function that the string refers to.
     *
     * @param fname the name of the function/action that a key/button is assigned to.
     * @return the integer keycode of the key that is assigned. Returns -1 if fname has no match to the stored values.
     */
    public int getKey(String fname){
        try {
            return currentKeyBindings.get(fname);
        }catch (Exception e){
            logger.warn("No key matching '" + fname + "'.");
        }
        return -1;
    }

    /**
     * Gets the value (the keycode) of the default key that was assigned to the function that the string refers to.
     *
     * @param fname the String name of the function/action that a key/button is assigned to.
     * @return the integer keycode of the default key that is assigned. Returns -1 if fname has no match to the stored
     * values.
     */

    public int getDefaultKey(String fname){
        try {
            return defaultKeyBindings.get(fname);
        }catch (Exception e){
            logger.warn("No default key matching '" + fname + "'.");
        }
        return -1;
    }

    /**
     * Sets a new key/button to a fuction/action to allow users to change their keybinding preferences during gameplay.
     *
     * @param fname the String name of the function/action to be changed
     * @param keycode the integer representing the key/button that is being assigned
     */
    public void setKey(String fname, int keycode){
        currentKeyBindings.put(fname, keycode);
    }

    /**
     * Sets the current key bindings to the default key bindings
     */
    public void setDefaultKeyBindings(){
        for (Map.Entry<String, Integer> entry : currentKeyBindings.entrySet())
        {
            defaultKeyBindings.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Restores a function/action to its default key/button
     * @param fname the String name of the function/action. Does nothing if fmane has no match to the stored values.
     */
    public void restoreDefaultKey(String fname){
        try {
            currentKeyBindings.replace(fname, getDefaultKey(fname));
        }catch (Exception e){
            logger.warn("No default key matching '" + fname + "'.");
        }
    }

    /**
     * Restores all functions/actions to their default keys/buttons
     */
    public void restoreAllDefaults(){
        for (Map.Entry<String, Integer> entry : defaultKeyBindings.entrySet())
        {
            currentKeyBindings.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Gets the current keybindings
     * @return HashMap of current keybindings containing strings of function names and the keycode integers of the
     * keys/buttons
     */
    public HashMap<String,Integer> getCurrentKeyBindings(){
        return currentKeyBindings;
    }

    /**
     * Gets the default keybindings
     * @return HashMap of default keybindings containing strings of function names and the keycode integers of the
     * keys/buttons
     */
    public HashMap<String, Integer> getDefaultKeyBindings(){
        return defaultKeyBindings;
    }
}
