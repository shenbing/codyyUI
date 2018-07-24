package com.coddy.spring;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * BrowserInit WebDriver初始化类
 * 
 * @author shenbing
 * 
 */
final class BrowserInit {

	/**
	 * 使用IE浏览器
	 * 
	 * @return IE的WebDriver对象
	 */
	static WebDriver getIe() {
		String dir_name = System.getProperty("user.dir") + "/driver/IEDriverServer.exe";
		System.setProperty("webdriver.ie.driver", dir_name);
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		InternetExplorerOptions ieOptions = new InternetExplorerOptions(ieCapabilities);
		WebDriver ie = new InternetExplorerDriver(ieOptions);
		return ie;
	}

	/**
	 * 使用Firefox浏览器
	 * 
	 * @return Firefox的WebDriver对象
	 */
	static WebDriver getFirefox() {
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/driver/geckodriver.exe");
		WebDriver firefox = new FirefoxDriver();
		// WebDriver ff = null;
		// java.io.File file = new java.io.File(System.getProperty("user.dir")
		// + SilencerProperties.getInstance().getString("Firefox.profile"));
		// if (file.exists())
		// {
		// FirefoxProfile profile = new FirefoxProfile(file);
		// ff = new FirefoxDriver(profile);
		// }
		// else
		// {
		// ff = new FirefoxDriver();
		// }
		// WebDriver ff = new FirefoxDriver();
		// FirefoxProfile profile = new FirefoxProfile();
		// profile.setEnableNativeEvents(true);
		// WebDriver ff = new FirefoxDriver(profile);
		// ProfilesIni allProfiles = new ProfilesIni();
		// FirefoxProfile profile = allProfiles.getProfile("default");
		return firefox;
	}

	/**
	 * 使用Chrome浏览器
	 * 
	 * @return Chrome的WebDriver对象
	 */
	static WebDriver getChrome() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/driver/chromedriver.exe");
		// HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		// chromePrefs.put("plugins.plugins_enabled",
		// new String[] { "Adobe Flash Player", "Chrome PDF Viewer", "Native
		// Client" });
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--test-type", "--ignore-certificate-errors");
		// options.setExperimentalOption("prefs", chromePrefs);
		WebDriver chrome = new ChromeDriver(options);
		// WebDriver chrome = new ChromeDriver();
		return chrome;
	}
}
