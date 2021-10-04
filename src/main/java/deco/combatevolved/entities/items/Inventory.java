package deco.combatevolved.entities.items;

import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.entities.dynamicentities.SkillTreev2;
import deco.combatevolved.managers.GameManager;

import java.util.ArrayList;
import java.util.Random;

public class Inventory {

    public class Stack {

        //Default maximum stack size
        private static final int DEFAULT_STACK_SIZE = 64;
    	//Maximum number of same items in a stack
        private int capacity;
        //Number of same items in the stack
        private int numItems;
        //Item in the stack
        private Item item;
    
        /**
         * Initialize a new stack with a single item
         * 
         * @param item item to dedicate stack to         
         * @throws IllegalArgumentException if the item is null
         */
        public Stack(Item item) {
            if (item == null) {
                throw itemException;
            }

            this.capacity = DEFAULT_STACK_SIZE;
            this.item = item;
            this.numItems = 1;
        }

        /**
         * Initialize a new stack with multiple items
         * 
         * @param item item to dedicate stack to
         * @param numItems number of items to start stack with
         * @throws IllegalArgumentException if item is null
         * @throws IllegalArgumentException if the number of items is greater than the maximum stack size or non-positive or item is null
         */
        public Stack(Item item, int numItems) {
            if (item == null) {
                throw itemException;
            }

            if (numItems <= 0 || numItems > DEFAULT_STACK_SIZE) {
                throw stackItemException;
            }

            this.capacity = DEFAULT_STACK_SIZE;
            this.item = item;
            this.numItems = numItems;
        }

        /**
         * Initialize a new stack with multiple items and set the MAX_STACK_SIZE
         *
         * @param item item to dedicate stack to
         * @param numItems number of items to start stack with
         * @param stackSize the maximum number of same items in this stack
         * @throws IllegalArgumentException if item is null
         * @throws IllegalArgumentException if stack capacity is less than 1
         * @throws IllegalArgumentException if the number of items is greater than the maximum stack size or non-positive or item is null
         */
        public Stack(Item item, int numItems, int stackSize) {
            if (item == null) {
                throw itemException;
            }

            if (stackSize <= 0) {
                throw stackCapacityException;
            }

            if (numItems <= 0 || numItems > stackSize) {
                throw stackItemException;
            }
            
            this.capacity = stackSize;
            this.item = item;
            this.numItems = numItems;
        }
        
        /**
         * Get the item in the stack
         * 
         * @return The item in stack
         */
        public Item getItem() {
        	return this.item;
        }
        
        /**
         * Get the number of item in the stack
         * 
         * @return The number of items in stack
         */
        public int getNumItems() {
        	return this.numItems;
        }
        
        /**
         * Set the number of items in stack
         * 
         * @param numItems number of items in stack
         * @throws IllegalArgumentException if the number of items is greater than the maximum stack size or non-positive
         */
        public void setNumItems(int numItems) {
        	if (numItems > capacity || numItems <= 0) {
                throw stackItemException;
            }
        	this.numItems = numItems;
        }

        public int capacity() {
            return capacity;
        }

        /**
         * return the boolean value whether the stack is full
         *
         * @return true if the stack is full or false otherwise
         */
        public boolean isFull() {
            return this.numItems >= capacity;
        }
        
    }

    private Stack[] stacks;

    private int numStacks;
    
    private int capacity;

    private static final int MAX_SLOT_COUNT = 64;

    private IllegalArgumentException itemException =
        new IllegalArgumentException("Item cannot be null");

    private IllegalArgumentException inventoryIndexException = 
        new IllegalArgumentException("Index out of bounds, index must be within inventory capacity");

    private IllegalArgumentException stackItemException = 
        new IllegalArgumentException("Invalid number of items, items must be within stack capacity");

    private IllegalArgumentException stackCapacityException = 
        new IllegalArgumentException("Invalid capacity, capacity must be greater than 0");

    private IllegalArgumentException negativeCountException = 
        new IllegalArgumentException("Invalid number of items, must be greater than 0");

    private IllegalArgumentException inventoryException =
        new IllegalArgumentException("Inventory cannot be null");

    // Random number
    private Random rand = new Random();

    /**
     * Initialize inventory with given capacity
     *
     * @param capacity maximum number of slots this inventory is capable of storing
     * @throws IllegalArgumentException if invalid capacity
     */
    public Inventory(int capacity) {
        if (capacity <= 0 || capacity > MAX_SLOT_COUNT) {
            throw new IllegalArgumentException("Capacity must be between 1 and 64 inclusive");
        }
        
        this.capacity = capacity;
        this.stacks = new Stack[this.capacity];
        this.numStacks = 0;
    }

    /**
     * Get number of item stack slots occupied in this inventory object
     * 
     * @return number of item stacks in inventory
     */
    public int stackCount() {
        return this.numStacks;
    }

    /**
     * Get capacity of this inventory (i.e. maximum number of slots)
     * 
     * @return maximum number of slots this inventory is capable of storing
     */
    public int capacity() {
        return this.capacity;
    }

    /**
     * Finds all stacks holding matching items
     *
     * @param item item to search stacks for
     * @return A list of index of all stacks found with a matching item
     */
    private ArrayList<Integer> findItemStacks(Item item) {
        ArrayList<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < this.capacity; i++) {
            if (this.stacks[i] != null && this.stacks[i].getItem().equals(item)) {
                indexList.add(i);
            }
        }

        return indexList;
    }

    /**
     * Get the total number of a item in inventory
     *
     * @param item item to search for its number in the inventory
     * @return The total number of a item in inventory
     */
    public int getItemsNum(Item item) {
        int numberOfItem = 0;
        ArrayList<Integer> indexList = findItemStacks(item);
        for (int index:indexList) {
            numberOfItem += getStack(index).getNumItems();
        }
        return numberOfItem;
    }

    /**
     * Finds first empty slot
     *
     * @return index of first empty slot found with a matching item, or -1 if not found
     */
    private int findFirstEmptySlot() {
        int index = -1;
        for (int i = 0; i < this.capacity; i++) {
            if (this.stacks[i] == null) {
                index = i;
                return index;
            }
        }
        return index;
    }

    /**
     * Fills existing stack with items until full or no items left
     * 
     * @param numItems number of items to add
     * @param index index of stack in inventory to add to
     * @return number of remaining items after adding to stack
     */
    private int fillStack(Item item, int index, int numItems) {
        int remainingItems = numItems;
        if (this.stacks[index] == null) {
            this.stacks[index] = new Stack(item);
            this.numStacks++;
            remainingItems--;
        }

        if (this.stacks[index].getItem().equals(item)) {
            int freeSpace = this.stacks[index].capacity() - this.stacks[index].getNumItems();
            if (freeSpace < 1) {
                return remainingItems;
            }
    
            if (remainingItems - freeSpace > 0) {
                this.stacks[index].setNumItems(this.stacks[index].capacity());
                return remainingItems - freeSpace;
            } else {
                this.stacks[index].setNumItems(this.stacks[index].getNumItems() + remainingItems);
                return 0;
            }

        }

        return remainingItems;
    }

    /**
     * create a stack with items and set stack's size
     *
     * @param item the item that will be added into the new stack
     * @param index index of stack in inventory to create to
     * @param numItems number of items to add
     * @param stackSize the size of the stack
     * @param clean true: if the stack at the index is already exist, clean it and create the new stack;
     *              false: if the stack at the index is already exist, stop create the new stack;
     * @return true if the stack create successfully, false otherwise
     */
    public boolean createStack(Item item, int index, int numItems, int stackSize, boolean clean) {
        if (clean) {
            this.stacks[index] = null;
        }
        if (this.stacks[index] == null && numItems > 0 && stackSize >= numItems) {
            this.stacks[index] = new Stack(item, numItems, stackSize);
            this.numStacks++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Add items to existing stacks of same item
     * If the item not exist in inventory, add it to the first empty slot
     * 
     * @param item item to add to inventory
     * @return 0 if item was added, 1 otherwise
     * @throws IllegalArgumentException if the item is null
     */
    public int addItem(Item item) {
        if (item == null) {
            throw itemException;
        }

        ArrayList<Integer> indexList = findItemStacks(item);
        for (int index:indexList) {
            if (!getStack(index).isFull() && addItemAtIndex(item, index) == 0) {
                return 0;
            }
        }
        int index = findFirstEmptySlot();
        if (index != -1 && addItemAtIndex(item, index) == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Add items to first empty slot even it is already in the inventory
     *
     * @param item item to add to inventory
     * @return 0 if item was added, 1 otherwise
     * @throws IllegalArgumentException if the item is null
     */
    public int addItemToEmpty(Item item) {
        if (item == null) {
            throw itemException;
        }
        int index = findFirstEmptySlot();
        if (index != -1 && addItemAtIndex(item, index) == 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Add items to any existing stacks of same item and/or to first empty slot in inventory
     * 
     * @param item item to add to inventory
     * @param numItems number of given item to add to inventory
     * @return 0 if all items were added, remaining items otherwise
     * @throws IllegalArgumentException if the item is null
     * @throws IllegalArgumentException if the number of items to add to stack is non-positive
     */
    public int addItem(Item item, int numItems) {
        if (item == null) {
            throw itemException;
        }

    	if (numItems < 1) {
            throw negativeCountException;
        }

        int remainingItems = numItems;
        for (int i = 0; i < this.capacity; i++) {
            if (this.stacks[i] != null && this.stacks[i].getItem().equals(item)) {
                remainingItems = fillStack(item, i, remainingItems);

                if (remainingItems == 0) {
                    return 0;
                }
            }
        }

        for (int i = 0; i < this.capacity; i++) {
            remainingItems = fillStack(item, i, remainingItems);

            if (remainingItems == 0) {
                return 0;
            }
        }

        return remainingItems;
    }
    
    /**
     * Add item to specified slot in inventory
     * 
     * @param item item to add to inventory
     * @param index spot to add item
     * @return 0 if item was added, 1 otherwise
     * @throws IllegalArgumentException if the item is null
     * @throws IllegalArgumentException if given index is out of inventory bounds
     */
    public int addItemAtIndex(Item item, int index) {
        if (item == null) {
            throw itemException;
        }

        if (index < 0 || index >= this.capacity) {
            throw inventoryIndexException;
        }

        return fillStack(item, index, 1);
    }
    
    /**
     * Add multiple of same item to specified slot in inventory
     * 
     * @param item item to add to inventory
     * @param index spot to add item
     * @param numItems number of given item to add to inventory
     * @return 0 if items were added, remaining items otherwise
     * @throws IllegalArgumentException if the item is null
     * @throws IllegalArgumentException if given index is out of inventory bounds
     * @throws IllegalArgumentException if the number of items to add to stack is non-positive
     */
    public int addItemAtIndex(Item item, int index, int numItems) {
        if (item == null) {
            throw itemException;
        }

        if (index < 0 || index >= this.capacity) {
            throw inventoryIndexException;
        }

        if (numItems < 1) {
            throw negativeCountException;
        }

        return fillStack(item, index, numItems);
    }
    
    /** 
     * Get item at specified index
     * 
     * @param index index to retrieve item from
     * @return Stack at specified index
     * @throws IllegalArgumentException if index is not within the inventory capacity
     */
    public Stack getStack(int index) {
        if (index < 0 || index >= this.capacity) {
            throw inventoryIndexException;
        }

        return this.stacks[index];
    }

    /**
     * Removes specified number of items from a stack
     * 
     * @param item item to remove from specified stack
     * @param index index of stack to remove from
     * @param numItems number of items to remove
     * @return number of items remaining to remove after removing from specified stack
     */
    private int emptyStack(Item item, int index, int numItems) {
        int toRemove = numItems;
        if (this.stacks[index] == null) {
            return toRemove;
        }

        if (this.stacks[index].getItem().equals(item)) {
            int remainingItems = this.stacks[index].getNumItems() - toRemove;
            if (this.stacks[index].getNumItems() > toRemove) {
                this.stacks[index].setNumItems(remainingItems);
                return 0;
            } else {
                this.stacks[index] = null;
                this.numStacks--;
                return remainingItems * -1;
            }
        }

        return toRemove;
    }

    /**
     * Remove item from inventory
     * Sets slot to null if stack is emptied
     * 
     * @param item item to remove from inventory
     * @return 0 if item was removed, 1 otherwise
     * @throws IllegalArgumentException if the item is null
     */
    public int removeItem(Item item) {
        if (item == null) {
            throw itemException;
        }

        for (int i = 0; i < this.capacity; i++) {
            if (emptyStack(item, i, 1) == 0) {
                return 0;
            }
        }

        return 1;
    }

    /**
     * Remove multiple of same item from inventory
     * Sets slot to null if stack is emptied
     * 
     * @param item item to remove from inventory
     * @return 0 if item was removed, remaining items to remove otherwise
     * @throws IllegalArgumentException if the item is null
     * @throws IllegalArgumentException if the number of items to remove from stack is non-positive
     */
    public int removeItem(Item item, int numItems) {        
        int remainingItems = numItems;
        if (item == null) {
            throw itemException;
        }

        if (numItems < 1) {
            throw negativeCountException;
        }

        for (int i = 0; i < this.capacity; i++) {
            remainingItems = emptyStack(item, i, remainingItems);

            if (remainingItems == 0) {
                return 0;
            }
        }

        return remainingItems;
    }
    
    /**
     * Remove item from specified slot in inventory
     * 
     * @param item item to remove from inventory
     * @param index spot to remove item
     * @return 0 if item was removed, 1 otherwise
     * @throws IllegalArgumentException if the item is null
     * @throws IllegalArgumentException if given index is out of inventory bounds
     */
    public int removeItemAtIndex(Item item, int index) {
        if (item == null) {
            throw itemException;
        }

        if (index < 0 || index >= this.capacity) {
            throw inventoryIndexException;
        }
        
        return emptyStack(item, index, 1);
    }
    
    /**
     * Remove multiple of same items from specified stack slot in inventory
     * 
     * @param item item to remove from inventory
     * @param index spot to remove item
     * @return 0 if item was removed, remaining items to remove otherwise
     * @throws IllegalArgumentException if the item is null
     * @throws IllegalArgumentException if given index is out of inventory bounds
     * @throws IllegalArgumentException if the number of items to remove from stack is non-positive
     */
    public int removeItemAtIndex(Item item, int index, int numItems) {
        if (item == null) {
            throw itemException;
        }

        if (index < 0 || index >= this.capacity) {
            throw inventoryIndexException;
        }

        if (numItems < 1) {
            throw negativeCountException;
        }
        
        return emptyStack(item, index, numItems);
    }

    /**
     * Swap positions of items in inventory
     * @param a index of first item to swap
     * @param b index of second item to swap
     * @throws ArrayIndexOutOfBoundsException
     */
    public void swap(int a, int b) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Transfer all items from this inventory to another
     * 
     * @param inventory inventory to transfer into
     * @return false if not all items were moved
     * @throws IllegalArgumentException if inventory is null
     */
    public boolean transfer(Inventory inventory) {
        if (inventory == null) {
            throw inventoryException;
        }

        for (int i = 0; i < this.capacity; i++) {
            if (this.stacks[i] != null) {
                if (inventory.addItem(this.stacks[i].getItem(), this.stacks[i].getNumItems()) != 0) {
                    return false;
                }
                
                this.stacks[i] = null;
                this.numStacks--;
            }
        }

        return true;
    }
    
    /**
     * Move item from a specific index in one inventory to a specific index in another
     * 
     * @param inventory inventory to move to
     * @param from index of slot to move items from
     * @param to index of slot to move items into
     * @param itemCount number of items to move
     * @return 0 if items were moved, remaining items to move otherwise
     * @throws IllegalArgumentException if inventory is null
     * @throws IllegalArgumentException if either index is not within their respective inventory capacities
     */
    public int transferItem(Inventory inventory, int from, int to, int itemCount) {
        if (inventory == null) {
            throw inventoryException;
        }

        if (from < 0 || from >= this.capacity || to < 0 || to >= inventory.capacity()) {
            throw inventoryIndexException;
        }

        if (itemCount < 1) {
            throw negativeCountException;
        }

        if (this.stacks[from] == null) {
            return itemCount;
        }

        int itemsToMove = Math.min(itemCount, this.stacks[from].getNumItems());

        int remainingItems = itemsToMove;
        if (inventory.getStack(to) == null 
                || inventory.getStack(to).getItem().equals(this.stacks[from].getItem())) {
            remainingItems = inventory.addItemAtIndex(this.stacks[from].getItem(), to, itemsToMove);
            if (remainingItems == 0) {
                if (itemCount >= this.stacks[from].getNumItems()) {
                    this.stacks[from] = null;
                    this.numStacks--;
                } else {
                    this.stacks[from].setNumItems(this.stacks[from].getNumItems() - itemsToMove);
                }
            }
        } 

        return remainingItems;
    }

    public void consumeItem(int index) {
        if (index < 0 || index >= this.capacity) {
            throw inventoryIndexException;
        }
        // Instance of player and skills
        PlayerAttributes player = (PlayerAttributes) GameManager.get().getWorld().
                getEntityById(GameManager.get().getPlayerEntityID());
        SkillTreev2 playerSkills = player.getPlayerSkills();
        boolean learntEndlessItems = false;
        // Checks if player has learnt endless items skill
        if (playerSkills.hasLearnt(playerSkills.getSkill("utilityEndlessItems"))) {
            learntEndlessItems = true;
        }
        // Has 25% chance to retain consumable if player has learnt endless items
        if (this.getStack(index).getNumItems() == 1) {
            if (learntEndlessItems) {
                int random = rand.nextInt(4) + 1; // creates a number between 1 and 4 inclusive
                if (random != 1) {
                    this.removeStack(index);
                }
            } else {
                this.removeStack(index);
            }
        } else {
            if (learntEndlessItems) { // Removes item 75% of the time
                int random = rand.nextInt(4) + 1; // creates a number between 1 and 4 inclusive
                if (random != 1) {
                    this.getStack(index).setNumItems(this.getStack(index).getNumItems() - 1);
                }
            } else {
                this.getStack(index).setNumItems(this.getStack(index).getNumItems() - 1);
            }
        }
    }
    
    
    /**
     * Sets the inventory stack to be null
     * 
     * @param index the index of the stack in the inventory
     * @throws IllegalArgumentException if given index is out of inventory bounds
     */
    public void removeStack(int index) {
    	if (index < 0 || index >= this.capacity) {
            throw inventoryIndexException;
        }
    	if (this.stacks[index] != null){
    		this.stacks[index] = null;
        	this.numStacks--;
    	}
    }
}