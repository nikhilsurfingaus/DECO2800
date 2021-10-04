package deco.combatevolved.entities.staticentities;

import deco.combatevolved.renderers.TowerInventoryRenderer;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class TowerInventoryRendererTest {
    private TowerInventoryRenderer tower;
    private JPanel panel;
    private JButton simpleBut, splashBut, sniperBut, zapBut, multiBut, slowBut;

    // @Before
    public void setup() {
        tower = new TowerInventoryRenderer();
        panel = new JPanel();
        simpleBut = new JButton();
        splashBut = new JButton();
        sniperBut = new JButton();
        zapBut = new JButton();
        multiBut = new JButton();
        slowBut = new JButton();
    }

    // @Test
    public void initComponent() {
        assertNotNull(tower);
        int width = tower.getWidth();
        int height = tower.getHeight();
        assertEquals(900, width);
        assertEquals(350, height);

        simpleBut = tower.getSimpleBut();
        String simpleTxt = simpleBut.getToolTipText();
        int simpleX = simpleBut.getX();
        int simpleY = simpleBut.getY();
        int simpleWidth = simpleBut.getWidth();
        int simpleHeight = simpleBut.getHeight();
        assertEquals("<html>Single Shooter" +
                "<br> Name: Railgun" +
                "<br> Damage: 5" +
                "<br> ROF: 2/sec" +
                "<br> Range: 2 hex on all sides" +
                "<br> Health: 200 hits" +
                "<br> Cost: 18 Wood, 12 Stone, 6 Copper" + "</html>", simpleTxt);
        assertEquals(40, simpleX);
        assertEquals(100, simpleY);
        assertEquals(91, simpleWidth);
        assertEquals(100, simpleHeight);

        splashBut = tower.getSplashBut();
        String splashTxt = splashBut.getToolTipText();
        int splashX = splashBut.getX();
        int splashY = splashBut.getY();
        int splashWidth = splashBut.getWidth();
        int splashHeight = splashBut.getHeight();
        assertEquals("<html>Splash Shooter" +
                "<br> Name: Omega Thermal Cannon" +
                "<br> Damage: 10" +
                "<br> ROF: 1/3sec" +
                "<br> Range: 4 hex on all sides" +
                "<br> Health: 250 hits" +
                "<br> Cost: 22 Wood, 16 Stone, 9 Iron" + "</html>", splashTxt);
        assertEquals(185, splashX);
        assertEquals(100, splashY);
        assertEquals(110, splashWidth);
        assertEquals(100, splashHeight);
    }


}
