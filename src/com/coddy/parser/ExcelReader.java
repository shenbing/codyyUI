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
 * Excel������
 * 
 * @author shenbing
 * 
 */
public class ExcelReader {
	/**
	 * �����ļ���
	 */
	private String fileName;
	/**
	 * �ռǾ��
	 */
	private Log log;
	/**
	 * ������������HashMap
	 */
	private HashMap<String, HashMap<String, String>> inputMap;
	/**
	 * ����Ԥ��ֵHashMap
	 */
	private HashMap<String, HashMap<String, String>> expectMap;

	/**
	 * ���췽��
	 * 
	 * @param f
	 *            �����ļ�·��
	 * @param log
	 *            ��־���
	 */
	public ExcelReader(String f, Log log) {
		this.fileName = f;
		this.log = log;
		this.initData();
	}

	/**
	 * ��ʼ������
	 */
	public void initData() {
		this.inputMap = this.getInputDataHashMap();
		this.expectMap = this.getExpectDataHashMap();
	}

	/**
	 * ��ȡ������������
	 * 
	 * @param key
	 *            ��������ID
	 * @return ���������������ݵ�HashMap
	 */
	public HashMap<String, String> getInputDataMap(String key) {
		return inputMap.get(key);
	}

	/**
	 * ��ȡ��������Ԥ�ڽ������
	 * 
	 * @param key
	 *            ��������ID
	 * @return ��������Ԥ�ڽ������HashMap
	 */
	public HashMap<String, String> getExpectDataMap(String key) {
		return expectMap.get(key);
	}

	/**
	 * ��ȡ���������������ݡ�Ԥ�ڽ������
	 * 
	 * @param caseID
	 *            ��������ID
	 * @return �����������ݡ�Ԥ�ڽ�����ݵ�HashMap
	 */
	public HashMap<String, String> getCaseDataMap(String caseID) {
		HashMap<String, String> caseDataMap = new HashMap<String, String>();
		caseDataMap.putAll(getInputDataMap(caseID));
		caseDataMap.putAll(getExpectDataMap(caseID));
		return caseDataMap;
	}

	/**
	 * ��ȡ��������ID���������ݵ�HashMap
	 * 
	 * @return ��������ID���������ݵ�HashMap
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
			// ��ȡExcel�ļ���Workbook����
			wb = WorkbookFactory.create(excelFileInputStream);
		} catch (InvalidFormatException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("AutoTest.fail")
					+ Messages.getString("checkDom4j"));
		} catch (IOException e) {
			this.log.error(Messages.getString("readFile") + this.fileName + Messages.getString("AutoTest.fail")
					+ Messages.getString("checkExcel"));
		}
		// ��ȡExcel�ļ��ĵ�һ��sheet����
		Sheet sheet = wb.getSheetAt(0);
		// ��ȡ��������ID��CellRangeAddress����List
		caseIDRegions = this.getCaseIDArrayList(sheet);
		for (CellRangeAddress caseIDRegion : caseIDRegions) {
			HashMap<String, String> tempHashMap = new HashMap<String, String>();
			int firstRow = caseIDRegion.getFirstRow();
			int lastRow = caseIDRegion.getLastRow();
			int firstColumn = caseIDRegion.getFirstColumn();
			Row caseIDRow = sheet.getRow(firstRow);
			Cell caseIDCell = caseIDRow.getCell(firstColumn);
			int cellType = caseIDCell.getCellType();
			// ��ȡ��������ID��Ԫ��ֵ
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
	 * ��ȡ��������ID��Ԥ�ڽ�����ݵ�HashMap
	 * 
	 * @return ��������ID��Ԥ�ڽ����HashMap
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
	 * ��ȡ��������ID��CellRangeAddress����List
	 * 
	 * @param sheet
	 *            ������������sheet
	 * @return ��������ID��CellRangeAddress����List
	 */
	private ArrayList<CellRangeAddress> getCaseIDArrayList(Sheet sheet) {
		ArrayList<CellRangeAddress> caseidList = null;
		int mergednum = sheet.getNumMergedRegions();
		caseidList = new ArrayList<CellRangeAddress>();
		// ArrayList<CellRangeAddress> casenameList = new
		// ArrayList<CellRangeAddress>();
		for (int i = 0; i < mergednum; i++) {
			// �ϲ���Ԫ��ĵ�һ����A
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
	 * ��ȡ��Ԫ��ֵ
	 * 
	 * @param valueCell
	 *            ��Ԫ�����
	 * @param type
	 *            ��Ԫ������ ��CELL_TYPE_NUMERIC ��ֵ�� 0 CELL_TYPE_STRING �ַ����� 1
	 *            CELL_TYPE_FORMULA ��ʽ�� 2 CELL_TYPE_BLANK ��ֵ 3 CELL_TYPE_BOOLEAN
	 *            ������ 4 CELL_TYPE_ERROR ���� 5
	 * @return ��Ԫ��ֵ
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
