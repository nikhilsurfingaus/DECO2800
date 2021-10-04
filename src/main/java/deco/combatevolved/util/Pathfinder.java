package deco.combatevolved.util;

import java.util.List;

import deco.combatevolved.worlds.AbstractWorld;
import deco.combatevolved.worlds.Tile;

public abstract class Pathfinder {
	
	public  abstract List<Tile> pathfind(AbstractWorld world, HexVector origin, HexVector destination);

}
