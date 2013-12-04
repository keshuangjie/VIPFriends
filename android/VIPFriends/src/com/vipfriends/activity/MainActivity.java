package com.vipfriends.activity;

import java.util.Vector;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;

import com.vipfriends.R;
import com.vipfriends.view.TabGroupView;

/**
 * 主页面
 * 
 * @author keshuangjie
 */
public class MainActivity extends FragmentActivity {
	
	private TabGroupView mTabGroupView;
	private Vector<BaseFragment> mFragments;
	private FragmentManager mFragmentManager;
	
	private String mUrls[] = {"http://192.168.201.75/", "file:///android_asset/register.html", 
	"file:///android_asset/personal1.html"};

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
		mTabGroupView.setPageList(mFragments);
		mTabGroupView.setFragmentManager(mFragmentManager);
		mTabGroupView.setSelected(0);
	}
	
}