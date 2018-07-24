package com.coddy.spring;

import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import com.coddy.utils.LogUtils;
import com.coddy.utils.Messages;

/**
 * MouceAction 鼠标操作封装类
 * 
 * @author shenbing
 * 
 */
public class MouceAction extends SubElement {
	/**
	 * 日志句柄
	 */
	protected Log log = LogUtils.getLog(MouceAction.class);

	/**
	 * 默认构造方法
	 */
	public MouceAction() {
		super();
	}

	/**
	 * 构造方法
	 * 
	 * @param bt
	 *            BrowserType枚举值
	 */
	public MouceAction(BrowserType bt) {
		super(bt);
	}

	/**
	 * 单击元素
	 * 
	 * @param element
	 *            被点击元素对象
	 */
	public void click(WebElement element) {
		if (element == null) {
			String message = "点击元素失败,元素为空";
			this.log.error(message);
			throwException(message);
			return;
		}
		Point location = element.getLocation();
		this.highLight(element);
		waitUntilElementVisible(element);
		element.click();
		this.log.info(Messages.getString("AutoTest.click") + "[" + location.toString() + "]");
	}

	/**
	 * 单击定位到Key的元素
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 */
	public void click(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "点击元素失败,元素为空";
				screenShot(key + "未能找到元素");
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		this.highLight(element);
		try {
			waitUntilElementVisible(element);
			element.click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot("点击元素" + key + "失败");
			this.log.error(error);
			throwException(error);
			return;
		} catch (WebDriverException e) {
			screenShot("点击元素" + key + "失败");
			this.log.error(e);
			sleep(TIME_OUT);
			waitUntilElementVisible(element);
			element.click();
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 单击定位到Key的所有元素
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 */
	public void clicks(String key) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "没有元素需要点击";
					screenShot(key + "没有元素需要点击");
					this.log.info(message);
				}
			}
			for (WebElement element : elements) {
				this.highLight(element);
				try {
					waitUntilElementVisible(element);
					element.click();
				} catch (TimeoutException e) {
					screenShot(element.getLocation() + "元素不可见");
					log.error(element.getLocation() + "元素不可见");
				}
			}
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 单击有所元素
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 */
	public void clicks(List<WebElement> elements) {
		try {
			if (elements == null || elements.size() == 0) {
				String message = "传入元素对象集合为空或null";
				screenShot("没有元素需要点击");
				this.log.info(message);
			}
			for (WebElement element : elements) {
				Point location = element.getLocation();
				this.highLight(element);
				waitUntilElementVisible(element);
				element.click();
				this.log.info(Messages.getString("AutoTest.click") + "[" + location.toString() + "]");
			}
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素集合失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * 单击定位到parentKey元素下childKey子元素
	 * 
	 * @param parentKey
	 *            文件中的定位父元素的key值
	 * @param childKey
	 *            文件中的定位子元素的key值
	 */
	public void clickSubElement(String parentKey, String childKey) {
		try {
			WebElement element = this.getSubElement(parentKey, childKey);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getSubElement(parentKey, childKey);
				if (element == null) {
					String message = "点击元素失败，根据父元素parentKey及子元素childKey未能找到元素";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			this.highLight(element);
			waitUntilElementVisible(element);
			element.click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + childKey + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * 单击定位到parentKey元素下childKey子元素
	 * 
	 * @param parentElement
	 *            父元素对象
	 * @param childKey
	 *            文件中的定位子元素的key值
	 */
	public void clickSubElement(WebElement parentElement, String childKey) {
		try {
			if (parentElement == null) {
				String message = "父元素不存在";
				this.log.error(message);
				throwException(message);
			}
			WebElement element = parentElement.findElement(this.getBy(childKey));
			if (element == null) {
				sleep(TIME_OUT);
				element = parentElement.findElement(this.getBy(childKey));
				if (element == null) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			waitUntilElementVisible(element);
			this.highLight(element);
			waitUntilElementVisible(element);
			element.click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + childKey + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * 单击定位到parentKey元素下childKey子元素
	 * 
	 * @param parentKey
	 *            文件中的定位父元素的key值
	 * @param childKey
	 *            文件中的定位子元素的key值
	 */
	public void clickSubElementContainsParentAttr(String parentKey, String attributeName, String attributevalue,
			String childKey) {
		try {
			WebElement element = this.getElementByAttr(parentKey, attributeName, attributevalue);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByAttr(parentKey, attributeName, attributevalue);
				if (element == null) {
					String message = "获取[" + parentKey + "]元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			WebElement subelement = this.getSubElement(element, childKey);
			if (subelement == null) {
				sleep(TIME_OUT);
				subelement = getSubElement(element, childKey);
				if (subelement == null) {
					String message = "获取[" + childKey + "]元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			this.highLight(subelement);
			waitUntilElementVisible(element);
			subelement.click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + childKey + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * 单击定位到parentKey元素下childKey子元素
	 * 
	 * @param parentKey
	 *            文件中的定位父元素的key值
	 * @param childKey
	 *            文件中的定位子元素的key值
	 */
	public void clickSubElementContainsParentText(String parentKey, String text, String childKey) {
		try {
			WebElement element = this.getElementByText(parentKey, text);
			if (element == null) {
				sleep(TIME_OUT);
				element = getElementByText(parentKey, text);
				if (element == null) {
					String message = "获取[" + parentKey + "]元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			WebElement subelement = this.getSubElement(element, childKey);
			if (subelement == null) {
				sleep(TIME_OUT);
				subelement = getSubElement(element, childKey);
				if (subelement == null) {
					String message = "获取[" + childKey + "]元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			this.highLight(subelement);
			waitUntilElementVisible(element);
			subelement.click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + childKey + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * 单击定位到parentKey元素下childKey子元素
	 * 
	 * @param parentKey
	 *            文件中的定位父元素的key值
	 * @param replaceKey
	 *            父元素定位值中需要被替换的值
	 * @param value
	 *            父元素定位值中替换值
	 * @param childKey
	 *            文件中的定位子元素的key值
	 */
	public void clickSubElement(String parentKey, String replaceKey, String value, String childKey) {
		try {
			WebElement element = this.getSubElementOfChangeParentKey(parentKey, replaceKey, value, childKey);
			if (element == null) {
				sleep(TIME_OUT);
				element = getSubElementOfChangeParentKey(parentKey, replaceKey, value, childKey);
				if (element == null) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			this.highLight(element);
			waitUntilElementVisible(element);
			element.click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + childKey + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * 单击定位到指定key值的元素，且元素的文本节点为text
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 * @param text
	 *            需要定位元素的文本信息
	 */
	public void clickAsText(String key, String text) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			for (WebElement element : elements) {
				String elementText = element.getText();
				if (elementText.equals(text)) {
					waitUntilElementVisible(element);
					this.highLight(element);
					waitUntilElementVisible(element);
					element.click();
					break;
				}
			}
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 单击定位到指定key值的元素，且元素的文本节点为text
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 * @param text
	 *            需要定位元素的文本信息
	 */
	public void clickContainsText(String key, String text) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			for (WebElement element : elements) {
				String elementText = element.getText();
				if (elementText.length() == 0) {
					continue;
				} else if (elementText.contains(text)) {
					waitUntilElementVisible(element);
					this.highLight(element);
					waitUntilElementVisible(element);
					element.click();
					this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
					break;
				}
			}
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * 单击定位到指定key值的元素，且元素的属性值为text
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 * @param attributeName
	 *            属性
	 * @param attributevalue
	 *            属性值
	 */
	public void clickAsAttributeValue(String key, String attributeName, String attributevalue) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			for (WebElement element : elements) {
				String elementValue = element.getAttribute(attributeName);
				if (elementValue == null) {
					continue;
				} else if (elementValue.equals(attributevalue)) {
					waitUntilElementVisible(element);
					this.highLight(element);
					element.click();
					this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
					break;
				}
			}
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * 单击element元素，且元素的属性值为text
	 * 
	 * @param key
	 *            element元素
	 * @param attributeName
	 *            属性
	 * @param attributevalue
	 *            属性值
	 */
	public void clickAsAttributeValue(WebElement element, String attributeName, String attributevalue) {
		if (element == null) {
			String message = "点击元素失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		String elementValue = element.getAttribute(attributeName);
		if (elementValue.equals(attributevalue)) {
			try {
				waitUntilElementVisible(element);
				this.highLight(element);
				element.click();
			} catch (StaleElementReferenceException sere) {
				String error = "点击元素" + element.getLocation() + "失败";
				screenShot(error);
				this.log.error(error);
				throwException(error);
				return;
			}
			this.log.info(Messages.getString("AutoTest.click") + "[" + element.getLocation() + "]");
		}
	}

	/**
	 * 单击定位到指定key值的元素，且元素的属性值包含text
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 * @param attributeName
	 *            属性
	 * @param attributevalue
	 *            属性值
	 */
	public void clickContainsAttributeValue(String key, String attributeName, String attributevalue) {
		try {
			WebElement element = this.getElementByAttr(key, attributeName, attributevalue);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByAttr(key, attributeName, attributevalue);
				if (element == null) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			this.highLight(element);
			waitUntilElementVisible(element);
			element.click();
			this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * 单击定位到指定key值的元素，且元素的属性值包含text
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 * @param attributeName
	 *            属性
	 * @param attributevalue
	 *            属性值
	 */
	public void clickByAttributeValue(String key, String attributeName, String attributevalue) {
		try {
			WebElement element = this.getElementByAttr(key, attributeName, attributevalue);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByAttr(key, attributeName, attributevalue);
				if (element == null) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			this.highLight(element);
			waitUntilElementVisible(element);
			element.click();
			this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * 点击element元素，且元素的属性值包含text
	 * 
	 * @param element
	 *            页面元素
	 * @param attributeName
	 *            属性
	 * @param attributevalue
	 *            属性值
	 */
	public void clickByAttributeValue(WebElement element, String attributeName, String attributevalue) {
		try {
			if (element == null) {
				String message = "点击元素失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
			this.highLight(element);
			waitUntilElementVisible(element);
			if (element.getAttribute(attributeName).contains(attributevalue)) {
				element.click();
			}
			this.log.info(Messages.getString("AutoTest.click") + "[" + element.getLocation() + "]");
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + element.getLocation() + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * 单击根据父元素parentKey、子元素childkey、子元素text定位到的子元素
	 * 
	 * @param parentKey
	 *            文件中的定位父元素的key值
	 * @param childKey
	 *            文件中的定位子元素的key值
	 * @param text
	 *            需要定位元素的文本信息
	 */
	public void clickSubElementAsText(String parentKey, String childKey, String text) {
		List<WebElement> elements = this.getElements(parentKey);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(parentKey);
			if (elements == null || elements.size() == 0) {
				String message = "点击元素失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
		for (WebElement element : elements) {
			String elementText = element.getText();
			if (elementText.contains(text)) {
				List<WebElement> subElements = element.findElements(this.getBy(childKey));
				if (subElements == null || subElements.size() == 0) {
					sleep(TIME_OUT);
					subElements = element.findElements(this.getBy(childKey));
					if (subElements == null || subElements.size() == 0) {
						String message = childKey + "元素集合不存在";
						screenShot(message);
						this.log.error(message);
						throwException(message);
					}
				}
				for (WebElement subElement : subElements) {
					if (subElement.getText().equals(text)) {
						try {
							this.waitUntilElementVisible(subElement);
						} catch (ElementNotVisibleException ene) {
							this.log.error(Messages.getString("AutoTest.getElment") + childKey
									+ Messages.getString("AutoTest.fail"));
							String message = "获取元素[" + childKey + "]失败";
							this.screenShot(message);
							throwException(message);
						}
						this.highLight(subElement);
						try {
							waitUntilElementVisible(element);
							subElement.click();
							this.log.info(Messages.getString("AutoTest.click") + "[" + text + " "
									+ this.getElementDes(childKey) + "]");
							break;
						} catch (StaleElementReferenceException sere) {
							String error = "点击元素" + childKey + "失败";
							screenShot(error);
							this.log.error(error);
							throwException(error);
						}
					}
				}
			}
		}
	}

	/**
	 * 单击根据父元素parentKey、子元素childkey、子元素No定位到的子元素
	 * 
	 * @param parentKey
	 *            文件中的定位父元素的key值
	 * @param childKey
	 *            文件中的定位子元素的key值
	 * @param No
	 *            需要定位元素的序号
	 */
	public void clickSubElementAsNo(String parentKey, String childKey, String No) {
		List<WebElement> elements = this.getElements(parentKey);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(parentKey);
			if (elements == null || elements.size() == 0) {
				String message = parentKey + "元素集合不存在";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
		for (WebElement element : elements) {
			List<WebElement> subElements = element.findElements(this.getBy(childKey));
			if (subElements == null || subElements.size() == 0) {
				sleep(TIME_OUT);
				subElements = element.findElements(this.getBy(childKey));
				if (subElements == null || subElements.size() == 0) {
					String message = childKey + "元素集合不存在";
					screenShot(message);
					this.log.error(message);
					throwException(message);
				}
			}
			WebElement subElement = subElements.get(Integer.parseInt(No));
			try {
				this.waitUntilElementVisible(subElement);
			} catch (ElementNotVisibleException ene) {
				this.log.error(
						Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
				String message = "获取元素[" + childKey + "]失败";
				this.screenShot(message);
				throwException(message);
			}
			this.highLight(subElement);
			try {
				waitUntilElementVisible(element);
				subElement.click();
				this.log.info(Messages.getString("AutoTest.click") + "第" + No + "个" + this.getElementDes(childKey));
				break;
			} catch (StaleElementReferenceException sere) {
				String error = "点击元素" + childKey + "失败";
				screenShot(error);
				this.log.error(error);
				throwException(error);
			}
		}

	}

	/**
	 * 单击根据父元素parentKey、子元素text定位到的父元素文本中包含text的子元素
	 * 
	 * @param parentKey
	 *            文件中的定位父元素的key值
	 * @param childKey
	 *            文件中的定位子元素的key值
	 * @param text
	 *            需要定位元素的文本信息
	 */
	public void clickSubElementContainsText(String parentKey, String childKey, String text) {
		List<WebElement> elements = this.getElements(parentKey);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(parentKey);
			if (elements == null || elements.size() == 0) {
				String message = parentKey + "元素集合不存在";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
		for (WebElement element : elements) {
			String elementText = element.getText();
			if (elementText.contains(text)) {
				WebElement subElement = element.findElement(this.getBy(childKey));
				if (subElement == null) {
					sleep(TIME_OUT);
					subElement = element.findElement(this.getBy(childKey));
					if (subElement == null) {
						String message = childKey + "元素不存在";
						screenShot(message);
						this.log.error(message);
						throwException(message);
					}
				}
				try {
					this.waitUntilElementVisible(subElement);
				} catch (ElementNotVisibleException ene) {
					this.log.error(
							Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
					String message = "获取元素[" + childKey + "]失败";
					this.screenShot(message);
					throwException(message);
				}
				this.highLight(subElement);
				try {
					subElement.click();
					this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
					break;
				} catch (StaleElementReferenceException sere) {
					String error = "点击元素" + childKey + "失败";
					screenShot(error);
					this.log.error(error);
					throwException(error);
				}
			}
		}
	}

	/**
	 * 单击根据父元素parentElement、子元素text定位到的父元素文本中包含text的子元素
	 * 
	 * @param parentElement
	 *            父元素对象
	 * @param childKey
	 *            文件中的定位子元素的key值
	 * @param text
	 *            需要定位元素的文本信息
	 */
	public void clickSubElementContainsText(WebElement parentElement, String childKey, String text) {
		if (parentElement == null) {
			String message = "传入WebElement对象为空";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
		List<WebElement> subElements = parentElement.findElements(this.getBy(childKey));
		if (subElements == null) {
			sleep(TIME_OUT);
			subElements = parentElement.findElements(this.getBy(childKey));
			if (subElements == null) {
				String message = "通过父元素没有找到" + this.getElementDes(childKey) + "元素";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
		for (WebElement subElement : subElements) {
			String value = subElement.getText();
			if (value.contains(text)) {
				try {
					this.waitUntilElementVisible(subElement);
				} catch (ElementNotVisibleException ene) {
					this.log.error(
							Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
					String message = "获取元素[" + childKey + "]失败";
					this.screenShot(message);
					throwException(message);
				}
				this.highLight(subElement);
				try {
					subElement.click();
					this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
					break;
				} catch (StaleElementReferenceException sere) {
					String error = "点击元素" + childKey + "失败";
					screenShot(error);
					this.log.error(error);
					throwException(error);
				}
			}
		}
	}

	/**
	 * 单击定位到Key的元素
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 * @param number
	 *            定位到元素集中的index
	 */
	public void click(String key, int number) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			waitUntilElementVisible(elements.get(number));
			this.highLight(elements.get(number));
			elements.get(number).click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 点击定位到Key的元素
	 * 
	 * @param key
	 *            文件中的定位元素的key值
	 * @param replaceKey
	 *            文件中定位元素需要被替换的值
	 * @param value
	 *            替换值
	 * 
	 */
	public void click(String key, String replaceKey, String value) {
		try {
			WebElement element = this.getElement(key, replaceKey, value);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElement(key, replaceKey, value);
				if (element == null) {
					String message = "点击元素失败";
					screenShot(message);
					this.log.error(message);
					throwException(message);
					return;
				}
			}
			this.highLight(element);
			waitUntilElementVisible(element);
			element.click();
		} catch (StaleElementReferenceException sere) {
			String error = "点击元素" + key + "失败";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 点击元素
	 * 
	 * @param xpath
	 *            通过xpath定位元素
	 * @param des
	 *            描述信息,用于打印日志及错误时截图
	 */
	public void clickByXpath(String xpath, String des) {
		WebElement element = this.getElementByXpath(xpath, des);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElementByXpath(xpath, des);
			if (element == null) {
				String message = "点击元素失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		this.highLight(element);
		waitUntilElementVisible(element);
		element.click();
		this.log.info(Messages.getString("AutoTest.click") + "[" + des + "]");
	}

	/**
	 * 获取动作Actions对象
	 * 
	 * @return Actions对象
	 */
	protected Actions getActions() {
		return new Actions(this.browser);
	}

	/**
	 * 通过数据文件提供的数据，将动作元素sourcekey的元素拖拽到目标targetKey的位置释放。
	 * 
	 * @param soureceKey
	 *            被拖动元素在文件中的key值
	 * @param targetKey
	 *            目标元素在文件中的key值
	 */
	public void dragAndDrop(String soureceKey, String targetKey) {
		WebElement source = this.getElement(soureceKey);
		WebElement target = this.getElement(targetKey);
		if (source == null || target == null) {
			source = this.getElement(soureceKey);
			target = this.getElement(targetKey);
			if (source == null || target == null) {
				String message = "拖动元素" + soureceKey + "到" + targetKey + "失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(source);
		waitUntilElementVisible(target);
		this.dragAndDrop(source, target);
		this.log.info(Messages.getString("AutoTest.dragAndDrop") + "[" + this.getElementDes(soureceKey) + "]到["
				+ this.getElementDes(targetKey) + "]");
	}

	/**
	 * 将元素source拖拽到target的元素里，并释放
	 * 
	 * @param source
	 *            被拖动元素对象
	 * @param target
	 *            目标元素对象
	 */
	public void dragAndDrop(WebElement source, WebElement target) {
		if (source == null || target == null) {
			String message = "拖拽失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		waitUntilElementVisible(source);
		waitUntilElementVisible(target);
		Actions acts = this.getActions();
		acts.dragAndDrop(source, target).perform();
		this.log.info(
				Messages.getString("AutoTest.dragAndDrop") + "[" + source.getText() + "]到[" + target.getText() + "]");
	}

	/**
	 * 双击定位到key的元素
	 * 
	 * @param key
	 *            文件中的元素对象key值
	 */
	public void doubleClick(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "双击元素失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.doubleClick(element);
		this.log.info(Messages.getString("AutoTest.doubleClick") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 通过xpath,双击元素
	 * 
	 * @param xpath
	 *            需要双击元素的xpath值
	 * @param des
	 *            描述信息
	 */
	public void doubleClickByXpath(String xpath, String des) {
		WebElement element = this.getElementByXpath(xpath, des);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, des);
			if (element == null) {
				String message = "双击元素失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.doubleClick(element);
		this.log.info(Messages.getString("AutoTest.doubleClick") + "[" + des + "]");
	}

	/**
	 * 双击元素
	 * 
	 * @param element
	 *            需要处理的元素
	 */
	public void doubleClick(WebElement element) {
		if (element == null) {
			String message = "双击失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		waitUntilElementVisible(element);
		this.highLight(element);
		Actions act = this.getActions();
		this.log.info("执行鼠标双击");
		act.doubleClick(element).perform();
	}

	/**
	 * 点击元素，该元素是通过javascript获得
	 * 
	 * @param jscode
	 *            返回元素对象的js代码
	 */
	public void clickByJavaScript(String jscode) {
		WebElement jelement = this.getElementByJavaScript(jscode);
		if (jelement == null) {
			sleep(TIME_OUT);
			jelement = this.getElementByJavaScript(jscode);
			if (jelement == null) {
				String message = "点击失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		this.highLight(jelement);
		waitUntilElementVisible(jelement);
		jelement.click();
		this.log.info(Messages.getString("AutoTest.click") + "[" + jelement.getText() + "]");
	}

	/**
	 * 右键点击元素
	 * 
	 * @param key
	 *            在文件中的元素对象的key值
	 */
	public void rightClick(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				String message = "右击失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.rightClick(element);
	}

	/**
	 * 右键点击元素
	 * 
	 * @param element
	 *            需要右键点击的对象
	 */
	public void rightClick(WebElement element) {
		Actions act = new Actions(this.browser);
		WebElement we = element;
		if (we == null) {
			String message = "右键点击失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		this.highLight(we);
		waitUntilElementVisible(we);
		act.contextClick(we).perform();
		this.log.info(Messages.getString("AutoTest.rightClick") + "[" + we.getText() + "]");
	}

	/**
	 * 右键点击通过xpath定位的元素
	 * 
	 * @param xpath
	 *            定位元素的xpath
	 */
	public void rightClickByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "右键点击");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "右键点击");
			if (element == null) {
				String message = "右击失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.rightClick(element);
		this.log.info(Messages.getString("AutoTest.rightClick") + "[" + element.getText() + "]");
	}

	/**
	 * 鼠标移动到指定元素上
	 * 
	 * @param key
	 *            需要悬停的元素在文件中的索引
	 */
	public void moveOn(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "悬停失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.moveOn(element);
		this.log.info(Messages.getString("AutoTest.moveOn") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 鼠标移动到指定元素上
	 * 
	 * @param key
	 *            需要悬停的元素在文件中的索引
	 */
	public void moveOn(String key, String replaceKey, String replaceValue) {
		WebElement element = this.getElement(key, replaceKey, replaceValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key, replaceKey, replaceValue);
			if (element == null) {
				String message = "悬停失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.moveOn(element);
		this.log.info(Messages.getString("AutoTest.moveOn") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 鼠标移动到指定元素上
	 * 
	 * @param key
	 *            需要悬停的元素在文件中的索引
	 */
	public void moveOnByAttr(String key, String attrKey, String attrValue) {
		WebElement element = this.getElementByAttr(key, attrKey, attrValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByAttr(key, attrKey, attrValue);
			if (element == null) {
				String message = "悬停失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.moveOn(element);
		this.log.info(Messages.getString("AutoTest.moveOn") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * 鼠标移动到指定元素上
	 * 
	 * @param xpath
	 *            定位元素的xpath
	 */
	public void moveOnByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "鼠标悬停");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "鼠标悬停");
			if (element == null) {
				String message = "悬停失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.moveOn(element);
	}

	/**
	 * 鼠标移动到指定元素上
	 * 
	 * @param element
	 *            鼠标需要悬停的元素
	 */
	public void moveOn(WebElement element) {
		if (element == null) {
			String message = "鼠标悬停失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		this.highLight(element);
		waitUntilElementVisible(element);
		try {
			Thread.sleep(new Random(System.currentTimeMillis()).nextInt(1000));
			Actions act = new Actions(this.browser);
			act.moveToElement(element).build().perform();
			this.log.info(Messages.getString("AutoTest.moveOn") + "[" + element.getText() + "]");
			Thread.sleep(new Random(System.currentTimeMillis()).nextInt(1000));
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 鼠标移动
	 * 
	 * @param x
	 *            x轴移动像数
	 * @param y
	 *            y轴移动像数
	 * 
	 */
	public void moveByOffset(int x, int y) {
		try {
			Thread.sleep(new Random(System.currentTimeMillis()).nextInt(1000));
			Actions act = new Actions(this.browser);
			act.moveByOffset(x, y).build().perform();
			this.log.info(Messages.getString("AutoTest.moveOn") + "[" + x + "," + y + "]");
			Thread.sleep(new Random(System.currentTimeMillis()).nextInt(1000));
		} catch (InterruptedException e) {
		}
	}

	/**
	 * 鼠标点击不释放
	 */
	public void clickAndHold() {
		Actions act = new Actions(this.browser);
		act.clickAndHold().perform();
		this.log.info(Messages.getString("AutoTest.click") + Messages.getString("AutoTest.noRealease"));
	}

	/**
	 * 鼠标点击元素不释放
	 * 
	 * @param element
	 *            需要鼠标点击不释放的元素对象
	 */
	public void clickAndHold(WebElement element) {
		if (element == null) {
			String message = "鼠标左键按下不释放失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		waitUntilElementVisible(element);
		this.highLight(element);
		Actions act = new Actions(this.browser);
		act.clickAndHold(element).perform();
		this.log.info(Messages.getString("AutoTest.click") + "[" + element.getText() + "]"
				+ Messages.getString("AutoTest.noRealease"));
	}

	/**
	 * 鼠标点击元素不释放
	 * 
	 * @param key
	 *            需要悬停的元素在文件中的索引
	 */
	public void clickAndHold(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "鼠标左键按下不释放失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.clickAndHold(element);
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]"
				+ Messages.getString("AutoTest.noRealease"));
	}

	/**
	 * 鼠标点击元素不释放
	 * 
	 * @param xpath
	 *            定位元素的xpath
	 */
	public void clickAndHoldByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "鼠标左键按下不释放");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "鼠标左键按下不释放");
			if (element == null) {
				String message = "鼠标左键按下不释放失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.clickAndHold(element);
		this.log.info(Messages.getString("AutoTest.click") + "[" + element.getText() + "]"
				+ Messages.getString("AutoTest.noRealease"));
	}

	/**
	 * 鼠标释放
	 */
	public void release() {
		Actions act = new Actions(this.browser);
		act.release().perform();
		this.log.info(Messages.getString("AutoTest.realease"));
	}

	/**
	 * 鼠标释放
	 * 
	 * @param element
	 *            需要鼠标点击不释放的元素对象
	 */
	public void release(WebElement element) {
		if (element == null) {
			String message = "鼠标释放失败";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		waitUntilElementVisible(element);
		this.highLight(element);
		Actions act = new Actions(this.browser);
		act.release(element).perform();
		this.log.info(Messages.getString("AutoTest.realease"));
	}

	/**
	 * 鼠标释放
	 * 
	 * @param key
	 *            需要悬停的元素在文件中的索引
	 */
	public void release(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "鼠标释放失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.release(element);
		this.log.info(Messages.getString("AutoTest.realease"));
	}

	/**
	 * 鼠标释放
	 * 
	 * @param xpath
	 *            定位元素的xpath
	 */
	public void releaseByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "鼠标释放");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "鼠标释放");
			if (element == null) {
				String message = "鼠标释放失败";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(element);
		this.release(element);
		this.log.info(Messages.getString("AutoTest.realease"));
	}
}
