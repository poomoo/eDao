package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Deal_Detail_ListViewAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.OrderListData;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MyListView;
import com.poomoo.edao.widget.MyListView.OnRefreshListener;

/**
 * 
 * @ClassName KeyManageActivity
 * @Description TODO 秘钥管理
 * @author 李苜菲
 * @date 2015年8月30日 下午10:24:03
 */
public class KeyManageActivity extends BaseActivity implements OnClickListener {
	private RadioButton button_apply, button_used, button_notUsed;
	private ImageView imageView_return;
	private TextView textView_buy_key;
	private MyListView listView;

	private Deal_Detail_ListViewAdapter adapter;

	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private boolean isFirst = true;// 是否第一次加载
	private eDaoClientApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		imageView_return = (ImageView) findViewById(R.id.key_manage_imageView_back);
		textView_buy_key = (TextView) findViewById(R.id.key_manage_textView_buy_key);
		listView = (MyListView) findViewById(R.id.key_manage_listView);

		button_apply = (RadioButton) findViewById(R.id.key_manage_radioButton_apply);
		button_used = (RadioButton) findViewById(R.id.key_manage_radioButton_used);
		button_notUsed = (RadioButton) findViewById(R.id.key_manage_radioButton_notUsed);

		imageView_return.setOnClickListener(this);
		textView_buy_key.setOnClickListener(this);
		button_apply.setOnClickListener(this);
		button_used.setOnClickListener(this);
		button_notUsed.setOnClickListener(this);

		showProgressDialog();
		getData();
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getData();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.key_manage_imageView_back:
			finish();
			break;
		case R.id.key_manage_textView_buy_key:
			break;
		case R.id.key_manage_radioButton_apply:
			curPage = 1;
			pageSize = 10;
			showProgressDialog();
			getData();
			break;
		case R.id.key_manage_radioButton_used:
			curPage = 1;
			pageSize = 10;
			showProgressDialog();
			getData();
			break;
		case R.id.key_manage_radioButton_notUsed:
			showProgressDialog();
			getData();
			break;
		}
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "50000");
		data.put("method", "50005");
		data.put("userId", application.getUserId());
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);

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
								if (responseData.getRsCode() == 1
										&& responseData.getJsonData().length() > 0) {
									try {
										JSONObject result = new JSONObject(
												responseData.getJsonData()
														.toString());

										JSONArray pager = result
												.getJSONArray("records");
										int length = pager.length();
										for (int i = 0; i < length; i++) {
											OrderListData data = new OrderListData();
											data = gson.fromJson(pager
													.getJSONObject(i)
													.toString(),
													OrderListData.class);
											// list.add(data);
										}
										if (isFirst) {
											// adapter = new
											// Deal_Detail_ListViewAdapter(
											// KeyManageActivity.this,
											// list);
											listView.setAdapter(adapter);
											isFirst = false;
										} else {
											adapter.notifyDataSetChanged();
										}
										curPage += 10;
										pageSize += 10;

									} catch (JSONException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
								}
								listView.onRefreshComplete();
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
								listView.onRefreshComplete();
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
