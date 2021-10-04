package deco.combatevolved.entities.items;

import java.util.*;

public class Equipment extends Item {
	 private IllegalArgumentException equipmentException =
		        new IllegalArgumentException("Didn't find the default setting of the equipment");

    private String type = null;
    // The attributes of the item, will be directly added into the player when it be equipped.
    // Attack of equipment
    private int attack = 0;
    // Defense of equipment
    private int defence = 0;
    // Speed of equipment
    private int speed = 0;
    // Health of equipment
    private int health = 0;

    /**
     * Class constructor
     * Create a equipment by provide its attributes
     *
     * type of the equipment currently have: gun, grenade, helmet, armour, shoes
     *
     * The format of attributes: (String attributeName, String attributeValue)
     * [attributeName] currently support: "attack", "defence" , "speed", "health"
     * [attributeValue] the attribute value will be directly add to the player when it be equipped.
     *                  Can be negative.
     *
     * Construct example:
     *       Map <String,String> attributes = new HashMap<String,String>();
     *       attributes.put("attack", "10");
     *       attributes.put("defence", "1");
     *       equipment sword  =  new Equipment("gun", 2, "gun", "99", "gun_tx", attributes);
     *
     * @param name name of the equipment
     * @param rarity rarity of the equipment
     * @param type type of the equipment
     * @param id ID of item
     * @param texture the texture/sprite of the equipment
     * @param attributes the attributes of the equipment
     */
    public Equipment(String name, int rarity, String type, String id, String texture, String description, Map <String,String> attributes) {
        super(name, rarity, id, texture, description);
        this.type = type;
        parseAttributes(attributes);
    }

    /**
     * Class constructor
     * Create a equipment use default setting
     *
     * type of the equipment currently have: gun, grenade, helmet, armour, shoes
     *
     * Construct example:
     *       new Equipment("gun_low", 1, "gun", "401", "gun_low");
     *
     * The default setting of the equipment can be edit at useDefaultAttributes() function.
     *
     * @param name name of the equipment
     * @param rarity rarity of the equipment
     * @param type type of the equipment
     * @param id ID of item
     * @param texture the texture/sprite of the equipment
     * @throws Exception if the default setting of the equipment not be found
     */
    public Equipment(String name, int rarity, String type, String id, String texture) {
        super(name, rarity, id, texture);
        if(!useDefaultAttributes()) {
            throw equipmentException;
        }
        this.type = type;
    }

    /**
     * get the equipment's type
     *
     * @return the equipment's type
     */
    public String getType() {return this.type.toLowerCase();}

    /**
     * get the equipment's attack
     *
     * @return the equipment's attack
     */
    public int getAttack() {return this.attack;}

    /**
     * get the equipment's defence
     *
     * @return the equipment's defence
     */
    public int getDefence() {return this.defence;}

    /**
     * get the equipment's speed
     *
     * @return the equipment's speed
     */
    public int getSpeed() {return this.speed;}

    /**
     * get the equipment's health
     *
     * @return the equipment's health
     */
    public int getHealth() {return this.health;}

    /**
     * load the default setting of the equipment
     *
     * @return true if default setting of the equipment exist and load successfully, otherwise false
     */
    private boolean useDefaultAttributes() {
        String name = this.getName();
        int rarity = this.getRarity();

        /*
         * Add your default setting of equipment bellow
         *
         * [name] String: name of the item
         * [rarity] int: rarity of the item
         * [attributeName] String: currently support: "attack", "defence" , "speed", "health"
         * [attributeValue] String: the value of the attribute
         *
         */

        if (name.equals("shoes_low") && rarity == 1) {
            this.speed = 1;
            this.defence = 5;
            this.setDescription("Speed+1, Defence+5");
            return true;
        }

        if (name.equals("shoes_mid") && rarity == 2) {
            this.speed = 3;
            this.defence = 10;
            this.setDescription("Speed+3, Defence+10");
            return true;
        }

        if (name.equals("shoes_high") && rarity == 3) {
            this.speed = 5;
            this.defence = 30;
            this.setDescription("Speed+5, Defence+30");
            return true;
        }

        if (name.equals("helmet_low") && rarity == 1) {
            this.defence = 5;
            this.setDescription("Defence+5");
            return true;
        }

        if (name.equals("helmet_mid") && rarity == 2) {
            this.defence = 20;
            this.setDescription("Defence+20");
            return true;
        }

        if (name.equals("helmet_high") && rarity == 3) {
            this.defence = 40;
            this.setDescription("Defence+40");
            return true;
        }

        if (name.equals("armour_low") && rarity == 1) {
            this.speed = 0;
            this.defence = 10;
            this.setDescription("Defence+10");
            return true;
        }

        if (name.equals("armour_mid") && rarity == 2) {
            this.speed = -1;
            this.defence = 30;
            this.setDescription("Defence+30,Speed-1");
            return true;
        }

        if (name.equals("armour_high") && rarity == 3) {
            this.speed = -2;
            this.defence = 50;
            this.setDescription("Defence+50,Speed-2");
            return true;
        }

        if (name.equals("gun_low") && rarity == 1) {
            this.setDescription("attack pattern: bullet");
            return true;
        }

        if (name.equals("gun_mid") && rarity == 2) {
            this.attack = 30;
            this.setDescription("attack pattern: bullet, attack+30");
            return true;
        }

        if (name.equals("gun_high") && rarity == 3) {
            this.attack = 50;
            this.setDescription("attack pattern: bullet, attack+50");
            return true;
        }
        if (name.equals("grenade_low") && rarity == 1) {
            this.setDescription("attack pattern: projectile");
            return true;
        }

        if (name.equals("grenade_mid") && rarity == 2) {
            this.attack = 30;
            this.setDescription("attack pattern: projectile, attack+30");
            return true;
        }

        if (name.equals("grenade_high") && rarity == 3) {
            this.attack = 50;
            this.setDescription("attack pattern: projectile, attack+50");
            return true;
        }

        //do not delete this setting, used for the functionality test
        if (name.equals("testDefaultSetting") && rarity == 5) {
            this.speed = 1;
            this.health = 10;
            this.setDescription("description test");
            return true;
        }

        return false;
    }

    /**
     * parse the attributes from the equipment's attributes map
     *
     * Map format:
     *       Map <String,String> attributes = new HashMap<String,String>();
     *       attributes.put("attack", "10");
     *       attributes.put("defence", "1");
     *
     * @param attributes the equipment's attributes map
     */
    private void parseAttributes(Map <String,String> attributes) {
        for(Map.Entry<String, String> entry : attributes.entrySet()){
            String mapKey = entry.getKey().toLowerCase();
            String mapValue = entry.getValue();
            switch (mapKey) {
                case "attack":
                    this.attack = Integer.parseInt(mapValue);
                    break;
                case "defence":
                    this.defence = Integer.parseInt(mapValue);
                    break;
                case "speed":
                    this.speed = Integer.parseInt(mapValue);
                    break;
                case "health":
                    this.health = Integer.parseInt(mapValue);
                    break;
                default:
        			break;
            }
        }
    }
}
