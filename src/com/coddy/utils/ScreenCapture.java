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
 * 截屏类
 * 
 * @author shenbing
 * 
 */
public class ScreenCapture {
	/**
	 * 日志句柄
	 */
	public static Log log = LogUtils.getLog(ScreenCapture.class);

	/**
	 * 
	 * @param driver
	 *            Webdriver实例对象名
	 * 
	 * @param title
	 *            图片名称描述
	 */
	public static void screenShot(WebDriver driver, String title) throws IOException {
		// 这里定义了截图存放目录名
		String dir_name = ConfigProperties.getInstance().getString("screenshot");
		if (!dir_name.contains(":")) {
			dir_name = System.getProperty("user.dir") + File.separator + dir_name;
		}
		// 判断是否存在该目录
		if (!(new File(dir_name).isDirectory())) {
			// 如果不存在则新建一个目录
			new File(dir_name).mkdir();
		}
		TakesScreenshot ts = (TakesScreenshot) driver;
		// 关键代码，执行屏幕截图，默认会把截图保存到temp目录
		File source_file = ts.getScreenshotAs(OutputType.FILE);
		// 这里将截图另存到我们需要保存的目录，例如screenshot\20120406-165210.png
		FileUtils.copyFile(source_file, new File(dir_name + File.separator + title + ".png"));
		log.info("截图\"" + title + ".png" + "\"成功！");
	}

	/**
	 * 截取电脑全屏
	 * 
	 * @param title
	 *            图片名称描述
	 */
	public static void screenShot(String title) {
		String dir_name = ConfigProperties.getInstance().getString("screenshot");
		if (!dir_name.contains(":")) {
			dir_name = System.getProperty("user.dir") + File.separator + dir_name;
		}
		// 判断是否存在该目录
		if (!(new File(dir_name).isDirectory())) {
			// 如果不存在则新建一个目录
			new File(dir_name).mkdir();
		}
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		BufferedImage screenshot = null;
		try {
			screenshot = (new Robot())
					.createScreenCapture(new Rectangle(0, 0, (int) d.getWidth(), (int) d.getHeight()));
		} catch (AWTException e) {
			log.error("截图\"" + title + ".png" + "\"失败！");
		}
		try {
			ImageIO.write(screenshot, "png", new File(dir_name + File.separator + title + ".png"));
			log.info("截图\"" + title + ".png" + "\"成功！");
		} catch (IOException e) {
			log.error("截图\"" + title + ".png" + "\"失败！");
		}
	}

	/**
	 * 电脑部分截屏
	 * 
	 * @param title
	 *            图片名称描述
	 * @param startx
	 *            截屏开始横坐标
	 * @param starty
	 *            截屏开始纵坐标
	 * @param width
	 *            截屏宽度
	 * @param height
	 *            截屏高度
	 */
	public static void screenShot(String title, int startx, int starty, int width, int height) {
		String dir_name = ConfigProperties.getInstance().getString("pngs");
		BufferedImage screenshot = null;
		try {
			screenshot = (new Robot()).createScreenCapture(new Rectangle(startx, starty, width, height));
		} catch (AWTException e) {
			log.error("截图\"" + title + ".png" + "\"失败！");
		}
		try {
			ImageIO.write(screenshot, "png", new File(dir_name + File.separator + title + ".png"));
			log.info("截图\"" + title + ".png" + "\"成功！");
		} catch (IOException e) {
			log.error("截图\"" + title + ".png" + "\"失败！");
		}
	}
}
