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
 * ElementData 页面元素类，用于获取页面元素
 * 
 * @author shenbing
 * 
 */
public class ElementData {
	/**
	 * 配置文件中默认超时
	 */
	public final static int TIME_OUT = Integer
			.parseInt(ConfigProperties.getInstance().getString("element.find.timeout"));
	/**
	 * 文件中数据HashMap
	 */
	@SuppressWarnings("rawtypes")
	private HashMap dataMap;
	/**
	 * 驱动浏览器对象
	 */
	private WebDriver browser;

	/**
	 * Yaml文件的构造方法
	 * 
	 * @param f
	 *            Yaml文件的文件对象
	 * @param browser
	 *            驱动浏览器对象
	 * @throws UnsupportedEncodingException
	 *             编码错误异常
	 * @throws FileNotFoundException
	 *             文件不存在异常
	 * @throws IOException
	 *             文件操作异常
	 * @throws DocumentException
	 *             xml文档异常
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
	 * 数据表的构造方法
	 * 
	 * @param tableName
	 *            数据表名
	 * @param browser
	 *            驱动浏览器对象
	 * @throws Exception
	 *             异常对象
	 */
	public ElementData(String tableName, WebDriver browser) throws Exception {
		this.browser = browser;
		DbReader dr = new DbReader(tableName);
		this.dataMap = dr.getDataMap();
	}

	/**
	 * 获取yaml或数据库中页面元素HashMap
	 * 
	 * @return yaml或数据库中页面元素HashMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getDataHash() {
		return this.dataMap;
	}

	/**
	 * 获取WebElement对象
	 * 
	 * @param key
	 *            定位元素的key值
	 * @return 元素对象
	 * @throws TimeoutException
	 *             超时异常
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
	 * 获取WebElement对象
	 * 
	 * @param key
	 *            定位元素的key值
	 * @param timeout
	 *            设置超时时间
	 * @return WebElement对象
	 * @throws TimeoutException
	 *             超时异常
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
	 * 获取WebElement对象
	 * 
	 * @param key
	 *            定位元素的key值
	 * @param replaceKey
	 *            被替换值
	 * @param value
	 *            替换值
	 * @return WebElement对象
	 * @throws TimeoutException
	 *             超时异常
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
	 * 获取WebElement对象
	 * 
	 * @param key
	 *            定位元素的key值
	 * @return 元素对象
	 * @throws TimeoutException
	 *             超时异常
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
		// 保证元素可见(有需要触发才显示的情况，取消等待)
		// wait.until(ExpectedConditions.visibilityOfAllElements(elements));
		return elements;
	}

	/**
	 * 获取yaml或数据库中Frame的标示值，该值用于切换Frame
	 * 
	 * @param framekey
	 *            定位frame元素的key值
	 * @return 返回文件中指定id、name、index值的Object
	 * @throws NumberFormatException
	 *             类型转换异常
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
	 * 获取By对象
	 * 
	 * @param key
	 *            定位元素的key值
	 * @return By对象
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
