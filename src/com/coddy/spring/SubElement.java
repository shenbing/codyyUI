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
 * SubElement ҳ����Ԫ���࣬��װʵ��Frame��Alert��ز�������
 * 
 * @author shenbing
 * 
 */
public class SubElement extends Element {
	/**
	 * ��־���
	 */
	protected Log log = LogUtils.getLog(SubElement.class);

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public SubElement() {
		super();
	}

	/**
	 * ���췽��
	 * 
	 * @param bt
	 *            BrowserTypeö��ֵ
	 */
	public SubElement(BrowserType bt) {
		super(bt);
	}

	/**
	 * ����ǰִ�е�driver��ѡ�񵽰���ָ��Ԫ�ص�frame��
	 * 
	 * @param key
	 *            ��yaml�ļ��д洢��frame
	 * @return �������������
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
			String message = "yaml�ļ��� Fram " + key + "�ṩ��������";
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
				this.log.info("�л���Frame[" + ov + "]�ɹ�");
			} catch (TimeoutException nfe) {
				String title = "��ȡFrame " + key + "ʧ��";
				this.screenShot(title);
				throwException(title);
			}
		} else {
			innerFrame = selectFrame(ov);
		}
		return innerFrame;
	}

	/**
	 * ����ǰִ�е�driver��ѡ�񵽰���ָ��Ԫ�ص�frame��
	 * 
	 * @param driver
	 *            �������������
	 * @param element
	 *            frameԪ�ض���
	 * @return �������������
	 */
	protected WebDriver selectFrame(WebDriver driver, WebElement element) {
		WebDriver innerFrame = null;
		if (driver == null || element == null) {
			this.log.error("��ȡԪ��ʧ�ܣ��޷�����");
			throwException("�л�frame����Iframeʧ��");
			return null;
		}
		try {
			WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
			ExceptFrame ef = new ExceptFrame(element);
			innerFrame = wait.until(ef);
			this.log.info("�л���Frame[" + element.getLocation() + "]�ɹ�");
		} catch (NoSuchFrameException e) {
			String message = "��Ҫ�л���frame����Iframe������";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} catch (StaleElementReferenceException e) {
			String message = "Ŀ��frame�в�������Ԫ�ض�λ";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return innerFrame;
	}

	/**
	 * ����ǰִ�е�driver��ѡ�񵽰���ָ��Ԫ�ص�frame��
	 * 
	 * @param element
	 *            frameԪ�ض���
	 * @return �������������
	 */
	public WebDriver selectFrameByElement(WebElement element) {
		return this.selectFrame(this.browser, element);
	}

	/**
	 * ����ǰִ�е�driver��ѡ�񵽰���ָ��Ԫ�ص�frame��
	 * 
	 * @param elementKey
	 *            ��λframeԪ�ص�keyֵ
	 */
	public void selectFrameByElement(String elementKey) {
		WebElement element = this.getElement(elementKey);
		this.selectFrame(this.browser, element);
	}

	/**
	 * ����ǰִ�е�driver��ѡ�񵽰���ָ��Ԫ�ص�frame��
	 * 
	 * @param par
	 *            �ɶ�λframe��ֵ
	 * @return �������������
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
				this.log.info("�л�����" + it + "��frame");
			} catch (TimeoutException nfe) {
				isOk = false;
			}
		} else {
			try {
				ExceptFrame ef = new ExceptFrame(par);
				innerFrame = wait.until(ef);
				this.log.info("�л��� frame '" + par + "'");
			} catch (TimeoutException nfe) {
				isOk = false;
			}
		}
		if (!isOk) {
			String error = "�л��� frame'" + par + "'ʧ��";
			this.log.error(error);
			this.screenShot(error);
			throwException(error);
		}
		return innerFrame;
	}

	/**
	 * ѡ��Ĭ��Frame
	 * 
	 * @return �������������
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
			this.log.info("�л��� Ĭ��frame�ɹ�");
		} catch (TimeoutException nfe) {
			isOk = false;
			this.log.info("�л��� Ĭ��frame��ʱʧ��");
		}
		return defaultFrameDriver;
	}

	/**
	 * ѡ��Frame
	 * 
	 * @return �������������
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
			this.log.info("�л�����frame�ɹ�");
		} catch (TimeoutException nfe) {
			isOk = false;
			this.log.info("�л�����frame��ʱʧ��");
		}
		return defaultFrameDriver;
	}

	/**
	 * ������һ��ʱ����л���alert
	 * 
	 * @param seconds
	 *            �л���ʱʱ��
	 * @return true / false
	 */
	public boolean switchToAlert(int seconds) {
		long start = System.currentTimeMillis();
		while ((System.currentTimeMillis() - start) < seconds * 1000) {
			try {
				this.browser.switchTo().alert();
				this.log.info("�л���Alert�ɹ���");
				return true;
			} catch (NoAlertPresentException ne) {
				throwException("��ǰû�п����л��ľ�����ʾ�� ");
			} catch (Exception e) {
				this.log.error("��ǰû�п����л��ľ�����ʾ�� ");
				throwException("��ǰû�п����л��ľ�����ʾ��");
			}
		}
		return false;
	}

	/**
	 * ���alert������ȷ����ť
	 */
	public void acceptAlert() {
		try {
			Alert alert = this.browser.switchTo().alert();
			alert.accept();
			this.log.info("������洰��ȷ����ť");
		} catch (NoAlertPresentException e) {
			String message = "���洰������߲�����";
			this.screenShot(message);
			throwException(message);
		}
	}

	/**
	 * ���alert�����е�ȡ����ť
	 */
	public void cancelAlert() {
		try {
			Alert alert = this.browser.switchTo().alert();
			alert.dismiss();
			this.log.info("������洰��ȡ����ť");
		} catch (NoAlertPresentException e) {
			String message = "���洰������߲�����";
			this.screenShot(message);
			throwException(message);
		}
	}

	/**
	 * ��ȡalert�����еľ�����Ϣ
	 * 
	 * @return alert�������е���Ϣ
	 */
	public String getTextOfAlert() {
		String text = null;
		try {
			Alert alert = this.browser.switchTo().alert();
			text = alert.getText();
		} catch (NoAlertPresentException e) {
			String message = "���洰������߲�����";
			this.screenShot(message);
			throwException(message);
		}
		return text;
	}

}
