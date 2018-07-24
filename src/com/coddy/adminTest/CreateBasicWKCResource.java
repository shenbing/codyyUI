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

public class CreateBasicWKCResource
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
		pageAction.click("��Դ����_΢�γ�_a");
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
		ActionCheck
				.verfiyTrue(pageAction.isTextPresent2(input.get("��Դ����_���������")),
						"���΢�γ���Դʧ��");
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
				.getInputData("FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_1");
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_1",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_2",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_3",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_4",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_5",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_6",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_7",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_8",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_9",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_10",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_11",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_12",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_13",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_14",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_15",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_16",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_17",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_18",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_19",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_20",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_21",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_22",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_23",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_24",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_25",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_26",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_27",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_28",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_29",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_30",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_31",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_32",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_33",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_34",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_35",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_36",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_37",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_38",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_39",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_40",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_41",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_42",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_43",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_44",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_45",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_46",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_47",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_48",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_49",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_50",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_51",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_52",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_53",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_54",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_55",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_56",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_57",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_58",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_59",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_60",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_61",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_62",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_63",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_64",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_65",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_66",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_67",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_68",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_69",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_70",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_71",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_72",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_73",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_74",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_75",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_76",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_77",
				"FUN_HT_YYSHT_JCSZ_ZYFL_WK_001_78");
	}
}
