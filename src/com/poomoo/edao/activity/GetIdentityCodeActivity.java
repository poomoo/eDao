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
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

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
 * @ClassName GetIdentityCodeActivity
 * @Description TODO 获取验证码
 * @author 李苜菲
 * @date 2015-8-19 上午10:18:27
 */
public class GetIdentityCodeActivity extends BaseActivity implements
		OnClickListener {
	private EditText editText_phone, editText_identity_code;
	private Button button_identity_code, button_next;

	private BroadcastReceiver smsReceiver;
	private IntentFilter filter;
	private String patternCoder = "(?<!\\d)\\d{6}(?!\\d)";
	private Gson gson = new Gson();
	private String tel = "", identyNum = "";
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
		editText_phone = (EditText) findViewById(R.id.get_identity_code_editText_phone);
		editText_identity_code = (EditText) findViewById(R.id.get_identity_code_editText_identitynum);

		button_identity_code = (Button) findViewById(R.id.get_identity_code_btn_identitynum);

		button_identity_code.setOnClickListener(this);
		button_next.setOnClickListener(this);
		button_identity_code.setClickable(false);

		editText_phone.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO 自动生成的方法存根
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				if (temp.toString().length() == 11)
					button_identity_code.setClickable(true);
				else
					button_identity_code.setClickable(false);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.get_identity_code_btn_identitynum:
			sendSms();
			break;
		case R.id.get_identity_code_btn_next:
			next();
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
				GetIdentityCodeActivity.this, 60000, 1000, button_identity_code);
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
	 * @Title: next
	 * @Description: TODO 下一步
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-19上午10:20:19
	 */
	private void next() {
		// TODO 自动生成的方法存根
		if (checkInput()) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("bizName", "10000");
			data.put("method", "10004");
			data.put("tel", tel);
			data.put("code", identyNum);
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
												GetIdentityCodeActivity.this,
												ResetPasswrodActivity.class));
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
		return true;
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
		ContentResolver cr = GetIdentityCodeActivity.this.getContentResolver();
		Cursor cursor = cr.query(uri, null, "address=? and type =1 and read=0",
				new String[] { address }, null);
		String smsbody = cursor.getString(cursor.getColumnIndex("body"));
		return smsbody;
	}
}
