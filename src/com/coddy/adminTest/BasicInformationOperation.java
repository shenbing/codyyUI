package com.coddy.adminTest;

import java.util.HashMap;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import com.coddy.DB.DbUnit;
import com.coddy.check.ActionCheck;
import com.coddy.spring.BrowserType;
import com.coddy.spring.FileType;
import com.coddy.spring.PageAction;
import com.coddy.utils.ConfigProperties;
import com.coddy.utils.TestDataProvider;

public class BasicInformationOperation
{
	public PageAction pageAction = null;
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
				+ "/pagedata/后台/内容管理.yalm", FileType.YALM);
	}

	@Test(dataProvider = "initDataProvider")
	public void initTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_内容管理_a");
		pageAction.sleep(2);
		pageAction.click("内容管理_信息列表_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "createInformationProvider", dependsOnMethods = "initTest")
	public void createInformationTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("信息列表_信息发布_a");
		pageAction.sleep(2);
		pageAction.input("信息发布_标题_input", input.get("信息发布_标题"));
		pageAction.click("信息发布_类型_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("信息发布_类型列表_input", "信息发布_类型行_div",
				input.get("选择类型"));
		if (input.get("选择类型").equals("通知"))
		{
			pageAction.click("信息发布_选择接收对象_a");
			pageAction.sleep(2);
			if (input.get("选择接收对象").equals("全选"))
			{
				pageAction.click("信息发布_选择接收对象_全选_input");
				pageAction.click("信息发布_选择接收对象_河南省_span");
			}
			else if (input.get("选择接收对象").equals("老师"))
			{
				pageAction.click("信息发布_选择接收对象_老师_input");
				pageAction.click("信息发布_选择接收对象_河南省_span");
			}
			else if (input.get("选择接收对象").equals("学生"))
			{
				pageAction.click("信息发布_选择接收对象_学生_input");
				pageAction.click("信息发布_选择接收对象_河南省_span");
			}
			else if (input.get("选择接收对象").equals("家长"))
			{
				pageAction.click("信息发布_选择接收对象_家长_input");
				pageAction.click("信息发布_选择接收对象_河南省_span");
			}
			pageAction.click("信息发布_选择接收对象_确定_span");
		}
		pageAction.click("信息发布_图片_span");
		pageAction.sleep(2);
		pageAction.click("图片_网络图片_li");
		pageAction.sleep(0.5);
		pageAction.input("图片_图片地址_input", input.get("图片_图片地址"));
		pageAction.click("图片_确定_a");
		pageAction.selectFrame("信息发布_内容_iframe");
		pageAction.input("信息发布_内容_body", input.get("信息发布_内容"));
		pageAction.selectDefaultFrame();
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.sleep(2);
		pageAction.click("信息发布_发布_a");
		pageAction.sleep(2);
		pageAction.acceptAlert();
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("信息发布_标题")),
				"发布信息失败");
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("选择类型")),
				"发布信息失败");
		pageAction.sleep(2);
		pageAction.selectDefaultFrame();
		pageAction.click("内容管理_信息列表_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createInformationTest")
	public void reviewInformationTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		if (input.get("选择类型").equals("新闻"))
		{
			pageAction.click("信息列表_新闻_a");
			pageAction.sleep(2);
		}
		else if (input.get("选择类型").equals("公告"))
		{
			pageAction.click("信息列表_公告_a");
			pageAction.sleep(2);
		}
		else if (input.get("选择类型").equals("通知"))
		{
			pageAction.click("信息列表_通知_a");
			pageAction.sleep(2);
		}
		String newsIDSql = "select publish_news_id from publish_news where title = '"
				+ input.get("信息发布_标题") + "'";
		String newsID = new DbUnit("db.properties").getValue(newsIDSql, 0, 0);
		pageAction.clickAsAttributeValue("信息列表_详情_a", "onclick",
				"informationDetail('" + newsID + "')");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("信息发布_标题")),
				"查看信息失败");
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("选择类型")),
				"查看信息失败");
		pageAction.click("列表详情_返回_a");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "reviewInformationTest")
	public void deleteInformationTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		if (input.get("选择类型").equals("新闻"))
		{
			pageAction.click("信息列表_新闻_a");
			pageAction.sleep(2);
		}
		else if (input.get("选择类型").equals("公告"))
		{
			pageAction.click("信息列表_公告_a");
			pageAction.sleep(2);
		}
		else if (input.get("选择类型").equals("通知"))
		{
			pageAction.click("信息列表_通知_a");
			pageAction.sleep(2);
		}
		String newsIDSql = "select publish_news_id from publish_news where title = '"
				+ input.get("信息发布_标题") + "'";
		String newsID = new DbUnit("db.properties").getValue(newsIDSql, 0, 0);
		String newsTypeSql = "select news_type from publish_news where title = '"
				+ input.get("信息发布_标题") + "'";
		String newsnewsType = new DbUnit("db.properties").getValue(newsTypeSql, 0, 0);
		pageAction.clickAsAttributeValue("信息列表_删除_a", "onclick",
				"deleteComment('" + newsID + "','" + newsnewsType + "')");
		pageAction.sleep(2);
		pageAction.click("信息列表_对话框_确定_a");
		pageAction.sleep(2);
		pageAction.selectDefaultFrame();
		pageAction.click("内容管理_信息列表_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.sleep(2);
		if (input.get("选择类型").equals("新闻"))
		{
			pageAction.click("信息列表_新闻_a");
			pageAction.sleep(2);
		}
		else if (input.get("选择类型").equals("公告"))
		{
			pageAction.click("信息列表_公告_a");
			pageAction.sleep(2);
		}
		else if (input.get("选择类型").equals("通知"))
		{
			pageAction.click("信息列表_通知_a");
			pageAction.sleep(2);
		}
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("信息发布_标题")), "发布删除失败");
		pageAction.sleep(2);
	}

	@AfterClass
	public void afterClass()
	{
		pageAction.close();
	}

	@DataProvider
	public Object[][] initDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/内容管理.xls")
				.getInputAndExpectData("FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_1");
	}

	@DataProvider
	public Object[][] createInformationProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/内容管理.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_1",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_2",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_3",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_4");
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/内容管理.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_1",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_2",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_3");
	}
}
