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
 * Browser ������࣬ʵ���������ع���
 * 
 * @author shenbing
 * 
 */
public class Browser {
	/**
	 * Ĭ�ϳ�ʱʱ��
	 */
	public int TIME_OUT = Integer.parseInt(ConfigProperties.getInstance().getString("element.find.timeout"));

	/**
	 * ���������
	 */
	protected WebDriver browser;

	/**
	 * ��־����
	 */
	protected Log log = LogUtils.getLog(Browser.class.getName());

	/**
	 * ����ִ�й����д��ڵ�ID��¼�б�
	 */
	public ArrayList<String> windowsList;

	/**
	 * ��ԭʼ���ھ��
	 */
	protected String winHandle;
	// /**
	// * ��ǰ�����
	// */
	// protected WebDriver currentBrowser;
	/**
	 * ���������ö��
	 */
	protected BrowserType browserType;

	/**
	 * Ĭ�Ϲ��췽��
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
	 * ���췽��
	 * 
	 * @param bt
	 *            BrowserTypeö��ֵ
	 */
	public Browser(BrowserType bt) {
		this.browserType = bt;
		this.windowsList = new ArrayList<String>();
		initBrowser();
	}

	/**
	 * ��ʼ�������
	 */
	public void initBrowser() {
		this.log.info(" **********���������!��ʼִ��!**********");
		this.log.info("��ǰ����ϵͳ��" + SystemUtils.getSystemInfo("os.name"));
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
	 * ��ȡ�������Ϣ
	 * 
	 * @return ����������Ͱ汾
	 */
	public String getBrowserInfo() {
		String s = SystemUtils.STR_NAV_JS;
		String value = (String) this.executeJavaScript(s);
		String browser = "��ǰִ��������� ";
		String version = ",�汾��";
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

			return "��ǰ�������δ����";
		}
		return browser + version;
	}

	/**
	 * �������ļ��е�URLʹ�ø÷���
	 */
	public void openUrl() {
		ConfigProperties sp = ConfigProperties.getInstance();
		String url = sp.getString("URL");
		String port = sp.getString("webPort");
		if (url == null || url.isEmpty()) {
			String message = "�����ļ���conf��silencer.properties�ļ��У�URL��վ��ַ����";
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
	 * ���û��Զ���URL
	 * 
	 * @param url
	 *            �Զ���URL��ַ
	 */
	public void openUrl(String url) {
		if (url == null || url.isEmpty()) {
			String message = "URL��վ��ַ����";
			this.throwException(message);
		}
		try {
			this.browser.get(url);
		} catch (TimeoutException e) {
		} finally {
			sleep(2);
			browser.get(url);
		}
		this.log.info("����ҳ��[" + url + "]");
		this.winHandle = this.browser.getWindowHandle();
		this.windowsList.add(winHandle);
	}

	/**
	 * ��ȡ��ǰURL
	 */
	public String getCurrentUrl() {
		String url = "";
		url = this.browser.getCurrentUrl();
		return url;
	}

	/**
	 * �׳�����ʱ�쳣
	 * 
	 * @param message
	 *            ������쳣��Ϣ
	 */
	public void throwException(String message) {
		throw new RuntimeException(message);
	}

	/**
	 * ��ȡ������������
	 * 
	 * @return WebDriverʵ������
	 */
	public WebDriver getBrowser() {
		return this.browser;
	}

	/**
	 * ������������
	 * 
	 * @param browser
	 *            ��Ҫ���Ƶ������
	 */
	public void setBrowser(WebDriver browser) {
		this.browser = browser;
	}

	/**
	 * ��ȡ��ԭʼ���ھ��
	 * 
	 * @return ���ھ����ʶ
	 */
	public String getMainHandle() {
		return this.winHandle;
	}

	/**
	 * ����µľ��
	 * 
	 * @param windowHandle
	 *            ���ھ����ʶ
	 */
	public void addHandle(String windowHandle) {
		if (windowHandle == null || windowHandle.length() == 0) {
			String message = "��ǰ�ṩ��window�ľ��Ϊ��";
			this.log.error(message);
			throwException(message);
		}
		if (this.windowsList.contains(windowHandle)) {
			String message = "���ھ���Ѿ����";
			this.log.error(message);
			throwException(message);
			return;
		}
		this.windowsList.add(windowHandle);
	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return ���ص�ǰ���ھ����ʶ
	 */
	public String getCurrentWindow() {
		return this.getBrowser().getWindowHandle();
	}

	/**
	 * �رյ�ǰ����
	 */
	public void closeCurrentWindow() {
		windowsList.remove(getCurrentWindow());
		getBrowser().close();
	}

	/**
	 * �л������µ����Ĵ���
	 */
	public void selectNewWindow() {
		this.sleep(1);
		// this.browser.getWindowHandles();
		Set<String> aWins = this.browser.getWindowHandles();
		for (String id : aWins) {
			if (this.windowsList.indexOf(id) == -1) {
				browser = this.selectWindow(browser, id);
				this.log.info("�л������´���");
				this.windowsList.add(id);
				break;
			}
		}
	}

	/**
	 * �л���ָ���Ĵ���win
	 * 
	 * @param win
	 *            ���ھ����ʶ
	 */
	public void selectWindow(String win) {
		this.selectWindow(this.browser, win);
	}

	/**
	 * �����ڴ�driver �л���NewID�Ĵ�����
	 * 
	 * @param driver
	 *            �������������
	 * @param newId
	 *            ���ھ����ʶ
	 * @return �������������
	 */
	public WebDriver selectWindow(WebDriver driver, String newId) {
		WebDriver d = null;
		WebDriverWait wait = new WebDriverWait(driver, TIME_OUT);
		ExceptWindow ew = new ExceptWindow(newId);
		this.sleep(1);
		try {
			d = wait.until(ew);
		} catch (TimeoutException te) {
			String message = "�л�����ʧ��";
			this.log.error(message);
			this.screenShot(message);
			throwException(message);
			return null;
		}
		this.log.info("�л����ڳɹ�");
		return d;
	}

	/**
	 * �л���ԭ������Ҫ����
	 */
	public void selectMainWindow() {
		this.selectWindow(browser, winHandle);
		this.log.info("�л���������");
	}

	/**
	 * �������ʷ��¼ǰ��
	 */
	public void forward() {
		this.getBrowser().navigate().forward();
	}

	/**
	 * �������ʷ��¼����
	 */
	public void back() {
		this.getBrowser().navigate().back();
	}

	/**
	 * ˢ��ҳ��
	 */
	public void refresh() {
		try {
			this.browser.navigate().refresh();
		} catch (Exception e) {
			String error = "ˢ��ҳ������!";
			this.log.error(error);
			this.screenShot(error);
			throwException("ˢ��ҳ������");
		}
	}

	/**
	 * �ر����������������
	 * 
	 * @param allProcess
	 *            �Ƿ�ر��������������������
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
	 * �ر��������ֻ�رձ���ʵ�����Ľ���
	 */
	public void close() {
		close(false);
	}

	/**
	 * �ͷ�IE��Դ ���쳣ִ������£������޷��ر�IE�����������������������д���
	 * IEDriverServer�Ľ���,ִ����Ϻ󣬱�ִ֤�л����ĸɾ����رմ��ڵ� IE��IEDriverServer����
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
	 * �ͷ�Firefox��Դ. ���쳣ִ������£������޷��ر�Firefox������� ��ִ֤�л����ĸɾ����رմ��ڵ�Firefox����
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
	 * �ͷ�Chrome��Դ. ���쳣ִ������£������޷��ر�Chrome�����������������������д���
	 * ChromeDriver�Ľ���,ִ����Ϻ󣬱�ִ֤�л����ĸɾ����رմ��ڵ�Chrome ��ChromeDriver����
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
	// title = "���Կ�ʼ";
	// }
	// this.log.info("==========================================================");
	// this.log.info("=");
	// this.log.info("= " + title);
	// this.log.info("= " + t.getName());
	// this.log.info("=");
	// this.log.info("==========================================================");
	// }

	/**
	 * ��ȡ��־����
	 * 
	 * @return ������־���
	 */
	public Log getLog() {
		return this.log;
	}

	/**
	 * ��¼��־-info
	 * 
	 * @param message
	 *            ��־��¼��Ϣ
	 */
	public void loginfo(String message) {
		this.log.info(message);
	}

	/**
	 * ��ͼ
	 * 
	 * @param message
	 *            ������ɴ����ͼ�ļ���
	 */
	protected void screenShot(String message) {
		String title = SystemUtils.getScrennShotTime() + message.replaceAll("/", "\\/");
		this.log.info("��鿴ץͼ�ļ�: " + ConfigProperties.getInstance().getString("screenshot") + File.separator + title);
		try {
			ScreenCapture.screenShot(this.browser, title);
		} catch (IOException e) {
			this.log.error("ץͼʧ��");
		}
	}

	/**
	 * �������ҳ��ִ��js�ű�
	 * 
	 * @param driver
	 *            �������������
	 * @param js
	 *            ��Ҫִ�нű�
	 * @param args
	 *            ��Ӧ��Ԫ�ز���
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
			String error = "ҳ��ִ��Java Script" + js + "����!";
			this.log.error(error);
			this.screenShot("ҳ��ִ��Java Script����");
			throwException("jsִ��ʧ��");
		}
		return null;
	}

	/**
	 * �ڵ�ǰ�������ִ��javascript�ű�
	 * 
	 * @param js
	 *            ��Ҫִ�нű�
	 * @param args
	 *            ��Ӧ��Ԫ�ز���
	 * @return Object
	 */
	public Object executeJavaScript(String js, Object... args) {
		WebDriver wd = this.getBrowser();
		return this.executeJavaScript(wd, js, args);
	}

	/**
	 * �ڵ�ǰ�������ִ��javascript�ű�
	 * 
	 * @param js
	 *            ��Ҫִ�нű�
	 * @return Object
	 */
	public Object executeJavaScript(String js) {
		WebDriver wd = this.getBrowser();
		return this.executeJavaScript(wd, js, new Object[0]);
	}

	/**
	 * ҳ��ȴ�
	 * 
	 * @param second
	 *            ��Ҫ�ȴ���ʱ�䣬����Ϊ��λ
	 */
	public void sleep(int second) {
		try {
			long millis = second * 1000;
			Thread.sleep(millis);
			this.log.info(Messages.getString("AutoTest.pageWait") + second + Messages.getString("AutoTest.millis"));
		} catch (InterruptedException e) {
			throwException("�ȴ�ʧ��");
		}
	}

	/**
	 * ҳ��ȴ�
	 * 
	 * @param second
	 *            ��Ҫ�ȴ���ʱ�䣬����Ϊ��λ
	 */
	public void sleep(double second) {
		try {
			long millis = (long) (second * 1000);
			Thread.sleep(millis);
			this.log.info(Messages.getString("AutoTest.pageWait") + second + Messages.getString("AutoTest.millis"));
		} catch (InterruptedException e) {
			throwException("�ȴ�ʧ��");
		}
	}

	/**
	 * ��ȡ����title
	 * 
	 * @return ����title
	 */
	public String getWindowTitle() {
		return this.browser.getTitle();
	}

	/**
	 * ��ȡ��ǰҳ������
	 * 
	 * @return ��ȡ��ǰҳ������
	 */
	public String getPageSource() {
		return this.browser.getPageSource();
	}

	/**
	 * ����Cookie
	 * 
	 * @param name
	 *            Cookie��keyֵ
	 * @param value
	 *            Cookie��valueֵ
	 * @return ���������Ƿ�ɹ���booleanֵ
	 */
	public boolean addCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		WebDriver dr = this.getBrowser();
		dr.manage().addCookie(cookie);
		// Set<Cookie> cookies = dr.manage().getCookies();
		return true;
	}

}
