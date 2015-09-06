package com.poomoo.edao.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.poomoo.edao.R;
import com.poomoo.edao.fragment.Fragment_Status;

/**
 * 
 * @ClassName MyOrderActivity
 * @Description TODO 我的订单
 * @author 李苜菲
 * @date 2015年8月30日 下午5:11:37
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener {
	private Fragment_Status fragment_Status;
	private Fragment curFragment;
	private RadioButton button_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		setDefaultFragment();
		init();
	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragment_Status = new Fragment_Status();
		curFragment = fragment_Status;
		fragmentTransaction.add(R.id.my_order_layout, fragment_Status);
		fragmentTransaction.commit();
	}

	private void init() {
		// TODO 自动生成的方法存根
		button_status = (RadioButton) findViewById(R.id.my_order_radioButton_status);
		button_status.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.my_order_radioButton_status:
			if (fragment_Status == null)
				fragment_Status = new Fragment_Status();
			switchFragment(fragment_Status);
			curFragment = fragment_Status;
			break;
		case R.id.my_order_radioButton_date:
			break;

		}
	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(R.id.my_order_layout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}
}
