package com.poomoo.edao.activity;

import java.io.Serializable;
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
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class MapActivity extends BaseActivity
		implements OnMapClickListener, OnMapStatusChangeListener, OnClickListener {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;

	private LinearLayout layout_store;
	private TextView textView_curCity;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	private BitmapDescriptor bd;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();;

	boolean isFirstLoc = true;// 是否首次定位

	private ImageView imageView_center_dot, imageView_mylocation;
	// 图层最大级别
	private final float maxRoom = 14;
	// 当前图层中心点经纬度
	private LatLng curCenterLatLng = null;
	private OverlayManager overlayManager;

	/**
	 * 最新一次的经纬度
	 */
	private double mCurrentLantitude;
	private double mCurrentLongitude;
	private String curCity = "定位中...";
	private Gson gson = new Gson();
	private List<StoreData> list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		setImmerseLayout(findViewById(R.id.map_layout));

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		initLocation();
		mLocationClient.start();

		init();

		bd = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_icon);
		mMapView = (MapView) findViewById(R.id.bmapView);
		imageView_center_dot = (ImageView) findViewById(R.id.map_imageView_center_dot);
		imageView_mylocation = (ImageView) findViewById(R.id.map_imageView_mylocaiton);
		imageView_mylocation.setOnClickListener(this);

		mBaiduMap = mMapView.getMap();

		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(maxRoom);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setOnMapClickListener(this);
		mBaiduMap.setOnMapStatusChangeListener(this);
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_curCity = (TextView) findViewById(R.id.map_textView_curcity);
		layout_store = (LinearLayout) findViewById(R.id.map_layout_store);

		textView_curCity.setText(curCity);
		layout_store.setOnClickListener(this);

		list = new ArrayList<StoreData>();
	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay(List<StoreData> infos) {
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		int i = 0;
		final List<OverlayOptions> list = new ArrayList<OverlayOptions>();
		for (StoreData info : infos) {
			// 位置
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			// 构建Marker图标
			View linlayout = MapActivity.this.getLayoutInflater().inflate(R.layout.popup_map_inform, null);
			BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(getInfoWindowView(linlayout, info));
			// BitmapDescriptor bitmap =
			// BitmapDescriptorFactory.fromView(linlayout);
			overlayOptions = new MarkerOptions().position(latLng).icon(bitmap).zIndex(i++);
			list.add(overlayOptions);
			System.out.println("list.add");
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			// marker.setIcon(bitmap);
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		LatLng myLL = new LatLng(mCurrentLantitude, mCurrentLongitude);
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(myLL);
		mBaiduMap.setMapStatus(u);
	}

	// // 获取坐标点
	// public void addInfosOverlay(List<StoreData> infos) {
	// mBaiduMap.clear();
	// LatLng latLng = null;
	// OverlayOptions overlayOptions = null;
	// Marker marker = null;
	// int i = 0;
	// final List<OverlayOptions> list = new ArrayList<OverlayOptions>();
	//
	// for (StoreData info : infos) {
	// // 用给定的经纬度构造GeoPoint，单位是微度 (度 * 1E6)
	// GeoPoint p = new GeoPoint((int) (info.getLatitude() * 1E6), (int)
	// (info.getLongitude() * 1E6));
	// // 准备overlay图像数据，根据实情情况修复
	// Drawable mark = drawBitmap(info);
	// mark.setBounds(0, 0, mark.getIntrinsicWidth(),
	// mark.getIntrinsicHeight());
	// OverlayItem item = new OverlayItem(p, null, null);
	// item.setMarker(mark);
	//
	// // 创建ItemizedOverlay
	// CustomOverlay itemOverlay = new CustomOverlay(mark, mMapView,
	// MapActivity.this);
	// // mMapView.getOverlay().add(itemOverlay);
	// itemOverlay.addItem(item);
	//
	// Bundle bundle = new Bundle();
	// bundle.putSerializable("info", info);
	// marker.setExtraInfo(bundle);
	// }
	// LatLng myLL = new LatLng(mCurrentLantitude, mCurrentLongitude);
	// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(myLL);
	// mBaiduMap.setMapStatus(u);
	//
	//
	// // 将IteminizedOverlay添加到MapView中
	// // mMapView.getOverlays().clear();
	//
	//
	// // 现在所有准备工作已准备好，使用以下方法管理overlay.
	// // 添加overlay, 当批量添加Overlay时使用addItem(List<OverlayItem>)效率更高
	// // itemOverlay.addItem(item1);
	// // itemOverlay.addItem(item2);
	// // itemOverlay.addItem(item3);
	// // mMapView.refresh();
	// }

	// public Drawable drawBitmap(StoreData data) {
	// Bitmap bmp = Bitmap.createBitmap(166, 126, Bitmap.Config.ARGB_8888);
	// Canvas canvas = new Canvas(bmp);
	// LayoutInflater inflater = (LayoutInflater)
	// getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// View layout = inflater.inflate(R.layout.popup_map_inform, null);
	// TextView storeName = (TextView)
	// layout.findViewById(R.id.popup_map_inform_textView_name);
	// TextView storeOwner = (TextView)
	// layout.findViewById(R.id.popup_map_inform_textView_owner);
	// ImageView storeImg = (ImageView)
	// layout.findViewById(R.id.popup_map_inform_imageView_pic);
	// // 使用ImageLoader加载网络图片
	// DisplayImageOptions options = new DisplayImageOptions.Builder()//
	// .showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
	// .showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
	// .cacheInMemory(true) // 内存缓存
	// .cacheOnDisk(true) // sdcard缓存
	// .bitmapConfig(Config.RGB_565)// 设置最低配置
	// .imageScaleType(ImageScaleType.EXACTLY)// 缩放图片
	// .build();
	//
	// storeName.setText(data.getShopName());
	// storeOwner.setText(data.getRealName());
	// ImageLoader.getInstance().displayImage(data.getPictures(), storeImg,
	// options);
	//
	// layout.setDrawingCacheEnabled(true);
	// layout.measure(View.MeasureSpec.makeMeasureSpec(canvas.getWidth(),
	// View.MeasureSpec.EXACTLY),
	// View.MeasureSpec.makeMeasureSpec(canvas.getHeight(),
	// View.MeasureSpec.EXACTLY));
	// layout.layout(0, 0, layout.getMeasuredWidth(),
	// layout.getMeasuredHeight());
	// Paint paint = new Paint();
	// canvas.drawBitmap(layout.getDrawingCache(), 0, 0, paint);
	// Drawable drawable = new BitmapDrawable(getResources(), bmp);
	// return drawable;
	// }

	private void initMarkerClickEvent() {
		// 对Marker的点击
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 将marker所在的经纬度的信息转化成屏幕上的坐标
				final LatLng ll = marker.getPosition();
				if (mBaiduMap.getMapStatus().zoom != maxRoom) {
					showCurrtenStroeOnMap(ll);
				} else {
					showCurrtenStroeOnMap(ll);
					// 获得marker中的数据
					StoreData info = (StoreData) marker.getExtraInfo().get("info");
					// View linlayout =
					// MapActivity.this.getLayoutInflater().inflate(R.layout.popup_map_inform,
					// null);
					// //
					// linlayout.setBackgroundResource(R.drawable.ic_map_popup_bg);
					// Point p = mBaiduMap.getProjection().toScreenLocation(ll);
					// p.y -= 60;
					// LatLng llInfo =
					// mBaiduMap.getProjection().fromScreenLocation(p);
					// // 为弹出的InfoWindow添加点击事件
					// mInfoWindow = new InfoWindow(getInfoWindowView(linlayout,
					// info), llInfo, 1);
					// // 显示InfoWindow
					// mBaiduMap.showInfoWindow(mInfoWindow);
					Bundle pBundle = new Bundle();
					pBundle.putSerializable("data", info);
					openActivity(StoreInformationActivity.class, pBundle);
				}

				return true;
			}
		});
	}

	private View getInfoWindowView(final View mMarkerLy, final StoreData store) {
		ViewHolder viewHolder = null;
		if (mMarkerLy.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.storeImg = (ImageView) mMarkerLy.findViewById(R.id.popup_map_inform_imageView_pic);
			viewHolder.storeRatingBar = (RatingBar) mMarkerLy.findViewById(R.id.popup_map_inform_ratingbar);
			viewHolder.storeName = (TextView) mMarkerLy.findViewById(R.id.popup_map_inform_textView_name);
			viewHolder.storeOwner = (TextView) mMarkerLy.findViewById(R.id.popup_map_inform_textView_owner);
			// viewHolder.storeScore = (TextView)
			// mMarkerLy.findViewById(R.id.popup_map_inform_textView_score);
			// viewHolder.storeDistance = (TextView)
			// mMarkerLy.findViewById(R.id.popup_map_inform_textView_distance);
			// viewHolder.storeInfo = (TextView)
			// mMarkerLy.findViewById(R.id.popup_map_inform_textView_inform);

			mMarkerLy.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) mMarkerLy.getTag();
		// 使用ImageLoader加载网络图片
		DisplayImageOptions options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.ic_launcher) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.imageScaleType(ImageScaleType.EXACTLY)// 缩放图片
				.build();
		System.out.println("加载图片:" + store.getPictures());
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.loadImage(store.getPictures(), new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// 图片处理
				System.out.println("加载图片成功" + loadedImage);
			}
		});
		viewHolder.storeImg.setImageBitmap(ImageLoader.getInstance().loadImageSync(store.getPictures()));
		// ImageLoader.getInstance().loadImageSync(store.getPictures());
		// ImageLoader.getInstance().displayImage(store.getPictures(),
		// viewHolder.storeImg, options);
		System.out.println("加载图片完成");

		viewHolder.storeRatingBar.setRating(store.getAvgScore());
		viewHolder.storeName.setText(store.getShopName());
		viewHolder.storeOwner.setText(store.getRealName());
		// viewHolder.storeScore.setText(store.getAvgScore() + "");
		// viewHolder.storeDistance.setText(store.getDistance());
		// viewHolder.storeInfo.setText(store.getAddress());
		mMarkerLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Bundle pBundle = new Bundle();
				pBundle.putSerializable("data", store);
				openActivity(StoreInformationActivity.class, pBundle);
			}
		});
		return mMarkerLy;
	}

	/**
	 * 复用弹出面板mMarkerLy的控件
	 * 
	 * @author
	 * 
	 */
	private class ViewHolder {
		ImageView storeImg;
		TextView storeName, storeOwner;
		RatingBar storeRatingBar;
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
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			mCurrentLantitude = location.getLatitude();
			mCurrentLongitude = location.getLongitude();
			curCity = location.getCity();
			textView_curCity.setText(curCity);
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(mCurrentLantitude).longitude(mCurrentLongitude).build();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			// 设置自定义图标
			// BitmapDescriptor mCurrentMarker =
			// BitmapDescriptorFactory.fromResource(R.drawable.ic_map_icon);
			// MyLocationConfiguration config = new
			// MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,
			// false, mCurrentMarker);
			// mBaiduMap.setMyLocationConfigeration(config);
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
	private void showCurrtenStroeOnMap(LatLng cenpt) {
		// 定义地图状态
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(maxRoom).build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
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
		curCenterLatLng = mBaiduMap.getMapStatus().target;
	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) {
		// TODO 自动生成的方法存根
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {
		// TODO 自动生成的方法存根
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.map_imageView_mylocaiton:
			LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			break;
		case R.id.map_layout_store:
			Bundle pBundle = new Bundle();
			pBundle.putString("fromFlag", "map");
			pBundle.putSerializable("list", (Serializable) list);
			openActivity(ShopListActivity.class, pBundle);
			break;
		}

	}

	/**
	 * 清除所有Overlay
	 * 
	 * @param view
	 */
	public void clearOverlay(View view) {
		mBaiduMap.clear();
	}

	/**
	 * 重新添加Overlay
	 * 
	 * @param view
	 */
	public void resetOverlay(View view) {
		clearOverlay(null);
		// initOverlay();
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
		mMapView.onDestroy();
		super.onDestroy(); // 回收 bitmap 资源 bdA.recycle();
		bd.recycle();
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "30000");
		data.put("method", "30007");

		data.put("longitude", mCurrentLongitude);
		data.put("latitude", mCurrentLantitude);
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
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());

								JSONArray data = result.getJSONArray("records");
								int length = data.length();
								for (int i = 0; i < length; i++) {
									StoreData storeData = new StoreData();
									storeData = gson.fromJson(data.getJSONObject(i).toString(), StoreData.class);
									list.add(storeData);
								}
								addInfosOverlay(list);
								initMarkerClickEvent();
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

}
