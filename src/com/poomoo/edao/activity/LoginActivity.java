package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName LoginActivity
 * @Description TODO 登录
 * @author 李苜菲
 * @date 2015-7-31 上午10:30:33
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	private EditText editText_phone, editText_password;
	private Button button_login;
	private TextView textView_regist, textView_forget_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_phone = (EditText) findViewById(R.id.login_editText_phone);
		editText_password = (EditText) findViewById(R.id.login_editText_password);

		button_login = (Button) findViewById(R.id.login_btn_login);

		textView_regist = (TextView) findViewById(R.id.login_textView_regist);
		textView_forget_password = (TextView) findViewById(R.id.login_textView_forget_password);

		button_login.setOnClickListener(this);
		textView_regist.setOnClickListener(this);
		textView_forget_password.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.login_btn_login:
			break;
		case R.id.login_textView_regist:
			break;
		case R.id.login_textView_forget_password:
			break;

		}
	}

}
