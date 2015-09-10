package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.service.Get_UserInfo_Service;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.TimeCountUtil;
import com.poomoo.edao.util.Utity;

public class PassWordManageActivity extends BaseActivity implements OnClickListener {
	private EditText editText_oldpassword, editText_newpassword, editText_newpasswordagain, editText_identifycode;
	private Button button_send, button_confirm, button_cancle;
	private TextView textView_phone, textView_title;
	private LinearLayout layout_oldpw;

	private eDaoClientApplication application;
	private Gson gson = new Gson();
	private String type = "", tel = "", identyNum = "", oldPassWord = "", passWord1 = "", passWord2 = "";
	private ProgressDialog progressDialog = null;
	private boolean isNeedOldPW = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		type = getIntent().getStringExtra("type");// 1-账户密码 2-支付密码
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_oldpassword = (EditText) findViewById(R.id.passwordmanage_editText_oldpassword);
		editText_newpassword = (EditText) findViewById(R.id.passwordmanage_editText_newpassword);
		editText_newpasswordagain = (EditText) findViewById(R.id.passwordmanage_editText_newpasswordagain);
		editText_identifycode = (EditText) findViewById(R.id.passwordmanage_editText_identifycode);
		textView_phone = (TextView) findViewById(R.id.passwordmanage_textView_phone);

		button_send = (Button) findViewById(R.id.passwordmanage_btn_sendIdentifyCode);
		button_confirm = (Button) findViewById(R.id.passwordmanage_btn_confirm);
		button_cancle = (Button) findViewById(R.id.passwordmanage_btn_cancle);

		layout_oldpw = (LinearLayout) findViewById(R.id.passwordmanage_layout_oldpassword);

		textView_title = (TextView) findViewById(R.id.navigation_textView_title);
		if (type.equals("1"))
			textView_title.setText("账户密码");
		else if (type.equals("2"))
			textView_title.setText("支付密码");
		else
			textView_title.setText("重置账户密码");

		if ((type.equals("2") && TextUtils.isEmpty(application.getPayPwdValue())) || type.equals("3")) {
			isNeedOldPW = false;
			layout_oldpw.setVisibility(View.GONE);
		}

		button_send.setOnClickListener(this);
		button_confirm.setOnClickListener(this);
		button_cancle.setOnClickListener(this);
		tel = application.getTel();
		textView_phone.setText(Utity.addStarByNum(3, 7, tel));
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.passwordmanage_btn_sendIdentifyCode:
			sendSms();
			break;
		case R.id.passwordmanage_btn_confirm:
			confirm();
			break;
		case R.id.passwordmanage_btn_cancle:
			finish();
			break;
		}
	}

	/**
	 * 
	 * 
	 * @Title: sendSms
	 * @Description: TODO 发送验证码
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-13下午3:19:34
	 */
	private void sendSms() {
		// TODO 自动生成的方法存根
		TimeCountUtil timeCountUtil = new TimeCountUtil(PassWordManageActivity.this, 60000, 1000, button_send);
		timeCountUtil.start();

		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10003");
		data.put("tel", tel);

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (responseData.getRsCode() == 1)
							Utity.showToast(getApplicationContext(), "验证码发送成功");
						else
							Utity.showToast(getApplicationContext(), "验证码发送失败");
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
						Utity.showToast(getApplicationContext(), "请检查网络");
					}
				});
			}
		});
	}

	/**
	 * 
	 * 
	 * @Title: confirm
	 * @Description: TODO 提交
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015年8月30日下午5:54:16
	 */
	private void confirm() {
		// TODO 自动生成的方法存根
		if (checkInput()) {
			if (type.equals("1"))
				account();
			else if (type.equals("2"))
				pay();
			else
				forget();
		}
	}

	private void account() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10007");
		data.put("tel", tel);
		data.put("password", passWord1);
		data.put("oldPassword", oldPassWord);
		data.put("code", identyNum);

		showProgressDialog();
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
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						} else {
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

	private void pay() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10009");
		data.put("tel", tel);
		data.put("payPwd", passWord1);
		data.put("oldPayPwd", oldPassWord);
		data.put("code", identyNum);

		showProgressDialog();
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
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						} else {
							Intent intent = new Intent(PassWordManageActivity.this, Get_UserInfo_Service.class);
							startService(intent);
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

	/**
	 * 
	 * 
	 * @Title: forget
	 * @Description: TODO 忘记密码
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015年9月10日上午9:57:02
	 */
	private void forget() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10006");
		data.put("tel", tel);
		data.put("password", passWord1);
		data.put("code", identyNum);

		showProgressDialog();
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
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
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

	private boolean checkInput() {
		if (isNeedOldPW) {
			oldPassWord = editText_oldpassword.getText().toString().trim();
			if (TextUtils.isEmpty(oldPassWord)) {
				editText_oldpassword.setFocusable(true);
				editText_oldpassword.setFocusableInTouchMode(true);
				editText_oldpassword.requestFocus();
				Utity.showToast(getApplicationContext(), "请输入旧密码");
				return false;
			}
		}

		passWord1 = editText_newpassword.getText().toString().trim();
		if (TextUtils.isEmpty(passWord1)) {
			editText_newpassword.setFocusable(true);
			editText_newpassword.setFocusableInTouchMode(true);
			editText_newpassword.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入新密码");
			return false;
		}
		passWord2 = editText_newpasswordagain.getText().toString().trim();
		if (TextUtils.isEmpty(passWord2)) {
			editText_newpasswordagain.setFocusable(true);
			editText_newpasswordagain.setFocusableInTouchMode(true);
			editText_newpasswordagain.requestFocus();
			Utity.showToast(getApplicationContext(), "请确定新密码");
			return false;
		}
		if (!passWord1.equals(passWord2)) {
			editText_newpassword.setText("");
			editText_newpasswordagain.setText("");
			editText_newpassword.setFocusable(true);
			editText_newpassword.setFilterTouchesWhenObscured(true);
			editText_newpassword.requestFocus();
			Utity.showToast(getApplicationContext(), "两次输入的密码不一致,请重新输入");
			return false;
		}
		identyNum = editText_identifycode.getText().toString().trim();
		if (TextUtils.isEmpty(identyNum)) {
			editText_identifycode.setFocusable(true);
			editText_identifycode.setFocusableInTouchMode(true);
			editText_identifycode.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入验证码");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @Title: showProgressDialog
	 * @Description: TODO 显示进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-12下午1:23:53
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("请稍后...");
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
	 * @throws @date
	 *             2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}
}
