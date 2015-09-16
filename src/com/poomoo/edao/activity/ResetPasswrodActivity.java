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

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 
 * @ClassName ResetPasswrodActivity
 * @Description TODO 重置密码
 * @author 李苜菲
 * @date 2015-8-19 上午10:25:36
 */
public class ResetPasswrodActivity extends BaseActivity implements
		OnClickListener {
	private EditText editText_password1, editText_password2;
	private Button button_confirm;

	private Gson gson = new Gson();
	private String passWord1 = "", passWord2 = "";
	private ProgressDialog progressDialog = null;
	private eDaoClientApplication applicaiton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_password);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		applicaiton=(eDaoClientApplication)getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_password1 = (EditText) findViewById(R.id.reset_password_editText_password);
		editText_password2 = (EditText) findViewById(R.id.reset_password_editText_password_again);

		button_confirm = (Button) findViewById(R.id.reset_password_btn_confirm);

		button_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.reset_password_btn_confirm:
			confirm();
			break;

		}
	}

	/**
	 * 
	 * 
	 * @Title: confirm
	 * @Description: TODO 确定
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-19上午10:27:53
	 */
	private void confirm() {
		// TODO 自动生成的方法存根
		if (checkInput()) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("bizName", "10000");
			data.put("method", "10006");
			data.put("userId", applicaiton.getUserId());
			data.put("password", passWord1);
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
			editText_password1.requestFocus();
			Utity.showToast(getApplicationContext(), "两次输入的密码不一致,请重新输入");
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

}
