package deco.combatevolved.entities.staticentities;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import deco.combatevolved.Tickable;
import deco.combatevolved.entities.RenderConstants;
import deco.combatevolved.worlds.AbstractWorld;
import deco.combatevolved.worlds.Tile;

public class Tree extends StaticEntity implements Tickable {
	private final Logger LOG = LoggerFactory.getLogger(Tree.class);

	public Tree() {super();}

	public Tree(float col, float row, int renderOrder, List<Part> parts) {
		super(col, row, renderOrder, parts);
		LOG.info("Making a tree at {}, {}", col, row);
		this.setTexture("tree");
	}

	public Tree(Tile t, boolean obstructed) {
        super(t, RenderConstants.TREE_RENDER, "tree", obstructed);
	}

	/**
	 * Tree constructor
	 * @param tile The tile to set the Tree on
	 * @param texture The texture to render the tree as
	 * @param obstructed Determine if the tile the tree is rendered on will
	 *                      become obstructed or not
	 */
	public Tree(Tile tile, String texture, boolean obstructed) {
		super(tile, RenderConstants.TREE_RENDER, "tree", obstructed);
		setTexture(texture);
	}


	/**
	 * Animates the trees on every game tick
	 *
	 * @param tick
	 *            Current game tick
	 */
	@Override
	public void onTick(long tick) {
		// Adding this in here to stop sonarqube smell
	}
}