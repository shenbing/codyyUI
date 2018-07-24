package com.coddy.adminTest;

import java.util.HashMap;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import com.coddy.check.ActionCheck;
import com.coddy.spring.BrowserType;
import com.coddy.spring.FileType;
import com.coddy.spring.PageAction;
import com.coddy.utils.ConfigProperties;
import com.coddy.utils.TestDataProvider;

public class CreateBasicQSYKResource
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
		pageAction.click("��Դ����_����һ��_a");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "loginAndChangeFrameTest")
	public void createBasicResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("����һ��_��Ӹ����_a");
		pageAction.sleep(2);
		pageAction.input("����һ��_���������_input", input.get("��Դ����_���������"));
		pageAction.click("��Դ����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("��Դ����_���������")),
				"�������һ����Դʧ��");
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
				.getInputData("FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_1");
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/��������.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_1",
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_2",
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_3",
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_4",
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_5",
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_6",
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_7",
				"FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_006_8");
	}
}
