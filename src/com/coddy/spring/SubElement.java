package com.coddy.spring;

import org.apache.commons.logging.Log;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.coddy.condition.ExceptFrame;
import com.coddy.condition.ExceptParentFrame;
import com.coddy.utils.LogUtils;
import com.coddy.utils.Messages;

/**
 * SubElement 页面子元素类，封装实现Frame、Alert相关操作功能
 * 
 * @author shenbing
 * 
 */
public class SubElement extends Element {
	/**
	 * 日志句柄
	 */
	protected Log log = LogUtils.getLog(SubElement.class);

	/**
	 * 默认构造方法
	 */
	public SubElement() {
		super();
	}

	/**
	 * 构造方法
	 * 
	 * @param bt
	 *            BrowserType枚举值
	 */
	public SubElement(BrowserType bt) {
		super(bt);
	}

	/**
	 * 将当前执行的driver，选择到包含指定元素的frame上
	 * 
	 * @param key
	 *            在yaml文件中存储的frame
	 * @return 驱动浏览器对象
	 */
	public WebDriver selectFrame(String key) {
		Object ov = null;
		WebDriver innerFrame = null;
		try {
			for (ElementData trace : this.traceList) {
				ov = trace.getFrame(key);
				if (ov != null) {
					break;
				}
			}

		} catch (NumberFormatException e) {
			String message = "yaml文件中 Fram " + key + "提供参数有误";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (ov == null) {
			String message = Messages.getString("AutoTest.swFail");
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
		if (ov instanceof String) {
			try {
				ExceptFrame ef = new ExceptFrame(ov);
				innerFrame = wait.until(ef);
				this.log.info("切换至Frame[" + ov + "]成功");
			} catch (TimeoutException nfe) {
				String title = "获取Frame " + key + "失败";
				this.screenShot(title);
				throwException(title);
			}
		} else {
			innerFrame = selectFrame(ov);
		}
		return innerFrame;
	}

	/**
	 * 将当前执行的driver，选择到包含指定元素的frame上
	 * 
	 * @param driver
	 *            浏览器驱动对象
	 * @param element
	 *            frame元素对象
	 * @return 浏览器驱动对象
	 */
	protected WebDriver selectFrame(WebDriver driver, WebElement element) {
		WebDriver innerFrame = null;
		if (driver == null || element == null) {
			this.log.error("获取元素失败，无法操作");
			throwException("切换frame或者Iframe失败");
			return null;
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
			ExceptFrame ef = new ExceptFrame(element);
			innerFrame = wait.until(ef);
			this.log.info("切换至Frame[" + element.getLocation() + "]成功");
		} catch (NoSuchFrameException e) {
			String message = "需要切换的frame或者Iframe不存在";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} catch (StaleElementReferenceException e) {
			String message = "目标frame中不能依赖元素定位";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return innerFrame;
	}

	/**
	 * 将当前执行的driver，选择到包含指定元素的frame上
	 * 
	 * @param element
	 *            frame元素对象
	 * @return 驱动浏览器对象
	 */
	public WebDriver selectFrameByElement(WebElement element) {
		return this.selectFrame(this.browser, element);
	}

	/**
	 * 将当前执行的driver，选择到包含指定元素的frame上
	 * 
	 * @param elementKey
	 *            定位frame元素的key值
	 */
	public void selectFrameByElement(String elementKey) {
		WebElement element = this.getElement(elementKey);
		this.selectFrame(this.browser, element);
	}

	/**
	 * 将当前执行的driver，选择到包含指定元素的frame上
	 * 
	 * @param par
	 *            可定位frame的值
	 * @return 浏览器驱动对象
	 */
	public WebDriver selectFrame(Object par) {
		WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
		// this.sleep(2);
		boolean isOk = true;
		WebDriver innerFrame = null;
		if (par instanceof Integer) {
			try {
				Integer it = (Integer) par;
				ExceptFrame ef = new ExceptFrame(par);
				innerFrame = wait.until(ef);
				it++;
				this.log.info("切换至第" + it + "个frame");
			} catch (TimeoutException nfe) {
				isOk = false;
			}
		} else {
			try {
				ExceptFrame ef = new ExceptFrame(par);
				innerFrame = wait.until(ef);
				this.log.info("切换至 frame '" + par + "'");
			} catch (TimeoutException nfe) {
				isOk = false;
			}
		}
		if (!isOk) {
			String error = "切换至 frame'" + par + "'失败";
			this.log.error(error);
			this.screenShot(error);
			throwException(error);
		}
		return innerFrame;
	}

	/**
	 * 选择默认Frame
	 * 
	 * @return 驱动浏览器对象
	 */
	@SuppressWarnings("unused")
	public WebDriver selectDefaultFrame() {
		boolean isOk = true;
		WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
		// this.sleep(2);
		WebDriver defaultFrameDriver = null;
		try {
			ExceptFrame ef = new ExceptFrame();
			defaultFrameDriver = wait.until(ef);
			this.log.info("切换至 默认frame成功");
		} catch (TimeoutException nfe) {
			isOk = false;
			this.log.info("切换至 默认frame超时失败");
		}
		return defaultFrameDriver;
	}

	/**
	 * 选择父Frame
	 * 
	 * @return 驱动浏览器对象
	 */
	@SuppressWarnings("unused")
	public WebDriver selectParentFrame() {
		boolean isOk = true;
		WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
		// this.sleep(2);
		WebDriver defaultFrameDriver = null;
		try {
			ExceptParentFrame ef = new ExceptParentFrame();
			defaultFrameDriver = wait.until(ef);
			this.log.info("切换至父frame成功");
		} catch (TimeoutException nfe) {
			isOk = false;
			this.log.info("切换至父frame超时失败");
		}
		return defaultFrameDriver;
	}

	/**
	 * 设置在一定时间后，切换到alert
	 * 
	 * @param seconds
	 *            切换超时时间
	 * @return true / false
	 */
	public boolean switchToAlert(int seconds) {
		long start = System.currentTimeMillis();
		while ((System.currentTimeMillis() - start) < seconds * 1000) {
			try {
				this.browser.switchTo().alert();
				this.log.info("切换到Alert成功！");
				return true;
			} catch (NoAlertPresentException ne) {
				throwException("当前没有可以切换的警告提示框 ");
			} catch (Exception e) {
				this.log.error("当前没有可以切换的警告提示框 ");
				throwException("当前没有可以切换的警告提示框");
			}
		}
		return false;
	}

	/**
	 * 点击alert弹窗的确定按钮
	 */
	public void acceptAlert() {
		try {
			Alert alert = this.browser.switchTo().alert();
			alert.accept();
			this.log.info("点击警告窗的确定按钮");
		} catch (NoAlertPresentException e) {
			String message = "警告窗有误或者不存在";
			this.screenShot(message);
			throwException(message);
		}
	}

	/**
	 * 点击alert窗口中的取消按钮
	 */
	public void cancelAlert() {
		try {
			Alert alert = this.browser.switchTo().alert();
			alert.dismiss();
			this.log.info("点击警告窗的取消按钮");
		} catch (NoAlertPresentException e) {
			String message = "警告窗有误或者不存在";
			this.screenShot(message);
			throwException(message);
		}
	}

	/**
	 * 获取alert弹框中的警告信息
	 * 
	 * @return alert弹出框中的信息
	 */
	public String getTextOfAlert() {
		String text = null;
		try {
			Alert alert = this.browser.switchTo().alert();
			text = alert.getText();
		} catch (NoAlertPresentException e) {
			String message = "警告窗有误或者不存在";
			this.screenShot(message);
			throwException(message);
		}
		return text;
	}

}
