package com.coddy.adminTest;

import java.util.HashMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import com.coddy.check.ActionCheck;
import com.coddy.spring.BrowserType;
import com.coddy.spring.FileType;
import com.coddy.spring.PageAction;
import com.coddy.utils.ConfigProperties;
import com.coddy.utils.TestDataProvider;
import com.gargoylesoftware.htmlunit.Page;

public class Test
{
	public PageAction pageAction;
	String url = null;
	
	public static void main(String[] args){
		PageAction driver = new PageAction(BrowserType.CHROME);
		driver.openUrl("http://www.jtljia.com");
		Actions act = new Actions(driver.getBrowser());
		act.moveToElement(driver.getBrowser().findElement(By.cssSelector("body > footer"))).build().perform();
		driver.sleep(5);
		driver.close();
		
		
	}

}
