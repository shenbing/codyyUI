package com.coddy.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.dom4j.DocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.coddy.condition.ExceptElement;
import com.coddy.condition.ExceptElements;
import com.coddy.utils.LogUtils;
import com.coddy.utils.Messages;
import com.coddy.utils.ConfigProperties;

/**
 * Element Ԫ�ط�װ�࣬ʵ��ҳ��Ԫ����ز�������
 * 
 * @author shenbing
 * 
 */
public class Element extends Browser {
	/**
	 * ElementData����List
	 */
	protected ArrayList<ElementData> traceList;
	/**
	 * ҳ��Ԫ���ļ��б�(yaml)
	 */
	protected ArrayList<File> fileList;
	/**
	 * ҳ��Ԫ���ļ��б�(DB table)
	 */
	protected ArrayList<String> tableList;

	/**
	 * ��־���
	 */
	protected Log log = LogUtils.getLog(Element.class);

	/**
	 * 
	 */
	protected String HIGHLIGHT_BORDER = ConfigProperties.getInstance().getString("AutoTest.hightLitht.border");

	/**
	 * Ĭ�Ϲ��췽��
	 */
	public Element() {
		super();
		this.fileList = new ArrayList<File>();
		this.tableList = new ArrayList<String>();
		this.traceList = new ArrayList<ElementData>();
	}

	/**
	 * ���췽��
	 * 
	 * @param bt
	 *            BrowserTypeö��ֵ
	 */
	public Element(BrowserType bt) {
		super(bt);
		this.fileList = new ArrayList<File>();
		this.tableList = new ArrayList<String>();
		this.traceList = new ArrayList<ElementData>();
	}

	/**
	 * ����ҳ���ļ����ݣ�yaml)
	 * 
	 * @param file
	 *            yaml��File����
	 */
	protected void loadData(File file, FileType ft) {
		ElementData yamlData = null;
		try {
			yamlData = new ElementData(file, this.browser, ft);
		} catch (UnsupportedEncodingException e) {
			this.log.error("Ԫ��ҳ���ļ� " + file.getAbsolutePath() + "�ı��뷽ʽ�����ϣ�����GBKͳһ���롣");
			throwException("��������");
		} catch (FileNotFoundException e) {
			this.log.error("Ԫ��ҳ���ļ�" + file.getAbsolutePath() + "�����ڡ�");
			throwException("yaml�ļ������ڣ�����ʧ��");
		} catch (IOException e) {
			this.log.error("Ԫ��ҳ���ļ�" + file.getAbsolutePath() + "�򿪻�ر��쳣");
			throwException("�ļ�����ʧ��");
		} catch (DocumentException e) {
			this.log.error("Ԫ��ҳ���ļ�" + file.getAbsolutePath() + "�򿪻�ر��쳣");
			throwException("�ļ�����ʧ��");
		}
		if (yamlData.getDataHash() == null) {
			this.log.error(Messages.getString("AutoTest.loadYamlfile") + file.getAbsolutePath()
					+ Messages.getString("AutoTest.fail"));
			throwException("�����ļ�ʧ��");
		} else {
			if (traceList.contains(yamlData)) {
				throwException("�ظ������ļ�");
			} else {
				traceList.add(yamlData);
				this.log.info(Messages.getString("AutoTest.loadYamlfile") + file.getAbsolutePath()
						+ Messages.getString("AutoTest.finish"));
			}
		}
	}

	/**
	 * �������ݱ��ȡԪ�صķ�ʽ
	 * 
	 * @param tableName
	 *            DB��table��
	 */
	protected void loadData(String tableName) {
		ElementData DbData = null;
		try {
			DbData = new ElementData(tableName, this.browser);

		} catch (Exception e) {
			this.log.error("Ԫ��ҳ�����ݿ��" + tableName + "�����ڡ�");
			throwException("���ݿ�����ڣ�����ʧ��" + e.getMessage());
		}
		if (DbData.getDataHash() == null) {
			this.log.error(
					Messages.getString("AutoTest.loadDbTable") + tableName + Messages.getString("AutoTest.fail"));
			throwException("�������ݿ��ʧ��");
		} else {
			if (traceList.contains(DbData)) {
				throwException("�ظ��������ݿ��");
			} else {
				traceList.add(DbData);
				this.log.info(
						Messages.getString("AutoTest.loadDbTable") + tableName + Messages.getString("AutoTest.finish"));
			}
		}
	}

	/**
	 * ����ҳ�������ļ�
	 * 
	 * @param pageFilePath
	 *            ҳ��Ԫ���ļ�·����yaml)
	 */
	public void loadPageData(String pageFilePath, FileType ft) {
		File f = new File(pageFilePath);
		if (!fileList.contains(f)) {
			this.fileList.add(f);
			this.loadData(f, ft);
		}
	}

	/**
	 * ����ҳ�����ݿ��
	 * 
	 * @param tableName
	 *            ҳ��Ԫ��DB ����
	 */
	public void loadPageDataDb(String tableName) {

		if (!tableList.contains(tableName)) {
			this.tableList.add(tableName);
			this.loadData(tableName);
		}
	}

	/**
	 * ����Ԫ�ظ�����ʾ
	 * 
	 * @param element
	 *            ҳ��Ԫ�ض���
	 */
	public void highLight(WebElement element) {
		if (element == null) {
			return;
		}
		String arg = "arguments[0].style.border = \"" + HIGHLIGHT_BORDER + "\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * ��ȡָ��key��Ԫ��
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getElement(String key) {
		WebElement element = null;
		try {
			for (ElementData trace : this.traceList) {
				element = trace.getElement(key);
				if (element != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		// try {
		// this.waitUntilElementVisible(element);
		// } catch (ElementNotVisibleException ene) {
		// this.log.error(Messages.getString("AutoTest.getElment") + key +
		// Messages.getString("AutoTest.fail"));
		// String message = "��ȡԪ��[" + key + "]ʧ��";
		// this.screenShot(message);
		// throwException(message);
		// }
		return element;
	}

	/**
	 * ��ȡָ��key��Ԫ��
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return Ԫ�ض���
	 */
	public List<WebElement> getElements(String key) {
		List<WebElement> elements = null;
		try {
			for (ElementData trace : this.traceList) {
				elements = trace.getElements(key);
				if (elements != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return elements;
	}

	/**
	 * ��ȡָ��key��Ԫ��
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param number
	 *            keyԪ�ص�������0��ʼ
	 * @return Ԫ�ض���
	 */
	public WebElement getElementAsNo(String key, int number) {
		List<WebElement> elements = null;
		try {
			for (ElementData trace : this.traceList) {
				elements = trace.getElements(key);
				if (elements != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (elements.size() < number) {
			throwException(key + "��λ��Ԫ������С��numberָ������");
		}
		return elements.get(number);
	}

	/**
	 * ��ȡԪ������
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return ����Ԫ������
	 */
	public int getElementsCount(String key) {
		List<WebElement> elements = null;
		try {
			for (ElementData trace : this.traceList) {
				elements = trace.getElements(key);
				if (elements != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (elements == null || elements.size() == 0) {
			return 0;
		}
		return elements.size();
	}

	/**
	 * ��ȡָ��key��Ԫ��
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param timeout
	 *            ָ����ʱʱ��
	 * @return Ԫ�ض���
	 */
	public WebElement getElement(String key, int timeout) {
		WebElement element = null;
		try {
			for (ElementData trace : this.traceList) {
				element = trace.getElement(key, timeout);
				if (element != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return element;
	}

	/**
	 * ��ȡָ��keyֵ��Ԫ��,����Ҫ�޸ĵ�replaceKey����value
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param replaceKey
	 *            ���滻ֵ
	 * @param value
	 *            �滻��ֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getElement(String key, String replaceKey, String value) {
		WebElement element = null;
		try {
			for (ElementData trace : this.traceList) {
				element = trace.getElement(key, replaceKey, value);
				if (element != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "��ȡԪ��[" + key + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return element;
	}

	/**
	 * ��ȡָ��By�����Ԫ��
	 * 
	 * @param by
	 *            ���Զ�λԪ�ص�By����
	 * @return Ԫ�ض���
	 */
	public WebElement getElement(By by) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
			element = wait.until(new ExceptElement(by));
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + by.toString().split(":")[1].trim().split(">")[0] + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "��ȡԪ��[" + by.toString().split(":")[1].trim().split(">")[0] + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return element;
	}

	/**
	 * ��ȡָ��By����Ķ��Ԫ��
	 * 
	 * @param by
	 *            ���Զ�λԪ�ص�By����
	 * @return Ԫ�ض���
	 */
	public List<WebElement> getElements(By by) {
		List<WebElement> elements = null;
		try {
			WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
			elements = wait.until(new ExceptElements(by));
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + by.toString().split(":")[1].trim().split(">")[0] + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (elements == null || elements.size() == 0) {
			String message = "��ȡԪ��[" + by.toString().split(":")[1].trim().split(">")[0] + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return elements;
	}

	/**
	 * �����ṩxpath��ȡ��ǰҳ���ϵ�Ԫ�� ��Ҫ�����ṩ��������Ԫ��
	 * 
	 * @param xpath
	 *            Ԫ��xpathֵ
	 * @param des
	 *            xpath����ֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getElementByXpath(String xpath, String des) {
		WebElement we = null;
		if (xpath == null || xpath.length() == 0) {
			this.log.error("xpathΪ�ա���λԪ��ʧ��");
		} else {
			try {
				WebDriverWait wait = new WebDriverWait(this.browser, this.TIME_OUT);
				By by = By.xpath(xpath);
				ExceptElement ee = new ExceptElement(by);
				we = wait.until(ee);
				this.log.info("ͨ��xpath:[" + xpath + "]��ȡҳ��Ԫ�سɹ�");
			} catch (TimeoutException nee) {
				this.log.error("ͨ��xpath:[" + xpath + des + "]��ȡҳ��Ԫ��ʧ��");
				String message = "ͨ��xpath��ȡ" + des;
				this.screenShot(message);
				throwException("ͨ��xpath:[" + xpath + des + "]��ȡҳ��Ԫ��ʧ��");
			}
			if (we == null) {
				this.log.error("ͨ��xpath:[" + xpath + des + "]��ȡҳ��Ԫ��ʧ��");
				String message = "ͨ��xpath��ȡ" + des;
				this.screenShot(message);
				throwException("ͨ��xpath:[" + xpath + des + "]��ȡҳ��Ԫ��ʧ��");
			}
		}
		// waitUntilElementVisible(we);
		return we;
	}

	/**
	 * ͨ��JavaScript��ȡԪ�� eg: String jscode= "var inp =
	 * document.getElementById('kw'); return inp;"; WebElement we =
	 * autoTest.getElementByJavaScript(jscode);
	 * 
	 * @param jscode
	 *            jsִ�д���
	 * @return Ԫ�ض���
	 */
	public WebElement getElementByJavaScript(String jscode) {
		WebElement jelement = null;
		jelement = (WebElement) this.executeJavaScript(jscode);
		return jelement;
	}

	/**
	 * ��ȡ��Ӧ��ǩ��Ԫ���б�
	 * 
	 * @param tag
	 *            Ԫ��tag��
	 * @return Ԫ�ض���List
	 */
	public List<WebElement> getElementsByTag(String tag) {
		return this.getBrowser().findElements(By.tagName(tag));
	}

	public WebElement getElementByAttr(String key, String attrname, String attrvalue) {
		List<WebElement> elements = this.getElements(key);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				screenShot(key + "Ԫ�ؼ��ϻ�ȡʧ��");
				this.log.error("Ԫ�ؼ��ϻ�ȡʧ�ܣ��޷���ȡ����");
			}
		}
		WebElement element = null;
		for (WebElement tempElement : elements) {
			// waitUntilElementVisible(element);
			String attributeValue = tempElement.getAttribute(attrname);
			if (attributeValue == null) {
				continue;
			}
			if (attributeValue.contains(attrvalue)) {
				element = tempElement;
				break;
			}
		}
		return element;
	}

	/**
	 * ��ȡ�ı����ݰ���text��Ԫ��
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param text
	 *            �ı�����
	 * @return Ԫ�ض���
	 */
	public WebElement getElementByText(String key, String text) {
		List<WebElement> elements = this.getElements(key);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				screenShot(key + "Ԫ�ؼ��ϻ�ȡʧ��");
				this.log.error("Ԫ�ؼ��ϻ�ȡʧ�ܣ��޷���ȡ����");
			}
		}
		WebElement element = null;
		for (WebElement tempElement : elements) {
			// waitUntilElementVisible(element);
			String value = tempElement.getText();
			if (value == null) {
				continue;
			}
			if (value.contains(text)) {
				element = tempElement;
				break;
			}
		}
		return element;
	}

	/**
	 * ��ȡ�ı����ݰ���text��Ԫ��
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param textList
	 *            �ı����ݼ���
	 * @return Ԫ�ض���
	 */
	public WebElement getElementByTexts(String key, List<String> textList) {
		List<WebElement> elements = this.getElements(key);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				screenShot(key + "Ԫ�ؼ��ϻ�ȡʧ��");
				this.log.error("Ԫ�ؼ��ϻ�ȡʧ�ܣ��޷���ȡ����");
			}
		}
		WebElement element = null;
		for (WebElement tempElement : elements) {
			// waitUntilElementVisible(element);
			String value = tempElement.getText();
			if (value == null) {
				continue;
			}
			if (containstrs(value, textList)) {
				element = tempElement;
			}
		}
		return element;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElement(String parentKey, String childKey) {
		WebElement parentElement = null;
		WebElement childElement = null;
		By by = null;
		try {
			for (ElementData trace : this.traceList) {
				parentElement = trace.getElement(parentKey);
				if (parentElement != null) {
					waitUntilElementVisible(parentElement);
					break;
				}
			}
			for (ElementData trace : this.traceList) {
				by = trace.getBy(childKey);
				if (by != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + parentKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (parentElement != null && by != null) {
			childElement = parentElement.findElement(by);
		} else if (parentElement == null) {
			String message = "��ȡԪ��[" + parentKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} else if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		// try {
		// this.waitUntilElementVisible(childElement);
		// } catch (ElementNotVisibleException ene) {
		// this.log.error(Messages.getString("AutoTest.getElment") + childKey +
		// Messages.getString("AutoTest.fail"));
		// String message = "��ȡԪ��[" + childKey + "]ʧ��";
		// this.screenShot(message);
		// throwException(message);
		// }
		if (childElement == null) {
			String message = "��ȡԪ��[" + childKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return childElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�����childKey��Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return ����Ԫ�ض���
	 */
	public List<WebElement> getSubElements(String parentKey, String childKey) {
		WebElement parentElement = null;
		List<WebElement> childElements = null;
		By by = null;
		try {
			for (ElementData trace : this.traceList) {
				parentElement = trace.getElement(parentKey);
				if (parentElement != null) {
					waitUntilElementVisible(parentElement);
					break;
				}
			}
			for (ElementData trace : this.traceList) {
				by = trace.getBy(childKey);
				if (by != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + parentKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (parentElement != null && by != null) {
			childElements = parentElement.findElements(by);
			if (childElements == null || childElements.size() == 0) {
				sleep(TIME_OUT);
				childElements = parentElement.findElements(by);
				if (childElements == null || childElements.size() == 0) {
					this.screenShot(childKey + "Ԫ��δ����");
					this.log.info(childKey + "Ԫ��δ����");
				}
			}
		} else if (parentElement == null) {
			String message = "��ȡԪ��[" + parentKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} else if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return childElements;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param element
	 *            ��Ԫ��
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElement(WebElement element, String childKey) {
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		waitUntilElementVisible(element);
		WebElement subElement = element.findElement(by);
		// try {
		// this.waitUntilElementVisible(subElement);
		// } catch (ElementNotVisibleException ene) {
		// this.log.error(Messages.getString("AutoTest.getElment") + childKey +
		// Messages.getString("AutoTest.fail"));
		// String message = "��ȡԪ��[" + childKey + "]ʧ��";
		// this.screenShot(message);
		// throwException(message);
		// }
		if (subElement == null) {
			sleep(TIME_OUT);
			subElement = element.findElement(by);
			if (subElement == null) {
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
		return subElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param element
	 *            ��Ԫ��
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementByAttr(WebElement element, String childKey, String attrname, String attrvalue) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		waitUntilElementVisible(element);
		List<WebElement> subElements = element.findElements(by);
		if (subElements == null || subElements.size() == 0) {
			sleep(TIME_OUT);
			subElements = element.findElements(by);
			if (subElements == null || subElements.size() == 0) {
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		for (WebElement subElement : subElements) {
			// this.waitUntilElementVisible(subElement);
			String actualValue = subElement.getAttribute(attrname);
			if (actualValue.contains(attrvalue)) {
				try {
					returnElement = subElement;
					break;
				} catch (ElementNotVisibleException ene) {
					this.log.error(
							Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
					String message = "��ȡԪ��[" + childKey + "]ʧ��";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param element
	 *            ��Ԫ��
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementByText(WebElement element, String childKey, String text) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		waitUntilElementVisible(element);
		List<WebElement> subElements = element.findElements(by);
		if (subElements == null || subElements.size() == 0) {
			sleep(TIME_OUT);
			subElements = element.findElements(by);
			if (subElements == null || subElements.size() == 0) {
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		for (WebElement subElement : subElements) {
			// this.waitUntilElementVisible(subElement);
			String actualValue = subElement.getText();
			if (actualValue.contains(text)) {
				try {
					returnElement = subElement;
					break;
				} catch (ElementNotVisibleException ene) {
					this.log.error(
							Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
					String message = "��ȡԪ��[" + childKey + "]ʧ��";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param parentkey
	 *            �ļ��и�Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param text
	 *            ��Ԫ�ص��ı�����
	 * 
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementByText(String parentkey, String childKey, String text) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		WebElement parentElement = getElement(parentkey);
		if (parentElement == null) {
			sleep(TIME_OUT);
			parentElement = getElement(parentkey);
			if (parentElement == null) {
				String message = "��ȡԪ��[" + parentkey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		waitUntilElementVisible(parentElement);
		List<WebElement> subElements = parentElement.findElements(by);
		if (subElements == null || subElements.size() == 0) {
			sleep(TIME_OUT);
			subElements = parentElement.findElements(by);
			if (subElements == null || subElements.size() == 0) {
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		for (WebElement subElement : subElements) {
			// this.waitUntilElementVisible(subElement);
			String actualValue = subElement.getText();
			if (actualValue.contains(text)) {
				try {
					returnElement = subElement;
					break;
				} catch (ElementNotVisibleException ene) {
					this.log.error(
							Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
					String message = "��ȡԪ��[" + childKey + "]ʧ��";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param parentkey
	 *            �ļ��и�Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param texts
	 *            ��Ԫ�ص��ı����ݼ���
	 * 
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementByTexts(String parentkey, String childKey, List<String> texts) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		WebElement parentElement = getElement(parentkey);
		if (parentElement == null) {
			sleep(TIME_OUT);
			parentElement = getElement(parentkey);
			if (parentElement == null) {
				String message = "��ȡԪ��[" + parentkey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		waitUntilElementVisible(parentElement);
		List<WebElement> subElements = parentElement.findElements(by);
		if (subElements == null || subElements.size() == 0) {
			sleep(TIME_OUT);
			subElements = parentElement.findElements(by);
			if (subElements == null || subElements.size() == 0) {
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		for (WebElement subElement : subElements) {
			// this.waitUntilElementVisible(subElement);
			String actualValue = subElement.getText();
			if (containstrs(actualValue, texts)) {
				try {
					returnElement = subElement;
					break;
				} catch (ElementNotVisibleException ene) {
					this.log.error(
							Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
					String message = "��ȡԪ��[" + childKey + "]ʧ��";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param parentkey
	 *            �ļ��и�Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param text
	 *            ��Ԫ�ص��ı�����
	 * 
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementByParentText(String parentkey, String childKey, String text) {
		WebElement subElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		List<WebElement> parentElements = getElements(parentkey);
		if (parentElements == null || parentElements.size() == 0) {
			sleep(TIME_OUT);
			parentElements = getElements(parentkey);
			if (parentElements == null || parentElements.size() == 0) {
				String message = "��ȡԪ��[" + parentkey + "]ʧ��";
				this.log.error(message);
				this.screenShot(message);
			}
		}
		for (WebElement parentElement : parentElements) {
			if (parentElement.getText().contains(text)) {
				subElement = parentElement.findElement(by);
				waitUntilElementVisible(subElement);
				break;
			}
		}
		if (subElement == null) {
			String message = "��ȡԪ��[" + childKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return subElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µĵڼ���childKey��Ԫ��
	 * 
	 * @param parentkey
	 *            �ļ��и�Ԫ�ص�keyֵ
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param No
	 *            ��Ԫ������,0��ʼ
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementAsNo(String parentkey, String childKey, String No) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		WebElement parentElement = getElement(parentkey);
		waitUntilElementVisible(parentElement);
		List<WebElement> subElements = parentElement.findElements(by);
		if (subElements == null || subElements.size() == 0) {
			subElements = parentElement.findElements(by);
			if (subElements == null || subElements.size() == 0) {
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		if (Integer.parseInt(No) >= subElements.size()) {
			this.screenShot(No + "�����ѷ���Ԫ������");
			this.log.error(No + "�����ѷ���Ԫ������");
		}
		returnElement = subElements.get(Integer.parseInt(No));
		try {
			// this.waitUntilElementVisible(returnElement);
			return returnElement;
		} catch (ElementNotVisibleException ene) {
			this.log.error(Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
			String message = "��ȡԪ��[" + childKey + "]ʧ��";
			this.screenShot(message);
			throwException(message);
		}
		return returnElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µĵڼ���childKey��Ԫ��
	 * 
	 * @param element
	 *            ��webelement
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param No
	 *            ��Ԫ��������0��ʼ
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementAsNo(WebElement element, String childKey, String No) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		waitUntilElementVisible(element);
		List<WebElement> subElements = element.findElements(by);
		if (subElements == null || subElements.size() == 0) {
			subElements = element.findElements(by);
			if (subElements == null || subElements.size() == 0) {
				String message = "��ȡԪ��[" + childKey + "]ʧ��";
				this.log.error(message);
				this.screenShot(message);
			}
		}
		if (Integer.parseInt(No) >= subElements.size()) {
			this.screenShot(No + "�����ѷ���Ԫ������");
			this.log.error(No + "�����ѷ���Ԫ������");
		}
		returnElement = subElements.get(Integer.parseInt(No));
		try {
			// this.waitUntilElementVisible(returnElement);
			return returnElement;
		} catch (ElementNotVisibleException ene) {
			this.log.error(Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
			String message = "��ȡԪ��[" + childKey + "]ʧ��";
			this.screenShot(message);
			throwException(message);
		}
		return returnElement;
	}

	/**
	 * ��ȡָ��parentKeyԪ���µ�childKey��Ԫ��
	 * 
	 * @param parentKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param replaceKey
	 *            ��Ԫ�ض�λֵ����Ҫ���滻��ֵ
	 * @param value
	 *            ��Ԫ�ض�λֵ���滻ֵ
	 * @param childKey
	 *            �ļ���Ԫ�ص�keyֵ
	 * @return Ԫ�ض���
	 */
	public WebElement getSubElementOfChangeParentKey(String parentKey, String replaceKey, String value,
			String childKey) {
		WebElement parentElement = null;
		WebElement childElement = null;
		By by = null;
		try {
			for (ElementData trace : this.traceList) {
				parentElement = trace.getElement(parentKey, replaceKey, value);
				if (parentElement != null) {
					waitUntilElementVisible(parentElement);
					break;
				}
			}
			for (ElementData trace : this.traceList) {
				by = trace.getBy(childKey);
				if (by != null) {
					break;
				}
			}
		} catch (TimeoutException e) {
			String message = "��ȡԪ��[" + parentKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (parentElement != null && by != null) {
			childElement = parentElement.findElement(by);
		} else if (parentElement == null) {
			String message = "��ȡԪ��[" + parentKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} else if (by == null) {
			String message = "��ȡ" + childKey + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (childElement == null) {
			String message = "��ȡԪ��[" + childKey + "]ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		try {
			// this.waitUntilElementVisible(childElement);
		} catch (ElementNotVisibleException ene) {
			this.log.error(Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
			String message = "��ȡԪ��[" + childKey + "]ʧ��";
			this.screenShot(message);
			throwException(message);
		}
		return childElement;
	}

	/**
	 * ��ȡ����keyԪ�ص��ı�����
	 * 
	 * @param key
	 *            �ļ���Ԫ��ָ��keyֵ
	 * @return �����ı�����
	 */
	public String getText(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "Ԫ��δ����");
				log.error(key + "Ԫ��δ����");
				throwException(key + "Ԫ��δ����");
			}
		}
		waitUntilElementVisible(element);
		return element.getText();
	}

	/**
	 * ��ȡָ��Ԫ�ص��ı�����
	 * 
	 * @param element
	 *            ָ��Ԫ��
	 * @return �ı�����
	 */
	public String getText(WebElement element) {
		waitUntilElementVisible(element);
		if (element == null) {
			throwException("��ȡ����ʧ��");
			return null;
		}
		return element.getText();
	}

	/**
	 * ��ȡָ��key��By����
	 * 
	 * @param key
	 *            �ļ���Ԫ��ָ��keyֵ
	 * @return ���ظ�keyֵ�Ķ�ӦBy����
	 */
	public By getBy(String key) {
		By by = null;
		for (ElementData trace : this.traceList) {
			by = trace.getBy(key);
			if (by != null) {
				break;
			}
		}
		if (by == null) {
			String message = "��ȡ" + key + "��By����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return by;
	}

	/**
	 * �ж�ָ���Ķ����Ƿ����
	 * 
	 * @param key
	 *            �ļ���λԪ�ص�keyֵ
	 * @return true / false
	 */
	public boolean elementExists(String key) {
		boolean exist = true;
		try {
			exist = this.getElement(key) != null ? true : false;
		} catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * �ж�ָ���Ķ����Ƿ����
	 * 
	 * @param key
	 *            �ļ���λԪ�ص�keyֵ
	 * @param attrname
	 *            ������
	 * @param attrvalue
	 *            ����ֵ
	 * @return true / false
	 */
	public boolean elementExistsByAttr(String key, String attrname, String attrvalue) {
		boolean exist = true;
		try {
			exist = this.getElementByAttr(key, attrname, attrvalue) != null ? true : false;
		} catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * �ж�ָ���Ķ����Ƿ����
	 * 
	 * @param by
	 *            By����
	 * @return true / false
	 */
	public boolean isExists(By by) {
		boolean exist = true;
		try {
			WebElement element;
			element = this.browser.findElement(by);
			exist = element != null ? true : false;
			if (element != null) {
				exist = element.isDisplayed() ? true : false;
			}
		} catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * �ж�ָ���Ķ����Ƿ���
	 * 
	 * @param by
	 *            By����
	 * @return true / false
	 */
	public boolean isExistsMore(By by) {
		boolean exist = true;
		try {
			List<WebElement> elementList = this.browser.findElements(by);
			if (!(elementList.size() > 1)) {
				exist = false;
			}
		} catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * �ж�ָ���Ķ����Ƿ����
	 * 
	 * @param xpath
	 *            Ԫ�ص�xpath��λ�ַ���
	 * @return true / false
	 */
	public boolean isElementExistsByXpath(String xpath) {
		boolean exist = true;
		try {
			exist = this.getElementByXpath(xpath, "�ж�Ԫ�ش���") != null ? true : false;
		} catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * Ԫ���Ƿ����
	 * 
	 * @param element
	 *            Ԫ�ض���
	 * @return true / false
	 */
	public boolean isElementExists(WebElement element) {
		boolean exist = true;
		if (element == null) {
			exist = false;
		}
		return exist;
	}

	/**
	 * ָ���ļ��е�����ΪkeyֵԪ�أ���ȡ��Ԫ��propertyNam������ֵ
	 * 
	 * @param key
	 *            �ļ���Ԫ�ص�keyֵ
	 * @param propertyName
	 *            ��ȡCSS���Զ���
	 * @return ����CSS����ֵ
	 */
	public String getElementCSSValue(String key, String propertyName) {
		String value = null;
		WebElement element = this.getElement(key);
		value = element.getCssValue(propertyName);
		return value;
	}

	/**
	 * ��ָ��ʱ����ѭ���ȴ���ֱ������ɼ�����ʱ֮��ֱ���׳����󲻿ɼ��쳣��Ϣ
	 * 
	 * @param element
	 *            Ԫ�ض���
	 * @param timeout
	 *            ��ʱʱ��
	 */
	protected void waitUntilElementVisible(WebElement element, int timeout) {
		WebDriverWait wait = new WebDriverWait(browser, TIME_OUT);
		wait.until(ExpectedConditions.visibilityOf(element));
		if (!element.isDisplayed()) {
			String info = "��ǰԪ����" + timeout + "���ﲻ��ʶ���ı�";
			this.log.info(info);
		}
	}

	/**
	 * ��ָ��ʱ����ѭ���ȴ���ֱ������ɼ���ʹ���û�ָ����Ĭ�ϳ�ʱ���á�
	 * 
	 * @param element
	 *            Ԫ�ض���
	 */
	protected void waitUntilElementVisible(WebElement element) {
		waitUntilElementVisible(element, this.TIME_OUT);
	}

	/**
	 * ��Ԫ��ֻ������ȡ��
	 * 
	 * @param key
	 *            �ļ��ж����keyֵ
	 */
	public void writenableElement(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "Ԫ��δ����");
				log.error(key + "Ԫ��δ����");
			}
		}
		String arg = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * ��Ԫ��ֻ������ȡ��
	 * 
	 * @param element
	 *            ����Ԫ�ض���
	 */
	public void writenableElement(WebElement element) {
		String arg = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * ��ȡ��ǰԪ�ص�������Ϣ
	 * 
	 * @param key
	 *            �ļ��ж����keyֵ
	 * @return �ļ��ж���Ϊkey��desֵ
	 */
	@SuppressWarnings("rawtypes")
	public String getElementDes(String key) {
		HashMap currentElement = null;
		String des = null;
		for (ElementData trace : this.traceList) {
			HashMap hm = trace.getDataHash();
			currentElement = (HashMap) hm.get(key);
			if (currentElement != null) {
				break;
			}
		}
		if (currentElement != null) {
			des = (String) currentElement.get("desc");
		}
		return des;
	}

	/**
	 * ͨ���ļ���ȡԪ������ֵ
	 * 
	 * @param element
	 *            ҳ��Ԫ��
	 * @param attributeName
	 *            ��Ҫ��ȡ������.
	 * @return ��ȡ������ֵ
	 */
	public String getAttribute(WebElement element, String attributeName) {
		if (element == null) {
			throwException("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
			return null;
		}
		String value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * ͨ���ļ���ȡԪ������ֵ
	 * 
	 * @param key
	 *            �ļ��л�ȡԪ�ص�key
	 * @param attributeName
	 *            ��Ҫ��ȡ������.
	 * @return ��ȡ������ֵ
	 */
	public String getAttribute(String key, String attributeName) {
		WebElement element = this.getElement(key);
		if (element == null) {
			throwException("Ԫ�ػ�ȡʧ�ܣ��޷���ȡ����");
			return null;
		}
		String value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * ͨ���ļ���ȡԪ������ֵ
	 * 
	 * @param key
	 *            �ļ��л�ȡԪ�ص�key
	 * @param attributeName
	 *            ��Ҫ��ȡ������.
	 * @return ��ȡ������ֵ��ArrayList
	 */
	@SuppressWarnings("null")
	public ArrayList<String> getAttributeValue(String key, String attributeName) {
		ArrayList<String> valueList = null;
		List<WebElement> elements = this.getElements(key);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				screenShot(key + "Ԫ�ؼ��ϻ�ȡʧ��");
				this.log.error("Ԫ�ؼ��ϻ�ȡʧ�ܣ��޷���ȡ����");
			}
		}
		for (WebElement element : elements) {
			String value = element.getAttribute(attributeName);
			if (value == null) {
				this.log.error(key + "ָ����Ԫ��û��" + attributeName + "ֵ");
				continue;
			}
			valueList.add(value);
		}
		if (valueList == null) {
			this.log.error(key + "ָ����Ԫ��û��" + attributeName + "ֵ");
			this.throwException(key + "ָ����Ԫ��û��" + attributeName + "ֵ");
		}
		return valueList;
	}

	/**
	 * ͨ��JS��ȡԪ������
	 * 
	 * @param js
	 *            ָ��Ԫ�ص�js
	 * @param attributeName
	 *            ��ȡԪ�ص�����
	 * @return ��ȡ������ֵ
	 */
	public String getAttributeByJS(String js, String attributeName) {
		String value = null;
		WebElement element = this.getElementByJavaScript(js);
		if (element == null) {
			throwException("Ԫ�ػ�ȡʧ��,�޷���ȡ����");
			return null;
		}
		value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * ͨ��xpath��ȡԪ������
	 * 
	 * @param xpath
	 *            ָ��Ԫ�ص�xpath
	 * @param attributeName
	 *            ��ȡԪ�ص�����
	 * @return ��ȡ������ֵ
	 */
	public String getAttributeByXpath(String xpath, String attributeName) {
		String value = null;
		WebElement element = this.getElementByXpath(xpath, "ͨ��xpath��ȡԪ��");
		if (element == null) {
			throwException("Ԫ�ػ�ȡʧ��");
			return null;
		}
		value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * ��ǰҳ���Ƿ����ı�
	 * 
	 * @param content
	 *            �ı�����
	 * @return true / false
	 */
	public boolean isTextPresent(String content) {
		boolean status = false;
		try {
			status = this.browser.getPageSource().contains(content);
		} catch (Exception e) {
			status = false;
		}
		return status;
	}

	/**
	 * ��ǰҳ�����Ƿ�����ı�����
	 * 
	 * @param pattern
	 * @return true / false
	 */
	public boolean isTextPresent2(String pattern) {
		String Xpath = "//*[contains(text(),\'" + pattern + "\')]";
		try {
			WebElement element = this.browser.findElement(By.xpath(Xpath));
			return element == null ? false : true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * �ж��Ƿ���link
	 * 
	 * @param linkText
	 *            Link��textֵ
	 * @return true / false
	 */
	public boolean isLink(String linkText) {
		List<WebElement> linkList = this.browser.findElements(By.tagName("a"));
		boolean status = false;
		for (WebElement webElement : linkList) {
			if (webElement.getText().equalsIgnoreCase(linkText)) {
				status = true;
				break;
			}
		}
		return status;
	}

	/**
	 * �ж��Ƿ�����disable����
	 * 
	 * @param key
	 *            Ԫ�ض�λԪ��
	 * @return true / false
	 */
	public boolean isEnable(String key) {
		WebElement element = getElement(key);
		return element.isEnabled();
	}

	/**
	 * �жϸ�Ԫ�����Ƿ���img
	 * 
	 * @param webElement
	 *            ��Ԫ�ض���
	 * @return true / false
	 */
	public boolean isImg(WebElement webElement) {
		List<WebElement> linkList = webElement.findElements(By.tagName("img"));
		if (linkList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	private boolean containstrs(String str, List<String> strList) {
		boolean flag = false;
		for (String s : strList) {
			if (str.contains(s)) {
				flag = true;
			} else {
				flag = false;
				break;
			}
		}
		return flag;
	}
}
