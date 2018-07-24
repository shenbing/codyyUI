package com.coddy.parser;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.coddy.DB.DbUnit;

/**
 * DB数据解析类
 * 
 * @author shenbing
 * 
 */

public class DbReader
{
	/**
	 * 日志句柄
	 */
	private Log logger = LogFactory.getLog(DbReader.class.getName());

	/**
	 * DB table名
	 */
	private String tableName;

	/**
	 * DBtable的HashMap<String, HashMap>
	 */
	private HashMap<String, HashMap<String, String>> map;

	// /**
	// * DBtable的HashMap
	// */
	// private HashMap<String, String> hmValue;

	/**
	 * 构造方法
	 * 
	 * @param tableName
	 *            table名
	 */
	public DbReader(String tableName)
	{
		this.tableName = tableName;
		this.map = new HashMap<String, HashMap<String, String>>();
		// this.hmValue = new HashMap<String, String>();
	}

	/**
	 * 读取数据库获取数据
	 * 
	 * @return tableName表的HashMap&lt;String, HashMap&gt;
	 * @throws Exception
	 *             抛出异常类型
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getDataMap() throws Exception
	{

		List<HashMap<String, String>> resultList = new DbUnit("db.properties")
				.queryOfMap("select * from " + tableName);
		for (HashMap<String, String> hm : resultList)
		{
			String key = hm.get("KEY");
			String id = hm.get("ID");
			String css = hm.get("CSS");
			String xpath = hm.get("XPATH");
			String name = hm.get("NAME");
			String linktext = hm.get("LINKTEXT");
			String classname = hm.get("CLASSNAME");
			String desc = hm.get("DESC");
			HashMap<String, String> hmValue = new HashMap<String, String>();
			// 当且只有id,css,xpath,name有且只有一个的时候才正确
			int idflag = 0;
			int cssflag = 0;
			int xpathflag = 0;
			int nameflag = 0;
			int linktextflag = 0;
			int classnameflag = 0;
			if (id != null)
			{
				idflag++;
			}
			if (css != null)
			{
				cssflag++;
			}
			if (xpath != null)
			{
				xpathflag++;
			}
			if (name != null)
			{
				nameflag++;
			}
			if (linktext != null)
			{
				linktextflag++;
			}
			if (classname != null)
			{
				classnameflag++;
			}
			// 当且只有id,css,xpath,name有且只有一个的时候才正确
			if (key != null)
			{
				if (idflag + cssflag + xpathflag + nameflag + linktextflag
						+ classnameflag == 1)
				{
					if (idflag == 1)
					{
						hmValue.put("id", id);
					}
					else if (cssflag == 1)
					{
						hmValue.put("css", css);
					}
					else if (xpathflag == 1)
					{
						hmValue.put("xpath", xpath);
					}
					else if (nameflag == 1)
					{
						hmValue.put("name", name);
					}
					else if (linktextflag == 1)
					{
						hmValue.put("linktext", name);
					}
					else if (classnameflag == 1)
					{
						hmValue.put("classname", name);
					}
				}
				else
				{
					logger.error("请检查元素" + key
							+ "数据表中的id,css,xpath,name。要求有且只有一个不为空");
					throw new Exception("元素定位文件错误，请检查key为" + key + "的记录");
				}
				if (desc != null)
				{
					hmValue.put("desc", desc);
				}
				else
				{
					hmValue.put("desc", "");
				}
				map.put(key, hmValue);
			}
			else
			{
				logger.error("数据表存在key为null的记录");
				throw new Exception("数据表存在key为null的记录");
			}
		}
		return map;
	}
}
