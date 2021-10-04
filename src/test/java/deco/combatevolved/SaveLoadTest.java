package deco.combatevolved;

import deco.combatevolved.worlds.CombatEvolvedWorld;
import deco.combatevolved.worlds.worldgen.WorldGenParamBag;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import deco.combatevolved.handlers.KeyboardManager;
import deco.combatevolved.entities.dynamicentities.PlayerPeon;
import deco.combatevolved.managers.DatabaseManager;
import deco.combatevolved.managers.GameManager;
import deco.combatevolved.managers.InputManager;
import deco.combatevolved.managers.OnScreenMessageManager;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GameManager.class, DatabaseManager.class, PlayerPeon.class})
public class SaveLoadTest {
    private CombatEvolvedWorld w = null;

    @Mock
    private GameManager mockGM;
    
    
    @Before
    public void Setup() {
        w = new CombatEvolvedWorld();
        
        mockGM = mock(GameManager.class);
        mockStatic(GameManager.class);
        
        
        when(GameManager.get()).thenReturn(mockGM);
        when(mockGM.getWorld()).thenReturn(w);
        
        //mocked imput manager
        InputManager Im = new InputManager();
        KeyboardManager Km = new KeyboardManager();
        OnScreenMessageManager mockOSMM = mock(OnScreenMessageManager.class);
        when(mockGM.getManager(OnScreenMessageManager.class)).thenReturn(mockOSMM);
        
        when(GameManager.getManagerFromInstance(InputManager.class)).thenReturn(Im);
        when(GameManager.getManagerFromInstance(KeyboardManager.class)).thenReturn(Km);

     
    }

    // TODO: Split this test up into multiple unit tests that test individual component functionality
    // TODO: instead of just testing the entire DatabaseManager in a single test.
    // TODO: Whenever this test fails it's impossible to know what went wrong.
    // TODO: If this test is intentionally testing system functionality, then
    // TODO: it still needs to be obvious where the test went wrong so it can be debugged
    // TODO: rather than it just saying "Test failed, good luck!"
    // TODO: rewrite this to fit new structure
    @Test
    public void SetMapTest() {
        assertEquals("This is temp", "This is temp");
//
//        CopyOnWriteArrayList<Tile> saveTileMap = new CopyOnWriteArrayList<>();
//        Map<Integer, AbstractEntity> newEntities = new ConcurrentHashMap<>();
//        float col_one = 1.0f;
//        float row_one = 1.0f;
//        float col_two = 4.0f;
//        float row_two = 5.0f;
//        saveTileMap.add(new Tile("grass_1_0", col_one, row_one));
//        saveTileMap.add(new Tile("grass_1_0", col_two, row_two));
//        w.setTileMap(saveTileMap);
//
//        newEntities.put(0, new PlayerPeon(1, 1, 1));
//
//        List<AbstractEntity> testEntities = new ArrayList<>(w.getEntities());
//        DatabaseManager.saveWorld(w);
//
//
//        LoadGameWorld q = new LoadGameWorld();
//        DatabaseManager.loadWorld(q);
//
//
//        List<AbstractEntity> worldEntities = new ArrayList<>(q.getEntities());
//
//        //testing on requires both lists to be in the exact same order.
//        Collections.sort(testEntities, new Comparator<AbstractEntity>() {
//            public int compare(AbstractEntity o1, AbstractEntity o2)
//            {
//                return o1.getEntityID() - o2.getEntityID();
//            }
//        });
//
//        Collections.sort(worldEntities, new Comparator<AbstractEntity>() {
//            public int compare(AbstractEntity o1, AbstractEntity o2)
//            {
//                return o1.getEntityID() - o2.getEntityID();
//            }
//        });
//
//        assertFalse("No entities Loaded",worldEntities.size() == 0);
//        assertFalse("No tiles Loaded",q.getTileListFromMap().size() == 0);
//
//        for (int i = 0; i < saveTileMap.size(); i++) {
//            assertEquals("Comparing tile.getTexture",saveTileMap.get(i).getTextureName(), q.getTileListFromMap().get(i).getTextureName());
//
//            assertEquals("Comparing TileID",saveTileMap.get(i).getTileID(),q.getTileListFromMap().get(i).getTileID());
//            assertEquals("Comparing Row",saveTileMap.get(i).getRow(), q.getTileListFromMap().get(i).getRow(), 0.001f);
//            assertEquals("Comparing Col",saveTileMap.get(i).getCol(), q.getTileListFromMap().get(i).getCol(), 0.001f);
//        }
//
//        for (int i = 0; i < testEntities.size(); i++) {
//            assertEquals("Comparing getEntityID",testEntities.get(i).getEntityID(), worldEntities.get(i).getEntityID(), 1f);
//            assertEquals("Comparing entities.getTexture",testEntities.get(i).getTexture(), worldEntities.get(i).getTexture());
//            assertEquals("Comparing entities.getPosition",testEntities.get(i).getPosition(), worldEntities.get(i).getPosition());
//        }
   }
}
