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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
 * @ClassName MyOrderActivity
 * @Description TODO 我的订单
 * @author 李苜菲
 * @date 2015年8月30日 下午5:11:37
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener {
	private RadioButton button_status, button_date, button_unpay, button_payed,
			button_delete;
	private RadioGroup group_status;
	private MyListView listView;

	private Deal_Detail_ListViewAdapter adapter;

	private List<OrderListData> list;
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private String status = "2";// Status
								// ：1临时订单（未支付），2正式订单（已支付），3历史订单（删除）
	private boolean isFirst = true;// 是否第一次加载
	private eDaoClientApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (MyListView) findViewById(R.id.my_order_listView);

		group_status = (RadioGroup) findViewById(R.id.my_order_radioGroup_status);

		button_status = (RadioButton) findViewById(R.id.my_order_radioButton_status);
		button_date = (RadioButton) findViewById(R.id.my_order_radioButton_date);
		button_unpay = (RadioButton) findViewById(R.id.my_order_radioButton_nopay);
		button_payed = (RadioButton) findViewById(R.id.my_order_radioButton_payed);
		button_delete = (RadioButton) findViewById(R.id.my_order_radioButton_delete);

		button_status.setOnClickListener(this);
		button_date.setOnClickListener(this);
		button_unpay.setOnClickListener(this);
		button_payed.setOnClickListener(this);
		button_delete.setOnClickListener(this);

		list=new ArrayList<OrderListData>();
		
		showProgressDialog();
		getData(status);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getData(status);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.my_order_radioButton_status:
			group_status.setVisibility(View.VISIBLE);
			status = "2";
			curPage = 1;
			pageSize = 10;
			break;
		case R.id.my_order_radioButton_date:
			break;
		case R.id.my_order_radioButton_nopay:
			status = "1";
			curPage = 1;
			pageSize = 10;
			break;
		case R.id.my_order_radioButton_payed:
			status = "2";
			curPage = 1;
			pageSize = 10;
			break;
		case R.id.my_order_radioButton_delete:
			status = "3";
			curPage = 1;
			pageSize = 10;
			break;
		}
		showProgressDialog();
		getData(status);
	}

	private void getData(String status) {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "50000");
		data.put("method", "50005");
		data.put("userId", application.getUserId());
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);
		data.put("status", status);
		data.put("ordersType", "");
		data.put("startDt", "");
		data.put("endDt", "");
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
											list.add(data);
										}
										if (isFirst) {
											adapter = new Deal_Detail_ListViewAdapter(
													MyOrderActivity.this, list);
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
