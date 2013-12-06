package com.vipfriends;

import com.vipfriends.util.DLog;
import com.vipfriends.util.FileService;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

/**
 * 应用管理类
 * @author keshuangjie
 */
public class MyApplication extends Application {

	private static MyApplication instance;
	private static Context context;

	public static MyApplication getInstance() {
		return instance;
	}

	public static Context getContext() {
		return context;
	}

	@Override
	public void onCreate() {
		if (DLog.D) {
			DLog.d("MyApplication", "MyApplication onCreate() -->> Process.myPid() "
					+ Process.myPid());
		}
		super.onCreate();
		instance = this;
		context = getApplicationContext();
		// 如果debug模式 不捕获 crash release 捕获
		if (!BuildConfig.DEBUG)
			Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler(
					this));
		
		FileService.createRootDir();

	}

	/**
	 * 强制退出（杀后台进程，不更新widget和message）
	 */
	public static void exitAll() {
		if (DLog.D) {
			DLog.d("Temp", "MyApplication exitAll() -->> ");
		}
		// 让后台进程去清理缓存文件
		clearCache();

		// 强制杀掉后台进程
		killBackground();
		System.exit(1);
		// 强制杀掉前台进程
		killStage();

	}

	/**
	 * 强制杀掉前台进程
	 */
	public static void killStage() {
		if (DLog.D) {
			DLog.d("Temp", "MyApplication killStage() -->> ");
		}

		Process.killProcess(Process.myPid());

	}

	/**
	 * 让后台进程去清理缓存文件
	 */
	public static void clearCache() {
		if (DLog.D) {
			DLog.d("Temp", "MyApplication clearCache() -->> ");
		}

		// 清理coookies（寄居）

	}

	/**
	 * 强制杀掉后台进程（如果正在清理缓存文件，那么会在清理缓存文件后自杀）
	 */
	public static void killBackground() {
		if (DLog.D) {
			DLog.d("Temp", "MyApplication killBackground() -->> ");
		}
	}


	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				break;
			}
		}
	};

	/**
	 * 统一 post 接口
	 */
	public void post(final Runnable action) {
		// Log.i("zhoubo", "handler==="+handler);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				action.run();
			}
		});
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

}