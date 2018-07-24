package com.coddy.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

/**
 * LogUtils Log工厂类
 * 
 * @author shenbing
 * 
 */
public class LogUtils {
	/**
	 * 加载log4j配置文件
	 */
	private static void logConfig() {
		PropertyConfigurator.configure(System.getProperty("user.dir") + "/conf/log4j.properties");
	}

	/**
	 * 获取日志句柄
	 * 
	 * @param t
	 *            Class对象
	 * @return Log对象
	 */
	@SuppressWarnings("rawtypes")
	public static Log getLog(Class t) {
		logConfig();
		return LogFactory.getLog(t);
	}

	/**
	 * 获取日志句柄
	 * 
	 * @param t
	 *            Class对象名称
	 * @return Log对象
	 */
	public static Log getLog(String t) {
		logConfig();
		return LogFactory.getLog(t);
	}

}
