package com.poomoo.edao.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.model.StoreEvaluationData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.app.ProgressDialog;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

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
			textView_info, textView_address, textView_tel, textView_more,
			textView_evaluate_name, textView_evaluate_date,
			textView_evaluate_info;
	private ImageView imageView;
	private RatingBar ratingBar_all, ratingBar_evaluate;
	private LinearLayout layout_evaluate;

	private StoreData listData;
	private List<StoreEvaluationData> list;
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_information);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		listData = (StoreData) getIntent().getSerializableExtra("data");
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
		textView_evaluate_name = (TextView) findViewById(R.id.item_store_evaluate_textView_name);
		textView_evaluate_date = (TextView) findViewById(R.id.item_store_evaluate_textView_date);
		textView_evaluate_info = (TextView) findViewById(R.id.item_store_evaluate_textView_info);

		imageView = (ImageView) findViewById(R.id.store_information_imageView_pic);

		ratingBar_all = (RatingBar) findViewById(R.id.store_information_ratingBar);
		ratingBar_evaluate = (RatingBar) findViewById(R.id.item_store_evaluate_ratingBar);
		layout_evaluate = (LinearLayout) findViewById(R.id.store_information_layout_evaluation);

		textView_name.setText(listData.getShopName());
		textView_score.setText(String.valueOf(listData.getAvgScore()));
		textView_distance.setText(listData.getDistance());
		textView_address.setText(listData.getAddress());
		textView_tel.setText(listData.getTel());
		ratingBar_all.setRating(listData.getAvgScore());
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 缩放图片
				.build();
		ImageLoader.getInstance().displayImage(listData.getPictures(),
				imageView, options);

		textView_more.setOnClickListener(this);
		// textView_info.setText(data.getShopName());
		list = new ArrayList<StoreEvaluationData>();
		getData();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.store_information_textView_more:
			Bundle pBundle = new Bundle();
			pBundle.putSerializable("list", (Serializable) list);
			pBundle.putFloat("score", listData.getAvgScore());
			openActivity(EvaluationListActivity.class, pBundle);
			break;
		}
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "30000");
		data.put("method", "30006");
		data.put("currPage", 1);
		data.put("pageSize", 10);
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
										int length = pager.length();
										for (int i = 0; i < length; i++) {
											StoreEvaluationData data = new StoreEvaluationData();
											data = gson.fromJson(pager
													.getJSONObject(i)
													.toString(),
													StoreEvaluationData.class);
											list.add(data);
										}

										layout_evaluate
												.setVisibility(View.VISIBLE);
										textView_evaluate_name.setText(list
												.get(0).getRealName());
										textView_evaluate_date.setText(list
												.get(0).getAppraiseDt());
										textView_evaluate_info.setText(list
												.get(0).getContent());
										ratingBar_evaluate.setRating(list
												.get(0).getScore());
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
