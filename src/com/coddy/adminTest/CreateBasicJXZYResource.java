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

public class CreateBasicJXZYResource
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
				+ "/pagedata/后台/基础设置.yalm", FileType.YALM);
	}

	@Test(dataProvider = "initDataProvider")
	public void loginAndChangeFrameTest(HashMap<String, String> input)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_基础设置_a");
		pageAction.sleep(2);
		pageAction.click("基础设置_资源分类_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.sleep(2);
		pageAction.click("资源分类_教学资源_a");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "loginAndChangeFrameTest")
	public void createBasiResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("选择年级")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "')";
		String classlevelDisciplineid = new DbUnit("db.properties").getValue(
				classlevelDisciplineSql, 0, 0);
		pageAction.sleep(2);
		pageAction.clickSubElement("资源分类_列表行_tr", "DBID",
				classlevelDisciplineid, "资源分类_添加子类别_a");
		pageAction.sleep(2);
		pageAction.input("资源分类_子类别名称_input", input.get("资源分类_子类别名称"));
		pageAction.click("资源分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("资源分类_子类别名称")), "添加教学资源失败");
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
				+ "/testdata/后台/基础设置.xls")
				.getInputData("FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_1");
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_1",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_2",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_3",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_4",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_5",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_6",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_7",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_8");
	}
}
