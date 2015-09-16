package com.poomoo.edao.activity;

import com.poomoo.edao.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @ClassName CreditManageActivity
 * @Description TODO 信用管理
 * @author 李苜菲
 * @date 2015-7-30 下午2:48:44
 */
public class CreditManageActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_username, textView_tel;
	private Button button_collection, button_pay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credit_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_tel = (TextView) findViewById(R.id.layout_userinfo_textView_tel);

		button_collection = (Button) findViewById(R.id.credit_manage_btn_collection);
		button_pay = (Button) findViewById(R.id.credit_manage_btn_pay);

		button_collection.setOnClickListener(this);
		button_pay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.credit_manage_btn_collection:
			break;
		case R.id.credit_manage_btn_pay:
			break;
		}
	}

}
