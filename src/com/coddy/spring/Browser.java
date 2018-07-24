package com.coddy.spring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.coddy.condition.ExceptWindow;
import com.coddy.utils.LogUtils;
import com.coddy.utils.Messages;
import com.coddy.utils.ConfigProperties;
import com.coddy.utils.ScreenCapture;
import com.coddy.utils.SystemUtils;

/**
 * Browser 浏览器类，实现浏览器相关功能
 * 
 * @author shenbing
 * 
 */
public class Browser {
	/**
	 * 默认超时时间
	 */
	public int TIME_OUT = Integer.parseInt(ConfigProperties.getInstance().getString("element.find.timeout"));

	/**
	 * 驱动浏览器
	 */
	protected WebDriver browser;

	/**
	 * 日志控制
	 */
	protected Log log = LogUtils.getLog(Browser.class.getName());

	/**
	 * 本次执行过程中窗口的ID记录列表
	 */
	public ArrayList<String> windowsList;

	/**
	 * 最原始窗口句柄
	 */
	protected String winHandle;
	// /**
	// * 当前浏览器
	// */
	// protected WebDriver currentBrowser;
	/**
	 * 浏览器类型枚举
	 */
	protected BrowserType browserType;

	/**
	 * 默认构造方法
	 */
	public Browser() {
		if (!"".equals(ConfigProperties.getInstance().getString("browserType"))
				|| null != ConfigProperties.getInstance().getString("browserType")) {
			switch (ConfigProperties.getInstance().getString("browserType")) {
			case "IE":
				this.browserType = BrowserType.IE;
				break;
			case "CHROME":
				this.browserType = BrowserType.CHROME;
				break;
			case "FIREFOX":
				this.browserType = BrowserType.FIREFOX;
				break;
			default:
				this.browserType = BrowserType.IE;
				break;
			}
		}
		this.windowsList = new ArrayList<String>();
		initBrowser();
	}

	/**
	 * 构造方法
	 * 
	 * @param bt
	 *            BrowserType枚举值
	 */
	public Browser(BrowserType bt) {
		this.browserType = bt;
		this.windowsList = new ArrayList<String>();
		initBrowser();
	}

	/**
	 * 初始化浏览器
	 */
	public void initBrowser() {
		this.log.info(" **********浏览器启动!开始执行!**********");
		this.log.info("当前操作系统：" + SystemUtils.getSystemInfo("os.name"));
		BrowserType bt = this.browserType;
		switch (bt) {
		case IE:
			this.browser = BrowserInit.getIe();
			this.log.info(Messages.getString("AutoTest.ieStart"));
			break;
		case FIREFOX:
			this.browser = BrowserInit.getFirefox();
			this.log.info(Messages.getString("AutoTest.ffstart"));
			break;
		case CHROME:
			this.browser = BrowserInit.getChrome();
			this.log.info(Messages.getString("AutoTest.ChromeStart"));
			break;
		default:
			this.log.error(Messages.getString("AutoTest.browserModel") + this.browserType
					+ Messages.getString("AutoTest.errorBroweser"));
			this.log.error(Messages.getString("AutoTest.run") + Messages.getString("AutoTest.fail"));
			System.exit(0);
			break;
		}
		this.log.info(this.getBrowserInfo());
		// this.currentBrowser = browser;
		this.browser.manage().window().maximize();
	}

	/**
	 * 获取浏览器信息
	 * 
	 * @return 返回浏览器和版本
	 */
	public String getBrowserInfo() {
		String s = SystemUtils.STR_NAV_JS;
		String value = (String) this.executeJavaScript(s);
		String browser = "当前执行浏览器： ";
		String version = ",版本：";
		if (value.contains("firefox")) {
			browser += "Firefox ";
			version += value.split("firefox/")[1];
		} else if (value.contains("msie")) {
			browser += "IE ";
			version += value.split("msie")[1].split(";")[0];
		} else if (value.contains("chrome")) {
			browser += "Chrome ";
			version += value.split("chrome/")[1].split(" ")[0];
		} else if (value.contains("safari") && !(value.contains("chrome"))) {
			browser += "Safari ";
			version += value.split("version/")[1].split(" ")[0];
		} else {

			return "当前浏览器尚未处理";
		}
		return browser + version;
	}

	/**
	 * 打开配置文件中的URL使用该方法
	 */
	public void openUrl() {
		ConfigProperties sp = ConfigProperties.getInstance();
		String url = sp.getString("URL");
		String port = sp.getString("webPort");
		if (url == null || url.isEmpty()) {
			String message = "配置文件包conf的silencer.properties文件中，URL基站地址有误";
			this.throwException(message);
		}
		String Url = null;
		if (port == null || port.isEmpty()) {
			Url = url;
		} else {
			Url = url + ":" + port;
		}
		this.openUrl(Url);
	}

	/**
	 * 打开用户自定义URL
	 * 
	 * @param url
	 *            自定义URL地址
	 */
	public void openUrl(String url) {
		if (url == null || url.isEmpty()) {
			String message = "URL基站地址有误";
			this.throwException(message);
		}
		try {
			this.browser.get(url);
		} catch (TimeoutException e) {
		} finally {
			sleep(2);
			browser.get(url);
		}
		this.log.info("加载页面[" + url + "]");
		this.winHandle = this.browser.getWindowHandle();
		this.windowsList.add(winHandle);
	}

	/**
	 * 获取当前URL
	 */
	public String getCurrentUrl() {
		String url = "";
		url = this.browser.getCurrentUrl();
		return url;
	}

	/**
	 * 抛出运行时异常
	 * 
	 * @param message
	 *            可输出异常信息
	 */
	public void throwException(String message) {
		throw new RuntimeException(message);
	}

	/**
	 * 获取驱动浏览器句柄
	 * 
	 * @return WebDriver实例对象
	 */
	public WebDriver getBrowser() {
		return this.browser;
	}

	/**
	 * 设置浏览器句柄
	 * 
	 * @param browser
	 *            需要定制的浏览器
	 */
	public void setBrowser(WebDriver browser) {
		this.browser = browser;
	}

	/**
	 * 获取最原始窗口句柄
	 * 
	 * @return 窗口句柄标识
	 */
	public String getMainHandle() {
		return this.winHandle;
	}

	/**
	 * 添加新的句柄
	 * 
	 * @param windowHandle
	 *            窗口句柄标识
	 */
	public void addHandle(String windowHandle) {
		if (windowHandle == null || windowHandle.length() == 0) {
			String message = "当前提供的window的句柄为空";
			this.log.error(message);
			throwException(message);
		}
		if (this.windowsList.contains(windowHandle)) {
			String message = "窗口句柄已经添加";
			this.log.error(message);
			throwException(message);
			return;
		}
		this.windowsList.add(windowHandle);
	}

	/**
	 * 获取当前窗口
	 * 
	 * @return 返回当前窗口句柄标识
	 */
	public String getCurrentWindow() {
		return this.getBrowser().getWindowHandle();
	}

	/**
	 * 关闭当前窗口
	 */
	public void closeCurrentWindow() {
		windowsList.remove(getCurrentWindow());
		getBrowser().close();
	}

	/**
	 * 切换至最新弹出的窗口
	 */
	public void selectNewWindow() {
		this.sleep(1);
		// this.browser.getWindowHandles();
		Set<String> aWins = this.browser.getWindowHandles();
		for (String id : aWins) {
			if (this.windowsList.indexOf(id) == -1) {
				browser = this.selectWindow(browser, id);
				this.log.info("切换至最新窗口");
				this.windowsList.add(id);
				break;
			}
		}
	}

	/**
	 * 切换至指定的窗口win
	 * 
	 * @param win
	 *            窗口句柄标识
	 */
	public void selectWindow(String win) {
		this.selectWindow(this.browser, win);
	}

	/**
	 * 将窗口从driver 切换到NewID的窗口上
	 * 
	 * @param driver
	 *            驱动浏览器对象
	 * @param newId
	 *            窗口句柄标识
	 * @return 驱动浏览器对象
	 */
	public WebDriver selectWindow(WebDriver driver, String newId) {
		WebDriver d = null;
		WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
		ExceptWindow ew = new ExceptWindow(newId);
		this.sleep(1);
		try {
			d = wait.until(ew);
		} catch (TimeoutException te) {
			String message = "切换窗口失败";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		this.log.info("切换窗口成功");
		return d;
	}

	/**
	 * 切换至原来的主要窗口
	 */
	public void selectMainWindow() {
		this.selectWindow(browser, winHandle);
		this.log.info("切换至主窗口");
	}

	/**
	 * 浏览器历史记录前进
	 */
	public void forward() {
		this.getBrowser().navigate().forward();
	}

	/**
	 * 浏览器历史记录后退
	 */
	public void back() {
		this.getBrowser().navigate().back();
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		try {
			this.browser.navigate().refresh();
		} catch (Exception e) {
			String error = "刷新页面有误!";
			this.log.error(error);
			this.screenShot(error);
			throwException("刷新页面有误");
		}
	}

	/**
	 * 关闭浏览器及驱动进程
	 * 
	 * @param allProcess
	 *            是否关闭所有浏览器及驱动进程
	 */
	public void close(boolean allProcess) {
		if (this.browser != null) {
			try {
				this.browser.quit();
			} catch (Exception e) {
				this.log.error(e.getMessage());
			}
			this.log.info(Messages.getString("AutoTest.endtip"));
		}
		if (allProcess) {
			if (this.browserType.equals(BrowserType.IE)) {
				this.destroyIE();
			}
			if (this.browserType.equals(BrowserType.CHROME)) {
				this.destroyChrome();
			}
			if (this.browserType.equals(BrowserType.FIREFOX)) {
				this.destroyFirefox();
			}
		}
	}

	/**
	 * 关闭浏览器，只关闭本次实例化的进程
	 */
	public void close() {
		close(false);
	}

	/**
	 * 释放IE资源 在异常执行情况下，存在无法关闭IE浏览器，并且在任务管理器中存在
	 * IEDriverServer的进程,执行完毕后，保证执行机器的干净，关闭存在的 IE和IEDriverServer进程
	 */
	public void destroyIE() {
		ArrayList<String> list = SystemUtils.getTaskList();
		if (list != null && !(list.isEmpty())) {
			boolean hasIedriver = false;
			for (String name : list) {
				if (name.equalsIgnoreCase("IEDriverServer.exe") || name.equalsIgnoreCase("IEDriverServer")) {
					hasIedriver = true;
					break;
				}
			}
			if (hasIedriver) {
				SystemUtils.killIeDriverSever();
			}
			boolean hasIe = false;
			for (String name : list) {
				if (name.equalsIgnoreCase("iexplore.exe") || name.equalsIgnoreCase("iexplore")) {
					hasIe = true;
					break;
				}
			}
			if (hasIe) {
				SystemUtils.killIE();
			}
		}
	}

	/**
	 * 释放Firefox资源. 在异常执行情况下，存在无法关闭Firefox浏览器， 保证执行机器的干净，关闭存在的Firefox进程
	 */
	public void destroyFirefox() {
		ArrayList<String> list = SystemUtils.getTaskList();
		if (list != null && !(list.isEmpty())) {
			boolean hasFirefox = false;
			for (String name : list) {
				if (name.equalsIgnoreCase("firefox.exe") || name.equalsIgnoreCase("firefox")) {
					hasFirefox = true;
					break;
				}
			}
			if (hasFirefox) {
				SystemUtils.killFirefox();
			}
			boolean hasfirefoxDriver = false;
			for (String name : list) {
				if (name.equalsIgnoreCase("geckodriver.exe") || name.equalsIgnoreCase("geckodriver")) {
					hasfirefoxDriver = true;
					break;
				}
			}
			if (hasfirefoxDriver) {
				SystemUtils.killFirefoxDriver();
			}
		}
	}

	/**
	 * 释放Chrome资源. 在异常执行情况下，存在无法关闭Chrome浏览器，并且在任务管理器中存在
	 * ChromeDriver的进程,执行完毕后，保证执行机器的干净，关闭存在的Chrome 和ChromeDriver进程
	 */
	public void destroyChrome() {
		ArrayList<String> list = SystemUtils.getTaskList();
		if (list != null && !(list.isEmpty())) {
			boolean hasChrome = false;
			for (String name : list) {
				if (name.startsWith("Chrome.exe") || name.startsWith("chrome.exe") || name.contains("chrome.exe")) {
					hasChrome = true;
					break;
				}
			}
			if (hasChrome) {
				SystemUtils.killChrome();
			}
			boolean hasChromeDriver = false;
			for (String name : list) {
				if (name.equalsIgnoreCase("ChromeDriver.exe") || name.equalsIgnoreCase("ChromeDriver")) {
					hasChromeDriver = true;
					break;
				}
			}
			if (hasChromeDriver) {
				SystemUtils.killChromeDriver();
			}
		}
	}

	//
	// public void setTestMessage(String title, Class<?> t)
	// {
	// if (title == null || title.length() == 0)
	// {
	// title = "测试开始";
	// }
	// this.log.info("==========================================================");
	// this.log.info("=");
	// this.log.info("= " + title);
	// this.log.info("= " + t.getName());
	// this.log.info("=");
	// this.log.info("==========================================================");
	// }

	/**
	 * 获取日志控制
	 * 
	 * @return 返回日志句柄
	 */
	public Log getLog() {
		return this.log;
	}

	/**
	 * 记录日志-info
	 * 
	 * @param message
	 *            日志记录信息
	 */
	public void loginfo(String message) {
		this.log.info(message);
	}

	/**
	 * 截图
	 * 
	 * @param message
	 *            用于组成错误截图文件名
	 */
	protected void screenShot(String message) {
		String title = SystemUtils.getScrennShotTime() + message.replaceAll("/", "\\/");
		this.log.info("请查看抓图文件: " + ConfigProperties.getInstance().getString("screenshot") + File.separator + title);
		try {
			ScreenCapture.screenShot(this.browser, title);
		} catch (IOException e) {
			this.log.error("抓图失败");
		}
	}

	/**
	 * 在相关网页上执行js脚本
	 * 
	 * @param driver
	 *            驱动浏览器对象
	 * @param js
	 *            需要执行脚本
	 * @param args
	 *            对应的元素参数
	 * @return Object
	 */
	public Object executeJavaScript(WebDriver driver, String js, Object... args) {
		try {
			if (null == args || args.length == 0) {
				return ((JavascriptExecutor) driver).executeScript(js);
			} else {
				return ((JavascriptExecutor) driver).executeScript(js, args);
			}
		} catch (Exception e) {
			String error = "页面执行Java Script" + js + "有误!";
			this.log.error(error);
			this.screenShot("页面执行Java Script有误");
			throwException("js执行失败");
		}
		return null;
	}

	/**
	 * 在当前浏览器上执行javascript脚本
	 * 
	 * @param js
	 *            需要执行脚本
	 * @param args
	 *            对应的元素参数
	 * @return Object
	 */
	public Object executeJavaScript(String js, Object... args) {
		WebDriver wd = this.getBrowser();
		return this.executeJavaScript(wd, js, args);
	}

	/**
	 * 在当前浏览器上执行javascript脚本
	 * 
	 * @param js
	 *            需要执行脚本
	 * @return Object
	 */
	public Object executeJavaScript(String js) {
		WebDriver wd = this.getBrowser();
		return this.executeJavaScript(wd, js, new Object[0]);
	}

	/**
	 * 页面等待
	 * 
	 * @param second
	 *            需要等待的时间，以秒为单位
	 */
	public void sleep(int second) {
		try {
			long millis = second * 1000;
			Thread.sleep(millis);
			this.log.info(Messages.getString("AutoTest.pageWait") + second + Messages.getString("AutoTest.millis"));
		} catch (InterruptedException e) {
			throwException("等待失败");
		}
	}

	/**
	 * 页面等待
	 * 
	 * @param second
	 *            需要等待的时间，以秒为单位
	 */
	public void sleep(double second) {
		try {
			long millis = (long) (second * 1000);
			Thread.sleep(millis);
			this.log.info(Messages.getString("AutoTest.pageWait") + second + Messages.getString("AutoTest.millis"));
		} catch (InterruptedException e) {
			throwException("等待失败");
		}
	}

	/**
	 * 获取窗口title
	 * 
	 * @return 窗口title
	 */
	public String getWindowTitle() {
		return this.browser.getTitle();
	}

	/**
	 * 获取当前页面内容
	 * 
	 * @return 获取当前页面内容
	 */
	public String getPageSource() {
		return this.browser.getPageSource();
	}

	/**
	 * 增加Cookie
	 * 
	 * @param name
	 *            Cookie的key值
	 * @param value
	 *            Cookie的value值
	 * @return 返回增加是否成功的boolean值
	 */
	public boolean addCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		WebDriver dr = this.getBrowser();
		dr.manage().addCookie(cookie);
		// Set<Cookie> cookies = dr.manage().getCookies();
		return true;
	}

}
