package com.coddy.condition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 
 * 设置frame切换
 * 
 * @author shenbing
 * 
 */
public class ExceptParentFrame implements ExpectedCondition<WebDriver>
{

	/**
	 * 实现ExpectedCondition接口的apply方法
	 * 
	 * @return 切换到父Frame的WebDriver对象
	 */
	public WebDriver apply(WebDriver d)
	{
		WebDriver driver = null;
		driver = d.switchTo().parentFrame();
		return driver;
	}
}