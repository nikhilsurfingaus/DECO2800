package deco.combatevolved.entities.items;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class InventoryTest {

    static Item item = new Item("item", 1);
    static Item item2 = new Item("item2", 2);
    static Item item3 = new Item("item3", 3);

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorUnderCapacity() {
        new Inventory(0);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorOverCapacity() {
        new Inventory(65);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testGetInvalidStack() {
        Inventory inventory = new Inventory(1);
        assertNull(inventory.getStack(0));
        inventory.getStack(2);
    }

    @Test
    public void testStackCount() {
        Inventory inventory = new Inventory(2);
        assertEquals(0, inventory.stackCount());
        inventory.addItem(item);
        assertEquals(1, inventory.stackCount());
    }

    @Test
    public void testCapacity() {
        assertEquals(2, new Inventory(2).capacity());
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void getNegativeStack() {
        Inventory inventory = new Inventory(1);
        inventory.getStack(-1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getInvalidStack() {
        Inventory inventory = new Inventory(1);
        inventory.getStack(1);
    }

    @Test
    public void testGetItemsNum() {
        Inventory inventory = new Inventory(5);
        assertEquals(0, inventory.addItemAtIndex(item,1,7));
        assertEquals(0, inventory.addItemAtIndex(item,3,3));
        assertEquals(0, inventory.addItemAtIndex(item,4,8));
        assertEquals(18, inventory.getItemsNum(item));
    }

    public static class TestAddItem {

        Inventory inventory;

        @Before
        public void before() {
            inventory = new Inventory(5);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testAddNullItem() {
            inventory.addItem(null);
        }
        
        @Test (expected = IllegalArgumentException.class)
        public void testAddMultipleNullItem() {
            inventory.addItem(null, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testNegativeAddMulitpleItems() {
            inventory.addItem(item, -1);
        }
        
        @Test
        public void testAddItem() {
            assertEquals(0, inventory.addItem(item));
            // check first stack has 1x item
            assertEquals(item, inventory.getStack(0).getItem());
            assertEquals(1, inventory.getStack(0).getNumItems());
            assertEquals(1, inventory.stackCount());
            // add a second item
            assertEquals(0, inventory.addItem(item));
            assertEquals(2, inventory.getStack(0).getNumItems());
            // check second stack is empty
            assertNull(inventory.getStack(1));
        }

        @Test
        public void testAddItemOverflow() {
            assertEquals(0, inventory.addItem(item, 65));
            // check first stack is at capacity
            assertEquals(item, inventory.getStack(0).getItem());
            assertEquals(64, inventory.getStack(0).getNumItems());
            // check second stack holds overflow
            assertEquals(item, inventory.getStack(1).getItem());
            assertEquals(1, inventory.getStack(1).getNumItems());
            // add another type of item
            assertEquals(0, inventory.addItem(item2, 64));
            // check third stack
            assertEquals(item2, inventory.getStack(2).getItem());
            assertEquals(64, inventory.getStack(2).getNumItems());
            // add a item to have overflow
            assertEquals(0, inventory.addItem(item2));
            // check fourth stack holds overflow
            assertEquals(item2, inventory.getStack(3).getItem());
            assertEquals(1, inventory.getStack(3).getNumItems());
        }

        @Test
        public void testAddItemToFullInventory() {
            for (int i = 0; i < inventory.capacity(); i++) {
                assertEquals(0, inventory.addItemToEmpty(new Item("item" + i, 1)));
            }
            assertEquals(5, inventory.stackCount());
            assertEquals(1, inventory.addItem(new Item("item6", 1)));
        }

        @Test 
        public void testAddMultipleItems() {
            assertEquals(0, inventory.addItem(item, 65));
            assertEquals(2, inventory.stackCount());
            assertEquals(item, inventory.getStack(0).getItem());
            assertEquals(64, inventory.getStack(0).getNumItems());
            assertEquals(item, inventory.getStack(1).getItem());
            assertEquals(1, inventory.getStack(1).getNumItems());
            assertEquals(0, inventory.addItem(item, 2));
            assertEquals(3, inventory.getStack(1).getNumItems());
        }

        @Test
        public void testAddMultipleItemsOverCapacity() {
            assertEquals(1, inventory.addItem(item, 64 * 5 + 1));
        }

    }

    public static class TestAddItemToEmpty {

        Inventory inventory;

        @Before
        public void before() {
            inventory = new Inventory(3);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testAddNullItem() {
            inventory.addItemToEmpty(null);
        }

        @Test
        public void testAddItemToFull() {
            for (int i = 0; i < inventory.capacity(); i++) {
                assertEquals(0, inventory.addItem(new Item("item" + i, 1)));
            }
            assertEquals(inventory.capacity(), inventory.stackCount());
            assertEquals(1, inventory.addItemToEmpty(item));
        }

    }

    public static class TestAddItemAtIndex {

        Inventory inventory;

        @Before
        public void before() {
            inventory = new Inventory(3);
        }
        
        @Test (expected = IllegalArgumentException.class)
        public void testAddNullItemAtIndex() {
            inventory.addItemAtIndex(null, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testAddItemNegativeIndex() {
            inventory.addItemAtIndex(item, -1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testAddItemInvalidIndex() {
            inventory.addItemAtIndex(item, inventory.capacity());
        }

        @Test
        public void testAddItemAtIndex() {
            assertEquals(0, inventory.addItemAtIndex(item, 1));
            assertNull(inventory.getStack(0));
            assertEquals(item, inventory.getStack(1).getItem());
            assertNull(inventory.getStack(2));
            assertEquals(1, inventory.stackCount());
        }
        
        @Test
        public void testAddItemAtIndexThenAddItem() {
        	assertEquals(0, inventory.addItemAtIndex(item, 0));
        	assertEquals(0, inventory.addItemAtIndex(item2, 2));
            assertEquals(0, inventory.addItem(item2));
            assertEquals(2, inventory.stackCount());
            assertNull(inventory.getStack(1));
        }

        @Test
        public void testAddItemAtIndexOverCapacity() {
            for (int i = 0; i < inventory.capacity(); i++) {
                assertEquals(0, inventory.addItem(new Item("item" + i, 1)));
            }
            assertEquals(3, inventory.stackCount());

            assertEquals(1, inventory.addItemAtIndex(item, 0));
        }

        @Test (expected = IllegalArgumentException.class)
        public void testAddMultipleNullItemsAtIndex() {
            inventory.addItemAtIndex(null, 1, 1);
        }
        
        @Test (expected = IllegalArgumentException.class)
        public void testAddMultipleItemsAtNegativeIndex() {
            inventory.addItemAtIndex(item, -1, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testAddMultipleItemsAtInvalidIndex() {
            inventory.addItemAtIndex(item, 3, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testAddNegativeItemsAtIndex() {
            inventory.addItemAtIndex(item, 0, -1);
        }
        

        @Test
        public void testAddMultipleItemsAtIndex() {
            assertEquals(1, inventory.addItemAtIndex(item, 1, 65));
            assertEquals(1, inventory.stackCount());
            assertNull(inventory.getStack(0));
            assertNull(inventory.getStack(2));
        }

    }

    public static class TestRemoveItem {

        Inventory inventory;

        @Before
        public void before() {
            inventory = new Inventory(3);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveNull() {
            inventory.removeItem(null);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveMultipleNull() {
            inventory.removeItem(null, 2);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveNegative() {
            inventory.removeItem(item, -1);
        }

        @Test
        public void testRemoveItem() {
            assertEquals(0, inventory.addItemAtIndex(item, 1));
            assertEquals(0, inventory.removeItem(item));
            assertNull(inventory.getStack(1));
        }

        @Test
        public void testRemoveNoItem() {
            assertEquals(1, inventory.removeItem(item));
        }

        @Test
        public void testRemoveItemsOverflow() {
            assertEquals(0, inventory.addItem(item, 65));
            assertEquals(64, inventory.getStack(0).getNumItems());
            assertEquals(1, inventory.getStack(1).getNumItems());
            assertEquals(1, inventory.removeItem(item, 66));
        }

        @Test
        public void testRemoveMultipleItems() {
            assertEquals(0, inventory.addItemAtIndex(item, 1, 5));
            assertEquals(0, inventory.removeItem(item, 2));
            assertEquals(3, inventory.getStack(1).getNumItems());
        }

    }

    public static class TestRemoveItemAtIndex {

        Inventory inventory;

        @Before
        public void before() {
            inventory = new Inventory(3);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveNullAtIndex() {
            inventory.removeItemAtIndex(null, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveMultipleNullAtIndex() {
            inventory.removeItemAtIndex(null, 1, 2);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveItemAtNegativeIndex() {
            inventory.removeItemAtIndex(item, -1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveItemAtInvalidIndex() {
            inventory.removeItemAtIndex(item, 4);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveMultipleItemsAtNegativeIndex() {
            inventory.removeItemAtIndex(item, -1, 2);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveMultipleItemsAtInvalidIndex() {
            inventory.removeItemAtIndex(item, 4, 2);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testRemoveNegativeItemsAtInvalidIndex() {
            inventory.removeItemAtIndex(item, 2, -1);
        }

        @Test
        public void testRemoveItemAtIndex() {
            assertEquals(1, inventory.removeItemAtIndex(item, 1));
            assertEquals(0, inventory.addItemAtIndex(item, 1));
            assertEquals(1, inventory.getStack(1).getNumItems());
            assertEquals(0, inventory.removeItemAtIndex(item, 1, 1));
            assertNull(inventory.getStack(1));
        }

        @Test
        public void testRemoveMultipleItemsAtIndex() {
            assertEquals(0, inventory.addItemAtIndex(item, 1, 5));
            assertEquals(5, inventory.getStack(1).getNumItems());
            assertEquals(0, inventory.removeItemAtIndex(item, 1, 2));
            assertEquals(3, inventory.getStack(1).getNumItems());
        }

        @Test
        public void testRemoveMultipleItemsAtNonmatchingStack() {
            assertEquals(0, inventory.addItemAtIndex(item2, 1));
            assertEquals(1, inventory.stackCount());
            assertEquals(2, inventory.removeItemAtIndex(item3, 1, 2));
        }

    }

    public static class TestTransfer {

        Inventory inventoryA, inventoryB;

        @Before
        public void before() {
            inventoryA = new Inventory(2);
            inventoryB = new Inventory(3);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testNullInventory() {
            inventoryA.transfer(null);
        }

        @Test
        public void testTransferSameItemMultipleStack() {
            assertEquals(0, inventoryA.addItem(item, 65));
            assertEquals(2, inventoryA.stackCount());
            assertTrue(inventoryA.transfer(inventoryB));
            assertEquals(0, inventoryA.stackCount());
            assertEquals(2, inventoryB.stackCount());
        }

        @Test
        public void testTransferDifferentItems() {
            assertEquals(0, inventoryA.addItemAtIndex(item2, 0));
            assertEquals(0, inventoryA.addItemAtIndex(item3, 1));
            assertEquals(2, inventoryA.stackCount());
            assertTrue(inventoryA.transfer(inventoryB));
            assertEquals(0, inventoryA.stackCount());
            assertEquals(2, inventoryB.stackCount());
            assertEquals(item2, inventoryB.getStack(0).getItem());
            assertEquals(item3, inventoryB.getStack(1).getItem());
        }

        @Test
        public void testTransferToFullInventory() {
            assertEquals(0, inventoryA.addItem(item3));
            assertEquals(0, inventoryB.addItem(item));
            assertEquals(0, inventoryA.addItem(item2));
            assertFalse(inventoryB.transfer(inventoryA));
        }

    }

    public static class TestTransferItem {

        Inventory inventoryA;
        Inventory inventoryB;

        @Before
        public void before() {
            inventoryA = new Inventory(2);
            inventoryB = new Inventory(3);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testNullInventory() {
            inventoryA.transferItem(null, 1, 1, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testNegativeFrom() {
            inventoryA.transferItem(inventoryB, -1, 1, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testInvalidFrom() {
            inventoryA.transferItem(inventoryB, 65, 1, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testNegativeTo() {
            inventoryA.transferItem(inventoryB, 1, -1, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testInvalidTo() {
            inventoryA.transferItem(inventoryB, 1, 65, 1);
        }

        @Test (expected = IllegalArgumentException.class)
        public void testNegativeItemCount() {
            inventoryA.transferItem(inventoryB, 1, 1, -1);
        }

        @Test
        public void testNoItemToTransfer() {
            assertEquals(1, inventoryA.transferItem(inventoryB, 1, 1, 1));
            assertNull(inventoryB.getStack(1));
        }

        @Test
        public void testTransferItem() {
            assertEquals(0, inventoryA.addItemAtIndex(item, 0));
            assertEquals(0, inventoryA.transferItem(inventoryB, 0, 1, 1));
            assertNull(inventoryA.getStack(0));
            assertEquals(item, inventoryB.getStack(1).getItem());
            assertEquals(1, inventoryB.stackCount());
        }

        @Test
        public void testTransferSlotNotMatched() {
            assertEquals(0, inventoryA.addItemAtIndex(item2, 1, 2));
            assertEquals(0, inventoryB.addItemAtIndex(item3, 1));
            assertEquals(2, inventoryA.transferItem(inventoryB, 1, 1, 4));
        }

        @Test
        public void testTransferSlotHasCapacity() {
            assertEquals(0, inventoryA.addItemAtIndex(item, 1, 5));
            assertEquals(0, inventoryB.addItemAtIndex(item, 2, 5));
            assertEquals(0, inventoryA.transferItem(inventoryB, 1, 2, 2));
        }

    }
    
    public static class SetEmpty {

        Inventory inventory;

        @Before
        public void before() {
            inventory = new Inventory(3);
            inventory.addItemAtIndex(item, 1);
        }
        
        @Test (expected = IllegalArgumentException.class)
        public void testNegativeEmpty() {
        	inventory.removeStack(-1);
        }
        
        @Test (expected = IllegalArgumentException.class)
        public void testOverEmpty() {
        	inventory.removeStack(3);
        }
        
        @Test 
        public void testEmpty() {
        	assertEquals(item, inventory.getStack(1).getItem());
        	assertEquals(1, inventory.stackCount());
        	inventory.removeStack(1);
        	assertNull(inventory.getStack(1));
        	assertEquals(0, inventory.stackCount());
        	inventory.removeStack(1);
        	assertEquals(0, inventory.stackCount());
        }
    }
}