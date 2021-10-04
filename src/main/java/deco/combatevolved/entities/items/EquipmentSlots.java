package deco.combatevolved.entities.items;

import deco.combatevolved.entities.dynamicentities.PlayerAttributes;

public class EquipmentSlots extends Inventory {

    //The player who use this equipment slots
    private PlayerAttributes player;
    //The index of the weapon stack
    private int weaponIndex = 0;
    //The index of the helmet stack
    private int helmetIndex = 1;
    //The index of the armour stack
    private int armourIndex = 2;
    //The index of the shoes stack
    private int shoesIndex = 3;
    //The slot index that will be edit, will set by setType() function
    private int editIndex = -1;
    //The weapon attack pattern
    private String pattern;

    /**
     * Initialize a new EquipmentSlots
     *
     * @param player The player who will use this equipment slots
     */
    public EquipmentSlots(PlayerAttributes player) {
        super(4);
        this.player = player;
    }

    /**
     * Get the player's weapon
     *
     * @return the player's weapon
     */
    public Equipment getWeapon() {
        Inventory.Stack equipmentStack = this.getStack(weaponIndex);
        if (equipmentStack != null){
            return (Equipment) equipmentStack.getItem();
        } else {
            return null;
        }
    }

    /**
     * Get the player's helmet
     *
     * @return the player's helmet
     */
    public Equipment getHelmet() {
        Inventory.Stack equipmentStack = this.getStack(helmetIndex);
        if (equipmentStack != null){
            return (Equipment) equipmentStack.getItem();
        } else {
            return null;
        }
    }

    /**
     * Get the player's shoes
     *
     * @return the player's shoes
     */
    public Equipment getShoes() {
        Inventory.Stack equipmentStack = this.getStack(shoesIndex);
        if (equipmentStack != null){
            return (Equipment) equipmentStack.getItem();
        } else {
            return null;
        }
    }

    /**
     * Get the player's armour
     *
     * @return the player's armour
     */
    public Equipment getArmour() {
        Inventory.Stack equipmentStack = this.getStack(armourIndex);
        if (equipmentStack != null){
            return (Equipment) equipmentStack.getItem();
        } else {
            return null;
        }
    }
    
    /**
     * Get a specific player equipment
     *
     * @param index the index related to the edit index
     * @return the player's specified equipment
     */
    public Equipment getEquipment(int index) {
        Inventory.Stack equipmentStack = this.getStack(index);
        if (equipmentStack != null){
            return (Equipment) equipmentStack.getItem();
        } else {
            return null;
        }
    }

    /**
     * equip an equipment from inventory
     *
     * @param inventory the inventory that store the equipment that will be equipped
     * @param equipment the equipment that will be equipped
     * @return true if the equipment is equipped successful, otherwise false
     */
    public boolean equip(Inventory inventory, Equipment equipment) {
        int inventoryItemNum = inventory.getItemsNum(equipment);
        //check equipment exist in the inventory
        if(inventoryItemNum >= 1 && setType(equipment)) {
            inventory.removeItem(equipment,1);
            if (editIndex != -1 && this.getStack(editIndex) != null && !unequip(equipment.getType(), inventory)){
                inventory.addItem(equipment,1);
                return false;
            }
            if (createSlot(equipment, editIndex)) {
                addAttributes(equipment);
                if(editIndex == 0) {
                    player.setAttackPattern(pattern);
                }
                return true;
            } else {
                inventory.addItem(equipment,1);
                return false;
            }
           
        }
        return false;
    }

    /**
     * equip an equipment by providing the equipment
     *
     * @param inventory the inventory that will store the replaced equipment
     * @param equipment the equipment that will be equipped
     * @return true if the equipment is equipped successful, otherwise false
     */
    public boolean uncheck_equip(Inventory inventory, Equipment equipment) {
            //set equipment type
            if (setType(equipment)) {
                if (editIndex != -1 && this.getStack(editIndex) != null && !unequip(equipment.getType(), inventory)){
                	return false;             
                }
                if (createSlot(equipment, editIndex)) {
                    addAttributes(equipment);
                    if(editIndex == 0) {
                        player.setAttackPattern(player.getDefaultAttackPattern());
                    }
                    return true;
                }
            }
            return false;
    }

    /**
     * unequip an equipment by provide equipment type that want to unequip
     *
     * @param type the equipment type that want to unequip
     * @return true if the equipment is unequipped successful, otherwise false
     */
    public boolean unequip(String type, Inventory inventory) {
        if(setType(type) && editIndex != -1 && this.getStack(editIndex) != null && inventory.addItem(this.getStack(editIndex).getItem(),1) == 0) {
            removeAttributes((Equipment) this.getStack(editIndex).getItem());
            if(editIndex == 0) {
                player.setAttackPattern(pattern);
            }
            this.removeStack(editIndex);
            return true;
        }
        return false;
    }

    /**
     * unequip an equipment by provide equipment that want to unequip
     *
     * @param equipment the equipment that want to unequip
     * @return true if the equipment is unequipped successful, otherwise false
     */
    public boolean unequip(Equipment equipment, Inventory inventory) {
        if (setType(equipment)) {
            if (this.getStack(editIndex) == null){
                return true;
            } else if (this.getStack(editIndex).getItem().equals(equipment) && 
        		inventory.addItem(this.getStack(editIndex).getItem(),1) == 0) {
                removeAttributes((Equipment) this.getStack(editIndex).getItem());
                if(editIndex == 0) {
                    player.setAttackPattern(player.getDefaultAttackPattern());
                }
                this.removeStack(editIndex);
                return true;
            }
        }
        return false;
    }
    
    /**
     * unequip an equipment by provide equipment that want to unequip
     *
     * @param index the equipment index related to the editIndex that want to unequip
     */
    public void unequip(int index) {
        removeAttributes((Equipment) this.getStack(index).getItem());
        if(editIndex == 0) {
            player.setAttackPattern(player.getDefaultAttackPattern());
        }
        this.removeStack(index);
    }

    /**
     * set the slot type that will be edit by providing type
     *
     * @param type the slot type that will be edit
     * @return true if the type is set successful, otherwise false
     */
    private boolean setType(String type) {
        this.editIndex = -1;
        type = type.toLowerCase();
        getType(type);
        return (editIndex != -1);
    }


    /**
     * set the slot type that will be edit by providing equipment
     *
     * @param equipment the equipment's type that will be edit
     * @return true if the type is set successful, otherwise false
     */
    private boolean setType(Equipment equipment) {
        String type = equipment.getType();
        this.editIndex = -1;
        getType(type);
        return (editIndex != -1);
    }
    
    /**
     * Gets the slot type that will be edited by providing equipment
     * 
     * @param type the type of slot
     */
    private void getType(String type) {
    	switch (type) {
        case "weapon":
            editIndex = weaponIndex;
            break;
        case "gun":
            editIndex = weaponIndex;
            pattern = "bullet";
            break;
        case "grenade":
            editIndex = weaponIndex;
            pattern = "projectile";
            break;
        case "helmet":
            editIndex = helmetIndex;
            break;
        case "armour":
            editIndex = armourIndex;
            break;
        case "shoes":
            editIndex = shoesIndex;
            break;
        default:
        	break;
    }
    }

    /**
     * create equipment slot stack
     *
     * @param equipment the equipment that will be equipped
     * @return true if input is created successfully, otherwise false
     */
    private boolean createSlot(Equipment equipment, int slotIndex) {
        if (this.getStack(slotIndex) == null) {
            this.createStack(equipment, slotIndex, 1, 1, true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * add equipment Attributes to the player
     *
     * @param equipment the equipment that will add its attributes
     */
    private void addAttributes(Equipment equipment) {
        player.statsChange(equipment.getAttack(),"attack");
        player.statsChange(equipment.getDefence(),"defence");
        player.statsChange(equipment.getSpeed(),"speed");
        player.statsChange(equipment.getHealth(),"health");
    }

    /**
     * remove equipment Attributes to the player
     *
     * @param equipment the equipment that will add its attributes
     */
    private void removeAttributes(Equipment equipment) {
        player.statsChange((-1*equipment.getAttack()),"attack");
        player.statsChange((-1*equipment.getDefence()),"defence");
        player.statsChange((-1*equipment.getSpeed()),"speed");
        player.statsChange((-1*equipment.getHealth()),"health");
    }


}
