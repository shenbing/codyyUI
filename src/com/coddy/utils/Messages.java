package com.coddy.utils;

import java.io.UnsupportedEncodingException;
import java.util.ResourceBundle;

/**
 * Messages ͳһ��Դ��
 * 
 * @author shenbing
 * 
 */
public class Messages {
	/**
	 * ��Դ�ļ���
	 */
	private static final String BUNDLE_NAME = "conf";

	/**
	 * ResourceBundleʵ��
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	/**
	 * Ĭ�Ϲ��췽��
	 */
	private Messages() {
	}

	/**
	 * ��ȡ��Դ�ļ��е�ֵ
	 * 
	 * @param key
	 *            ��Դ�ļ���keyֵ
	 * @return ������Դ�ļ���key��Ӧ��valueֵ
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