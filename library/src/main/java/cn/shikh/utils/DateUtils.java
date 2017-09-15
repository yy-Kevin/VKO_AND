package cn.shikh.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
	private static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DAY_FPRMAT = "yyyy-MM-dd";
	static SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd ");
	static SimpleDateFormat DAY_HOUR_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat HOUR_FORMAT = new SimpleDateFormat("HH:mm");

	
	/**
	 * 是否为同一天
	 * @param firstTime
	 * @param secTime
	 * @return
	 */
	public static boolean isSamdDay(long firstTime,long secTime){
		return DAY_FORMAT.format(new Date(firstTime)).equals(DAY_FORMAT.format(new Date(secTime)));
	}
	/**
	 *  是否为当天
	 * @param time
	 * @return
	 */
	public static boolean isToday(long time){
		return DAY_FORMAT.format(new Date(time)).equals(DAY_FORMAT.format(new Date()));
	}

	/**
	 * 将日期转化成 今天+时间或
	 * @param date
	 * @return
     */
	public static String dateToRelativeTime(Date date) {
		if (DAY_FORMAT.format(date).equals(DAY_FORMAT.format(new Date()))) { // 今天
			return "今天 ".concat(HOUR_FORMAT.format(date));

		} else { //
			return DAY_HOUR_FORMAT.format(date);
		}

	}
	/**
	 * 将时间转化成 今天+时间或
	 * @param
	 * @return
	 */
	public static String dateToRelativeTime(long time) {
		return dateToRelativeTime(new Date(time));
	}

	/**
	 * 将String 的时间转化成 今天+时间或
	 * @param time
	 * @return
     */
	public static String dateToRelativeTime(String time) {
		if (time == null || "".equals(time)) {
			return "";
		}
		try {
			Date  date = DAY_HOUR_FORMAT.parse(time);
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");//小写的mm表示的是分钟  
//			java.util.Date date=sdf.parse(time); 
			return dateToRelativeTime(date);
		} catch (Exception e) {
			return time;
		}

		

	}

	public static String formatDate( long time) {
		return DAY_FORMAT.format(new Date(time));
	}
	
	public static String formatDate(String format, long time) {
		Date date = new Date(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	public static String formatDate(Date time) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_FORMAT);
		return simpleDateFormat.format(time);
	}
	
	public static String formatDayDate(Date time) {
		if(time == null) return "";
		return DAY_FORMAT.format(time);
	}
	
	
	public static Date stringToDate(String strTime) {
		Date date = null;
		try {
			SimpleDateFormat formatter;
			if(strTime.length() == 19){
				 formatter = new SimpleDateFormat(DEFAULT_FORMAT);
			}else{
				formatter = new SimpleDateFormat(DAY_FPRMAT);
			}
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	public static Date stringToDate(String strTime,String format) {
		Date date = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	
	public static Long stringToLong(String time){
		String t = null;
		if(time!=null){
			if(time.length()>19){
				t=time.substring(0, 19);
			}else if(time.length() >10){
				t=time.substring(0, 10);
			}else{
				t=time;
			}
		}
		if(t != null){
			System.out.println("--------t----"+t);
			return stringToDate(t).getTime();
		}
		return null;
	}

	public static String getCurDateStr() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
				+ c.get(Calendar.DAY_OF_MONTH) + " "
				+ c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
				+ ":" + c.get(Calendar.SECOND);
	}

	public static Date getCurDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		try {
			Date now = new Date();
			String test = sdf.format(now).toString();
			return sdf.parse(test);
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	public static String getFromData(String time){
		Calendar calendar = Calendar.getInstance();
		try {
			if (!TextUtils.isEmpty(time)) {
				calendar.setTime(DAY_FORMAT.parse(time));
			}
			return DAY_FORMAT.format(calendar.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param data1
	 * @param data2
	 * @return data1 是否在 data 之前
	 */
	public static boolean isCompareData(String data1,String data2){
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT, Locale.CHINA);
		try {
			Date d1 = sdf.parse(data1);
			Date d2 = sdf.parse(data2);
			return d1.before(d2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static String currentData() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DAY_FPRMAT);
		return simpleDateFormat.format(new Date());
	}
	
	public static String currentData(String dataFprmat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataFprmat);
		return simpleDateFormat.format(new Date());
	}


}
