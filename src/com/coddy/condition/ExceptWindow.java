package com.coddy.condition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 设置窗口切换
 * 
 * @author shenbing
 * 
 */
public class ExceptWindow implements ExpectedCondition<WebDriver>
{
	/**
	 * 可定位窗口的值
	 */
	private String id;

	/**
	 * 构造方法
	 * 
	 * @param id
	 *            可定位窗口的值
	 */
	public ExceptWindow(String id)
	{
		this.id = id;
	}

	/**
	 * 实现ExpectedCondition接口的apply方法
	 * 
	 * @return 切换到指定窗口的WebDriver对象
	 */
	public WebDriver apply(WebDriver d)
	{
		WebDriver newWindow = null;
		newWindow = d.switchTo().window(id);
		return newWindow;
	}
}