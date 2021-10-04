package deco.combatevolved.managers;

import deco.combatevolved.Tickable;
import deco.combatevolved.observers.CycleObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * day night cycle to check whether it is day or night
 */
public class DayNightCycle extends TickableManager implements Tickable {
    private int cycleCode; //value indicates current cycle day = 1, night = 2.
    private long lastCycleChange; //last time the cycle changed
    private int dayLength; //length of a day in milliseconds
    private int nightLength; //length of a night in milliseconds
    private int sunsetLength;
    private List<CycleObserver> cyclesObservers = new ArrayList<>();


    public DayNightCycle(long initTime, int dayLength, int nightLength){
        this.lastCycleChange = initTime;
        this.dayLength = dayLength;
        this.nightLength = nightLength;
        this.sunsetLength = 5000;
        this.cycleCode = 1;
    }

    public DayNightCycle(long initTime, int dayLength, int nightLength, int sunsetLength){
        this.lastCycleChange = initTime;
        this.dayLength = dayLength;
        this.nightLength = nightLength;
        this.sunsetLength = sunsetLength;
        this.cycleCode = 1;
    }

    //default method
    public DayNightCycle(){
        this.lastCycleChange = System.currentTimeMillis();
        this.dayLength = 60000;
        this.nightLength = 60000;
        this.sunsetLength = 5000;
        this.cycleCode = 1;
    }

    /**
     * adds a observer to the day night cycle
     * @param cycleObserver the observer being added
     */
    public void addObserver(CycleObserver cycleObserver) {
        this.cyclesObservers.add(cycleObserver);
    }

    /**
     * removes a observer from the day night cycle
     * @param cycleObserver the observer being removed
     */
    public void removeObserver(CycleObserver cycleObserver) {
        this.cyclesObservers.remove(cycleObserver);
    }

    @Override
    public void onTick(long i) {
        if (!GameManager.get().getManager(NetworkManager.class).isHost())
            return;

        if (this.cycleCode == 1) {
            if (System.currentTimeMillis() - this.lastCycleChange >= this.dayLength - this.sunsetLength) {
                this.cycleCode = 3;
            } else {
                return;
            }
        } else if (this.cycleCode == 3) {
            if (System.currentTimeMillis() - this.lastCycleChange >= this.dayLength) {
                this.cycleCode = 2;
                this.lastCycleChange = System.currentTimeMillis();
            } else {
                return;
            }
        } else {
            if (System.currentTimeMillis() - this.lastCycleChange >= this.nightLength) {
                this.cycleCode = 1;
                this.lastCycleChange = System.currentTimeMillis();
            } else {
                return;
            }
        }
        alertObservers();
    }

    /**
     * sets the length of the day
     * @param dayLength the new length of the day
     */
    public void setDayLength(int dayLength) {
        this.dayLength = dayLength;
    }

    /**
     * sets teh length of the night
     * @param nightLength the new length of the night
     */
    public void setNightLength(int nightLength) {
        this.nightLength = nightLength;
    }

    public void setCycleCode(int cycleCode) {
        this.cycleCode = cycleCode;
        alertObservers();
    }

    public int getCycleCode() {
        return cycleCode;
    }

    public long getNightLength() {
        return this.nightLength;
    }

    private void alertObservers() {
        for (CycleObserver cycleObserver : this.cyclesObservers) { // notifies observers of cycle change
            cycleObserver.notifyCycleChange(cycleCode);
        }
    }
}
