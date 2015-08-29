package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.zbar.lib.CaptureActivity;

/**
 * 
 * @ClassName TransferOfPaymentActivity1
 * @Description TODO 转账支付1
 * @author 李苜菲
 * @date 2015-7-30 上午11:02:41
 */
public class TransferActivity1 extends BaseActivity implements OnClickListener {

	private TextView textView_username, textView_phonenum, textView_balance;
	private EditText editText_payee_phonenum;
	private LinearLayout layout_payby_phone, layout_payby_2dimencode;
	private Button button_confirm;
	private eDaoClientApplication application = null;
	private static final int READCONTRACT = 1;
	private final static int TWODIMENCODE = 2;

	private String name = "", phoneNum = "", referrerUserId = "",
			referrerName = "";
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer1);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_balance = (TextView) findViewById(R.id.transfer1_textView_balance);

		editText_payee_phonenum = (EditText) findViewById(R.id.transfer1_editText_phonenum);

		layout_payby_phone = (LinearLayout) findViewById(R.id.transfer1_layout_payby_phone);
		layout_payby_2dimencode = (LinearLayout) findViewById(R.id.transfer1_layout_payby_2dimencode);

		button_confirm = (Button) findViewById(R.id.transfer1_btn_confirm);

		layout_payby_phone.setOnClickListener(this);
		layout_payby_2dimencode.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

		textView_username
				.setText(Utity.addStarByName(application.getRealName()));
		textView_phonenum
				.setText(Utity.addStarByNum(3, 7, application.getTel()));
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.transfer1_layout_payby_phone:
			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI), READCONTRACT);
			break;
		case R.id.transfer1_layout_payby_2dimencode:
			openActivityForResult(CaptureActivity.class, TWODIMENCODE);
			break;
		case R.id.transfer1_btn_confirm:
			// openActivity(TransferActivity2.class);
			if (checkInput())
				getMerchantName();
			break;
		}

	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		phoneNum = editText_payee_phonenum.getText().toString().trim();
		if (TextUtils.isEmpty(phoneNum)) {
			Utity.showToast(getApplication(), "请填写收款人信息");
			return false;
		}
		return true;
	}

	private void getMerchantName() {
		progressDialog = null;
		showProgressDialog("请稍后...");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20002");
		data.put("referrerTel", phoneNum);

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
										Bundle pBundle = new Bundle();
										pBundle.putString("realName",
												referrerName);
										pBundle.putString("tel", phoneNum);
										pBundle.putString("money", "");
										pBundle.putString("userId",
												referrerUserId);
										pBundle.putString("payType", "1");
										openActivity(TransferActivity2.class,
												pBundle);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == READCONTRACT && resultCode == Activity.RESULT_OK) {
			// ContentProvider展示数据类似一个单个数据库表
			// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
			ContentResolver reContentResolverol = getContentResolver();
			// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
			Uri contactData = data.getData();
			// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			// 获得DATA表中的名字
			name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// 条件为联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
			Cursor phone = reContentResolverol.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phone.moveToNext()) {
				phoneNum = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			}

			editText_payee_phonenum.setText(phoneNum);
			editText_payee_phonenum.setSelection(editText_payee_phonenum
					.getText().toString().length());
			return;
		}

		if (requestCode == TWODIMENCODE && resultCode == Activity.RESULT_OK) {
			System.out.println("data:" + data.getStringExtra("result"));
			String result = data.getStringExtra("result");
			String[] temp = result.split("\\n");
			for (String str : temp) {
				if (str.startsWith("N:")) {
					String[] temp1 = str.split(":");
					System.out.println("temp1:" + temp1[1]);
				}
				if (str.startsWith("TEL:")) {
					String[] temp1 = str.split(":");
					editText_payee_phonenum.setText(temp1[1]);
					editText_payee_phonenum
							.setSelection(editText_payee_phonenum.getText()
									.toString().length());
				}
				if (str.startsWith("NOTE:")) {
					String[] temp1 = str.split(":");
					System.out.println("temp1:" + temp1[1]);
				}

			}

		}

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
