package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName ConfirmOrderActivity
 * @Description TODO确认订单
 * @author 李苜菲
 * @date 2015-8-3 上午9:48:19
 */
public class ConfirmOrderActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener {
	private CheckBox checkBox_bill;
	private LinearLayout layout_bill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_order);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		checkBox_bill = (CheckBox) findViewById(R.id.confirm_order_checkbox_bill);
		layout_bill = (LinearLayout) findViewById(R.id.confirm_order_layout_bill);

		checkBox_bill.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO 自动生成的方法存根
		if (isChecked)
			layout_bill.setVisibility(View.VISIBLE);
		else
			layout_bill.setVisibility(View.GONE);
	}

}
