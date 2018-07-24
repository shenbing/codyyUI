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

public class BasicQSYKOperation
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
		pageAction.click("资源分类_轻松一刻_a");
		pageAction.sleep(2);
		pageAction.click("轻松一刻_添加根类别_a");
		pageAction.sleep(2);
		pageAction.input("轻松一刻_根类别名称_input", input.get("资源分类_根类别名称"));
		pageAction.click("资源分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("资源分类_根类别名称")),
				"添加轻松一刻资源失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createResourceTest")
	public void createSubResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String qsykSql = "select base_catalog_id from base_catalog where parent_id = 'ROOT' and catalog_name = '"
				+ input.get("资源分类_根类别名称") + "'";
		String qsykID = new DbUnit("db.properties").getValue(qsykSql, 0, 0);
		pageAction.clickSubElement("资源分类_列表行_tr", "DBID", qsykID,
				"资源分类_添加子类别_a");
		pageAction.sleep(2);
		pageAction.input("资源分类_子类别名称_input", input.get("资源分类_子类别名称"));
		pageAction.click("资源分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("资源分类_子类别名称")),
				"添加轻松一刻子类别资源失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createSubResourceTest")
	public void deleteSubResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String qsykSubSql = "select base_catalog_id from base_catalog where catalog_name = '"
				+ input.get("资源分类_子类别名称")
				+ "' and parent_id = ("
				+ "select base_catalog_id from base_catalog where parent_id = 'ROOT' and catalog_name = '"
				+ input.get("资源分类_根类别名称") + "')";
		String qsykSubID = new DbUnit("db.properties").getValue(qsykSubSql, 0, 0);
		pageAction.clickSubElement("资源分类_列表行_tr", "DBID", qsykSubID,
				"资源分类_删除_a");
		pageAction.click("资源分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("资源分类_子类别名称")),
				"删除轻松一刻子类别失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteSubResourceTest")
	public void deleteRootResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String qsykRootSql = "select base_catalog_id from base_catalog where parent_id = 'ROOT' and catalog_name = '"
				+ input.get("资源分类_根类别名称") + "'";
		String qsykRootID = new DbUnit("db.properties").getValue(qsykRootSql, 0, 0);
		pageAction.clickSubElement("资源分类_列表行_tr", "DBID", qsykRootID,
				"资源分类_删除_a");
		pageAction.click("资源分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("资源分类_根类别名称")),
				"删除轻松一刻根类别失败");
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
				.getInputAndExpectData("FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_003_1");
	}
}
