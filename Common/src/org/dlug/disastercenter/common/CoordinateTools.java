package org.dlug.disastercenter.common;

public class CoordinateTools {
	private final static double LAT_DEGREE_PER_KM = 0.008983;
	private final static double LNG_DEGREE_PER_KM = 0.015060;
	
	private final static double LAT_DEGREE_PER_KMA_Y = 0.045714229;
	private final static double LNG_DEGREE_PER_KMA_X = 0.052940268;
	private final static double INCREASE_RATIO_PER_LAT = 0.136325514;
	
	private final static int KMA_AMOUNT_X = 149;
	private final static int KMA_AMOUNT_Y = 253;
	
	private final static double KMA_EARTH_RADIUS = 6371.00877;
	private final static double KMA_MAP_GRID = 5;
	private final static double KMA_MAP_STANDARD_LAT1 = 30.0;
	private final static double KMA_MAP_STANDARD_LAT2 = 60.0;
	private final static double KMA_MAP_ORIGIN_LNG = 126.0;
	private final static double KMA_MAP_ORIGIN_LAT = 38.0;
	private final static double KMA_MAP_ORIGIN_X = 210 / KMA_MAP_GRID;
	private final static double KMA_MAP_ORIGIN_Y = 675 / KMA_MAP_GRID;

	private final static double PI = Math.asin(1.0) * 2.0;
	private final static double DEGRAD = PI / 180.0;
	private final static double RADDEG = 180.0 / PI;
	
	private static double re, standardLat1, standardLat2, originLng, originLat, sn, sf, ro;
	
	static{
		re = KMA_EARTH_RADIUS / KMA_MAP_GRID;
		standardLat1 = KMA_MAP_STANDARD_LAT1 * DEGRAD;
		standardLat2 = KMA_MAP_STANDARD_LAT2 * DEGRAD;
		originLng = KMA_MAP_ORIGIN_LNG * DEGRAD;
		originLat = KMA_MAP_ORIGIN_LAT * DEGRAD;
		
		sn = Math.tan(PI * 0.25 + standardLat2 * 0.5) / Math.tan(PI * 0.25 + standardLat1 * 0.5);
		sn = Math.log(Math.cos(standardLat1) / Math.cos(standardLat2)) / Math.log(sn);
		
		sf = Math.tan(PI * 0.25 + standardLat1 * 0.5);
		sf = Math.pow(sf, sn) * Math.cos(standardLat1) / sn;
		
		ro = Math.tan(PI * 0.25 + originLat * 0.5);
		ro = re * sf / Math.pow(ro,  sn);
	}
	
	public static class CoordKma{
		public int x;
		public int y;
	}
	
	public static class CoordLatLng{
		public double lat;
		public double lng;
	}
	
	public static double latDegree2KM(double latitudeDegree){
		return latitudeDegree / LAT_DEGREE_PER_KM;
	}
	
	public static double lngDegree2KM(double longitudeDegree){
		return longitudeDegree / LNG_DEGREE_PER_KM;
	}

/*	
	public static CoordKma latlng2Kma(double latitude, double longitude){
		double defaultX = longitude - 123.3102;
		double defaultY = latitude - 31.6518;
				
		CoordKma result = new CoordKma();
		
		result.x = (int) ((defaultX + (defaultY * INCREASE_RATIO_PER_LAT)) / LNG_DEGREE_PER_KMA_X);
		result.y = (int) (defaultY / LAT_DEGREE_PER_KMA_Y);
		
		return result;
	}
	
	public static CoordLatLng Kma2latlng(int kmaX, int kmaY){
		double defaultY = kmaY * LAT_DEGREE_PER_KMA_Y;
		double defaultX = (kmaX * (LNG_DEGREE_PER_KMA_X) - (defaultY * INCREASE_RATIO_PER_LAT));
				
		CoordLatLng result = new CoordLatLng();
		
		result.lat = defaultY + 31.6518;
		result.lng = defaultX + 123.3102;
		
		return result;
	}
*/
	
	public static double calcDistance(double srcLat, double srcLng, double dstLat, double dstLng){
		double degreeDiffLat = Math.asin(dstLat - srcLat);
		double degreeDiffLng = Math.asin(dstLng - srcLng);

		double xLength = lngDegree2KM(degreeDiffLng);
		double yLength = latDegree2KM(degreeDiffLat);
		
		return Math.sqrt((xLength * xLength) + (yLength * yLength));
	}
	
	public static CoordLatLng Kma2latlng(int kmaX, int kmaY){
		if(kmaX < 1 || kmaX > KMA_AMOUNT_X || kmaY < 1 || kmaY > KMA_AMOUNT_Y)
			return null;
		
		double xn = kmaX - KMA_MAP_ORIGIN_X - 1;
		double yn = ro - kmaY + KMA_MAP_ORIGIN_Y - 1;
		
		double ra = Math.sqrt(xn * xn + yn * yn);
		
		if(sn < 0.0)
			--ra;
		
		double alat = Math.pow((re*sf / ra), (1.0 /sn));
		alat = 2.0 * Math.atan(alat) - PI * 0.5;
		
		double theta;
		if(Math.abs(xn) <= 0.0){
			theta = 0.0;
		} else {
			if(Math.abs(yn) <= 0.0){
				theta = PI * 0.5;
				if(xn < 0)
					--theta;
			} else
				theta = Math.atan2(xn, yn);
		}
		
		double alon = theta / sn + originLng;
		
		CoordLatLng result = new CoordLatLng();
		result.lat = (alat * RADDEG);
		result.lng = (alon * RADDEG);
		
		return result;
	}
	
	public static CoordKma latlng2Kma(double lat, double lng){
		
		double ra = Math.tan(PI * 0.25 + lat * DEGRAD * 0.5);
		ra = re * sf / Math.pow(ra,  sn);
		
		double theta = lng * DEGRAD - originLng;
		
		if(theta > PI)
			theta -= 2.0 * PI;
		
		if(theta < -PI)
			theta += 2.0 * PI;
		
		theta *= sn;
		
		CoordKma result = new CoordKma();
		result.x = (int) (((ra * Math.sin(theta)) + KMA_MAP_ORIGIN_X) + 1.5);
		result.y = (int) (((ro - ra * Math.cos(theta)) + KMA_MAP_ORIGIN_Y) + 1.5);
		
		return result;
	}
}
