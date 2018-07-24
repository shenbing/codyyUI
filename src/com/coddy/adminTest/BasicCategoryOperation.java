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

public class BasicCategoryOperation
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
	public void createDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
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
		pageAction.click("基本分类_学科管理_a");
		pageAction.sleep(2);
		pageAction.click("学科管理_添加学科_a");
		pageAction.input("学科管理_添加学科_学科名称_input", input.get("学科管理_添加学科_学科名称"));
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("学科管理_添加学科_学科名称")),
				"添加学段失败");
		pageAction.click("学科管理_返回_a");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "testDataProvider")
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
				input.get("添加学段_学段名称"), "添加学段失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createSemesterTest")
	public void createClasslevelTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("添加学段_学段名称") + "'";
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
						.getText(), input.get("添加年级_年级名称"), "添加年级失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createClasslevelTest")
	public void addDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classleveSql = "select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称") + "'";
		String classleveid = new DbUnit("db.properties").getValue(classleveSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", classleveid,
				"学段年级学科管理_添加学科_a");
		pageAction.sleep(2);
		pageAction.clickSubElementAsText("添加学科_学科表_table", "添加学科_学科表行_tr",
				input.get("选择学科"));
		pageAction.click("添加学科_确定_a");
		pageAction.sleep(2);
		String disciplineCountSql = "select count(1) from base_classlevel_discipline where base_classlevel_id = '"
				+ classleveid + "'";
		String disciplineCount = new DbUnit("db.properties").getValue(disciplineCountSql, 0,
				0);
		ActionCheck.verfiyEquals(disciplineCount, expect.get("添加学科数量"),
				"添加学科失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "addDisciplineTest")
	public void createVolumeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
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
						.getText(), input.get("添加分册_分册名称"), "添加分册失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createVolumeTest")
	public void createChapterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String volumeSql = "select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科")
				+ "')) and volume_name = '"
				+ input.get("添加分册_分册名称") + "'";
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
						.getText(), input.get("添加章节_章节名称"), "添加章节失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createChapterTest")
	public void deleteChapterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String chapterSql = "select base_chapter_id from base_chapter where chapter_name = '"
				+ input.get("添加章节_章节名称")
				+ "' and base_volume_id in (select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科")
				+ "')) and volume_name = '"
				+ input.get("添加分册_分册名称") + "')";
		String chaperid = new DbUnit("db.properties").getValue(chapterSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", chaperid,
				"基本分类_删除_a");
		pageAction.sleep(2);
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String chapterCountSql = "select count(base_chapter_id) from base_chapter where chapter_name = '"
				+ input.get("添加章节_章节名称")
				+ "' and base_volume_id in (select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科")
				+ "')) and volume_name = '"
				+ input.get("添加分册_分册名称") + "')";
		String chapterCount = new DbUnit("db.properties").getValue(chapterCountSql, 0, 0);
		ActionCheck.verfiyEquals(chapterCount, "0", "删除章节失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteChapterTest")
	public void deleteVolumeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String volumeSql = "select base_volume_id from base_volume where volume_name = '"
				+ input.get("添加分册_分册名称")
				+ "' and base_classlevel_discipline_id in (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "'))";
		String volumeid = new DbUnit("db.properties").getValue(volumeSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", volumeid,
				"基本分类_删除_a");
		pageAction.sleep(2);
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String volumeCountSql = "select count(base_volume_id) from base_volume where volume_name = '"
				+ input.get("添加分册_分册名称")
				+ "' and base_classlevel_discipline_id in (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "'))";
		String volumeCount = new DbUnit("db.properties").getValue(volumeCountSql, 0, 0);
		ActionCheck.verfiyEquals(volumeCount, "0", "删除分册失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteVolumeTest")
	public void removeDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String disciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "')";
		String disciplineid = new DbUnit("db.properties").getValue(disciplineSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", disciplineid,
				"基本分类_删除_a");
		pageAction.sleep(2);
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String disciplineCountSql = "select count(base_classlevel_discipline_id) from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("添加年级_年级名称")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("选择学科") + "')";
		String disciplineCount = new DbUnit("db.properties").getValue(disciplineCountSql, 0,
				0);
		ActionCheck.verfiyEquals(disciplineCount, "0", "删除学科失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "removeDisciplineTest")
	public void deleteClasslevelTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelSql = "select base_classlevel_id from base_classlevel where base_semester_id in (select base_semester_id from base_semester where semester_name = '"
				+ input.get("添加学段_学段名称") + "')";
		String classlevelid = new DbUnit("db.properties").getValue(classlevelSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", classlevelid,
				"基本分类_删除_a");
		pageAction.sleep(2);
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String classlevelCountSql = "select count(base_classlevel_id) from base_classlevel where base_semester_id in (select base_semester_id from base_semester where semester_name = '"
				+ input.get("添加学段_学段名称") + "')";
		String classlevelCount = new DbUnit("db.properties").getValue(classlevelCountSql, 0,
				0);
		ActionCheck.verfiyEquals(classlevelCount, "0", "删除年级失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteClasslevelTest")
	public void deleteSemesterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("添加学段_学段名称") + "'";
		String semesterid = new DbUnit("db.properties").getValue(semesterSql, 0, 0);
		pageAction.clickSubElement("学段年级学科管理_列表行_tr", "DBID", semesterid,
				"基本分类_删除_a");
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		String sql = "select count(base_semester_id) from base_semester where semester_name = '"
				+ input.get("添加学段_学段名称") + "'";
		String count = new DbUnit("db.properties").getValue(sql, 0, 0);
		ActionCheck.verfiyEquals(count, "0", "删除学段失败");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteSemesterTest")
	public void deleteDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("基本分类_学科管理_a");
		pageAction.sleep(2);
		pageAction.clickSubElementContainsText("学科管理_列表行_tag", "基本分类_删除_a",
				input.get("学科管理_添加学科_学科名称"));
		pageAction.click("基本分类_对话框_确定_a");
		pageAction.sleep(2);
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("学科管理_添加学科_学科名称")),
				"删除学段失败");
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
				.getInputAndExpectData("FUN_HT_YYSHT_JCSZ_XKGL_004_1");
	}
}
