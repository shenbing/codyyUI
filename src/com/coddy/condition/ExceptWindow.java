package com.coddy.condition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * ���ô����л�
 * 
 * @author shenbing
 * 
 */
public class ExceptWindow implements ExpectedCondition<WebDriver>
{
	/**
	 * �ɶ�λ���ڵ�ֵ
	 */
	private String id;

	/**
	 * ���췽��
	 * 
	 * @param id
	 *            �ɶ�λ���ڵ�ֵ
	 */
	public ExceptWindow(String id)
	{
		this.id = id;
	}

	/**
	 * ʵ��ExpectedCondition�ӿڵ�apply����
	 * 
	 * @return �л���ָ�����ڵ�WebDriver����
	 */
	public WebDriver apply(WebDriver d)
	{
		WebDriver newWindow = null;
		newWindow = d.switchTo().window(id);
		return newWindow;
	}
}