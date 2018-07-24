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
				+ "/pagedata/��̨/����.yalm", FileType.YALM);
		pageAction.loadPageData(System.getProperty("user.dir")
				+ "/pagedata/��̨/��������.yalm", FileType.YALM);
	}

	@Test(dataProvider = "testDataProvider")
	public void createDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_��������_a");
		pageAction.sleep(2);
		pageAction.click("��������_��������_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.sleep(2);
		pageAction.click("��������_ѧ�ƹ���_a");
		pageAction.sleep(2);
		pageAction.click("ѧ�ƹ���_���ѧ��_a");
		pageAction.input("ѧ�ƹ���_���ѧ��_ѧ������_input", input.get("ѧ�ƹ���_���ѧ��_ѧ������"));
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("ѧ�ƹ���_���ѧ��_ѧ������")),
				"���ѧ��ʧ��");
		pageAction.click("ѧ�ƹ���_����_a");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "testDataProvider")
	public void createSemesterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("��������_���ѧ��_a");
		pageAction.input("���ѧ��_ѧ������_input", input.get("���ѧ��_ѧ������"));
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String sql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("���ѧ��_ѧ������") + "'";
		String id = new DbUnit("db.properties").getValue(sql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", id).getText(),
				input.get("���ѧ��_ѧ������"), "���ѧ��ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createSemesterTest")
	public void createClasslevelTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("���ѧ��_ѧ������") + "'";
		String semesterid = new DbUnit("db.properties").getValue(semesterSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", semesterid,
				"ѧ���꼶ѧ�ƹ���_����꼶_a");
		pageAction.sleep(2);
		pageAction.input("����꼶_�꼶����_input", input.get("����꼶_�꼶����"));
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String classlevelSql = "select base_classlevel_id from base_classlevel where base_semester_id = '"
				+ semesterid
				+ "' and classlevel_name = '"
				+ input.get("����꼶_�꼶����") + "'";
		String classlevelid = new DbUnit("db.properties").getValue(classlevelSql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", classlevelid)
						.getText(), input.get("����꼶_�꼶����"), "����꼶ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createClasslevelTest")
	public void addDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classleveSql = "select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����") + "'";
		String classleveid = new DbUnit("db.properties").getValue(classleveSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", classleveid,
				"ѧ���꼶ѧ�ƹ���_���ѧ��_a");
		pageAction.sleep(2);
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ��ѧ��"));
		pageAction.click("���ѧ��_ȷ��_a");
		pageAction.sleep(2);
		String disciplineCountSql = "select count(1) from base_classlevel_discipline where base_classlevel_id = '"
				+ classleveid + "'";
		String disciplineCount = new DbUnit("db.properties").getValue(disciplineCountSql, 0,
				0);
		ActionCheck.verfiyEquals(disciplineCount, expect.get("���ѧ������"),
				"���ѧ��ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "addDisciplineTest")
	public void createVolumeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��") + "')";
		String classlevelDisciplineid = new DbUnit("db.properties").getValue(
				classlevelDisciplineSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID",
				classlevelDisciplineid, "ѧ���꼶ѧ�ƹ���_��ӷֲ�_a");
		pageAction.sleep(2);
		pageAction.input("��ӷֲ�_�ֲ�����_input", input.get("��ӷֲ�_�ֲ�����"));
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String volumeSql = "select base_volume_id from base_volume where volume_name = '"
				+ input.get("��ӷֲ�_�ֲ�����")
				+ "' and base_classlevel_discipline_id = '"
				+ classlevelDisciplineid + "'";
		String volumeid = new DbUnit("db.properties").getValue(volumeSql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", volumeid)
						.getText(), input.get("��ӷֲ�_�ֲ�����"), "��ӷֲ�ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createVolumeTest")
	public void createChapterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String volumeSql = "select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��")
				+ "')) and volume_name = '"
				+ input.get("��ӷֲ�_�ֲ�����") + "'";
		String volumeid = new DbUnit("db.properties").getValue(volumeSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", volumeid,
				"ѧ���꼶ѧ�ƹ���_����½�_a");
		pageAction.sleep(2);
		pageAction.input("����½�_�½�����_input", input.get("����½�_�½�����"));
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String chapterSql = "select base_chapter_id from base_chapter where chapter_name = '"
				+ input.get("����½�_�½�����")
				+ "' and base_volume_id = '"
				+ volumeid + "'";
		String chapterid = new DbUnit("db.properties").getValue(chapterSql, 0, 0);
		ActionCheck.verfiyContains(
				pageAction.getElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", chapterid)
						.getText(), input.get("����½�_�½�����"), "����½�ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createChapterTest")
	public void deleteChapterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String chapterSql = "select base_chapter_id from base_chapter where chapter_name = '"
				+ input.get("����½�_�½�����")
				+ "' and base_volume_id in (select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��")
				+ "')) and volume_name = '"
				+ input.get("��ӷֲ�_�ֲ�����") + "')";
		String chaperid = new DbUnit("db.properties").getValue(chapterSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", chaperid,
				"��������_ɾ��_a");
		pageAction.sleep(2);
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String chapterCountSql = "select count(base_chapter_id) from base_chapter where chapter_name = '"
				+ input.get("����½�_�½�����")
				+ "' and base_volume_id in (select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��")
				+ "')) and volume_name = '"
				+ input.get("��ӷֲ�_�ֲ�����") + "')";
		String chapterCount = new DbUnit("db.properties").getValue(chapterCountSql, 0, 0);
		ActionCheck.verfiyEquals(chapterCount, "0", "ɾ���½�ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteChapterTest")
	public void deleteVolumeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String volumeSql = "select base_volume_id from base_volume where volume_name = '"
				+ input.get("��ӷֲ�_�ֲ�����")
				+ "' and base_classlevel_discipline_id in (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��") + "'))";
		String volumeid = new DbUnit("db.properties").getValue(volumeSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", volumeid,
				"��������_ɾ��_a");
		pageAction.sleep(2);
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String volumeCountSql = "select count(base_volume_id) from base_volume where volume_name = '"
				+ input.get("��ӷֲ�_�ֲ�����")
				+ "' and base_classlevel_discipline_id in (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��") + "'))";
		String volumeCount = new DbUnit("db.properties").getValue(volumeCountSql, 0, 0);
		ActionCheck.verfiyEquals(volumeCount, "0", "ɾ���ֲ�ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteVolumeTest")
	public void removeDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String disciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��") + "')";
		String disciplineid = new DbUnit("db.properties").getValue(disciplineSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", disciplineid,
				"��������_ɾ��_a");
		pageAction.sleep(2);
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String disciplineCountSql = "select count(base_classlevel_discipline_id) from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("����꼶_�꼶����")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��") + "')";
		String disciplineCount = new DbUnit("db.properties").getValue(disciplineCountSql, 0,
				0);
		ActionCheck.verfiyEquals(disciplineCount, "0", "ɾ��ѧ��ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "removeDisciplineTest")
	public void deleteClasslevelTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelSql = "select base_classlevel_id from base_classlevel where base_semester_id in (select base_semester_id from base_semester where semester_name = '"
				+ input.get("���ѧ��_ѧ������") + "')";
		String classlevelid = new DbUnit("db.properties").getValue(classlevelSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", classlevelid,
				"��������_ɾ��_a");
		pageAction.sleep(2);
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String classlevelCountSql = "select count(base_classlevel_id) from base_classlevel where base_semester_id in (select base_semester_id from base_semester where semester_name = '"
				+ input.get("���ѧ��_ѧ������") + "')";
		String classlevelCount = new DbUnit("db.properties").getValue(classlevelCountSql, 0,
				0);
		ActionCheck.verfiyEquals(classlevelCount, "0", "ɾ���꼶ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteClasslevelTest")
	public void deleteSemesterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("���ѧ��_ѧ������") + "'";
		String semesterid = new DbUnit("db.properties").getValue(semesterSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", semesterid,
				"��������_ɾ��_a");
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String sql = "select count(base_semester_id) from base_semester where semester_name = '"
				+ input.get("���ѧ��_ѧ������") + "'";
		String count = new DbUnit("db.properties").getValue(sql, 0, 0);
		ActionCheck.verfiyEquals(count, "0", "ɾ��ѧ��ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteSemesterTest")
	public void deleteDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("��������_ѧ�ƹ���_a");
		pageAction.sleep(2);
		pageAction.clickSubElementContainsText("ѧ�ƹ���_�б���_tag", "��������_ɾ��_a",
				input.get("ѧ�ƹ���_���ѧ��_ѧ������"));
		pageAction.click("��������_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("ѧ�ƹ���_���ѧ��_ѧ������")),
				"ɾ��ѧ��ʧ��");
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
				+ "/testdata/��̨/��������.xls")
				.getInputAndExpectData("FUN_HT_YYSHT_JCSZ_XKGL_004_1");
	}
}
