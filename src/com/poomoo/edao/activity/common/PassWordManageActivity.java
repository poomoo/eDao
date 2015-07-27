package com.poomoo.edao.activity.common;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.edao.R;

public class PassWordManageActivity extends BaseActivity {
	private EditText editText_oldpassword,editText_newpassword,editText_newpasswordagain,editText_identifycode;
	private Button button_send,button_confirm,button_cancle;
	private TextView textView_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passwordmanage);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_oldpassword = (EditText)findViewById(R.id.passwordmanage_editText_oldpassword);
		editText_newpassword = (EditText)findViewById(R.id.passwordmanage_editText_newpassword);
		editText_newpasswordagain = (EditText)findViewById(R.id.passwordmanage_editText_newpasswordagain);
		editText_identifycode = (EditText)findViewById(R.id.passwordmanage_editText_identifycode);
		textView_phone = (TextView)findViewById(R.id.passwordmanage_textView_phone);
		button_send = (Button)findViewById(R.id.passwordmanage_btn_sendIdentifyCode);
		button_confirm = (Button)findViewById(R.id.passwordmanage_btn_confirm);
		button_cancle = (Button)findViewById(R.id.passwordmanage_btn_cancle);
	}

}
