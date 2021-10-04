package deco.combatevolved.managers;
import deco.combatevolved.entities.AbstractEntity;
import deco.combatevolved.entities.staticentities.ControlTowers;
import deco.combatevolved.mainmenu.EndGamePopup;
import deco.combatevolved.observers.ComTowerObserver;


import java.util.ArrayList;
import java.util.List;

public class ControlTowerManager extends TickableManager{
    private List<ComTowerObserver> comTowerObservers = new ArrayList<>();

    private int maxConquestCompletion;


    public ControlTowerManager() {
        this.maxConquestCompletion = 0;
    }
    /**
     * adds a observer to the ComTowerManager
     * @param comTowerObserver the observer being added
     */
    public void addObserver(ComTowerObserver comTowerObserver) {
        this.comTowerObservers.add(comTowerObserver);
    }

    /**
     * removes a observer from the ComTowerManager
     * @param comTowerObserver the observer being removed
     */
    public void removeObserver(ComTowerObserver comTowerObserver) {
        this.comTowerObservers.remove(comTowerObserver);
    }

    /**
     * returns the current maximum conquests
     * @return the current maximum conquest
     */
    public int getMaxConquestCompletion() {
        return this.maxConquestCompletion;
    }

    @Override
    public void onTick(long i) {
        this.maxConquestCompletion = 0;
        for (AbstractEntity controlTower : GameManager.get().getWorld().getEntities()) {

            if (controlTower instanceof ControlTowers  &&
                    this.maxConquestCompletion < ((ControlTowers) controlTower).getConquestCompletion()) {
                //checking for highest conquest completion
                this.maxConquestCompletion = ((ControlTowers) controlTower).getConquestCompletion();
            }
        }
        for (ComTowerObserver comTowerObserver : this.comTowerObservers) { // notifies observers of cycle change
            comTowerObserver.notifyComConquest(maxConquestCompletion); //sends the conquest rate
        }
    }

}
