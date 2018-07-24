package com.coddy.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * LogUtils Log������
 * 
 * @author shenbing
 * 
 */
public class LogUtils {
	/**
	 * ����log4j�����ļ�
	 */
	private static void logConfig() {
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/conf/log4j.properties");
	}

	/**
	 * ��ȡ��־���
	 * 
	 * @param t
	 *            Class����
	 * @return Log����
	 */
	@SuppressWarnings("rawtypes")
	public static Log getLog(Class t) {
		logConfig();
		return LogFactory.getLog(t);
	}

	/**
	 * ��ȡ��־���
	 * 
	 * @param t
	 *            Class��������
	 * @return Log����
	 */
	public static Log getLog(String t) {
		logConfig();
		return LogFactory.getLog(t);
	}

}
