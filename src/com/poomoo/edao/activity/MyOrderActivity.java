package com.poomoo.edao.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.poomoo.edao.R;
import com.poomoo.edao.fragment.Fragment_Deleted;
import com.poomoo.edao.fragment.Fragment_Payed;
import com.poomoo.edao.fragment.Fragment_Status;
import com.poomoo.edao.fragment.Fragment_UnPayed;

/**
 * 
 * @ClassName MyOrderActivity
 * @Description TODO 我的订单
 * @author 李苜菲
 * @date 2015年8月30日 下午5:11:37
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener {
	private RadioButton button_unpay, button_payed, button_delete;

	private Fragment_Payed fragment_Payed;
	private Fragment_UnPayed fragment_UnPayed;
	private Fragment_Deleted fragment_Deleted;
	private Fragment curFragment;

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
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragment_Payed = new Fragment_Payed();
		curFragment = fragment_Payed;
		fragmentTransaction.add(R.id.my_order_layout, fragment_Payed);
		fragmentTransaction.commit();
	}

	private void init() {
		// TODO 自动生成的方法存根
		button_payed = (RadioButton) findViewById(R.id.my_order_radioButton_payed);
		button_unpay = (RadioButton) findViewById(R.id.my_order_radioButton_nopay);
		button_delete = (RadioButton) findViewById(R.id.my_order_radioButton_delete);
		button_unpay.setOnClickListener(this);
		button_payed.setOnClickListener(this);
		button_delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.my_order_radioButton_payed:
			if (fragment_Payed == null)
				fragment_Payed = new Fragment_Payed();
			switchFragment(fragment_Payed);
			curFragment = fragment_Payed;
			break;
		case R.id.my_order_radioButton_nopay:
			if (fragment_UnPayed == null)
				fragment_UnPayed = new Fragment_UnPayed();
			switchFragment(fragment_UnPayed);
			curFragment = fragment_UnPayed;
			break;
		case R.id.my_order_radioButton_delete:
			if (fragment_Deleted == null)
				fragment_Deleted = new Fragment_Deleted();
			switchFragment(fragment_Deleted);
			curFragment = fragment_Deleted;
			break;

		}
	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(R.id.my_order_layout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}
}
