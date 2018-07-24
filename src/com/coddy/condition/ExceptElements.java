package com.coddy.condition;

import java.util.ArrayList;
import java.util.List;
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
public class ExceptElements implements ExpectedCondition<List<WebElement>>
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
	public ExceptElements(By by)
	{
		this.by = by;
	}

	/**
	 * ʵ��ExpectedCondition�ӿڵ�apply����
	 * 
	 * @return By����λ��WebElement����
	 */
	public List<WebElement> apply(WebDriver d)
	{
		List<WebElement> elements = new ArrayList<WebElement>();
		elements = d.findElements(by);
		return elements;
	}
}