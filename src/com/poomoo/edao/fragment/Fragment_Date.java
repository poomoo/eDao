package com.poomoo.edao.fragment;

import java.util.ArrayList;
import java.util.Calendar;
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

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class Fragment_Date extends Fragment implements OnClickListener {
	private TextView textView_start_date, textView_end_date;
	private Button button_date_confrim;
	private View noDataView;
	private MyListView listView;
	private Deal_Detail_ListViewAdapter adapter;
	private List<OrderListData> list;

	private String startDate = "", endDate = "";

	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private eDaoClientApplication application = null;
	private boolean isFirst = true;// 是否第一次加载
	private boolean isFresh = false;// 是否刷新标志

	private Calendar cal = Calendar.getInstance();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		application = (eDaoClientApplication) getActivity().getApplication();
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_date, container, false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_start_date = (TextView) getView().findViewById(R.id.fragment_date_textView_start_date);
		textView_end_date = (TextView) getView().findViewById(R.id.fragment_date_textView_end_date);

		listView = (MyListView) getView().findViewById(R.id.fragment_date_listView);

		button_date_confrim = (Button) getView().findViewById(R.id.fragment_date_btn_date_confirm);

		textView_start_date.setOnClickListener(this);
		textView_end_date.setOnClickListener(this);
		button_date_confrim.setOnClickListener(this);

		String curDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-"
				+ cal.get(Calendar.DAY_OF_MONTH);
		textView_start_date.setText(curDate);
		textView_end_date.setText(curDate);

		list = new ArrayList<OrderListData>();
		adapter = new Deal_Detail_ListViewAdapter(getActivity(), list);
		listView.setAdapter(adapter);
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
		case R.id.fragment_date_textView_start_date:
			System.out.println("点击起始日期");
			pickDate(textView_start_date);
			break;
		case R.id.fragment_date_textView_end_date:
			System.out.println("点击结束日期");
			pickDate(textView_end_date);
			break;
		case R.id.fragment_date_btn_date_confirm:
			if (checkInput()) {
				isFirst = true;
				isFresh = false;
				int size = list.size();
				if (size > 0) {
					list.removeAll(list);
					adapter.notifyDataSetChanged();
					listView.setAdapter(adapter);
				}
				curPage = 1;
				pageSize = 10;
				showProgressDialog();
				getData(eDaoClientConfig.status);
			}
			break;
		}
	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		startDate = textView_start_date.getText().toString();
		endDate = textView_end_date.getText().toString();
		if (!Utity.isSmaller(startDate, endDate)) {
			Utity.showToast(getActivity().getApplication(), eDaoClientConfig.timeIllegal);
			return false;
		}

		return true;
	}

	private void pickDate(final TextView textView) {
		final DatePickerDialog mDialog = new DatePickerDialog(getActivity(), null, cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

		// 手动设置按钮
		mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
				DatePicker datePicker = mDialog.getDatePicker();
				int year = datePicker.getYear();
				int month = datePicker.getMonth() + 1;
				int day = datePicker.getDayOfMonth();

				textView.setText(year + "-" + month + "-" + day);
			}
		});
		// 取消按钮，如果不需要直接不设置即可
		mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		mDialog.show();
	}

	private void getData(String status) {
		// TODO 自动生成的方法存根
		if (isFirst)
			showProgressDialog();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "50000");
		data.put("method", "50005");
		data.put("userId", application.getUserId());
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);
		data.put("status", status);
		data.put("ordersType", "");
		data.put("startDt", startDate);
		data.put("endDt", endDate);
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
								if (!isFresh)
									showEmptyView();
								else
									Utity.showToast(getActivity().getApplicationContext(), responseData.getMsg());
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
			ViewStub noDataViewStub = (ViewStub) getView().findViewById(R.id.fragment_date_viewStub);
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
