package fi.utu.tech.weatherInfo;

/*
 * Class presenting current weather
 * Is returned by  weather service class
 */

public class WeatherData {

	private boolean isRaining;
	private double temperature;

	/*
	 * What kind of data is needed? What are the variable types. Define class
	 * variables to hold the data
	 */

	/*
	 * Since this class is only a container for weather data we only need to set the
	 * data in the constructor.
	 */

	public WeatherData() {

	}

	public WeatherData(boolean isRaining,double temperature){
		this.isRaining = isRaining;
		this.temperature = temperature;
	}

	public boolean getIsRaining() {
		return this.isRaining;
	}

	public double getTemperature() {
		return this.temperature;
	}
}
