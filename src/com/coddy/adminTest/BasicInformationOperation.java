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

public class BasicInformationOperation
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
				+ "/pagedata/��̨/���ݹ���.yalm", FileType.YALM);
	}

	@Test(dataProvider = "initDataProvider")
	public void initTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_���ݹ���_a");
		pageAction.sleep(2);
		pageAction.click("���ݹ���_��Ϣ�б�_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "createInformationProvider", dependsOnMethods = "initTest")
	public void createInformationTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.click("��Ϣ�б�_��Ϣ����_a");
		pageAction.sleep(2);
		pageAction.input("��Ϣ����_����_input", input.get("��Ϣ����_����"));
		pageAction.click("��Ϣ����_����_input");
		pageAction.sleep(0.5);
		pageAction.clickSubElementAsText("��Ϣ����_�����б�_input", "��Ϣ����_������_div",
				input.get("ѡ������"));
		if (input.get("ѡ������").equals("֪ͨ"))
		{
			pageAction.click("��Ϣ����_ѡ����ն���_a");
			pageAction.sleep(2);
			if (input.get("ѡ����ն���").equals("ȫѡ"))
			{
				pageAction.click("��Ϣ����_ѡ����ն���_ȫѡ_input");
				pageAction.click("��Ϣ����_ѡ����ն���_����ʡ_span");
			}
			else if (input.get("ѡ����ն���").equals("��ʦ"))
			{
				pageAction.click("��Ϣ����_ѡ����ն���_��ʦ_input");
				pageAction.click("��Ϣ����_ѡ����ն���_����ʡ_span");
			}
			else if (input.get("ѡ����ն���").equals("ѧ��"))
			{
				pageAction.click("��Ϣ����_ѡ����ն���_ѧ��_input");
				pageAction.click("��Ϣ����_ѡ����ն���_����ʡ_span");
			}
			else if (input.get("ѡ����ն���").equals("�ҳ�"))
			{
				pageAction.click("��Ϣ����_ѡ����ն���_�ҳ�_input");
				pageAction.click("��Ϣ����_ѡ����ն���_����ʡ_span");
			}
			pageAction.click("��Ϣ����_ѡ����ն���_ȷ��_span");
		}
		pageAction.click("��Ϣ����_ͼƬ_span");
		pageAction.sleep(2);
		pageAction.click("ͼƬ_����ͼƬ_li");
		pageAction.sleep(0.5);
		pageAction.input("ͼƬ_ͼƬ��ַ_input", input.get("ͼƬ_ͼƬ��ַ"));
		pageAction.click("ͼƬ_ȷ��_a");
		pageAction.selectFrame("��Ϣ����_����_iframe");
		pageAction.input("��Ϣ����_����_body", input.get("��Ϣ����_����"));
		pageAction.selectDefaultFrame();
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.sleep(2);
		pageAction.click("��Ϣ����_����_a");
		pageAction.sleep(2);
		pageAction.acceptAlert();
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("��Ϣ����_����")),
				"������Ϣʧ��");
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("ѡ������")),
				"������Ϣʧ��");
		pageAction.sleep(2);
		pageAction.selectDefaultFrame();
		pageAction.click("���ݹ���_��Ϣ�б�_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createInformationTest")
	public void reviewInformationTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		if (input.get("ѡ������").equals("����"))
		{
			pageAction.click("��Ϣ�б�_����_a");
			pageAction.sleep(2);
		}
		else if (input.get("ѡ������").equals("����"))
		{
			pageAction.click("��Ϣ�б�_����_a");
			pageAction.sleep(2);
		}
		else if (input.get("ѡ������").equals("֪ͨ"))
		{
			pageAction.click("��Ϣ�б�_֪ͨ_a");
			pageAction.sleep(2);
		}
		String newsIDSql = "select publish_news_id from publish_news where title = '"
				+ input.get("��Ϣ����_����") + "'";
		String newsID = new DbUnit("db.properties").getValue(newsIDSql, 0, 0);
		pageAction.clickAsAttributeValue("��Ϣ�б�_����_a", "onclick",
				"informationDetail('" + newsID + "')");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("��Ϣ����_����")),
				"�鿴��Ϣʧ��");
		ActionCheck.verfiyTrue(pageAction.isTextPresent(input.get("ѡ������")),
				"�鿴��Ϣʧ��");
		pageAction.click("�б�����_����_a");
		pageAction.sleep(2);
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "reviewInformationTest")
	public void deleteInformationTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		if (input.get("ѡ������").equals("����"))
		{
			pageAction.click("��Ϣ�б�_����_a");
			pageAction.sleep(2);
		}
		else if (input.get("ѡ������").equals("����"))
		{
			pageAction.click("��Ϣ�б�_����_a");
			pageAction.sleep(2);
		}
		else if (input.get("ѡ������").equals("֪ͨ"))
		{
			pageAction.click("��Ϣ�б�_֪ͨ_a");
			pageAction.sleep(2);
		}
		String newsIDSql = "select publish_news_id from publish_news where title = '"
				+ input.get("��Ϣ����_����") + "'";
		String newsID = new DbUnit("db.properties").getValue(newsIDSql, 0, 0);
		String newsTypeSql = "select news_type from publish_news where title = '"
				+ input.get("��Ϣ����_����") + "'";
		String newsnewsType = new DbUnit("db.properties").getValue(newsTypeSql, 0, 0);
		pageAction.clickAsAttributeValue("��Ϣ�б�_ɾ��_a", "onclick",
				"deleteComment('" + newsID + "','" + newsnewsType + "')");
		pageAction.sleep(2);
		pageAction.click("��Ϣ�б�_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		pageAction.selectDefaultFrame();
		pageAction.click("���ݹ���_��Ϣ�б�_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.sleep(2);
		if (input.get("ѡ������").equals("����"))
		{
			pageAction.click("��Ϣ�б�_����_a");
			pageAction.sleep(2);
		}
		else if (input.get("ѡ������").equals("����"))
		{
			pageAction.click("��Ϣ�б�_����_a");
			pageAction.sleep(2);
		}
		else if (input.get("ѡ������").equals("֪ͨ"))
		{
			pageAction.click("��Ϣ�б�_֪ͨ_a");
			pageAction.sleep(2);
		}
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("��Ϣ����_����")), "����ɾ��ʧ��");
		pageAction.sleep(2);
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
				+ "/testdata/��̨/���ݹ���.xls")
				.getInputAndExpectData("FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_1");
	}

	@DataProvider
	public Object[][] createInformationProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/���ݹ���.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_1",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_2",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_3",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_4");
	}

	@DataProvider
	public Object[][] testDataProvider()
	{
		return new TestDataProvider(System.getProperty("user.dir")
				+ "/testdata/��̨/���ݹ���.xls").getInputAndExpectData(
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_1",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_2",
				"FUN_HT_YYSHT_NRGL_XXGL_FBXX_004_3");
	}
}
