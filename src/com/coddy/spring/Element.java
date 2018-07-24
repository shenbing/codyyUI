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
 * Element 元素封装类，实现页面元素相关操作功能
 * 
 * @author shenbing
 * 
 */
public class Element extends Browser {
	/**
	 * ElementData对象List
	 */
	protected ArrayList<ElementData> traceList;
	/**
	 * 页面元素文件列表(yaml)
	 */
	protected ArrayList<File> fileList;
	/**
	 * 页面元素文件列表(DB table)
	 */
	protected ArrayList<String> tableList;

	/**
	 * 日志句柄
	 */
	protected Log log = LogUtils.getLog(Element.class);

	/**
	 * 
	 */
	protected String HIGHLIGHT_BORDER = ConfigProperties.getInstance().getString("AutoTest.hightLitht.border");

	/**
	 * 默认构造方法
	 */
	public Element() {
		super();
		this.fileList = new ArrayList<File>();
		this.tableList = new ArrayList<String>();
		this.traceList = new ArrayList<ElementData>();
	}

	/**
	 * 构造方法
	 * 
	 * @param bt
	 *            BrowserType枚举值
	 */
	public Element(BrowserType bt) {
		super(bt);
		this.fileList = new ArrayList<File>();
		this.tableList = new ArrayList<String>();
		this.traceList = new ArrayList<ElementData>();
	}

	/**
	 * 加载页面文件数据（yaml)
	 * 
	 * @param file
	 *            yaml的File对象
	 */
	protected void loadData(File file, FileType ft) {
		ElementData yamlData = null;
		try {
			yamlData = new ElementData(file, this.browser, ft);
		} catch (UnsupportedEncodingException e) {
			this.log.error("元素页面文件 " + file.getAbsolutePath() + "的编码方式不符合，请用GBK统一编码。");
			throwException("编码有误");
		} catch (FileNotFoundException e) {
			this.log.error("元素页面文件" + file.getAbsolutePath() + "不存在。");
			throwException("yaml文件不存在，加载失败");
		} catch (IOException e) {
			this.log.error("元素页面文件" + file.getAbsolutePath() + "打开或关闭异常");
			throwException("文件操作失败");
		} catch (DocumentException e) {
			this.log.error("元素页面文件" + file.getAbsolutePath() + "打开或关闭异常");
			throwException("文件操作失败");
		}
		if (yamlData.getDataHash() == null) {
			this.log.error(Messages.getString("AutoTest.loadYamlfile") + file.getAbsolutePath()
					+ Messages.getString("AutoTest.fail"));
			throwException("加载文件失败");
		} else {
			if (traceList.contains(yamlData)) {
				throwException("重复加载文件");
			} else {
				traceList.add(yamlData);
				this.log.info(Messages.getString("AutoTest.loadYamlfile") + file.getAbsolutePath()
						+ Messages.getString("AutoTest.finish"));
			}
		}
	}

	/**
	 * 加载数据表读取元素的方式
	 * 
	 * @param tableName
	 *            DB中table名
	 */
	protected void loadData(String tableName) {
		ElementData DbData = null;
		try {
			DbData = new ElementData(tableName, this.browser);

		} catch (Exception e) {
			this.log.error("元素页面数据库表" + tableName + "不存在。");
			throwException("数据库表不存在，加载失败" + e.getMessage());
		}
		if (DbData.getDataHash() == null) {
			this.log.error(
					Messages.getString("AutoTest.loadDbTable") + tableName + Messages.getString("AutoTest.fail"));
			throwException("加载数据库表失败");
		} else {
			if (traceList.contains(DbData)) {
				throwException("重复加载数据库表");
			} else {
				traceList.add(DbData);
				this.log.info(
						Messages.getString("AutoTest.loadDbTable") + tableName + Messages.getString("AutoTest.finish"));
			}
		}
	}

	/**
	 * 加载页面数据文件
	 * 
	 * @param pageFilePath
	 *            页面元素文件路径（yaml)
	 */
	public void loadPageData(String pageFilePath, FileType ft) {
		File f = new File(pageFilePath);
		if (!fileList.contains(f)) {
			this.fileList.add(f);
			this.loadData(f, ft);
		}
	}

	/**
	 * 加载页面数据库表
	 * 
	 * @param tableName
	 *            页面元素DB 表名
	 */
	public void loadPageDataDb(String tableName) {

		if (!tableList.contains(tableName)) {
			this.tableList.add(tableName);
			this.loadData(tableName);
		}
	}

	/**
	 * 设置元素高亮显示
	 * 
	 * @param element
	 *            页面元素对象
	 */
	public void highLight(WebElement element) {
		if (element == null) {
			return;
		}
		String arg = "arguments[0].style.border = \"" + HIGHLIGHT_BORDER + "\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * 获取指定key的元素
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @return 元素对象
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
			String message = "获取元素[" + key + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "获取元素[" + key + "]失败";
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
		// String message = "获取元素[" + key + "]失败";
		// this.screenShot(message);
		// throwException(message);
		// }
		return element;
	}

	/**
	 * 获取指定key的元素
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @return 元素对象
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
			String message = "获取元素[" + key + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return elements;
	}

	/**
	 * 获取指定key的元素
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @param number
	 *            key元素的索引，0开始
	 * @return 元素对象
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
			String message = "获取元素[" + key + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (elements.size() < number) {
			throwException(key + "定位的元素数量小于number指定数量");
		}
		return elements.get(number);
	}

	/**
	 * 获取元素数量
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @return 返回元素数量
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
			String message = "获取元素[" + key + "]失败";
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
	 * 获取指定key的元素
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @param timeout
	 *            指定超时时间
	 * @return 元素对象
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
			String message = "获取元素[" + key + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "获取元素[" + key + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return element;
	}

	/**
	 * 获取指定key值的元素,将需要修改的replaceKey换成value
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @param replaceKey
	 *            被替换值
	 * @param value
	 *            替换后值
	 * @return 元素对象
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
			String message = "获取元素[" + key + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "获取元素[" + key + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return element;
	}

	/**
	 * 获取指定By对象的元素
	 * 
	 * @param by
	 *            可以定位元素的By对象
	 * @return 元素对象
	 */
	public WebElement getElement(By by) {
		WebElement element = null;
		try {
			WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
			element = wait.until(new ExceptElement(by));
		} catch (TimeoutException e) {
			String message = "获取元素[" + by.toString().split(":")[1].trim().split(">")[0] + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (element == null) {
			String message = "获取元素[" + by.toString().split(":")[1].trim().split(">")[0] + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return element;
	}

	/**
	 * 获取指定By对象的多个元素
	 * 
	 * @param by
	 *            可以定位元素的By对象
	 * @return 元素对象
	 */
	public List<WebElement> getElements(By by) {
		List<WebElement> elements = null;
		try {
			WebDriverWait wait = new WebDriverWait(this.browser, TIME_OUT);
			elements = wait.until(new ExceptElements(by));
		} catch (TimeoutException e) {
			String message = "获取元素[" + by.toString().split(":")[1].trim().split(">")[0] + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		if (elements == null || elements.size() == 0) {
			String message = "获取元素[" + by.toString().split(":")[1].trim().split(">")[0] + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		return elements;
	}

	/**
	 * 根据提供xpath获取当前页面上的元素 主要用于提供参数化的元素
	 * 
	 * @param xpath
	 *            元素xpath值
	 * @param des
	 *            xpath描述值
	 * @return 元素对象
	 */
	public WebElement getElementByXpath(String xpath, String des) {
		WebElement we = null;
		if (xpath == null || xpath.length() == 0) {
			this.log.error("xpath为空。定位元素失败");
		} else {
			try {
				WebDriverWait wait = new WebDriverWait(this.browser, this.TIME_OUT);
				By by = By.xpath(xpath);
				ExceptElement ee = new ExceptElement(by);
				we = wait.until(ee);
				this.log.info("通过xpath:[" + xpath + "]获取页面元素成功");
			} catch (TimeoutException nee) {
				this.log.error("通过xpath:[" + xpath + des + "]获取页面元素失败");
				String message = "通过xpath获取" + des;
				this.screenShot(message);
				throwException("通过xpath:[" + xpath + des + "]获取页面元素失败");
			}
			if (we == null) {
				this.log.error("通过xpath:[" + xpath + des + "]获取页面元素失败");
				String message = "通过xpath获取" + des;
				this.screenShot(message);
				throwException("通过xpath:[" + xpath + des + "]获取页面元素失败");
			}
		}
		// waitUntilElementVisible(we);
		return we;
	}

	/**
	 * 通过JavaScript获取元素 eg: String jscode= "var inp =
	 * document.getElementById('kw'); return inp;"; WebElement we =
	 * autoTest.getElementByJavaScript(jscode);
	 * 
	 * @param jscode
	 *            js执行代码
	 * @return 元素对象
	 */
	public WebElement getElementByJavaScript(String jscode) {
		WebElement jelement = null;
		jelement = (WebElement) this.executeJavaScript(jscode);
		return jelement;
	}

	/**
	 * 获取对应标签的元素列表
	 * 
	 * @param tag
	 *            元素tag名
	 * @return 元素对象List
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
				screenShot(key + "元素集合获取失败");
				this.log.error("元素集合获取失败，无法获取数据");
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
	 * 获取文本内容包含text的元素
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @param text
	 *            文本内容
	 * @return 元素对象
	 */
	public WebElement getElementByText(String key, String text) {
		List<WebElement> elements = this.getElements(key);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				screenShot(key + "元素集合获取失败");
				this.log.error("元素集合获取失败，无法获取数据");
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
	 * 获取文本内容包含text的元素
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @param textList
	 *            文本内容集合
	 * @return 元素对象
	 */
	public WebElement getElementByTexts(String key, List<String> textList) {
		List<WebElement> elements = this.getElements(key);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				screenShot(key + "元素集合获取失败");
				this.log.error("元素集合获取失败，无法获取数据");
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
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param parentKey
	 *            文件中元素的key值
	 * @param childKey
	 *            文件中元素的key值
	 * @return 元素对象
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
			String message = "获取元素[" + parentKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (parentElement != null && by != null) {
			childElement = parentElement.findElement(by);
		} else if (parentElement == null) {
			String message = "获取元素[" + parentKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} else if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		// try {
		// this.waitUntilElementVisible(childElement);
		// } catch (ElementNotVisibleException ene) {
		// this.log.error(Messages.getString("AutoTest.getElment") + childKey +
		// Messages.getString("AutoTest.fail"));
		// String message = "获取元素[" + childKey + "]失败";
		// this.screenShot(message);
		// throwException(message);
		// }
		if (childElement == null) {
			String message = "获取元素[" + childKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return childElement;
	}

	/**
	 * 获取指定parentKey元素下的所有childKey子元素
	 * 
	 * @param parentKey
	 *            文件中元素的key值
	 * @param childKey
	 *            文件中元素的key值
	 * @return 所有元素对象
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
			String message = "获取元素[" + parentKey + "]失败";
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
					this.screenShot(childKey + "元素未发现");
					this.log.info(childKey + "元素未发现");
				}
			}
		} else if (parentElement == null) {
			String message = "获取元素[" + parentKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} else if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return childElements;
	}

	/**
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param element
	 *            父元素
	 * @param childKey
	 *            文件中元素的key值
	 * @return 元素对象
	 */
	public WebElement getSubElement(WebElement element, String childKey) {
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
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
		// String message = "获取元素[" + childKey + "]失败";
		// this.screenShot(message);
		// throwException(message);
		// }
		if (subElement == null) {
			sleep(TIME_OUT);
			subElement = element.findElement(by);
			if (subElement == null) {
				String message = "获取元素[" + childKey + "]失败";
				this.screenShot(message);
				this.log.error(message);
				throwException(message);
			}
		}
		return subElement;
	}

	/**
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param element
	 *            父元素
	 * @param childKey
	 *            文件中元素的key值
	 * @return 元素对象
	 */
	public WebElement getSubElementByAttr(WebElement element, String childKey, String attrname, String attrvalue) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
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
				String message = "获取元素[" + childKey + "]失败";
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
					String message = "获取元素[" + childKey + "]失败";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param element
	 *            父元素
	 * @param childKey
	 *            文件中元素的key值
	 * @return 元素对象
	 */
	public WebElement getSubElementByText(WebElement element, String childKey, String text) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
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
				String message = "获取元素[" + childKey + "]失败";
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
					String message = "获取元素[" + childKey + "]失败";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param parentkey
	 *            文件中父元素的key值
	 * @param childKey
	 *            文件中元素的key值
	 * @param text
	 *            子元素的文本内容
	 * 
	 * @return 元素对象
	 */
	public WebElement getSubElementByText(String parentkey, String childKey, String text) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		WebElement parentElement = getElement(parentkey);
		if (parentElement == null) {
			sleep(TIME_OUT);
			parentElement = getElement(parentkey);
			if (parentElement == null) {
				String message = "获取元素[" + parentkey + "]失败";
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
				String message = "获取元素[" + childKey + "]失败";
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
					String message = "获取元素[" + childKey + "]失败";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param parentkey
	 *            文件中父元素的key值
	 * @param childKey
	 *            文件中元素的key值
	 * @param texts
	 *            子元素的文本内容集合
	 * 
	 * @return 元素对象
	 */
	public WebElement getSubElementByTexts(String parentkey, String childKey, List<String> texts) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		WebElement parentElement = getElement(parentkey);
		if (parentElement == null) {
			sleep(TIME_OUT);
			parentElement = getElement(parentkey);
			if (parentElement == null) {
				String message = "获取元素[" + parentkey + "]失败";
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
				String message = "获取元素[" + childKey + "]失败";
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
					String message = "获取元素[" + childKey + "]失败";
					this.screenShot(message);
					throwException(message);
				}
			}
		}
		return returnElement;
	}

	/**
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param parentkey
	 *            文件中父元素的key值
	 * @param childKey
	 *            文件中元素的key值
	 * @param text
	 *            父元素的文本内容
	 * 
	 * @return 元素对象
	 */
	public WebElement getSubElementByParentText(String parentkey, String childKey, String text) {
		WebElement subElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		List<WebElement> parentElements = getElements(parentkey);
		if (parentElements == null || parentElements.size() == 0) {
			sleep(TIME_OUT);
			parentElements = getElements(parentkey);
			if (parentElements == null || parentElements.size() == 0) {
				String message = "获取元素[" + parentkey + "]失败";
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
			String message = "获取元素[" + childKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return subElement;
	}

	/**
	 * 获取指定parentKey元素下的第几个childKey子元素
	 * 
	 * @param parentkey
	 *            文件中父元素的key值
	 * @param childKey
	 *            文件中元素的key值
	 * @param No
	 *            子元素索引,0开始
	 * @return 元素对象
	 */
	public WebElement getSubElementAsNo(String parentkey, String childKey, String No) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
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
				String message = "获取元素[" + childKey + "]失败";
				this.screenShot(message);
				this.log.error(message);
			}
		}
		if (Integer.parseInt(No) >= subElements.size()) {
			this.screenShot(No + "大于已发现元素数量");
			this.log.error(No + "大于已发现元素数量");
		}
		returnElement = subElements.get(Integer.parseInt(No));
		try {
			// this.waitUntilElementVisible(returnElement);
			return returnElement;
		} catch (ElementNotVisibleException ene) {
			this.log.error(Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
			String message = "获取元素[" + childKey + "]失败";
			this.screenShot(message);
			throwException(message);
		}
		return returnElement;
	}

	/**
	 * 获取指定parentKey元素下的第几个childKey子元素
	 * 
	 * @param element
	 *            父webelement
	 * @param childKey
	 *            文件中元素的key值
	 * @param No
	 *            子元素索引，0开始
	 * @return 元素对象
	 */
	public WebElement getSubElementAsNo(WebElement element, String childKey, String No) {
		WebElement returnElement = null;
		By by = null;
		by = this.getBy(childKey);
		if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		waitUntilElementVisible(element);
		List<WebElement> subElements = element.findElements(by);
		if (subElements == null || subElements.size() == 0) {
			subElements = element.findElements(by);
			if (subElements == null || subElements.size() == 0) {
				String message = "获取元素[" + childKey + "]失败";
				this.log.error(message);
				this.screenShot(message);
			}
		}
		if (Integer.parseInt(No) >= subElements.size()) {
			this.screenShot(No + "大于已发现元素数量");
			this.log.error(No + "大于已发现元素数量");
		}
		returnElement = subElements.get(Integer.parseInt(No));
		try {
			// this.waitUntilElementVisible(returnElement);
			return returnElement;
		} catch (ElementNotVisibleException ene) {
			this.log.error(Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
			String message = "获取元素[" + childKey + "]失败";
			this.screenShot(message);
			throwException(message);
		}
		return returnElement;
	}

	/**
	 * 获取指定parentKey元素下的childKey子元素
	 * 
	 * @param parentKey
	 *            文件中元素的key值
	 * @param replaceKey
	 *            父元素定位值中需要被替换的值
	 * @param value
	 *            父元素定位值中替换值
	 * @param childKey
	 *            文件中元素的key值
	 * @return 元素对象
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
			String message = "获取元素[" + parentKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (parentElement != null && by != null) {
			childElement = parentElement.findElement(by);
		} else if (parentElement == null) {
			String message = "获取元素[" + parentKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		} else if (by == null) {
			String message = "获取" + childKey + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		if (childElement == null) {
			String message = "获取元素[" + childKey + "]失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		try {
			// this.waitUntilElementVisible(childElement);
		} catch (ElementNotVisibleException ene) {
			this.log.error(Messages.getString("AutoTest.getElment") + childKey + Messages.getString("AutoTest.fail"));
			String message = "获取元素[" + childKey + "]失败";
			this.screenShot(message);
			throwException(message);
		}
		return childElement;
	}

	/**
	 * 获取索引key元素的文本内容
	 * 
	 * @param key
	 *            文件中元素指定key值
	 * @return 返回文本内容
	 */
	public String getText(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "元素未发现");
				log.error(key + "元素未发现");
				throwException(key + "元素未发现");
			}
		}
		waitUntilElementVisible(element);
		return element.getText();
	}

	/**
	 * 获取指定元素的文本内容
	 * 
	 * @param element
	 *            指定元素
	 * @return 文本内容
	 */
	public String getText(WebElement element) {
		waitUntilElementVisible(element);
		if (element == null) {
			throwException("获取内容失败");
			return null;
		}
		return element.getText();
	}

	/**
	 * 获取指定key的By对象
	 * 
	 * @param key
	 *            文件中元素指定key值
	 * @return 返回该key值的对应By对象
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
			String message = "获取" + key + "的By对象失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
		}
		return by;
	}

	/**
	 * 判断指定的对象是否存在
	 * 
	 * @param key
	 *            文件定位元素的key值
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
	 * 判断指定的对象是否存在
	 * 
	 * @param key
	 *            文件定位元素的key值
	 * @param attrname
	 *            属性名
	 * @param attrvalue
	 *            属性值
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
	 * 判断指定的对象是否存在
	 * 
	 * @param by
	 *            By对象
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
	 * 判断指定的对象是否多个
	 * 
	 * @param by
	 *            By对象
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
	 * 判断指定的对象是否存在
	 * 
	 * @param xpath
	 *            元素的xpath定位字符串
	 * @return true / false
	 */
	public boolean isElementExistsByXpath(String xpath) {
		boolean exist = true;
		try {
			exist = this.getElementByXpath(xpath, "判断元素存在") != null ? true : false;
		} catch (Exception e) {
			exist = false;
		}
		return exist;
	}

	/**
	 * 元素是否存在
	 * 
	 * @param element
	 *            元素对象
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
	 * 指定文件中的索引为key值元素，获取该元素propertyNam的属性值
	 * 
	 * @param key
	 *            文件中元素的key值
	 * @param propertyName
	 *            获取CSS属性对象
	 * @return 属性CSS对象值
	 */
	public String getElementCSSValue(String key, String propertyName) {
		String value = null;
		WebElement element = this.getElement(key);
		value = element.getCssValue(propertyName);
		return value;
	}

	/**
	 * 在指定时间内循环等待，直到对象可见，超时之后直接抛出对象不可见异常信息
	 * 
	 * @param element
	 *            元素对象
	 * @param timeout
	 *            超时时间
	 */
	protected void waitUntilElementVisible(WebElement element, int timeout) {
		WebDriverWait wait = new WebDriverWait(browser, TIME_OUT);
		wait.until(ExpectedConditions.visibilityOf(element));
		if (!element.isDisplayed()) {
			String info = "当前元素在" + timeout + "秒里不能识别文本";
			this.log.info(info);
		}
	}

	/**
	 * 在指定时间内循环等待，直到对象可见，使用用户指定的默认超时设置。
	 * 
	 * @param element
	 *            元素对象
	 */
	protected void waitUntilElementVisible(WebElement element) {
		waitUntilElementVisible(element, this.TIME_OUT);
	}

	/**
	 * 将元素只读属性取消
	 * 
	 * @param key
	 *            文件中对象的key值
	 */
	public void writenableElement(String key) {
		WebElement element = this.getElement(key);
		if (element == null) {
			sleep(TIME_OUT);
			element = this.getElement(key);
			if (element == null) {
				screenShot(key + "元素未发现");
				log.error(key + "元素未发现");
			}
		}
		String arg = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * 将元素只读属性取消
	 * 
	 * @param element
	 *            操作元素对象
	 */
	public void writenableElement(WebElement element) {
		String arg = "arguments[0].readOnly = \"\"";
		this.executeJavaScript(arg, element);
	}

	/**
	 * 获取当前元素的描述信息
	 * 
	 * @param key
	 *            文件中对象的key值
	 * @return 文件中对象为key的des值
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
	 * 通过文件获取元素属性值
	 * 
	 * @param element
	 *            页面元素
	 * @param attributeName
	 *            所要获取的属性.
	 * @return 获取的属性值
	 */
	public String getAttribute(WebElement element, String attributeName) {
		if (element == null) {
			throwException("元素获取失败，无法获取数据");
			return null;
		}
		String value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * 通过文件获取元素属性值
	 * 
	 * @param key
	 *            文件中获取元素的key
	 * @param attributeName
	 *            所要获取的属性.
	 * @return 获取的属性值
	 */
	public String getAttribute(String key, String attributeName) {
		WebElement element = this.getElement(key);
		if (element == null) {
			throwException("元素获取失败，无法获取数据");
			return null;
		}
		String value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * 通过文件获取元素属性值
	 * 
	 * @param key
	 *            文件中获取元素的key
	 * @param attributeName
	 *            所要获取的属性.
	 * @return 获取的属性值的ArrayList
	 */
	@SuppressWarnings("null")
	public ArrayList<String> getAttributeValue(String key, String attributeName) {
		ArrayList<String> valueList = null;
		List<WebElement> elements = this.getElements(key);
		if (elements == null || elements.size() == 0) {
			sleep(TIME_OUT);
			elements = this.getElements(key);
			if (elements == null || elements.size() == 0) {
				screenShot(key + "元素集合获取失败");
				this.log.error("元素集合获取失败，无法获取数据");
			}
		}
		for (WebElement element : elements) {
			String value = element.getAttribute(attributeName);
			if (value == null) {
				this.log.error(key + "指定的元素没有" + attributeName + "值");
				continue;
			}
			valueList.add(value);
		}
		if (valueList == null) {
			this.log.error(key + "指定的元素没有" + attributeName + "值");
			this.throwException(key + "指定的元素没有" + attributeName + "值");
		}
		return valueList;
	}

	/**
	 * 通过JS获取元素属性
	 * 
	 * @param js
	 *            指定元素的js
	 * @param attributeName
	 *            获取元素的属性
	 * @return 获取的属性值
	 */
	public String getAttributeByJS(String js, String attributeName) {
		String value = null;
		WebElement element = this.getElementByJavaScript(js);
		if (element == null) {
			throwException("元素获取失败,无法获取数据");
			return null;
		}
		value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * 通过xpath获取元素属性
	 * 
	 * @param xpath
	 *            指定元素的xpath
	 * @param attributeName
	 *            获取元素的属性
	 * @return 获取的属性值
	 */
	public String getAttributeByXpath(String xpath, String attributeName) {
		String value = null;
		WebElement element = this.getElementByXpath(xpath, "通过xpath获取元素");
		if (element == null) {
			throwException("元素获取失败");
			return null;
		}
		value = element.getAttribute(attributeName);
		return value;
	}

	/**
	 * 当前页面是否含有文本
	 * 
	 * @param content
	 *            文本内容
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
	 * 当前页面中是否出现文本内容
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
	 * 判断是否是link
	 * 
	 * @param linkText
	 *            Link的text值
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
	 * 判断是否设置disable属性
	 * 
	 * @param key
	 *            元素定位元素
	 * @return true / false
	 */
	public boolean isEnable(String key) {
		WebElement element = getElement(key);
		return element.isEnabled();
	}

	/**
	 * 判断父元素下是否有img
	 * 
	 * @param webElement
	 *            父元素对象
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
