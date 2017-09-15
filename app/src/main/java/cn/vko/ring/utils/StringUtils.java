package cn.vko.ring.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

public class StringUtils {

	static DecimalFormat df = new DecimalFormat("#0.00");

	public final static boolean isBlank(String str) {
		return null == str || str.equals("");
	}

	
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @return String
	 */
	public static String doEmpty(String str) {
		return doEmpty(str, "");
	}

	public static String decimalFromart(double value) {

		return df.format(value);
	}

	/**
	 * 处理空字符串
	 * 
	 * @param str
	 * @param defaultValue
	 * @return String
	 */
	public static String doEmpty(String str, String defaultValue) {
		if (str == null || str.equalsIgnoreCase("null")
				|| str.trim().equals("")) {
			str = defaultValue;
		} else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}

	/**
	 * 编码
	 * 
	 * @param value
	 * @return
	 */
	public static String encoder(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		} else {
			try {
				return URLEncoder.encode(value,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return value;
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return
	 */
	public static String decoder(String str) {

		try {
			return URLDecoder.decode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public static String Join(String str,List<String> array){
		StringBuffer sb = new StringBuffer(); 
		for(int i =0;i<array.size();i++){
			if (array.get(i) == null || array.get(i) == "") {  
                continue;  
            }  
			sb.append(array.get(i));
			if(i < (array.size()-1)){
				sb.append(str);
			}
		}
		return sb.toString();
	}
	
	/**
	  * 将map转化为json字符串
	  * 
	  * @param map
	  * @return
	  */
	 public static String MapToJson(Map<String,String> map) {
	  String json = "";
	  if (map != null) {
		  json = JSON.toJSONString(map,true); 
	  }
	  return json;
	 }

	public static String StringToInt(String s) {
		Double dou = Double.valueOf(s);
		int in = dou.intValue();
		String str = secToTime(in);
		return str;
	}
	public static String secToTime(int time) {
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00:00";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = unitFormat(minute) + ":" + unitFormat(second);
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99:59:59";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":"
						+ unitFormat(second);
			}
		}
		return timeStr;
	}
	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}
	public static String LongToString(Long l){
		Long in=l/1024/1024;
		String str=String.valueOf(in);
		return str;
	}
	
}
