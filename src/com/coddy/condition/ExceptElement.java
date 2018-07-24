package com.coddy.condition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * ����Ԫ�ز��ң�Ԫ�ش���
 * 
 * @author shenbing
 * 
 */
public class ExceptElement implements ExpectedCondition<WebElement>
{
	/**
	 * By����
	 */
	private By by;

	/**
	 * ���췽��
	 * 
	 * @param by
	 *            �ɶ�λWebElement��Byʵ��
	 */
	public ExceptElement(By by)
	{
		this.by = by;
	}

	/**
	 * ʵ��ExpectedCondition�ӿڵ�apply����
	 * 
	 * @return By����λ��WebElement����
	 */
	public WebElement apply(WebDriver d)
	{
		WebElement element = null;
		element = d.findElement(by);
		return element;
	}
}