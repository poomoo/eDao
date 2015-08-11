package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName Registration2Activity
 * @Description TODO 注册
 * @author 李苜菲
 * @date 2015-7-31 上午10:09:29
 */
public class Registration2Activity extends BaseActivity implements
		OnClickListener {
	private EditText editText_phone, editText_identity_code, editText_password1,editText_password2;
	private Button button_identity_code, button_regist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration2);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_phone = (EditText) findViewById(R.id.registration2_editText_phone);
		editText_identity_code = (EditText) findViewById(R.id.registration2_editText_identitynum);
		editText_password1 = (EditText) findViewById(R.id.registration2_editText_password);
		editText_password2 = (EditText) findViewById(R.id.registration2_editText_password_again);

		button_identity_code = (Button) findViewById(R.id.registration2_btn_identitynum);
		button_regist = (Button) findViewById(R.id.registration2_btn_regist);

		button_identity_code.setOnClickListener(this);
		button_regist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.registration2_btn_identitynum:
			break;
		case R.id.registration2_btn_regist:
			break;

		}
	}

}
