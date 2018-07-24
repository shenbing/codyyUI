package com.coddy.condition;

import java.util.ArrayList;
import java.util.List;
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
public class ExceptElements implements ExpectedCondition<List<WebElement>>
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
	public ExceptElements(By by)
	{
		this.by = by;
	}

	/**
	 * 实现ExpectedCondition接口的apply方法
	 * 
	 * @return By对象定位的WebElement对象
	 */
	public List<WebElement> apply(WebDriver d)
	{
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = d.findElements(by);
		return elements;
	}
}