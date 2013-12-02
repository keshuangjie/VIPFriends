package com.vipfriends;

import com.vipfriends.view.ProgressWebView;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewFragment extends Fragment {

	private ProgressWebView mWebView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mWebView = new ProgressWebView(getActivity());

		mWebView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(getActivity(), "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
		});

		mWebView.loadUrl("file:///android_asset/register.html");
		return mWebView;
	}
}
