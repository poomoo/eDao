package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName PersonalCenterActivity
 * @Description TODO 个人中心
 * @author 李苜菲
 * @date 2015-7-28 下午5:02:51
 */
public class PersonalCenterActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_name, textView_tel;
	private LinearLayout layout_credit, layout_bankaccount,
			layout_accountpassword, layout_paypassword, layout_twodimencode;
	private Button button_logout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personalcenter);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_name = (TextView) findViewById(R.id.personalcenter_textView_username);
		textView_tel = (TextView) findViewById(R.id.personalcenter_textView_tel);
		layout_credit = (LinearLayout) findViewById(R.id.personalcenter_layout_credit_manage);
		layout_bankaccount = (LinearLayout) findViewById(R.id.personalcenter_layout_bankaccount_manage);
		layout_accountpassword = (LinearLayout) findViewById(R.id.personalcenter_layout_accountpassword_manage);
		layout_paypassword = (LinearLayout) findViewById(R.id.personalcenter_layout_paypassword_manage);
		layout_twodimencode = (LinearLayout) findViewById(R.id.personalcenter_layout_twodimencode);
		button_logout = (Button) findViewById(R.id.personalcenter_btn_logout);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.personalcenter_layout_credit_manage:
			break;
		case R.id.personalcenter_layout_bankaccount_manage:
			break;
		case R.id.personalcenter_layout_accountpassword_manage:
			break;
		case R.id.personalcenter_layout_paypassword_manage:
			break;
		case R.id.personalcenter_layout_twodimencode:
			break;
		case R.id.personalcenter_btn_logout:
			break;
		}

	}
}
