package com.poomoo.edao.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName MywalletActivity
 * @Description TODO 我的钱包
 * @author 李苜菲
 * @date 2015-7-29 下午4:31:49
 */
public class MywalletActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum, textView_balance,
			textView_hangding_charge, textView_hangding_toplimit,
			textView_bankname, textView_account_name, textView_bankaccount;
	private EditText editText_handing_money;
	private Button button_recharge, button_handing;

	private eDaoClientApplication application = null;
	private SharedPreferences sharedPreferences_certification = null;
	private static final String bankaccount = "6226622662266226123";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_mywallet);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_balance = (TextView) findViewById(R.id.mywallet_textView_balance);
		textView_hangding_charge = (TextView) findViewById(R.id.mywallet_textView_handing_charge);
		textView_hangding_toplimit = (TextView) findViewById(R.id.mywallet_textView_handing_toplimit);
		textView_bankname = (TextView) findViewById(R.id.mywallet_textView_bankname);
		textView_account_name = (TextView) findViewById(R.id.mywallet_textView_account_name);
		textView_bankaccount = (TextView) findViewById(R.id.mywallet_textView_bankaccount);

		editText_handing_money = (EditText) findViewById(R.id.mywallet_editText_handing_money);

		button_recharge = (Button) findViewById(R.id.mywallet_btn_recharge);
		button_handing = (Button) findViewById(R.id.mywallet_btn_handing);

		button_recharge.setOnClickListener(this);
		button_handing.setOnClickListener(this);

		textView_username
				.setText(Utity.addStarByName(application.getRealName()));
		textView_phonenum
				.setText(Utity.addStarByNum(3, 7, application.getTel()));
		textView_account_name.setText(Utity.addStarByName(application
				.getRealName()));
		sharedPreferences_certification = getSharedPreferences(
				"certificaitonInfo", Context.MODE_PRIVATE);
		textView_bankname.setText(sharedPreferences_certification.getString(
				"bankName", ""));
		textView_bankaccount.setText(Utity.addStarByNum(3, 16, bankaccount));
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.mywallet_btn_recharge:
			break;
		case R.id.mywallet_btn_handing:
			break;
		}

	}

}
