package deco.combatevolved.handlers;

import deco.combatevolved.managers.GameManager;
import deco.combatevolved.observers.CycleObserver;
import deco.combatevolved.worlds.weather.WeatherAlerter;
import deco.combatevolved.worlds.weather.WeatherState;

public class WorldState implements CycleObserver, WeatherAlerter {
    private int cycleCode;
    private boolean hasChanged = true;
    private WeatherState[] forecast;

    @Override
    public void notifyCycleChange(int cycleCode) {
        if (this.cycleCode != cycleCode) {
            this.cycleCode = cycleCode;
            hasChanged = true;
        }
    }

    @Override
    public void Alert() {
        forecast = GameManager.get().getWorld().getWeather().getForecast();
        hasChanged = true;
    }

    public boolean check() {
        if (hasChanged) {
            hasChanged = false;
            return true;
        }
        return false;
    }

    public int getDayNightCycleCode() {
        return cycleCode;
    }

    public WeatherState[] getForecast() {
        return forecast;
    }
}
