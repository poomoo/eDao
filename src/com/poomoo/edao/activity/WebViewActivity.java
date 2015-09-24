package com.poomoo.edao.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class WebViewActivity extends BaseActivity {
	private WebView webView;
	private TextView textView_title;

	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private static String content = "";
	private String from = "", method = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		webView = (WebView) findViewById(R.id.webView);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		// webView.loadUrl("file:///android_asset/123.html");
		// webView.getSettings().setDefaultTextEncodingName("UTF-8");
		// try {
		// webView.loadData(
		// new String(InputStreamToByte(getAssets().open("123.html"))),
		// "text/html", "UTF-8");
		// } catch (IOException e) {
		// // TODO 自动生成的 catch 块
		// e.printStackTrace();
		// }
		from = getIntent().getStringExtra("from");
		textView_title = (TextView) findViewById(R.id.navigation_textView_title);
		if (from.equals("help")) {
			method = "70005";
			textView_title.setText("帮助中心");
		} else {
			method = "70004";
			textView_title.setText("通知");
		}

		getData();
	}

	private void getData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "70000");
		data.put("method", method);
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
						if (responseData.getRsCode() == 1) {
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());
								content = result.getString("content");
								webView.loadData(content, "text/html", "UTF-8");
							} catch (JSONException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						} else {
							finish();
							Utity.showToast(getApplicationContext(), responseData.getMsg());
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
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}

				});
			}
		});
	}

	private byte[] InputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

}
