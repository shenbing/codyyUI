package com.coddy.parser;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * xml������
 * 
 * @author shenbing
 * 
 */
public class XmlReader
{
	/**
	 * xml�ļ�����
	 */
	private File xmlFile = null;

	/**
	 * Ĭ�Ϲ��췽��
	 * 
	 * @param file
	 */
	public XmlReader(File file)
	{
		this.xmlFile = file;
	}

	/**
	 * ����xml�ļ�����ȡ����
	 * 
	 * @return xml�ļ����ݵ�HashMap
	 * @throws DocumentException
	 *             xml�ĵ��쳣
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getDataMap() throws DocumentException
	{
		HashMap map = new HashMap();
		SAXReader reader = new SAXReader();
		Document document = reader.read(xmlFile);
		Element node = document.getRootElement();
		for (Iterator<?> i = node.elementIterator(); i.hasNext();)
		{
			HashMap<String, String> tempMap = new HashMap<String, String>();
			Element pageElement = (Element) i.next();
			String name = pageElement.getName();
			String desc = pageElement.getText();
			for (Iterator<?> j = pageElement.attributeIterator(); j.hasNext();)
			{
				Attribute elementAttr = (Attribute) j.next();
				String key = elementAttr.getName();
				String value = elementAttr.getValue();
				tempMap.put("desc", desc);
				tempMap.put(key, value);
				break;
			}
			map.put(name, tempMap);
		}
		return map;
	}
}
