package deco.combatevolved.entities.items;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StackTest {

	Inventory inventory = new Inventory(1);
	Item item = new Item("test", 1);

	@Test
	public void testConstructor() {
		Inventory.Stack stack = inventory.new Stack(item);
		assertEquals(item, stack.getItem());
		assertEquals(1, stack.getNumItems());
	}

	@Test (expected = IllegalArgumentException.class)
	public void testOverloadConstructorNullItem() {
		inventory.new Stack(null, 1, 1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testOverloadConstructorNegativeStack() {
		inventory.new Stack(item, 1, -1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testOverloadConstructorNegativeItems() {
		inventory.new Stack(item, -1, 1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testOverloadConstructorTooManyItems() {
		inventory.new Stack(item, 65, 1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testNullConstructor() {
		inventory.new Stack(null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testNullMultiItemConstructor() {
		inventory.new Stack(null, 1);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testOverMaxSize() {
		inventory.new Stack(item, 65);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testNonPositiveSize() {
		inventory.new Stack(item, 0);
	}
	
	@Test 
	public void testMaxSize() {
		Inventory.Stack stack = inventory.new Stack(item, 64);
		assertEquals(item, stack.getItem());
		assertEquals(64, stack.getNumItems());
	}
	
	@Test
	public void testGetNumItems() {
		Inventory.Stack stack = inventory.new Stack(item, 20);
		assertEquals(20, stack.getNumItems());
	}
	
	@Test 
	public void testSetNumItems() {
		Inventory.Stack stack = inventory.new Stack(item, 1);
		assertEquals(item, stack.getItem());
		assertEquals(1, stack.getNumItems());
		stack.setNumItems(20);
		assertEquals(item, stack.getItem());
		assertEquals(20, stack.getNumItems());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetNumItemsOverMax() {
		Inventory.Stack stack = inventory.new Stack(item, 1);
		assertEquals(item, stack.getItem());
		assertEquals(1, stack.getNumItems());
		stack.setNumItems(65);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testSetNumItemsNonPositive() {
		Inventory.Stack stack = inventory.new Stack(item, 1);
		assertEquals(item, stack.getItem());
		assertEquals(1, stack.getNumItems());
		stack.setNumItems(0);
	}
	
}
