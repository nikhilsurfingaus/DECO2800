package deco.combatevolved.worlds;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.dynamicentities.Projectile;
import deco.combatevolved.entities.staticentities.ItemEntity;
import deco.combatevolved.entities.staticentities.StaticEntity;
import deco.combatevolved.handlers.WorldState;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.NetworkManager;
import deco.combatevolved.worlds.biomes.*;
import deco.combatevolved.util.HexVector;
import deco.combatevolved.worlds.biomes.*;
import deco.combatevolved.worlds.weather.WeatherModel;
import deco.combatevolved.worlds.worldgen.WorldGenParamBag;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * AbstractWorld is the Game AbstractWorld
 *
 * It provides storage for the WorldEntities and other universal world level items.
 */
public abstract class AbstractWorld {
    // List to store all the entities within the game
    protected List<AbstractEntity> entities = new CopyOnWriteArrayList<>();

    // List to store the entities to delete within the game
    protected List<AbstractEntity> entitiesToDelete = new CopyOnWriteArrayList<>();

    // List to store the tiles that need to be deleted
    protected List<Tile> tilesToDelete = new CopyOnWriteArrayList<>();

    private List<ItemEntity> droppedItems = new CopyOnWriteArrayList<>();

    protected WorldGenParamBag worldGenParamBag = new WorldGenParamBag();

    // Handler for world state
    private WorldState worldState;

    private int weatherCount = 0;
    private static final int WEATHER_TIME = 5;
    private WeatherModel weather;

    // clicked position on world
    private List<Float> clickedPos1 = new ArrayList<>();
    private List<Float> clickedPos2 = new ArrayList<>();

    /**
     * AbstractWorld Constructor
     */
    protected AbstractWorld() {
        this.weather = new WeatherModel();
    	generateWorld();
    	generateNeighbours();
    	generateTileIndexes();

    	worldState = new WorldState();
    	weather.subscribeToWeatherAlert(worldState);
    }

    protected AbstractWorld(String hostname, String username) {
        super();
    }

    /**
     * Main method to generate the game world
     */

    protected abstract void generateWorld();

    /**
     * Method to map and generate the neighbours for each tile
     */
    public void generateNeighbours() {
        ArrayList<Tile> tempTiles = generateListOfTilesFromBiomeMap();

    //multiply coords by 2 to remove floats
    	Map<Integer, Map<Integer, Tile>> tileMap = new HashMap<>();
		Map<Integer, Tile> columnMap;
		for(Tile tile : tempTiles) {
			columnMap = tileMap.getOrDefault((int)tile.getCol()*2, new HashMap<>());
			columnMap.put((int) (tile.getRow()*2), tile);
			tileMap.put((int) (tile.getCol()*2), columnMap);
		}
		
		for(Tile tile : tempTiles) {
			int col = (int) (tile.getCol()*2);
			int row = (int) (tile.getRow()*2);
			
			//West
			if(tileMap.containsKey(col - 2)) {
				//North West
				if (tileMap.get(col - 2).containsKey(row + 1)) {
					tile.addNeighbour(Tile.NORTH_WEST,tileMap.get(col - 2).get(row+ 1));
				}
				
				//South West
				if (tileMap.get(col - 2).containsKey(row -  1)) {
					tile.addNeighbour(Tile.SOUTH_WEST,tileMap.get(col -2).get(row - 1));
				}
			}
			
			//Central
			if(tileMap.containsKey(col)) {
				//North
				if (tileMap.get(col).containsKey(row + 2)) {
					tile.addNeighbour(Tile.NORTH,tileMap.get(col).get(row + 2));
				}
				
				//South
				if (tileMap.get(col).containsKey(row - 2)) {
					tile.addNeighbour(Tile.SOUTH,tileMap.get(col).get(row - 2));
				}
			}
			
			//East
			if(tileMap.containsKey(col + 2)) {
				//North East
				if (tileMap.get(col + 2).containsKey(row+1)) {
					tile.addNeighbour(Tile.NORTH_EAST,tileMap.get(col + 2).get(row+1));
				}
				
				//South East
				if (tileMap.get(col + 2).containsKey(row- 1)) {
					tile.addNeighbour(Tile.SOUTH_EAST,tileMap.get(col + 2).get(row- 1));
				}
			}
		}
    }

    /**
     * Generator method for the tile indexes
     */
    private void generateTileIndexes() {
        ArrayList<Tile> tempTiles = generateListOfTilesFromBiomeMap();

    	for(Tile tile : tempTiles) {
    		tile.calculateIndex();
    	}
    }
    
    /**
     * Getter method to return a list of entities in this world
     * @return All Entities in the world
     */
    public List<AbstractEntity> getEntities() {
        return new CopyOnWriteArrayList<>(this.entities);
    }
    
    /**
     * Returns a list of all non static entities in this world
     * @return All non static entities
     */
    public List<AbstractEntity> getNonStaticEntities() {
        List<AbstractEntity> entityList = new CopyOnWriteArrayList<>(this.entities);
        for (AbstractEntity a : entityList) {
            if (a instanceof StaticEntity) {
                entityList.remove(a);
            }
        }
        return entityList;
    }

    /**
     * Returns a list of all non static entities and item entities in this world
     * @return All non static entities
     */
    public List<AbstractEntity> getNonStaticAndItemEntities() {
        List<AbstractEntity> entityList = new CopyOnWriteArrayList<>(this.entities);
        for (AbstractEntity a : entityList) {
            if (a instanceof StaticEntity && !(a instanceof ItemEntity)) {
                entityList.remove(a);
            }
        }
        return entityList;
    }

    /**
     *  Returns a list of entities in this world, ordered by their render level
     *  Getter method to return a list of entities in this world, ordered by
     *  their render level
     *  @return all entities in the world 
     */
    public List<AbstractEntity> getSortedEntities(){
		List<AbstractEntity> e = new CopyOnWriteArrayList<>(this.entities);
    	Collections.sort(e);
		return e;
    }

    /**
     * Adds an entity to the world
     * @param entity the entity to add
     */
    public void addEntity(AbstractEntity entity) {
        entities.add(entity);
        GameManager.get().getManager(NetworkManager.class).addEntity(entity);
    }

    /**
     * Adds a tower entity to the world
     * @param entity the entity to add
     * @param clickedPos1 postion 1 of the entity
     * @param clickedPos2 postion 2 of the entity
     */
    public void addEntityTower(AbstractEntity entity, float clickedPos1, float clickedPos2) {
        entities.add(entity);
        this.clickedPos1.add(clickedPos1);
        this.clickedPos2.add(clickedPos2);
        GameManager.get().getManager(NetworkManager.class).addEntity(entity);
    }

    /**
     * Checks if the clicked position/Tile already exist a tower
     * @param clickedPos1 position 1 to be checked
     * @param clickedPos2 position 2 to be checked
     * @return whether if there is a tower already
     */
    public boolean positionHasTower(float clickedPos1, float clickedPos2) {
        if ((this.clickedPos1.contains(clickedPos1) && clickedPos1 > clickedPos1 - 1f && clickedPos1 < clickedPos1 + 1f)
            && (this.clickedPos2.contains(clickedPos2) && clickedPos2 > clickedPos2 - 1f && clickedPos2 < clickedPos2 + 1f)) {
            return true;
        }
        return false;
    }

    /**
     * Removes an entity from the world
     * @param entity the entity to remove
     */
    public void removeEntity(AbstractEntity entity) {
        boolean e = entities.remove(entity);
    }

    public void removeRockets() {
        for (AbstractEntity entity : this.getEntities()) {
            if (entity instanceof Projectile) {
                entity.dispose();
            }
        }
    }

    /**
     * Setter method to set the entities within the world
     * @param entities The list of entities to set within the world
     */
	public void setEntities(List<AbstractEntity> entities) {
		this.entities = entities;
	}

	public List<Tile> getBiomeTileListFromMap(BiomeType biome) {
	    return this.worldGenParamBag.getMapOfTilesInBiomes().get(biome);
    }

    /**
     * Getter method to return the list of tiles within the game
     * @return The list containing a map of all the tiles within the game
     */
    public List<Tile> getTileListFromMap() {
        return generateListOfTilesFromBiomeMap();
    }

    /**
     * Getter method to return the map of tiles to biomes within the game
     * @return The map of tiles to biomes within the game
     */
    public ConcurrentHashMap<BiomeType, CopyOnWriteArrayList<Tile>> getTileMap() {
        return (ConcurrentHashMap<BiomeType, CopyOnWriteArrayList<Tile>>)this.worldGenParamBag.getMapOfTilesInBiomes();
    }

    /**
     * Getter method to return the list of tiles within the game for each biome
     * @return The list containing a map of all the tiles within the game
     */
    public ConcurrentHashMap<BiomeType, CopyOnWriteArrayList<Tile>> getBiomeMap() {
        return new ConcurrentHashMap<>(this.worldGenParamBag.getMapOfTilesInBiomes());
    }

    /**
     * Getter method to return a Tile from a column and row
     * @param col The column of the tile to return
     * @param row The row of the tile to return
     * @return The tile located in the supplied row amd column
     */
    public Tile getTile(float col, float row) {
    	return getTile(new HexVector(col,row));
    }

    /**
     * Getter method to return a Tile based on a HexVector position
     * @param position The position of the tile to return
     * @return The Tile based on the supplied position, else null if no Tile
     * exists in the supplied position
     */
    public Tile getTile(HexVector position) {
        ArrayList<Tile> tempTiles = generateListOfTilesFromBiomeMap();
        for (Tile t : tempTiles) {
            if (t.getCoordinates().equals(position)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Gets a a hex vector at the given position, allowing for error
     * @param position the position of the tile to return
     * @param error the allowable difference between the tile's position and
     *              the given position
     * @return a tile within error distance of the given position
     */
    public Tile getTile(HexVector position, float error) {
        ArrayList<Tile> tempTiles = generateListOfTilesFromBiomeMap();

        for (Tile tile : tempTiles) {
            if (tile.getCoordinates().isCloseEnoughToBeTheSame(position, error)) {
                return tile;
            }
        }
        return null;
    }

    /**
     * Getter method to return a Tile based on a supplied index
     * @param index The index of the Tile to be returned
     * @return The Tile in the supplied index, else null if no Tile exists in
     * the supplied index
     */
    public Tile getTile(int index) {
        ArrayList<Tile> tempTiles = generateListOfTilesFromBiomeMap();

        for (Tile t : tempTiles) {
        	if (t.getTileID() == index) {
        		return t;
			}
		}
		return null;
    }

    /**
     * Setter method to set the tiles within the game
     * @param biomesMap The List of tiles to set within the game, split by biome
     */
    public void setBiomeMap(ConcurrentHashMap<BiomeType, CopyOnWriteArrayList<Tile>> biomesMap) {
        this.worldGenParamBag.setMapOfTilesInBiomes(new ConcurrentHashMap<>(biomesMap));
    }


    /**
     * Updater method to update an existing Tile with a new Tile object
     * within the map
     * @param tile The new Tile to update with
     */
    public void updateTile(Tile tile) {
        for (Map.Entry<BiomeType, CopyOnWriteArrayList<Tile>> entry :
                this.worldGenParamBag.getMapOfTilesInBiomes().entrySet()) {
            CopyOnWriteArrayList<Tile> tiles = entry.getValue();
            for (Tile t : tiles) {
                if (t.getTileID() == tile.getTileID()) {
                    if (!t.equals(tile)) {
                        tiles.remove(t);
                        tiles.add(tile);
                    }
                    return;
                }
            }
        }
    }

    /**
     * Updater method to update an existing Tile with a new Tile object or add a new Tile object
     * within the map
     * @param tile The new Tile to update with
     */
    public void updateTile(Tile tile, BiomeType biomeType) {
        // Remove old tile
        for (Map.Entry<BiomeType, CopyOnWriteArrayList<Tile>> entry :
                this.worldGenParamBag.getMapOfTilesInBiomes().entrySet()) {
            CopyOnWriteArrayList<Tile> tiles = entry.getValue();
            for (Tile t : tiles) {
                if (t.getTileID() == tile.getTileID()) {
                    tiles.remove(t);
                }
            }
        }

        // Add biome if doesn't exist
        if (!this.worldGenParamBag.getMapOfTilesInBiomes().containsKey(biomeType))
            this.worldGenParamBag.getMapOfTilesInBiomes().put(biomeType,
                    new CopyOnWriteArrayList<>());

        // Add new tile
        this.worldGenParamBag.getMapOfTilesInBiomes().get(biomeType).add(tile);
    }

    /**
     * Updater method to update an existing entity with a new Entity object
     * within the map
     * @param entity The Entity to update an existing Entity with
     */
    //TODO ADDRESS THIS..?
    public void updateEntity(AbstractEntity entity) {
        for (AbstractEntity e : this.entities) {
            if (e.getEntityID() == entity.getEntityID()) {
                this.entities.remove(e);
                this.entities.add(entity);
                return;
            }
        }
        this.entities.add(entity);

        // Since MultiEntities need to be attached to the tiles they live on, setup that connection.
        if (entity instanceof StaticEntity) {
            ((StaticEntity) entity).setup();
        }
    }

    public void onTick(long i) {
        for (AbstractEntity e : entitiesToDelete) {
            entities.remove(e);
        }

        tickWeather();

//        for (Tile t : tilesToDelete) {
//            tiles.remove(t);
//        }
    }

    public void tickWeather() {
        if (weatherCount == WEATHER_TIME) {
            getWeather().nextWeather();
            weatherCount = 0;
        }else {
            weatherCount++;
        }
    }

    /**
     * Delete's a Tile from the map
     * @param tileId The id of the Tile to delete
     */
    public void deleteTile(int tileId) {
        Tile tile = GameManager.get().getWorld().getTile(tileId);
        if (tile != null) {
            tile.dispose();
        }
    }

    /**
     * Delete's an Entity from the map
     * @param entityId The id of the Entity to delete
     */
    public void deleteEntity(int entityId) {
        for (AbstractEntity e : this.getEntities()) {
            if (e.getEntityID() == entityId) {
                e.dispose();
            }
        }
    }

    /**
     * Getter method to return an Entity from an id
     * @param entityId The id of the entity to return
     * @return The entity relating to the supplied id, else null if no entity
     * exists
     */
    public AbstractEntity getEntityById (int entityId) {
        for (AbstractEntity e : this.getEntities()) {
            if (e.getEntityID() == entityId) {
                return e;
            }
        }
        return null;
    }

    /**
     * Queues a List of Entities to be deleted the map
     * @param entities The List of entities to be deleted
     */
    public void queueEntitiesForDelete(List<AbstractEntity> entities) {
        entitiesToDelete.addAll(entities);
    }

    /**
     * Queues a List of Tiles to be deleted from the map
     * @param tiles The List of entities to be deleted
     */
    public void queueTilesForDelete(List<Tile> tiles) {
        tilesToDelete.addAll(tiles);
    }

    private ArrayList<Tile> generateListOfTilesFromBiomeMap() {
        ArrayList<Tile> tempTiles = new ArrayList<>();

        // Loop over the entries in the biomesMap
        for (Map.Entry<BiomeType, CopyOnWriteArrayList<Tile>> entry :
                this.worldGenParamBag.getMapOfTilesInBiomes().entrySet()) {
            CopyOnWriteArrayList<Tile> value = entry.getValue();
            tempTiles.addAll(value);
        }
        return tempTiles;
    }

    /**
     * @return returns the current state of the world
     */
    public WorldState getWorldState() {
        return worldState;
    }

    /**
     * @return returns the current state of the weather
     */
    public WeatherModel getWeather() {
        return weather;
    }

    /**
     * Get's the current ParamBag for the world
     * @return The ParamBag for the world
     */
    public WorldGenParamBag getWorldGenParamBag() { return this.worldGenParamBag; }
}
