package fi.utu.tech.weatherInfo;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FMIWeatherService {

	private final String CapURL = "https://opendata.fmi.fi/wfs?request=GetCapabilities";
	private final String FeaURL = "https://opendata.fmi.fi/wfs?request=GetFeature";
	private final String ValURL = "https://opendata.fmi.fi/wfs?request=GetPropertyValue";
	private static final String DataURL = "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::forecast::hirlam::surface::point::multipointcoverage&place=turku&";

	/*
	 * In this method your are required to fetch weather data from The Finnish
	 * Meteorological Institute. The data is received in XML-format.
	 */

	public static WeatherData getWeather() {

		WeatherData weatherData = null;
		try {
			weatherData = new WeatherData(isRaining(),getAvgTemperature());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return weatherData;
	}

	public static boolean isRaining() throws Exception {
		URL queryURL = new URL(DataURL+"parameters=Humidity");
		//connect to URL
		HttpURLConnection con = (HttpURLConnection) queryURL.openConnection();
		//setup connection
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept","text/xml");
		//read XML
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		dbf.setValidating(false);
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.parse(con.getInputStream());
		doc.getDocumentElement().normalize();
		//print status
		System.out.println("Connect to humidity API successfully");
		//get the only node under tag "gml:dou..."
		String rawValue = doc.getElementsByTagName("gml:doubleOrNilReasonTupleList").item(0).getFirstChild().getNodeValue();
		String[] items = rawValue.replaceAll(" ","").split("\n");

		//process values return from API
		boolean result = false;
		for(int i=1;i<items.length-1;i++){
			double value = Double.valueOf(items[i]);
			if(value==100.00){
				result = true;
				break;
			}
		}
		return result;
	}

	public static double getAvgTemperature() throws Exception{
		URL queryURL = new URL(DataURL+"parameters=Temperature");
		//connect to URL
		HttpURLConnection con = (HttpURLConnection) queryURL.openConnection();
		//setup connection
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept","text/xml");
		//read XML
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(false);
		dbf.setValidating(false);
		DocumentBuilder builder = dbf.newDocumentBuilder();
		Document doc = builder.parse(con.getInputStream());
		doc.getDocumentElement().normalize();
		//print status
		System.out.println("Connect to temperature API successfully");
		//get the only node under tag "gml:dou..."
		String rawValue = doc.getElementsByTagName("gml:doubleOrNilReasonTupleList").item(0).getFirstChild().getNodeValue();
		String[] items = rawValue.replaceAll(" ","").split("\n");

		//process value return from API, get average temperature
		double sum=0;
		for(int i=1;i<items.length-1;i++){
			double value = Double.valueOf(items[i]);
			sum += value;
		}//for
		return sum/ items.length-2;
	}

	public static void main(String[] args) {
		try {
			System.out.println(FMIWeatherService.isRaining());
			System.out.println(FMIWeatherService.getAvgTemperature());
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
