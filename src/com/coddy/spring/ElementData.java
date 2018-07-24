package com.coddy.spring;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import org.dom4j.DocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.coddy.condition.ExceptElement;
import com.coddy.condition.ExceptElements;
import com.coddy.parser.DbReader;
import com.coddy.parser.XmlReader;
import com.coddy.parser.YamlReader;
import com.coddy.utils.ConfigProperties;

/**
 * ElementData ҳ��Ԫ���࣬���ڻ�ȡҳ��Ԫ��
 * 
 * @author shenbing
 * 
 */
public class ElementData {
	/**
	 * �����ļ���Ĭ�ϳ�ʱ
	 */
	public final static int TIME_OUT = Integer
			.parseInt(ConfigProperties.getInstance().getString("element.find.timeout"));
	/**
	 * �ļ�������HashMap
	 */
	@SuppressWarnings("rawtypes")
	private HashMap dataMap;
	/**
	 * �������������
	 */
	private WebDriver browser;

	/**
	 * Yaml�ļ��Ĺ��췽��
	 * 
	 * @param f
	 *            Yaml�ļ����ļ�����
	 * @param browser
	 *            �������������
	 * @throws UnsupportedEncodingException
	 *             ��������쳣
	 * @throws FileNotFoundException
	 *             �ļ��������쳣
	 * @throws IOException
	 *             �ļ������쳣
	 * @throws DocumentException
	 *             xml�ĵ��쳣
	 */
	public ElementData(File f, WebDriver browser, FileType fileType)
			throws UnsupportedEncodingException, FileNotFoundException, IOException, DocumentException {
		if (fileType == FileType.YALM) {
			this.browser = browser;
			YamlReader yr = new YamlReader(f);
			this.dataMap = yr.getDataMap();
		} else if (fileType == FileType.XML) {
			this.browser = browser;
			XmlReader xr = new XmlReader(f);
			this.dataMap = xr.getDataMap();
		}
	}

	/**
	 * ���ݱ�Ĺ��췽��
	 * 
	 * @param tableName
	 *            ���ݱ���
	 * @param browser
	 *            �������������
	 * @throws Exception
	 *             �쳣����
	 */
	public ElementData(String tableName, WebDriver browser) throws Exception {
		this.browser = browser;
		DbReader dr = new DbReader(tableName);
		this.dataMap = dr.getDataMap();
	}

	/**
	 * ��ȡyaml�����ݿ���ҳ��Ԫ��HashMap
	 * 
	 * @return yaml�����ݿ���ҳ��Ԫ��HashMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getDataHash() {
		return this.dataMap;
	}

	/**
	 * ��ȡWebElement����
	 * 
	 * @param key
	 *            ��λԪ�ص�keyֵ
	 * @return Ԫ�ض���
	 * @throws TimeoutException
	 *             ��ʱ�쳣
	 */
	@SuppressWarnings("rawtypes")
	public WebElement getElement(String key) throws TimeoutException {
		HashMap op = (HashMap) dataMap.get(key);
		WebElement element = null;
		String selecter = null;
		Object keyID = "id";
		Object keyLinkText = "linktext";
		Object keyClassName = "classname";
		Object keyXpath = "xpath";
		Object keyName = "name";
		Object keyCSS = "css";
		Object tagName = "tagname";
		if (op == null) {
			return null;
		}
		WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
		By by = null;
		if (op.containsKey(keyID)) {
			selecter = (String) op.get(keyID);
			by = By.id(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyLinkText)) {
			selecter = (String) op.get(keyLinkText);
			by = By.linkText(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyClassName)) {
			selecter = (String) op.get(keyClassName);
			by = By.className(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyXpath)) {
			selecter = (String) op.get(keyXpath);
			by = By.xpath(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyName)) {
			selecter = (String) op.get(keyName);
			by = By.name(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyCSS)) {
			selecter = (String) op.get(keyCSS);
			by = By.cssSelector(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(tagName)) {
			selecter = (String) op.get(tagName);
			by = By.tagName(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else {
			return null;
		}
		return element;
	}

	/**
	 * ��ȡWebElement����
	 * 
	 * @param key
	 *            ��λԪ�ص�keyֵ
	 * @param timeout
	 *            ���ó�ʱʱ��
	 * @return WebElement����
	 * @throws TimeoutException
	 *             ��ʱ�쳣
	 */
	@SuppressWarnings("rawtypes")
	public WebElement getElement(String key, int timeout) throws TimeoutException {
		HashMap op = (HashMap) dataMap.get(key);
		WebElement element = null;
		String selecter = null;
		Object keyID = "id";
		Object keyLinkText = "linktext";
		Object keyClassName = "classname";
		Object keyXpath = "xpath";
		Object keyName = "name";
		Object keyCSS = "css";
		Object tagName = "tagname";
		if (op == null) {
			return null;
		}
		WebDriverWait wait = new WebDriverWait(this.browser, timeout);
		By by = null;
		if (op.containsKey(keyID)) {
			selecter = (String) op.get(keyID);
			by = By.id(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyLinkText)) {
			selecter = (String) op.get(keyLinkText);
			by = By.linkText(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyClassName)) {
			selecter = (String) op.get(keyClassName);
			by = By.className(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyXpath)) {
			selecter = (String) op.get(keyXpath);
			by = By.xpath(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyName)) {
			selecter = (String) op.get(keyName);
			by = By.name(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyCSS)) {
			selecter = (String) op.get(keyCSS);
			by = By.cssSelector(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(tagName)) {
			selecter = (String) op.get(tagName);
			by = By.tagName(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else {
			return null;
		}
		return element;
	}

	/**
	 * ��ȡWebElement����
	 * 
	 * @param key
	 *            ��λԪ�ص�keyֵ
	 * @param replaceKey
	 *            ���滻ֵ
	 * @param value
	 *            �滻ֵ
	 * @return WebElement����
	 * @throws TimeoutException
	 *             ��ʱ�쳣
	 */
	@SuppressWarnings("rawtypes")
	public WebElement getElement(String key, String replaceKey, String value) throws TimeoutException {
		HashMap op = (HashMap) dataMap.get(key);
		WebElement element = null;
		String selecter = null;
		Object keyID = "id";
		Object keyLinkText = "linktext";
		Object keyClassName = "classname";
		Object keyXpath = "xpath";
		Object keyName = "name";
		Object keyCSS = "css";
		Object tagName = "tagname";
		if (op == null) {
			return null;
		}
		WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
		By by = null;
		if (op.containsKey(keyID)) {
			selecter = (String) op.get(keyID);
			selecter = selecter.replace(replaceKey, value);
			by = By.id(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyLinkText)) {
			selecter = (String) op.get(keyLinkText);
			selecter = selecter.replace(replaceKey, value);
			by = By.linkText(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyClassName)) {
			selecter = (String) op.get(keyClassName);
			selecter = selecter.replace(replaceKey, value);
			by = By.className(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyXpath)) {
			selecter = (String) op.get(keyXpath);
			selecter = selecter.replace(replaceKey, value);
			by = By.xpath(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyName)) {
			selecter = (String) op.get(keyName);
			selecter = selecter.replace(replaceKey, value);
			by = By.name(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(keyCSS)) {
			selecter = (String) op.get(keyCSS);
			selecter = selecter.replace(replaceKey, value);
			by = By.cssSelector(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else if (op.containsKey(tagName)) {
			selecter = (String) op.get(tagName);
			selecter = selecter.replace(replaceKey, value);
			by = By.tagName(selecter);
			ExceptElement ee = new ExceptElement(by);
			element = wait.until(ee);
		} else {
			return null;
		}
		return element;
	}

	/**
	 * ��ȡWebElement����
	 * 
	 * @param key
	 *            ��λԪ�ص�keyֵ
	 * @return Ԫ�ض���
	 * @throws TimeoutException
	 *             ��ʱ�쳣
	 */
	@SuppressWarnings("rawtypes")
	public List<WebElement> getElements(String key) throws TimeoutException {
		HashMap op = (HashMap) dataMap.get(key);
		List<WebElement> elements = null;
		String selecter = null;
		Object keyID = "id";
		Object keyLinkText = "linktext";
		Object keyClassName = "classname";
		Object keyXpath = "xpath";
		Object keyName = "name";
		Object keyCSS = "css";
		Object tagName = "tagname";
		if (op == null) {
			return null;
		}
		WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
		By by = null;
		if (op.containsKey(keyID)) {
			selecter = (String) op.get(keyID);
			by = By.id(selecter);
			ExceptElements ee = new ExceptElements(by);
			elements = wait.until(ee);
		} else if (op.containsKey(keyLinkText)) {
			selecter = (String) op.get(keyLinkText);
			by = By.linkText(selecter);
			ExceptElements ee = new ExceptElements(by);
			elements = wait.until(ee);
		} else if (op.containsKey(keyClassName)) {
			selecter = (String) op.get(keyClassName);
			by = By.className(selecter);
			ExceptElements ee = new ExceptElements(by);
			elements = wait.until(ee);
		} else if (op.containsKey(keyXpath)) {
			selecter = (String) op.get(keyXpath);
			by = By.xpath(selecter);
			ExceptElements ee = new ExceptElements(by);
			elements = wait.until(ee);
		} else if (op.containsKey(keyName)) {
			selecter = (String) op.get(keyName);
			by = By.name(selecter);
			ExceptElements ee = new ExceptElements(by);
			elements = wait.until(ee);
		} else if (op.containsKey(keyCSS)) {
			selecter = (String) op.get(keyCSS);
			by = By.cssSelector(selecter);
			ExceptElements ee = new ExceptElements(by);
			elements = wait.until(ee);
		} else if (op.containsKey(tagName)) {
			selecter = (String) op.get(tagName);
			by = By.tagName(selecter);
			ExceptElements ee = new ExceptElements(by);
			elements = wait.until(ee);
		} else {
			return null;
		}
		// ��֤Ԫ�ؿɼ�(����Ҫ��������ʾ�������ȡ���ȴ�)
		// wait.until(ExpectedConditions.visibilityOfAllElements(elements));
		return elements;
	}

	/**
	 * ��ȡyaml�����ݿ���Frame�ı�ʾֵ����ֵ�����л�Frame
	 * 
	 * @param framekey
	 *            ��λframeԪ�ص�keyֵ
	 * @return �����ļ���ָ��id��name��indexֵ��Object
	 * @throws NumberFormatException
	 *             ����ת���쳣
	 */
	@SuppressWarnings("rawtypes")
	public Object getFrame(String framekey) throws NumberFormatException {
		HashMap hf = (HashMap) this.dataMap.get(framekey);
		Object keyID = "id";
		Object keyName = "name";
		Object keyIndex = "index";
		if (hf == null) {
			return null;
		}
		if (hf.containsKey(keyID)) {
			return hf.get(keyID);
		} else if (hf.containsKey(keyName)) {
			return hf.get(keyName);

		} else if (hf.containsKey(keyIndex)) {
			String value = String.valueOf(hf.get(keyIndex));
			Integer iv = -1;
			iv = Integer.parseInt(value);
			return iv;
		}
		return null;
	}

	/**
	 * ��ȡBy����
	 * 
	 * @param key
	 *            ��λԪ�ص�keyֵ
	 * @return By����
	 */
	@SuppressWarnings("rawtypes")
	protected By getBy(String key) {
		HashMap op = (HashMap) dataMap.get(key);
		String selecter = null;
		Object keyID = "id";
		Object keyLinkText = "linktext";
		Object keyClassName = "classname";
		Object keyXpath = "xpath";
		Object keyName = "name";
		Object keyCSS = "css";
		Object tagName = "tagname";
		if (op == null) {
			return null;
		}
		By by = null;
		if (op.containsKey(keyID)) {
			selecter = (String) op.get(keyID);
			by = By.id(selecter);
		} else if (op.containsKey(keyLinkText)) {
			selecter = (String) op.get(keyLinkText);
			by = By.linkText(selecter);
		} else if (op.containsKey(keyClassName)) {
			selecter = (String) op.get(keyClassName);
			by = By.className(selecter);
		} else if (op.containsKey(keyXpath)) {
			selecter = (String) op.get(keyXpath);
			by = By.xpath(selecter);
		} else if (op.containsKey(keyName)) {
			selecter = (String) op.get(keyName);
			by = By.name(selecter);
		} else if (op.containsKey(keyCSS)) {
			selecter = (String) op.get(keyCSS);
			by = By.cssSelector(selecter);
		} else if (op.containsKey(tagName)) {
			selecter = (String) op.get(tagName);
			by = By.tagName(selecter);
		} else {
			return null;
		}
		return by;
	}
}
