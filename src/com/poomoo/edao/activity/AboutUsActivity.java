package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName AboutUsActivity
 * @Description TODO 关于我们
 * @author 李苜菲
 * @date 2015年7月31日 上午12:12:56
 */
public class AboutUsActivity extends BaseActivity {
	private TextView textView_tel, textView_web, textView_weibo,
			textView_weixin;

	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_tel = (TextView) findViewById(R.id.aboutus_textView_tel);
		textView_web = (TextView) findViewById(R.id.aboutus_textView_web);
		textView_weibo = (TextView) findViewById(R.id.aboutus_textView_weibo);
		textView_weixin = (TextView) findViewById(R.id.aboutus_textView_weixin);
		System.out.println("eDaoClientConfig:" + eDaoClientConfig.tel
				+ eDaoClientConfig.web + eDaoClientConfig.weibo
				+ eDaoClientConfig.weixin);
		if (TextUtils.isEmpty(eDaoClientConfig.tel)
				|| TextUtils.isEmpty(eDaoClientConfig.web)
				|| TextUtils.isEmpty(eDaoClientConfig.weibo)
				|| TextUtils.isEmpty(eDaoClientConfig.weixin))
			getData();
		else {
			textView_tel.setText(eDaoClientConfig.tel);
			textView_web.setText(eDaoClientConfig.web);
			textView_weibo.setText(eDaoClientConfig.weibo);
			textView_weixin.setText(eDaoClientConfig.weixin);
		}
	}

	private void getData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "70000");
		data.put("method", "70006");
		showProgressDialog();
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

					@Override
					public void onFinish(final ResponseData responseData) {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								if (responseData.getRsCode() == 1) {
									try {
										JSONObject result = new JSONObject(
												responseData.getJsonData()
														.toString());
										eDaoClientConfig.tel = result
												.getString("serverTel");
										eDaoClientConfig.web = result
												.getString("webAddress");
										eDaoClientConfig.weibo = result
												.getString("wbAddress");
										eDaoClientConfig.weixin = result
												.getString("wxAddress");
										textView_tel
												.setText(eDaoClientConfig.tel);
										textView_web
												.setText(eDaoClientConfig.web);
										textView_weibo
												.setText(eDaoClientConfig.weibo);
										textView_weixin
												.setText(eDaoClientConfig.weixin);
									} catch (JSONException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
								} else {
									finish();
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
	 * @throws
	 * @date 2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}
}
