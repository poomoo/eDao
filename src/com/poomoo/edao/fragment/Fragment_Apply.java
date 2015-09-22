package com.poomoo.edao.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

public class Fragment_Apply extends Fragment {
	private View noDataView;
	private MyListView listView;

	private Deal_Detail_ListViewAdapter adapter;
	private List<OrderListData> list;
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private eDaoClientApplication application = null;
	private boolean isFirst = true;// 是否第一次加载
	private boolean isFresh = false;// 是否刷新
	private String orderType = "4"; // orderType订单类型

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_pub, container, false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		System.out.println("Fragment_Payed init");
		listView = (MyListView) getView().findViewById(R.id.fragment_pub_listView);
		application = (eDaoClientApplication) getActivity().getApplication();
		list = new ArrayList<OrderListData>();
		if (isFirst)
			showProgressDialog();
		getData(eDaoClientConfig.status, orderType);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				isFresh = true;
				getData(eDaoClientConfig.status, orderType);
			}
		});
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
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根

				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {
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
										list.add(data);
									}
									if (isFirst) {
										adapter = new Deal_Detail_ListViewAdapter(getActivity(), list);
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
								if (isFresh)
									Utity.showToast(getActivity().getApplicationContext(), responseData.getMsg());
								else
									showEmptyView();
							}
							listView.onRefreshComplete();
						}

					});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO 自动生成的方法存根
							closeProgressDialog();
							listView.onRefreshComplete();
							Utity.showToast(getActivity().getApplicationContext(), eDaoClientConfig.checkNet);
						}

					});
			}
		});
	}

	public void showEmptyView() {
		listView.setVisibility(View.GONE);
		if (noDataView == null) {
			ViewStub noDataViewStub = (ViewStub) getView().findViewById(R.id.fragment_pub_viewStub);
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

	/**
	 * 
	 * 
	 * @Title: showProgressDialog
	 * @Description: TODO 显示进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-12下午1:23:53
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(getActivity());
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
	 * @throws @date
	 *             2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
