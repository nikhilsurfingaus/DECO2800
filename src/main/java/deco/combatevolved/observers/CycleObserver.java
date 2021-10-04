package deco.combatevolved.observers;

public interface CycleObserver {

    /**
     * notifies the observer if the day night cycle has changed
     * @param cycleCode the current cycle
     */
    void notifyCycleChange(int cycleCode);
}
