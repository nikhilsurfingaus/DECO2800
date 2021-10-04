package deco.combatevolved.tasks;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.*;

public class TowerAttackTaskTest {
    private TowerAttackTask p;
    @Before
    public void setup(){
        p = mock(TowerAttackTask.class);
    }
    @Test
    public void isComplete() {
        when(p.isComplete()).thenReturn(true);
        p.isComplete();
        verify(p,times(1)).isComplete();
    }

    @Test
    public void isAlive() {
        when(p.isAlive()).thenReturn(true);
        p.isAlive();
        verify(p,times(1)).isAlive();

    }

    @Test
    public void getTarget() {
        when(p.isAlive()).thenReturn(true);
        p.isAlive();
        verify(p,times(1)).isAlive();
    }

    @Test
    public void getLogger() {
        p.onTick(1);
        assertTrue(!p.isComplete());
        verify(p,times(1)).onTick(1);
    }
}