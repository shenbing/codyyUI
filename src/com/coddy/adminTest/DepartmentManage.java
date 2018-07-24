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

public class DepartmentManage
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
		pageAction.click("��������_���ֹݹ���_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.selectItemByText("���ֹݹ���_ʡ��_select", input.get("���ֹݹ���_ʡ��"));
		pageAction.sleep(0.5);
		pageAction.selectItemByText("���ֹݹ���_�ؼ�����_select",
				input.get("���ֹݹ���_�ؼ�����"));
		pageAction.sleep(0.5);
		pageAction.selectItemByText("���ֹݹ���_�ؼ�����_select",
				input.get("���ֹݹ���_�ؼ�����"));
		pageAction.sleep(0.5);
		pageAction.input("���ֹݹ���_���ֹ�����_input", input.get("���ֹݹ���_���ֹ�����"));
		pageAction.click("���ֹݹ���_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("���ֹݹ���_���ֹ��б���_tr", "���ֹݹ���_�鿴_a",
				input.get("���ֹݹ���_���ֹ�����"));
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(
				pageAction.getCellTextByID("���ֹݹ���_���ֹ�����_table", "0.1"),
				input.get("���ֹݹ���_���ֹ�����"), "�鿴���ֹ�����ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "searchAndReviewSchoolTest")
	public void resetpasswordTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String account = pageAction.getCellTextByID("���ֹݹ���_���ֹ�����_table", "3.1");
		pageAction.click("���ֹ�����_��������_a");
		pageAction.click("���ֹ�����_��������ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		String message = pageAction.getText("���ֹ�����_������������Ϣ_center");
		String result = message.split("\n")[0];
		String newPassword = message.split("\n")[1].split("��")[1];
		ActionCheck.verfiyEquals(result, expect.get("����������ʾ"), "��������ʧ��");
		pageAction.click("���ֹ�����_��ʾ��_ȷ��_a");
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
				expect.get("���ֹݵ�½��Ϣ"), "�������½�˺�ʧ��");
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
		pageAction.click("���ֹ�����_�����б�_a");
		pageAction.sleep(2);
		pageAction.input("���ֹݹ���_���ֹ�����_input", input.get("���ֹݹ���_���ֹ�����"));
		pageAction.click("���ֹݹ���_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("���ֹݹ���_���ֹ��б���_tr", "���ֹݹ���_����_a",
				input.get("���ֹݹ���_���ֹ�����"));
		pageAction.sleep(2);
		pageAction.click("���ֹݹ���_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("���ֹݹ���_��������˽����ʾ_div"),
				expect.get("������˲��������ʾ"), "���ֹ�����ʧ��");
		pageAction.click("���ֹݹ���_��ʾ��_ȷ��_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "lockTest")
	public void unLockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("���ֹݹ���_���ֹ��б���_tr", "���ֹݹ���_����_a",
				input.get("���ֹݹ���_���ֹ�����"));
		pageAction.sleep(2);
		pageAction.click("���ֹݹ���_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("���ֹݹ���_��������˽����ʾ_div"),
				expect.get("������˲��������ʾ"), "���ֹݽ���ʧ��");
		pageAction.click("���ֹݹ���_��ʾ��_ȷ��_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "unLockTest")
	public void noVerifie(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("���ֹݹ���_���ֹ��б���_tr",
				"���ֹݹ���_�����_a", input.get("���ֹݹ���_���ֹ�����"));
		pageAction.sleep(2);
		pageAction.click("���ֹݹ���_�����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("���ֹݹ���_��������˽����ʾ_div"),
				expect.get("������˲��������ʾ"), "���ֹ������ʧ��");
		pageAction.click("���ֹݹ���_��ʾ��_ȷ��_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "noVerifie")
	public void resetVerifie(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("���ֹݹ���_���ֹ��б���_tr",
				"���ֹݹ���_�ָ����_a", input.get("���ֹݹ���_���ֹ�����"));
		pageAction.sleep(2);
		pageAction.click("���ֹݹ���_�ָ����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("���ֹݹ���_��������˽����ʾ_div"),
				expect.get("������˲��������ʾ"), "���ֹݻָ����ʧ��");
		pageAction.click("���ֹݹ���_��ʾ��_ȷ��_a");
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
				.getInputAndExpectData("FUN_HT_YYSHT_JGGL_JYJDJG_CK_001_1");
	}
}
