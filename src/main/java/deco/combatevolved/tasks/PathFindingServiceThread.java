package deco.combatevolved.tasks;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.util.BFSPathfinder;
import deco.combatevolved.util.Pathfinder;
import deco.combatevolved.worlds.Tile;

import java.util.List;

public class PathFindingServiceThread implements Runnable {
	private Thread thread;
	MovementTask movementTask;

	public PathFindingServiceThread(MovementTask movementTask) {
		this.movementTask = movementTask;
	}

	public void run() {
		Pathfinder pathfinder = new BFSPathfinder();
		if (movementTask.entity.getNextCoords() != null){
			List<Tile> path1;
			List<Tile> path2;
			path1 = (pathfinder.pathfind(GameManager.get().getWorld(),
					movementTask.entity.getNextCoords(), movementTask.destination));
			path2 = (pathfinder.pathfind(GameManager.get().getWorld(),
					movementTask.entity.getPosition(), movementTask.destination));
			if( path1.size() <= path2.size()) {
				movementTask.setPath(path1);
			} else {
				movementTask.setPath(path2);
			}
			//System.out.println("go");
		} else {
			movementTask.setPath( pathfinder.pathfind(GameManager.get().getWorld(),
					movementTask.entity.getPosition(), movementTask.destination));
		}
	}

	public void start() {
		
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}
	public boolean isAlive() {
		return thread.isAlive();
	}
}
