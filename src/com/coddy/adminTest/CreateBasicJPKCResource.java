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

public class CreateBasicJPKCResource
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
		pageAction.click("资源分类_精品课程_a");
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
				pageAction.isTextPresent2(input.get("资源分类_子类别名称")), "添加精品资源失败");
	}

	// @Test(dataProvider = "testDataProvider", dependsOnMethods =
	// "createBasiResourceTest")
	// public void createBasiSubResourceTest(HashMap<String, String> input,
	// HashMap<String, String> expect)
	// {
	// pageAction.click("资源分类_精品课程_a");
	// pageAction.sleep(2);
	// String catalogSql =
	// "select base_teach_catalog_id from base_teach_catalog where catalog_name = '"
	// + input.get("选择资源")
	// +
	// "' and base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
	// + input.get("选择年级")
	// +
	// "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
	// + input.get("选择学科") + "'))";
	// String catalogid = new DbUnit("db.properties").getValue(
	// catalogSql, 0, 0);
	// pageAction.clickSubElement("资源分类_列表行_tr", "DBID",
	// catalogid, "资源分类_添加子类别_a");
	// pageAction.sleep(2);
	// pageAction.input("资源分类_子类别名称_input", input.get("资源分类_子类别名称"));
	// pageAction.click("资源分类_对话框_确定_a");
	// pageAction.sleep(2);
	// ActionCheck.verfiyTrue(
	// pageAction.isTextPresent2(input.get("资源分类_子类别名称")), "添加精品资源失败");
	// }

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
				.getInputData("FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_1");
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_1",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_2",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_3",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_4",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_5",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_6",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_7",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_8",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_9",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_10",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_11",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_12",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_13",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_14",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_15",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_16",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_17",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_18",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_19",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_20",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_21",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_22",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_23",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_24",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_25",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_26",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_27",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_28",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_29",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_30",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_31",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_32",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_33",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_34",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_35",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_36",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_37",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_38",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_39",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_40",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_41",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_42",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_43",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_44",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_45",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_46",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_47",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_48",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_49",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_50",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_51",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_52",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_53",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_54",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_55",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_56",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_57",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_58",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_59",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_60",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_61",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_62",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_63",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_64",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_65",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_66",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_67",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_68",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_69",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_70",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_71",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_72",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_73",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_74",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_75",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_76",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_77",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_78",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_79",
				"FUN_HT_YYSHT_JCSZ_ZYFL_JPK_001_80");
	}
}
