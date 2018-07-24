package com.coddy.utils;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * ������
 * 
 * @author shenbing
 * 
 */
public class ScreenCapture {
	/**
	 * ��־���
	 */
	public static Log log = LogUtils.getLog(ScreenCapture.class);

	/**
	 * 
	 * @param driver
	 *            Webdriverʵ��������
	 * 
	 * @param title
	 *            ͼƬ��������
	 */
	public static void screenShot(WebDriver driver, String title) throws IOException {
		// ���ﶨ���˽�ͼ���Ŀ¼��
		String dir_name = ConfigProperties.getInstance().getString("screenshot");
		if (!dir_name.contains(":")) {
			dir_name = System.getProperty("user.dir") + File.separator + dir_name;
		}
		// �ж��Ƿ���ڸ�Ŀ¼
		if (!(new File(dir_name).isDirectory())) {
			// ������������½�һ��Ŀ¼
			new File(dir_name).mkdir();
		}
		TakesScreenshot ts = (TakesScreenshot) driver;
		// �ؼ����룬ִ����Ļ��ͼ��Ĭ�ϻ�ѽ�ͼ���浽tempĿ¼
		File source_file = ts.getScreenshotAs(OutputType.FILE);
		// ���ｫ��ͼ��浽������Ҫ�����Ŀ¼������screenshot\20120406-165210.png
		FileUtils.copyFile(source_file, new File(dir_name + File.separator + title + ".png"));
		log.info("��ͼ\"" + title + ".png" + "\"�ɹ���");
	}

	/**
	 * ��ȡ����ȫ��
	 * 
	 * @param title
	 *            ͼƬ��������
	 */
	public static void screenShot(String title) {
		String dir_name = ConfigProperties.getInstance().getString("screenshot");
		if (!dir_name.contains(":")) {
			dir_name = System.getProperty("user.dir") + File.separator + dir_name;
		}
		// �ж��Ƿ���ڸ�Ŀ¼
		if (!(new File(dir_name).isDirectory())) {
			// ������������½�һ��Ŀ¼
			new File(dir_name).mkdir();
		}
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		BufferedImage screenshot = null;
		try {
			screenshot = (new Robot())
					.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
		} catch (AWTException e) {
			log.error("��ͼ\"" + title + ".png" + "\"ʧ�ܣ�");
		}
		try {
			ImageIO.write(screenshot, "png", new File(dir_name + File.separator + title + ".png"));
			log.info("��ͼ\"" + title + ".png" + "\"�ɹ���");
		} catch (IOException e) {
			log.error("��ͼ\"" + title + ".png" + "\"ʧ�ܣ�");
		}
	}

	/**
	 * ���Բ��ֽ���
	 * 
	 * @param title
	 *            ͼƬ��������
	 * @param startx
	 *            ������ʼ������
	 * @param starty
	 *            ������ʼ������
	 * @param width
	 *            �������
	 * @param height
	 *            �����߶�
	 */
	public static void screenShot(String title, int startx, int starty, int width, int height) {
		String dir_name = ConfigProperties.getInstance().getString("pngs");
		BufferedImage screenshot = null;
		try {
			screenshot = (new Robot()).createScreenCapture(new Rectangle(startx, starty, width, height));
		} catch (AWTException e) {
			log.error("��ͼ\"" + title + ".png" + "\"ʧ�ܣ�");
		}
		try {
			ImageIO.write(screenshot, "png", new File(dir_name + File.separator + title + ".png"));
			log.info("��ͼ\"" + title + ".png" + "\"�ɹ���");
		} catch (IOException e) {
			log.error("��ͼ\"" + title + ".png" + "\"ʧ�ܣ�");
		}
	}
}
