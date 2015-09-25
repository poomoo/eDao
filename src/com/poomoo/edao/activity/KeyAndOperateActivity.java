package com.poomoo.edao.activity;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.util.Utity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName KeyAndOperateActivity
 * @Description TODO 经营管理
 * @author 李苜菲
 * @date 2015年8月31日 下午9:30:40
 */
public class KeyAndOperateActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum;
	private LinearLayout layout_key, layout_operate, layout_repay;
	private eDaoClientApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key_and_operate);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);

		layout_key = (LinearLayout) findViewById(R.id.key_and_operate_layout_key_manage);
		layout_operate = (LinearLayout) findViewById(R.id.key_and_operate_layout_operate_manage);
		layout_repay = (LinearLayout) findViewById(R.id.key_and_operate_layout_repay_manage);

		layout_key.setOnClickListener(this);
		layout_operate.setOnClickListener(this);
		layout_repay.setOnClickListener(this);

		Utity.setUserAndTel(textView_username, textView_phonenum, application);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.key_and_operate_layout_key_manage:
			openActivity(KeyManageActivity.class);
			finish();
			break;
		case R.id.key_and_operate_layout_operate_manage:
			openActivity(OperateManageActivity.class);
			finish();
			break;
		case R.id.key_and_operate_layout_repay_manage:
			openActivity(RepayManageActivity.class);
			finish();
			break;
		}
	}

}
