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

public class BasicSJOperation
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
	public void createResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
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
		pageAction.click("资源分类_试卷_a");
		pageAction.sleep(2);
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("选择年级")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "')";
		String classlevelDisciplineid = new DbUnit("db.properties").getValue(
				classlevelDisciplineSql, 0, 0);
		pageAction.clickSubElement("资源分类_列表行_tr", "DBID",
				classlevelDisciplineid, "资源分类_添加子类别_a");
		pageAction.sleep(2);
		pageAction.input("资源分类_子类别名称_input", input.get("资源分类_子类别名称"));
		pageAction.click("资源分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("资源分类_子类别名称")), "添加试卷资源失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createResourceTest")
	public void deleteResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("选择年级")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "')";
		String classlevelDisciplineid = new DbUnit("db.properties").getValue(
				classlevelDisciplineSql, 0, 0);
		String resourceidSql = "select base_teach_catalog_id from base_teach_catalog where catalog_name ='"
				+ input.get("资源分类_子类别名称")
				+ "' and base_classlevel_discipline_id = '"
				+ classlevelDisciplineid + "'";
		String resourceid = new DbUnit("db.properties").getValue(resourceidSql, 0, 0);
		pageAction.clickSubElement("资源分类_列表行_tr", "DBID", resourceid,
				"资源分类_删除_a");
		pageAction.sleep(2);
		pageAction.click("资源分类_对话框_确定_a");
		pageAction.sleep(2);
		String resourceCountSql = "select count(base_teach_catalog_id) from base_teach_catalog where catalog_name ='"
				+ input.get("资源分类_子类别名称")
				+ "' and base_classlevel_discipline_id = '"
				+ classlevelDisciplineid + "'";
		String resourceCount = new DbUnit("db.properties").getValue(resourceCountSql, 0, 0);
		ActionCheck.verfiyEquals(resourceCount, "0", "删除试卷失败");
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
				.getInputAndExpectData("FUN_HT_YYSHT_JCSZ_ZYFL_SJ_003_1");
	}
}
