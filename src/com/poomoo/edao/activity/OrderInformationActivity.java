package com.poomoo.edao.activity;

import com.poomoo.edao.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;

/**
 * 
 * @ClassName OrderInformationActivity
 * @Description TODO订单详情
 * @author 李苜菲
 * @date 2015-8-3 上午9:48:32
 */
public class OrderInformationActivity extends BaseActivity implements
		OnClickListener {
	private CheckBox checkBox_bill;
	private LinearLayout layout_bill;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_information);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));

		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根

	}

}
