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

public class SchoolManage
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
				+ "/pagedata/��̨/��������.yalm", FileType.YALM);
	}

	@Test(dataProvider = "testDataProvider")
	public void searchAndReviewSchoolTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_��������_a");
		pageAction.sleep(2);
		pageAction.click("��������_ѧУ����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.selectItemByText("ѧУ����_ʡ��_select", input.get("ѧУ����_ʡ��"));
		pageAction.sleep(0.5);
		pageAction.selectItemByText("ѧУ����_�ؼ�����_select", input.get("ѧУ����_�ؼ�����"));
		pageAction.sleep(0.5);
		pageAction.selectItemByText("ѧУ����_�ؼ�����_select", input.get("ѧУ����_�ؼ�����"));
		pageAction.sleep(0.5);
		pageAction.input("ѧУ����_ѧУ����_input", input.get("ѧУ����_ѧУ����"));
		pageAction.click("ѧУ����_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("ѧУ����_ѧУ�б���_tr", "ѧУ����_�鿴_a",
				input.get("ѧУ����_ѧУ����"));
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(
				pageAction.getCellTextByID("ѧУ����_ѧУ����_table", "0.1"),
				input.get("ѧУ����_ѧУ����"), "�鿴ѧУ����ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "searchAndReviewSchoolTest")
	public void resetpasswordTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String account = pageAction.getCellTextByID("ѧУ����_ѧУ����_table", "3.1");
		pageAction.click("ѧУ����_��������_a");
		pageAction.click("ѧУ����_��������ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		String message = pageAction.getText("ѧУ����_������������Ϣ_center");
		String result = message.split("\n")[0];
		String newPassword = message.split("\n")[1].split("��")[1];
		ActionCheck.verfiyEquals(result, expect.get("����������ʾ"), "��������ʧ��");
		pageAction.click("ѧУ����_��ʾ��_ȷ��_a");
		PageAction pa = new PageAction(BrowserType.FIREFOX);
		pa.loadPageData(System.getProperty("user.dir") + "/pagedata/����.yalm",
				FileType.YALM);
		pa.loadPageData(System.getProperty("user.dir") + "/pagedata/�ҵ�����.yalm",
				FileType.YALM);
		pa.openUrl();
		pa.sleep(1);
		pa.input("��½_�û���_input", account);
		pa.input("��½_����_input", newPassword);
		pa.click("��½_��½_input");
		pa.sleep(1);
		ActionCheck.verfiyEquals(pa.getText("ͷ��_��½�ʺ���Ϣ_a"),
				input.get("ѧУ����_ѧУ����"), "�������½�˺�ʧ��");
		pa.moveOn("ͷ��_ͷ��_img");
		pa.click("ͷ��_��������_a");
		pa.click("�ҵ�����_�޸�����_a");
		pa.sleep(1);
		pa.input("�޸�����_��ǰ����_input", newPassword);
		pa.input("�޸�����_������_input", input.get("�޸�����_������"));
		pa.input("�޸�����_ȷ��������_input", input.get("�޸�����_������"));
		pa.click("�޸�����_����_input");
		pa.sleep(1);
		ActionCheck.verfiyEquals(pa.getText("�ҵ�����_��ʾ��Ϣ_span"),
				expect.get("�޸�������ʾ"), "�޸�����ʧ��");
		pa.closeCurrentWindow();
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "resetpasswordTest")
	public void lockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("ѧУ����_�����б�_a");
		pageAction.sleep(2);
		pageAction.input("ѧУ����_ѧУ����_input", input.get("ѧУ����_ѧУ����"));
		pageAction.click("ѧУ����_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("ѧУ����_ѧУ�б���_tr", "ѧУ����_����_a",
				input.get("ѧУ����_ѧУ����"));
		pageAction.sleep(2);
		pageAction.click("ѧУ����_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("ѧУ����_�������������ʾ_div"),
				expect.get("ѧУ����_�������������ʾ"), "ѧУ����ʧ��");
		pageAction.click("ѧУ����_��ʾ��_ȷ��_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "lockTest")
	public void unLockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("ѧУ����_ѧУ�б���_tr", "ѧУ����_����_a",
				input.get("ѧУ����_ѧУ����"));
		pageAction.sleep(2);
		pageAction.click("ѧУ����_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("ѧУ����_�������������ʾ_div"),
				expect.get("ѧУ����_�������������ʾ"), "ѧУ����ʧ��");
		pageAction.click("ѧУ����_��ʾ��_ȷ��_a");
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
				.getInputAndExpectData("FUN_HT_YYSHT_JGGL_XXGL_CK_001_1");
	}
}
