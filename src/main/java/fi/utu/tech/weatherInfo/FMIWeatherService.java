package fi.utu.tech.weatherInfo;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

public class FMIWeatherService {

	private final String CapURL = "https://opendata.fmi.fi/wfs?request=GetCapabilities";
	private final String FeaURL = "https://opendata.fmi.fi/wfs?request=GetFeature";
	private final String ValURL = "https://opendata.fmi.fi/wfs?request=GetPropertyValue";
	private final String DataURL = "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::forecast::hirlam::surface::point::multipointcoverage&place=turku&";

	/*
	 * In this method your are required to fetch weather data from The Finnish
	 * Meteorological Institute. The data is received in XML-format.
	 */

	public static WeatherData getWeather() {

		WeatherData weatherData = new WeatherData();

		return weatherData;
	}

}
