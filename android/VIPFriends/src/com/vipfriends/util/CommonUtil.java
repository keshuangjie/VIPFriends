package com.vipfriends.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.vipfriends.MyApplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.LayoutInflater;

/**
 * CommonUtil 工具类
 *
 *
 */
public final class CommonUtil{

	private static final String TAG = "CommonUtil";
	private static String marketPriceFlag = null;// 服务端下发的全局市场价开关

	public static LayoutInflater getLayoutInflater() {
		return LayoutInflater.from(MyApplication.getContext());
	}

	/**
	 * 检测网络连接是否可用
	 *
	 * @param ctx
	 * @return true 可用; false 不可用
	 */
	public static boolean CheckNetWork() {
		// 连接管理器
		ConnectivityManager cm = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		}
		NetworkInfo[] netinfo = cm.getAllNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		for (int i = 0; i < netinfo.length; i++) {
			if (netinfo[i].isConnected()) {
				return true;
			}
		}
		return false;
	}



	/**
	 * Copyright 2011 Jingdong Android Mobile Application
	 *
	 * @author lijingzuo
	 *
	 *         Time: 2011-9-6 上午09:35:56
	 *
	 *         Name:
	 *
	 *         Description:
	 *
	 * @see #queryBrowserUrl(String action, URLParamMap params,
	 *      BrowserUrlListener listener)
	 *
	 */
	public interface BrowserUrlListener {
		void onComplete(String url);
	}

	public static Intent newBrowserIntent(Uri uri, boolean force) {
		Intent i = new Intent(Intent.ACTION_VIEW, uri);

		// 由于外部浏览器可能无法准确跳转回来，所以现在只调用系统的浏览器
		if (force) {
			i.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
		}

		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return i;
	}

	/**
	 * 判断intent是否有效
	 */
	public static boolean isIntentAvailable(Intent intent) {
		PackageManager packageManager = MyApplication.getContext().getPackageManager();
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		boolean isSupter = false;
		if(list!=null&&list.size() > 0){
			isSupter = true;
		}

		return isSupter;
	}



	public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			DLog.v("WifiPreference IpAddress", ex.toString());
		}
		return null;
	}

	/**
	 * 检测SDcard是否存在
	 *
	 * @return true:存在、false:不存在
	 */
	public static boolean checkSDcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}

	/**
	 * 检查EMAIL地址 用户名和网站名称必须>=1位字符
	 * */
	public static boolean checkEmailWithSuffix(String email) {
		String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		return startCheck(regex, email);
	}

	public static boolean startCheck(String reg, String string) {
		boolean tem = false;

		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(string);

		tem = matcher.matches();
		return tem;
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_""-",汉字， 用户名有最小长度和最大长度限制，比如用户名必须是4-20位
	 * */
	public static boolean checkUsername(String username, int min, int max) {
		String regex = "[\\w\u4e00-\u9fa5\\-a-zA-Z0-9_]{" + min + "," + max + "}";
		return startCheck(regex, username);
	}

	/**
	 * 检验密码 取值范围为a-z,A-Z,0-9,"_","-"，不能以"_"结尾 用户名有最小长度和最大长度限制，比如密码必须是6-20位 //
	 *
	 * @param password
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean checkPassword(String password, int min, int max) {

		String regex = "[a-zA-Z_0-9\\-]{" + min + "," + max + "}";
		return startCheck(regex, password);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾 有最小位数限制的用户名，比如：用户名最少为4位字符
	 * */
	public static boolean checkUsername(String username, int min) {
		// [\\w\u4e00-\u9fa5]{2,}?
		String regex = "[\\w\u4e00-\u9fa5\\-a-zA-Z0-9_]{" + min + ",}";

		return startCheck(regex, username);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_","-",汉字 最少一位字符，最大字符位数无限制
	 * */
	public static boolean checkUsername(String username) {
		String regex = "[\\w\u4e00-\u9fa5\\-a-zA-Z0-9_]+";
		return startCheck(regex, username);
	}

	/**
	 * 检验用户名 取值范围为a-z,A-Z,0-9,"_","-",汉字 最少一位字符，最大字符位数无限制
	 * */
	public static boolean checkAddrWithSpace(String username) {
		String regex = "[\\w\u4e00-\u9fa5\\-\\x20]+";
		return startCheck(regex, username);
	}

	/**
	 *
	 * @author YangHuijun
	 *
	 *         Time: 2011-3-4 下午03:25:46
	 *
	 *         Name: 取得字符串的长度
	 *
	 * @return:
	 *
	 *          Description: 取得字符串的长度，中文2个长度，中文以外1个长度
	 */
	public static int getLength(String checkStr) {
		char[] nameChars = checkStr.toCharArray();
		int length = 0;
		for (int i = 0; i < nameChars.length; i++) {
			if (isChinese(nameChars[i])) {
				length = length + 2;
			} else {
				length = length + 1;
			}
		}
		return length;
	}

	/**
	 * @author YangHuijun
	 *
	 *         Time: 2011-3-4 下午03:27:42
	 *
	 *         Name: 是否中文
	 *
	 * @return: true-中文 false-非中文
	 *
	 *          Description: 判断给定字符是否中文
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

}
