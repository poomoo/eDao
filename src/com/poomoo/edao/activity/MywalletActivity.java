package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.MywalletData;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.service.Get_UserInfo_Service;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MessageBox_YES;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @ClassName MywalletActivity
 * @Description TODO 我的钱包
 * @author 李苜菲
 * @date 2015-7-29 下午4:31:49
 */
public class MywalletActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum, textView_balance, textView_handing_charge,
			textView_handing_toplimit, textView_bankname, textView_account_name, textView_bankaccount,
			textView_isEnough, textView_ecoin, textView_credit, textView_allow;
	private EditText editText_handing_money;
	private Button button_recharge, button_handing;

	private String money = "";
	private eDaoClientApplication application = null;
	private Gson gson = new Gson();
	private MessageBox_YES box_YES;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private double allow_fee = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
		textView_handing_charge = (TextView) findViewById(R.id.mywallet_textView_handing_charge);
		textView_handing_toplimit = (TextView) findViewById(R.id.mywallet_textView_handing_toplimit);
		textView_bankname = (TextView) findViewById(R.id.mywallet_textView_bankname);
		textView_account_name = (TextView) findViewById(R.id.mywallet_textView_account_name);
		textView_bankaccount = (TextView) findViewById(R.id.mywallet_textView_bankaccount);
		textView_isEnough = (TextView) findViewById(R.id.mywallet_textView_isEnough);
		textView_ecoin = (TextView) findViewById(R.id.mywallet_textView_ecoin);
		textView_credit = (TextView) findViewById(R.id.mywallet_textView_credit);
		textView_allow = (TextView) findViewById(R.id.mywallet_textView_handing_fee_allow);

		editText_handing_money = (EditText) findViewById(R.id.mywallet_editText_handing_money);

		button_recharge = (Button) findViewById(R.id.mywallet_btn_recharge);
		button_handing = (Button) findViewById(R.id.mywallet_btn_handing);

		textView_ecoin.setSelected(true);

		button_recharge.setOnClickListener(this);
		button_handing.setOnClickListener(this);
		button_handing.setClickable(false);

		Utity.setUserAndTel(textView_username, textView_phonenum, application);
		textView_account_name.setText(Utity.addStarByName(application.getRealName()));
		editText_handing_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				System.out.println("afterTextChanged");
				String temp = s.toString();
				int posDot = temp.indexOf(".");
				if (posDot > 0)
					if (temp.length() - posDot - 1 > 2)
						s.delete(posDot + 3, posDot + 4);

				System.out.println("s:" + s.toString());
				if (s.length() == 0) {
					textView_isEnough.setVisibility(View.GONE);
					button_handing.setClickable(false);
					button_handing.setBackgroundResource(R.drawable.style_btn_no_background);
					return;
				}

				double money = Double.parseDouble(s.toString().trim());
				System.out.println("money:" + money + " " + (double) application.getTotalEb());

				if (money > (double) application.getTotalEb()) {
					textView_isEnough.setVisibility(View.VISIBLE);
					textView_isEnough.setText(eDaoClientConfig.balanceIsNotEnough);
					button_handing.setClickable(false);
					button_handing.setBackgroundResource(R.drawable.style_btn_no_background);
				} else if (money > allow_fee) {
					textView_isEnough.setVisibility(View.VISIBLE);
					textView_isEnough.setText(eDaoClientConfig.moreBalance);
					button_handing.setClickable(false);
					button_handing.setBackgroundResource(R.drawable.style_btn_no_background);
				} else {
					textView_isEnough.setVisibility(View.GONE);
					button_handing.setClickable(true);
					button_handing.setBackgroundResource(R.drawable.style_btn_yes_background);
				}
			}
		});
		System.out.println("进入钱包" + application.getCorvertMaxFee() + ":" + application.getHandlingFee());
		// if (TextUtils.isEmpty(application.getHandlingFee())) {
		showProgressDialog("请稍后");
		getData();
		// } else {
		// initData();
		// }
	}

	private void initData() {
		textView_balance.setText("￥" + application.getTotalEb());
		textView_handing_charge.setText(application.getHandlingFee() + "元/笔");
		textView_handing_toplimit.setText(application.getCorvertMaxFee() + "元以内/笔");
		textView_allow.setText("￥" + application.getCanCovertFee());
		textView_bankname.setText(application.getBankName());
		textView_bankaccount.setText(Utity.addStarByNum(3, 16, application.getBankCardId()));
		if (!TextUtils.isEmpty(application.getCanCovertFee()))
			allow_fee = Double.parseDouble(application.getCanCovertFee());
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (!application.getRealNameAuth().equals("1")) {
			openActivity(CertificationActivity.class);
			finish();
			return;
		}
		switch (v.getId()) {
		case R.id.mywallet_btn_recharge:
			if (!Utity.isWXAppInstalledAndSupported(this, msgApi)) {
				Utity.showToast(getApplicationContext(), eDaoClientConfig.notInstallWX);
				return;
			}
			openActivity(RechargeActivity.class);
			finish();
			break;
		case R.id.mywallet_btn_handing:
			if (checkInput()) {
				confirm();
			}
			break;
		}
	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		money = editText_handing_money.getText().toString().trim();
		if (TextUtils.isEmpty(money)) {
			Utity.showToast(getApplicationContext(), "请输入金额");
			return false;
		}
		return true;
	}

	private void confirm() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "50000");
		data.put("method", "50008");
		data.put("fromUserId", application.getUserId());
		data.put("payFee", money);

		showProgressDialog("提交中...");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() != 1) {
							box_YES = new MessageBox_YES(MywalletActivity.this);
							box_YES.showDialog(responseData.getMsg(), null);
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
							startService(new Intent(MywalletActivity.this, Get_UserInfo_Service.class));
							finish();
						}

					}
				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}

	private void getData() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10014");
		data.put("userId", application.getUserId());

		showProgressDialog("提交中...");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() != 1) {
							finish();
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						} else {
							MywalletData data = new MywalletData();
							data = gson.fromJson(responseData.getJsonData(), MywalletData.class);
							application.setHandlingFee(data.getHandlingFee());
							application.setCovertMinFee(data.getCovertMinFee());
							application.setCorvertMaxFee(data.getCovertMaxFee());
							application.setCanCovertFee(data.getCanCovertFee());
							application.setTotalEb(data.getTotalEb());
							application.setBankName(data.getBankName());
							application.setBankCardId(data.getBankCardId());
							initData();
						}
					}
				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						finish();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}

}
