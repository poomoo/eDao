package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.MywalletData;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.DialogResultListener;
import com.poomoo.edao.widget.MessageBox_YES;

/**
 * 
 * @ClassName MywalletActivity
 * @Description TODO 我的钱包
 * @author 李苜菲
 * @date 2015-7-29 下午4:31:49
 */
public class MywalletActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum, textView_balance,
			textView_handing_charge, textView_handing_toplimit,
			textView_bankname, textView_account_name, textView_bankaccount;
	private EditText editText_handing_money;
	private Button button_recharge, button_handing;

	private String money = "";
	private eDaoClientApplication application = null;
	private SharedPreferences sharedPreferences_certification = null;
	private static final String bankaccount = "6226622662266226123";
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private MessageBox_YES box_YES;

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
		textView_handing_charge = (TextView) findViewById(R.id.mywallet_textView_handing_charge);
		textView_handing_toplimit = (TextView) findViewById(R.id.mywallet_textView_handing_toplimit);
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
		// sharedPreferences_certification = getSharedPreferences(
		// "certificaitonInfo", Context.MODE_PRIVATE);
		// textView_bankname.setText(sharedPreferences_certification.getString(
		// "bankName", "建设银行"));
		// textView_bankaccount.setText(Utity.addStarByNum(3, 16, bankaccount));
		System.out.println("进入钱包" + application.getCorvertMaxFee());
		if (TextUtils.isEmpty(application.getHandlingFee())) {
			showProgressDialog("请稍后");
			getData();
		} else {
			initData();
		}
	}

	private void initData() {
		textView_balance.setText(application.getTotalEb());
		textView_handing_charge.setText(application.getHandlingFee()+"元/次");
		textView_handing_toplimit.setText(application.getCovertMinFee() + "-"
				+ application.getCorvertMaxFee()+"元/次");
		textView_bankname.setText(application.getBankName());
		textView_bankaccount.setText(Utity.addStarByNum(3, 16,
				application.getBankCardId()));
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.mywallet_btn_recharge:
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
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

					@Override
					public void onFinish(final ResponseData responseData) {
						// TODO 自动生成的方法存根
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO 自动生成的方法存根
								closeProgressDialog();
								if (responseData.getRsCode() != 1) {
									box_YES = new MessageBox_YES(
											MywalletActivity.this);
									box_YES.showDialog(responseData.getMsg(),null);
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

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
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
								} else {
									MywalletData data = new MywalletData();
									data = gson.fromJson(
											responseData.getJsonData(),
											MywalletData.class);
									application.setHandlingFee(data
											.getHandlingFee());
									application.setCovertMinFee(data
											.getCovertMinFee());
									application.setCorvertMaxFee(data
											.getCovertMaxFee());
									application.setTotalEb(data.getTotalEb());
									application.setBankName(data.getBankName());
									application.setBankCardId(data
											.getBankCardId());
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
								Utity.showToast(getApplicationContext(),
										eDaoClientConfig.checkNet);
							}
						});
					}
				});
	}

	/**
	 * 
	 * 
	 * @Title: showProgressDialog
	 * @Description: TODO 显示进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-12下午1:23:53
	 */
	private void showProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(msg);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 
	 * 
	 * @Title: closeProgressDialog
	 * @Description: TODO 关闭进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
