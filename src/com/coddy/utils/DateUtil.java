package com.coddy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * ��ȡ��ǰʱ�䣬��ʽΪyyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar calendar = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(calendar.getTime());
	}

	/**
	 * ��ȡ�������ڣ���ʽΪyyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getToday() {
		Calendar calendar = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(calendar.getTime());
	}

	/**
	 * �жϵ�ǰ���������ڼ�
	 * 
	 * @param pTime
	 *            ��Ҫ�жϵ�ʱ��
	 * @return dayForWeek �жϽ��
	 * @Exception �����쳣
	 */
	public static int dayForWeek(String pTime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			System.out.println(pTime + ":���ڸ�ʽ����");
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
	 * ����ָ������֮ǰ��֮������������
	 * 
	 * @param currentDate
	 *            ��Ҫ�жϵ�ʱ��,��ʽ:yyyy-MM-dd
	 * @param days
	 *            -�ſ�ͷ��ʾ��ǰ�������ڣ�+�ſ�ͷ��ʾ�����������
	 * @return specialDate �������
	 * @throws Exception
	 * @Exception �����쳣
	 */
	public static String getSpecialDay(String currentDate, String days) {
		String specialDate = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(format.parse(currentDate));
		} catch (ParseException e) {
			System.out.println(currentDate + ":���ڸ�ʽ����");
		}
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + Integer.valueOf(days));
		specialDate = format.format(calendar.getTime());
		return specialDate;
	}

	/**
	 * ͨ��ʱ����������ж�����ʱ��ļ������
	 * 
	 * @param beginDate
	 *            ��ʼ����
	 * @param endDate
	 *            ��������
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
