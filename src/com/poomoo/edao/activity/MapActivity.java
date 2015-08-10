package com.poomoo.edao.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.poomoo.edao.R;
import com.poomoo.edao.model.Store;

public class MapActivity extends Activity implements OnMapClickListener {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private InfoWindow mInfoWindow;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	private BitmapDescriptor bdA;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	boolean isFirstLoc = true;// 是否首次定位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		initLocation();
		mLocationClient.start();

		bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		mMapView = (MapView) findViewById(R.id.bmapView);

		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setOnMapClickListener(this);
		addInfosOverlay(Store.infos);
		initMarkerClickEvent();
	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay(List<Store> infos) {
		System.out.println("info:" + infos.size());
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		for (Store info : infos) {
			// 位置
			latLng = new LatLng(info.getLatitude(), info.getLongitude());
			// 图标
			overlayOptions = new MarkerOptions().position(latLng).icon(bdA)
					.zIndex(5);
			marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		// 将地图移到到最后一个经纬度位置
		MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(u);
	}

	private void initMarkerClickEvent() {
		// 对Marker的点击
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				// 获得marker中的数据
				Store info = (Store) marker.getExtraInfo().get("info");
				View linlayout = MapActivity.this.getLayoutInflater().inflate(
						R.layout.popup_map_inform, null);
				// 将marker所在的经纬度的信息转化成屏幕上的坐标
				final LatLng ll = marker.getPosition();
				showCurrtenStroeOnMap(ll);

				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				// 为弹出的InfoWindow添加点击事件
				mInfoWindow = new InfoWindow(
						getInfoWindowView(linlayout, info), llInfo, -10);

				// 显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		});
	}

	private View getInfoWindowView(View mMarkerLy, final Store store) {
		ViewHolder viewHolder = null;
		if (mMarkerLy.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.storeImg = (ImageView) mMarkerLy
					.findViewById(R.id.popup_map_inform_imageView_pic);
			viewHolder.storeRatingBar = (RatingBar) mMarkerLy
					.findViewById(R.id.popup_map_inform_ratingbar);
			viewHolder.storeName = (TextView) mMarkerLy
					.findViewById(R.id.popup_map_inform_textView_name);
			viewHolder.storeScore = (TextView) mMarkerLy
					.findViewById(R.id.popup_map_inform_textView_score);
			viewHolder.storeDistance = (TextView) mMarkerLy
					.findViewById(R.id.popup_map_inform_textView_distance);
			viewHolder.storeInfo = (TextView) mMarkerLy
					.findViewById(R.id.popup_map_inform_textView_inform);

			mMarkerLy.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) mMarkerLy.getTag();
		viewHolder.storeImg.setImageResource(store.getImgId());
		viewHolder.storeRatingBar.setRating(store.getScore());
		viewHolder.storeName.setText(store.getName());
		viewHolder.storeScore.setText(store.getScore() + "");
		viewHolder.storeDistance.setText(store.getDistance());
		viewHolder.storeInfo.setText(store.getInfo());
		mMarkerLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				startActivity(new Intent(MapActivity.this,
						AboutUsActivity.class));
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
		TextView storeName, storeScore, storeDistance, storeInfo;
		RatingBar storeRatingBar;
	}

	// public void initOverlay() {
	// // add marker overlay
	// LatLng llA = new LatLng(39.963175, 116.400244);
	// LatLng llB = new LatLng(39.942821, 116.369199);
	// LatLng llC = new LatLng(39.939723, 116.425541);
	// LatLng llD = new LatLng(39.906965, 116.401394);
	//
	// OverlayOptions ooA = new MarkerOptions().position(llA).icon(bdA)
	// .zIndex(9).draggable(true);
	// mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
	// OverlayOptions ooB = new MarkerOptions().position(llB).icon(bdB)
	// .zIndex(5);
	// mMarkerB = (Marker) (mBaiduMap.addOverlay(ooB));
	// OverlayOptions ooC = new MarkerOptions().position(llC).icon(bdC)
	// .perspective(false).anchor(0.5f, 0.5f).rotate(30).zIndex(7);
	// mMarkerC = (Marker) (mBaiduMap.addOverlay(ooC));
	// ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
	// giflist.add(bdA);
	// giflist.add(bdB);
	// giflist.add(bdC);
	// OverlayOptions ooD = new MarkerOptions().position(llD).icons(giflist)
	// .zIndex(0).period(10);
	// mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
	//
	// // add ground overlay
	// LatLng southwest = new LatLng(39.92235, 116.380338);
	// LatLng northeast = new LatLng(39.947246, 116.414977);
	// LatLngBounds bounds = new LatLngBounds.Builder().include(northeast)
	// .include(southwest).build();
	//
	// OverlayOptions ooGround = new GroundOverlayOptions()
	// .positionFromBounds(bounds).image(bdGround).transparency(0.8f);
	// mBaiduMap.addOverlay(ooGround);
	//
	// MapStatusUpdate u = MapStatusUpdateFactory
	// .newLatLng(bounds.getCenter());
	// mBaiduMap.setMapStatus(u);
	//
	// mBaiduMap.setOnMarkerDragListener(new OnMarkerDragListener() {
	// public void onMarkerDrag(Marker marker) {
	// }
	//
	// public void onMarkerDragEnd(Marker marker) {
	// Toast.makeText(
	// getApplicationContext(),
	// "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
	// + marker.getPosition().longitude,
	// Toast.LENGTH_LONG).show();
	// }
	//
	// public void onMarkerDragStart(Marker marker) {
	// }
	// });
	// }

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
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy(); // 回收 bitmap 资源 bdA.recycle();
		bdA.recycle();
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
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			// Toast.makeText(
			// getApplicationContext(),
			// "latitude:" + Double.toString(latitude) + "longitude:"
			// + Double.toString(longitude), 1).show();
			Log.i("latitude", Double.toString(latitude));
			Log.i("longitude", Double.toString(longitude));
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(latitude).longitude(longitude)
					.build();
			LatLng ll = new LatLng(latitude, longitude);
			OverlayOptions ooA = new MarkerOptions().position(ll).icon(bdA)
					.zIndex(9).draggable(true);
			// mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				Log.i("百度地图定位", ll.toString());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
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
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(18)
				.build();
		// 定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
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
}
