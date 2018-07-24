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

public class CreateBasicKnowledge
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
		pageAction.click("基础设置_知识点分类_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "knowledgeDataProvider", dependsOnMethods = "loginAndChangeFrameTest")
	public void createBasicKnowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("选择学段") + "'";
		String semesterid = new DbUnit("db.properties").getValue(semesterSql, 0, 0);
		pageAction.sleep(2);
		String disciplineSql = "select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "'";
		String disciplineid = new DbUnit("db.properties").getValue(disciplineSql, 0, 0);
		pageAction.sleep(2);
		String userID = semesterid + "-" + disciplineid;
		pageAction.clickSubElement("知识点管理_学科列表行_tr", "DBID", userID,
				"知识点管理_添加知识点_a");
		pageAction.sleep(2);
		pageAction.input("知识点管理_知识点名称_input", input.get("知识点管理_知识点名称"));
		pageAction.click("知识点管理_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("知识点管理_知识点名称")), "添加知识点失败");
	}

	@Test(dataProvider = "subknowledgeDataProvider", dependsOnMethods = "createBasicKnowledgeTest")
	public void createBasicSubknowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String knowledgeSql = "select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("选择知识点") + "' and parent_id = 'ROOT'";
		String knowledgeid = new DbUnit("db.properties").getValue(knowledgeSql, 0, 0);
		pageAction.sleep(2);
		pageAction.clickSubElement("知识点管理_学科列表行_tr", "DBID", knowledgeid,
				"知识点管理_添加子知识点_a");
		pageAction.sleep(2);
		pageAction.input("知识点管理_知识点名称_input", input.get("知识点管理_子知识点名称"));
		pageAction.click("知识点管理_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("知识点管理_子知识点名称")),
				"添加子知识点失败");
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
				.getInputData("FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_1");
	}

	@DataProvider
	public Object[][] knowledgeDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_1",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_2",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_3",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_4",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_5",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_6",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_7",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_8",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_9",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_10",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_11",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_12",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_13",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_14",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_15",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_17",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_18",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_19",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_20",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_21",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_22",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_23",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_24",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_25",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_001_26");
	}

	@DataProvider
	public Object[][] subknowledgeDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_1",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_2",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_3",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_4",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_5",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_6",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_7",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_8",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_9",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_10",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_11",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_12",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_13",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_14",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_15",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_16",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_17",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_18",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_19",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_20",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_21",
				"FUN_HT_YYSHT_JCSZ_ZSDFL_TJZZSD_001_22");
	}
}
