package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName CreditPayByPhoneActivity
 * @Description TODO 手机号支付
 * @author 李苜菲
 * @date 2015年7月30日 下午10:43:03
 */
public class CreditPayByPhoneActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_payee_name, textView_payee_phonenum;
	private EditText editText_money, editText_remark;
	private Button button_pay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_credit_pay_by_phone);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_payee_name = (TextView) findViewById(R.id.credit_pay_by_phone_textView_payee_name);
		textView_payee_phonenum = (TextView) findViewById(R.id.credit_pay_by_phone_textView_payee_phone);

		editText_money = (EditText) findViewById(R.id.credit_pay_by_phone_editText_money);
		editText_remark = (EditText) findViewById(R.id.credit_pay_by_phone_editText_remark);

		button_pay = (Button) findViewById(R.id.credit_pay_by_phone_btn_pay);

		button_pay.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根

	}

}
