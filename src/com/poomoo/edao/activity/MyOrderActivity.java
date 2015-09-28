package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.MyOrder_ListViewAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.OrderListData;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MyListView;
import com.poomoo.edao.widget.MyListView.OnRefreshListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.RadioButton;

/**
 * 
 * @ClassName MyOrderActivity
 * @Description TODO 我的订单
 * @author 李苜菲
 * @date 2015年8月30日 下午5:11:37
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener {
	private RadioButton button_unpay, button_payed, button_delete;
	private View noDataView;

	private eDaoClientApplication application = null;

	private MyListView listView;
	private MyOrder_ListViewAdapter adapter;
	private List<OrderListData> list;
	private Gson gson = new Gson();
	private int curPage = 1, pageSize = 10;
	private String status = "1";// ：1临时订单（未支付），2正式订单（已支付），3历史订单（删除）
	private boolean isFirst = true;// 是否第一次加载
	private boolean isFresh = false;// 是否刷新标志

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
		button_payed = (RadioButton) findViewById(R.id.my_order_radioButton_payed);
		button_unpay = (RadioButton) findViewById(R.id.my_order_radioButton_nopay);
		button_delete = (RadioButton) findViewById(R.id.my_order_radioButton_delete);
		listView = (MyListView) findViewById(R.id.my_order_listView);

		button_unpay.setOnClickListener(this);
		button_payed.setOnClickListener(this);
		button_delete.setOnClickListener(this);

		MyOrder_ListViewAdapter.type = "1";
		list = new ArrayList<OrderListData>();
		adapter = new MyOrder_ListViewAdapter(this, list);
		listView.setAdapter(adapter);

		getData(status);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				isFresh = true;
				getData(eDaoClientConfig.status);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.my_order_radioButton_nopay:
			status = "1";
			MyOrder_ListViewAdapter.type = "1";
			break;
		case R.id.my_order_radioButton_payed:
			status = "2";
			MyOrder_ListViewAdapter.type = "2";
			break;
		case R.id.my_order_radioButton_delete:
			status = "3";
			MyOrder_ListViewAdapter.type = "3";
			break;
		}
		isFirst = true;
		isFresh = false;
		curPage = 1;
		pageSize = 10;
		int size = list.size();
		if (size > 0) {
			list.removeAll(list);
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
		}
		getData(status);
	}

	public class MyListener implements OnClickListener {
		private int position = 0;
		private Button currentBtn;

		public MyListener(int position, Button currentBtn) {
			super();
			this.position = position;
			this.currentBtn = currentBtn;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getTag().equals("evaluate")) {
				System.out.println("shopId:" + list.get(position).getShopId());
				MyOrder_ListViewAdapter.currentBtn = this.currentBtn;

				Bundle pBundle = new Bundle();
				pBundle.putString("shopId", list.get(position).getShopId());
				pBundle.putString("ordersId", list.get(position).getOrdersId());
				openActivityForResult(StoreEvaluateActivity.class, pBundle, 1);
			} else {
				confirm(position);
			}
		}

	}

	private void getData(String status) {
		// TODO 自动生成的方法存根
		if (isFirst)
			showProgressDialog("请稍后...");
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "50000");
		data.put("method", "50011");
		data.put("userId", application.getUserId());
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);
		data.put("status", status);
		data.put("ordersType", "");
		data.put("startDt", "");
		data.put("endDt", "");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() == 1 && responseData.getJsonData().length() > 0) {
							showListView();
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());

								JSONArray pager = result.getJSONArray("records");
								int length = pager.length();
								for (int i = 0; i < length; i++) {
									OrderListData data = new OrderListData();
									data = gson.fromJson(pager.getJSONObject(i).toString(), OrderListData.class);
									System.out.println("data shopId:" + data.getShopId());
									list.add(data);
								}
								if (isFirst) {
									isFirst = false;
								}
								adapter.notifyDataSetChanged();

								curPage += 10;
								pageSize += 10;

							} catch (JSONException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						} else {
							if (!isFresh)
								showEmptyView();
							else
								Utity.showToast(getApplicationContext(), responseData.getMsg());
						}

						listView.onRefreshComplete();
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
						System.out.println("onError");
						closeProgressDialog();
						listView.onRefreshComplete();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}

	public void confirm(int position) {
		// TODO 自动生成的方法存根
		System.out.println("调用confirm");
		showProgressDialog("请稍后...");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "50000");
		data.put("method", "50004");
		data.put("ordersId", list.get(position).getOrdersId());
		data.put("opType", 1);
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根

						if (responseData.getRsCode() == 1) {
							isFresh = false;
							curPage = 1;
							pageSize = 10;
							int size = list.size();
							if (size > 0) {
								list.removeAll(list);
								adapter.notifyDataSetChanged();
								listView.setAdapter(adapter);
							}
							getData(status);
						} else {
							closeProgressDialog();
							Utity.showToast(getApplicationContext(), responseData.getMsg());
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
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}

	public void showEmptyView() {
		listView.setVisibility(View.GONE);
		if (noDataView == null) {
			ViewStub noDataViewStub = (ViewStub) findViewById(R.id.my_order_viewStub);
			noDataView = noDataViewStub.inflate();
		} else {
			noDataView.setVisibility(View.VISIBLE);
		}
	}

	public void showListView() {
		listView.setVisibility(View.VISIBLE);
		if (noDataView != null) {
			noDataView.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1) {
			System.out.println("评价成功！");
			MyOrder_ListViewAdapter.currentBtn.setText("已评价");
			MyOrder_ListViewAdapter.currentBtn.setClickable(false);
			MyOrder_ListViewAdapter.currentBtn.setBackgroundResource(R.drawable.style_btn_no_background);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("MyOrder销毁");
	}

}
