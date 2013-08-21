package org.dlug.disastercenter.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantDisasterType {
	private static Map<Integer, String> typeMap = new HashMap<Integer, String>();
	private static List<Integer> typeList = new ArrayList<Integer>();
	
	public static final int RAIN_HARD_WATCH = 901;
	public static final int RAIN_HARD_ALERT = 902;
	
	public static final int WIND_FAST_WATCH = 903;
	public static final int WIND_FAST_ALERT = 904;
	
	public static final int TEMP_HIGH_WATCH = 905;
	public static final int TEMP_HIGH_ALERT = 906;
	
	public static final int TEMP_LOW_WATCH = 907;
	public static final int TEMP_LOW_ALERT = 908;
	
	static{
		Object[] data = {
				100, "수해",
					101, "폭우",
					102, "홍수",
					103, "해일",
					104, "태풍",
				200, "눈",
					201, "폭설",
					202, "눈사태",
					203, "혹한",
				300, "더위",
					301, "폭염",
					302, "열대야",
				400, "붕괴",
					401, "건물붕괴",
					402, "교량붕괴",
					403, "산사태",
					404, "지진",
				500, "화재",
					501, "화재",
					502, "산불",
				600, "대기오염",
					601, "스모그",
					602, "방사능",
					603, "미세먼지",
					604, "황사",
				700, "생화학",
					701, "호흡기 감염",
					702, "소화기 감염",
					703, "눈병",
				800, "기타",
					801, "전쟁",
				900, "기상청용",
					RAIN_HARD_WATCH, "호우주의보",
					RAIN_HARD_ALERT, "호우경보",
					WIND_FAST_WATCH, "강풍주의보",
					WIND_FAST_ALERT, "강풍경보",
					TEMP_HIGH_WATCH, "폭염주의보",
					TEMP_HIGH_ALERT, "폭염경보",
					TEMP_LOW_WATCH, "한파주의보",
					TEMP_LOW_ALERT, "한파경보"
		};
		
		for(int i = 0; i < data.length; i += 2){
			typeList.add((Integer) data[i]);
			typeMap.put((Integer) data[i], (String) data[i + 1]);
		}
	}
	
	public static String code2string(int code){
		return typeMap.get(code);
	}
	
	public static List<Integer> getTypeList(){
		return typeList;
	}
	
	public static Map<Integer, String> getTypeMap(){
		return typeMap;
	}
}
