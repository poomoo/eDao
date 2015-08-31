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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Shop_List_ListViewAdapter;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.ShopListData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName StoreInformationActivity
 * @Description TODO 商铺详情
 * @author 李苜菲
 * @date 2015年8月3日 下午9:38:57
 */
public class StoreInformationActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_name, textView_score, textView_distance,
			textView_info, textView_address, textView_tel, textView_more;
	private RatingBar ratingBar;
	private LinearLayout layout;

	private ShopListData listData;
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_information);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		listData = (ShopListData) getIntent().getSerializableExtra("data");
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_name = (TextView) findViewById(R.id.store_information_textView_store_name);
		textView_score = (TextView) findViewById(R.id.store_information_textView_score);
		textView_distance = (TextView) findViewById(R.id.store_information_textView_distance);
		textView_info = (TextView) findViewById(R.id.store_information_textView_info);
		textView_address = (TextView) findViewById(R.id.store_information_textView_address);
		textView_tel = (TextView) findViewById(R.id.store_information_textView_tel);
		textView_more = (TextView) findViewById(R.id.store_information_textView_more);

		ratingBar = (RatingBar) findViewById(R.id.store_information_ratingBar);
		layout = (LinearLayout) findViewById(R.id.store_information_layout_evaluation);

		textView_name.setText(listData.getShopName());
		textView_score.setText(String.valueOf(listData.getAvgScore()));
		textView_distance.setText(listData.getDistance());
		textView_address.setText(listData.getAddress());
		textView_tel.setText(listData.getTel());
		ratingBar.setRating(listData.getAvgScore());
		// textView_info.setText(data.getShopName());
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.store_information_textView_more:
			openActivity(EvaluationListActivity.class);
			break;
		}
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "30000");
		data.put("method", "30006");
		data.put("currPage", 0);
		data.put("pageSize", 1);
		data.put("shopId", listData.getShopId());
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

					@Override
					public void onFinish(final ResponseData responseData) {
						// TODO 自动生成的方法存根
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

										ShopListData shopList = new ShopListData();
										shopList = gson.fromJson(pager
												.getJSONObject(0).toString(),
												ShopListData.class);
										layout.setVisibility(View.VISIBLE);
									} catch (JSONException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
									}
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
								Utity.showToast(getApplicationContext(),
										"获取用户评价失败 " + eDaoClientConfig.checkNet);
							}

						});
					}
				});
	}
}
