package com.coddy.check;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.testng.Assert;
import com.coddy.spring.PageAction;
import com.coddy.utils.LogUtils;
import com.coddy.utils.ScreenCapture;

/**
 * ����������
 * 
 * @author shenbing
 * 
 */
public class ActionCheck
{
	/**
	 * ��־���
	 */
	public static Log log = LogUtils.getLog(ActionCheck.class);

	/**
	 * ���Բ���ֵ������ʧ�ܺ�WebDriver����
	 * 
	 * @param pageAction
	 *            PageAction����
	 * @param status
	 *            Ҫ���Ե�booleanֵ
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyTrue(PageAction pageAction, boolean status,
			String title)
	{
		try
		{
			Assert.assertTrue(status);
		}
		catch (AssertionError e)
		{
			try
			{
				ScreenCapture.screenShot(pageAction.getBrowser(), title);
			}
			catch (IOException e1)
			{
				log.error("WebDriver��ͼʧ�ܣ�");
			}
			throw new AssertionError(e);
		}
	}

	/**
	 * ���Բ���ֵ������ʧ�ܺ�JAVA����
	 * 
	 * @param status
	 *            Ҫ���Ե�booleanֵ
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyTrue(boolean status, String title)
	{
		try
		{
			Assert.assertTrue(status);
		}
		catch (AssertionError e)
		{
			ScreenCapture.screenShot(title);
			throw new AssertionError(e);
		}

	}

	/**
	 * ���Բ���ֵ������ʧ�ܺ�WebDriver����
	 * 
	 * @param pageAction
	 *            PageAction����
	 * @param status
	 *            Ҫ���Ե�booleanֵ
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyFalse(PageAction pageAction, boolean status,
			String title)
	{
		try
		{
			Assert.assertFalse(status);
		}
		catch (AssertionError e)
		{
			try
			{
				ScreenCapture.screenShot(pageAction.getBrowser(), title);
			}
			catch (IOException e1)
			{
				log.error("WebDriver��ͼʧ�ܣ�");
			}
			throw new AssertionError(e);
		}
	}

	/**
	 * ���Բ���ֵ������ʧ�ܺ�JAVA����
	 * 
	 * @param status
	 *            Ҫ���Ե�booleanֵ
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyFalse(boolean status, String title)
	{
		try
		{
			Assert.assertFalse(status);
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
	 * @param expect
	 *            �����ַ���
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyEquals(String actual, String expect, String title)
	{
		try
		{
			Assert.assertEquals(actual, expect);
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
	 *            ʵ��ֵ
	 * @param expect
	 *            ����ֵ
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyEquals(int actual, int expect, String title)
	{
		try
		{
			Assert.assertEquals(actual, expect);
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
	 * @param expect
	 *            �����ַ���
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyUnEquals(String actual, String expect,
			String title)
	{
		if ((expect == null) || (actual == null))
		{
			return;
		}

		else if (actual.equals(expect))
		{
			String message = actual + " is equal with " + expect;
			ScreenCapture.screenShot(title);
			throw new AssertionError(message);
		}
		else
		{
			return;
		}
	}
	
	/**
	 * �����ַ����ǰ���������ʧ�ܺ�JAVA����
	 * 
	 * @param actual
	 *            ʵ���ַ���
	 * @param expect
	 *            �����ַ���
	 * @param title
	 *            ��ͼ�ļ�����Ϣ
	 */
	public static void verfiyContains(String actual, String expect,
			String title)
	{
		if ((expect == null) || (actual == null))
		{
			return;
		}

		else if (!actual.contains(expect))
		{
			String message = actual + " not contains " + expect;
			ScreenCapture.screenShot(title);
			throw new AssertionError(message);
		}
		else
		{
			return;
		}
	}
}
