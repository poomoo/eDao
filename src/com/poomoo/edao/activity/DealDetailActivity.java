package com.poomoo.edao.activity;

import com.poomoo.edao.R;
import com.poomoo.edao.fragment.Fragment_Classify;
import com.poomoo.edao.fragment.Fragment_Date;
import com.poomoo.edao.fragment.Fragment_Status;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

/**
 * 
 * @ClassName DealDetailActivity
 * @Description TODO 交易明细
 * @author 李苜菲
 * @date 2015年8月30日 下午3:42:38
 */
public class DealDetailActivity extends BaseActivity implements OnClickListener {
	private Fragment_Status fragment_Status;
	private Fragment_Classify fragment_Classify;
	private Fragment_Date fragment_Date;
	private Fragment curFragment;
	private RadioButton button_status, button_classify, button_date;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_detail);
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
		fragmentTransaction.add(R.id.deal_detail_layout, fragment_Status);
		fragmentTransaction.commit();
	}

	private void init() {
		// TODO 自动生成的方法存
		button_status = (RadioButton) findViewById(R.id.deal_detail_radioButton_status);
		button_classify = (RadioButton) findViewById(R.id.deal_detail_radioButton_classify);
		button_date = (RadioButton) findViewById(R.id.deal_detail_radioButton_date);

		button_status.setOnClickListener(this);
		button_classify.setOnClickListener(this);
		button_date.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.deal_detail_radioButton_status:
			if (fragment_Status == null)
				fragment_Status = new Fragment_Status();
			switchFragment(fragment_Status);
			curFragment = fragment_Status;
			break;
		case R.id.deal_detail_radioButton_classify:
			if (fragment_Classify == null)
				fragment_Classify = new Fragment_Classify();
			switchFragment(fragment_Classify);
			curFragment = fragment_Classify;
			break;
		case R.id.deal_detail_radioButton_date:
			System.out.println("点击时间");
			if (fragment_Date == null)
				fragment_Date = new Fragment_Date();
			switchFragment(fragment_Date);
			curFragment = fragment_Date;
			break;

		}
	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(R.id.deal_detail_layout,
					to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}

}
