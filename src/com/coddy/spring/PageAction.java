package com.coddy.spring;

import java.util.List;

import org.apache.commons.logging.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;

import com.coddy.utils.LogUtils;
import com.coddy.utils.Messages;

/**
 * PageAction 页面操作封装类
 * 
 * @author shenbing
 * 
 */
public class PageAction extends MouceAction {
	/**
	 * 日志句柄
	 */
	protected Log log = LogUtils.getLog(PageAction.class);

	/**
	 * 该构造方法是用以默认选择IE为启动
	 */
	public PageAction() {
		super();
	}

	/**
	 * 设置浏览器类型 {BrowserType {IE, FIREFOX, CHROME,HTTPUNIT }} Using eg:
	 * BrowserType.IE,BrowserType.FIREFOX
	 * 
	 * @param bt
	 *            浏览器类型枚举值
	 */
	public PageAction(BrowserType bt) {
		super(bt);
	}

	/**
	 * 设置焦点在元素上
	 * 
	 * @param xpath
	 *            可定位元素对象的xpath值
	 */
	public void focusOnWithXpath(String xpath) {
		try {
			WebElement element = this.getElementByXpath(xpath, "设置焦点");
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByXpath(xpath, "设置焦点");
				if (element == null) {
					this.log.error("对" + xpath + "的元素设置焦点失败");
					screenShot("对" + xpath + "的元素设置焦点失败");
					return;
				}
			}
			this.highLight(element);
			this.executeJavaScript("arguments[0].focus()", element);
		} catch (StaleElementReferenceException sere) {
			String error = "通过" + xpath + "获取的元素" + "设置焦点";
			this.screenShot(error);
			this.log.error(error);
			return;
		}
	}

	/**
	 * 设置焦点在元素上
	 * 
	 * @param key
	 *            文件中可定位元素对象的key值
	 */
	public void focusOn(String key) {
		try {
			WebElement element = this.getElement(key);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElement(key);
				if (element == null) {
					this.log.error("元素" + key + "设置焦点失败");
					screenShot("元素" + key + "设置焦点失败");
					return;
				}
			}
			this.highLight(element);
			this.executeJavaScript("arguments[0].focus()", element);
		} catch (StaleElementReferenceException sere) {
			this.log.error("元素" + key + "设置焦点失败");
			screenShot("元素" + key + "设置焦点失败");
			return;
		}
	}

	/**
	 * 通过javaScript获取输入框或者文本框，然后向输入框输入给定的内容
	 * 
	 * @param jscode
	 *            定位输入框或者文本框的javascript脚本
	 * @param value
	 *            需要输入的数据
	 */
	public void inputByJavaScript(String jscode, String value) {
		WebElement jelement = this.getElementByJavaScript(jscode);
		if (jelement == null) {
			sleep(TIME_OUT);
			jelement = this.getElementByJavaScript(jscode);
			if (jelement == null) {
				screenShot("获取元素失败，无法输入");
				this.log.error("获取元素失败，无法输入");
				return;
			}
		}
		this.highLight(jelement);
		jelement.sendKeys(value);
	}

	/**
	 * 在等到指定对象可见之后在该对象上做清理操作，一般用于输入框和选择框。
	 * 
	 * @param key
	 *            在yaml文件中的元素
	 */
	public void clear(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				String message = "清除输入内容或者取消选择失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		try {
			waitUntilElementVisible(element);
			writenableElement(element);
			element.clear();
		} catch (ElementNotVisibleException e) {
			String message = this.getElementDes(key) + "在当前页面上不可见";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = this.getElementDes(key) + "clear 操作失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * 在等到指定对象可见之后在该对象上做清理操作，一般用于输入框和选择框。
	 * 
	 * @param element
	 *            清楚值的页面元素
	 */
	public void clear(WebElement element) {
		if (element == null) {
			String message = "清除输入内容或者取消选择失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		try {
			waitUntilElementVisible(element);
			writenableElement(element);
			element.clear();
		} catch (ElementNotVisibleException e) {
			String message = element.getLocation() + "在当前页面上不可见";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = element.getLocation() + "clear 操作失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * 在等到指定对象可见之后在该对象上做清理操作，一般用于输入框和选择框。
	 * 
	 * @param key
	 *            在yaml文件中的元素
	 */
	public void clear(String key, String replacekey, String replaceValue) {
		WebElement element = this.getElement(key, replacekey, replaceValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key, replacekey, replaceValue);
			if (element == null) {
				String message = "清除输入内容或者取消选择失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		try {
			waitUntilElementVisible(element);
			writenableElement(element);
			element.clear();
		} catch (ElementNotVisibleException e) {
			String message = this.getElementDes(key) + "在当前页面上不可见";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = this.getElementDes(key) + "clear 操作失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * 在等到指定对象可见之后在该对象上做清理操作，一般用于输入框和选择框。
	 * 
	 * @param key
	 *            在yaml文件中的元素
	 */
	public void clears(String key) {
		List<WebElement> elements = this.getElements(key);
		if (elements == null) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null) {
				String message = "没有需要清除的输入元素";
				screenShot(message);
				this.log.info(message);
			}
		}
		for (WebElement element : elements) {
			try {
				waitUntilElementVisible(element);
				writenableElement(element);
				element.clear();
			} catch (ElementNotVisibleException e) {
				String message = this.getElementDes(key) + "在当前页面上不可见";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			} catch (Exception e) {
				String message = this.getElementDes(key) + "clear 操作失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
	}

	/**
	 * 在等到指定对象可见之后在该对象上做清理操作，一般用于输入框和选择框。
	 * 
	 * @param elements
	 *            元素集合
	 */
	public void clears(List<WebElement> elements) {
		if (elements == null || elements.size() == 0) {
			String message = "传入元素对象集合为空或null";
			screenShot(message);
			this.log.info(message);
		}
		for (WebElement element : elements) {
			try {
				waitUntilElementVisible(element);
				writenableElement(element);
				element.clear();
			} catch (ElementNotVisibleException e) {
				String message = "元素" + element.getLocation() + "在当前页面上不可见";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			} catch (Exception e) {
				String message = "元素" + element.getLocation() + "clear 操作失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
	}

	/**
	 * 在等到指定对象可见之后在该对象上做清理操作，一般用于输入框和选择框。
	 * 
	 * @param xpath
	 *            需要定位元素的xpath
	 */
	public void clearByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "清空或者取消选择");
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElementByXpath(xpath, "清空或者取消选择");
			if (element == null) {
				String message = "对通过xpath[" + xpath + "]获取的元素清除输入内容或者取消选择有误";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		try {
			waitUntilElementVisible(element);
		} catch (ElementNotVisibleException e) {
			String message = "通过" + xpath + "定位的元素在当前页面上不可见";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
		element.clear();
	}

	/**
	 * 删除富文本内容
	 * 
	 * @param key
	 *            需要定位富文本元素的key值
	 */
	public void deleteRichText(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				String message = "清除输入内容或者取消选择失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		try {
			waitUntilElementVisible(element);
			writenableElement(element);
			this.executeJavaScript("arguments[0].innerHTML=''", element);
		} catch (ElementNotVisibleException e) {
			String message = this.getElementDes(key) + "在当前页面上不可见";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = this.getElementDes(key) + "clear 操作失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * 向定位到key的输入框中直接输入数据
	 * 
	 * @param key
	 *            在yaml文件中的元素
	 * @param value
	 *            输入的数据
	 */
	public void input(String key, String value) {
		WebElement element = getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "获取元素失败，无法输入";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		this.highLight(element);
		String arg = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(arg, element);
		element.sendKeys(value);
		this.log.info("[" + this.getElementDes(key) + "]" + Messages.getString("input") + "\"" + value + "\"");
	}

	/**
	 * 向定位到key的输入框中直接输入数据
	 * 
	 * @param element
	 *            输入值的页面元素
	 * @param value
	 *            输入的数据
	 */
	public void input(WebElement element, String value) {
		if (element == null) {
			String message = "获取元素失败，无法输入";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		this.highLight(element);
		String arg = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(arg, element);
		element.sendKeys(value);
		this.log.info("[" + element.getLocation() + "]" + Messages.getString("input") + "\"" + value + "\"");
	}

	/**
	 * 向定位到key的输入框中直接输入数据
	 * 
	 * @param key
	 *            在yaml文件中的元素
	 * @param value
	 *            输入的数据
	 */
	public void inputs(String key, String value) {
		List<WebElement> webElements = getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot("元素获取失败，无法获取数据");
				this.log.error("元素获取失败，无法获取数据");
				throwException("元素获取失败，无法获取数据");
			}
		}
		for (WebElement element : webElements) {
			this.highLight(element);
			String arg = "arguments[0].readOnly = \"\"";
			this.executeJavaScript(arg, element);
			element.sendKeys(value);
			this.log.info("[" + this.getElementDes(key) + "]" + Messages.getString("input") + "\"" + value + "\"");
		}
	}

	/**
	 * 向定位到key的输入框中直接输入数据
	 * 
	 * @param key
	 *            在yaml文件中的元素
	 * @param value
	 *            输入的数据
	 */
	public void inputAsNo(String key, String value, int No) {
		List<WebElement> webElements = getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot("元素获取失败，无法获取数据");
				this.log.error("元素获取失败，无法获取数据");
				throwException("元素获取失败，无法获取数据");
			}
		}
		if (No >= webElements.size()) {
			screenShot("获取元素失败，无法输入");
			this.log.error(No + "数大于已发现元素数量（从0开始）");
			throwException(No + "数大于已发现元素数量（从0开始）");
		}
		WebElement element = webElements.get(No);
		if (element == null) {
			String message = "获取元素失败，无法输入";
			this.log.error(message);
			throwException(message);
			return;
		}
		this.highLight(element);
		String arg = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(arg, element);
		element.sendKeys(value);
		this.log.info("[" + this.getElementDes(key) + "]" + Messages.getString("input") + "\"" + value + "\"");
	}

	/**
	 * 向定位到key的输入框中直接输入数据
	 * 
	 * @param key
	 *            在yaml文件中的元素
	 * @param replaceKey
	 *            在yaml文件中key值中需要被替换的值
	 * @param valueKey
	 *            在yaml文件中key值中需要替换的值
	 * @param value
	 *            输入的数据
	 */
	public void input(String key, String replaceKey, String valueKey, String value) {
		WebElement element = getElement(key, replaceKey, valueKey);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key, replaceKey, valueKey);
			if (element == null) {
				String message = "获取元素失败，无法输入";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		this.highLight(element);
		element.sendKeys(value);
		this.log.info("[" + this.getElementDes(key) + "]" + Messages.getString("input") + "\"" + value + "\"");
	}

	/**
	 * 模拟键盘键入相关数据
	 * 
	 * keysToSend,eg:xx.sendKeys(Keys.ENTER)
	 * 
	 * @param keysToSend
	 */
	public void sendKeys(CharSequence... keysToSend) {
		try {
			this.getActions().sendKeys(keysToSend).perform();
			R: for (CharSequence cs : keysToSend) {
				for (Keys k : Keys.values()) {
					if (cs.equals(k)) {
						this.log.info("模拟键盘键入" + k.name());
						break R;
					}
				}
			}
		} catch (Exception e) {
			throwException("键盘操作失败");
		}
	}

	/**
	 * 选择Radio
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void selectRadio(String key) {
		WebElement webElement = this.getElement(key);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key);
			if (webElement == null) {
				screenShot("获取元素失败，无法操作");
				this.log.error("获取元素失败，无法操作");
				throwException("Radio操作失败");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("操作失败");
			throwException("操作失败");
		}
		if (!webElement.isSelected()) {
			webElement.click();
		}
	}

	/**
	 * 选择Radio
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void selectRadio(WebElement webElement) {
		if (webElement == null) {
			screenShot("获取元素失败，无法操作");
			this.log.error("获取元素失败，无法操作");
			throwException("Radio操作失败");
			return;
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("操作失败");
			throwException("操作失败");
		}
		if (!webElement.isSelected()) {
			webElement.click();
		}
	}

	/**
	 * 选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void selectCheckBox(String key) {
		WebElement webElement = this.getElement(key);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key);
			if (webElement == null) {
				screenShot(key + "元素未发现");
				this.log.error(key + ":元素未发现");
				throwException(getElementDes(key) + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot(key + "操作失败");
			throwException("操作失败");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * 选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void selectCheckBox(WebElement webElement) {
		if (webElement == null) {
			screenShot("传入元素为null");
			this.log.error("传入元素为null");
			throwException("传入元素为null");
			return;
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("无法操作");
			throwException("操作失败");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * 去选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void unselectCheckBox(String key) {
		WebElement webElement = this.getElement(key);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key);
			if (webElement == null) {
				screenShot(key + "操作失败");
				this.log.error(key + ":操作失败");
				throwException(getElementDes(key) + ":操作失败");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot(key + "操作失败");
			throwException("操作失败");
		}
		if (webElement.isEnabled()) {
			if (webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * 去选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void unselectCheckBox(WebElement webElement) {
		if (webElement == null) {
			screenShot("传入元素对象为空");
			this.log.error("传入元素对象为空");
			throwException("传入元素对象为空");
			return;
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("操作失败");
			throwException("操作失败");
		}
		if (webElement.isEnabled()) {
			if (webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * 去选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void unselectCheckBox(String key, String replaceKey, String replaceValue) {
		WebElement webElement = this.getElement(key, replaceKey, replaceValue);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key, replaceKey, replaceValue);
			if (webElement == null) {
				screenShot("获取元素失败，无法操作");
				this.log.error("获取元素失败，无法操作");
				throwException("CheckBox操作失败");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("操作失败");
			throwException("操作失败");
		}
		if (webElement.isEnabled()) {
			if (webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * 选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void selectCheckBoxByAttr(String key, String attrname, String attrvalue) {
		WebElement webElement = this.getElementByAttr(key, attrname, attrvalue);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElementByAttr(key, attrname, attrvalue);
			if (webElement == null) {
				screenShot("获取元素失败，无法操作");
				this.log.error("获取元素失败，无法操作");
				throwException("CheckBox操作失败");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("操作失败");
			throwException("操作失败");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * 选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void selectCheckBox(String key, String replaceKey, String replaceValue) {
		WebElement webElement = this.getElement(key, replaceKey, replaceValue);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key, replaceKey, replaceValue);
			if (webElement == null) {
				screenShot("获取元素失败，无法操作");
				this.log.error("获取元素失败，无法操作");
				throwException("CheckBox操作失败");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("操作失败");
			throwException("操作失败");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * 去选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void batchUnselectCheckBox(String key) {
		List<WebElement> webElements = this.getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = this.getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot(key + "元素集合未发现");
				this.log.info(key + ":元素集合未发现");
			}
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(key + "元素集合未发现");
				this.log.error(key + "元素不可见");
			}
			if (webElement.isEnabled()) {
				if (webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * 去选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void batchUnselectCheckBox(List<WebElement> webElements) {
		if (webElements == null || webElements.size() == 0) {
			screenShot("传入元素对象集合为空或null");
			this.log.info("传入元素对象集合为空或null");
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(webElement.getLocation() + "元素不可见");
				this.log.error(webElement.getLocation() + "元素不可见");
			}
			if (webElement.isEnabled()) {
				if (webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * 批量选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void batchSelectCheckBox(String key) {
		List<WebElement> webElements = this.getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = this.getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot(key + "元素未发现");
				this.log.info(key + ":元素未发现");
			}
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(key + "元素不可见");
				this.log.error(key + "元素不可见");
			}
			if (webElement.isEnabled()) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * 批量选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void batchSelectCheckBox(List<WebElement> webElements) {
		if (webElements == null || webElements.size() == 0) {
			screenShot("传入对象集合为空或null");
			this.log.info("传入对象集合为空或null");
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(webElement.getLocation() + "元素不可见");
				this.log.error(webElement.getLocation() + "元素不可见");
			}
			if (webElement.isEnabled()) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * 批量选择CheckBox
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 */
	public void selectCheckBox(String... key) {
		int num = key.length;
		for (int i = 0; i < num; i++) {
			WebElement webElement = this.getElement(key[i]);
			if (webElement == null) {
				sleep(TIME_OUT);
				webElement = this.getElement(key[i]);
				if (webElement == null) {
					screenShot("获取第" + key[i] + "个元素失败");
					this.log.error("获取元素失败，无法操作");
					throwException("CheckBox操作失败");
					return;
				}
			}
			try {
				this.waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot("操作失败");
				throwException("操作失败");
			}
			if (webElement.isEnabled()) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * 设置字符串
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 * @param value
	 *            需要设置的字符串
	 */
	public void setValue(String key, String value) {
		this.writenableElement(key);
		this.input(key, value);
	}

	/**
	 * 设置元素text文本
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 * @param value
	 *            需要设置的字符串
	 */
	public void setText(String key, String value) {
		WebElement element = getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				screenShot(key + "元素未发现");
				log.error(key + ":元素未发现");
				throwException(key + ":元素未发现");
				return;
			}
		}
		String arg = "arguments[0].innerHTML = \"" + value + "\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * 设置元素text文本
	 * 
	 * @param element
	 *            元素对象
	 * @param value
	 *            需要设置的字符串
	 */
	public void setText(WebElement element, String value) {
		if (element == null) {
			screenShot("传入元素为空");
			throwException("传入元素为空");
			return;
		}
		String arg = "arguments[0].innerHTML = \"" + value + "\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * 设置日期
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 * @param value
	 *            需要设置的日期，格式为2014-04-10
	 */
	public void setDate(String key, String value) {
		this.writenableElement(key);
		this.setElementValue(key, value);
		this.sleep(0.5);
		this.sendKeys(Keys.ENTER);
	}

	/**
	 * 设置日期
	 * 
	 * @param webElement
	 *            需要设置值的对象
	 * @param value
	 *            需要设置的日期，格式为2014-04-10
	 */
	public void setDate(WebElement element, String value) {
		if (element == null) {
			screenShot("传入元素为空");
			throwException("传入元素为空");
			return;
		}
		this.setElementValue(element, value);
		this.sleep(0.5);
		this.sendKeys(Keys.ENTER);
	}

	public void setElementValue(String key, String value) {
		WebElement element = getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = key + "获取元素失败，无法输入";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		this.highLight(element);
		waitUntilElementVisible(element);
		String readOnly = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(readOnly, element);
		String setValue = "arguments[0].value = \"" + value + "\"";
		this.executeJavaScript(setValue, element);
		this.log.info("[" + this.getElementDes(key) + "]" + Messages.getString("input") + "\"" + value + "\"");
	}

	public void setElementValue(WebElement webElement, String value) {
		if (webElement == null) {
			screenShot("传入元素对象为空");
			this.log.error("传入元素对象为空");
			throwException("传入元素对象为空");
			return;
		}
		this.highLight(webElement);
		waitUntilElementVisible(webElement);
		String readOnly = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(readOnly, webElement);
		String setValue = "arguments[0].value = \"" + value + "\"";
		this.executeJavaScript(setValue, webElement);
		this.log.info("[" + webElement.getLocation() + "]" + Messages.getString("input") + "\"" + value + "\"");
	}

	/**
	 * 按照指定序号选择yaml文件中的key元素下拉列表中的选项。
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 * @param index
	 *            需要选择的选项的索引值
	 */
	public void selectItemByIndex(String key, int index) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "元素未发现");
				this.log.error(key + ":元素未发现");
				throwException(key + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			screenShot("操作失败");
			throwException("操作失败");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByIndex(index);
			this.log.info("选择下拉列表第" + index + "个选项");
		} catch (UnexpectedTagNameException utne) {
			screenShot("选择下拉列表第" + index + "个选项失败");
			this.log.error("通过" + key + "获取的元素" + this.getElementDes(key) + ",不是HTML表单的下拉列表");
			throwException("下拉列表操作失败");
		} catch (NoSuchElementException nee) {
			screenShot("选择下拉列表第" + index + "个选项失败");
			this.log.error("控件" + this.getElementDes(key) + ",不能通过索引" + index + "获取Options的值");
			throwException("下拉列表操作失败");
		}
	}

	/**
	 * 按照指定序号选择给定xpath的下拉列表中的选项。
	 * 
	 * @param xpath
	 *            文件中定位元素对象的key值
	 * @param index
	 *            需要索引选型值的索引
	 */
	public void selectXpathItemByIndex(String xpath, int index) {
		WebElement element = this.getElementByXpath(xpath, "定位列表框");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "定位列表框");
			if (element == null) {
				screenShot(xpath + "元素未发现");
				this.log.error(xpath + ":元素未发现");
				throwException(xpath + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "当前元素不可见";
			this.screenShot(error);
			log.error("当前元素不可见");
			throwException("下拉列表操作失败");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByIndex(index);
			this.log.info("选择下拉列表第" + index + "个选项");
		} catch (UnexpectedTagNameException utne) {
			String error = "当前元素不是HTML表单的下拉列表";
			this.screenShot(error);
			this.log.error(error);
			throwException("下拉列表操作失败");
		} catch (NoSuchElementException nee) {
			String error = "给定控件不能通过索引" + index + "获取Options的值";
			this.screenShot(error);
			this.log.error(error);
			throwException("下拉列表操作失败");
		}
	}

	/**
	 * 获取yaml文件中key的下拉列表框的文本内容为text的选项
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 * @param text
	 *            需要选择的文本内容
	 */
	public void selectItemByText(String key, String text) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "元素未发现");
				this.log.error(key + ":元素未发现");
				throwException(key + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			screenShot("下拉列表操作失败,元素不能访问");
			throwException("下拉列表操作失败,元素不能访问");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByVisibleText(text);
			this.log.info("选择下拉列表[" + text + "]");
		} catch (UnexpectedTagNameException utne) {
			screenShot("选择下拉列表[" + text + "]失败");
			this.log.error("通过" + key + "获取的元素" + this.getElementDes(key) + ",不是HTML表单的下落列表");
			throwException("下拉列表操作失败");
		} catch (NoSuchElementException nee) {
			screenShot(this.getElementDes(key) + ",不能通过" + text + "获取Options的值");
			this.log.error("控件" + this.getElementDes(key) + ",不能通过" + text + "获取Options的值");
			throwException("下拉列表操作失败");
		}
	}

	/**
	 * 获取yaml文件中key的下拉列表框的文本内容为text的选项
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 * @param text
	 *            需要选择的文本内容
	 */
	public void selectItemByText(String key, String replaceKey, String replaceValue, String text) {
		WebElement element = this.getElement(key, replaceKey, replaceValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "元素未发现");
				this.log.error(key + ":元素未发现");
				throwException(key + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			screenShot("下拉列表操作失败");
			throwException("下拉列表操作失败");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByVisibleText(text);
			this.log.info("选择下拉列表[" + text + "]");
		} catch (UnexpectedTagNameException utne) {
			screenShot("选择下拉列表[" + text + "]失败");
			this.log.error("通过" + key + "获取的元素" + this.getElementDes(key) + ",不是HTML表单的下落列表");
			throwException("下拉列表操作失败");
		} catch (NoSuchElementException nee) {
			screenShot("选择下拉列表[" + text + "]失败");
			this.log.error("控件" + this.getElementDes(key) + ",不能通过" + text + "获取Options的值");
			throwException("下拉列表操作失败");
		}
	}

	/**
	 * 通过Xpath选取下拉列表框。选择其文本为text的选项
	 * 
	 * @param xpath
	 *            下拉列表的xpath
	 * @param text
	 *            文本选项
	 */
	public void selectItemTextByXpath(String xpath, String text) {
		WebElement element = this.getElementByXpath(xpath, "定位列表框");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "定位列表框");
			if (element == null) {
				screenShot(xpath + "元素未发现");
				this.log.error(xpath + ":元素未发现");
				throwException(xpath + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "当前元素不可见";
			this.screenShot(error);
			log.error("当前元素不可见");
			throwException("下拉列表操作失败");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByVisibleText(text);
			this.log.info("选择下拉列表[" + text + "]");
		} catch (UnexpectedTagNameException utne) {
			String error = "当前元素不是HTML表单的下拉列表";
			this.screenShot(error);
			this.log.error(error);
			throwException("下拉列表操作失败");
		} catch (NoSuchElementException nee) {
			String error = "给定控件不能通过文本内容" + text + "获取选项值";
			this.screenShot(error);
			this.log.error(error);
			throwException("下拉列表操作失败");
		}
	}

	/**
	 * 按照指定选项的实际值（不是可见文本值，而是对象的“value”属性的值）选择下拉列表中的选项。
	 * 
	 * @param key
	 *            文件中定位元素对象的key值
	 * 
	 * @param itemValue
	 *            下拉列表子项的value值
	 */
	public void selectItemByValue(String key, String itemValue) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				screenShot(key + "元素未发现");
				this.log.error(key + ":元素未发现");
				throwException(key + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "当前元素不可见";
			this.screenShot(error);
			log.error("当前元素不可见");
			throwException("下拉列表操作失败");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByValue(itemValue);
			this.log.info("选择下拉列表中value值为[" + itemValue + "]的选项");
		} catch (UnexpectedTagNameException utne) {
			String error = "当前元素不是HTML表单的下拉列表";
			this.screenShot(error);
			this.log.error("通过" + key + "获取的元素" + this.getElementDes(key) + ",不是HTML表单的下落列表");
			throwException("下拉列表操作失败");
		} catch (NoSuchElementException nee) {
			String error = "给定控件不能通过" + itemValue + "获取Options的值";
			this.screenShot(error);
			this.log.error("控件" + this.getElementDes(key) + ",不能通过选项值" + itemValue + "选择该控件");
			throwException("下拉列表操作失败");
		}
	}

	/**
	 * 按照给定的xpath定位到下拉列表，并通过指定选项的实际值（不是可见文本值，而是对象的“value”属性的值）选择下拉列表中的选项。
	 * 
	 * @param xpath
	 *            自定义定位元素的xpath
	 * @param itemValue
	 *            下拉列表子项的value值
	 */
	public void selectItemValueByXpath(String xpath, String itemValue) {
		WebElement element = this.getElementByXpath(xpath, "下拉列表控件");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "下拉列表控件");
			if (element == null) {
				screenShot(xpath + "元素未发现");
				this.log.error(xpath + ":元素未发现");
				throwException(xpath + ":元素未发现");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "当前元素不可见";
			this.screenShot(error);
			log.error("当前元素不可见");
			throwException("下拉列表操作失败");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByValue(itemValue);
			this.log.info("选择下拉列表中value值为[" + itemValue + "]的选项");
		} catch (UnexpectedTagNameException utne) {
			String error = "当前元素不是HTML表单的下拉列表";
			this.screenShot(error);
			this.log.error("通过" + xpath + "定位的元素不是HTML表单的下落列表");
			throwException("下拉列表操作失败");
		} catch (NoSuchElementException nee) {
			String error = "给定控件不能通过" + itemValue + "获取Options的值";
			this.screenShot(error);
			this.log.error("下拉列表控件,不能通过选型值" + itemValue + "选择该控件");
			throwException("下拉列表操作失败");
		}
	}

	/**
	 * 获取table指定Cell值
	 * 
	 * @param key
	 *            定位table元素的key值
	 * @param tableCellAddress
	 *            table中Cell的对应值，eg:1.2
	 * @return 指定Cell的值
	 */
	public String getCellTextByID(String key, String tableCellAddress) {
		WebElement tableElement = this.getElement(key);
		if (tableElement == null) {
			sleep(TIME_OUT);
			tableElement = this.getElement(key);
			if (tableElement == null) {
				screenShot(key + "元素未发现");
				log.error(key + ":元素未发现");
				throwException(key + ":元素未发现");
			}
		}
		// 对所要查找的单元格位置字符串进行分解，得到其对应行、列。
		int index = tableCellAddress.trim().indexOf('.');
		int row = Integer.parseInt(tableCellAddress.substring(0, index));
		int cell = Integer.parseInt(tableCellAddress.substring(index + 1));
		// 得到table表中所有行对象，并得到所要查询的行对象。
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
		if (rows == null || rows.size() == 0) {
			sleep(TIME_OUT);
			rows = tableElement.findElements(By.tagName("tr"));
			if (rows == null || rows.size() == 0) {
				screenShot("第" + rows + "行元素未发现");
				log.error("第" + rows + "行元素未发现");
				throwException("第" + rows + "行元素未发现");
			}
		}
		if (row >= rows.size()) {
			screenShot(rows + "超过元素集合数量");
			log.error(rows + "超过元素集合数量");
			throwException(rows + "超过元素集合数量");
		}
		WebElement theRow = rows.get(row);
		// 调用getCell方法得到对应的列对象，然后得到要查询的文本。
		String text = getCell(theRow, cell).getText();
		return text;
	}

	/**
	 * 获取table中的指定Cell
	 * 
	 * @param Row
	 *            table中行元素对象
	 * @param cell
	 *            table中cell的值
	 * @return Cell元素对象
	 */
	private WebElement getCell(WebElement row, int cell) {
		if (row == null) {
			screenShot("传入元素为空或null");
			log.error("传入元素为空或null");
		}
		List<WebElement> cells;
		WebElement target = null;
		// 列里面有"<th>"、"<td>"两种标签，所以分开处理。
		if (row.findElements(By.tagName("th")).size() > 0) {
			cells = row.findElements(By.tagName("th"));
			target = cells.get(cell);
		}
		if (row.findElements(By.tagName("td")).size() > 0) {
			cells = row.findElements(By.tagName("td"));
			target = cells.get(cell);
		}
		return target;
	}

}
