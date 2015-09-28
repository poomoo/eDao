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
import com.poomoo.edao.adapter.Store_Evaluation_ListViewAdapter;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.StoreEvaluationData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MyListView;
import com.poomoo.edao.widget.MyListView.OnRefreshListener;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 
 * @ClassName EvaluationListActivity
 * @Description TODO 评价列表
 * @author 李苜菲
 * @date 2015年8月3日 下午9:59:05
 */
public class EvaluationListActivity extends BaseActivity {
	private TextView textView_score;
	private RatingBar ratingBar;
	private MyListView listView;
	private List<StoreEvaluationData> list;
	private Store_Evaluation_ListViewAdapter adapter;
	private int curPage = 1, pageSize = 10;
	private ProgressDialog progressDialog = null;
	private Gson gson = new Gson();
	private float score = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation_list);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		// list = (ArrayList<StoreEvaluationData>) getIntent()
		// .getSerializableExtra("list");
		score = getIntent().getFloatExtra("score", 0);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_score = (TextView) findViewById(R.id.evaluation_list_avgScore);
		ratingBar = (RatingBar) findViewById(R.id.evaluation_list_ratingBar);
		listView = (MyListView) findViewById(R.id.evaluation_listView);

		textView_score.setText(String.valueOf(score));
		ratingBar.setRating(score);

		list = new ArrayList<StoreEvaluationData>();
		adapter = new Store_Evaluation_ListViewAdapter(this, list);
		listView.setAdapter(adapter);
		showProgressDialog("请稍后...");
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
		data.put("bizName", "30000");
		data.put("method", "30003");
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);
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
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());

								JSONArray pager = result.getJSONArray("records");
								int length = pager.length();
								for (int i = 0; i < length; i++) {
									StoreEvaluationData data = new StoreEvaluationData();
									data = gson.fromJson(pager.getJSONObject(i).toString(), StoreEvaluationData.class);
									list.add(data);
								}
								adapter.notifyDataSetChanged();

								curPage += 1;

							} catch (JSONException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						} else {
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
						closeProgressDialog();
						listView.onRefreshComplete();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}

				});
			}
		});
	}

}
