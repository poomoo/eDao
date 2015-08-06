package com.poomoo.edao.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.poomoo.edao.R;
import com.poomoo.edao.model.Info;

public class MapActivity extends Activity {
	/**
	 * MapView 是地图主控件
	 */
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Marker mMarkerA;
	private Marker mMarkerB;
	private Marker mMarkerC;
	private Marker mMarkerD;
	private InfoWindow mInfoWindow;

	// 初始化全局 bitmap 信息，不用时及时 recycle
	BitmapDescriptor bdA;
	private Context mContext;
	BitmapDescriptor bdB;
	BitmapDescriptor bdC;
	BitmapDescriptor bdD;
	BitmapDescriptor bd;
	BitmapDescriptor bdGround;

	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	boolean isFirstLoc = true;// 是否首次定位
	private RelativeLayout mMarkerInfoLy;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mContext = this;

		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		initLocation();
		mLocationClient.start();

		bdA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
		bdB = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
		bdC = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
		bdD = BitmapDescriptorFactory.fromResource(R.drawable.icon_markd);
		bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
		bdGround = BitmapDescriptorFactory
				.fromResource(R.drawable.ground_overlay);
		mMapView = (MapView) findViewById(R.id.bmapView);
		mMarkerInfoLy = (RelativeLayout) findViewById(R.id.id_marker_info);
		
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
		mBaiduMap.setMapStatus(msu);
		addInfosOverlay(Info.infos);
		initMarkerClickEvent();
//		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
//			public boolean onMarkerClick(final Marker marker) {
//
//				if (marker == mMarkerA || marker == mMarkerD) {
//					Toast.makeText(mContext, "当前位置", 0).show();
//				} else if (marker == mMarkerB) {
//					Toast.makeText(mContext, "跳转的页面BBBB", 0).show();
//				} else if (marker == mMarkerC) {
//					Toast.makeText(mContext, "跳转的页面CCCC", 0).show();
//				}
//				return true;
//			}
//		});
	}

	/**
	 * 初始化图层
	 */
	public void addInfosOverlay(List<Info> infos) {
		mBaiduMap.clear();
		LatLng latLng = null;
		OverlayOptions overlayOptions = null;
		Marker marker = null;
		for (Info info : infos) {
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
				Info info = (Info) marker.getExtraInfo().get("info");

				InfoWindow mInfoWindow;
				// 生成一个TextView用户在地图中显示InfoWindow
				TextView location = new TextView(getApplicationContext());
				location.setBackgroundResource(R.drawable.location_tips);
				location.setPadding(30, 20, 30, 50);
				location.setText(info.getName());
				// 将marker所在的经纬度的信息转化成屏幕上的坐标
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
				// 为弹出的InfoWindow添加点击事件
				mInfoWindow = new InfoWindow(bdA, llInfo, 1,
						new OnInfoWindowClickListener() {

							@Override
							public void onInfoWindowClick() {
								// 隐藏InfoWindow
								mBaiduMap.hideInfoWindow();
							}
						});

				// 显示InfoWindow
				mBaiduMap.showInfoWindow(mInfoWindow);
				// 设置详细信息布局为可见
				mMarkerInfoLy.setVisibility(View.VISIBLE);
				// 根据商家信息为详细信息布局设置信息
				popupInfo(mMarkerInfoLy, info);
				return true;
			}
		});
	}

	/**
	 * 根据info为布局上的控件设置信息
	 * 
	 * @param mMarkerInfo2
	 * @param info
	 */
	protected void popupInfo(RelativeLayout mMarkerLy, Info info) {
		ViewHolder viewHolder = null;
		if (mMarkerLy.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.infoImg = (ImageView) mMarkerLy
					.findViewById(R.id.info_img);
			viewHolder.infoName = (TextView) mMarkerLy
					.findViewById(R.id.info_name);
			viewHolder.infoDistance = (TextView) mMarkerLy
					.findViewById(R.id.info_distance);
			viewHolder.infoZan = (TextView) mMarkerLy
					.findViewById(R.id.info_zan);

			mMarkerLy.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) mMarkerLy.getTag();
		viewHolder.infoImg.setImageResource(info.getImgId());
		viewHolder.infoDistance.setText(info.getDistance());
		viewHolder.infoName.setText(info.getName());
		viewHolder.infoZan.setText(info.getZan() + "");
	}

	/**
	 * 复用弹出面板mMarkerLy的控件
	 * 
	 * @author
	 * 
	 */
	private class ViewHolder {
		ImageView infoImg;
		TextView infoName;
		TextView infoDistance;
		TextView infoZan;
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
		bdB.recycle();
		bdC.recycle();
		bdD.recycle();
		bd.recycle();
		bdGround.recycle();
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

	/**
	 * 实现实时位置回调监听
	 */
	// public class MyLocationListener implements BDLocationListener {
	//
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// Toast.makeText(
	// getApplicationContext(),
	// "latitude:" + Double.toString(location.getLatitude())
	// + "longitude:"
	// + Double.toString(location.getLongitude()), 1)
	// .show();
	// // Receive Location
	// StringBuffer sb = new StringBuffer(256);
	// sb.append("time : ");
	// sb.append(location.getTime());
	// sb.append("\nerror code : ");
	// sb.append(location.getLocType());
	// sb.append("\nlatitude : ");
	// sb.append(location.getLatitude());
	// sb.append("\nlontitude : ");
	// sb.append(location.getLongitude());
	// sb.append("\nradius : ");
	// sb.append(location.getRadius());
	// if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
	// sb.append("\nspeed : ");
	// sb.append(location.getSpeed());// 单位：公里每小时
	// sb.append("\nsatellite : ");
	// sb.append(location.getSatelliteNumber());
	// sb.append("\nheight : ");
	// sb.append(location.getAltitude());// 单位：米
	// sb.append("\ndirection : ");
	// sb.append(location.getDirection());
	// sb.append("\naddr : ");
	// sb.append(location.getAddrStr());
	// sb.append("\ndescribe : ");
	// sb.append("gps定位成功");
	//
	// } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {//
	// 网络定位结果
	// sb.append("\naddr : ");
	// sb.append(location.getAddrStr());
	// // 运营商信息
	// sb.append("\noperationers : ");
	// sb.append(location.getOperators());
	// sb.append("\ndescribe : ");
	// sb.append("网络定位成功");
	// } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {//
	// 离线定位结果
	// sb.append("\ndescribe : ");
	// sb.append("离线定位成功，离线定位结果也是有效的");
	// } else if (location.getLocType() == BDLocation.TypeServerError) {
	// sb.append("\ndescribe : ");
	// sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
	// } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
	// sb.append("\ndescribe : ");
	// sb.append("网络不同导致定位失败，请检查网络是否通畅");
	// } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
	// sb.append("\ndescribe : ");
	// sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
	// }
	// sb.append("\nlocationdescribe : ");// 位置语义化信息
	// sb.append(location.getLocationDescribe());
	// List<Poi> list = location.getPoiList();// POI信息
	// if (list != null) {
	// sb.append("\npoilist size = : ");
	// sb.append(list.size());
	// for (Poi p : list) {
	// sb.append("\npoi= : ");
	// sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
	// }
	// }
	// Log.i("BaiduLocationApiDem", sb.toString());
	// }
	//
	// }

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
			mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
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
	private void showMyLocationOnMap(double latitude, double longitude) {

		// 定位成功后关闭定位
		mLocationClient.stop();

		// 取消监听函数。
		mLocationClient.unRegisterLocationListener(myListener);
	}
}
