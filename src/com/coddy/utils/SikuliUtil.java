package com.coddy.utils;

import java.io.File;
import org.apache.commons.logging.Log;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Match;
import org.sikuli.script.Screen;

public class SikuliUtil {

	public static Log log = LogUtils.getLog(SikuliUtil.class);

	public static Screen screen = new Screen();

	public static void click(String imageFile) {
		try {
			if (isFile(imageFile)) {
				screen.click(imageFile);
			}
		} catch (FindFailed e) {
			String message = "未找到" + imageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
	}

	public static void doubleClick(String imageFile) {
		try {
			if (isFile(imageFile)) {
				screen.click(imageFile);
			}
		} catch (FindFailed e) {
			String message = "未找到" + imageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
	}

	public static void rightClick(String imageFile) {
		try {
			if (isFile(imageFile)) {
				screen.rightClick(imageFile);
			}
		} catch (FindFailed e) {
			String message = "未找到" + imageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
	}

	public static void input(String imageFile, String text) {
		if (isFile(imageFile)) {
			screen.type(imageFile, text);
		}
	}

	public static void keyDown(int key) {
		screen.keyDown(key);
	}

	public static void keyDown(String key) {
		screen.keyDown(key);
	}

	public static void paste(String imageFile, String text) {
		try {
			if (isFile(imageFile)) {
				screen.paste(imageFile, text);
			}
		} catch (FindFailed e) {
			String message = "未找到" + imageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
	}

	public static void hover(String imageFile) {
		try {
			if (isFile(imageFile)) {
				screen.hover(imageFile);
			}
		} catch (FindFailed e) {
			String message = "未找到" + imageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
	}

	public static void wheel(String imageFile, int x, int y) {
		try {
			if (isFile(imageFile)) {
				screen.wheel(imageFile, x, y);
			}
		} catch (FindFailed e) {
			String message = "未找到" + imageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
	}

	public static void dragDrop(String sourceImageFile, String targetImageFile) {
		try {
			if (isFile(sourceImageFile) && isFile(targetImageFile)) {
				screen.dragDrop(sourceImageFile, targetImageFile);
			}
		} catch (FindFailed e) {
			String message = "未找到" + sourceImageFile + "或" + targetImageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
	}

	public static Match find(String imageFile) {
		Match match = null;
		try {
			if (isFile(imageFile)) {
				match = screen.find(imageFile);
			}
		} catch (FindFailed e) {
			String message = "未找到" + imageFile + "指定对象";
			log.error(message);
			ScreenCapture.screenShot(message);
			e.printStackTrace();
		}
		return match;
	}

	public static boolean exists(String imageFile) {
		Match match = null;
		if (isFile(imageFile)) {
			match = screen.exists(imageFile);
		}
		return match == null ? false : true;
	}

	public static boolean isFile(String imageFile) {
		boolean result = false;
		if (imageFile.length() == 0 || null == imageFile) {
			log.error("参数:" + imageFile + "错误");
			return result;
		}
		File file = new File(imageFile);
		if (file.exists() && file.isFile()) {
			result = true;
		} else {
			result = false;
			log.error("文件:" + imageFile + "不存在或是目录");
		}
		return result;
	}
}
