package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.TimeCountUtil;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName Registration2Activity
 * @Description TODO 注册
 * @author 李苜菲
 * @date 2015-7-31 上午10:09:29
 */
public class RegistrationActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener {
	private EditText editText_phone, editText_identity_code,
			editText_password1, editText_password2;
	private TextView textView_isUsed;
	private Button button_identity_code, button_regist;

	private BroadcastReceiver smsReceiver;
	private IntentFilter filter;
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	private Gson gson = new Gson();
	private String tel = "", identyNum = "", passWord1 = "", passWord2 = "";
	private ProgressDialog progressDialog = null;
	private static final String address = "10690529100103";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
		receiveSms();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_phone = (EditText) findViewById(R.id.registration2_editText_phone);
		editText_identity_code = (EditText) findViewById(R.id.registration2_editText_identitynum);
		editText_password1 = (EditText) findViewById(R.id.registration2_editText_password);
		editText_password2 = (EditText) findViewById(R.id.registration2_editText_password_again);
		textView_isUsed = (TextView) findViewById(R.id.registration2_textView_isUsed);

		button_identity_code = (Button) findViewById(R.id.registration2_btn_identitynum);
		button_regist = (Button) findViewById(R.id.registration2_btn_regist);

		editText_phone.setOnFocusChangeListener(this);
		button_identity_code.setOnClickListener(this);
		button_regist.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.registration2_btn_identitynum:
			sendSms();
			break;
		case R.id.registration2_btn_regist:
			regist();
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
	 * @throws
	 * @date 2015-8-13下午3:19:34
	 */
	private void sendSms() {
		// TODO 自动生成的方法存根
		TimeCountUtil timeCountUtil = new TimeCountUtil(
				RegistrationActivity.this, 60000, 1000, button_identity_code);
		timeCountUtil.start();

		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10003");
		data.put("tel", tel);

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

					@Override
					public void onFinish(final ResponseData responseData) {
						// TODO 自动生成的方法存根
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								if (responseData.getRsCode() == 1)
									Utity.showToast(getApplicationContext(),
											"验证码发送成功");
								else
									Utity.showToast(getApplicationContext(),
											"验证码发送失败");
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
								Utity.showToast(getApplicationContext(),
										"请检查网络");
							}
						});
					}
				});
	}

	/**
	 * 
	 * 
	 * @Title: receiveSms
	 * @Description: TODO 自动接收验证码
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-13下午2:35:53
	 */
	private void receiveSms() {
		filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		smsReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] objs = (Object[]) intent.getExtras().get("pdus");
				for (Object obj : objs) {
					byte[] pdu = (byte[]) obj;
					SmsMessage sms = SmsMessage.createFromPdu(pdu);
					String sender = sms.getDisplayOriginatingAddress();// 获取短信的发送者
					// String from = sms.getOriginatingAddress();
					if (sender.equals(address)) {
						final String code = patternCode(getSMSBody());
						if (!TextUtils.isEmpty(code)) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO 自动生成的方法存根
									editText_identity_code.setText(code);
								}
							});

						}
					}
				}
			}
		};
		registerReceiver(smsReceiver, filter);
	}

	/**
	 * 
	 * 
	 * @Title: regist
	 * @Description: TODO 注册
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-13下午3:20:02
	 */
	private void regist() {
		// TODO 自动生成的方法存根
		if (checkInput()) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("bizName", "10000");
			data.put("method", "10005");
			data.put("tel", tel);
			data.put("code", identyNum);
			data.put("password", passWord1);
			TelephonyManager tm = (TelephonyManager) this
					.getSystemService(Context.TELEPHONY_SERVICE);
			data.put("channelId", tm.getDeviceId());// 设备IMEI
			showProgressDialog();
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
										Utity.showToast(
												getApplicationContext(),
												responseData.getMsg());
									} else {
										startActivity(new Intent(
												RegistrationActivity.this,
												LoginActivity.class));
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
	}

	/**
	 * 
	 * 
	 * @Title: checkInput
	 * @Description: TODO 检查输入信息
	 * @author 李苜菲
	 * @return
	 * @return boolean
	 * @throws
	 * @date 2015-8-13下午3:41:51
	 */
	private boolean checkInput() {
		tel = editText_phone.getText().toString().trim();
		if (TextUtils.isEmpty(tel) || tel.length() != 11) {
			editText_phone.setText("");
			editText_phone.setFocusable(true);
			editText_phone.setFocusableInTouchMode(true);
			editText_phone.requestFocus();
			Utity.showToast(getApplicationContext(), "手机号不正确,请重新输入");
			return false;
		}

		identyNum = editText_identity_code.getText().toString().trim();
		if (TextUtils.isEmpty(identyNum)) {
			editText_identity_code.setFocusable(true);
			editText_identity_code.setFocusableInTouchMode(true);
			editText_identity_code.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入验证码");
			return false;
		}
		passWord1 = editText_password1.getText().toString().trim();
		if (TextUtils.isEmpty(passWord1)) {
			editText_password1.setFocusable(true);
			editText_password1.setFocusableInTouchMode(true);
			editText_password1.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入密码");
			return false;
		}
		passWord2 = editText_password2.getText().toString().trim();
		if (TextUtils.isEmpty(passWord2)) {
			editText_password2.setFocusable(true);
			editText_password2.setFocusableInTouchMode(true);
			editText_password2.requestFocus();
			Utity.showToast(getApplicationContext(), "请确定密码");
			return false;
		}
		if (!passWord1.equals(passWord2)) {
			editText_password1.setText("");
			editText_password2.setText("");
			editText_password1.setFocusable(true);
			editText_password1.setFilterTouchesWhenObscured(true);
			editText_password1.requestFocus();
			Utity.showToast(getApplicationContext(), "两次输入的密码不一致,请重新输入");
			return false;
		}
		return true;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO 自动生成的方法存根
		tel = editText_phone.getText().toString().trim();
		if (hasFocus) {
			editText_phone.setText("");
			textView_isUsed.setText("");
		}
		// if (!hasFocus && tel.length() != 11) {
		// Toast.makeText(getApplicationContext(), "手机号长度不对",
		// Toast.LENGTH_SHORT).show();
		// editText_phone.setFocusable(true);
		// editText_phone.setFocusableInTouchMode(true);
		// editText_phone.requestFocus();
		// }
		if (!hasFocus && tel.length() == 11) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("bizName", "10000");
			data.put("method", "10002");
			data.put("tel", tel);

			HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
					new HttpCallbackListener() {

						@Override
						public void onFinish(final ResponseData responseData) {
							// TODO 自动生成的方法存根
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO 自动生成的方法存根
									if (responseData.getRsCode() == 1) {
										button_identity_code
												.setBackgroundResource(R.drawable.style_identy_button_yes_frame);
										button_identity_code.setTextColor(Color
												.parseColor("#0079ff"));
										button_identity_code.setClickable(true);
									} else {
										textView_isUsed.setText("*该手机已注册");
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
									Utity.showToast(getApplicationContext(),
											"手机号验证失败");
								}
							});
						}
					});

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(smsReceiver);
	}

	/**
	 * 
	 * 
	 * @Title: patternCode
	 * @Description: TODO 匹配短信中间的6个数字（验证码等）
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws
	 * @date 2015-8-13下午2:32:45
	 */

	private String patternCode(String patternContent) {
		if (TextUtils.isEmpty(patternContent)) {
			return null;
		}
		Pattern p = Pattern.compile(patternCoder);
		Matcher matcher = p.matcher(patternContent);
		if (matcher.find()) {
			return matcher.group();
		}
		return null;
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
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("注册中...");
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

	/**
	 * 
	 * 
	 * @Title: getSMSBody
	 * @Description: TODO 获取短信内容
	 * @author 李苜菲
	 * @return
	 * @return String
	 * @throws
	 * @date 2015-8-13下午5:18:39
	 */
	public String getSMSBody() {
		Uri uri = Uri.parse("content://sms");
		ContentResolver cr = RegistrationActivity.this.getContentResolver();
		Cursor cursor = cr.query(uri, null, "address=? and type =1 and read=0",
				new String[] { address }, null);
		String smsbody = cursor.getString(cursor.getColumnIndex("body"));
		return smsbody;
	}
}
