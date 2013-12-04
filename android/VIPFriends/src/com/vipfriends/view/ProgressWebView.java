package com.vipfriends.view;

import com.vipfriends.util.DLog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ProgressWebView extends WebView {
	private static final String TAG = "ProgressWebView";
	
	private ProgressBar mProgressbar;
	private Context mContext;

	public ProgressWebView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init() {
		mProgressbar = new ProgressBar(mContext, null,
				android.R.attr.progressBarStyleHorizontal);
		mProgressbar.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, 6));
		addView(mProgressbar);

		this.getSettings().setJavaScriptEnabled(true);
		this.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		
		/** scrollBar样式 */
		this.setScrollbarFadingEnabled(true);
		this.setVerticalScrollBarEnabled(false);
		this.setOverScrollMode(WebView.OVER_SCROLL_ALWAYS);
		
		this.setWebChromeClient(new WebChromeClient());
	}
	
	@Override
	public boolean onCheckIsTextEditor() {
		return true; 
	}

	public class WebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				mProgressbar.setVisibility(GONE);
			} else {
				if (mProgressbar.getVisibility() == GONE)
					mProgressbar.setVisibility(VISIBLE);
				mProgressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			DLog.i(TAG, "Alert message: " + message);
			final String alertMessage = message;
			if (!TextUtils.isEmpty(message)) {
				Toast.makeText(mContext, alertMessage, Toast.LENGTH_LONG)
						.show();
				result.cancel();
				return true;
			}
			return super.onJsAlert(view, url, message, result);
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) mProgressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		mProgressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}
	
}