package com.weipai.common;

import java.util.Properties;



public  abstract class Params {
	
	static Properties properties = AppCf.getProperties();
	//获取红包提现申请分页条数
	public static final  Integer pageNumber = Integer.valueOf(properties.get("pageNumber").toString());
	public static final  double percentage1 = Double.valueOf(properties.get("percentage1").toString());
	public static final  double percentage2 = Double.valueOf(properties.get("percentage2").toString());
	
	public static double getPercentage1(int power){
		return Double.valueOf(properties.get("percentage1_"+power).toString());
	}
	
	public static double getPercentage2(int power){
		return Double.valueOf(properties.get("percentage2_"+power).toString());
	}
	
	public static String getImgPath(){
		return properties.get("abspath").toString();
	}
	
}
