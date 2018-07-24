package com.coddy.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import org.ho.yaml.Yaml;

import com.coddy.utils.ConfigProperties;

/**
 * Yaml解析类
 * 
 * @author shenbing
 * 
 */
public class YamlReader {

	/**
	 * Yaml文件对象
	 */
	private File file;

	/**
	 * 构造方法
	 * 
	 * @param yamlFile
	 *            Yaml文件对象
	 */
	public YamlReader(File yamlFile) {
		this.file = yamlFile;
	}

	/**
	 * 读取Yaml文件
	 * 
	 * @return Yaml文件中数据的HashMap
	 * @throws UnsupportedEncodingException
	 *             编码异常
	 * @throws FileNotFoundException
	 *             文件不存在异常
	 * @throws IOException
	 *             文件操作异常
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getDataMap() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		String encoding = ConfigProperties.getInstance().getString("yaml_encoding");
		if ("".equals(encoding) || null == encoding) {
			encoding = "UTF-8";
		}
		InputStreamReader read = null;
		HashMap map = null;
		read = new InputStreamReader(new FileInputStream(file), encoding);
		if (read != null) {
			map = Yaml.loadType(read, HashMap.class);
			read.close();
		}
		return map;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException{
		YamlReader yamlReader = new YamlReader(new File("D:\\javaworkspace\\educationcloudui\\pagedata\\前台_工作台.yalm"));
	}
}
