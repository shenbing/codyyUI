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
 * PageAction ҳ�������װ��
 * 
 * @author shenbing
 * 
 */
public class PageAction extends MouceAction {
	/**
	 * ��־���
	 */
	protected Log log = LogUtils.getLog(PageAction.class);

	/**
	 * �ù��췽��������Ĭ��ѡ��IEΪ����
	 */
	public PageAction() {
		super();
	}

	/**
	 * ������������� {BrowserType {IE, FIREFOX, CHROME,HTTPUNIT }} Using eg:
	 * BrowserType.IE,BrowserType.FIREFOX
	 * 
	 * @param bt
	 *            ���������ö��ֵ
	 */
	public PageAction(BrowserType bt) {
		super(bt);
	}

	/**
	 * ���ý�����Ԫ����
	 * 
	 * @param xpath
	 *            �ɶ�λԪ�ض����xpathֵ
	 */
	public void focusOnWithXpath(String xpath) {
		try {
			WebElement element = this.getElementByXpath(xpath, "���ý���");
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElementByXpath(xpath, "���ý���");
				if (element == null) {
					this.log.error("��" + xpath + "��Ԫ�����ý���ʧ��");
					screenShot("��" + xpath + "��Ԫ�����ý���ʧ��");
					return;
				}
			}
			this.highLight(element);
			this.executeJavaScript("arguments[0].focus()", element);
		} catch (StaleElementReferenceException sere) {
			String error = "ͨ��" + xpath + "��ȡ��Ԫ��" + "���ý���";
			this.screenShot(error);
			this.log.error(error);
			return;
		}
	}

	/**
	 * ���ý�����Ԫ����
	 * 
	 * @param key
	 *            �ļ��пɶ�λԪ�ض����keyֵ
	 */
	public void focusOn(String key) {
		try {
			WebElement element = this.getElement(key);
			if (element == null) {
				sleep(TIME_OUT);
				element = this.getElement(key);
				if (element == null) {
					this.log.error("Ԫ��" + key + "���ý���ʧ��");
					screenShot("Ԫ��" + key + "���ý���ʧ��");
					return;
				}
			}
			this.highLight(element);
			this.executeJavaScript("arguments[0].focus()", element);
		} catch (StaleElementReferenceException sere) {
			this.log.error("Ԫ��" + key + "���ý���ʧ��");
			screenShot("Ԫ��" + key + "���ý���ʧ��");
			return;
		}
	}

	/**
	 * ͨ��javaScript��ȡ���������ı���Ȼ����������������������
	 * 
	 * @param jscode
	 *            ��λ���������ı����javascript�ű�
	 * @param value
	 *            ��Ҫ���������
	 */
	public void inputByJavaScript(String jscode, String value) {
		WebElement jelement = this.getElementByJavaScript(jscode);
		if (jelement == null) {
			sleep(TIME_OUT);
			jelement = this.getElementByJavaScript(jscode);
			if (jelement == null) {
				screenShot("��ȡԪ��ʧ�ܣ��޷�����");
				this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
				return;
			}
		}
		this.highLight(jelement);
		jelement.sendKeys(value);
	}

	/**
	 * �ڵȵ�ָ������ɼ�֮���ڸö����������������һ������������ѡ���
	 * 
	 * @param key
	 *            ��yaml�ļ��е�Ԫ��
	 */
	public void clear(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				String message = "����������ݻ���ȡ��ѡ��ʧ��";
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
			String message = this.getElementDes(key) + "�ڵ�ǰҳ���ϲ��ɼ�";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = this.getElementDes(key) + "clear ����ʧ��";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * �ڵȵ�ָ������ɼ�֮���ڸö����������������һ������������ѡ���
	 * 
	 * @param element
	 *            ���ֵ��ҳ��Ԫ��
	 */
	public void clear(WebElement element) {
		if (element == null) {
			String message = "����������ݻ���ȡ��ѡ��ʧ��";
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
			String message = element.getLocation() + "�ڵ�ǰҳ���ϲ��ɼ�";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = element.getLocation() + "clear ����ʧ��";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * �ڵȵ�ָ������ɼ�֮���ڸö����������������һ������������ѡ���
	 * 
	 * @param key
	 *            ��yaml�ļ��е�Ԫ��
	 */
	public void clear(String key, String replacekey, String replaceValue) {
		WebElement element = this.getElement(key, replacekey, replaceValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key, replacekey, replaceValue);
			if (element == null) {
				String message = "����������ݻ���ȡ��ѡ��ʧ��";
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
			String message = this.getElementDes(key) + "�ڵ�ǰҳ���ϲ��ɼ�";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = this.getElementDes(key) + "clear ����ʧ��";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * �ڵȵ�ָ������ɼ�֮���ڸö����������������һ������������ѡ���
	 * 
	 * @param key
	 *            ��yaml�ļ��е�Ԫ��
	 */
	public void clears(String key) {
		List<WebElement> elements = this.getElements(key);
		if (elements == null) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null) {
				String message = "û����Ҫ���������Ԫ��";
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
				String message = this.getElementDes(key) + "�ڵ�ǰҳ���ϲ��ɼ�";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			} catch (Exception e) {
				String message = this.getElementDes(key) + "clear ����ʧ��";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
	}

	/**
	 * �ڵȵ�ָ������ɼ�֮���ڸö����������������һ������������ѡ���
	 * 
	 * @param elements
	 *            Ԫ�ؼ���
	 */
	public void clears(List<WebElement> elements) {
		if (elements == null || elements.size() == 0) {
			String message = "����Ԫ�ض��󼯺�Ϊ�ջ�null";
			screenShot(message);
			this.log.info(message);
		}
		for (WebElement element : elements) {
			try {
				waitUntilElementVisible(element);
				writenableElement(element);
				element.clear();
			} catch (ElementNotVisibleException e) {
				String message = "Ԫ��" + element.getLocation() + "�ڵ�ǰҳ���ϲ��ɼ�";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			} catch (Exception e) {
				String message = "Ԫ��" + element.getLocation() + "clear ����ʧ��";
				screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
	}

	/**
	 * �ڵȵ�ָ������ɼ�֮���ڸö����������������һ������������ѡ���
	 * 
	 * @param xpath
	 *            ��Ҫ��λԪ�ص�xpath
	 */
	public void clearByXpath(String xpath) {
		WebElement element = this.getElementByXpath(xpath, "��ջ���ȡ��ѡ��");
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElementByXpath(xpath, "��ջ���ȡ��ѡ��");
			if (element == null) {
				String message = "��ͨ��xpath[" + xpath + "]��ȡ��Ԫ������������ݻ���ȡ��ѡ������";
				screenShot(message);
				this.log.error(message);
				throwException(message);
				return;
			}
		}
		try {
			waitUntilElementVisible(element);
		} catch (ElementNotVisibleException e) {
			String message = "ͨ��" + xpath + "��λ��Ԫ���ڵ�ǰҳ���ϲ��ɼ�";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
		element.clear();
	}

	/**
	 * ɾ�����ı�����
	 * 
	 * @param key
	 *            ��Ҫ��λ���ı�Ԫ�ص�keyֵ
	 */
	public void deleteRichText(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				String message = "����������ݻ���ȡ��ѡ��ʧ��";
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
			String message = this.getElementDes(key) + "�ڵ�ǰҳ���ϲ��ɼ�";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		} catch (Exception e) {
			String message = this.getElementDes(key) + "clear ����ʧ��";
			screenShot(message);
			this.log.error(message);
			throwException(message);
		}
	}

	/**
	 * ��λ��key���������ֱ����������
	 * 
	 * @param key
	 *            ��yaml�ļ��е�Ԫ��
	 * @param value
	 *            ���������
	 */
	public void input(String key, String value) {
		WebElement element = getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				String message = "��ȡԪ��ʧ�ܣ��޷�����";
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
	 * ��λ��key���������ֱ����������
	 * 
	 * @param element
	 *            ����ֵ��ҳ��Ԫ��
	 * @param value
	 *            ���������
	 */
	public void input(WebElement element, String value) {
		if (element == null) {
			String message = "��ȡԪ��ʧ�ܣ��޷�����";
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
	 * ��λ��key���������ֱ����������
	 * 
	 * @param key
	 *            ��yaml�ļ��е�Ԫ��
	 * @param value
	 *            ���������
	 */
	public void inputs(String key, String value) {
		List<WebElement> webElements = getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
				this.log.error("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
				throwException("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
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
	 * ��λ��key���������ֱ����������
	 * 
	 * @param key
	 *            ��yaml�ļ��е�Ԫ��
	 * @param value
	 *            ���������
	 */
	public void inputAsNo(String key, String value, int No) {
		List<WebElement> webElements = getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
				this.log.error("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
				throwException("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
			}
		}
		if (No >= webElements.size()) {
			screenShot("��ȡԪ��ʧ�ܣ��޷�����");
			this.log.error(No + "�������ѷ���Ԫ����������0��ʼ��");
			throwException(No + "�������ѷ���Ԫ����������0��ʼ��");
		}
		WebElement element = webElements.get(No);
		if (element == null) {
			String message = "��ȡԪ��ʧ�ܣ��޷�����";
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
	 * ��λ��key���������ֱ����������
	 * 
	 * @param key
	 *            ��yaml�ļ��е�Ԫ��
	 * @param replaceKey
	 *            ��yaml�ļ���keyֵ����Ҫ���滻��ֵ
	 * @param valueKey
	 *            ��yaml�ļ���keyֵ����Ҫ�滻��ֵ
	 * @param value
	 *            ���������
	 */
	public void input(String key, String replaceKey, String valueKey, String value) {
		WebElement element = getElement(key, replaceKey, valueKey);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key, replaceKey, valueKey);
			if (element == null) {
				String message = "��ȡԪ��ʧ�ܣ��޷�����";
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
	 * ģ����̼����������
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
						this.log.info("ģ����̼���" + k.name());
						break R;
					}
				}
			}
		} catch (Exception e) {
			throwException("���̲���ʧ��");
		}
	}

	/**
	 * ѡ��Radio
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void selectRadio(String key) {
		WebElement webElement = this.getElement(key);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key);
			if (webElement == null) {
				screenShot("��ȡԪ��ʧ�ܣ��޷�����");
				this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
				throwException("Radio����ʧ��");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("����ʧ��");
			throwException("����ʧ��");
		}
		if (!webElement.isSelected()) {
			webElement.click();
		}
	}

	/**
	 * ѡ��Radio
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void selectRadio(WebElement webElement) {
		if (webElement == null) {
			screenShot("��ȡԪ��ʧ�ܣ��޷�����");
			this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
			throwException("Radio����ʧ��");
			return;
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("����ʧ��");
			throwException("����ʧ��");
		}
		if (!webElement.isSelected()) {
			webElement.click();
		}
	}

	/**
	 * ѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void selectCheckBox(String key) {
		WebElement webElement = this.getElement(key);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key);
			if (webElement == null) {
				screenShot(key + "Ԫ��δ����");
				this.log.error(key + ":Ԫ��δ����");
				throwException(getElementDes(key) + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot(key + "����ʧ��");
			throwException("����ʧ��");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * ѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void selectCheckBox(WebElement webElement) {
		if (webElement == null) {
			screenShot("����Ԫ��Ϊnull");
			this.log.error("����Ԫ��Ϊnull");
			throwException("����Ԫ��Ϊnull");
			return;
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("�޷�����");
			throwException("����ʧ��");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * ȥѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void unselectCheckBox(String key) {
		WebElement webElement = this.getElement(key);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key);
			if (webElement == null) {
				screenShot(key + "����ʧ��");
				this.log.error(key + ":����ʧ��");
				throwException(getElementDes(key) + ":����ʧ��");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot(key + "����ʧ��");
			throwException("����ʧ��");
		}
		if (webElement.isEnabled()) {
			if (webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * ȥѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void unselectCheckBox(WebElement webElement) {
		if (webElement == null) {
			screenShot("����Ԫ�ض���Ϊ��");
			this.log.error("����Ԫ�ض���Ϊ��");
			throwException("����Ԫ�ض���Ϊ��");
			return;
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("����ʧ��");
			throwException("����ʧ��");
		}
		if (webElement.isEnabled()) {
			if (webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * ȥѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void unselectCheckBox(String key, String replaceKey, String replaceValue) {
		WebElement webElement = this.getElement(key, replaceKey, replaceValue);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key, replaceKey, replaceValue);
			if (webElement == null) {
				screenShot("��ȡԪ��ʧ�ܣ��޷�����");
				this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
				throwException("CheckBox����ʧ��");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("����ʧ��");
			throwException("����ʧ��");
		}
		if (webElement.isEnabled()) {
			if (webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * ѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void selectCheckBoxByAttr(String key, String attrname, String attrvalue) {
		WebElement webElement = this.getElementByAttr(key, attrname, attrvalue);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElementByAttr(key, attrname, attrvalue);
			if (webElement == null) {
				screenShot("��ȡԪ��ʧ�ܣ��޷�����");
				this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
				throwException("CheckBox����ʧ��");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("����ʧ��");
			throwException("����ʧ��");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * ѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void selectCheckBox(String key, String replaceKey, String replaceValue) {
		WebElement webElement = this.getElement(key, replaceKey, replaceValue);
		if (webElement == null) {
			sleep(TIME_OUT);
			webElement = this.getElement(key, replaceKey, replaceValue);
			if (webElement == null) {
				screenShot("��ȡԪ��ʧ�ܣ��޷�����");
				this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
				throwException("CheckBox����ʧ��");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(webElement);
		} catch (Exception e) {
			screenShot("����ʧ��");
			throwException("����ʧ��");
		}
		if (webElement.isEnabled()) {
			if (!webElement.isSelected()) {
				webElement.click();
			}
		}
	}

	/**
	 * ȥѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void batchUnselectCheckBox(String key) {
		List<WebElement> webElements = this.getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = this.getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot(key + "Ԫ�ؼ���δ����");
				this.log.info(key + ":Ԫ�ؼ���δ����");
			}
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(key + "Ԫ�ؼ���δ����");
				this.log.error(key + "Ԫ�ز��ɼ�");
			}
			if (webElement.isEnabled()) {
				if (webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * ȥѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void batchUnselectCheckBox(List<WebElement> webElements) {
		if (webElements == null || webElements.size() == 0) {
			screenShot("����Ԫ�ض��󼯺�Ϊ�ջ�null");
			this.log.info("����Ԫ�ض��󼯺�Ϊ�ջ�null");
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(webElement.getLocation() + "Ԫ�ز��ɼ�");
				this.log.error(webElement.getLocation() + "Ԫ�ز��ɼ�");
			}
			if (webElement.isEnabled()) {
				if (webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * ����ѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void batchSelectCheckBox(String key) {
		List<WebElement> webElements = this.getElements(key);
		if (webElements == null || webElements.size() == 0) {
			sleep(TIME_OUT);
			webElements = this.getElements(key);
			if (webElements == null || webElements.size() == 0) {
				screenShot(key + "Ԫ��δ����");
				this.log.info(key + ":Ԫ��δ����");
			}
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(key + "Ԫ�ز��ɼ�");
				this.log.error(key + "Ԫ�ز��ɼ�");
			}
			if (webElement.isEnabled()) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * ����ѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void batchSelectCheckBox(List<WebElement> webElements) {
		if (webElements == null || webElements.size() == 0) {
			screenShot("������󼯺�Ϊ�ջ�null");
			this.log.info("������󼯺�Ϊ�ջ�null");
		}
		for (WebElement webElement : webElements) {
			try {
				waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot(webElement.getLocation() + "Ԫ�ز��ɼ�");
				this.log.error(webElement.getLocation() + "Ԫ�ز��ɼ�");
			}
			if (webElement.isEnabled()) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * ����ѡ��CheckBox
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 */
	public void selectCheckBox(String... key) {
		int num = key.length;
		for (int i = 0; i < num; i++) {
			WebElement webElement = this.getElement(key[i]);
			if (webElement == null) {
				sleep(TIME_OUT);
				webElement = this.getElement(key[i]);
				if (webElement == null) {
					screenShot("��ȡ��" + key[i] + "��Ԫ��ʧ��");
					this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
					throwException("CheckBox����ʧ��");
					return;
				}
			}
			try {
				this.waitUntilElementVisible(webElement);
			} catch (Exception e) {
				screenShot("����ʧ��");
				throwException("����ʧ��");
			}
			if (webElement.isEnabled()) {
				if (!webElement.isSelected()) {
					webElement.click();
				}
			}
		}
	}

	/**
	 * �����ַ���
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * @param value
	 *            ��Ҫ���õ��ַ���
	 */
	public void setValue(String key, String value) {
		this.writenableElement(key);
		this.input(key, value);
	}

	/**
	 * ����Ԫ��text�ı�
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * @param value
	 *            ��Ҫ���õ��ַ���
	 */
	public void setText(String key, String value) {
		WebElement element = getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				screenShot(key + "Ԫ��δ����");
				log.error(key + ":Ԫ��δ����");
				throwException(key + ":Ԫ��δ����");
				return;
			}
		}
		String arg = "arguments[0].innerHTML = \"" + value + "\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * ����Ԫ��text�ı�
	 * 
	 * @param element
	 *            Ԫ�ض���
	 * @param value
	 *            ��Ҫ���õ��ַ���
	 */
	public void setText(WebElement element, String value) {
		if (element == null) {
			screenShot("����Ԫ��Ϊ��");
			throwException("����Ԫ��Ϊ��");
			return;
		}
		String arg = "arguments[0].innerHTML = \"" + value + "\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * ��������
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * @param value
	 *            ��Ҫ���õ����ڣ���ʽΪ2014-04-10
	 */
	public void setDate(String key, String value) {
		this.writenableElement(key);
		this.setElementValue(key, value);
		this.sleep(0.5);
		this.sendKeys(Keys.ENTER);
	}

	/**
	 * ��������
	 * 
	 * @param webElement
	 *            ��Ҫ����ֵ�Ķ���
	 * @param value
	 *            ��Ҫ���õ����ڣ���ʽΪ2014-04-10
	 */
	public void setDate(WebElement element, String value) {
		if (element == null) {
			screenShot("����Ԫ��Ϊ��");
			throwException("����Ԫ��Ϊ��");
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
				String message = key + "��ȡԪ��ʧ�ܣ��޷�����";
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
			screenShot("����Ԫ�ض���Ϊ��");
			this.log.error("����Ԫ�ض���Ϊ��");
			throwException("����Ԫ�ض���Ϊ��");
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
	 * ����ָ�����ѡ��yaml�ļ��е�keyԪ�������б��е�ѡ�
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * @param index
	 *            ��Ҫѡ���ѡ�������ֵ
	 */
	public void selectItemByIndex(String key, int index) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "Ԫ��δ����");
				this.log.error(key + ":Ԫ��δ����");
				throwException(key + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			screenShot("����ʧ��");
			throwException("����ʧ��");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByIndex(index);
			this.log.info("ѡ�������б��" + index + "��ѡ��");
		} catch (UnexpectedTagNameException utne) {
			screenShot("ѡ�������б��" + index + "��ѡ��ʧ��");
			this.log.error("ͨ��" + key + "��ȡ��Ԫ��" + this.getElementDes(key) + ",����HTML���������б�");
			throwException("�����б����ʧ��");
		} catch (NoSuchElementException nee) {
			screenShot("ѡ�������б��" + index + "��ѡ��ʧ��");
			this.log.error("�ؼ�" + this.getElementDes(key) + ",����ͨ������" + index + "��ȡOptions��ֵ");
			throwException("�����б����ʧ��");
		}
	}

	/**
	 * ����ָ�����ѡ�����xpath�������б��е�ѡ�
	 * 
	 * @param xpath
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * @param index
	 *            ��Ҫ����ѡ��ֵ������
	 */
	public void selectXpathItemByIndex(String xpath, int index) {
		WebElement element = this.getElementByXpath(xpath, "��λ�б��");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "��λ�б��");
			if (element == null) {
				screenShot(xpath + "Ԫ��δ����");
				this.log.error(xpath + ":Ԫ��δ����");
				throwException(xpath + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "��ǰԪ�ز��ɼ�";
			this.screenShot(error);
			log.error("��ǰԪ�ز��ɼ�");
			throwException("�����б����ʧ��");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByIndex(index);
			this.log.info("ѡ�������б��" + index + "��ѡ��");
		} catch (UnexpectedTagNameException utne) {
			String error = "��ǰԪ�ز���HTML���������б�";
			this.screenShot(error);
			this.log.error(error);
			throwException("�����б����ʧ��");
		} catch (NoSuchElementException nee) {
			String error = "�����ؼ�����ͨ������" + index + "��ȡOptions��ֵ";
			this.screenShot(error);
			this.log.error(error);
			throwException("�����б����ʧ��");
		}
	}

	/**
	 * ��ȡyaml�ļ���key�������б����ı�����Ϊtext��ѡ��
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * @param text
	 *            ��Ҫѡ����ı�����
	 */
	public void selectItemByText(String key, String text) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "Ԫ��δ����");
				this.log.error(key + ":Ԫ��δ����");
				throwException(key + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			screenShot("�����б����ʧ��,Ԫ�ز��ܷ���");
			throwException("�����б����ʧ��,Ԫ�ز��ܷ���");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByVisibleText(text);
			this.log.info("ѡ�������б�[" + text + "]");
		} catch (UnexpectedTagNameException utne) {
			screenShot("ѡ�������б�[" + text + "]ʧ��");
			this.log.error("ͨ��" + key + "��ȡ��Ԫ��" + this.getElementDes(key) + ",����HTML���������б�");
			throwException("�����б����ʧ��");
		} catch (NoSuchElementException nee) {
			screenShot(this.getElementDes(key) + ",����ͨ��" + text + "��ȡOptions��ֵ");
			this.log.error("�ؼ�" + this.getElementDes(key) + ",����ͨ��" + text + "��ȡOptions��ֵ");
			throwException("�����б����ʧ��");
		}
	}

	/**
	 * ��ȡyaml�ļ���key�������б����ı�����Ϊtext��ѡ��
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * @param text
	 *            ��Ҫѡ����ı�����
	 */
	public void selectItemByText(String key, String replaceKey, String replaceValue, String text) {
		WebElement element = this.getElement(key, replaceKey, replaceValue);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "Ԫ��δ����");
				this.log.error(key + ":Ԫ��δ����");
				throwException(key + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			screenShot("�����б����ʧ��");
			throwException("�����б����ʧ��");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByVisibleText(text);
			this.log.info("ѡ�������б�[" + text + "]");
		} catch (UnexpectedTagNameException utne) {
			screenShot("ѡ�������б�[" + text + "]ʧ��");
			this.log.error("ͨ��" + key + "��ȡ��Ԫ��" + this.getElementDes(key) + ",����HTML���������б�");
			throwException("�����б����ʧ��");
		} catch (NoSuchElementException nee) {
			screenShot("ѡ�������б�[" + text + "]ʧ��");
			this.log.error("�ؼ�" + this.getElementDes(key) + ",����ͨ��" + text + "��ȡOptions��ֵ");
			throwException("�����б����ʧ��");
		}
	}

	/**
	 * ͨ��Xpathѡȡ�����б��ѡ�����ı�Ϊtext��ѡ��
	 * 
	 * @param xpath
	 *            �����б��xpath
	 * @param text
	 *            �ı�ѡ��
	 */
	public void selectItemTextByXpath(String xpath, String text) {
		WebElement element = this.getElementByXpath(xpath, "��λ�б��");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "��λ�б��");
			if (element == null) {
				screenShot(xpath + "Ԫ��δ����");
				this.log.error(xpath + ":Ԫ��δ����");
				throwException(xpath + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "��ǰԪ�ز��ɼ�";
			this.screenShot(error);
			log.error("��ǰԪ�ز��ɼ�");
			throwException("�����б����ʧ��");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByVisibleText(text);
			this.log.info("ѡ�������б�[" + text + "]");
		} catch (UnexpectedTagNameException utne) {
			String error = "��ǰԪ�ز���HTML���������б�";
			this.screenShot(error);
			this.log.error(error);
			throwException("�����б����ʧ��");
		} catch (NoSuchElementException nee) {
			String error = "�����ؼ�����ͨ���ı�����" + text + "��ȡѡ��ֵ";
			this.screenShot(error);
			this.log.error(error);
			throwException("�����б����ʧ��");
		}
	}

	/**
	 * ����ָ��ѡ���ʵ��ֵ�����ǿɼ��ı�ֵ�����Ƕ���ġ�value�����Ե�ֵ��ѡ�������б��е�ѡ�
	 * 
	 * @param key
	 *            �ļ��ж�λԪ�ض����keyֵ
	 * 
	 * @param itemValue
	 *            �����б������valueֵ
	 */
	public void selectItemByValue(String key, String itemValue) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = getElement(key);
			if (element == null) {
				screenShot(key + "Ԫ��δ����");
				this.log.error(key + ":Ԫ��δ����");
				throwException(key + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "��ǰԪ�ز��ɼ�";
			this.screenShot(error);
			log.error("��ǰԪ�ز��ɼ�");
			throwException("�����б����ʧ��");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByValue(itemValue);
			this.log.info("ѡ�������б���valueֵΪ[" + itemValue + "]��ѡ��");
		} catch (UnexpectedTagNameException utne) {
			String error = "��ǰԪ�ز���HTML���������б�";
			this.screenShot(error);
			this.log.error("ͨ��" + key + "��ȡ��Ԫ��" + this.getElementDes(key) + ",����HTML���������б�");
			throwException("�����б����ʧ��");
		} catch (NoSuchElementException nee) {
			String error = "�����ؼ�����ͨ��" + itemValue + "��ȡOptions��ֵ";
			this.screenShot(error);
			this.log.error("�ؼ�" + this.getElementDes(key) + ",����ͨ��ѡ��ֵ" + itemValue + "ѡ��ÿؼ�");
			throwException("�����б����ʧ��");
		}
	}

	/**
	 * ���ո�����xpath��λ�������б���ͨ��ָ��ѡ���ʵ��ֵ�����ǿɼ��ı�ֵ�����Ƕ���ġ�value�����Ե�ֵ��ѡ�������б��е�ѡ�
	 * 
	 * @param xpath
	 *            �Զ��嶨λԪ�ص�xpath
	 * @param itemValue
	 *            �����б������valueֵ
	 */
	public void selectItemValueByXpath(String xpath, String itemValue) {
		WebElement element = this.getElementByXpath(xpath, "�����б�ؼ�");
		if (element == null) {
			sleep(TIME_OUT);
			element = getElementByXpath(xpath, "�����б�ؼ�");
			if (element == null) {
				screenShot(xpath + "Ԫ��δ����");
				this.log.error(xpath + ":Ԫ��δ����");
				throwException(xpath + ":Ԫ��δ����");
				return;
			}
		}
		try {
			this.waitUntilElementVisible(element);
		} catch (Exception e) {
			String error = "��ǰԪ�ز��ɼ�";
			this.screenShot(error);
			log.error("��ǰԪ�ز��ɼ�");
			throwException("�����б����ʧ��");
		}
		Select select = null;
		this.highLight(element);
		try {
			select = new Select(element);
			select.selectByValue(itemValue);
			this.log.info("ѡ�������б���valueֵΪ[" + itemValue + "]��ѡ��");
		} catch (UnexpectedTagNameException utne) {
			String error = "��ǰԪ�ز���HTML���������б�";
			this.screenShot(error);
			this.log.error("ͨ��" + xpath + "��λ��Ԫ�ز���HTML���������б�");
			throwException("�����б����ʧ��");
		} catch (NoSuchElementException nee) {
			String error = "�����ؼ�����ͨ��" + itemValue + "��ȡOptions��ֵ";
			this.screenShot(error);
			this.log.error("�����б�ؼ�,����ͨ��ѡ��ֵ" + itemValue + "ѡ��ÿؼ�");
			throwException("�����б����ʧ��");
		}
	}

	/**
	 * ��ȡtableָ��Cellֵ
	 * 
	 * @param key
	 *            ��λtableԪ�ص�keyֵ
	 * @param tableCellAddress
	 *            table��Cell�Ķ�Ӧֵ��eg:1.2
	 * @return ָ��Cell��ֵ
	 */
	public String getCellTextByID(String key, String tableCellAddress) {
		WebElement tableElement = this.getElement(key);
		if (tableElement == null) {
			sleep(TIME_OUT);
			tableElement = this.getElement(key);
			if (tableElement == null) {
				screenShot(key + "Ԫ��δ����");
				log.error(key + ":Ԫ��δ����");
				throwException(key + ":Ԫ��δ����");
			}
		}
		// ����Ҫ���ҵĵ�Ԫ��λ���ַ������зֽ⣬�õ����Ӧ�С��С�
		int index = tableCellAddress.trim().indexOf('.');
		int row = Integer.parseInt(tableCellAddress.substring(0, index));
		int cell = Integer.parseInt(tableCellAddress.substring(index + 1));
		// �õ�table���������ж��󣬲��õ���Ҫ��ѯ���ж���
		List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
		if (rows == null || rows.size() == 0) {
			sleep(TIME_OUT);
			rows = tableElement.findElements(By.tagName("tr"));
			if (rows == null || rows.size() == 0) {
				screenShot("��" + rows + "��Ԫ��δ����");
				log.error("��" + rows + "��Ԫ��δ����");
				throwException("��" + rows + "��Ԫ��δ����");
			}
		}
		if (row >= rows.size()) {
			screenShot(rows + "����Ԫ�ؼ�������");
			log.error(rows + "����Ԫ�ؼ�������");
			throwException(rows + "����Ԫ�ؼ�������");
		}
		WebElement theRow = rows.get(row);
		// ����getCell�����õ���Ӧ���ж���Ȼ��õ�Ҫ��ѯ���ı���
		String text = getCell(theRow, cell).getText();
		return text;
	}

	/**
	 * ��ȡtable�е�ָ��Cell
	 * 
	 * @param Row
	 *            table����Ԫ�ض���
	 * @param cell
	 *            table��cell��ֵ
	 * @return CellԪ�ض���
	 */
	private WebElement getCell(WebElement row, int cell) {
		if (row == null) {
			screenShot("����Ԫ��Ϊ�ջ�null");
			log.error("����Ԫ��Ϊ�ջ�null");
		}
		List<WebElement> cells;
		WebElement target = null;
		// ��������"<th>"��"<td>"���ֱ�ǩ�����Էֿ�����
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
