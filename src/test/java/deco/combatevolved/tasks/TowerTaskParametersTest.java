package deco.combatevolved.tasks;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;


public class TowerTaskParametersTest {
    private TowerTaskParameters tpt;
    @Before
    public void setup(){
        tpt = mock(TowerTaskParameters.class);
        }
    @Test
    public void getTarget() {
        tpt.getTarget();
        verify(tpt,times(1)).getTarget();
    }

    @Test
    public void isTargetFound() {
        when(tpt.isTargetFound()).thenReturn(true);
        tpt.isTargetFound();
        verify(tpt,times(1)).isTargetFound();
    }
}