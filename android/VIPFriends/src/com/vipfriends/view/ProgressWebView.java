package com.vipfriends.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ProgressWebView extends WebView {

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
				ViewGroup.LayoutParams.MATCH_PARENT, 3));
		addView(mProgressbar);
		
		this.getSettings().setJavaScriptEnabled(true);
		this.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
		this.setWebChromeClient(new WebChromeClient());
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