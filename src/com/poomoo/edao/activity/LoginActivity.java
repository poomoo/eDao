package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.UserInfoData;
import com.poomoo.edao.service.Get_UserInfo_Service;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MessageBox_YES;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

	private String phoneNum = "", passWord = "";

	private Gson gson = new Gson();
	private MessageBox_YES box_YES;
	// private MessageBox_YESNO box_YESNO;
	private ProgressDialog progressDialog = null;
	private UserInfoData loginResData = null;

	private SharedPreferences loginsp;
	private Editor editor = null;
	private eDaoClientApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
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

		loginsp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		editor = loginsp.edit();

		if (!TextUtils.isEmpty(loginsp.getString("tel", "")))
			editText_phone.setText(loginsp.getString("tel", ""));
		// if (!TextUtils.isEmpty(loginsp.getString("passWord", ""))) {
		// passWord = loginsp.getString("passWord", "");
		// // 对密码进行AES解密
		// try {
		// passWord = Utity.decrypt(eDaoClientConfig.key, passWord);
		// } catch (Exception e) {
		// System.out.println("密码解密错误:" + e.getMessage());
		// Utity.showToast(getApplicationContext(), "密码解密错误:" + e.getMessage());
		// passWord = "";
		// }
		// editText_password.setText(passWord);
		// }
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.login_btn_login:
			login();
			break;
		case R.id.login_textView_regist:
			openActivity(ProtocolActivity.class);
			break;
		case R.id.login_textView_forget_password:
			phoneNum = editText_phone.getText().toString().trim();
			if (TextUtils.isEmpty(phoneNum)) {
				Utity.showToast(getApplicationContext(), "请输入手机号!");
				return;
			}
			if (phoneNum.length() != 11) {
				Utity.showToast(getApplicationContext(), "手机号长度不正确,请重新输入!");
				return;
			}
			checkTel();
			break;
		}
	}

	private void login() {
		if (checkInput()) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("bizName", "10000");
			data.put("method", "10001");
			data.put("tel", phoneNum);
			data.put("password", passWord);

			showProgressDialog("登录中...");
			HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {
				@Override
				public void onFinish(final ResponseData responseData) {
					// TODO 自动生成的方法存根
					closeProgressDialog();
					System.out.println("进入onFinish");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							if (responseData.getRsCode() != 1) {
								box_YES = new MessageBox_YES(LoginActivity.this);
								box_YES.showDialog(responseData.getMsg(), null);
							} else {
								loginResData = new UserInfoData();
								loginResData = gson.fromJson(responseData.getJsonData(), UserInfoData.class);
								editor.putString("userId", loginResData.getUserId());
								editor.putString("realName", loginResData.getRealName());
								editor.putString("tel", loginResData.getTel());
								editor.putString("type", loginResData.getType());
								editor.putString("realNameAuth", loginResData.getRealNameAuth());
								editor.putString("payPwdValue", loginResData.getPayPwdValue());
								try {
									passWord = Utity.encrypt(eDaoClientConfig.key, passWord);
								} catch (Exception e) {
									Utity.showToast(getApplicationContext(), "密码加密错误:" + e.getMessage());
									passWord = "";
								}
								// editor.putString("passWord", passWord);
								editor.putString("quickmarkPic", loginResData.getQuickmarkPic());
								editor.putBoolean("isLogin", true);
								editor.commit();
								application.setRealName(loginResData.getRealName());
								application.setTel(loginResData.getTel());
								application.setUserId(loginResData.getUserId());
								application.setType(loginResData.getType());
								application.setRealNameAuth(loginResData.getRealNameAuth());
								application.setPayPwdValue(loginResData.getPayPwdValue());
								application.setQuickmarkPic(loginResData.getQuickmarkPic());
								startService(new Intent(LoginActivity.this, Get_UserInfo_Service.class));
								LoginActivity.this
										.startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
								LoginActivity.this.finish();
								System.out.println("跳转");
							}
						}
					});

				}

				@Override
				public void onError(final Exception e) {
					// TODO 自动生成的方法存根
					closeProgressDialog();
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
						}

					});
				}
			});
		}
	}

	/**
	 * 
	 * 
	 * @Title: checkTel
	 * @Description: TODO 校验手机是否注册
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015年9月23日上午11:45:02
	 */
	private void checkTel() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10002");
		data.put("tel", phoneNum);
		showProgressDialog("请稍后...");

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (responseData.getRsCode() != 1) {
							Bundle pBundle = new Bundle();
							pBundle.putString("type", "3");
							pBundle.putString("tel", phoneNum);
							openActivity(PassWordManageActivity.class, pBundle);
						} else {
							Utity.showToast(getApplicationContext(), phoneNum + "未注册");
						}
					}
				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						Utity.showToast(getApplicationContext(), "手机号验证失败");
					}
				});
			}
		});

	}

	/**
	 * 
	 * 
	 * @Title: checkInput
	 * @Description: TODO 检查输入信息
	 * @author 李苜菲
	 * @return
	 * @return boolean
	 * @throws @date
	 *             2015-8-12上午10:11:38
	 */
	private boolean checkInput() {
		phoneNum = editText_phone.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNum) || phoneNum.length() != 11) {
			editText_phone.setText("");
			editText_phone.setFocusable(true);
			editText_phone.requestFocus();
			Utity.showToast(getApplicationContext(), "手机号不正确,请重新输入");
			return false;
		}

		passWord = editText_password.getText().toString().trim();
		if (TextUtils.isEmpty(passWord)) {
			editText_password.setFocusable(true);
			editText_password.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入密码");
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
	private void showProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(msg);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
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
