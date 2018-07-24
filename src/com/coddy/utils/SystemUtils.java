package com.coddy.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * 系统操作类
 * 
 * @author shenbing
 * 
 */
public class SystemUtils {
	/**
	 * 获取浏览器信息
	 */
	public final static String STR_NAV_JS = " return navigator.userAgent.toLowerCase() ; ";

	/**
	 * 命令行 cmd
	 */
	public static String CMD_KILL = "cmd /c tskill ";
	/**
	 * 关闭IE driver 进程
	 */
	public static String CMD_KILL_IEDRIVER = CMD_KILL + "IEDriverServer";
	/**
	 * 关闭IE进程
	 */
	public static String CMD_KILL_IE = CMD_KILL + "iexplore";
	/**
	 * 关闭Firefox进程
	 */
	public static String CMD_KILL_FIREFOX = CMD_KILL + "firefox";
	/**
	 * 关闭Chrome进程
	 */
	public static String CMD_KILL_CHROME = CMD_KILL + "chrome";
	/**
	 * 关闭Chrome进程
	 */
	public static String CMD_KILL_CHROMEDRIVER = CMD_KILL + "chromedriver";

	/**
	 * 关闭firefox driver进程
	 */
	public static String CMD_KILL_FIREFOXDRIVER = CMD_KILL + "geckodriver";

	public static String CMD_DELETE_TMPFILES = "cmd /c del /f /s /q %systemdrive%\\*.tmp";

	/**
	 * 获取日期
	 * 
	 * @return String yyyy-MM-dd-HH-mm-ss格式化后日期信息
	 */
	public static String getScrennShotTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 执行Windows系统命令
	 * 
	 * @param cmd
	 *            需要执行的命令
	 */
	public static void executeWinCMD(String cmd) {
		Process pr = null;
		try {
			Runtime rt = Runtime.getRuntime();
			pr = rt.exec(cmd);
			pr.waitFor();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pr != null) {
				pr.destroy();
			}
		}
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行带参数的Windows系统命令
	 * 
	 * @param cmd
	 *            需要执行的命令
	 * @param params
	 *            系统命令参数
	 */
	public static void executeWinCMD(String cmd, String... params) {
		Process pr = null;
		for (int i = 0; i < params.length; i++) {
			cmd = cmd + " " + params[0];
		}
		try {
			Runtime rt = Runtime.getRuntime();
			pr = rt.exec(cmd);
			pr.waitFor();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pr != null) {
				pr.destroy();
			}
		}
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭所有IE窗口
	 */
	public static void killIE() {
		executeWinCMD(CMD_KILL_IE);
	}

	/**
	 * 关闭所有IeDriverServer进程
	 */
	public static void killIeDriverSever() {
		executeWinCMD(CMD_KILL_IEDRIVER);
	}

	/**
	 * 关闭所有Firefox进程
	 */
	public static void killFirefox() {
		executeWinCMD(CMD_KILL_FIREFOX);
	}

	/**
	 * 关闭所有chrome
	 */
	public static void killChrome() {
		executeWinCMD(CMD_KILL_CHROME);
	}

	/**
	 * 关闭所有chromeDriver进程
	 */
	public static void killChromeDriver() {
		executeWinCMD(CMD_KILL_CHROMEDRIVER);
	}

	/**
	 * 关闭所有firefoxDriver进程
	 */
	public static void killFirefoxDriver() {
		executeWinCMD(CMD_KILL_FIREFOXDRIVER);
	}

	/**
	 * 删除运行过程中产生的临时文件
	 */
	public static void deleteTmpFiles() {
		executeWinCMD(CMD_DELETE_TMPFILES);
	}

	/**
	 * 获取系统进程
	 * 
	 * @return 系统进程的ArrayList
	 */
	public static ArrayList<String> getTaskList() {
		WindowsTasklist list = WindowsTasklist.getInstance();
		return list.getTaskList();
	}

	/**
	 * 获取系统信息
	 * 
	 * @param cmd
	 *            系统参数信息，如os.name、os.version
	 * @return 参数指定的系统信息
	 */
	public static String getSystemInfo(String cmd) {
		return System.getProperty(cmd);
	}

	/**
	 * MD5加密
	 * 
	 * @param string
	 *            需要加密的字符串
	 * @return 返回加密后的字符串
	 */
	public static String encodeMD5(String string) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		String result = null;
		try {
			byte[] btInput = string.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			result = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("_", "");
	}

	/**
	 * MD5加密
	 * 
	 */
	public static String getMD5(String string) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(string.getBytes());
			return new BigInteger(1, md5.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取当前时间，格式为yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar calendar = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(calendar.getTime());
	}

}