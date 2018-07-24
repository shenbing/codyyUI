package com.coddy.condition;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * 
 * 设置frame切换
 * 
 * @author shenbing
 * 
 */
public class ExceptFrame implements ExpectedCondition<WebDriver> {
	/**
	 * 可定位Frame的属性值
	 */
	private Object par = null;

	/**
	 * 默认构造方法
	 */
	public ExceptFrame() {
		this.par = null;
	}

	/**
	 * 构造方法
	 * 
	 * @param par
	 *            可定位Frame的属性值
	 */
	public ExceptFrame(Object par) {
		this.par = par;
	}

	/**
	 * 实现ExpectedCondition接口的apply方法
	 * 
	 * @return 切换到指定Frame的WebDriver对象
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