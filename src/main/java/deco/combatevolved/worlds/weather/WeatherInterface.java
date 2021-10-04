package deco.combatevolved.worlds.weather;

import deco.combatevolved.worlds.weather.WeatherAlerter;

/**
 * An interface used for weather. 
 * Use this interface to know how weather is modeled in the game!
 */
public interface WeatherInterface {

	/**
	 * A function used to obtain the current weather state of the player's
	 * location.
	 * 
	 * @return WeatherState An Enum representation of the current weather state
	 *         0 - plain 1 - rain 2 - storm
	 */
	public WeatherState getWeatherState();

	/**
	 * A function used to obtain the forecast of the world's future weather.
	 * 
	 * @return an array of future WeatherStates (array of Enums)
	 */
	public WeatherState[] getForecast();

	/**
	 * A function which returns true if an improvement to the weather has been
	 * made.
	 * 
	 * @return boolean to show that the improvement has been made
	 */
	public Boolean improveWeather();

	/**
	 * A function which returns true if the weather has been successfully
	 * deteriorated.
	 * 
	 * @return boolean to show change has been made
	 */
	public Boolean worsenWeather();
	
	/**
	 * A function which returns true if an improvement to the forecast has been
	 * made.
	 * 
	 * @return boolean to show that the improvement has been made
	 */
	public Boolean improveForecast();

	/**
	 * A function which returns true if the forecast has been successfully
	 * deteriorated.
	 * 
	 * @return boolean to show change has been made
	 */
	public Boolean worsenForecast();

	/**
	 * Pass your implemented WeatherAlerter object to this method so it may
	 * receive weather alerts.
	 * 
	 * @param wa
	 *            Your implemented WeatherAlerter object
	 * @return Boolean True if subscription was successful
	 */
	public Boolean subscribeToWeatherAlert(WeatherAlerter wa);
}
