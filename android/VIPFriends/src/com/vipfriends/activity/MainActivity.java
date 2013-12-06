package com.vipfriends.activity;

import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.widget.Toast;

import com.vipfriends.MyApplication;
import com.vipfriends.R;
import com.vipfriends.util.DLog;
import com.vipfriends.view.TabGroupView;

/**
 * 主页面
 * 
 * @author keshuangjie
 */
public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	
	private static final int BACK_EXIT_TIME = 3000;
	
	private TabGroupView mTabGroupView;
	private Vector<BaseFragment> mFragments;
	private FragmentManager mFragmentManager;
	
	private String mUrls[] = {"file:///android_asset/mainpage/home.html", "file:///android_asset/mypage/me.html",
			"file:///android_asset/register.html"};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		initView();
	}

	private void initView() {
		mTabGroupView = (TabGroupView) findViewById(R.id.item_tab_group);
		mFragments = new Vector<BaseFragment>();
		BaseFragment fragment;
		Bundle bundle;
		
		for(int i=0; i<mUrls.length; i++){
			fragment = new WebViewFragment();
			bundle = new Bundle();
			bundle.putString("url", mUrls[i]);
			fragment.setArguments(bundle);
			mFragments.add(fragment);
		}
		
		mFragmentManager = getSupportFragmentManager();
		
		mTabGroupView.setContainerViewId(R.id.realtabcontent);
		mTabGroupView.setFragmentList(mFragments);
		mTabGroupView.setFragmentManager(mFragmentManager);
		mTabGroupView.setSelected(0);
	}
	
	private long time;
	private boolean mGoBack = false;
	
	@Override
	public void onBackPressed() {
		BaseFragment fragment = mTabGroupView.getCurrentFragment();
		if(fragment != null && fragment.canGoBack()){
			return;
		}else{
			if (mGoBack
					&& (System.currentTimeMillis() - time) < BACK_EXIT_TIME) {
				finish();
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(300);
						} catch (Exception e) {}
						MyApplication.exitAll();
					}
				}).start();
			} else {
				mGoBack = true;
				time = System.currentTimeMillis();
				Toast.makeText(this, "再按一次退出程序",
						BACK_EXIT_TIME).show();
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		DLog.i(TAG, "onActivityResult");
		BaseFragment fragment = mTabGroupView.getCurrentFragment();
		if(fragment != null){
			fragment.onActivityResult(requestCode, resultCode, data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}