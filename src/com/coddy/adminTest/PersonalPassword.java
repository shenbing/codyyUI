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

public class PersonalPassword
{
	public PageAction pageAction;
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
				+ "/pagedata/��̨/�����˺�.yalm", FileType.YALM);
	}

	@Test(dataProvider = "errorOldPWDProvider")
	public void errorOldPWDTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_�����˺�_a");
		pageAction.sleep(2);
		pageAction.click("�����˺�_�޸�����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.input("�޸�����_ԭ����_input", input.get("�޸�����_ԭ����"));
		pageAction.input("�޸�����_������_input", input.get("�޸�����_������"));
		pageAction.input("�޸�����_ȷ��������_input", input.get("�޸�����_ȷ��������"));
		pageAction.click("�޸�����_�޸�_input");
		ActionCheck.verfiyEquals(pageAction.getText("�޸�����_ԭ���������ʾ_i"),
				expect.get("�޸�����_ԭ���������ʾ"), "ԭ�������");
		pageAction.selectDefaultFrame();
		pageAction.click("����_�˳�_a");
	}

	@Test(dataProvider = "differentNewPWDProvider")
	public void differentNewPWDTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_�����˺�_a");
		pageAction.sleep(2);
		pageAction.click("�����˺�_�޸�����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.input("�޸�����_ԭ����_input", input.get("�޸�����_ԭ����"));
		pageAction.input("�޸�����_������_input", input.get("�޸�����_������"));
		pageAction.input("�޸�����_ȷ��������_input", input.get("�޸�����_ȷ��������"));
		pageAction.click("�޸�����_�޸�_input");
		ActionCheck.verfiyEquals(pageAction.getText("�޸�����_������������벻һ�´�����ʾ_i"),
				expect.get("�޸�����_������������벻һ�´�����ʾ"), "������������벻һ��");
		pageAction.selectDefaultFrame();
		pageAction.click("����_�˳�_a");
	}

	@Test(dataProvider = "rightPWDProvider")
	public void rightPWDTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_�����˺�_a");
		pageAction.sleep(2);
		pageAction.click("�����˺�_�޸�����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.input("�޸�����_ԭ����_input", input.get("�޸�����_ԭ����"));
		pageAction.input("�޸�����_������_input", input.get("�޸�����_������"));
		pageAction.input("�޸�����_ȷ��������_input", input.get("�޸�����_ȷ��������"));
		pageAction.click("�޸�����_�޸�_input");
		ActionCheck.verfiyEquals(pageAction.getText("�޸�����_�޸Ľ����Ϣ_span"),
				expect.get("�޸�����_�޸Ľ����Ϣ"), "�޸�����");
		pageAction.selectDefaultFrame();
		pageAction.click("����_�˳�_a");
	}

	@AfterClass
	public void afterClass()
	{
		pageAction.close();
	}

	@DataProvider
	public Object[][] errorOldPWDProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/�����˺�.xls").getInputAndExpectData("FUN_HT_YYSHT_GRZX_XGMM_001_1");
	}

	@DataProvider
	public Object[][] differentNewPWDProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/�����˺�.xls").getInputAndExpectData("FUN_HT_YYSHT_GRZX_XGMM_001_2");
	}

	@DataProvider
	public Object[][] rightPWDProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/�����˺�.xls").getInputAndExpectData("FUN_HT_YYSHT_GRZX_XGMM_001_3");
	}
}
