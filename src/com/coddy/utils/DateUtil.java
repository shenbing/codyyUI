package com.coddy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar calendar = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(calendar.getTime());
	}

	/**
	 * 获取今天日期，格式为yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getToday() {
		Calendar calendar = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(calendar.getTime());
	}

	/**
	 * 判断当前日期是星期几
	 * 
	 * @param pTime
	 *            修要判断的时间
	 * @return dayForWeek 判断结果
	 * @Exception 发生异常
	 */
	public static int dayForWeek(String pTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			System.out.println(pTime + ":日期格式错误");
		}
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}

	/**
	 * 计算指定日期之前或之后天数的日期
	 * 
	 * @param currentDate
	 *            修要判断的时间,格式:yyyy-MM-dd
	 * @param days
	 *            -号开头表示向前推算日期，+号开头表示向后推算日期
	 * @return specialDate 计算后结果
	 * @throws Exception
	 * @Exception 发生异常
	 */
	public static String getSpecialDay(String currentDate, String days) {
		String specialDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(currentDate));
		} catch (ParseException e) {
			System.out.println(currentDate + ":日期格式错误");
		}
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + Integer.valueOf(days));
		specialDate = format.format(calendar.getTime());
		return specialDate;
	}

	/**
	 * 通过时间秒毫秒数判断两个时间的间隔天数
	 * 
	 * @param beginDate
	 *            开始日期
	 * @param endDate
	 *            结束日期
	 * @return
	 */
	public static int differentDaysByMillisecond(Date beginDate, Date endDate) {
		int days = (int) ((endDate.getTime() - beginDate.getTime()) / (1000 * 3600 * 24));
		return days;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(dayForWeek("2018-03-19"));
		System.out.println(getToday());
	}
}
