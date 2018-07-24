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

public class ParentsManage
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
				+ "/pagedata/��̨/�û�����.yalm", FileType.YALM);
	}

	@Test(dataProvider = "testDataProvider")
	public void searchAndReviewParentTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_�û�����_a");
		pageAction.sleep(2);
		pageAction.click("�û�����_�ҳ�����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.click("�ҳ�����_���ڵ���_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("�ҳ�����_���ڵ����б�_div", "�ҳ�����_��ѯ�����б�_div",
				input.get("�ҳ�����_���ڵ���"));
		pageAction.click("�ҳ�����_ѧ��_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("�ҳ�����_ѧ���б�_div", "�ҳ�����_��ѯ�����б�_div",
				input.get("�ҳ�����_ѧ��"));
		pageAction.click("�ҳ�����_ѧУ����_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("�ҳ�����_ѧУ�����б�_div", "�ҳ�����_��ѯ�����б�_div",
				input.get("�ҳ�����_ѧУ����"));
		pageAction.click("�ҳ�����_�꼶_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("�ҳ�����_�꼶�б�_div", "�ҳ�����_��ѯ�����б�_div",
				input.get("�ҳ�����_�꼶"));
		pageAction.click("�ҳ�����_�༶_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("�ҳ�����_�༶�б�_div", "�ҳ�����_��ѯ�����б�_div",
				input.get("�ҳ�����_�༶"));
		pageAction.input("�ҳ�����_��������_input", input.get("�ҳ�����_��������"));
		pageAction.click("�ҳ�����_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("�ҳ�����_�ҳ��б���_tr", "�ҳ�����_�鿴_a",
				input.get("�ҳ�����_��������"));
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("�ҳ�����_������_li").split("\n")[1],
				input.get("�ҳ�����_�ҳ�����"), "�鿴�ҳ�����ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "searchAndReviewParentTest")
	public void resetpasswordTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String account = pageAction.getText("�ҳ�����_ID��_li").split("\n")[1].trim();
		pageAction.click("�ҳ�����_��������_input");
		pageAction.sleep(2);
		String message = pageAction.getText("�ҳ�����_������������Ϣ_div");
		String result = message.split("\n")[0];
		String newPassword = message.split("\n")[1].split(":")[1];
		ActionCheck.verfiyEquals(result, expect.get("����������ʾ"), "��������ʧ��");
		pageAction.click("�ҳ�����_��ʾ��_ȷ��_span");
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
				expect.get("�ҳ���½��Ϣ"), "�������½�˺�ʧ��");
		pa.click("�˵�_�ʻ�����_a");
		pa.sleep(1);
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
		pageAction.click("�ҳ�����_����_a");
		pageAction.sleep(2);
		pageAction.input("�ҳ�����_��������_input", input.get("�ҳ�����_��������"));
		pageAction.click("�ҳ�����_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("�ҳ�����_�ҳ��б���_tr", "�ҳ�����_����_a",
				input.get("�ҳ�����_��������"));
		pageAction.sleep(2);
		pageAction.click("�ҳ�����_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("�ҳ�����_�������������ʾ_div"),
				expect.get("�������������������ʾ"), "�ҳ�����ʧ��");
		pageAction.click("�ҳ�����_��ʾ��_ȷ��_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "lockTest")
	public void unLockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("�ҳ�����_�ҳ��б���_tr", "�ҳ�����_����_a",
				input.get("�ҳ�����_��������"));
		pageAction.sleep(2);
		pageAction.click("�ҳ�����_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("�ҳ�����_�������������ʾ_div"),
				expect.get("�������������������ʾ"), "�ҳ��ݽ���ʧ��");
		pageAction.click("�ҳ�����_��ʾ��_ȷ��_a");
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
				+ "/testdata/��̨/�û�����.xls")
				.getInputAndExpectData("FUN_HT_YYSHT_YHGL_JZGL_CK_002_1");
	}
}
