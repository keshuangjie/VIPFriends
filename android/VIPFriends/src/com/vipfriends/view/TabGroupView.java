package com.vipfriends.view;

import java.util.Vector;

import com.vipfriends.activity.BaseFragment;
import com.vipfriends.util.DLog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabGroupView extends LinearLayout implements OnClickListener {
	private static final String TAG = "TabGroupView";
	
	private final Context mContext;
	private boolean isInited = false;
	private OnClickListener onClickListener;
	private View currentView = null;
	private Vector<BaseFragment> pageList;
	private FragmentManager mFragmentManager;
	private BaseFragment mCurrentFragment;
	private int mContainerViewId;

	public TabGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public void setContainerViewId(int containerViewId) {
		this.mContainerViewId = containerViewId;
	}

	public void setFragmentManager(FragmentManager fragmentManager) {
		this.mFragmentManager = fragmentManager;
	}

	public void setPageList(Vector<BaseFragment> pageList) {
		this.pageList = pageList;
	}

	public Vector<BaseFragment> getPageList() {
		return this.pageList;
	}

	public void setTabText(String[] text) {
		for (int i = 0; i < this.getChildCount(); i++) {
			ViewGroup viewGroup = (ViewGroup) this.getChildAt(i);
			if (viewGroup.getChildCount() < 1) {
				break;
			}
			View view = viewGroup.getChildAt(0);
			if (view != null && view instanceof TextView) {
				TextView tv = (TextView) view;
				tv.setText(text[i]);
			} else {
				if (viewGroup.getChildCount() <= 1) {
					break;
				}
				view = viewGroup.getChildAt(1);
				if (view != null && view instanceof TextView) {
					TextView tv = (TextView) view;
					tv.setText(text[i]);
				}
			}
		}
	}

	public TextView getTextTab(int index) {
		if (this.getChildCount() > index) {
			ViewGroup viewGroup = (ViewGroup) this.getChildAt(index);
			if (viewGroup.getChildCount() > 0) {
				View view = viewGroup.getChildAt(0);
				if (view != null && view instanceof TextView) {
					return (TextView) view;
				}

				if (viewGroup.getChildCount() > 1) {
					view = viewGroup.getChildAt(1);
					if (view != null && view instanceof TextView) {
						return (TextView) view;
					}
				}
			}
		}
		return null;
	}

	/*
	 * 是否隐藏 tab 中的icon 图片isGone true 隐藏isGone false 不隐藏
	 */
	public void setTabIconGone(boolean isGone) {
		if (!isGone) {
			return;
		}
		for (int i = 0; i < this.getChildCount(); i++) {
			if (((ViewGroup) this.getChildAt(i)).getChildAt(0) instanceof ImageView) {
				ImageView iv = (ImageView) ((ViewGroup) this.getChildAt(i))
						.getChildAt(0);
				iv.setVisibility(View.GONE);
			} else {
				if (((ViewGroup) this.getChildAt(i)).getChildAt(1) instanceof ImageView) {
					ImageView iv = (ImageView) ((ViewGroup) this.getChildAt(i))
							.getChildAt(1);
					iv.setVisibility(View.GONE);
				}
			}
		}
	}

	public void setSelected(int index) {
		setSelected(index, null);
	}

	public void setSelected(int index, Object obj) {
		if (pageList != null && pageList.size() > index && index >= 0) {
			if (!isInited) {
				init();
				isInited = true;
			}
			View v = this.getChildAt(index);
			if (obj != null) {
				v.setTag(obj);
			}
			onClick(v);
		}
	}

	@Override
	public void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		DLog.i("zhoubo", "onLayout");
		if (!isInited) {
			init();
			isInited = true;
		}
	}

	public void init() {
		DLog.i(TAG, "init:getChildCount" + this.getChildCount());
		for (int i = 0; i < this.getChildCount(); i++) {
			this.getChildAt(i).setOnClickListener(this);
			this.getChildAt(i).setId(i);
		}

	}

	@Override
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	public int getSelectIndex() {
		if (currentView != null)
			return currentView.getId();
		return 0;
	}

	public BaseFragment getCurrentPage() {
		if (pageList != null && pageList.size() > 1)
			return pageList.get(getSelectIndex());
		return null;
	}

	public BaseFragment getPage(int index) {
		if (pageList != null && pageList.size() > 1 && index < pageList.size())
			return pageList.get(index);
		return null;
	}

	@Override
	public void onClick(View v) {
		if (onClickListener != null) {
			onClickListener.onClick(v);
		} else {
			if (currentView != null) {
				if (currentView.equals(v)) {
					return;
				} else {
					DLog.i("zhoubo",
							"currentView.getId()==" + currentView.getId());
					if (pageList == null)
						return;
					currentView.setSelected(false);
				}
			}
			v.setSelected(true);
			if (pageList == null)
				return;
			switchContent(currentView, v);
		}
		currentView = v;

	}

	public void switchContent(View fromView, View toView) {
		BaseFragment from = null;
		if(fromView != null){
			from = pageList.get(fromView.getId());
		}
		BaseFragment to = pageList.get(toView.getId());
		if (mCurrentFragment != to) {
			mCurrentFragment = to;
			FragmentTransaction transaction = mFragmentManager
					.beginTransaction();
			if(from != null){
				transaction.hide(from);
			}
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.add(mContainerViewId, to).commit(); // 隐藏当前的BaseFragment，add下一个到Activity中
			} else {
				transaction.show(to).commit(); // 隐藏当前的BaseFragment，显示下一个
			}
		}
	}
}