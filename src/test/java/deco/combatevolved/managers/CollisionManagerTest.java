package deco.combatevolved.managers;

import static org.junit.Assert.*;

import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.worldgen.WorldGenParamBag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import deco.combatevolved.entities.dynamicentities.AgentEntity;
import deco.combatevolved.util.HexVector;

public class CollisionManagerTest {
	private static CombatEvolvedWorld testWorld;
	private static CollisionManager collisionManager;
	private AgentEntity entity1;
	private AgentEntity entity2;

	@Before
	public void setUp() {
		testWorld = new CombatEvolvedWorld();
		GameManager.get().setWorld(testWorld);
		collisionManager = CollisionManager.get();
		entity1 = new AgentEntity(0, 0, 0.05f);
		entity2 = new AgentEntity(1, 1, 0.05f);
		testWorld.addEntity(entity1);
		testWorld.addEntity(entity2);
	}
	
	@After
	public void tearDown() {
		testWorld = null;
		collisionManager = null;
		entity1 = null;
		entity2 = null;
	}

	@Test
	public void noCollisionTest() {
		assertFalse(collisionManager.willCollide(entity1, new HexVector(0f, 0f)));
	}
	
	@Test
	public void collisionTest() {
		assertTrue(collisionManager.willCollide(entity1, new HexVector(1f, 1f)));
	}

}
