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

public class CreateBasicSet
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

	@Test(dataProvider = "loginDataProvider")
	public void loginTestAndChangeFrame(HashMap<String, String> input)
	{
		pageAction.openUrl(url);
		pageAction.input("登陆_用户名_input", input.get("登陆_用户名"));
		pageAction.input("登陆_密码_input", input.get("登陆_密码"));
		pageAction.click("登陆_登陆_input");
		pageAction.sleep(2);
		pageAction.click("公共_基础设置_a");
		pageAction.sleep(2);
		pageAction.click("基础设置_基本分类_a");
		pageAction.sleep(2);
		pageAction.selectFrame("公共_Frame_iframe");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "semesterDataProvider", dependsOnMethods = "loginTestAndChangeFrame")
	public void createSemesterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("基本分类_添加学段_a");
		pageAction.input("添加学段_学段名称_input", input.get("添加学段_学段名称"));
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String sql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("添加学段_学段名称") + "'";
		String id = new DbUnit("db.properties").getValue(sql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("学段年级学科管理_列表行_tr", "DBID", id).getText(),
				expect.get("添加学段_学段名称"), "添加学段失败");
	}

	@Test(dataProvider = "classlevelDataProvider", dependsOnMethods = "createSemesterTest")
	public void createClasslevelTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("选择学段") + "'";
		String semesterid = new DbUnit("db.properties").getValue(semesterSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", semesterid,
				"学段年级学科管理_添加年级_a");
		pageAction.sleep(2);
		pageAction.input("添加年级_年级名称_input", input.get("添加年级_年级名称"));
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String classlevelSql = "select base_classlevel_id from base_classlevel where base_semester_id = '"
				+ semesterid
				+ "' and classlevel_name = '"
				+ input.get("添加年级_年级名称") + "'";
		String classlevelid = new DbUnit("db.properties").getValue(classlevelSql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("学段年级学科管理_列表行_tr", "DBID", classlevelid)
						.getText(), expect.get("添加年级_年级名称"), "添加年级失败");
	}

	@Test(dataProvider = "disciplineDataProvider", dependsOnMethods = "createClasslevelTest")
	public void createDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classleveSql = "select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("选择年级") + "'";
		String classleveid = new DbUnit("db.properties").getValue(classleveSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", classleveid,
				"学段年级学科管理_添加学科_a");
		pageAction.sleep(2);
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择语文学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择数学学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择英语学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择生物学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择历史学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择美术学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择信息技术学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择地理学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择政治学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择音乐学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择品德学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择体育学科"));
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择科学学科"));
		pageAction.click("添加学科_确定_a");
		pageAction.sleep(2);
		String disciplineCountSql = "select count(1) from base_classlevel_discipline where base_classlevel_id = '"
				+ classleveid + "'";
		String disciplineCount = new DbUnit("db.properties").getValue(disciplineCountSql, 0,
				0);
		ActionCheck.verfiyEquals(disciplineCount, expect.get("添加学科数量"),
				"添加学科失败");
	}

	@Test(dataProvider = "volumeDataProvider", dependsOnMethods = "createDisciplineTest")
	public void createVolumeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("选择年级")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "')";
		String classlevelDisciplineid = new DbUnit("db.properties").getValue(
				classlevelDisciplineSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID",
				classlevelDisciplineid, "学段年级学科管理_添加分册_a");
		pageAction.sleep(2);
		pageAction.input("添加分册_分册名称_input", input.get("添加分册_分册名称"));
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String volumeSql = "select base_volume_id from base_volume where volume_name = '"
				+ input.get("添加分册_分册名称")
				+ "' and base_classlevel_discipline_id = '"
				+ classlevelDisciplineid + "'";
		String volumeid = new DbUnit("db.properties").getValue(volumeSql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("学段年级学科管理_列表行_tr", "DBID", volumeid)
						.getText(), expect.get("添加分册_分册名称"), "添加分册失败");
	}

	@Test(dataProvider = "chapterDataProvider", dependsOnMethods = "createVolumeTest")
	public void createChapterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String volumeSql = "select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("选择年级")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科")
				+ "')) and volume_name = '"
				+ input.get("选择分册") + "'";
		String volumeid = new DbUnit("db.properties").getValue(volumeSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", volumeid,
				"学段年级学科管理_添加章节_a");
		pageAction.sleep(2);
		pageAction.input("添加章节_章节名称_input", input.get("添加章节_章节名称"));
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String chapterSql = "select base_chapter_id from base_chapter where chapter_name = '"
				+ input.get("添加章节_章节名称")
				+ "' and base_volume_id = '"
				+ volumeid + "'";
		String chapterid = new DbUnit("db.properties").getValue(chapterSql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("学段年级学科管理_列表行_tr", "DBID", chapterid)
						.getText(), expect.get("添加章节_章节名称"), "添加章节失败");
	}

	@AfterClass
	public void afterClass()
	{
		pageAction.close();
	}

	@DataProvider
	public Object[][] loginDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls")
				.getInputData("FUN_HT_YYSHT_JCSZ_JCFL_XD_001_1");
	}

	@DataProvider
	public Object[][] semesterDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_JCFL_XD_001_1",
				"FUN_HT_YYSHT_JCSZ_JCFL_XD_001_2");
	}

	@DataProvider
	public Object[][] classlevelDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_1",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_2",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_3",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_4",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_5",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_6",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_7",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_8",
				"FUN_HT_YYSHT_JCSZ_JCFL_NJ_001_9");
	}

	@DataProvider
	public Object[][] disciplineDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_1",
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_2",
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_3",
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_4");
	}

	@DataProvider
	public Object[][] volumeDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_1",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_2",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_3",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_4",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_5",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_6",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_7",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_8",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_9",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_10",
				"FUN_HT_YYSHT_JCSZ_JCFL_FC_001_11");
	}

	@DataProvider
	public Object[][] chapterDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/后台/基础设置.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_1",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_2",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_3",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_4",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_5",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_6",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_7",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_8",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_9",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_10",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_11",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_12",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_13",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_14",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_15",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_16",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_17",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_18",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_19",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_20",
				"FUN_HT_YYSHT_JCSZ_JCFL_ZJ_001_21");
	}

}
