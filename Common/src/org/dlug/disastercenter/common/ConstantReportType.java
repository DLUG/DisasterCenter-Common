package org.dlug.disastercenter.common;

import java.util.HashMap;
import java.util.Map;

public class ConstantReportType {
	private static Map<Integer, String> typeMap;
	
	public static final int PUBLIC_DATA_REPORT = 1000;
	public static final int USER_REPORT = 2000;
	
	public final static int KMA_FORCAST = 1101;
	public final static int KMA_CURRENT = 1201;
	
	public final static int DAUM_CURRENT = 1202;
	
	static{
		typeMap = new HashMap<Integer, String>();
		typeMap.put(0, "공공DB (OLD)");
		typeMap.put(1, "사용자신고 (OLD)");
		typeMap.put(PUBLIC_DATA_REPORT, "공공DB");
		typeMap.put(USER_REPORT, "사용자신고");
		typeMap.put(KMA_FORCAST, "기상청 예보");
		typeMap.put(KMA_CURRENT, "기상청 현재기상");
		typeMap.put(DAUM_CURRENT, "Daum 기상센서 현재기상");
	}
	
	public static String code2string(int code){
		if(typeMap.containsKey(code))
			return typeMap.get(code);

		return null;
	}
	
	public static Map<Integer, String> getTypeMap(){
		return typeMap;
	}
}
