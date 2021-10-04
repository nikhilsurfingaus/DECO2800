package deco.combatevolved.renderers.inventoryrenderer;

import java.util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.utils.Align;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.PlayerAttributes;
import deco.combatevolved.entities.dynamicentities.VehicleEntity;
import deco.combatevolved.entities.items.*;
import deco.combatevolved.entities.items.consumableitems.Consumable;
import deco.combatevolved.entities.items.consumableitems.ActiveItem;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.managers.*;
import deco.combatevolved.observers.KeyDownObserver;
import deco.combatevolved.observers.TouchDownObserver;
import deco.combatevolved.renderers.inventoryrenderer.CraftingWindowRenderer;
import deco.combatevolved.renderers.Renderer;
import deco.combatevolved.worlds.Tile;

public class InventoryRenderer implements Renderer, TouchDownObserver, KeyDownObserver {
	//Texture of individual inventory slot
    static Texture invSlot = GameManager.get().getManager(TextureManager.class).getTexture("testInvSlot");
    //Texture of inventory description background
    private Texture invDesBackground = GameManager.get().getManager(TextureManager.class).getTexture("invDesBackground");
    //Texture of rarity star
    private Texture star = GameManager.get().getManager(TextureManager.class).getTexture("star");
    //Dimensions of item
    private static final int ITEM_DIM = 40;
    //Display the vehicle inventory
    boolean displayStash = false;
    //Player within 1 tile of vehicle
    private boolean withinStashDist;
    //Font
    BitmapFont font = new BitmapFont(Gdx.files.internal("resources/fonts/Arial.fnt"));
    //text
    protected static GlyphLayout text = new GlyphLayout();
    //Transfer disabled warning
    private Dialog dialog = new Dialog("Warning", GameManager.get().getSkin());
    //Player user
    private PlayerAttributes player;
    //Vehicle
    private VehicleEntity vehicle;
    //Inventory of player
    private Inventory playerInventory;
    //Recycle of player
    private Recycle playerRecycle;
    //Equipments of player
    private EquipmentSlots playerEquipments;
    //Inventory of vehicle
    private Inventory stash;
    //Previously hovered inventory
    private Inventory hoverInv;
    //Previously hovered inventory stack index
    private int hoverInd;
    //Time mouse has hovered over same stack
    private long hoverTime;
//    //Path to equip sound
//    private String equipSound = "Sound Effect/EFFECT_equipItem_01_Inactive.mp3";
//    //Path to get item sound
//    private String getSound = "Sound Effect/EFFECT_getItem_02_Inactive.mp3";


    EquipmentRenderer equipmentRenderer;
    RecycleRenderer recycleRenderer;
    StashRenderer stashRenderer;
    PlayerBarRenderer playerBarRenderer;

    OrthographicCamera camera;
    //2D scene graph of game
    Stage stage;
    //Inventory that is selected on click
    Inventory selectedInv = null;
    //Index of the selected stack in the selected inventory
    int selectedIndex = -1;
    //Inventory that can temporarily hold items
    Inventory selected = new Inventory(1);
    // The renderer for the crafting window
    private CraftingWindowRenderer craftingWindowRenderer;

    /**
     * Set initial renderer variables
     */
    public void create () {
        font.getData().setScale(0.5f);
        font.setColor(1, 1, 1, 1);       
        dialog.text("You must be near the vehicle to transfer");
        dialog.button("OK");

    	this.stage = GameManager.get().getStage();
		GameManager.getManagerFromInstance(InputManager.class).addTouchDownListener(this);
        GameManager.getManagerFromInstance(KeyboardManager.class).registerForKeyDown(this);
        findStash();
        
        player = (PlayerAttributes) GameManager.get().getWorld().getEntityById(GameManager.get().getPlayerEntityID());
        playerInventory = player.getInventory();
        playerRecycle = player.getRecycle();
        playerEquipments = player.getEquipmentSlots();

        equipmentRenderer = new EquipmentRenderer(invSlot, font, text, player);
        recycleRenderer = new RecycleRenderer(invSlot, font, text, playerRecycle);
        stashRenderer = new StashRenderer(invSlot, font, text, stash);
        playerBarRenderer = new PlayerBarRenderer(invSlot, font, text, player);
        
    }

    @Override
    /**
     * Handle rendering of inventory
     *
     * @param batch Batch to render onto
     * @param camera The camera
     */
    public void render(SpriteBatch batch, OrthographicCamera camera) {
        withinStashDist = player.distance(vehicle) < 2;
        
        batch.begin();
        this.camera = camera;
        playerBarRenderer.render(batch, camera);
        stashRenderer.render(batch, camera);
        stashRenderer.stashDist(withinStashDist);
        equipmentRenderer.render(batch, camera);
        recycleRenderer.render(batch, camera);
        
        if(Gdx.input.isKeyPressed(player.getControls().getKey("StashInventory"))) {
        	if (withinStashDist) {
            	playerInventory.transfer(stash);
    	        GameManager.get().getManager(SoundManager.class).playSound("Sound Effect/EFFECT_select_01_Inactive.mp3");
        	} else {
            	dialog.show(stage);
    	        GameManager.get().getManager(SoundManager.class).playSound("Sound Effect/EFFECT_select_03_Inactive.mp3");
        	}
        }
        
        if (selected.stackCount() == 1) {
        	renderSelectedItem(batch);
        } else {
        	renderItemDes(batch);
        }

        batch.end();
    }

    /**
     * get status of the displayStash
     *
     * @return  True if the displayStash is opened, false otherwise
     */
    public boolean getDisplayStash() {
        return displayStash;
    }

    /**
     * Toggles the vehicle inventory to be rendered if player is within distance of vehicle
     */
    public void toggleStash() {
        equipmentRenderer.toggleDisplay();
        recycleRenderer.toggleDisplay();
        stashRenderer.toggleDisplay();
        displayStash = !displayStash;

        if(displayStash) {
            GameManager.get().getManager(CameraManager.class).setMouseCameraMove(false);
        } else {
            GameManager.get().getManager(CameraManager.class).setMouseCameraMove(true);
        }
    }

    
    /**
     * Finds a vehicle instance and makes a reference to its stash
     */
    private void findStash() {
        List<AbstractEntity> entities = GameManager.get().getWorld().getSortedEntities();
        for (AbstractEntity entity : entities) {
            if (entity instanceof VehicleEntity) {
            	vehicle = (VehicleEntity) entity;
                stash = ((VehicleEntity) entity).getInventory();
            }
        }
    }


	@Override
	public void notifyKeyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.NUM_1:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(0);
			break;
		case Input.Keys.NUM_2:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(1);
			break;
		case Input.Keys.NUM_3:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(2);
			break;
		case Input.Keys.NUM_4:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(3);
			break;
		case Input.Keys.NUM_5:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(4);
			break;
		case Input.Keys.NUM_6:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(5);
			break;
		case Input.Keys.NUM_7:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(6);
			break;
		case Input.Keys.NUM_8:
//	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			selectStack(7);
			break;
		default:
			break;
		}
	}

	@Override
	public void notifyTouchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 coords = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float[] mouse = new float[] { coords.x, coords.y };
		Vector2 cameraPos = new Vector2(mouse[0], mouse[1]);
		//Left click
		if (button == player.getControls().getKey("SelectItemStack")) {
            if (selectedInv != null) {
                leftMouseCommand(cameraPos);
            } else if (recycleRenderer.convert(cameraPos)) {
                playerRecycle.convert(player);
    	        GameManager.get().getManager(SoundManager.class).playSound("Sound Effect/EFFECT_cashRegister_02_Inactive.mp3");
            } else {
                selectStack(cameraPos, 0);
            }
            return;
        }
        
        // Right click
		if (button == player.getControls().getKey("SelectSingleItem")) {
			if (selectedInv != null ) {
                rightMouseCommand(cameraPos);
                return;
            }
            Inventory clickedInventory = calculateInventory(cameraPos);
            int clickedIndex = calculateIndex(cameraPos, clickedInventory);
            
            if (clickedInventory == stash && !withinStashDist) {
                return;
            }

            if (clickedInventory != null && clickedInventory.getStack(clickedIndex) != null && clickedInventory.getStack(clickedIndex).getItem().isConsumable()) {

                if (clickedInventory.getStack(clickedIndex).getItem() instanceof ActiveItem) {
                    // type casting here is fine since we've checked it's consumable
                    player.addConsumable((ActiveItem)clickedInventory.getStack(clickedIndex).getItem());
                }
                ((Consumable) clickedInventory.getStack(clickedIndex).getItem()).consume();
                clickedInventory.consumeItem(clickedIndex);

                return;
            }
            
            if (clickedInventory != null && clickedInventory.getStack(clickedIndex) != null) {
                selectStack(cameraPos, 1);
            }   
		}
    }
	
	/**
	 * Checks click boundaries when a selected item exists and calls corresponding left click method
	 * 
	 * @param cameraPos The coordinates of the mouse click
	 */
	private void leftMouseCommand(Vector2 cameraPos) {

		if(clickedCrafting(cameraPos)) { // function checks if user clicked inside crafting box
            if (selected.getStack(0) == null) {
                return;
            }
            
            int index = 0;
            // since we know we clicked inside the crafting box, here we grab which box we clicked
            for (int i = 0; i < 9; i++) {
                if (craftingWindowRenderer.getCraftingBoxes().get(i).contains(cameraPos)) {
                    index = i;
                }
            }

            craftingWindowRenderer.addToCraftingTable(selected.getStack(0).getItem(), index);
            selected.removeItem(selected.getStack(0).getItem()); // here
            checkSelectedNull();

        } else {
			Inventory clickedInventory = calculateInventory(cameraPos);
			int clickedIndex = calculateIndex(cameraPos, clickedInventory);
			if (clickedInventory == stash && !withinStashDist) {
				return;
			}
			//Click is outside of any boundaries
			if (clickedInventory == null) {
				handleDropCommand(selected.getStack(0).getNumItems());
			//Click is in same spot as last click
			} else if ((selectedInv == clickedInventory) && (selectedIndex == clickedIndex) 
            		&& (selectedInv.getStack(selectedIndex) != null)) {
				selectStack(cameraPos, 1);
			//Click is in new spot
			} else if (clickedIndex >= 0){
				mergeLeft(clickedInventory, clickedIndex);
			}
		}
	}
	
	/**
	 * Drops the selected Item one tile above player
	 * 
	 * @param numItems number of items to drop
	 */
    private void handleDropCommand(int numItems) {
    	float[] dropPosition = player.getPosTile(player.getCol(), player.getRow() + 1, 0);
        Tile tile = GameManager.get().getWorld().getTile(dropPosition[0], dropPosition[1]);
        if (tile != null && (tile.getParent() instanceof ItemEntity)) {
        	ItemEntity entity = (ItemEntity) tile.getParent();
        	selected.transfer(entity.getInventory());
        } else {
            GameManager.get().getWorld().addEntity(new ItemEntity(tile, selected.getStack(0).getItem(), numItems));
        	selected.removeItem(selected.getStack(0).getItem(), numItems);
        }
        checkSelectedNull();
    }	
	
	/**
	 * Checks click boundaries when a selected item exists and calls corresponding right click method
	 * 
	 * @param cameraPos The coordinates of the mouse click
	 */
	private void rightMouseCommand(Vector2 cameraPos) {
		Inventory clickedInventory = calculateInventory(cameraPos);
		int clickedIndex = calculateIndex(cameraPos, clickedInventory);
		if (clickedInventory == stash && !withinStashDist) {
			return;
		}
		//Click is outside of any boundaries
		if (clickedInventory == null) {
			handleDropCommand(1);
		//Click is in same spot as last click
		} else if ((selectedInv == clickedInventory) && (selectedIndex == clickedIndex) 
        		&& (selectedInv.getStack(selectedIndex) != null)) {
			selectStack(cameraPos, 1);
		//Click is in new spot
		} else if (clickedIndex >= 0) {
			mergeRight(clickedInventory, clickedIndex);
		}
	}
	
	/**
	 * Checks to see if the selected stack is null and if so 
	 * sets the selectedInv and index to their default values
	 */
	private void checkSelectedNull() {
		if (selected.getStack(0) == null) {
			selectedInv = null;
			selectedIndex = -1;
		}
	}
	
	/**
	 * Checks which inventory boundary the click was in
	 * 
	 * @param position The coordinates of the mouse click
	 * @return the inventory clicked or null if no inventory clicked
	 */
	private Inventory calculateInventory(Vector2 position) {
		if (playerBarRenderer.contains(position)) {
			return playerInventory;
		} else if (stashRenderer.contains(position)) {
			return stash;
		} else if (recycleRenderer.contains(position)) {
			return (Inventory) playerRecycle;
		} else if (equipmentRenderer.contains(position)) {
			return (Inventory) playerEquipments;
        }
        return null;
	}
	
	/**
	 * Checks the index of the slot in the clicked inventory
	 * 
	 * @param position The coordinates of the mouse click
	 * @param inventory The inventory clicked
	 * @return the index of the slot in the inventory
	 */
	private int calculateIndex(Vector2 position, Inventory inventory) {
		if (inventory == playerInventory) {
			return playerBarRenderer.clickIndex(position);
		} else if (inventory == stash) {
			return stashRenderer.clickIndex(position);
		} else if (inventory == playerRecycle) {
			return recycleRenderer.clickIndex(position);
		} else if (inventory == playerEquipments) {
			return equipmentRenderer.clickIndex(position);
		}
		return -1;
	}
	
    /**
	 * Sets and stores the selected stack
	 *
	 * @ param cameraPos the camera coordinates that were clicked
	 * @ param remove the number to remove from the clicked stack
     */
    private void selectStack(Vector2 cameraPos, int remove) {
        selectedInv = null;
        Inventory clickedInventory = calculateInventory(cameraPos);
        if (clickedInventory == null) {
            return;
        }
        
		int clickedIndex = calculateIndex(cameraPos, clickedInventory);
		if (clickedInventory == stash && !withinStashDist) {
			return;
        }
        
		if (clickedIndex >= 0) {
			transfer(clickedInventory, clickedIndex, remove);
		}  
    }
    
    /**
   	  * Sets and stores the selected stack with hotkeys
   	  *
   	  * @ param index Index to select in player inventory
      */
    private void selectStack(int index) { 	
    	//Checks to see if an item is already selected
    	if (selected.stackCount() == 1) {
    		selected.transferItem(selectedInv, 0, selectedIndex, selected.getStack(0).getNumItems());
    	}
	    if (playerInventory.getStack(index) != null) {
	   		selectedInv = playerInventory;
	        selectedIndex = index;
	        Inventory.Stack stack = selectedInv.getStack(selectedIndex);
	        selectedInv.transferItem(selected, selectedIndex, 0, stack.getNumItems());
	   	}
    } 
    
    /**
     * Transfers items between clicked inventory and the currently selected
     * 
     * @param inv inventory to transfer to
     * @param index index of space clicked
     * @param transfer the quantity to transfer
     */
    private void transfer(Inventory inv, int index, int transfer) {
    	if (inv.getStack(index) != null) {
            selectedInv = inv;
            selectedIndex = index;
            Inventory.Stack stack = selectedInv.getStack(selectedIndex);
            if (inv == playerEquipments) {
            	Inventory temp = new Inventory(1);
            	temp.addItem(playerEquipments.getEquipment(index));
            	playerEquipments.unequip(index);
                temp.transferItem(selected, 0, 0, temp.getStack(0).getNumItems());
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
            }
            //right click
            else if (transfer != 0 && stack != null) {
            	selectedInv.transferItem(selected, selectedIndex, 0, transfer);
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
            //left click
            } else if (stack != null){
                selectedInv.transferItem(selected, selectedIndex, 0, stack.getNumItems());
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);

            }
        }
    }
	
	/**
	 * Merges selected item to the clicked item
	 * Used on left click
	 * 
	 * @param inv the inventory of the selected item
	 * @param index the index of the selected item
	 */
	private void mergeLeft(Inventory inv, int index) {
		Inventory.Stack sourceStack = selected.getStack(0);

		//No item at destination
		if (inv.getStack(index) == null) {
            if (inv == playerEquipments) {
        		Item stackItem = selected.getStack(0).getItem();
                // check whether the select item is a equipment
                if (stackItem instanceof Equipment) {
                    playerEquipments.equip(selected, (Equipment) stackItem);
//        	        GameManager.get().getManager(SoundManager.class).playSound(equipSound);
                }
        	} else {
                selected.transferItem(inv, 0, index, sourceStack.getNumItems());
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
        	}
        } else {
			Inventory.Stack destStack = inv.getStack(index);
        	
        	if (sourceStack.getItem().equals(destStack.getItem()) &&
					destStack.getNumItems() != 64) {
				int remaining = selected.transferItem(inv, 0, index, sourceStack.getNumItems());
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
				if ((remaining > 0) && (remaining < 64)) {
					selected.removeItem(selected.getStack(0).getItem(), 
							selected.getStack(0).getNumItems() - remaining);
				}
			} else if (inv == playerEquipments) {
				Item stackItem = selected.getStack(0).getItem();
                // check whether the select item is a equipment
                if (stackItem instanceof Equipment) {
                	Inventory temp = new Inventory(1);
                	temp.addItem(playerEquipments.getEquipment(index));
                	playerEquipments.unequip(index);
                    playerEquipments.equip(selected, (Equipment) stackItem);
                    temp.transferItem(selected, 0, 0, destStack.getNumItems());
//        	        GameManager.get().getManager(SoundManager.class).playSound(equipSound);
                }
			} else {
                Inventory temp = new Inventory(1);
                inv.transferItem(temp, index, 0, destStack.getNumItems());
                selected.transferItem(inv, 0, index, selected.getStack(0).getNumItems());
                temp.transferItem(selected, 0, 0, temp.getStack(0).getNumItems());
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			}
		}
		checkSelectedNull();
    }
	
	/**
	 * Merges single quantity of source item to the selected item
	 * Used on right click
	 * 
	 * @param inv the inventory of the selected item
	 * @param index the index of the selected item
	 */
	private void mergeRight(Inventory inv, int index) {
		Inventory.Stack sourceStack = selected.getStack(0);

		//No item at destination
		if (inv.getStack(index) == null) {
			if (inv == playerEquipments) {
        		Item stackItem = selected.getStack(0).getItem();
                // check whether the select item is a equipment
                if (stackItem instanceof Equipment) {
                    playerEquipments.equip(selected, (Equipment) stackItem);
//        	        GameManager.get().getManager(SoundManager.class).playSound(equipSound);
                }
        	} else {
    			selected.transferItem(inv, 0, index, 1);	
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
        	}
		} else if (inv == playerEquipments) {
			Item stackItem = selected.getStack(0).getItem();
            // check whether the select item is a equipment
            if (stackItem instanceof Equipment) {
            	Inventory temp = new Inventory(1);
            	temp.addItem(playerEquipments.getEquipment(index));
            	playerEquipments.unequip(index);
                playerEquipments.equip(selected, (Equipment) stackItem);
                temp.transferItem(selected, 0, 0, 1);
//    	        GameManager.get().getManager(SoundManager.class).playSound(equipSound);
            }
		} else {
			Inventory.Stack destStack = inv.getStack(index);
			if (sourceStack.getItem().equals(destStack.getItem()) && destStack.getNumItems() != 64) {
				selected.transferItem(inv, 0, index, 1);
//    	        GameManager.get().getManager(SoundManager.class).playSound(getSound);
			}
		}
		checkSelectedNull();	
    }
	
	/**
	 * Renders any selected item onto mouse position
	 * 
	 * @param batch The sprite batch
	 */
	private void renderSelectedItem(SpriteBatch batch) {
		Inventory.Stack stack = selected.getStack(0);
		Texture item = GameManager.get().getManager(TextureManager.class)
                .getTexture(stack.getItem().getTexture());
        String quantity = "x" + stack.getNumItems();
        Vector3 coords = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        float[] mouse = new float[] { coords.x, coords.y };
        batch.draw(item, mouse[0], mouse[1], ITEM_DIM, ITEM_DIM);
        font.draw(batch, quantity, mouse[0] + 25, mouse[1] + 20);
	}
	
	/**
	 * Renders a description box showing item texture, name, description and rarity.
	 * 
	 * @param batch The sprite batch
	 */
	private void renderItemDes(SpriteBatch batch) {
		Vector3 coords = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 mousePos = new Vector2(coords.x, coords.y);
        Inventory hoveredInventory = calculateInventory(mousePos);
        if (hoveredInventory == null) {
        	hoverTime = System.currentTimeMillis();
            return;
        }
		int hoveredIndex = calculateIndex(mousePos, hoveredInventory);
		if (hoveredIndex != -1) {
			Inventory.Stack stack = hoveredInventory.getStack(hoveredIndex);
			if (stack != null && checkHoverTime(hoveredInventory, hoveredIndex)) {
				Sprite sprite = new Sprite(invDesBackground);
				sprite.setBounds(mousePos.x, mousePos.y, 300, 120);
				sprite.draw(batch);
				font.draw(batch, stack.getItem().getName(), mousePos.x, mousePos.y + 120);
				font.draw(batch, stack.getItem().getDescription(), mousePos.x, mousePos.y + 100,
						300, Align.left, true);
				font.draw(batch, "Rarity:", mousePos.x + ITEM_DIM + 30, mousePos.y + 30,
						300, Align.left, true);
				Texture item = GameManager.get().getManager(TextureManager.class)
		                .getTexture(stack.getItem().getTexture());
				batch.draw(item, mousePos.x, mousePos.y, ITEM_DIM, ITEM_DIM);
				for (int i = 0; i < stack.getItem().getRarity(); i++) {
					batch.draw(star, mousePos.x + ITEM_DIM + 80 + i * 20, mousePos.y + 15, 20, 20);
				}
			}	
		}
	}

	/**
	 * Determine if mouse has been hovering over same item for one or more seconds
	 * 
	 * @param hoveredInventory The inventory the mouse is currently over
	 * @param hoveredIndex The index the mouse is currently over
	 * @return true if mouse has movered over same stack for one or more seconds
	 */
	private boolean checkHoverTime(Inventory hoveredInventory, int hoveredIndex) {
		if (hoveredInventory == hoverInv && hoveredIndex == hoverInd) {
			return ((System.currentTimeMillis() - hoverTime)/ 1000 >= 1) ;
		} else {
			hoverInv = hoveredInventory;
			hoverInd = hoveredIndex;
			hoverTime = System.currentTimeMillis();
			return false;
		}
	
	}
	
    /**
     * Sets this game's crafting window renderer
     * @param craftingWindowRenderer the crafting window renderer for this game
     */
    public void setCraftingWindowRenderer(CraftingWindowRenderer craftingWindowRenderer) {
        this.craftingWindowRenderer = craftingWindowRenderer;
    }

    /**
     * Checks if the user has clicked on the crafting table
     * @return Whether or not the user has clicked on the crafting table
     */
    private boolean clickedCrafting(Vector2 cameraPos) {
        if (craftingWindowRenderer.isCrafting) {
            for (int i = 0; i < 9; i++) {
                if (craftingWindowRenderer.getCraftingBoxes().get(i).contains(cameraPos)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if mouse is in player inventory
     * @return true if mouse is in player inventory
     */
    public boolean playerInvClicked() {
    	Vector3 coords = this.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 mousePos = new Vector2(coords.x, coords.y);
		return playerBarRenderer.contains(mousePos);
    }
    
}
