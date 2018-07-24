package com.coddy.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import com.coddy.utils.Messages;

/**
 * Excel解析类
 * 
 * @author shenbing
 * 
 */
public class ExcelReader {
	/**
	 * 加载文件名
	 */
	private String fileName;
	/**
	 * 日记句柄
	 */
	private Log log;
	/**
	 * 用例输入数据HashMap
	 */
	private HashMap<String, HashMap<String, String>> inputMap;
	/**
	 * 用例预期值HashMap
	 */
	private HashMap<String, HashMap<String, String>> expectMap;

	/**
	 * 构造方法
	 * 
	 * @param f
	 *            数据文件路径
	 * @param log
	 *            日志句柄
	 */
	public ExcelReader(String f, Log log) {
		this.fileName = f;
		this.log = log;
		this.initData();
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		this.inputMap = this.getInputDataHashMap();
		this.expectMap = this.getExpectDataHashMap();
	}

	/**
	 * 获取用例输入数据
	 * 
	 * @param key
	 *            测试用例ID
	 * @return 测试用例输入数据的HashMap
	 */
	public HashMap<String, String> getInputDataMap(String key) {
		return inputMap.get(key);
	}

	/**
	 * 获取测试用例预期结果数据
	 * 
	 * @param key
	 *            测试用例ID
	 * @return 测试用例预期结果数据HashMap
	 */
	public HashMap<String, String> getExpectDataMap(String key) {
		return expectMap.get(key);
	}

	/**
	 * 获取测试用例输入数据、预期结果数据
	 * 
	 * @param caseID
	 *            测试用例ID
	 * @return 测试输入数据、预期结果数据的HashMap
	 */
	public HashMap<String, String> getCaseDataMap(String caseID) {
		HashMap<String, String> caseDataMap = new HashMap<String, String>();
		caseDataMap.putAll(getInputDataMap(caseID));
		caseDataMap.putAll(getExpectDataMap(caseID));
		return caseDataMap;
	}

	/**
	 * 获取测试用例ID与输入数据的HashMap
	 * 
	 * @return 测试用例ID与输入数据的HashMap
	 */
	private HashMap<String, HashMap<String, String>> getInputDataHashMap() {
		HashMap<String, HashMap<String, String>> inputHashMap = new HashMap<String, HashMap<String, String>>();
		FileInputStream excelFileInputStream = null;
		ArrayList<CellRangeAddress> caseIDRegions = null;
		Workbook wb = null;
		try {
			excelFileInputStream = new FileInputStream(this.fileName);
		} catch (FileNotFoundException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("noexists"));
		}
		try {
			// 获取Excel文件的Workbook对象
			wb = WorkbookFactory.create(excelFileInputStream);
		} catch (InvalidFormatException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("AutoTest.fail")
					+ Messages.getString("checkDom4j"));
		} catch (IOException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("AutoTest.fail")
					+ Messages.getString("checkExcel"));
		}
		// 获取Excel文件的第一个sheet对象
		Sheet sheet = wb.getSheetAt(0);
		// 获取测试用例ID的CellRangeAddress对象List
		caseIDRegions = this.getCaseIDArrayList(sheet);
		for (CellRangeAddress caseIDRegion : caseIDRegions) {
			HashMap<String, String> tempHashMap = new HashMap<String, String>();
			int firstRow = caseIDRegion.getFirstRow();
			int lastRow = caseIDRegion.getLastRow();
			int firstColumn = caseIDRegion.getFirstColumn();
			Row caseIDRow = sheet.getRow(firstRow);
			Cell caseIDCell = caseIDRow.getCell(firstColumn);
			int cellType = caseIDCell.getCellType();
			// 获取测试用例ID单元格值
			String caseIDString = getCellValue(caseIDCell, cellType);
			for (int i = firstRow; i < lastRow + 1; i++) {
				Row row = sheet.getRow(i);
				Cell keyCell = row.getCell(2);
				if (keyCell == null) {
					continue;
				}
				Cell valueCell = row.getCell(3);
				if (valueCell == null) {
					continue;
				}
				int keyType = keyCell.getCellType();
				int valueType = valueCell.getCellType();
				String keyValue = getCellValue(keyCell, keyType);
				String valueValue = getCellValue(valueCell, valueType);
				if (keyValue == null || keyValue.equals("")) {
					continue;
				} else {
					tempHashMap.put(keyValue, valueValue);
				}
			}
			inputHashMap.put(caseIDString, tempHashMap);
		}
		return inputHashMap;
	}

	/**
	 * 获取测试用例ID与预期结果数据的HashMap
	 * 
	 * @return 测试用例ID与预期结果的HashMap
	 */
	private HashMap<String, HashMap<String, String>> getExpectDataHashMap() {
		HashMap<String, HashMap<String, String>> ExpectHashMap = new HashMap<String, HashMap<String, String>>();
		FileInputStream excelFileInputStream = null;
		ArrayList<CellRangeAddress> caseIDRegions = null;
		Workbook wb = null;
		try {
			excelFileInputStream = new FileInputStream(this.fileName);
		} catch (FileNotFoundException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("noexists"));
		}
		try {
			wb = WorkbookFactory.create(excelFileInputStream);
		} catch (InvalidFormatException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("AutoTest.fail")
					+ Messages.getString("checkDom4j"));
		} catch (IOException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("AutoTest.fail")
					+ Messages.getString("checkExcel"));
		}
		Sheet sheet = wb.getSheetAt(0);
		caseIDRegions = this.getCaseIDArrayList(sheet);
		for (CellRangeAddress caseIDRegion : caseIDRegions) {
			HashMap<String, String> tempHashMap = new HashMap<String, String>();
			int firstRow = caseIDRegion.getFirstRow();
			int lastRow = caseIDRegion.getLastRow();
			int firstColumn = caseIDRegion.getFirstColumn();
			Row caseIDRow = sheet.getRow(firstRow);
			Cell caseIDCell = caseIDRow.getCell(firstColumn);
			int cellType = caseIDCell.getCellType();
			String caseIDString = getCellValue(caseIDCell, cellType);
			for (int i = firstRow; i < lastRow + 1; i++) {
				Row row = sheet.getRow(i);
				Cell keyCell = row.getCell(4);
				if (keyCell == null) {
					continue;
				}
				Cell valueCell = row.getCell(5);
				if (valueCell == null) {
					continue;
				}
				int keyType = keyCell.getCellType();
				int valueType = valueCell.getCellType();
				String keyValue = getCellValue(keyCell, keyType);
				String valueValue = getCellValue(valueCell, valueType);
				if (keyValue == null || keyValue.equals("")) {
					continue;
				} else {
					tempHashMap.put(keyValue, valueValue);
				}
			}
			ExpectHashMap.put(caseIDString, tempHashMap);
		}
		return ExpectHashMap;
	}

	/**
	 * 获取测试用例ID的CellRangeAddress对象List
	 * 
	 * @param sheet
	 *            测试用例所在sheet
	 * @return 测试用例ID的CellRangeAddress对象List
	 */
	private ArrayList<CellRangeAddress> getCaseIDArrayList(Sheet sheet) {
		ArrayList<CellRangeAddress> caseidList = null;
		int mergednum = sheet.getNumMergedRegions();
		caseidList = new ArrayList<CellRangeAddress>();
		// ArrayList<CellRangeAddress> casenameList = new
		// ArrayList<CellRangeAddress>();
		for (int i = 0; i < mergednum; i++) {
			// 合并单元格的第一列是A
			if (sheet.getMergedRegion(i).getFirstColumn() == 0) {
				caseidList.add(sheet.getMergedRegion(i));
			}
			// else if (sheet.getMergedRegion(i).getFirstColumn() == 1)
			// {
			// casenameList.add(sheet.getMergedRegion(i));
			// }
		}
		return caseidList;

	}

	/**
	 * 获取单元格值
	 * 
	 * @param valueCell
	 *            单元格对象
	 * @param type
	 *            单元格类型 ：CELL_TYPE_NUMERIC 数值型 0 CELL_TYPE_STRING 字符串型 1
	 *            CELL_TYPE_FORMULA 公式型 2 CELL_TYPE_BLANK 空值 3 CELL_TYPE_BOOLEAN
	 *            布尔型 4 CELL_TYPE_ERROR 错误 5
	 * @return 单元格值
	 */
	private String getCellValue(Cell valueCell, int type) {
		String value = "";
		switch (type) {
		case 0:
			DecimalFormat df = new DecimalFormat("0");
			value = String.valueOf(df.format(valueCell.getNumericCellValue()));
			break;
		case 1:
			value = valueCell.getStringCellValue() == null ? "" : valueCell.getStringCellValue();
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			value = "null".equals(String.valueOf(valueCell.getErrorCellValue())) ? ""
					: String.valueOf(valueCell.getErrorCellValue());
			break;
		default:
			break;
		}
		return value;
	}

}
