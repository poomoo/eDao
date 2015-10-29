package com.poomoo.edao.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.map.Cluster;
import com.poomoo.edao.map.MBound;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MapActivity extends BaseActivity
		implements OnMapClickListener, OnMapStatusChangeListener, OnClickListener {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LinearLayout layout_store;
	private TextView textView_curCity;
	private ImageView imageView_mylocation;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	boolean isFirstLoc = true;// 是否首次定位
	boolean isFirstChange = true;// 是否首次改变地图状态
	// 图层最大级别
	private final float maxZoom = 14;
	private float curZoom = 0;
	private float lastZoom = 0;
	// 当前图层中心点经纬度
	private LatLng curMapCenterLatLng = null;
	private LatLng lastMapCenterLatLng = null;
	/**
	 * 最新一次的经纬度
	 */
	private double mCurrentLantitude;
	private double mCurrentLongitude;

	private String curCity = "定位中...";
	private Gson gson = new Gson();
	private List<StoreData> list;

	private Timer timerSyncNef = null;// 定时
	private TimerTask syncNef = null;// 定时任务

	private Cluster mCluster;
	private Integer mGridSize = 60;
	private Boolean isAverageCenter = false;
	private double mDistance = 100;// 聚合距离
	private double radius = 5000;// 搜索半径
	private boolean allow = false;

	public static MapActivity instance = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		setImmerseLayout(findViewById(R.id.map_layout));
		instance = this;

		init();

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		initLocation();
		mLocationClient.start();

		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(maxZoom);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setOnMapClickListener(this);
		mBaiduMap.setOnMapStatusChangeListener(this);

		initMarkerClickEvent();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_curCity = (TextView) findViewById(R.id.map_textView_curcity);
		layout_store = (LinearLayout) findViewById(R.id.map_layout_store);

		mMapView = (MapView) findViewById(R.id.bmapView);
		imageView_mylocation = (ImageView) findViewById(R.id.map_imageView_mylocaiton);

		textView_curCity.setText(curCity);
		mBaiduMap = mMapView.getMap();
		layout_store.setOnClickListener(this);
		imageView_mylocation.setOnClickListener(this);

		list = new ArrayList<StoreData>();

		mCluster = new Cluster(this, mBaiduMap, mGridSize, isAverageCenter, mGridSize, mDistance);
	}

	private void initMarkerClickEvent() {
		// 对Marker的点击
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 将marker所在的经纬度的信息转化成屏幕上的坐标
				System.out.println("点击marker:" + marker.getPosition() + marker.toString());
				final LatLng ll = marker.getPosition();
				int index = marker.getZIndex();
				if (index == 2) {
					float tempZoom = 18;
					if (curZoom != 17)
						tempZoom = curZoom + 1.0f;
					showCurrtenStroeOnMap(ll, tempZoom);
				} else {
					// 获得marker中的数据
					StoreData info = (StoreData) marker.getExtraInfo().get("info");

					Bundle pBundle = new Bundle();
					pBundle.putSerializable("data", info);
					openActivity(StoreInformationActivity.class, pBundle);
				}
				return true;
			}
		});
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
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			curMapCenterLatLng = mBaiduMap.getMapStatus().target;
			mCurrentLantitude = location.getLatitude();
			mCurrentLongitude = location.getLongitude();
			curCity = location.getCity();
			textView_curCity.setText(curCity);
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(mCurrentLantitude).longitude(mCurrentLongitude).build();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				getData();
			}

		}
	}

	/**
	 * 根据传入的经纬度在地图上显示
	 * 
	 * @param latitude
	 * @param longitude
	 */
	private void showCurrtenStroeOnMap(LatLng cenpt, float zoom) {
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(zoom).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		allow = true;
		// 改变地图状态
		mBaiduMap.setMapStatus(mMapStatusUpdate);
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO 自动生成的方法存根
		mBaiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void onMapStatusChange(MapStatus arg0) {
		// TODO 自动生成的方法存根

		// if (allow) {
		// System.out.println("onMapStatusChange");
		// reFreshMapData();
		// }
		//
		// allow = false;
	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) {
		// TODO 自动生成的方法存根
		System.out.println("onMapStatusChangeFinish");
		reFreshMapData();
	}

	private void reFreshMapData() {
		if (isFirstChange)
			isFirstChange = false;
		else {
			if (timerSyncNef != null)
				timerSyncNef.cancel();
			syncNef = null;
			syncNef = new TimerTask() {
				@Override
				public void run() {
					try {
						curMapCenterLatLng = mBaiduMap.getMapStatus().target;

						lastZoom = curZoom;
						curZoom = mBaiduMap.getMapStatus().zoom;
						System.out.println("地图状态改变:" + lastMapCenterLatLng + ":" + curMapCenterLatLng);
						mCurrentLongitude = curMapCenterLatLng.longitude;
						mCurrentLantitude = curMapCenterLatLng.latitude;

						if (isRefresh()) {
							lastMapCenterLatLng = curMapCenterLatLng;
							Message msg = new Message();
							msg.what = 1;
							myHandler.sendMessage(msg);
						}

						syncNef = null;
						timerSyncNef.cancel();
						this.cancel();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timerSyncNef = null;
			timerSyncNef = new Timer(true);
			timerSyncNef.schedule(syncNef, eDaoClientConfig.mapRefresh, 1);// 拖动地图停止2秒钟后执行
		}
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {
		// TODO 自动生成的方法存根
		System.out.println("onMapStatusChangeStart");
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.map_imageView_mylocaiton:
			LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
			// float zoom = mBaiduMap.getMapStatus().zoom;
			// // showCurrtenStroeOnMap(ll, zoom);
			System.out.println("点击定位:" + ll);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			Utity.showToast(getApplicationContext(), "正在定位中...");
			break;
		case R.id.map_layout_store:
			Bundle pBundle = new Bundle();
			pBundle.putString("fromFlag", "map");
			pBundle.putSerializable("list", (Serializable) list);
			openActivity(ShopListActivity.class, pBundle);
			break;
		}

	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
		mLocationClient.unRegisterLocationListener(myListener); // 取消注册监听函数
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
	}

	@Override
	protected void onDestroy() {
		mMapView.removeAllViews();
		mMapView.onDestroy();
		super.onDestroy(); // 回收 bitmap 资源 bdA.recycle();
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "30000");
		data.put("method", "30007");
		data.put("longitude", mCurrentLongitude);
		data.put("latitude", mCurrentLantitude);
		calcRadius();
		data.put("radius", radius);
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (responseData.getRsCode() == 1 && responseData.getJsonData().length() > 0) {
							mBaiduMap.clear();
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());

								JSONArray data = result.getJSONArray("records");

								list.clear();
								int length = data.length();
								for (int i = 0; i < length; i++) {
									StoreData storeData = new StoreData();
									storeData = gson.fromJson(data.getJSONObject(i).toString(), StoreData.class);
									list.add(storeData);
								}
								// StoreData storeData = new StoreData();
								// storeData.setPictures("http://pic.nipic.com/2007-11-09/2007119121849495_2.jpg");
								// storeData.setLatitude(26.612613);
								// storeData.setLongitude(106.634157);
								// storeData.setShopName("测试1");
								// storeData.setRealName("测试人1");
								// list.add(storeData);
								//
								// storeData = new StoreData();
								// storeData.setPictures("http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg");
								// storeData.setLatitude(26.613008);
								// storeData.setLongitude(106.633726);
								// storeData.setShopName("测试2");
								// storeData.setRealName("测试人2");
								// list.add(storeData);
								// addInfosOverlay(list);
								System.out.println("当前图层级别:" + mBaiduMap.getMapStatus().zoom);
								if (mBaiduMap.getMapStatus().zoom >= 18) {
									// mBaiduMap.clear();
									// refreshVersionClusterMarker(markers);
								} else {
									System.out.println("重新绘制");
									mBaiduMap.clear();
									switch ((int) mBaiduMap.getMapStatus().zoom) {
									case 17:
										mDistance = 50;
										break;
									case 16:
										mDistance = 100;
										break;
									case 15:
										mDistance = 200;
										break;
									case 14:
										mDistance = 500;
										break;
									case 13:
										mDistance = 1000;
										break;
									case 12:
										mDistance = 2000;
										break;
									case 11:
										mDistance = 5000;
										break;
									case 10:
										mDistance = 10*1000;
										break;
									case 9:
										mDistance = 20*1000;
										break;
									case 8:
										mDistance = 25*1000;
										break;
									case 7:
										mDistance = 50*1000;
										break;
									case 6:
										mDistance = 100*1000;
										break;
									case 5:
										mDistance = 200*1000;
										break;
									case 4:
										mDistance = 500*1000;
										break;
									default:
										mDistance = 1000*1000;
										break;
									}

									mCluster.setmDistance(mDistance);
									mCluster.setmBaiduMap(mBaiduMap);
									mCluster.setInfos(list);
									// mCluster.createCluster(refreshVersionClusterMarker(markers));
									mCluster.createCluster(list);
								}
							} catch (JSONException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						}
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
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}

				});
			}
		});
	}

	private Boolean isRefresh() {
		if (curZoom != lastZoom) {// 图层改变
			return true;
		} else {
			MBound bound = new MBound(lastMapCenterLatLng, lastMapCenterLatLng);
			bound = Utity.getExtendedBounds(mBaiduMap, bound, 255);
			if (!Utity.isMarkersInCluster(curMapCenterLatLng, bound)) {
				System.out.println("当前图层中心坐标不在范围内时刷新");
				return true;
			} else
				System.out.println("当前图层中心坐标在范围内");
		}

		return false;
	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO 自动生成的方法存根
			super.handleMessage(msg);
			if (msg.what == 1) {
				showProgressDialog("加载中...");
				getData();
			}
		}

	};

	private void calcRadius() {
		LatLngBounds bounds = mBaiduMap.getMapStatus().bound;
		LatLng NE = bounds.northeast;
		LatLng SW = bounds.southwest;
		double neDis = DistanceUtil.getDistance(curMapCenterLatLng, NE);
		double swDis = DistanceUtil.getDistance(curMapCenterLatLng, SW);
		radius = Math.min(neDis, swDis);
	}
}
