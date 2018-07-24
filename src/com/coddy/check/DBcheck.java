package com.coddy.check;

import org.apache.commons.logging.Log;
import org.testng.Assert;
import com.coddy.utils.LogUtils;
import com.coddy.utils.ScreenCapture;

/**
 * 数据库断言类
 * 
 * @author shenbing
 * 
 */
public class DBcheck
{
	/**
	 * 日志句柄
	 */
	public static Log log = LogUtils.getLog(DBcheck.class);

	/**
	 * 断言数量是否相同
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
	 * 断言字符串是否相同，断言失败后JAVA截屏
	 * 
	 * @param actual
	 *            实际字符串
	 * @param expected
	 *            期望字符串
	 * @param title
	 *            截图文件名信息
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
	 * 断言字符串是否不相同，断言失败后JAVA截屏
	 * 
	 * @param actual
	 *            实际字符串
	 * @param expected
	 *            期望字符串
	 * @param title
	 *            截图文件名信息
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
