package org.dlug.disastercenter.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ApiDaumSensor {
	private ApiDaumSensor instance = new ApiDaumSensor();

	public final static SimpleDateFormat DATE_FORMAT_DAUM = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	public final static SimpleDateFormat DATE_FORMAT_DAUM_FOR_QUERY = new SimpleDateFormat("yyyyMMddHHmm");
	
	private final static String API_URL = "http://apis.daum.net/sensorql/weather.json";
	private static String API_KEY;
	
	
	private ApiDaumSensor(){
		Properties apiProperty = new Properties();
		try {
			apiProperty.load(this.getClass().getClassLoader().getResourceAsStream("apis.property"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		API_KEY = apiProperty.getProperty("daumapi.weathersensor");
	}
	
	public static Date convertDateType(JSONObject daumDate){
		try {
			return DATE_FORMAT_DAUM.parse((String) daumDate.get("$date"));
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static long getSensorAmount(){
		String query = "SELECT count(resourceId) FROM sensorMetaCollection;";
		JSONArray resultArray = getJSON(query);
		
		if(resultArray == null){
			System.out.println("ApiDaumSensor: API_ERROR");
			return 0;
		}
		
		return (Long) ((JSONObject) resultArray.get(0)).get("count");
	}
	
	public static JSONArray getJSONSensors(){
		String query = "SELECT * FROM sensorMetaCollection;";

		return getJSON(query);
	}
	
	public static JSONArray getJSONSensors(int offset){
		String query = "SELECT * FROM sensorMetaCollection LIMIT " + offset + ", 50;";

		return getJSON(query);
	}
	
	public static JSONArray getJSONHotestTempYesterday(int sensorId){
		Calendar startCal = Calendar.getInstance();
		startCal.add(Calendar.DATE, -1);
		Calendar endCal = Calendar.getInstance();
		
		startCal.add(Calendar.HOUR_OF_DAY, 12);
		endCal.setTime(startCal.getTime());
		endCal.add(Calendar.HOUR_OF_DAY, 5);
		
		String startDatetime = DATE_FORMAT_DAUM_FOR_QUERY.format(startCal.getTime());
		String endDatetime = DATE_FORMAT_DAUM_FOR_QUERY.format(endCal.getTime());
		String query = "SELECT * FROM sensorRecortCollection WHERE observationTime > " + startDatetime 
				+ " AND observationTime < " + endDatetime
				+ " SORT temperature DESC LIMIT 0, 1;";

		return getJSON(query);
	}
	
	public static JSONArray getJSONLowestHumidityYesterday(int sensorId){
		Calendar startCal = Calendar.getInstance();
		startCal.add(Calendar.DATE, -1);
		Calendar endCal = Calendar.getInstance();
		
		startCal.add(Calendar.HOUR_OF_DAY, 12);
		endCal.setTime(startCal.getTime());
		endCal.add(Calendar.HOUR_OF_DAY, 5);
		
		String startDatetime = DATE_FORMAT_DAUM_FOR_QUERY.format(startCal.getTime());
		String endDatetime = DATE_FORMAT_DAUM_FOR_QUERY.format(endCal.getTime());
		String query = "SELECT * FROM sensorRecortCollection WHERE observationTime > " + startDatetime 
				+ " AND observationTime < " + endDatetime
				+ " SORT humidity ASC LIMIT 0, 1;";

		return getJSON(query);
	}
	
	public static JSONArray getJSONColdestTempYesterday(int sensorId){
		Calendar startCal = Calendar.getInstance();
		startCal.add(Calendar.DATE, -1);
		Calendar endCal = Calendar.getInstance();
		
		startCal.add(Calendar.HOUR_OF_DAY, 4);
		endCal.setTime(startCal.getTime());
		endCal.add(Calendar.HOUR_OF_DAY, 4);
		
		String startDatetime = DATE_FORMAT_DAUM_FOR_QUERY.format(startCal.getTime());
		String endDatetime = DATE_FORMAT_DAUM_FOR_QUERY.format(endCal.getTime());
		String query = "SELECT * FROM sensorRecortCollection WHERE observationTime > " + startDatetime 
				+ " AND observationTime < " + endDatetime
				+ " SORT temperature ASC LIMIT 0, 1;";

		return getJSON(query);
	}
	
	public static JSONArray getJSONSensorHotterThanWatchLimit(int period){
		Date thisDatetime = new Date();
		
		long startTimestamp = thisDatetime.getTime() - period;
		Date startDatetime = new Date(startTimestamp);
		
		String startDatetimeString = DATE_FORMAT_DAUM_FOR_QUERY.format(startDatetime);
		String query = "SELECT * FROM sensorRecordCollection WHERE temperature > 330 AND observationTime > " + startDatetimeString;
		
		return getJSON(query);
	}
	
	public static JSONArray getJSONSensorFasterWindThanWatchLimit(int period){
		Date thisDatetime = new Date();
		
		long startTimestamp = thisDatetime.getTime() - period;
		Date startDatetime = new Date(startTimestamp);
		
		String startDatetimeString = DATE_FORMAT_DAUM_FOR_QUERY.format(startDatetime);
		String query = "SELECT * FROM sensorRecordCollection WHERE windSpeed > 140 AND observationTime > " + startDatetimeString;
		
		return getJSON(query);
	}
	
	private static JSONArray getJSON(String query){
		try {
			query = URLEncoder.encode(query, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String requestUrl = API_URL + "?apikey=" + API_KEY + "&q=" + query;
		
		try {
			URL url;
			url = new URL(requestUrl);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
			String result = "";
			String inputLine;
			
			while ((inputLine = reader.readLine()) != null){
				result += inputLine;
			}

			JSONParser parser = new JSONParser();
			
			JSONObject json = (JSONObject) parser.parse(result);
			
			return (JSONArray) json.get("sensorQL");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}