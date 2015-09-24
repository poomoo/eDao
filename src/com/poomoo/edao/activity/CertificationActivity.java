package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MessageBox_YES;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @ClassName CertificationActivity
 * @Description TODO 实名认证
 * @author 李苜菲
 * @date 2015-8-14 下午2:22:02
 */
public class CertificationActivity extends BaseActivity implements OnClickListener {
	private EditText editText_realName, editText_idNum;
	private Button button_next;

	private SharedPreferences sharedPreferences_certificaitonInfo = null;
	private Editor editor = null;
	private String realName = "", idNum = "";
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private MessageBox_YES box_YES;
	private boolean isUpload = false;
	private eDaoClientApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_certification);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_realName = (EditText) findViewById(R.id.registration_editText_realName);
		editText_idNum = (EditText) findViewById(R.id.registration_editText_idNum);

		button_next = (Button) findViewById(R.id.registration_btn_confirm);

		sharedPreferences_certificaitonInfo = getSharedPreferences("certificaitonInfo", Context.MODE_PRIVATE);
		if (sharedPreferences_certificaitonInfo.getBoolean("uploadStatus", false)) {
			editText_realName.setText(sharedPreferences_certificaitonInfo.getString("realName", ""));
			editText_idNum.setText(sharedPreferences_certificaitonInfo.getString("idCardNum", ""));
			isUpload = true;
		}

		button_next.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {

		case R.id.registration_btn_confirm:
			if (isUpload) {
				openActivity(UploadPicsActivity.class);
				finish();
			} else if (checkInput()) {
				certificate();
			}
			break;
		}
	}

	private void certificate() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10017");
		data.put("userId", application.getUserId());
		data.put("realName", realName);
		data.put("idCardNum", idNum);

		showProgressDialog("认证中...");
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
							box_YES = new MessageBox_YES(CertificationActivity.this);
							box_YES.showDialog(responseData.getMsg(), null);
						} else {
							sharedPreferences_certificaitonInfo = getSharedPreferences("certificaitonInfo",
									Context.MODE_PRIVATE);
							editor = sharedPreferences_certificaitonInfo.edit();
							editor.putString("realName", realName);
							editor.putString("idCardNum", idNum);
							// editor.putString("bankProvince",
							// province);
							// editor.putString("bankCity", city);
							// editor.putString("bankName", bank);
							// editor.putString("bankCardId", account1);
							editor.putBoolean("uploadStatus", true);
							editor.commit();
							openActivity(UploadPicsActivity.class);
							CertificationActivity.this.finish();
						}
					}
				});

			}

			@Override
			public void onError(final Exception e) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						// TODO 自动生成的方法存根
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}

				});
			}
		});
	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		realName = editText_realName.getText().toString().trim();
		if (TextUtils.isEmpty(realName)) {
			editText_realName.setFocusable(true);
			editText_realName.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入姓名");
			return false;
		}
		idNum = editText_idNum.getText().toString().trim();
		if (TextUtils.isEmpty(idNum)) {
			editText_idNum.setFocusable(true);
			editText_idNum.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入身份证号");
			return false;
		}

		return true;
	}

}
