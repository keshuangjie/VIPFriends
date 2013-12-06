package com.vipfriends.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.vipfriends.util.DLog;
import com.vipfriends.util.UserPhotoUpload;
import com.vipfriends.view.ProgressWebView;

/**
 * 
 * @author keshuangjie
 */
public class WebViewFragment extends BaseFragment {
	private static final String TAG = "WebViewFragment";
	
	public static final String JS_INVOKE = "invoke";
	
	//本地图片
	private final static int LOCAL_PICTURE = 1;
	//照相图片
	private final static int CAMERA_PICTURE = 2;
	//剪裁图片
	private final static int CUT_PICTURE = 3;

	private ProgressWebView mWebView;
	private String mUrl;
	
	private UserPhotoUpload mUserPhotoUpload;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DLog.i(TAG, "onCreate");
		Bundle arguments = getArguments();
		if(arguments != null){
			mUrl = arguments.getString("url");
		}else{
			DLog.i(TAG, "arguments is null");
		}
		DLog.i(TAG, "url=" + mUrl);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		DLog.i(TAG, "onCreate");
		
		if(mWebView == null){
			
			mWebView = new ProgressWebView(getActivity());

			mWebView.setWebViewClient(new WebViewClient() {
				public void onReceivedError(WebView view, int errorCode,
						String description, String failingUrl) {
					Toast.makeText(getActivity(), "Oh no! " + description,
							Toast.LENGTH_SHORT).show();
				}
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return true;
				}
			});
			mWebView.addJavascriptInterface(new JSInvoke(), JS_INVOKE);
			
			mWebView.loadUrl(mUrl);
		}
		
		ViewGroup parent = (ViewGroup) mWebView.getParent(); 
		if (parent != null) {            
			parent.removeView(mWebView);      
		} 
		
		return mWebView;
	}
	
	public boolean canGoBack(){
		DLog.i(TAG, "mWebView.canGoBack():" + mWebView.canGoBack());
		if(mWebView.canGoBack()){
			mWebView.goBack();
			return true;
		}
		return false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		DLog.i(TAG, "onActivityResult:" + requestCode);
		DLog.i(TAG, "requestCode:" + requestCode);
		
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			// 如果是直接从相册获取
			case LOCAL_PICTURE:
				if (data != null && mUserPhotoUpload != null) {
					mUserPhotoUpload.startPhotoZoom(data.getData());
				}
				break;
			// 如果是调用相机拍照时
			case CAMERA_PICTURE:
				try {
					File userPhotoFile = mUserPhotoUpload.getUserPhotoFile();
					if (userPhotoFile != null && userPhotoFile.isFile() && mUserPhotoUpload != null) {
						mUserPhotoUpload.startPhotoZoom(Uri.fromFile(userPhotoFile));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			// 取得裁剪后的图片
			case CUT_PICTURE:
				if (data != null && mUserPhotoUpload != null) {
					mUserPhotoUpload.setPicToView(data);
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private class JSInvoke{
		
		@SuppressWarnings("unused")
		public void updateUserFace(){
			DLog.i(TAG, "updateUserFace():js invoke java success");
			mUserPhotoUpload = new UserPhotoUpload(getActivity());
			mUserPhotoUpload.ShowPickDialog();
		}
	}
	
}
