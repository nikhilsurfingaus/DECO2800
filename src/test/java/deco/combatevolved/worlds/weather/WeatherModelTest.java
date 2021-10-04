/**
 * 
 */
package deco.combatevolved.worlds.weather;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import deco.combatevolved.worlds.weather.WeatherState;

/**
 * @author Angus
 *
 */
public class WeatherModelTest {

	private WeatherModel WM;
	private WeatherAlerter mockedWA;

	/**
	 *
	 *
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.WM = new WeatherModel();
	}

	/**
	 *
	 *
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link deco.combatevolved.worlds.weather.WeatherModel#WeatherModel()}.
	 */
	@Test
	public void testWeatherModel() {
		assertFalse(this.WM.equals(null));
		assertFalse(WM.getWeatherState().equals(null));
		assertFalse(WM.getForecast().equals(null));
	}

	/**
	 * Test method for
	 * {@link deco.combatevolved.worlds.weather.WeatherModel#getWeatherState()}.
	 */
	@Test
	public void testGetWeatherState() {
		assertFalse(WM.getWeatherState().equals(null));
	}

	/**
	 * Test method for
	 * {@link deco.combatevolved.worlds.weather.WeatherModel#getForcast()}.
	 */
	@Test
	public void testGetForecast() {
		assertFalse(WM.getForecast().equals(null));
		WeatherState[] WMF = WM.getForecast();

		assertTrue(WMF.length == 16);
		for (int i = 0; i < 16; i++) {
			assertFalse(WMF[i].equals(null));
			// System.out.println(WMF[i]);
		}
	}

	/**
	 * Test method for
	 * {@link deco.combatevolved.worlds.weather.WeatherModel#improveWeather()}
	 * and
	 * {@link deco.combatevolved.worlds.weather.WeatherModel#worsenWeather()}.
	 */
	@Test
	public void testImproveAndWorsenWeather() {
		WeatherState[] beforeF = WM.getForecast();
		WeatherState before = WM.getWeatherState();

//		printForecast();
		testWeatherCongruency();

		if (before.equals(WeatherState.WEATHER_STATE_0)) {
			WM.worsenWeather();
			assertFalse(before.equals(WM.getWeatherState()));
		} else {
			WM.improveWeather();
//			printForecast();
			assertFalse(before.equals(WM.getWeatherState()));
		}
		
//		printForecast();
		testWeatherCongruency();

		before = WM.getWeatherState();
		if (before.equals(WeatherState.WEATHER_STATE_0)) {
			WM.worsenWeather();
			assertFalse(before.equals(WM.getWeatherState()));
		} else {
			WM.improveWeather();
			assertFalse(before.equals(WM.getWeatherState()));
		}
//		printForecast();
		testWeatherCongruency();
	}

	@Test
	public void improveForecast() {
		// printForecast();
		WM.improveForecast();
		// printForecast();
		testWeatherCongruency();
	}

	@Test
	public void worsenForecast() {
//		printForecast();
		WM.worsenForecast();
//		printForecast();
		testWeatherCongruency();
	}
	
	@Test
	public void nextWeather() {
		int before = WM.getCurrentForecastPosition();
		WM.nextWeather();
		assertFalse(before == WM.getCurrentForecastPosition());
		for(int i = 0; i < 30; i++) {
			WM.nextWeather();
		}
		int pos = WM.getCurrentForecastPosition();
		assertTrue(pos >= 0 && 15 >= pos);
	}

	/**
	 * Test method for
	 * {@link deco.combatevolved.worlds.weather.WeatherModel#notifyWeatherAlerters()}
	 * and
	 * {@link deco.combatevolved.worlds.weather.WeatherModel#subscribeToWeatherAlert(deco.combatevolved.worlds.weather.WeatherAlerter)}.
	 */
	@Test
	public void testNotifyWeatherAlerters() {
		// Create mocked WeatherAlerter instances
		WeatherAlerter mockedWA2 = mock(WeatherAlerter.class);
		WeatherAlerter mockedWA3 = mock(WeatherAlerter.class);

		// Below not necessary, but handy to know for future reference
		// doNothing().when(mockedWA2).Alert();

		// Add to weather model
		WM.subscribeToWeatherAlert(mockedWA2);
		// Have WeatherModel send out notifications
		WM.notifyWeatherAlerters();
		// Verify the notifications were sent
		verify(mockedWA2).Alert();
		// Add the other mocked instance
		WM.subscribeToWeatherAlert(mockedWA3);
		// Call another WeatherModel method that necessitates sending
		// notifications
		WM.improveForecast();

		// Because mockedWA2 was added earlier, should have received two
		// notifications, but mockedWA3 should have received only one
		verify(mockedWA2, times(2)).Alert();
		verify(mockedWA3, times(1)).Alert();

	}
	
	@Test
	public void testGetForecastString() {
		WM.improveForecast();
		assertTrue(WM.getCurrentForecastPosition() == 0);
		
		WeatherState[] testForecast = new WeatherState[WM.getForecast().length];
		for (int i = 0; i < testForecast.length; i++) {
			testForecast[i] = WeatherState.WEATHER_STATE_0;
		}
		
		WM.setForecast(testForecast);
		//k == 0
		assertTrue(WM.getForecastString().equals("[*0*,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]"));
		
		WM.nextWeather();
		//k == 1
		assertTrue(WM.getForecastString().equals("[0,*0*,0,0,0,0,0,0,0,0,0,0,0,0,0,0]"));
		
		for (int i = 0; i < testForecast.length; i++) {
			WM.nextWeather();
		}
		//k == n
		assertTrue(WM.getForecastString().charAt(3) == "*".charAt(0));
		assertTrue(WM.getForecastString().charAt(5) == "*".charAt(0));
	}
	
	@Test
	public void testGetClimateFactor() {
		for(int i = 0; i < 6; i++) {
			WM.worsenForecast();
		}
		assertTrue(WM.getClimateFactor() == 100);
		
		WM.improveForecast();
		
		assertTrue(WM.getClimateFactor() == 80);
	}

	public void testWeatherCongruency() {
		WeatherState[] WMF = WM.getForecast();

		for (int i = 1; i < 16; i++) {
			switch (WMF[i]) {
			case WEATHER_STATE_0:
				// System.out.println("Fine pos: " + i + " state: " + WMF[i]);
				assertFalse(WMF[i - 1].equals(WeatherState.WEATHER_STATE_2));
				continue;
			case WEATHER_STATE_1:
				// System.out.println("Fine pos: " + i + " state: " + WMF[i]);
				continue;
			case WEATHER_STATE_2:
				// System.out.println("Fine pos: " + i + " state: " + WMF[i]);
				assertFalse(WMF[i - 1].equals(WeatherState.WEATHER_STATE_0));
				continue;
			default:
				// System.out.println("!!!! pos: " + i + " state: " + WMF[i]);
				assertFalse(true);
				break;
			}
		}
	}

	@SuppressWarnings("unused")
	public void printForecast() {
		WeatherState[] WMF = WM.getForecast();
		String result = "";
		int WS0 = 0;
		int WS1 = 0;
		int WS2 = 0;

		for (int i = 0; i < 16; i++) {
			result += WMF[i].asInteger() + " ";
			switch (WMF[i]) {
			case WEATHER_STATE_0:
				WS0++;
				continue;
			case WEATHER_STATE_1:
				WS1++;
				continue;
			case WEATHER_STATE_2:
				WS2++;
				continue;
			default:
				assertFalse(true);
				break;
			}
		}
		// System.out.println("0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 ");
		// System.out.println("0s: " + WS0 + " 1s: " + WS1 + " 2s: " + WS2);
		 System.out.println(result);
	}
}
