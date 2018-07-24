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

public class TeachersManage
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
	public void searchAndReviewTeacherTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_�û�����_a");
		pageAction.sleep(2);
		pageAction.click("�û�����_��ʦ����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.click("��ʦ����_���ڵ�����_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("��ʦ����_���ڵ������б�_div", "��ʦ����_��ѯ�����б�_div",
				input.get("��ʦ����_���ڵ�����"));
		pageAction.click("��ʦ����_���ڵ�������_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("��ʦ����_���ڵ��������б�_div", "��ʦ����_��ѯ�����б�_div",
				input.get("��ʦ����_���ڵ�����"));
		pageAction.click("��ʦ����_ѧУ����_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("��ʦ����_ѧУ�����б�_div", "��ʦ����_��ѯ�����б�_div",
				input.get("��ʦ����_ѧУ����"));
		pageAction.click("��ʦ����_ѧ��_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("��ʦ����_ѧ���б�_div", "��ʦ����_��ѯ�����б�_div",
				input.get("��ʦ����_ѧ��"));
		pageAction.click("��ʦ����_ѧ��_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("��ʦ����_ѧ���б�_div", "��ʦ����_��ѯ�����б�_div",
				input.get("��ʦ����_ѧ��"));
		pageAction.input("��ʦ����_��ʦ����_input", input.get("��ʦ����_��ʦ����"));
		pageAction.click("��ʦ����_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("��ʦ����_��ʦ�б���_tr", "��ʦ����_�鿴_a",
				input.get("��ʦ����_��ʦ����"));
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("��ʦ����_������_li").split("\n")[1],
				input.get("��ʦ����_��ʦ����"), "�鿴��ʦ����ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "searchAndReviewTeacherTest")
	public void resetpasswordTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String account = pageAction.getText("��ʦ����_ID��_li").split("\n")[1].trim();
		pageAction.click("��ʦ����_��������_input");
		pageAction.sleep(2);
		String message = pageAction.getText("��ʦ����_������������Ϣ_div");
		String result = message.split("\n")[0];
		String newPassword = message.split("\n")[1].split(":")[1];
		ActionCheck.verfiyEquals(result, expect.get("����������ʾ"), "��������ʧ��");
		pageAction.click("��ʦ����_��ʾ��_ȷ��_span");
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
				expect.get("��ʦ��½��Ϣ"), "�������½�˺�ʧ��");
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
		pageAction.click("��ʦ����_����_a");
		pageAction.sleep(2);
		pageAction.input("��ʦ����_��ʦ����_input", input.get("��ʦ����_��ʦ����"));
		pageAction.click("��ʦ����_����_span");
		pageAction.sleep(3);
		pageAction.clickSubElementContainsText("��ʦ����_��ʦ�б���_tr", "��ʦ����_����_a",
				input.get("��ʦ����_��ʦ����"));
		pageAction.sleep(2);
		pageAction.click("��ʦ����_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("��ʦ����_�������������ʾ_div"),
				expect.get("�������������������ʾ"), "��ʦ����ʧ��");
		pageAction.click("��ʦ����_��ʾ��_ȷ��_a");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "lockTest")
	public void unLockTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.clickSubElementContainsText("��ʦ����_��ʦ�б���_tr", "��ʦ����_����_a",
				input.get("��ʦ����_��ʦ����"));
		pageAction.sleep(2);
		pageAction.click("��ʦ����_����ȷ�Ͽ�_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyEquals(pageAction.getText("��ʦ����_�������������ʾ_div"),
				expect.get("�������������������ʾ"), "��ʦ�ݽ���ʧ��");
		pageAction.click("��ʦ����_��ʾ��_ȷ��_a");
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
				.getInputAndExpectData("FUN_HT_YYSHT_YHGL_JSGL_CK_002_1");
	}
}
