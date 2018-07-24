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
				+ "/pagedata/��̨/����.yalm", FileType.YALM);
		pageAction.loadPageData(System.getProperty("user.dir")
				+ "/pagedata/��̨/��������.yalm", FileType.YALM);
	}

	@Test(dataProvider = "initDataProvider")
	public void loginAndChangeFrameTest(HashMap<String, String> input)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_��������_a");
		pageAction.sleep(2);
		pageAction.click("��������_��Դ����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.sleep(2);
		pageAction.click("��Դ����_��ѧ��Դ_a");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "loginAndChangeFrameTest")
	public void createBasiResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String classlevelDisciplineSql = "select base_classlevel_discipline_id from base_classlevel_discipline where base_classlevel_id = (select base_classlevel_id from base_classlevel where classlevel_name = '"
				+ input.get("ѡ���꼶")
				+ "') and base_discipline_id = (select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��") + "')";
		String classlevelDisciplineid = new DbUnit("db.properties").getValue(
				classlevelDisciplineSql, 0, 0);
		pageAction.sleep(2);
		pageAction.clickSubElement("��Դ����_�б���_tr", "DBID",
				classlevelDisciplineid, "��Դ����_��������_a");
		pageAction.sleep(2);
		pageAction.input("��Դ����_���������_input", input.get("��Դ����_���������"));
		pageAction.click("��Դ����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("��Դ����_���������")), "��ӽ�ѧ��Դʧ��");
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
				+ "/testdata/��̨/��������.xls")
				.getInputData("FUN_HT_YYSHT_JCSZ_ZYFL_JXZY_001_1");
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
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
