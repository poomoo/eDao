package com.poomoo.edao.activity;

import com.poomoo.edao.R;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ProtocolActivity extends BaseActivity implements OnClickListener {
	private TextView textView_content;
	private Button button_argee, button_refuse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_protocol);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		textView_content = (TextView) findViewById(R.id.protocol_textView_content);
		button_argee = (Button) findViewById(R.id.protocol_btn_agree);
		button_refuse = (Button) findViewById(R.id.protocol_btn_refuse);

		textView_content.setMovementMethod(ScrollingMovementMethod.getInstance());
		button_argee.setOnClickListener(this);
		button_refuse.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.protocol_btn_agree:
			openActivity(RegistrationActivity.class);
			finish();
			break;
		case R.id.protocol_btn_refuse:
			finish();
			break;
		}
	}

}
