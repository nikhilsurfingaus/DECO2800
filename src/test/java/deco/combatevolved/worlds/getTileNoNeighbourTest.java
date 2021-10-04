package deco.combatevolved.worlds;

import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.staticentities.Rock;
import deco.combatevolved.worlds.biomes.BiomeType;
import deco.combatevolved.worlds.worldgen.WorldGenParamBag;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class getTileNoNeighbourTest {
    CombatEvolvedWorld world = new CombatEvolvedWorld();
    CombatEvolvedWorld world2 = new CombatEvolvedWorld();
    CombatEvolvedWorld fullworld = new CombatEvolvedWorld();

    @Before
    public void setUp() {
        List<Tile> tile = fullworld.getTileListFromMap();
        for(Tile t: tile) {
            fullworld.entities.add(new Rock(t,true));
        }
    }

//    @Test
//    public void getTileNoNeighbourObstructTest() {
//		Tile t = world.getTileNoNeighbourObstruct(world.getTileListFromMap());
//		Map<Integer, Tile> neighbours = t.getNeighbours();
//		assertFalse(t.isObstructed());
//		for(Tile tile: neighbours.values()) {
//			assertFalse(tile.isObstructed());
//		}
//	}
	
	@Test(expected = NullPointerException.class)
	public void getTileNoNeighbourObstructNullListTest(){
		world.getTileNoNeighbourObstruct(null);
	}
	
	@Test
	public void getTileNoNeighbourObstructFullWorldTest() {
		Tile t = fullworld.getTileNoNeighbourObstruct(fullworld.getBiomeTileListFromMap(BiomeType.TEMPERATE_RAINFOREST));
		assertTrue(t == null);
	}
	
//	@Test(expected = NullPointerException.class)
//	public void getTileNoNeighbourObstructSingleTileWorldTest() {
//		List<Tile> tileList = world2.getTileListFromMap();
//		for(Tile t: tileList) {
//			world2.deleteTile(t.getTileID());
//		}
//		world2.getTileListFromMap().add(new Tile("plains_0"));
//		world2.getTileNoNeighbourObstruct(world2.getTileListFromMap());
//	}

    @Test
    public void getTileNoNeighbourObstructSingleFreeTileTest() {
        AbstractEntity entity = fullworld.getEntities().get(1);
        fullworld.removeEntity(entity);
        Tile t = fullworld.getTileNoNeighbourObstruct(fullworld.getTileListFromMap());
        assertTrue(t == null);
    }
}
