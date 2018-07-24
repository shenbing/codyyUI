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

public class StudentsManage
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
				+ "/pagedata/后台/用户管理.yalm", FileType.YALM);
	}

	@Test(dataProvider = "testDataProvider")
	public void searchAndReviewStudentTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_用户管理_a");
		pageAction.sleep(2);
		pageAction.click("用户管理_学生管理_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.click("学生管理_所在地区_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("学生管理_所在地区列表_div", "学生管理_查询下拉列表_div",
				input.get("学生管理_所在地区"));
		pageAction.click("学生管理_学段_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("学生管理_学段列表_div", "学生管理_查询下拉列表_div",
				input.get("学生管理_学段"));
		pageAction.click("学生管理_学校名称_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("学生管理_学校名称列表_div", "学生管理_查询下拉列表_div",
				input.get("学生管理_学校名称"));
		pageAction.click("学生管理_年级_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("学生管理_年级列表_div", "学生管理_查询下拉列表_div",
				input.get("学生管理_年级"));
		pageAction.click("学生管理_班级_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("学生管理_班级列表_div", "学生管理_查询下拉列表_div",
				input.get("学生管理_班级"));
		pageAction.input("学生管理_学生姓名_input", input.get("学生管理_学生姓名"));
		pageAction.click("学生管理_搜索_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("学生管理_学生列表行_tr", "学生管理_查看_a",
				input.get("学生管理_学生姓名"));
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("学生详情_姓名行_li").split("\n")[1],
				input.get("学生管理_学生姓名"), "查看学生详情失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "searchAndReviewStudentTest")
	public void resetpasswordTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String account = pageAction.getText("学生详情_ID行_li").split("\n")[1].trim();
		pageAction.click("学生详情_重置密码_input");
		pageAction.sleep(2);
		String message = pageAction.getText("学生详情_重置密码结果信息_div");
		String result = message.split("\n")[0];
		String newPassword = message.split("\n")[1].split(":")[1];
		ActionCheck.verfiyEquals(result, expect.get("重置密码提示"), "重置密码失败");
		pageAction.click("学生详情_提示框_确定_span");
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
				expect.get("学生登陆信息"), "新密码登陆账号失败");
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
		pageAction.click("学生详情_返回_a");
		pageAction.sleep(2);
		pageAction.input("学生管理_学生姓名_input", input.get("学生管理_学生姓名"));
		pageAction.click("学生管理_搜索_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("学生管理_学生列表行_tr", "学生管理_锁定_a",
				input.get("学生管理_学生姓名"));
		pageAction.sleep(2);
		pageAction.click("学生管理_锁定确认框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("学生管理_锁定解锁结果提示_div"),
				expect.get("锁定解锁定操作结果提示"), "学生锁定失败");
		pageAction.click("学生管理_提示框_确定_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "lockTest")
	public void unLockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("学生管理_学生列表行_tr", "学生管理_解锁_a",
				input.get("学生管理_学生姓名"));
		pageAction.sleep(2);
		pageAction.click("学生管理_解锁确认框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("学生管理_锁定解锁结果提示_div"),
				expect.get("锁定解锁定操作结果提示"), "学生馆解锁失败");
		pageAction.click("学生管理_提示框_确定_a");
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
				+ "/testdata/后台/用户管理.xls")
				.getInputAndExpectData("FUN_HT_YYSHT_YHGL_XSGL_CK_002_1");
	}
}
