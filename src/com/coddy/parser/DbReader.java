package com.coddy.parser;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.coddy.DB.DbUnit;

/**
 * DB���ݽ�����
 * 
 * @author shenbing
 * 
 */

public class DbReader
{
	/**
	 * ��־���
	 */
	private Log logger = LogFactory.getLog(DbReader.class.getName());

	/**
	 * DB table��
	 */
	private String tableName;

	/**
	 * DBtable��HashMap<String, HashMap>
	 */
	private HashMap<String, HashMap<String, String>> map;

	// /**
	// * DBtable��HashMap
	// */
	// private HashMap<String, String> hmValue;

	/**
	 * ���췽��
	 * 
	 * @param tableName
	 *            table��
	 */
	public DbReader(String tableName)
	{
		this.tableName = tableName;
		this.map = new HashMap<String, HashMap<String, String>>();
		// this.hmValue = new HashMap<String, String>();
	}

	/**
	 * ��ȡ���ݿ��ȡ����
	 * 
	 * @return tableName���HashMap&lt;String, HashMap&gt;
	 * @throws Exception
	 *             �׳��쳣����
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
			// ����ֻ��id,css,xpath,name����ֻ��һ����ʱ�����ȷ
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
			// ����ֻ��id,css,xpath,name����ֻ��һ����ʱ�����ȷ
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
					logger.error("����Ԫ��" + key
							+ "���ݱ��е�id,css,xpath,name��Ҫ������ֻ��һ����Ϊ��");
					throw new Exception("Ԫ�ض�λ�ļ���������keyΪ" + key + "�ļ�¼");
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
				logger.error("���ݱ����keyΪnull�ļ�¼");
				throw new Exception("���ݱ����keyΪnull�ļ�¼");
			}
		}
		return map;
	}
}
