package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.poomoo.edao.model.PayInfoData;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName DealerApplyActivity
 * @Description TODO 经销商申请
 * @author 李苜菲
 * @date 2015年7月30日 下午11:45:57
 */
public class DealerApplyActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_username, textView_phonenum, textView_money,
			textView_merchant_name;
	private EditText editText_merchant_phone;
	private Button button_confirm;

	private eDaoClientApplication application = null;
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private String merchant_phone = "", referrerUserId = "", referrerName = "",
			merchant_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_dealer_apply);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_money = (TextView) findViewById(R.id.dealer_textView_money);
		textView_merchant_name = (TextView) findViewById(R.id.dealer_textView_merchant_name);

		editText_merchant_phone = (EditText) findViewById(R.id.dealer_editText_merchant_phone);

		button_confirm = (Button) findViewById(R.id.dealer_btn_confirm);

		button_confirm.setOnClickListener(this);

		Utity.setUserAndTel(textView_username, textView_phonenum, application);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (!application.getRealNameAuth().equals("1")) {
			openActivity(CertificationActivity.class);
			startActivity(new Intent(this, CertificationActivity.class));
			finish();
		}
		switch (v.getId()) {
		case R.id.dealer_textView_merchant_name:
			getMerchantName();
			break;
		case R.id.dealer_btn_confirm:
			if (checkInput()) {
				apply();
			}
			break;
		}
	}

	/**
	 * 
	 * 
	 * @Title: apply
	 * @Description: TODO 提交申请
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-17下午4:53:39
	 */
	private void apply() {
		progressDialog = null;
		showProgressDialog("提交申请中...");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20003");
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		data.put("userId", sp.getString("userId", ""));
		data.put("joinType", "1");
		data.put("address", "");
		data.put("referrerTel", merchant_phone);
		data.put("referrerUserId", referrerUserId);
		data.put("referrerName", referrerName);

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
								if (responseData.getRsCode() == 1) {
									PayInfoData payInfo = gson.fromJson(
											responseData.getJsonData(),
											PayInfoData.class);
									Bundle pBundle = new Bundle();
									pBundle.putString("userId",
											payInfo.getUserId());
									pBundle.putString("realName",
											payInfo.getRealName());
									pBundle.putString("tel", payInfo.getTel());
									pBundle.putString("money", textView_money
											.getText().toString());
									pBundle.putString("payType", "");
									openActivity(
											TransferActivity2.class,
											pBundle);
									finish();
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
	 * @Title: checkInput
	 * @Description: TODO 检查输入项
	 * @author 李苜菲
	 * @return
	 * @return boolean
	 * @throws
	 * @date 2015-8-17下午4:52:44
	 */
	private boolean checkInput() {
		// TODO 自动生成的方法存根
		merchant_phone = editText_merchant_phone.getText().toString().trim();
		if (TextUtils.isEmpty(merchant_phone)) {
			editText_merchant_phone.setFocusable(true);
			editText_merchant_phone.setFocusableInTouchMode(true);
			editText_merchant_phone.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入服务商手机号");
			return false;
		}
		merchant_name = textView_merchant_name.getText().toString().trim();
		if (TextUtils.isEmpty(merchant_name)) {
			Utity.showToast(getApplicationContext(), "请查询服务商户名");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @Title: getMerchantName
	 * @Description: TODO 查询服务商户名
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-17下午4:52:19
	 */
	private void getMerchantName() {
		merchant_phone = editText_merchant_phone.getText().toString().trim();
		if (merchant_phone.length() != 11) {
			Utity.showToast(getApplicationContext(), "手机号长度不对");
			return;
		}
		progressDialog = null;
		showProgressDialog("查询服务商户名");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20002");
		data.put("referrerTel", merchant_phone);

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
								if (responseData.getRsCode() == 1) {
									try {
										JSONObject result = new JSONObject(
												responseData.getJsonData()
														.toString());
										referrerUserId = result
												.getString("referrerUserId");
										referrerName = result
												.getString("referrerName");
										textView_merchant_name
												.setText(referrerName);
									} catch (JSONException e) {
									}
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
