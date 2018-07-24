package com.coddy.utils;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import com.coddy.parser.ExcelReader;

/**
 * �ṩ���������࣬��װ��testng��ʶ���Object[][]�ṹ
 * 
 * @author shenbing
 * 
 */
public class TestDataProvider
{
	/**
	 * ��ȡ�ļ�·��
	 */
	private String filepath = null;

	/**
	 * ��־���
	 */
	private Log log = LogUtils.getLog(TestDataProvider.class);

	/**
	 * ���췽��
	 * 
	 * @param filepath
	 *            �����ļ�·��
	 */
	public TestDataProvider(String filepath)
	{
		this.filepath = filepath;
	}

	/**
	 * ��ȡ��������������Ԥ�ڽ��
	 * 
	 * @param caseID
	 *            ��������ID
	 * @return �ļ�����Object[][]�ṹ
	 */
	public Object[][] getInputAndExpectData(String caseID)
	{
		ExcelReader eReader = new ExcelReader(filepath, this.log);
		HashMap<String, String> inputDataMap = eReader.getInputDataMap(caseID);
		HashMap<String, String> expectDataMap = eReader
				.getExpectDataMap(caseID);
		Object[][] inputDataObjects = new Object[][] { { inputDataMap,
				expectDataMap } };
		return inputDataObjects;
	}

	/**
	 * ��ȡ�����������������Ԥ�ڽ��,����ͬ�������裬��ͬ�������ݳ���ʹ��
	 * 
	 * @param caseID
	 *            �����������ID
	 * @return �ļ�����Object[][]�ṹ
	 */
	public Object[][] getInputAndExpectData(String... caseID)
	{
		int num = caseID.length;
		ExcelReader eReader = new ExcelReader(filepath, this.log);
		Object[][] inputDataObjects = new Object[num][];
		for (int i = 0; i < num; i++)
		{
			HashMap<String, String> inputDataMap = eReader
					.getInputDataMap(caseID[i]);
			HashMap<String, String> expectDataMap = eReader
					.getExpectDataMap(caseID[i]);
			inputDataObjects[i] = new Object[] { inputDataMap, expectDataMap };
		}
		return inputDataObjects;
	}

	/**
	 * ��ȡ������������
	 * 
	 * @param caseID
	 *            ��������ID
	 * @return �ļ�����Object[][]�ṹ
	 */
	public Object[][] getInputData(String caseID)
	{
		ExcelReader eReader = new ExcelReader(filepath, this.log);
		HashMap<String, String> inputDataMap = eReader.getInputDataMap(caseID);
		Object[][] inputDatObjects = new Object[][] { { inputDataMap } };
		return inputDatObjects;
	}

	/**
	 * ��ȡ���������������
	 * 
	 * @param caseID
	 *            �����������ID
	 * @return �ļ�����Object[][]�ṹ
	 */
	public Object[][] getInputData(String... caseID)
	{
		int num = caseID.length;
		ExcelReader eReader = new ExcelReader(filepath, this.log);
		Object[][] inputDataObjects = new Object[num][];
		for (int i = 0; i < num; i++)
		{
			HashMap<String, String> inputDataMap = eReader
					.getInputDataMap(caseID[i]);
			inputDataObjects[i] = new Object[] { inputDataMap };
		}
		return inputDataObjects;
	}

	/**
	 * ��ȡ��������Ԥ�ڽ��
	 * 
	 * @param caseID
	 *            ��������ID
	 * @return �ļ�����Object[][]�ṹ
	 */
	public Object[][] getExpectData(String caseID)
	{
		ExcelReader eReader = new ExcelReader(filepath, this.log);
		HashMap<String, String> expectDataMap = eReader
				.getExpectDataMap(caseID);
		Object[][] inputDatObjects = new Object[][] { { expectDataMap } };
		return inputDatObjects;
	}

	/**
	 * ��ȡ�����������Ԥ�ڽ��
	 * 
	 * @param caseID
	 *            �����������ID
	 * @return �ļ�����Object[][]�ṹ
	 */
	public Object[][] getExpectData(String... caseID)
	{
		int num = caseID.length;
		ExcelReader eReader = new ExcelReader(filepath, this.log);
		Object[][] expectDataObjects = new Object[num][];
		for (int i = 0; i < num; i++)
		{
			HashMap<String, String> expectDataMap = eReader
					.getExpectDataMap(caseID[i]);
			expectDataObjects[i] = new Object[] { expectDataMap };
		}
		return expectDataObjects;
	}
}
