package com.coddy.condition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 
 * ����frame�л�
 * 
 * @author shenbing
 * 
 */
public class ExceptParentFrame implements ExpectedCondition<WebDriver>
{

	/**
	 * ʵ��ExpectedCondition�ӿڵ�apply����
	 * 
	 * @return �л�����Frame��WebDriver����
	 */
	public WebDriver apply(WebDriver d)
	{
		WebDriver driver = null;
		driver = d.switchTo().parentFrame();
		return driver;
	}
}