package com.coddy.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;

/**
 * 读取配置文件类，config.properties文件是必须的，默认加载该文件
 * 
 * @author shenbing
 * 
 */

public class ConfigProperties {
	private static HashMap<String, String> map = new HashMap<String, String>();

	private static ConfigProperties configProperties = null;

	private ConfigProperties() {
		loadFile("config.properties");
	}

	public synchronized static ConfigProperties getInstance() {
		if (configProperties == null) {
			configProperties = new ConfigProperties();
		}
		return configProperties;

	}

	private void loadFile(String properties) {
		String filePath = System.getProperty("user.dir") + "/conf/" + properties;
		InputStream in = null;
		InputStreamReader ir = null;
		LineNumberReader input = null;
		try {
			in = new BufferedInputStream(new FileInputStream(filePath));
			ir = new InputStreamReader(in);
			input = new LineNumberReader(ir);
			String line;
			line = input.readLine();
			while (line != null) {
				parceLine(line);
				line = input.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ir != null) {
					ir.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void parceLine(String line) {
		String str = line;
		String tmp = str.trim();
		if (tmp.startsWith("#") || tmp.isEmpty()) {
			return;
		}
		String[] value = tmp.split("=", 2);
		if (value.length != 2) {
			System.out.println(tmp);
		}
		map.put(value[0].trim(), value[1].trim());

	}

	public String getString(String key) {
		return map.get(key);
	}

	public int getInt(String key) {
		return Integer.valueOf(map.get(key));
	}
}