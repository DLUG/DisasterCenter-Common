package org.dlug.disastercenter.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ApiDaumLocal {
	private final static String API_URL = "http://apis.daum.net/local/geo/coord2addr";
	private final static String API_KEY = "DAUM_LOCAL_DEMO_APIKEY";
	
	private final static String COORD_SYSTEM = "wgs84";
	
	public static String getAddress(double lat, double lng){
		JSONObject queryResult = getJSON(lat, lng);
		
		return queryResult.get("name1") + " " + queryResult.get("name2"); 
	}
	
	private static JSONObject getJSON(double lat, double lng){
		String requestUrl = API_URL + "?apikey=" + API_KEY 
				+ "&latitude=" + lat + "&longitude=" + lng 
				+ "&inputCoordSystem=" + COORD_SYSTEM
				+ "&output=json";
		
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
			
			return (JSONObject) parser.parse(result);
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