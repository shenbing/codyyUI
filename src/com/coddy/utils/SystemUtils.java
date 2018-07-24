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
 * ϵͳ������
 * 
 * @author shenbing
 * 
 */
public class SystemUtils {
	/**
	 * ��ȡ�������Ϣ
	 */
	public final static String STR_NAV_JS = " return navigator.userAgent.toLowerCase() ; ";

	/**
	 * ������ cmd
	 */
	public static String CMD_KILL = "cmd /c tskill ";
	/**
	 * �ر�IE driver ����
	 */
	public static String CMD_KILL_IEDRIVER = CMD_KILL + "IEDriverServer";
	/**
	 * �ر�IE����
	 */
	public static String CMD_KILL_IE = CMD_KILL + "iexplore";
	/**
	 * �ر�Firefox����
	 */
	public static String CMD_KILL_FIREFOX = CMD_KILL + "firefox";
	/**
	 * �ر�Chrome����
	 */
	public static String CMD_KILL_CHROME = CMD_KILL + "chrome";
	/**
	 * �ر�Chrome����
	 */
	public static String CMD_KILL_CHROMEDRIVER = CMD_KILL + "chromedriver";

	/**
	 * �ر�firefox driver����
	 */
	public static String CMD_KILL_FIREFOXDRIVER = CMD_KILL + "geckodriver";

	public static String CMD_DELETE_TMPFILES = "cmd /c del /f /s /q %systemdrive%\\*.tmp";

	/**
	 * ��ȡ����
	 * 
	 * @return String yyyy-MM-dd-HH-mm-ss��ʽ����������Ϣ
	 */
	public static String getScrennShotTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH-mm-ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ִ��Windowsϵͳ����
	 * 
	 * @param cmd
	 *            ��Ҫִ�е�����
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
	 * ִ�д�������Windowsϵͳ����
	 * 
	 * @param cmd
	 *            ��Ҫִ�е�����
	 * @param params
	 *            ϵͳ�������
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
	 * �ر�����IE����
	 */
	public static void killIE() {
		executeWinCMD(CMD_KILL_IE);
	}

	/**
	 * �ر�����IeDriverServer����
	 */
	public static void killIeDriverSever() {
		executeWinCMD(CMD_KILL_IEDRIVER);
	}

	/**
	 * �ر�����Firefox����
	 */
	public static void killFirefox() {
		executeWinCMD(CMD_KILL_FIREFOX);
	}

	/**
	 * �ر�����chrome
	 */
	public static void killChrome() {
		executeWinCMD(CMD_KILL_CHROME);
	}

	/**
	 * �ر�����chromeDriver����
	 */
	public static void killChromeDriver() {
		executeWinCMD(CMD_KILL_CHROMEDRIVER);
	}

	/**
	 * �ر�����firefoxDriver����
	 */
	public static void killFirefoxDriver() {
		executeWinCMD(CMD_KILL_FIREFOXDRIVER);
	}

	/**
	 * ɾ�����й����в�������ʱ�ļ�
	 */
	public static void deleteTmpFiles() {
		executeWinCMD(CMD_DELETE_TMPFILES);
	}

	/**
	 * ��ȡϵͳ����
	 * 
	 * @return ϵͳ���̵�ArrayList
	 */
	public static ArrayList<String> getTaskList() {
		WindowsTasklist list = WindowsTasklist.getInstance();
		return list.getTaskList();
	}

	/**
	 * ��ȡϵͳ��Ϣ
	 * 
	 * @param cmd
	 *            ϵͳ������Ϣ����os.name��os.version
	 * @return ����ָ����ϵͳ��Ϣ
	 */
	public static String getSystemInfo(String cmd) {
		return System.getProperty(cmd);
	}

	/**
	 * MD5����
	 * 
	 * @param string
	 *            ��Ҫ���ܵ��ַ���
	 * @return ���ؼ��ܺ���ַ���
	 */
	public static String encodeMD5(String string) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		String result = null;
		try {
			byte[] btInput = string.getBytes();
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			mdInst.update(btInput);
			// �������
			byte[] md = mdInst.digest();
			// ������ת����ʮ�����Ƶ��ַ�����ʽ
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
	 * ��ȡUUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString().replace("_", "");
	}

	/**
	 * MD5����
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
	 * ��ȡ��ǰʱ�䣬��ʽΪyyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		Calendar calendar = java.util.Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(calendar.getTime());
	}

}