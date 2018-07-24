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

public class SchoolManage
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
				+ "/pagedata/后台/机构管理.yalm", FileType.YALM);
	}

	@Test(dataProvider = "testDataProvider")
	public void searchAndReviewSchoolTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_机构管理_a");
		pageAction.sleep(2);
		pageAction.click("机构管理_学校管理_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.selectItemByText("学校管理_省份_select", input.get("学校管理_省份"));
		pageAction.sleep(0.5);
		pageAction.selectItemByText("学校管理_地级城市_select", input.get("学校管理_地级城市"));
		pageAction.sleep(0.5);
		pageAction.selectItemByText("学校管理_县级城市_select", input.get("学校管理_县级城市"));
		pageAction.sleep(0.5);
		pageAction.input("学校管理_学校名称_input", input.get("学校管理_学校名称"));
		pageAction.click("学校管理_搜索_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("学校管理_学校列表行_tr", "学校管理_查看_a",
				input.get("学校管理_学校名称"));
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(
				pageAction.getCellTextByID("学校管理_学校详情_table", "0.1"),
				input.get("学校管理_学校名称"), "查看学校详情失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "searchAndReviewSchoolTest")
	public void resetpasswordTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String account = pageAction.getCellTextByID("学校管理_学校详情_table", "3.1");
		pageAction.click("学校详情_重置密码_a");
		pageAction.click("学校详情_重置密码确认框_确定_a");
		pageAction.sleep(2);
		String message = pageAction.getText("学校详情_重置密码结果信息_center");
		String result = message.split("\n")[0];
		String newPassword = message.split("\n")[1].split("：")[1];
		ActionCheck.verfiyEquals(result, expect.get("重置密码提示"), "重置密码失败");
		pageAction.click("学校详情_提示框_确定_a");
		PageAction pa = new PageAction(BrowserType.FIREFOX);
		pa.loadPageData(System.getProperty("user.dir") + "/pagedata/公共.yalm",
				FileType.YALM);
		pa.loadPageData(System.getProperty("user.dir") + "/pagedata/我的资料.yalm",
				FileType.YALM);
		pa.openUrl();
		pa.sleep(1);
		pa.input("登陆_用户名_input", account);
		pa.input("登陆_密码_input", newPassword);
		pa.click("登陆_登陆_input");
		pa.sleep(1);
		ActionCheck.verfiyEquals(pa.getText("头部_登陆帐号信息_a"),
				input.get("学校管理_学校名称"), "新密码登陆账号失败");
		pa.moveOn("头部_头像_img");
		pa.click("头部_个人资料_a");
		pa.click("我的资料_修改密码_a");
		pa.sleep(1);
		pa.input("修改密码_当前密码_input", newPassword);
		pa.input("修改密码_新密码_input", input.get("修改密码_新密码"));
		pa.input("修改密码_确认新密码_input", input.get("修改密码_新密码"));
		pa.click("修改密码_保存_input");
		pa.sleep(1);
		ActionCheck.verfiyEquals(pa.getText("我的资料_提示信息_span"),
				expect.get("修改密码提示"), "修改密码失败");
		pa.closeCurrentWindow();
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "resetpasswordTest")
	public void lockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("学校详情_返回列表_a");
		pageAction.sleep(2);
		pageAction.input("学校管理_学校名称_input", input.get("学校管理_学校名称"));
		pageAction.click("学校管理_搜索_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("学校管理_学校列表行_tr", "学校管理_锁定_a",
				input.get("学校管理_学校名称"));
		pageAction.sleep(2);
		pageAction.click("学校管理_锁定确认框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("学校管理_锁定解锁结果提示_div"),
				expect.get("学校管理_锁定解锁结果提示"), "学校锁定失败");
		pageAction.click("学校管理_提示框_确定_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "lockTest")
	public void unLockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("学校管理_学校列表行_tr", "学校管理_解锁_a",
				input.get("学校管理_学校名称"));
		pageAction.sleep(2);
		pageAction.click("学校管理_解锁确认框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("学校管理_锁定解锁结果提示_div"),
				expect.get("学校管理_锁定解锁结果提示"), "学校解锁失败");
		pageAction.click("学校管理_提示框_确定_a");
	}

	@AfterClass
	public void afterClass()
	{
		pageAction.close();
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/机构管理.xls")
				.getInputAndExpectData("FUN_HT_YYSHT_JGGL_XXGL_CK_001_1");
	}
}
