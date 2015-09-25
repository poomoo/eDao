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
 * @ClassName RepayOperateActivity
 * @Description TODO 报酬管理F
 * @author 李苜菲
 * @date 2015年9月24日 下午5:10:36
 */
public class RepayManageActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum, textView_year, textView_month, textView_day,
			textView_year_money, textView_month_money, textView_day_money;
	private eDaoClientApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repay_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);

		textView_year = (TextView) findViewById(R.id.repay_textView_year);
		textView_month = (TextView) findViewById(R.id.repay_textView_month);
		textView_day = (TextView) findViewById(R.id.repay_textView_day);
		textView_year_money = (TextView) findViewById(R.id.repay_textView_year_money);
		textView_month_money = (TextView) findViewById(R.id.repay_textView_month_money);
		textView_day_money = (TextView) findViewById(R.id.repay_textView_day_money);

		textView_year.setOnClickListener(this);
		textView_month.setOnClickListener(this);
		textView_day.setOnClickListener(this);

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
			openActivity(OperateManageActivity.class);
			finish();
			break;
		}
	}

}
