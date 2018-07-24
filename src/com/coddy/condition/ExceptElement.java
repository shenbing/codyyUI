package com.coddy.condition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 设置元素查找，元素存在
 * 
 * @author shenbing
 * 
 */
public class ExceptElement implements ExpectedCondition<WebElement>
{
	/**
	 * By对象
	 */
	private By by;

	/**
	 * 构造方法
	 * 
	 * @param by
	 *            可定位WebElement的By实例
	 */
	public ExceptElement(By by)
	{
		this.by = by;
	}

	/**
	 * 实现ExpectedCondition接口的apply方法
	 * 
	 * @return By对象定位的WebElement对象
	 */
	public WebElement apply(WebDriver d)
	{
		WebElement element = null;
		element = d.findElement(by);
		return element;
	}
}