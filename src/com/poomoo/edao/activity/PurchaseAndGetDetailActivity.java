package com.poomoo.edao.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.poomoo.edao.R;
import com.poomoo.edao.fragment.Fragment_Detail;
import com.poomoo.edao.fragment.Fragment_History;

/**
 * 
 * @ClassName PurchaseAndGetDetail
 * @Description TODO 消费领取
 * @author 李苜菲
 * @date 2015-8-27 上午9:44:08
 */
public class PurchaseAndGetDetailActivity extends BaseActivity implements
		OnClickListener {
	private RadioButton button_history, button_detail;
	private FrameLayout frameLayout;

	private Fragment_History fragment_History;
	private Fragment_Detail fragment_Detail;
	private Fragment curFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_purchase_and_get_detail);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		button_history = (RadioButton) findViewById(R.id.purchase_and_get_detail_radiobutton_history);
		button_detail = (RadioButton) findViewById(R.id.purchase_and_get_detail_radiobutton_detail);
		frameLayout = (FrameLayout) findViewById(R.id.purchase_and_get_detail_frameLayout);

		button_history.setOnClickListener(this);
		button_detail.setOnClickListener(this);

		setDefaultFragment();
	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragment_History = new Fragment_History();
		fragmentTransaction.add(R.id.purchase_and_get_detail_frameLayout,
				fragment_History);
		curFragment = fragment_History;
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根

		switch (v.getId()) {
		case R.id.purchase_and_get_detail_radiobutton_history:
			if (fragment_History == null)
				fragment_History = new Fragment_History();
			switchFragment(fragment_History);
			curFragment = fragment_History;
			break;
		case R.id.purchase_and_get_detail_radiobutton_detail:
			if (fragment_Detail == null)
				fragment_Detail = new Fragment_Detail();
			switchFragment(fragment_Detail);
			curFragment = fragment_Detail;
			break;
		}

	}

	private void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(
					R.id.purchase_and_get_detail_frameLayout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}

}
