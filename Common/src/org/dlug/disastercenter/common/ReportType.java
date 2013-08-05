package org.dlug.disastercenter.common;

public class ReportType {
	public static final int PUBLIC_DATA_REPORT = 0;
	public static final int USER_REPORT = 1;
	
	public static String code2string(int code){
		if(code == 0)
			return "공공DB";
		else {
			return "사용자신고";
		}
	}
}
