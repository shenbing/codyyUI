package com.coddy.check;

import org.apache.commons.logging.Log;
import org.testng.Assert;
import com.coddy.utils.LogUtils;
import com.coddy.utils.ScreenCapture;

/**
 * ���ݿ������
 * 
 * @author shenbing
 * 
 */
public class DBcheck
{
	/**
	 * ��־���
	 */
	public static Log log = LogUtils.getLog(DBcheck.class);

	/**
	 * ���������Ƿ���ͬ
	 * 
	 */
	public static void verfiyEquals(Integer actual, Integer expected,
			String title)
	{
		if ((expected == null) || (actual == null))
		{
			return;
		}

		else if (actual == expected)
		{
			return;
		}
		else
		{
			String message = actual + " is not equal with " + expected;
			ScreenCapture.screenShot(title);
			throw new AssertionError(message);
		}
	}

	/**
	 * �����ַ����Ƿ���ͬ������ʧ�ܺ�JAVA����
	 * 
	 * @param actual
	 *            ʵ���ַ���
	 * @param expected
	 *            �����ַ���
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyEquals(String actual, String expected, String title)
	{
		try
		{
			Assert.assertEquals(actual, expected);
		}
		catch (AssertionError e)
		{
			ScreenCapture.screenShot(title);
			throw new AssertionError(e);
		}

	}

	/**
	 * �����ַ����Ƿ���ͬ������ʧ�ܺ�JAVA����
	 * 
	 * @param actual
	 *            ʵ���ַ���
	 * @param expected
	 *            �����ַ���
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyUnEquals(String actual, String expected,
			String title)
	{
		if ((expected == null) || (actual == null))
		{
			return;
		}
		else if (actual.equals(expected))
		{
			String message = actual + " is equal with " + expected;
			ScreenCapture.screenShot(title);
			throw new AssertionError(message);
		}
		else
		{
			return;
		}
	}
}
