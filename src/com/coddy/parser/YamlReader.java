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
 * Yaml������
 * 
 * @author shenbing
 * 
 */
public class YamlReader {

	/**
	 * Yaml�ļ�����
	 */
	private File file;

	/**
	 * ���췽��
	 * 
	 * @param yamlFile
	 *            Yaml�ļ�����
	 */
	public YamlReader(File yamlFile) {
		this.file = yamlFile;
	}

	/**
	 * ��ȡYaml�ļ�
	 * 
	 * @return Yaml�ļ������ݵ�HashMap
	 * @throws UnsupportedEncodingException
	 *             �����쳣
	 * @throws FileNotFoundException
	 *             �ļ��������쳣
	 * @throws IOException
	 *             �ļ������쳣
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
		YamlReader yamlReader = new YamlReader(new File("D:\\javaworkspace\\educationcloudui\\pagedata\\ǰ̨_����̨.yalm"));
	}
}
