package com.vipfriends;

import android.content.Context;
import android.os.Process;

/**
 * 未捕获异常接口 处理实现类
 * @author keshuangjie
 */
public class MyUncaughtExceptionHandler implements
		Thread.UncaughtExceptionHandler {

	private final Thread.UncaughtExceptionHandler mOldUncaughtExceptionHandler;

	public MyUncaughtExceptionHandler(Context c) {
		mOldUncaughtExceptionHandler = Thread
				.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(final Thread thread, final Throwable ex) {
		if (!myUncaughtException(thread, ex)
				&& mOldUncaughtExceptionHandler != null) {
			ex.printStackTrace();
			mOldUncaughtExceptionHandler.uncaughtException(thread, ex);
		} else {
			ex.printStackTrace();
			android.os.Process.killProcess(Process.myTid());
			System.exit(0);
		}
	}

	private boolean myUncaughtException(Thread thread, final Throwable ex) {
//		Intent i = new Intent(context, ErrorActivity.class);
//		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		try {
//			if (Log.D) {
//				ex.printStackTrace();
//			}
//			Writer w = new StringWriter();
//			ex.printStackTrace(new PrintWriter(w));
//			String error = w.append("\n\n").append(CommonUtil.getDevicesInfo())
//					.toString();
//			i.putExtra("exception", ex);
//			i.putExtra("user", StatisticsReportUtil.getReportString(true, true));
//			i.putExtra("error", error);
//			context.startActivity(i);
//		} catch (Exception e) {
			System.exit(0);
//		}
		return true;
	}

}
