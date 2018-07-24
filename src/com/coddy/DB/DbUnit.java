package com.coddy.DB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;

/**
 * 
 * @author shenbing
 * 
 */
public class DbUnit {
	private Logger logger = LoggerFactory.getLogger(DbUnit.class);

	private String fileName = null;

	public DbUnit() {
		this.fileName = "config.properties";
	}

	public DbUnit(String fileName) {
		this.fileName = fileName;
	}

	public int update(String sql) {
		DruidPooledConnection connection = null;
		Statement sqlStament = null;
		try {
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.createStatement();
			logger.info(formatSql(sql));
			return sqlStament.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return -1;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return -1;
		} finally {
			try {
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			;
		}
	}

	public int batchUpdate(ArrayList<String> sqlList) {
		DruidPooledConnection connection = null;
		Statement sqlStament = null;
		try {
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.createStatement();
			for (String sql : sqlList) {
				logger.info(formatSql(sql));
				sqlStament.executeUpdate(sql);
			}
			connection.commit();
			return sqlList.size();
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + "]", e);
			return -1;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + "]", e);
			return -1;
		} finally {
			try {
				connection.rollback();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public int update(String sql, Object param) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		try {
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			if (param instanceof String) {
				sqlStament.setString(1, (String) param);
			} else if (param instanceof Integer) {
				sqlStament.setInt(1, (int) param);
			} else {
				sqlStament.setObject(1, param);
			}
			logger.info(formatSql(sql, param));
			return sqlStament.executeUpdate();
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return -1;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return -1;
		} finally {
			try {
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public int update(String sql, @SuppressWarnings("rawtypes") ArrayList list) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		try {
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) instanceof String) {
					sqlStament.setString(i + 1, (String) list.get(i));
				} else if (list.get(i) instanceof Integer) {
					sqlStament.setInt(i + 1, (int) list.get(i));
				} else {
					sqlStament.setObject(i + 1, list.get(i));
				}
			}
			logger.info(formatSql(sql, list.toArray()));
			return sqlStament.executeUpdate();
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return -1;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return -1;
		} finally {
			try {
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private String getColumnTypeName(ResultSet resultSet, int column) throws SQLException {
		String columnType = resultSet.getMetaData().getColumnTypeName(column);
		return columnType;
	}

	private String getCellValue(ResultSet resultSet, int column) throws SQLException, IOException {
		String result = null;
		String columnType = null;
		columnType = getColumnTypeName(resultSet, column);
		if (columnType.equalsIgnoreCase("VARCHAR2") || columnType.equalsIgnoreCase("VARCHAR")
				|| columnType.equalsIgnoreCase("CHAR") || columnType.equalsIgnoreCase("mediumtext")) {
			result = resultSet.getString(column) == null ? "" : resultSet.getString(column);
		} else if (columnType.equalsIgnoreCase("TIMESTAMP")) {
			result = resultSet.getTimestamp(column) == null ? "" : String.valueOf(resultSet.getTimestamp(column));
		} else if (columnType.equalsIgnoreCase("CLOB")) {
			result = clobToString(resultSet.getClob(column));
		} else if (columnType.equalsIgnoreCase("NUMBER") || columnType.equalsIgnoreCase("INTEGER")
				|| columnType.equalsIgnoreCase("int")) {
			result = String.valueOf(resultSet.getInt(column)) == null ? "" : String.valueOf(resultSet.getInt(column));
		} else if (columnType.equalsIgnoreCase("DATE") || columnType.equalsIgnoreCase("datetime")) {
			result = resultSet.getDate(column) == null ? "" : String.valueOf(resultSet.getDate(column));
		} else if (columnType.equalsIgnoreCase("BLOB")) {
			result = BlobToString(resultSet.getBlob(column)) == null ? "" : BlobToString(resultSet.getBlob(column));
		} else if (columnType.equalsIgnoreCase("bigint") || columnType.equalsIgnoreCase("decimal")) {
			result = resultSet.getBigDecimal(column) == null ? "" : String.valueOf(resultSet.getBigDecimal(column));
		}
		return result;

	}

	private String clobToString(Clob clob) throws SQLException, IOException {
		String reString = "";
		if (clob == null) {
			return reString;
		}
		Reader is = clob.getCharacterStream();
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}

	private String BlobToString(Blob blob) throws SQLException {
		if (blob == null) {
			return "";
		}
		String content = new String(blob.getBytes((long) 1, (int) blob.length()));
		return content;
	}

	public ArrayList<ArrayList<String>> queryOfList(String sql) {
		DruidPooledConnection connection = null;
		Statement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.createStatement();
			set = sqlStament.executeQuery(sql);
			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
			while (set != null && set.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < colCount; i++) {
					temp.add(getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<HashMap<String, String>> queryOfMap(String sql) {
		DruidPooledConnection connection = null;
		Statement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.createStatement();
			set = sqlStament.executeQuery(sql);
			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
			while (set != null && set.next()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				for (int i = 0; i < colCount; i++) {
					temp.put(rsmd.getColumnName(i + 1).toUpperCase(), getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql), e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<ArrayList<String>> queryOfList(String sql, String param) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, param));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			sqlStament.setString(1, param);
			set = sqlStament.executeQuery();

			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
			while (set != null && set.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < colCount; i++) {
					temp.add(getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<HashMap<String, String>> queryOfMap(String sql, String param) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, param));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			sqlStament.setString(1, param);
			set = sqlStament.executeQuery();

			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
			while (set != null && set.next()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				for (int i = 0; i < colCount; i++) {
					temp.put(rsmd.getColumnName(i + 1).toUpperCase(), getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<ArrayList<String>> queryOfList(String sql, int param) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, param));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			sqlStament.setInt(1, param);
			set = sqlStament.executeQuery();
			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
			while (set != null && set.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < colCount; i++) {
					temp.add(getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<HashMap<String, String>> queryOfMap(String sql, int param) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, param));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			sqlStament.setInt(1, param);
			set = sqlStament.executeQuery();

			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
			while (set != null && set.next()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				for (int i = 0; i < colCount; i++) {
					temp.put(rsmd.getColumnName(i + 1).toUpperCase(), getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, param) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<ArrayList<String>> queryOfList(String sql, Object x, Object y) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, x, y));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			sqlStament.setObject(1, x);
			sqlStament.setObject(2, y);
			set = sqlStament.executeQuery();
			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
			while (set != null && set.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < colCount; i++) {
					temp.add(getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, x, y) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, x, y) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<HashMap<String, String>> queryOfMap(String sql, Object x, Object y) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, x, y));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			sqlStament.setObject(1, x);
			sqlStament.setObject(2, y);
			set = sqlStament.executeQuery();

			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
			while (set != null && set.next()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				for (int i = 0; i < colCount; i++) {
					temp.put(rsmd.getColumnName(i + 1).toUpperCase(), getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, x, y) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, x, y) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList<String>> queryOfList(String sql, ArrayList list) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, list.toArray()));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				sqlStament.setObject(i + 1, list.get(i));
			}
			set = sqlStament.executeQuery();
			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<ArrayList<String>> resultList = new ArrayList<ArrayList<String>>();
			while (set != null && set.next()) {
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < colCount; i++) {
					temp.add(getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, list.toArray()) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, list.toArray()) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<HashMap<String, String>> queryOfMap(String sql, ArrayList list) {
		DruidPooledConnection connection = null;
		PreparedStatement sqlStament = null;
		ResultSet set = null;
		try {
			logger.info(formatSql(sql, list.toArray()));
			connection = DbPoolUtil.getInstance().getConnection(fileName);
			sqlStament = connection.prepareStatement(sql);
			for (int i = 0; i < list.size(); i++) {
				sqlStament.setObject(i + 1, list.get(i));
			}
			set = sqlStament.executeQuery();
			ResultSetMetaData rsmd = set.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
			while (set != null && set.next()) {
				HashMap<String, String> temp = new HashMap<String, String>();
				for (int i = 0; i < colCount; i++) {
					temp.put(rsmd.getColumnName(i + 1).toUpperCase(), getCellValue(set, i + 1));
				}
				resultList.add(temp);
			}
			return resultList;
		} catch (SQLException e) {
			logger.error("数据库操作失败：" + formatSql(sql, list.toArray()) + "]", e);
			return null;
		} catch (Exception e) {
			logger.error("数据库操作失败：" + formatSql(sql, list.toArray()) + "]", e);
			return null;
		} finally {
			try {
				set.close();
				sqlStament.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public String getValue(ArrayList<HashMap<String, String>> resultList, String colName, int rowNum) {
		try {
			HashMap<String, String> tempMap = resultList.get(rowNum);
			return tempMap.get(colName.toUpperCase());
		} catch (Exception e) {
			logger.error("从数据库查询到的结果集中获取指定列的值发生异常，结果集size[" + resultList.size() + "]请求行[" + rowNum + "]请求列[" + colName,
					e);
			return null;
		}
	}

	public String getValue(String sql, String colName, int rowNum) {
		ArrayList<HashMap<String, String>> listOfMap = new ArrayList<HashMap<String, String>>();
		try {
			listOfMap = queryOfMap(sql);
			HashMap<String, String> tempMap = listOfMap.get(rowNum);
			String value = tempMap.get(colName);
			return value;
		} catch (Exception e) {
			logger.error(formatSql(sql) + "从数据库查询到的结果集中获取指定列的值发生异常，结果集size[" + listOfMap.size() + "]请求行[" + rowNum
					+ "]请求列[" + colName + "]", e);
			return null;
		}
	}

	public Object getValue(ArrayList<ArrayList<String>> resultList, int rowNum, int colNum) {
		try {
			ArrayList<String> tempList = resultList.get(rowNum);
			return tempList.get(colNum);
		} catch (Exception e) {
			logger.error("从数据库查询到的结果集中获取指定列的值发生异常，结果集size[" + resultList.size() + "]请求行[" + rowNum + "]请求列[" + colNum,
					e);
			return null;
		}
	}

	public String getValue(String sql, int rowNum, int colNum) {
		ArrayList<ArrayList<String>> listOfList = new ArrayList<ArrayList<String>>();
		try {
			listOfList = queryOfList(sql);
			ArrayList<String> tempList = listOfList.get(rowNum);
			String value = tempList.get(colNum);
			return value;
		} catch (Exception e) {
			logger.error(formatSql(sql) + "从数据库查询到的结果集中获取指定列的值发生异常，结果集size[" + listOfList.size() + "]请求行[" + rowNum
					+ "]请求列[" + colNum + "]", e);
			return null;
		}
	}

	public String getValue(ArrayList<ArrayList<String>> resultList, int colNum) {
		try {
			return getValue(resultList, 0, colNum) == null ? "" : getValue(resultList, 0, colNum).toString();
		} catch (Exception e) {
			logger.error("从数据库查询到的结果集中获取指定列的值发生异常，结果集size[" + resultList.size() + "]请求列[" + colNum + "]", e);
			return null;
		}
	}

	public String getValue(ArrayList<HashMap<String, String>> resultList, String colname) {
		try {

			return getValue(resultList, colname, 0) == null ? "" : getValue(resultList, colname, 0).toString();
		} catch (Exception e) {
			logger.error("从数据库查询到的结果集中获取指定列的值发生异常，结果集size[" + resultList.size() + "]请求列[" + colname + "]", e);
			return null;
		}
	}

	public String formatSql(String presql, Object... args) {
		for (int i = 0; i < args.length; i++) {
			presql = presql.replaceFirst("\\?", String.valueOf(args[i]));
		}
		return presql;
	}

}
