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

public class BasicKnowledgeOperation
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

	@Test(dataProvider = "testDataProvider")
	public void createKnowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		pageAction.openUrl(url);
		pageAction.input("��½_�û���_input", input.get("��½_�û���"));
		pageAction.input("��½_����_input", input.get("��½_����"));
		pageAction.click("��½_��½_input");
		pageAction.sleep(2);
		pageAction.click("����_��������_a");
		pageAction.sleep(2);
		pageAction.click("��������_֪ʶ�����_a");
		pageAction.sleep(2);
		pageAction.selectFrame("����_Frame_iframe");
		pageAction.sleep(2);
		String semesterSql = "select base_semester_id from base_semester where semester_name = '"
				+ input.get("ѡ��ѧ��") + "'";
		String semesterid = new DbUnit("db.properties").getValue(semesterSql, 0, 0);
		String disciplineSql = "select base_discipline_id from base_discipline where discipline_name = '"
				+ input.get("ѡ��ѧ��") + "'";
		String disciplineid = new DbUnit("db.properties").getValue(disciplineSql, 0, 0);
		String userID = semesterid + "-" + disciplineid;
		pageAction.clickSubElement("֪ʶ�����_ѧ���б���_tr", "DBID", userID,
				"֪ʶ�����_���֪ʶ��_a");
		pageAction.sleep(2);
		pageAction.input("֪ʶ�����_֪ʶ������_input", input.get("֪ʶ�����_֪ʶ������"));
		pageAction.click("֪ʶ�����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("֪ʶ�����_֪ʶ������")), "���֪ʶ��ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createKnowledgeTest")
	public void createSubknowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String knowledgeSql = "select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("ѡ��֪ʶ��") + "' and parent_id = 'ROOT'";
		String knowledgeid = new DbUnit("db.properties").getValue(knowledgeSql, 0, 0);
		pageAction.clickSubElement("֪ʶ�����_ѧ���б���_tr", "DBID", knowledgeid,
				"֪ʶ�����_�����֪ʶ��_a");
		pageAction.sleep(2);
		pageAction.input("֪ʶ�����_֪ʶ������_input", input.get("֪ʶ�����_��֪ʶ������"));
		pageAction.click("֪ʶ�����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("֪ʶ�����_��֪ʶ������")),
				"�����֪ʶ��ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createSubknowledgeTest")
	public void deleteSubknowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String subknowledgeSql = "select base_knowledge_id from base_knowledge where parent_id in (select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("ѡ��֪ʶ��") + "' and parent_id = 'ROOT')";
		String subknowledgeid = new DbUnit("db.properties").getValue(subknowledgeSql, 0, 0);
		pageAction.clickSubElement("֪ʶ�����_ѧ���б���_tr", "DBID", subknowledgeid,
				"֪ʶ�����_ɾ��_a");
		pageAction.sleep(2);
		pageAction.click("֪ʶ�����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String subknowledgeCountSql = "select count(base_knowledge_id) from base_knowledge where parent_id in (select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("ѡ��֪ʶ��") + "' and parent_id = 'ROOT')";
		String subknowledgeCount = new DbUnit("db.properties").getValue(subknowledgeCountSql,
				0, 0);
		ActionCheck.verfiyEquals(subknowledgeCount, "0", "ɾ����֪ʶ��ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteSubknowledgeTest")
	public void deleteknowledgeTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String knowledgeSql = "select base_knowledge_id from base_knowledge where knowledge_name = '"
				+ input.get("ѡ��֪ʶ��") + "' and parent_id = 'ROOT'";
		String knowledgeid = new DbUnit("db.properties").getValue(knowledgeSql, 0, 0);
		pageAction.clickSubElement("֪ʶ�����_ѧ���б���_tr", "DBID", knowledgeid,
				"֪ʶ�����_ɾ��_a");
		pageAction.sleep(2);
		pageAction.click("֪ʶ�����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		String knowledgeCountSql = "select count(base_knowledge_id) from base_knowledge where knowledge_name = '"
				+ input.get("ѡ��֪ʶ��") + "' and parent_id = 'ROOT'";
		String knowledgeCount = new DbUnit("db.properties").getValue(knowledgeCountSql, 0, 0);
		ActionCheck.verfiyEquals(knowledgeCount, "0", "ɾ����֪ʶ��ʧ��");
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
				.getInputAndExpectData("FUN_HT_YYSHT_JCSZ_ZSDFL_TJZSD_003_1");
	}
}
