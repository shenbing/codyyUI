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

public class BasicQSYKOperation
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
	public void createResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
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
		pageAction.click("����һ��_��Ӹ����_a");
		pageAction.sleep(2);
		pageAction.input("����һ��_���������_input", input.get("��Դ����_���������"));
		pageAction.click("��Դ����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("��Դ����_���������")),
				"�������һ����Դʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createResourceTest")
	public void createSubResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String qsykSql = "select base_catalog_id from base_catalog where parent_id = 'ROOT' and catalog_name = '"
				+ input.get("��Դ����_���������") + "'";
		String qsykID = new DbUnit("db.properties").getValue(qsykSql, 0, 0);
		pageAction.clickSubElement("��Դ����_�б���_tr", "DBID", qsykID,
				"��Դ����_��������_a");
		pageAction.sleep(2);
		pageAction.input("��Դ����_���������_input", input.get("��Դ����_���������"));
		pageAction.click("��Դ����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyTrue(
				pageAction.isTextPresent2(input.get("��Դ����_���������")),
				"�������һ���������Դʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "createSubResourceTest")
	public void deleteSubResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String qsykSubSql = "select base_catalog_id from base_catalog where catalog_name = '"
				+ input.get("��Դ����_���������")
				+ "' and parent_id = ("
				+ "select base_catalog_id from base_catalog where parent_id = 'ROOT' and catalog_name = '"
				+ input.get("��Դ����_���������") + "')";
		String qsykSubID = new DbUnit("db.properties").getValue(qsykSubSql, 0, 0);
		pageAction.clickSubElement("��Դ����_�б���_tr", "DBID", qsykSubID,
				"��Դ����_ɾ��_a");
		pageAction.click("��Դ����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("��Դ����_���������")),
				"ɾ������һ�������ʧ��");
	}

	@Test(dataProvider = "testDataProvider", dependsOnMethods = "deleteSubResourceTest")
	public void deleteRootResourceTest(HashMap<String, String> input,
			HashMap<String, String> expect)
	{
		String qsykRootSql = "select base_catalog_id from base_catalog where parent_id = 'ROOT' and catalog_name = '"
				+ input.get("��Դ����_���������") + "'";
		String qsykRootID = new DbUnit("db.properties").getValue(qsykRootSql, 0, 0);
		pageAction.clickSubElement("��Դ����_�б���_tr", "DBID", qsykRootID,
				"��Դ����_ɾ��_a");
		pageAction.click("��Դ����_�Ի���_ȷ��_a");
		pageAction.sleep(2);
		ActionCheck.verfiyFalse(
				pageAction.isTextPresent2(input.get("��Դ����_���������")),
				"ɾ������һ�̸����ʧ��");
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
				.getInputAndExpectData("FUN_HT_YYSHT_JCSZ_ZYFL_QSYK_003_1");
	}
}
