package com.coddy.condition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 
 * ����frame�л�
 * 
 * @author shenbing
 * 
 */
public class ExceptFrame implements ExpectedCondition<WebDriver> {
	/**
	 * �ɶ�λFrame������ֵ
	 */
	private Object par = null;

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public ExceptFrame() {
		this.par = null;
	}

	/**
	 * ���췽��
	 * 
	 * @param par
	 *            �ɶ�λFrame������ֵ
	 */
	public ExceptFrame(Object par) {
		this.par = par;
	}

	/**
	 * ʵ��ExpectedCondition�ӿڵ�apply����
	 * 
	 * @return �л���ָ��Frame��WebDriver����
	 */
	public WebDriver apply(WebDriver d) {
		WebDriver driver = null;
		if (par instanceof String) {
			String frame = (String) par;
			driver = d.switchTo().frame(frame);
		} else if (par instanceof Integer) {
			driver = d.switchTo().frame((Integer) par);
		} else if (par instanceof WebElement) {
			driver = d.switchTo().frame((WebElement) par);
		} else if (par == null) {
			driver = d.switchTo().defaultContent();
		}
		return driver;
	}
}