package com.coddy.utils;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

/**
 * Messages 统一资源类
 * 
 * @author shenbing
 * 
 */
public class Messages {
	/**
	 * 资源文件名
	 */
	private static final String BUNDLE_NAME = "conf";

	/**
	 * ResourceBundle实例
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * 默认构造方法
	 */
	private Messages() {
	}

	/**
	 * 获取资源文件中的值
	 * 
	 * @param key
	 *            资源文件中key值
	 * @return 返回资源文件中key对应的value值
	 */
	public static String getString(String key) {
		String value = RESOURCE_BUNDLE.getString(key);
		try {
			String keyValue = new String(value.getBytes("ISO-8859-1"), "UTF-8");
			return keyValue;
		} catch (UnsupportedEncodingException e) {
			return '!' + key + '!';
		}
	}

	public static void main(String[] args) {
		System.out.println(Messages.getString("AutoTest.ffstart"));
	}
}