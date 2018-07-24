package com.coddy.utils;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

/**
 * 获取win系统中的系统进程
 * 
 * @author shenbing
 * 
 */
public class WindowsTasklist
{

	/**
	 * 默认构造方法
	 */
	private WindowsTasklist()
	{
	}

	/**
	 * 获取WindowsTasklist实例
	 * 
	 * @return WindowsTasklist实例
	 */
	public static WindowsTasklist getInstance()
	{
		return new WindowsTasklist();
	}

	/**
	 * 获取系统进程List
	 * 
	 * @return 系统进程List
	 */
	public ArrayList<String> getTaskList()
	{
		String cmd = "cmd /c tasklist";
		Process process = null;
		ArrayList<String> list = new ArrayList<String>();
		String line = null;
		int i = 0;
		try
		{
			process = Runtime.getRuntime().exec(cmd);
			InputStream is = process.getInputStream();
			InputStreamReader ir = new InputStreamReader(is);
			LineNumberReader input = new LineNumberReader(ir);
			line = input.readLine();
			while (line != null)
			{
				if (i >= 3)
				{
					String taskName = paserLine(line);
					if (!list.contains(taskName))
					{
						list.add(taskName);
					}
				}
				i++;
				line = input.readLine();
			}
		}
		catch (java.io.IOException e)
		{
			System.err.println("IOException " + e.getMessage());
		}
		finally
		{
			if (process != null)
			{
				process.destroy();
			}
		}
		return list;
	}

	/**
	 * 数据格式化处理
	 * 
	 * @param line
	 *            读取的单个进程值
	 * @return 数据处理后的值
	 */
	private String paserLine(String line)
	{
		String row = line;
		String name = row.substring(0, 30).trim();
		return name;

	}
}
