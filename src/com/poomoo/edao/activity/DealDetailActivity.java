package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Deal_Detail_ListViewAdapter;
import com.poomoo.edao.adapter.Pub_GridViewAdapter;
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
 * @ClassName DealDetailActivity
 * @Description TODO 交易明细
 * @author 李苜菲
 * @date 2015年8月30日 下午3:42:38
 */
public class DealDetailActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener {
	private RadioButton button_status, button_classify, button_date,
			button_unpay, button_payed, button_delete;
	private RadioGroup group_status;
	private GridView gridView;
	private MyListView listView;

	private Pub_GridViewAdapter gridViewAdapter;
	private Deal_Detail_ListViewAdapter adapter;

	private List<OrderListData> list;
	private List<HashMap<String, String>> menu_list;
	private final static String[] items = new String[] { "意币转账", "充值", "购买秘钥",
			"申请加盟", "购买商品", "提现申请" };
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private String status = "2", orderType = "";// Status
												// ：1临时订单（未支付），2正式订单（已支付），3历史订单（删除）
												// orderType订单类型
	private boolean isFirst = true;// 是否第一次加载
	private eDaoClientApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deal_detail);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (MyListView) findViewById(R.id.deal_detail_listView);
		group_status = (RadioGroup) findViewById(R.id.deal_detail_radioGroup_status);
		/*
		 * layout_classify = (LinearLayout)
		 * findViewById(R.id.order_list_layout_classify); group1 = (RadioGroup)
		 * findViewById(R.id.order_list_radioGroup_classify_1); group2 =
		 * (RadioGroup) findViewById(R.id.order_list_radioGroup_classify_2);
		 */
		gridView = (GridView) findViewById(R.id.deal_detail_gridView_classify);

		button_status = (RadioButton) findViewById(R.id.deal_detail_radioButton_status);
		button_classify = (RadioButton) findViewById(R.id.deal_detail_radioButton_classify);
		button_date = (RadioButton) findViewById(R.id.deal_detail_radioButton_date);
		button_unpay = (RadioButton) findViewById(R.id.deal_detail_radioButton_nopay);
		button_payed = (RadioButton) findViewById(R.id.deal_detail_radioButton_payed);
		button_delete = (RadioButton) findViewById(R.id.deal_detail_radioButton_delete);

		button_status.setOnClickListener(this);
		button_classify.setOnClickListener(this);
		button_date.setOnClickListener(this);
		button_unpay.setOnClickListener(this);
		button_payed.setOnClickListener(this);
		button_delete.setOnClickListener(this);

		// list = new ArrayList<HashMap<String, String>>();
		// HashMap<String, String> hashMap;
		// for (int i = 0; i < 10; i++) {
		// hashMap = new HashMap<String, String>();
		// hashMap.put("id", "1234567890" + i);
		// hashMap.put("money", "￥" + (100 + i + ".00"));
		// hashMap.put("state", "已支付");
		// hashMap.put("date", "2015-06-06");
		// list.add(hashMap);
		// }
		// adapter = new Deal_Detail_ListViewAdapter(this, list);
		// listView.setAdapter(adapter);

		menu_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap;
		for (String string : items) {
			hashMap = new HashMap<String, String>();
			hashMap.put("name", string);
			menu_list.add(hashMap);
		}
		// simpleAdapter = new SimpleAdapter(this, list,
		// R.layout.item_gridview_order_list, new String[] { "name" },
		// new int[] { R.id.item_order_list_textView });
		gridViewAdapter = new Pub_GridViewAdapter(this, menu_list);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);

		showProgressDialog();
		getData(status, orderType);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getData(status, orderType);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.deal_detail_radioButton_status:
			group_status.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.GONE);
			status = "2";
			orderType = "";
			curPage = 1;
			pageSize = 10;
			break;
		case R.id.deal_detail_radioButton_classify:
			group_status.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);
			orderType = "1";
			curPage = 1;
			pageSize = 10;
			break;
		case R.id.deal_detail_radioButton_date:
			break;
		case R.id.deal_detail_radioButton_nopay:
			status = "1";
			orderType = "";
			curPage = 1;
			pageSize = 10;
			break;
		case R.id.deal_detail_radioButton_payed:
			status = "2";
			orderType = "";
			curPage = 1;
			pageSize = 10;
			break;
		case R.id.deal_detail_radioButton_delete:
			status = "3";
			orderType = "";
			curPage = 1;
			pageSize = 10;
			break;
		}
		showProgressDialog();
		getData(status, orderType);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
		gridViewAdapter.setTextColor();
		System.out.println("arg2:" + position + ":"
				+ Pub_GridViewAdapter.textViews.get(position));
		Pub_GridViewAdapter.textViews.get(position).setTextColor(
				Color.parseColor("#1995EB"));
		orderType = position + 1 + "";
		curPage = 1;
		pageSize = 10;
		showProgressDialog();
		getData(status, orderType);
	}

	private void getData(String status, String orderType) {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "50000");
		data.put("method", "50005");
		data.put("userId", application.getUserId());
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);
		data.put("status", status);
		data.put("ordersType", orderType);
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
													DealDetailActivity.this,
													list);
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
