package com.vipfriends.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
	
	protected Bundle mData;
	
	public void setData(Bundle bundle){
		this.mData = bundle;
	}

}
