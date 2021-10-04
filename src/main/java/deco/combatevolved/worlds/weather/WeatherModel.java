package deco.combatevolved.worlds.weather;

import java.util.ArrayList;
import java.util.Random;

/**
 * WeatherModel class in charge of creating the weather conditons in game, as well as
 * developing these conditions
 */
public class WeatherModel implements WeatherInterface {

	private ArrayList<WeatherAlerter> weatherAlerters = new ArrayList<>();
	private WeatherState[] forecast;

	// Just a counter for our position in the forecast.
	private int currentForecastPosition;
	private final int forecastLength = 16;
	/**
	 * climateFactor is kind of like the "score to beat" in a DnD roll. Roll
	 * above the climate factor, and the weather will improve by one weather
	 * state. Roll below, and the weather will deteriorate by one weather state.
	 * Must be between 0 and 100 inclusive (D100)
	 */
	private int climateFactor;

	public WeatherModel() {
		this.currentForecastPosition = 0;
		this.forecast = new WeatherState[forecastLength];
		for (int i = 0; i < 16; i++) {
			this.forecast[i] = WeatherState.WEATHER_STATE_1;
		}
		this.forecast[0] = WeatherState.WEATHER_STATE_0;
		this.climateFactor = 20;
		regenerateForecast();
	}

	@Override
	public WeatherState getWeatherState() {
		return this.forecast[this.currentForecastPosition];
	}

	@Override
	public WeatherState[] getForecast() {
		// Returns deep copy
		return forecast.clone();
	}

	/**
	 * @return returns the forecast as a readable string for use in setting the weather
	 */
	public String getForecastString() {
		String forecastString = "[";
		for(int i = 0; i< forecastLength; i++) {
			if(i == this.getCurrentForecastPosition()) {
				forecastString = forecastString.concat("*"+this.forecast[i].asInteger().toString()+"*");				
			}else {
				forecastString = forecastString.concat(this.forecast[i].asInteger().toString());
			}
			if(i != (forecastLength - 1)) {
				forecastString = forecastString.concat(",");
			}
		}
		forecastString = forecastString.concat("]");
		return forecastString;
	}

	public void setForecast(WeatherState[] forecast) {
		this.forecast = forecast;
	}

	/**
	 * I'd have to write it out, but I'm pretty sure these are the only
	 * combinations that can mathematically satisfy the expected behaviour of
	 * the method while preserving forecast congruency. (non-Javadoc)
	 * 
	 * @see deco.combatevolved.worlds.weather.WeatherInterface#improveWeather()
	 */
	@Override
	public Boolean improveWeather() {
		int currentWeather = this.currentForecastPosition;
		int previousWeather = currentWeather - 1;
		int nextWeather = currentWeather + 1;
		int nextNextWeather = currentWeather + 2;
		if (previousWeather == -1)
			previousWeather = forecastLength - 1;
		if (nextWeather > forecastLength - 1)
			nextWeather = 0;
		if (nextNextWeather > forecastLength - 1)
			nextWeather = 1;

		switch (this.forecast[currentWeather]) {
		case WEATHER_STATE_0:
			this.forecast[previousWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[currentWeather] = WeatherState.WEATHER_STATE_0;
			this.forecast[nextWeather] = WeatherState.WEATHER_STATE_0;
			this.forecast[nextNextWeather] = WeatherState.WEATHER_STATE_1;
			break;
		case WEATHER_STATE_1:
			this.forecast[previousWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[currentWeather] = WeatherState.WEATHER_STATE_0;
			this.forecast[nextWeather] = WeatherState.WEATHER_STATE_1;
			break;
		case WEATHER_STATE_2:
			this.forecast[previousWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[currentWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[nextWeather] = WeatherState.WEATHER_STATE_1;
			break;
		default:
			return false;
		}
		notifyWeatherAlerters();
		return true;
	}

	/**
	 * Improves the current weather conditions in game
	 *
	 * @param forecastPosition current forecast position
	 * @return returns true if the weather state could be altered
	 */
	private Boolean improveWeather(int forecastPosition) {
		int previousWeather = forecastPosition - 1;

		if (previousWeather == -1)
			previousWeather = forecastLength - 1;

		switch (this.forecast[previousWeather]) {
		case WEATHER_STATE_0:
			this.forecast[forecastPosition] = WeatherState.WEATHER_STATE_0;
			break;
		case WEATHER_STATE_1:
			this.forecast[forecastPosition] = WeatherState.WEATHER_STATE_0;
			break;
		case WEATHER_STATE_2:
			this.forecast[forecastPosition] = WeatherState.WEATHER_STATE_1;
			break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * Reduces the current weather conditions in game
	 *
	 * @return returns true if the weather state could be altered to be worse
	 */
	@Override
	public Boolean worsenWeather() {
		int currentWeather = this.currentForecastPosition;
		int previousWeather = currentWeather - 1;
		int nextWeather = currentWeather + 1;
		int nextNextWeather = currentWeather + 2;
		if (previousWeather == -1)
			previousWeather = forecastLength - 1;
		if (nextWeather > forecastLength - 1)
			nextWeather = 0;
		if (nextNextWeather > forecastLength - 1)
			nextWeather = 1;

		switch (this.forecast[currentWeather]) {
		case WEATHER_STATE_0:
			this.forecast[previousWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[currentWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[nextWeather] = WeatherState.WEATHER_STATE_1;
			break;
		case WEATHER_STATE_1:
			this.forecast[previousWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[currentWeather] = WeatherState.WEATHER_STATE_2;
			this.forecast[nextWeather] = WeatherState.WEATHER_STATE_1;
			break;
		case WEATHER_STATE_2:
			this.forecast[previousWeather] = WeatherState.WEATHER_STATE_1;
			this.forecast[currentWeather] = WeatherState.WEATHER_STATE_2;
			this.forecast[nextWeather] = WeatherState.WEATHER_STATE_2;
			this.forecast[nextNextWeather] = WeatherState.WEATHER_STATE_1;
			break;
		default:
			return false;
		}
		notifyWeatherAlerters();
		return true;
	}

	private Boolean worsenWeather(int forecastPosition) {
		int previousWeather = forecastPosition - 1;

		if (previousWeather == -1)
			previousWeather = forecastLength - 1;

		switch (this.forecast[previousWeather]) {
		case WEATHER_STATE_0:
			this.forecast[forecastPosition] = WeatherState.WEATHER_STATE_1;
			break;
		case WEATHER_STATE_1:
			this.forecast[forecastPosition] = WeatherState.WEATHER_STATE_2;
			break;
		case WEATHER_STATE_2:
			this.forecast[forecastPosition] = WeatherState.WEATHER_STATE_2;
			break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * Improves the forecast outlook in game to be used and integrated into
	 * weather development.
	 *
	 * @return returns true if the forecast could be improved
	 */
	@Override
	public Boolean improveForecast() {
		this.climateFactor -= 20;
		if (this.climateFactor > 100)
			this.climateFactor = 100;
		if (this.climateFactor < 0)
			this.climateFactor = 0;
		if (regenerateForecast()) {
			notifyWeatherAlerters();
			return true;
		}
		return false;
	}

	/**
	 * Worsens the forecast outlook in game to be used and integrated into
	 * weather development.
	 *
	 * @return returns true if the forecast could be reduced
	 */
	@Override
	public Boolean worsenForecast() {
		this.climateFactor += 20;
		if (this.climateFactor > 100)
			this.climateFactor = 100;
		if (this.climateFactor < 0)
			this.climateFactor = 0;
		if (regenerateForecast()) {
			notifyWeatherAlerters();
			return true;
		}
		return false;
	}

	@Override
	public Boolean subscribeToWeatherAlert(WeatherAlerter wa) {
		return weatherAlerters.add(wa);
	}

	/**
	 * Calls the Alert() method in all WeatherAlerter objects currently
	 * subscribed to the weather model.
	 *
	 */
	public void notifyWeatherAlerters() {
		for (WeatherAlerter wa : weatherAlerters) {
			wa.Alert();
		}
	}

	/**
	 * (Re)Generates the forecast pseudo randomly, given the existing climate
	 * factor.
	 *
	 * @return Boolean If the process succeeded or not.
	 */
	private Boolean regenerateForecast() {
		this.currentForecastPosition = 0;
		Random r = new Random();
		int nextWeatherState = 0;
		// For each position in forecast
		for (int i = forecastLength - 1; i >= 0; i--) {
			// Roll the D100
			nextWeatherState = r.nextInt(100);
			// Improve Weather?
			if (nextWeatherState >= climateFactor) {
				if (improveWeather(forecastLength - 1 - i)) {
					continue;
				} else {
					return false;
				}
			// Deteriorate Weather
			} else {
				if (worsenWeather(forecastLength - 1 - i)) {
				} else {
					return false;
				}
			}
		}
		return true;
	}

	public int getCurrentForecastPosition() {
		return currentForecastPosition;
	}

	/**
	 * Moves the weather along by 1 in the forecast.
	 *
	 * @return Boolean True if no catastrophic failure
	 */
	public Boolean nextWeather() {
		// Move along by 1
		this.currentForecastPosition++;
		// Are we at end of forecast?
		if (this.currentForecastPosition > forecastLength - 1) {
			// Reset counter
			this.currentForecastPosition = 0;
			// Create new forecast
			if (regenerateForecast()) {
				// Notify about new forecast
				notifyWeatherAlerters();
				return true;
			} else {
				return false;
			}

		} else {
			return true;
		}
	}
	
	public int getClimateFactor() {
		return this.climateFactor;
	}
}
