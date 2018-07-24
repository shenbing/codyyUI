package com.coddy.adminTest;

import java.util.HashMap;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import com.coddy.check.ActionCheck;
import com.coddy.spring.BrowserType;
import com.coddy.spring.FileType;
import com.coddy.spring.PageAction;
import com.coddy.utils.ConfigProperties;
import com.coddy.utils.TestDataProvider;

public class PersonalPassword
{
	public PageAction pageAction;
	String url = null;

	@BeforeClass
	public void beforeClass()
	{
		ConfigProperties silencer = ConfigProperties.getInstance();
		url = silencer.getString("AdminURL");
		pageAction = new PageAction(BrowserType.FIREFOX);
		pageAction.loadPageData(System.getProperty("user.dir")
				+ "/pagedata/后台/公共.yalm", FileType.YALM);
		pageAction.loadPageData(System.getProperty("user.dir")
				+ "/pagedata/后台/个人账号.yalm", FileType.YALM);
	}

	@Test(dataProvider = "errorOldPWDProvider")
	public void errorOldPWDTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_个人账号_a");
		pageAction.sleep(2);
		pageAction.click("个人账号_修改密码_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.input("修改密码_原密码_input", input.get("修改密码_原密码"));
		pageAction.input("修改密码_新密码_input", input.get("修改密码_新密码"));
		pageAction.input("修改密码_确认新密码_input", input.get("修改密码_确认新密码"));
		pageAction.click("修改密码_修改_input");
		ActionCheck.verfiyEquals(pageAction.getText("修改密码_原密码错误提示_i"),
				expect.get("修改密码_原密码错误提示"), "原密码错误");
		pageAction.selectDefaultFrame();
		pageAction.click("公共_退出_a");
	}

	@Test(dataProvider = "differentNewPWDProvider")
	public void differentNewPWDTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_个人账号_a");
		pageAction.sleep(2);
		pageAction.click("个人账号_修改密码_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.input("修改密码_原密码_input", input.get("修改密码_原密码"));
		pageAction.input("修改密码_新密码_input", input.get("修改密码_新密码"));
		pageAction.input("修改密码_确认新密码_input", input.get("修改密码_确认新密码"));
		pageAction.click("修改密码_修改_input");
		ActionCheck.verfiyEquals(pageAction.getText("修改密码_两次输入的密码不一致错误提示_i"),
				expect.get("修改密码_两次输入的密码不一致错误提示"), "两次输入的密码不一致");
		pageAction.selectDefaultFrame();
		pageAction.click("公共_退出_a");
	}

	@Test(dataProvider = "rightPWDProvider")
	public void rightPWDTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_个人账号_a");
		pageAction.sleep(2);
		pageAction.click("个人账号_修改密码_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.input("修改密码_原密码_input", input.get("修改密码_原密码"));
		pageAction.input("修改密码_新密码_input", input.get("修改密码_新密码"));
		pageAction.input("修改密码_确认新密码_input", input.get("修改密码_确认新密码"));
		pageAction.click("修改密码_修改_input");
		ActionCheck.verfiyEquals(pageAction.getText("修改密码_修改结果信息_span"),
				expect.get("修改密码_修改结果信息"), "修改密码");
		pageAction.selectDefaultFrame();
		pageAction.click("公共_退出_a");
	}

	@AfterClass
	public void afterClass()
	{
		pageAction.close();
	}

	@DataProvider
	public Object[][] errorOldPWDProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/个人账号.xls").getInputAndExpectData("FUN_HT_YYSHT_GRZX_XGMM_001_1");
	}

	@DataProvider
	public Object[][] differentNewPWDProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/个人账号.xls").getInputAndExpectData("FUN_HT_YYSHT_GRZX_XGMM_001_2");
	}

	@DataProvider
	public Object[][] rightPWDProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/个人账号.xls").getInputAndExpectData("FUN_HT_YYSHT_GRZX_XGMM_001_3");
	}
}
