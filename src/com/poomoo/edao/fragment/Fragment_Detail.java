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
import com.poomoo.edao.adapter.Get_DetailAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.DetailData;
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

public class Fragment_Detail extends Fragment {
	private View noDataView;
	private MyListView listView;

	private Get_DetailAdapter adapter;
	private List<DetailData> list;
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private eDaoClientApplication application = null;
	private boolean isFirst = true;// 是否第一次加载
	private boolean isFresh = false;// 是否刷新

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_get_detail, container, false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (MyListView) getView().findViewById(R.id.get_detail_listView);
		application = (eDaoClientApplication) getActivity().getApplication();
		showProgressDialog();
		list = new ArrayList<DetailData>();
		getData();
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				isFresh = true;
				getData();
			}
		});
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "70000");
		data.put("method", "70009");
		data.put("userId", application.getUserId());
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				if (getActivity() != null)
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							// TODO 自动生成的方法存根
							if (responseData.getRsCode() == 1 && responseData.getJsonData().length() > 0) {
								showListView();
								try {
									JSONObject result = new JSONObject(responseData.getJsonData().toString());

									JSONArray pager = result.getJSONArray("records");
									int length = pager.length();
									for (int i = 0; i < length; i++) {
										DetailData Detail = new DetailData();
										Detail = gson.fromJson(pager.getJSONObject(i).toString(), DetailData.class);
										list.add(Detail);
									}
									if (isFirst) {
										adapter = new Get_DetailAdapter(getActivity(), list);
										listView.setAdapter(adapter);
										isFirst = false;
									} else {
										adapter.notifyDataSetChanged();
									}
									curPage += 1;

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
			ViewStub noDataViewStub = (ViewStub) getView().findViewById(R.id.get_detail_viewStub);
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
