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
				+ "/pagedata/��̨/����.yalm", FileType.YALM);
		pageAction.loadPageData(System.getProperty("user.dir")
				+ "/pagedata/��̨/��������.yalm", FileType.YALM);
	}

	@Test(dataProvider = "loginDataProvider")
	public void loginTestAndChangeFrame(HashMap<String, String> input)
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
	}

	@Test(dataProvider = "semesterDataProvider", dependsOnMethods = "loginTestAndChangeFrame")
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
				expect.get("���ѧ��_ѧ������"), "���ѧ��ʧ��");
	}

	@Test(dataProvider = "classlevelDataProvider", dependsOnMethods = "createSemesterTest")
	public void createClasslevelTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("ѡ��ѧ��") + "'";
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
						.getText(), expect.get("����꼶_�꼶����"), "����꼶ʧ��");
	}

	@Test(dataProvider = "disciplineDataProvider", dependsOnMethods = "createClasslevelTest")
	public void createDisciplineTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classleveSql = "select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("ѡ���꼶") + "'";
		String classleveid = new DbUnit("db.properties").getValue(classleveSql, 0, 0);
		pageAction.clickSubElement("ѧ���꼶ѧ�ƹ���_�б���_tr", "DBID", classleveid,
				"ѧ���꼶ѧ�ƹ���_���ѧ��_a");
		pageAction.sleep(2);
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ������ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ����ѧѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ��Ӣ��ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ������ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ����ʷѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ������ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ����Ϣ����ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ�����ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ������ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ������ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ��Ʒ��ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ������ѧ��"));
		pageAction.clickSubElementAsText("���ѧ��_ѧ�Ʊ�_table", "���ѧ��_ѧ�Ʊ���_tr",
				input.get("ѡ���ѧѧ��"));
		pageAction.click("���ѧ��_ȷ��_a");
		pageAction.sleep(2);
		String disciplineCountSql = "select count(1) from base_classlevel_discipline where base_classlevel_id = '"
				+ classleveid + "'";
		String disciplineCount = new DbUnit("db.properties").getValue(disciplineCountSql, 0,
				0);
		ActionCheck.verfiyEquals(disciplineCount, expect.get("���ѧ������"),
				"���ѧ��ʧ��");
	}

	@Test(dataProvider = "volumeDataProvider", dependsOnMethods = "createDisciplineTest")
	public void createVolumeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("ѡ���꼶")
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
						.getText(), expect.get("��ӷֲ�_�ֲ�����"), "��ӷֲ�ʧ��");
	}

	@Test(dataProvider = "chapterDataProvider", dependsOnMethods = "createVolumeTest")
	public void createChapterTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String volumeSql = "select base_volume_id from base_volume where base_classlevel_discipline_id = (select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("ѡ���꼶")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��")
				+ "')) and volume_name = '"
				+ input.get("ѡ��ֲ�") + "'";
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
						.getText(), expect.get("����½�_�½�����"), "����½�ʧ��");
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
				+ "/testdata/��̨/��������.xls")
				.getInputData("FUN_HT_YYSHT_JCSZ_JCFL_XD_001_1");
	}

	@DataProvider
	public Object[][] semesterDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_JCFL_XD_001_1",
				"FUN_HT_YYSHT_JCSZ_JCFL_XD_001_2");
	}

	@DataProvider
	public Object[][] classlevelDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
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
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_1",
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_2",
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_3",
				"FUN_HT_YYSHT_JCSZ_JCFL_XK_001_4");
	}

	@DataProvider
	public Object[][] volumeDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
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
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
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
