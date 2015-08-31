package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName LoveFundActivity
 * @Description TODO 爱心基金
 * @author 李苜菲
 * @date 2015-8-11 下午5:36:39
 */
public class LoveFundActivity extends BaseActivity {
	private TextView textView_username, textView_phonenum, textView_myown,
			textView_total;

	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private String my_fund = "", total_fund = "";

	private eDaoClientApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_love_fund);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_myown = (TextView) findViewById(R.id.love_fund_textView_myown);
		textView_total = (TextView) findViewById(R.id.love_fund_textView_total);
		Utity.setUserAndTel(textView_username, textView_phonenum, application);
		getData();
	}

	private void getData() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10016");
		data.put("userId", application.getUserId());
		showProgressDialog("查询中...");
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
									try {
										JSONObject result = new JSONObject(
												responseData.getJsonData());
										my_fund = result
												.getString("myLoveFund");
										total_fund = result
												.getString("totalLoveFund");
										textView_myown.setText("￥" + my_fund);
										textView_total
												.setText("￥" + total_fund);
									} catch (JSONException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
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
