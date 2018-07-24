package com.coddy.check;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.testng.Assert;
import com.coddy.spring.PageAction;
import com.coddy.utils.LogUtils;
import com.coddy.utils.ScreenCapture;

/**
 * 操作断言类
 * 
 * @author shenbing
 * 
 */
public class ActionCheck
{
	/**
	 * 日志句柄
	 */
	public static Log log = LogUtils.getLog(ActionCheck.class);

	/**
	 * 断言布尔值，断言失败后WebDriver截屏
	 * 
	 * @param pageAction
	 *            PageAction对象
	 * @param status
	 *            要断言的boolean值
	 * @param title
	 *            截图文件名信息
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
				log.error("WebDriver截图失败！");
			}
			throw new AssertionError(e);
		}
	}

	/**
	 * 断言布尔值，断言失败后JAVA截屏
	 * 
	 * @param status
	 *            要断言的boolean值
	 * @param title
	 *            截图文件名信息
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
	 * 断言布尔值，断言失败后WebDriver截屏
	 * 
	 * @param pageAction
	 *            PageAction对象
	 * @param status
	 *            要断言的boolean值
	 * @param title
	 *            截图文件名信息
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
				log.error("WebDriver截图失败！");
			}
			throw new AssertionError(e);
		}
	}

	/**
	 * 断言布尔值，断言失败后JAVA截屏
	 * 
	 * @param status
	 *            要断言的boolean值
	 * @param title
	 *            截图文件名信息
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
	 * 断言字符串是否相同，断言失败后JAVA截屏
	 * 
	 * @param actual
	 *            实际字符串
	 * @param expect
	 *            期望字符串
	 * @param title
	 *            截图文件名信息
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
	 * 断言字符串是否相同，断言失败后JAVA截屏
	 * 
	 * @param actual
	 *            实际值
	 * @param expect
	 *            期望值
	 * @param title
	 *            截图文件名信息
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
	 * 断言字符串是否不相同，断言失败后JAVA截屏
	 * 
	 * @param actual
	 *            实际字符串
	 * @param expect
	 *            期望字符串
	 * @param title
	 *            截图文件名信息
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
	 * 断言字符串是包含，断言失败后JAVA截屏
	 * 
	 * @param actual
	 *            实际字符串
	 * @param expect
	 *            期望字符串
	 * @param title
	 *            截图文件名信息
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
