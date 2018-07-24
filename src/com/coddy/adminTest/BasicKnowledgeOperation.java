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

public class BasicKnowledgeOperation
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

	@Test(dataProvider = "testDataProvider")
	public void createKnowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
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
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("选择学段") + "'";
		String semesterid = new DbUnit("db.properties").getValue(semesterSql, 0, 0);
		String disciplineSql = "select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "'";
		String disciplineid = new DbUnit("db.properties").getValue(disciplineSql, 0, 0);
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

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createKnowledgeTest")
	public void createSubknowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String knowledgeSql = "select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("选择知识点") + "' and parent_id = 'ROOT'";
		String knowledgeid = new DbUnit("db.properties").getValue(knowledgeSql, 0, 0);
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

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createSubknowledgeTest")
	public void deleteSubknowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String subknowledgeSql = "select base_knowledge_id from base_knowledge where parent_id in (select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("选择知识点") + "' and parent_id = 'ROOT')";
		String subknowledgeid = new DbUnit("db.properties").getValue(subknowledgeSql, 0, 0);
		pageAction.clickSubElement("知识点管理_学科列表行_tr", "DBID", subknowledgeid,
				"知识点管理_删除_a");
		pageAction.sleep(2);
		pageAction.click("知识点管理_对话框_确定_a");
		pageAction.sleep(2);
		String subknowledgeCountSql = "select count(base_knowledge_id) from base_knowledge where parent_id in (select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("选择知识点") + "' and parent_id = 'ROOT')";
		String subknowledgeCount = new DbUnit("db.properties").getValue(subknowledgeCountSql,
				0, 0);
		ActionCheck.verfiyEquals(subknowledgeCount, "0", "删除子知识点失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteSubknowledgeTest")
	public void deleteknowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String knowledgeSql = "select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("选择知识点") + "' and parent_id = 'ROOT'";
		String knowledgeid = new DbUnit("db.properties").getValue(knowledgeSql, 0, 0);
		pageAction.clickSubElement("知识点管理_学科列表行_tr", "DBID", knowledgeid,
				"知识点管理_删除_a");
		pageAction.sleep(2);
		pageAction.click("知识点管理_对话框_确定_a");
		pageAction.sleep(2);
		String knowledgeCountSql = "select count(base_knowledge_id) from base_knowledge where knowledge_name = '"
				+ input.get("选择知识点") + "' and parent_id = 'ROOT'";
		String knowledgeCount = new DbUnit("db.properties").getValue(knowledgeCountSql, 0, 0);
		ActionCheck.verfiyEquals(knowledgeCount, "0", "删除子知识点失败");
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
				+ "/testdata/后台/基础设置.xls")
				.getInputAndExpectData("FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_003_1");
	}
}
