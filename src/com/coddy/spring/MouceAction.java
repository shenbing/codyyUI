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
 * MouceAction ��������װ��
 * 
 * @author shenbing
 * 
 */
public class MouceAction extends SubElement {
	/**
	 * ��־���
	 */
	protected Log log = LogUtils.getLog(MouceAction.class);

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public MouceAction() {
		super();
	}

	/**
	 * ���췽��
	 * 
	 * @param bt
	 *            BrowserTypeö��ֵ
	 */
	public MouceAction(BrowserType bt) {
		super(bt);
	}

	/**
	 * ����Ԫ��
	 * 
	 * @param element
	 *            �����Ԫ�ض���
	 */
	public void click(WebElement element) {
		if (element == null) {
			String message = "���Ԫ��ʧ��,Ԫ��Ϊ��";
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
	 * ������λ��Key��Ԫ��
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 */
	public void click(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "���Ԫ��ʧ��,Ԫ��Ϊ��";
				screenShot(key + "δ���ҵ�Ԫ��");
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot("���Ԫ��" + key + "ʧ��");
			this.log.error(error);
			throwException(error);
			return;
		} catch (WebDriverException e) {
			screenShot("���Ԫ��" + key + "ʧ��");
			this.log.error(e);
			sleep(TIME_OUT);
			waitUntilElementVisible(element);
			element.click();
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * ������λ��Key������Ԫ��
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 */
	public void clicks(String key) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "û��Ԫ����Ҫ���";
					screenShot(key + "û��Ԫ����Ҫ���");
					this.log.info(message);
				}
			}
			for (WebElement element : elements) {
				this.highLight(element);
				try {
					waitUntilElementVisible(element);
					element.click();
				} catch (TimeoutException e) {
					screenShot(element.getLocation() + "Ԫ�ز��ɼ�");
					log.error(element.getLocation() + "Ԫ�ز��ɼ�");
				}
			}
		} catch (StaleElementReferenceException sere) {
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * ��������Ԫ��
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 */
	public void clicks(List<WebElement> elements) {
		try {
			if (elements == null || elements.size() == 0) {
				String message = "����Ԫ�ض��󼯺�Ϊ�ջ�null";
				screenShot("û��Ԫ����Ҫ���");
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
			String error = "���Ԫ�ؼ���ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * ������λ��parentKeyԪ����childKey��Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 */
	public void clickSubElement(String parentKey, String childKey) {
		try {
			WebElement element = this.getSubElement(parentKey, childKey);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getSubElement(parentKey, childKey);
				if (element == null) {
					String message = "���Ԫ��ʧ�ܣ����ݸ�Ԫ��parentKey����Ԫ��childKeyδ���ҵ�Ԫ��";
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
			String error = "���Ԫ��" + childKey + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * ������λ��parentKeyԪ����childKey��Ԫ��
	 * 
	 * @param parentElement
	 *            ��Ԫ�ض���
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 */
	public void clickSubElement(WebElement parentElement, String childKey) {
		try {
			if (parentElement == null) {
				String message = "��Ԫ�ز�����";
				this.log.error(message);
				throwException(message);
			}
			WebElement element = parentElement.findElement(this.getBy(childKey));
			if (element == null) {
				sleep(TIME_OUT);
				element = parentElement.findElement(this.getBy(childKey));
				if (element == null) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + childKey + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * ������λ��parentKeyԪ����childKey��Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 */
	public void clickSubElementContainsParentAttr(String parentKey, String attributeName, String attributevalue,
			String childKey) {
		try {
			WebElement element = this.getElementByAttr(parentKey, attributeName, attributevalue);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByAttr(parentKey, attributeName, attributevalue);
				if (element == null) {
					String message = "��ȡ[" + parentKey + "]Ԫ��ʧ��";
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
					String message = "��ȡ[" + childKey + "]Ԫ��ʧ��";
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
			String error = "���Ԫ��" + childKey + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * ������λ��parentKeyԪ����childKey��Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 */
	public void clickSubElementContainsParentText(String parentKey, String text, String childKey) {
		try {
			WebElement element = this.getElementByText(parentKey, text);
			if (element == null) {
				sleep(TIME_OUT);
				element = getElementByText(parentKey, text);
				if (element == null) {
					String message = "��ȡ[" + parentKey + "]Ԫ��ʧ��";
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
					String message = "��ȡ[" + childKey + "]Ԫ��ʧ��";
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
			String error = "���Ԫ��" + childKey + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * ������λ��parentKeyԪ����childKey��Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param replaceKey
	 *            ��Ԫ�ض�λֵ����Ҫ���滻��ֵ
	 * @param value
	 *            ��Ԫ�ض�λֵ���滻ֵ
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 */
	public void clickSubElement(String parentKey, String replaceKey, String value, String childKey) {
		try {
			WebElement element = this.getSubElementOfChangeParentKey(parentKey, replaceKey, value, childKey);
			if (element == null) {
				sleep(TIME_OUT);
				element = getSubElementOfChangeParentKey(parentKey, replaceKey, value, childKey);
				if (element == null) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + childKey + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
	}

	/**
	 * ������λ��ָ��keyֵ��Ԫ�أ���Ԫ�ص��ı��ڵ�Ϊtext
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 * @param text
	 *            ��Ҫ��λԪ�ص��ı���Ϣ
	 */
	public void clickAsText(String key, String text) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * ������λ��ָ��keyֵ��Ԫ�أ���Ԫ�ص��ı��ڵ�Ϊtext
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 * @param text
	 *            ��Ҫ��λԪ�ص��ı���Ϣ
	 */
	public void clickContainsText(String key, String text) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * ������λ��ָ��keyֵ��Ԫ�أ���Ԫ�ص�����ֵΪtext
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 * @param attributeName
	 *            ����
	 * @param attributevalue
	 *            ����ֵ
	 */
	public void clickAsAttributeValue(String key, String attributeName, String attributevalue) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * ����elementԪ�أ���Ԫ�ص�����ֵΪtext
	 * 
	 * @param key
	 *            elementԪ��
	 * @param attributeName
	 *            ����
	 * @param attributevalue
	 *            ����ֵ
	 */
	public void clickAsAttributeValue(WebElement element, String attributeName, String attributevalue) {
		if (element == null) {
			String message = "���Ԫ��ʧ��";
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
				String error = "���Ԫ��" + element.getLocation() + "ʧ��";
				screenShot(error);
				this.log.error(error);
				throwException(error);
				return;
			}
			this.log.info(Messages.getString("AutoTest.click") + "[" + element.getLocation() + "]");
		}
	}

	/**
	 * ������λ��ָ��keyֵ��Ԫ�أ���Ԫ�ص�����ֵ����text
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 * @param attributeName
	 *            ����
	 * @param attributevalue
	 *            ����ֵ
	 */
	public void clickContainsAttributeValue(String key, String attributeName, String attributevalue) {
		try {
			WebElement element = this.getElementByAttr(key, attributeName, attributevalue);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByAttr(key, attributeName, attributevalue);
				if (element == null) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * ������λ��ָ��keyֵ��Ԫ�أ���Ԫ�ص�����ֵ����text
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 * @param attributeName
	 *            ����
	 * @param attributevalue
	 *            ����ֵ
	 */
	public void clickByAttributeValue(String key, String attributeName, String attributevalue) {
		try {
			WebElement element = this.getElementByAttr(key, attributeName, attributevalue);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByAttr(key, attributeName, attributevalue);
				if (element == null) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * ���elementԪ�أ���Ԫ�ص�����ֵ����text
	 * 
	 * @param element
	 *            ҳ��Ԫ��
	 * @param attributeName
	 *            ����
	 * @param attributevalue
	 *            ����ֵ
	 */
	public void clickByAttributeValue(WebElement element, String attributeName, String attributevalue) {
		try {
			if (element == null) {
				String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + element.getLocation() + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
	}

	/**
	 * �������ݸ�Ԫ��parentKey����Ԫ��childkey����Ԫ��text��λ������Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param text
	 *            ��Ҫ��λԪ�ص��ı���Ϣ
	 */
	public void clickSubElementAsText(String parentKey, String childKey, String text) {
		List<WebElement> elements = this.getElements(parentKey);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(parentKey);
			if (elements == null || elements.size() == 0) {
				String message = "���Ԫ��ʧ��";
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
						String message = childKey + "Ԫ�ؼ��ϲ�����";
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
							String message = "��ȡԪ��[" + childKey + "]ʧ��";
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
							String error = "���Ԫ��" + childKey + "ʧ��";
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
	 * �������ݸ�Ԫ��parentKey����Ԫ��childkey����Ԫ��No��λ������Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param No
	 *            ��Ҫ��λԪ�ص����
	 */
	public void clickSubElementAsNo(String parentKey, String childKey, String No) {
		List<WebElement> elements = this.getElements(parentKey);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(parentKey);
			if (elements == null || elements.size() == 0) {
				String message = parentKey + "Ԫ�ؼ��ϲ�����";
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
					String message = childKey + "Ԫ�ؼ��ϲ�����";
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
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.screenShot(message);
				throwException(message);
			}
			this.highLight(subElement);
			try {
				waitUntilElementVisible(element);
				subElement.click();
				this.log.info(Messages.getString("AutoTest.click") + "��" + No + "��" + this.getElementDes(childKey));
				break;
			} catch (StaleElementReferenceException sere) {
				String error = "���Ԫ��" + childKey + "ʧ��";
				screenShot(error);
				this.log.error(error);
				throwException(error);
			}
		}

	}

	/**
	 * �������ݸ�Ԫ��parentKey����Ԫ��text��λ���ĸ�Ԫ���ı��а���text����Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param text
	 *            ��Ҫ��λԪ�ص��ı���Ϣ
	 */
	public void clickSubElementContainsText(String parentKey, String childKey, String text) {
		List<WebElement> elements = this.getElements(parentKey);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(parentKey);
			if (elements == null || elements.size() == 0) {
				String message = parentKey + "Ԫ�ؼ��ϲ�����";
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
						String message = childKey + "Ԫ�ز�����";
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
					String message = "��ȡԪ��[" + childKey + "]ʧ��";
					this.screenShot(message);
					throwException(message);
				}
				this.highLight(subElement);
				try {
					subElement.click();
					this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
					break;
				} catch (StaleElementReferenceException sere) {
					String error = "���Ԫ��" + childKey + "ʧ��";
					screenShot(error);
					this.log.error(error);
					throwException(error);
				}
			}
		}
	}

	/**
	 * �������ݸ�Ԫ��parentElement����Ԫ��text��λ���ĸ�Ԫ���ı��а���text����Ԫ��
	 * 
	 * @param parentElement
	 *            ��Ԫ�ض���
	 * @param childKey
	 *            �ļ��еĶ�λ��Ԫ�ص�keyֵ
	 * @param text
	 *            ��Ҫ��λԪ�ص��ı���Ϣ
	 */
	public void clickSubElementContainsText(WebElement parentElement, String childKey, String text) {
		if (parentElement == null) {
			String message = "����WebElement����Ϊ��";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
		List<WebElement> subElements = parentElement.findElements(this.getBy(childKey));
		if (subElements == null) {
			sleep(TIME_OUT);
			subElements = parentElement.findElements(this.getBy(childKey));
			if (subElements == null) {
				String message = "ͨ����Ԫ��û���ҵ�" + this.getElementDes(childKey) + "Ԫ��";
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
					String message = "��ȡԪ��[" + childKey + "]ʧ��";
					this.screenShot(message);
					throwException(message);
				}
				this.highLight(subElement);
				try {
					subElement.click();
					this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(childKey) + "]");
					break;
				} catch (StaleElementReferenceException sere) {
					String error = "���Ԫ��" + childKey + "ʧ��";
					screenShot(error);
					this.log.error(error);
					throwException(error);
				}
			}
		}
	}

	/**
	 * ������λ��Key��Ԫ��
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 * @param number
	 *            ��λ��Ԫ�ؼ��е�index
	 */
	public void click(String key, int number) {
		try {
			List<WebElement> elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				sleep(TIME_OUT);
				elements = this.getElements(key);
				if (elements == null || elements.size() == 0) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * �����λ��Key��Ԫ��
	 * 
	 * @param key
	 *            �ļ��еĶ�λԪ�ص�keyֵ
	 * @param replaceKey
	 *            �ļ��ж�λԪ����Ҫ���滻��ֵ
	 * @param value
	 *            �滻ֵ
	 * 
	 */
	public void click(String key, String replaceKey, String value) {
		try {
			WebElement element = this.getElement(key, replaceKey, value);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElement(key, replaceKey, value);
				if (element == null) {
					String message = "���Ԫ��ʧ��";
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
			String error = "���Ԫ��" + key + "ʧ��";
			screenShot(error);
			this.log.error(error);
			throwException(error);
			return;
		}
		this.log.info(Messages.getString("AutoTest.click") + "[" + this.getElementDes(key) + "]");
	}

	/**
	 * ���Ԫ��
	 * 
	 * @param xpath
	 *            ͨ��xpath��λԪ��
	 * @param des
	 *            ������Ϣ,���ڴ�ӡ��־������ʱ��ͼ
	 */
	public void clickByXpath(String xpath, String des) {
		WebElement element = this.getElementByXpath(xpath, des);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElementByXpath(xpath, des);
			if (element == null) {
				String message = "���Ԫ��ʧ��";
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
	 * ��ȡ����Actions����
	 * 
	 * @return Actions����
	 */
	protected Actions getActions() {
		return new Actions(this.browser);
	}

	/**
	 * ͨ�������ļ��ṩ�����ݣ�������Ԫ��sourcekey��Ԫ����ק��Ŀ��targetKey��λ���ͷš�
	 * 
	 * @param soureceKey
	 *            ���϶�Ԫ�����ļ��е�keyֵ
	 * @param targetKey
	 *            Ŀ��Ԫ�����ļ��е�keyֵ
	 */
	public void dragAndDrop(String soureceKey, String targetKey) {
		WebElement source = this.getElement(soureceKey);
		WebElement target = this.getElement(targetKey);
		if (source == null || target == null) {
			source = this.getElement(soureceKey);
			target = this.getElement(targetKey);
			if (source == null || target == null) {
				String message = "�϶�Ԫ��" + soureceKey + "��" + targetKey + "ʧ��";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		waitUntilElementVisible(source);
		waitUntilElementVisible(target);
		this.dragAndDrop(source, target);
		this.log.info(Messages.getString("AutoTest.dragAndDrop") + "[" + this.getElementDes(soureceKey) + "]��["
				+ this.getElementDes(targetKey) + "]");
	}

	/**
	 * ��Ԫ��source��ק��target��Ԫ������ͷ�
	 * 
	 * @param source
	 *            ���϶�Ԫ�ض���
	 * @param target
	 *            Ŀ��Ԫ�ض���
	 */
	public void dragAndDrop(WebElement source, WebElement target) {
		if (source == null || target == null) {
			String message = "��קʧ��";
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
				Messages.getString("AutoTest.dragAndDrop") + "[" + source.getText() + "]��[" + target.getText() + "]");
	}

	/**
	 * ˫����λ��key��Ԫ��
	 * 
	 * @param key
	 *            �ļ��е�Ԫ�ض���keyֵ
	 */
	public void doubleClick(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "˫��Ԫ��ʧ��";
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
	 * ͨ��xpath,˫��Ԫ��
	 * 
	 * @param xpath
	 *            ��Ҫ˫��Ԫ�ص�xpathֵ
	 * @param des
	 *            ������Ϣ
	 */
	public void doubleClickByXpath(String xpath, String des) {
		WebElement element = this.getElementByXpath(xpath, des);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, des);
			if (element == null) {
				String message = "˫��Ԫ��ʧ��";
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
	 * ˫��Ԫ��
	 * 
	 * @param element
	 *            ��Ҫ�����Ԫ��
	 */
	public void doubleClick(WebElement element) {
		if (element == null) {
			String message = "˫��ʧ��";
			screenShot(message);
			this.log.error(message);
			throwException(message);
			return;
		}
		waitUntilElementVisible(element);
		this.highLight(element);
		Actions act = this.getActions();
		this.log.info("ִ�����˫��");
		act.doubleClick(element).perform();
	}

	/**
	 * ���Ԫ�أ���Ԫ����ͨ��javascript���
	 * 
	 * @param jscode
	 *            ����Ԫ�ض����js����
	 */
	public void clickByJavaScript(String jscode) {
		WebElement jelement = this.getElementByJavaScript(jscode);
		if (jelement == null) {
			sleep(TIME_OUT);
			jelement = this.getElementByJavaScript(jscode);
			if (jelement == null) {
				String message = "���ʧ��";
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
	 * �Ҽ����Ԫ��
	 * 
	 * @param key
	 *            ���ļ��е�Ԫ�ض����keyֵ
	 */
	public void rightClick(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				String message = "�һ�ʧ��";
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
	 * �Ҽ����Ԫ��
	 * 
	 * @param element
	 *            ��Ҫ�Ҽ�����Ķ���
	 */
	public void rightClick(WebElement element) {
		Actions act = new Actions(this.browser);
		WebElement we = element;
		if (we == null) {
			String message = "�Ҽ����ʧ��";
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
	 * �Ҽ����ͨ��xpath��λ��Ԫ��
	 * 
	 * @param xpath
	 *            ��λԪ�ص�xpath
	 */
	public void rightClickByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "�Ҽ����");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "�Ҽ����");
			if (element == null) {
				String message = "�һ�ʧ��";
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
	 * ����ƶ���ָ��Ԫ����
	 * 
	 * @param key
	 *            ��Ҫ��ͣ��Ԫ�����ļ��е�����
	 */
	public void moveOn(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "��ͣʧ��";
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
	 * ����ƶ���ָ��Ԫ����
	 * 
	 * @param key
	 *            ��Ҫ��ͣ��Ԫ�����ļ��е�����
	 */
	public void moveOn(String key, String replaceKey, String replaceValue) {
		WebElement element = this.getElement(key, replaceKey, replaceValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key, replaceKey, replaceValue);
			if (element == null) {
				String message = "��ͣʧ��";
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
	 * ����ƶ���ָ��Ԫ����
	 * 
	 * @param key
	 *            ��Ҫ��ͣ��Ԫ�����ļ��е�����
	 */
	public void moveOnByAttr(String key, String attrKey, String attrValue) {
		WebElement element = this.getElementByAttr(key, attrKey, attrValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByAttr(key, attrKey, attrValue);
			if (element == null) {
				String message = "��ͣʧ��";
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
	 * ����ƶ���ָ��Ԫ����
	 * 
	 * @param xpath
	 *            ��λԪ�ص�xpath
	 */
	public void moveOnByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "�����ͣ");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "�����ͣ");
			if (element == null) {
				String message = "��ͣʧ��";
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
	 * ����ƶ���ָ��Ԫ����
	 * 
	 * @param element
	 *            �����Ҫ��ͣ��Ԫ��
	 */
	public void moveOn(WebElement element) {
		if (element == null) {
			String message = "�����ͣʧ��";
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
	 * ����ƶ�
	 * 
	 * @param x
	 *            x���ƶ�����
	 * @param y
	 *            y���ƶ�����
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
	 * ��������ͷ�
	 */
	public void clickAndHold() {
		Actions act = new Actions(this.browser);
		act.clickAndHold().perform();
		this.log.info(Messages.getString("AutoTest.click") + Messages.getString("AutoTest.noRealease"));
	}

	/**
	 * �����Ԫ�ز��ͷ�
	 * 
	 * @param element
	 *            ��Ҫ��������ͷŵ�Ԫ�ض���
	 */
	public void clickAndHold(WebElement element) {
		if (element == null) {
			String message = "���������²��ͷ�ʧ��";
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
	 * �����Ԫ�ز��ͷ�
	 * 
	 * @param key
	 *            ��Ҫ��ͣ��Ԫ�����ļ��е�����
	 */
	public void clickAndHold(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "���������²��ͷ�ʧ��";
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
	 * �����Ԫ�ز��ͷ�
	 * 
	 * @param xpath
	 *            ��λԪ�ص�xpath
	 */
	public void clickAndHoldByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "���������²��ͷ�");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "���������²��ͷ�");
			if (element == null) {
				String message = "���������²��ͷ�ʧ��";
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
	 * ����ͷ�
	 */
	public void release() {
		Actions act = new Actions(this.browser);
		act.release().perform();
		this.log.info(Messages.getString("AutoTest.realease"));
	}

	/**
	 * ����ͷ�
	 * 
	 * @param element
	 *            ��Ҫ��������ͷŵ�Ԫ�ض���
	 */
	public void release(WebElement element) {
		if (element == null) {
			String message = "����ͷ�ʧ��";
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
	 * ����ͷ�
	 * 
	 * @param key
	 *            ��Ҫ��ͣ��Ԫ�����ļ��е�����
	 */
	public void release(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "����ͷ�ʧ��";
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
	 * ����ͷ�
	 * 
	 * @param xpath
	 *            ��λԪ�ص�xpath
	 */
	public void releaseByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "����ͷ�");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "����ͷ�");
			if (element == null) {
				String message = "����ͷ�ʧ��";
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
