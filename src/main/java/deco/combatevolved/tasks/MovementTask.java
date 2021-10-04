package deco.combatevolved.tasks;

import java.util.List;

import deco.combatevolved.entities.dynamicentities.DynamicEntity;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.PathFindingService;
import deco.combatevolved.worlds.Tile;
import deco.combatevolved.util.HexVector;

public class MovementTask extends AbstractTask{
	
	private boolean complete;
	
	private boolean computingPath = false;
	private boolean taskAlive = true;
	
	DynamicEntity entity;
	HexVector destination;
	
	private List<Tile> path;

	public MovementTask(DynamicEntity entity, HexVector destination) {
		super(entity);

		this.entity = entity;
		this.destination = destination;
		this.complete = false;//path == null || path.isEmpty();
	}

	@Override
	public void onTick(long tick) {
		if(path != null && !GameManager.get().getPaused()) {
			//we have a path
			if(path.isEmpty()) {
				complete = true;

			} else {
				entity.moveTowards(path.get(0).getCoordinates());

				if(entity.getPosition().isCloseEnoughToBeTheSame(path.get(0).getCoordinates())) {
					path.remove(0);
					if(path.isEmpty()) {
						entity.setNextCoords(null);
					} else {
						entity.setNextCoords(path.get(0).getCoordinates());
					}
				}
			}
		} else if (computingPath) {
			// change sprite to show waiting??


		} else {
			//ask for a path
			computingPath = true;
			GameManager.get().getManager(PathFindingService.class).addPath(this);
		}
	}

	/**
	 * Resets an old movement task so that it can be reused
	 * @param entity The new entity for this movement task to control
	 * @param destination the new destination for the entity
	 */
	public void reset(DynamicEntity entity, HexVector destination) {
		this.entity = entity;
		this.destination = destination;
		complete = false;
		computingPath = false;
		taskAlive = true;
		path = null;
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	public void setPath(List<Tile> path) {

		if (path == null) {
			taskAlive = false;
		}

		this.path = path;
		computingPath = false;
	}
	
	public List<Tile> getPath() {
		return path;
	}

	@Override
	public boolean isAlive() {
		return taskAlive;
	}

}
