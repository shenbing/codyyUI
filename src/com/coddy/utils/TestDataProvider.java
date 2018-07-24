package com.coddy.utils;

import java.util.HashMap;
import org.apache.commons.logging.Log;
import com.coddy.parser.ExcelReader;

/**
 * 提供测试数据类，封装成testng可识别的Object[][]结构
 * 
 * @author shenbing
 * 
 */
public class TestDataProvider
{
	/**
	 * 读取文件路径
	 */
	private String filepath = null;

	/**
	 * 日志句柄
	 */
	private Log log = LogUtils.getLog(TestDataProvider.class);

	/**
	 * 构造方法
	 * 
	 * @param filepath
	 *            数据文件路径
	 */
	public TestDataProvider(String filepath)
	{
		this.filepath = filepath;
	}

	/**
	 * 获取测试用例输入与预期结果
	 * 
	 * @param caseID
	 *            测试用例ID
	 * @return 文件数据Object[][]结构
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
	 * 获取多个测试用例输入与预期结果,供相同操作步骤，不同测试数据场景使用
	 * 
	 * @param caseID
	 *            多个测试用例ID
	 * @return 文件数据Object[][]结构
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
	 * 获取测试用例输入
	 * 
	 * @param caseID
	 *            测试用例ID
	 * @return 文件数据Object[][]结构
	 */
	public Object[][] getInputData(String caseID)
	{
		ExcelReader eReader = new ExcelReader(filepath, this.log);
		HashMap<String, String> inputDataMap = eReader.getInputDataMap(caseID);
		Object[][] inputDatObjects = new Object[][] { { inputDataMap } };
		return inputDatObjects;
	}

	/**
	 * 获取多个测试用例输入
	 * 
	 * @param caseID
	 *            多个测试用例ID
	 * @return 文件数据Object[][]结构
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
	 * 获取测试用例预期结果
	 * 
	 * @param caseID
	 *            测试用例ID
	 * @return 文件数据Object[][]结构
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
	 * 获取多个测试用例预期结果
	 * 
	 * @param caseID
	 *            多个测试用例ID
	 * @return 文件数据Object[][]结构
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
