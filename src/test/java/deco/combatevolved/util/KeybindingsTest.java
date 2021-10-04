package deco.combatevolved.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static com.badlogic.gdx.Input.*;
import static junit.framework.TestCase.fail;

public class KeybindingsTest{

    private Keybindings controls;

    @Before
    public void setup(){
        controls = new Keybindings();
    }

    @Test
    public void testInitialisation() {

        Assert.assertNotNull(controls);
        Assert.assertNotNull(controls.getCurrentKeyBindings());
        Assert.assertNotNull(controls.getDefaultKeyBindings());

        Assert.assertEquals(controls.getDefaultKeyBindings().size(), controls.getCurrentKeyBindings().size());
    }

    @Test
    public void testGetKey(){
        controls = new Keybindings();

        int key= controls.getKey("MoveUp");
        Assert.assertEquals(Keys.W, key);
        Assert.assertEquals(51,key);
    }

    @Test
    public void testSetKey(){
        controls.setKey("MoveUp", Keys.UP);

        //assert that the current keybinding is different to the default keybinding
        Assert.assertEquals(Keys.UP, controls.getKey("MoveUp"));
        Assert.assertEquals(Keys.W, controls.getDefaultKey("MoveUp"));
    }

    @Test
    public void testRestoreDefaultKey(){

        //assert that the key has been changed
        controls.setKey("MoveUp", Keys.UP);
        Assert.assertEquals(Keys.UP, controls.getKey("MoveUp"));

        //assert that the key has been turned back to default
        controls.restoreDefaultKey("MoveUp");
        Assert.assertEquals(controls.getDefaultKey("MoveUp"), controls.getKey("MoveUp"));
    }

    @Test
    public void testRestoreAllDefaults(){

        controls.setKey("MoveUp", Keys.UP);
        controls.setKey("MoveDown", Keys.DOWN);
        controls.setKey("Sprint", Keys.SPACE);

        //Assert that the keys that were changed have gone back to default
        controls.restoreAllDefaults();
        Assert.assertEquals(Keys.W, controls.getKey("MoveUp"));
        Assert.assertEquals(Keys.S, controls.getKey("MoveDown"));
        Assert.assertEquals(Keys.SHIFT_LEFT, controls.getKey("Sprint"));
    }

    @Test
    public void testExceptionHandlers(){

        try{
            int n = controls.getKey("piklcjdlkfhn");
            Assert.assertEquals(-1,n);

            n = controls.getDefaultKey("ihkshufkh");
            Assert.assertEquals(-1,n);

            controls.restoreDefaultKey("idhfkhfdikh");

        }catch (Exception e){
            fail("Exceptions should have been caught.");
        }






    }
}
