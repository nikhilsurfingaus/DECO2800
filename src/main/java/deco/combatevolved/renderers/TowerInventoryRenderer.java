package deco.combatevolved.renderers;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TowerInventoryRenderer extends JFrame {
    // Display the tower inventory
    private boolean displayed = false;

    private JPanel panel = new JPanel();
    private JButton closeBut = new JButton();
    private JButton simpleBut = new JButton();
    private JButton splashBut = new JButton();
    private JButton sniperBut = new JButton();
    private JButton zapBut = new JButton();
    private JButton multiBut = new JButton();
    private JButton slowBut = new JButton();

    /**
     * Constructor to initialise the tower inventory
     */
    public TowerInventoryRenderer() {
        initComponent();
    }

    /**
     * Set up components of tower inventory, creating towers as button and add
     * action listeners to buttons
     */
    public void initComponent() {
        setBackground(Color.DARK_GRAY);
        setSize(900, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setOpacity(0.96f);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setShape(new RoundRectangle2D.Double(0, 0, 900, 300, 80, 80));
        setType(javax.swing.JFrame.Type.UTILITY);
        panel.setLayout(null);
        add(panel);

        closeBut.setBounds(850, 13, 30, 30);
        ImageIcon close = new ImageIcon("resources/towers/x button.png");
        setImage(panel, closeBut, close);

        simpleBut.setToolTipText("<html>Single Shooter" +
                "<br> Name: Railgun" +
                "<br> Damage: 5" +
                "<br> ROF: 2/sec" +
                "<br> Range: 2 hex on all sides" +
                "<br> Health: 200 hits" +
                "<br> Cost: 18 Wood, 12 Stone, 6 Copper" + "</html>");
        simpleBut.setBounds(40, 100, 91, 100);
        ImageIcon towerOne = new ImageIcon("resources/towers/simpletower.png");
        setImage(panel, simpleBut, towerOne);

        splashBut.setToolTipText("<html>Splash Shooter" +
                "<br> Name: Omega Thermal Cannon" +
                "<br> Damage: 10" +
                "<br> ROF: 1/3sec" +
                "<br> Range: 4 hex on all sides" +
                "<br> Health: 250 hits" +
                "<br> Cost: 22 Wood, 16 Stone, 9 Iron" + "</html>");
        splashBut.setBounds(185, 100, 110, 100);
        ImageIcon towerTwo = new ImageIcon("resources/towers/splashtower.png");
        setImage(panel, splashBut, towerTwo);

        sniperBut.setToolTipText("<html>Sniper Gun" +
                "<br> Name: Gauss Cannon" +
                "<br> Damage: 20" +
                "<br> ROF: 1/sec" +
                "<br> Range: 8 hex on all sides" +
                "<br> Health: 280 hits" +
                "<br> Cost: 22 Stone, 14 Iron, 3 Steel" + "</html>");
        sniperBut.setBounds(330, 100, 91, 108);
        ImageIcon towerThree = new ImageIcon("resources/towers/snipertower.png");
        setImage(panel, sniperBut, towerThree);

        zapBut.setToolTipText("<html>Zap Gun" +
                "<br> Damage: 20" +
                "<br> ROF: 1/sec" +
                "<br> Range: 8 hex on all sides" +
                "<br> Health: 280 hits" +
                "<br> Cost: 22 Stone, 14 Iron, 3 Steel" + "</html>");
        zapBut.setBounds(475, 100, 91, 115);
        ImageIcon towerFour = new ImageIcon("resources/towers/zaptower_n.png");
        setImage(panel, zapBut, towerFour);

        multiBut.setToolTipText("<html>Multi Gun" +
                "<br> Damage: 20" +
                "<br> ROF: 1/sec" +
                "<br> Range: 8 hex on all sides" +
                "<br> Health: 280 hits" +
                "<br> Cost: 22 Stone, 14 Iron, 3 Steel" + "</html>");
        multiBut.setBounds(621, 100, 105, 100);
        ImageIcon towerFive = new ImageIcon("resources/towers/multitower.png");
        setImage(panel, multiBut, towerFive);

        slowBut.setToolTipText("<html>Slime Gun" +
                "<br> Name: Slime Shooter" +
                "<br> Duration: 7 sec" +
                "<br> Damage: none" +
                "<br> ROF: 1/2sec" +
                "<br> Range: 4 hex on all sides" +
                "<br> Health: 300 hits" + "</html>");
        slowBut.setBounds(766, 100, 105, 100);
        ImageIcon towerSix = new ImageIcon("resources/towers/slimetower_down.png");
        setImage(panel, slowBut, towerSix);

        setLabel();

        closeBut.addActionListener(e -> toggleTowerInventory());
        simpleBut.addActionListener(e -> toggleTowerInventory());
        splashBut.addActionListener(e -> toggleTowerInventory());
        sniperBut.addActionListener(e -> toggleTowerInventory());
        zapBut.addActionListener(e -> toggleTowerInventory());
        multiBut.addActionListener(e -> toggleTowerInventory());
        slowBut.addActionListener(e -> toggleTowerInventory());
    }

    /**
     * Helper method for tower inventory to set the layout of towers
     *
     * @param panel the frame buttons are on
     * @param button the button being set
     * @param image the image being set
     */
    public void setImage(JPanel panel, JButton button, ImageIcon image) {
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        java.awt.Image closeButImage = image.getImage();
        Image scaled = closeButImage.getScaledInstance(button.getWidth(),
                button.getHeight(), Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaled));
        panel.add(button);
    }

    /**
     * Set the names of each tower and background of tower inventory
     */
    public void setLabel() {
        JLabel labelOne = new JLabel("Towers");
        labelOne.setFont(new Font("TimesRoman", Font.TRUETYPE_FONT, 20));
        labelOne.setForeground(Color.WHITE);
        labelOne.setBounds(420, 0, 100, 50);
        panel.add(labelOne);

        JLabel labelTwo = new JLabel("Simple Tower");
        labelTwo.setFont(new Font("TimesRoman", Font.BOLD, 15));
        labelTwo.setForeground(Color.GRAY);
        labelTwo.setBounds(40, 185, 100, 100);
        panel.add(labelTwo);

        JLabel labelThree = new JLabel("Splash Tower");
        labelThree.setFont(new Font("TimesRoman", Font.BOLD, 15));
        labelThree.setForeground(Color.GRAY);
        labelThree.setBounds(187, 185, 100, 100);
        panel.add(labelThree);

        JLabel labelFour = new JLabel("Sniper Tower");
        labelFour.setFont(new Font("TimesRoman", Font.BOLD, 15));
        labelFour.setForeground(Color.GRAY);
        labelFour.setBounds(330, 185, 100, 100);
        panel.add(labelFour);

        JLabel labelFive = new JLabel("Zap Tower");
        labelFive.setFont(new Font("TimesRoman", Font.BOLD, 15));
        labelFive.setForeground(Color.GRAY);
        labelFive.setBounds(481, 185, 100, 100);
        panel.add(labelFive);

        JLabel labelSix = new JLabel("Multi Tower");
        labelSix.setFont(new Font("TimesRoman", Font.BOLD, 15));
        labelSix.setForeground(Color.GRAY);
        labelSix.setBounds(626, 185, 100, 100);
        panel.add(labelSix);

        JLabel labelSeven = new JLabel("Slow Tower");
        labelSeven.setFont(new Font("TimesRoman", Font.BOLD, 15));
        labelSeven.setForeground(Color.GRAY);
        labelSeven.setBounds(771, 185, 100, 100);
        panel.add(labelSeven);

        JLabel background = new JLabel(new ImageIcon("resources/towers/towerbackground.png"));
        background.setLayout(new FlowLayout());
        background.setBounds(0, 0, 900, 350);
        panel.add(background);
    }

    /**
     * Toggles whether the tower inventory is being displayed
     */
    public void toggleTowerInventory() {
        displayed = !displayed;
        setVisible(displayed);
    }

    /**
     * Indicates if the tower inventory is currently open
     * @return whether the tower inventory is currently open
     */
    public boolean isDisplayed() {
        return displayed;
    }

    /**
     * @return Simple tower button
     */
    public JButton getSimpleBut() {
        return simpleBut;
    }

    /**
     * @return Splash tower button
     */
    public JButton getSplashBut() {
        return splashBut;
    }

    /**
     * @return Sniper tower button
     */
    public JButton getSniperBut() {
        return sniperBut;
    }

    /**
     * @return Zap tower button
     */
    public JButton getZapBut() {
        return zapBut;
    }

    /**
     * @return Multi tower button
     */
    public JButton getMultiBut() {
        return multiBut;
    }

    /**
     * @return Slow tower button
     */
    public JButton getSlowBut() {
        return slowBut;
    }
}
