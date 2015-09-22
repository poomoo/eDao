package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Shop_List_ListViewAdapter;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MyListView;
import com.poomoo.edao.widget.MyListView.OnRefreshListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName ShopListActivity
 * @Description TODO 店铺列表
 * @author 李苜菲
 * @date 2015-8-4 下午3:43:49
 */
public class ShopListActivity extends BaseActivity implements OnItemClickListener, OnClickListener {
	private EditText editText_keywords;
	private ImageView imageView_back;
	private TextView textView_classify;
	private MyListView listView;
	private View noDataView;

	private Shop_List_ListViewAdapter adapter;
	private List<StoreData> list;

	private LocationClient mLocationClient = null;
	private BDLocationListener myListener = new MyLocationListener();

	private double curLon = 0.0, curLat = 0.0;
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private String categoryId = ""; // 1金银首饰 2酒店娱乐 3餐饮美食4服装鞋类5生活超市6旅游度假7美容保健
									// 8宣传广告
									// 9数码电器10皮具箱包11酒店服务12户外休闲13汽车服务14教育培训15农副产品16医药服务17交通运输18办公家具19
									// 建房建材 20 机械设备
	private String fromFlag = "";// 来源activity map-地图 store-商城
	private boolean isFresh = false;// 是否刷新标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_list);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.shop_list_layout));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

		imageView_back = (ImageView) findViewById(R.id.shop_list_imageView_back);
		textView_classify = (TextView) findViewById(R.id.shop_list_textView_classify);
		editText_keywords = (EditText) findViewById(R.id.shop_list_editText_keywords);
		listView = (MyListView) findViewById(R.id.shop_list_listView);

		fromFlag = getIntent().getStringExtra("fromFlag");
		if (fromFlag.equals("map")) {
			list = (ArrayList<StoreData>) getIntent().getSerializableExtra("list");
		} else {
			categoryId = getIntent().getStringExtra("categoryId");
			textView_classify.setText(eDaoClientConfig.store_class[Integer.parseInt(categoryId) - 1]);

			list = new ArrayList<StoreData>();
		}
		
		showProgressDialog();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener);
		initLocation();
		mLocationClient.start();

		adapter = new Shop_List_ListViewAdapter(ShopListActivity.this, list);
		listView.setAdapter(adapter);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				isFresh = true;
				getData();
			}
		});

		listView.setOnItemClickListener(this);
		imageView_back.setOnClickListener(this);
		textView_classify.setOnClickListener(this);

	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，
		int span = 1 * 10 * 1000;

		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			curLat = location.getLatitude();
			curLon = location.getLongitude();
			getData();
			mLocationClient.unRegisterLocationListener(myListener);
		}
	}

	private void getData() {
		// TODO 自动生成的方法存根
		if (!isFresh) {
			int size = list.size();
			if (size > 0) {
				list.removeAll(list);
				adapter.notifyDataSetChanged();
				listView.setAdapter(adapter);
			}
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "30000");
		data.put("method", "30003");
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);
		data.put("categoryId", categoryId);
		data.put("goodsName", "");
		data.put("centerLon ", curLon);
		data.put("centerLat", curLat);
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						System.out.println("onFinish");
						System.out.println("getJsonData:" + responseData.getJsonData().length());
						if (responseData.getRsCode() == 1 && responseData.getJsonData().length() > 0) {
							showListView();
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());

								JSONArray pager = result.getJSONArray("records");
								int length = pager.length();
								for (int i = 0; i < length; i++) {
									StoreData shopList = new StoreData();
									shopList = gson.fromJson(pager.getJSONObject(i).toString(), StoreData.class);
									list.add(shopList);
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle pBundle = new Bundle();
		pBundle.putSerializable("data", list.get(arg2));
		openActivity(StoreInformationActivity.class, pBundle);
	}

	public void showEmptyView() {
		listView.setVisibility(View.GONE);
		if (noDataView == null) {
			ViewStub noDataViewStub = (ViewStub) findViewById(R.id.shop_list_viewStub);
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
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.shop_list_imageView_back:
			finish();
			break;
		case R.id.shop_list_textView_classify:
			openActivityForResult(PubActivity.class, 1);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == 1) {
			String name = data.getStringExtra("name");
			categoryId = data.getStringExtra("id");
			textView_classify.setText(name);
			curPage = 1;
			pageSize = 10;
			isFresh = false;
			getData();
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
	 * @throws @date
	 *             2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ImageLoader.getInstance().clearMemoryCache();
		ImageLoader.getInstance().clearDiskCache();
	}

}
