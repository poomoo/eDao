package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName CreditPayActivity
 * @Description TODO 信用支付
 * @author 李苜菲
 * @date 2015-7-30 下午5:35:07
 */
public class CreditPayActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_tel;
	private LinearLayout layout_payby_phone, layout_payby_2dimencode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credit_pay);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_tel = (TextView) findViewById(R.id.layout_userinfo_textView_tel);

		layout_payby_phone = (LinearLayout) findViewById(R.id.credit_pay_layout_payby_phonenum);
		layout_payby_2dimencode = (LinearLayout) findViewById(R.id.credit_pay_layout_payby_2dimencode);

		layout_payby_phone.setOnClickListener(this);
		layout_payby_2dimencode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.credit_pay_layout_payby_phonenum:
			break;
		case R.id.credit_pay_layout_payby_2dimencode:
			break;
		}
	}

}
