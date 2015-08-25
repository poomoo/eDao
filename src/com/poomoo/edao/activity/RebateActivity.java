package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Rebate_ListViewAdapter;
import com.poomoo.edao.adapter.Shop_List_ListViewAdapter;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.ShopList;
import com.poomoo.edao.model.UserRebateData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MyListView;
import com.poomoo.edao.widget.MyListView.OnRefreshListener;

/**
 * 
 * @ClassName RebateActivity
 * @Description TODO 普惠全民
 * @author 李苜菲
 * @date 2015-7-29 下午2:14:43
 */
public class RebateActivity extends BaseActivity {
	private MyListView listView;
	private Rebate_ListViewAdapter adapter;
	private List<UserRebateData> list;

	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private boolean isFirst = true;// 是否第一次加载

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rebate);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (MyListView) findViewById(R.id.rebate_listView);

		list = new ArrayList<UserRebateData>();
		showProgressDialog();
		getData();
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getData();
			}
		});

	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "70000");
		data.put("method", "70007");
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
											UserRebateData data = new UserRebateData();
											data = gson.fromJson(pager
													.getJSONObject(i)
													.toString(),
													UserRebateData.class);
											list.add(data);
										}
										if (isFirst) {
											adapter = new Rebate_ListViewAdapter(
													RebateActivity.this, list);
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
