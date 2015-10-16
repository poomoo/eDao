package com.poomoo.edao.map;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.edao.R;
import com.poomoo.edao.activity.MapActivity;
import com.poomoo.edao.activity.StoreInformationActivity;
import com.poomoo.edao.model.StoreData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Cluster {

	private MapActivity context;
	private BaiduMap mBaiduMap;
	private int mMinClusterSize;// 没有使用
	private Boolean isAverageCenter;
	private int mGridSize;
	private double mDistance;
	private BitmapDescriptor bitmap = null;
	private List<ClusterMarker> mClusterMarkers;
	private OverlayOptions overlayOptions = null;
	private List<StoreData> infos = null;
	private LatLng latLng = null;
	private Marker marker = null;
	private boolean isAlone = false;

	public Cluster(MapActivity context, BaiduMap mBaiduMap, int minClusterSize, Boolean isAverageCenter, int mGridSize,
			double mDistance) {
		this.context = context;
		this.mBaiduMap = mBaiduMap;
		this.mMinClusterSize = minClusterSize;
		this.isAverageCenter = isAverageCenter;
		this.mGridSize = mGridSize;
		this.mDistance = mDistance;

		mClusterMarkers = new ArrayList<ClusterMarker>();
	}

	public void createCluster(List<Marker> markerList) {
		System.out.println("进入createCluster");
		this.mClusterMarkers.clear();
		// ArrayList<Marker> itemList = new ArrayList<Marker>();

		for (int i = 0; i < markerList.size(); i++) {
			addCluster(markerList.get(i));
		}
		System.out.println("mClusterMarkers:" + mClusterMarkers.size());
		for (int i = 0; i < mClusterMarkers.size(); i++) {
			ClusterMarker cm = mClusterMarkers.get(i);
			setClusterDrawable(cm);
			if (!isAlone) {
				Marker marker = (Marker) mBaiduMap.addOverlay(cm.getOverlayOptions());
				Bundle bundle = new Bundle();
				bundle.putSerializable("isAlone", false);
				marker.setExtraInfo(bundle);
				System.out.println("添加marker");
				// itemList.add(marker);
			}
		}

		// System.out.println("itemList:" + itemList.size());
		// return itemList;
	}

	private void addCluster(Marker marker) {
		LatLng markGeo = marker.getPosition();
		// 没有ClusterMarkers
		if (mClusterMarkers.size() == 0) {
			ClusterMarker clusterMarker = new ClusterMarker(marker.getPosition());
			clusterMarker.AddMarker(marker, isAverageCenter);
			MBound bound = new MBound(markGeo, markGeo);
			bound = getExtendedBounds(mBaiduMap, bound, mGridSize);
			clusterMarker.setmGridBounds(bound);
			mClusterMarkers.add(clusterMarker);
		} else {
			ClusterMarker clusterContain = null;
			double distance = mDistance;

			for (int i = 0; i < mClusterMarkers.size(); i++) {
				ClusterMarker clusterMarker = mClusterMarkers.get(i);
				LatLng center = clusterMarker.getmCenter();
				double d = DistanceUtil.getDistance(center, markGeo);
				System.out.println("两点间的距离:" + d + "设定距离:" + distance);
				if (d < distance) {
					distance = d;
					clusterContain = clusterMarker;
					System.out.println("满足聚合条件");
				} else {
					System.out.println("不满足聚合条件");
				}
			}
			// 现存的clusterMarker 没有符合条件的
			if (clusterContain == null || !isMarkersInCluster(markGeo, clusterContain.getmGridBounds())) {
				// "======clusterContain=======================--------------");
				System.out.println("现存的clusterMarker 没有符合条件的");
				ClusterMarker clusterMarker = new ClusterMarker(marker.getPosition());
				clusterMarker.AddMarker(marker, isAverageCenter);
				MBound bound = new MBound(markGeo, markGeo);
				bound = getExtendedBounds(mBaiduMap, bound, mGridSize);
				clusterMarker.setmGridBounds(bound);
				mClusterMarkers.add(clusterMarker);
			} else {
				clusterContain.AddMarker(marker, isAverageCenter);
			}
		}
	}

	private void setClusterDrawable(ClusterMarker clusterMarker) {

		View drawableView = LayoutInflater.from(context).inflate(R.layout.drawable_mark, null);
		TextView text = (TextView) drawableView.findViewById(R.id.drawble_mark);
		text.setPadding(3, 3, 3, 3);

		int markNum = clusterMarker.getmMarkers().size();
		if (markNum >= 2) {
			text.setText(markNum + "");
			if (markNum < 11) {
				text.setBackgroundResource(R.drawable.m0);
			} else if (markNum > 10 && markNum < 21) {
				text.setBackgroundResource(R.drawable.m1);
			} else if (markNum > 20 && markNum < 31) {
				text.setBackgroundResource(R.drawable.m2);
			} else if (markNum > 30 && markNum < 41) {
				text.setBackgroundResource(R.drawable.m3);
			} else {
				text.setBackgroundResource(R.drawable.m4);
			}
			bitmap = BitmapDescriptorFactory.fromView(drawableView);
			overlayOptions = new MarkerOptions().position(clusterMarker.getmCenter()).icon(bitmap).zIndex(14);
			clusterMarker.setOverlayOptions(overlayOptions);
			isAlone = false;
		} else {
			// 获得marker中的数据
			StoreData info = (StoreData) clusterMarker.getmMarkers().get(0).getExtraInfo().get("info");
			final List<OverlayOptions> overlayOptionsList = new ArrayList<OverlayOptions>();

			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.loadImage(info.getPictures(), new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// 图片处理
					System.out.println("加载图片成功:" + imageUri);
					for (StoreData info : infos) {
						if (info.getPictures().equals(imageUri)) {
							// 位置
							latLng = new LatLng(info.getLatitude(), info.getLongitude());
							// 构建Marker图标
							View linlayout = MapActivity.instance.getLayoutInflater().inflate(R.layout.popup_map_inform,
									null);
							BitmapDescriptor bitmap = BitmapDescriptorFactory
									.fromView(getInfoWindowView(linlayout, info, loadedImage));
							overlayOptions = new MarkerOptions().position(latLng).icon(bitmap).zIndex(14);
							overlayOptionsList.add(overlayOptions);
							marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
							Bundle bundle = new Bundle();
							bundle.putSerializable("isAlone", true);
							marker.setExtraInfo(bundle);
						}
					}
				}
			});
			isAlone = true;
		}
	}

	/**
	 * 判断坐标点是否在MBound 覆盖区域内
	 * 
	 * @param markerGeo
	 * @param bound
	 * @return
	 */
	private Boolean isMarkersInCluster(LatLng markerGeo, MBound bound) {
		System.out.println("markerGeo.latitude:" + markerGeo.latitude + ":" + bound.getLeftBottomLat() + ","
				+ bound.getRightTopLat() + "/n" + "markerGeo.longitude:" + markerGeo.longitude + ":"
				+ bound.getLeftBottomLng() + "," + bound.getRightTopLng());
		if (markerGeo.latitude > bound.getLeftBottomLat() && markerGeo.latitude < bound.getRightTopLat()
				&& markerGeo.longitude > bound.getLeftBottomLng() && markerGeo.longitude < bound.getRightTopLng()) {
			System.out.println("markerGeo:" + markerGeo + "在范围内");
			return true;
		}
		System.out.println("markerGeo:" + markerGeo + "不在范围内");
		return false;

	}

	private MBound getExtendedBounds(BaiduMap map, MBound bound, Integer gridSize) {
		// Log.d("getExtendBounds", "size:"+gridSize);
		MBound tbounds = cutBoundsInRange(bound);

		Projection projection = map.getProjection();
		Point pixelNE = new Point();
		Point pixelSW = new Point();

		pixelNE = projection.toScreenLocation(tbounds.getRightTop());
		pixelSW = projection.toScreenLocation(tbounds.getLeftBottom());

		pixelNE.x += gridSize;
		pixelNE.y -= gridSize;
		pixelSW.x -= gridSize;
		pixelSW.y += gridSize;
		LatLng rightTop = projection.fromScreenLocation(pixelNE);
		LatLng leftBottom = projection.fromScreenLocation(pixelSW);

		return new MBound(rightTop, leftBottom);
	}

	private MBound cutBoundsInRange(MBound bounds) {
		double maxX = getRange(bounds.getRightTopLat(), -74000000, 74000000);
		double minX = getRange(bounds.getRightTopLat(), -74000000, 74000000);
		double maxY = getRange(bounds.getRightTopLng(), -180000000, 180000000);
		double minY = getRange(bounds.getLeftBottomLng(), -180000000, 180000000);
		return new MBound(minX, minY, maxX, maxY);
	}

	private double getRange(double i, double min, double max) {
		i = Math.max(i, min);
		i = Math.min(i, max);
		return i;
	}

	private class ViewHolder {
		ImageView storeImg;
		TextView storeName, storeOwner;
		RatingBar storeRatingBar;
	}

	private View getInfoWindowView(View mMarkerLy, final StoreData store, Bitmap bitmap) {
		ViewHolder viewHolder = null;
		if (mMarkerLy.getTag() == null) {
			viewHolder = new ViewHolder();
			viewHolder.storeImg = (ImageView) mMarkerLy.findViewById(R.id.popup_map_inform_imageView_pic);
			viewHolder.storeRatingBar = (RatingBar) mMarkerLy.findViewById(R.id.popup_map_inform_ratingbar);
			viewHolder.storeName = (TextView) mMarkerLy.findViewById(R.id.popup_map_inform_textView_name);
			viewHolder.storeOwner = (TextView) mMarkerLy.findViewById(R.id.popup_map_inform_textView_owner);

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
		viewHolder.storeImg.setImageBitmap(bitmap);
		System.out.println("加载图片完成");

		viewHolder.storeRatingBar.setRating(store.getAvgScore());
		viewHolder.storeName.setText(store.getShopName());
		viewHolder.storeOwner.setText(store.getRealName());
		mMarkerLy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Bundle pBundle = new Bundle();
				pBundle.putSerializable("data", store);
				Intent intent = new Intent(MapActivity.instance, StoreInformationActivity.class);
				intent.putExtras(pBundle);
				MapActivity.instance.startActivity(intent);
			}
		});
		return mMarkerLy;
	}

	public BaiduMap getmBaiduMap() {
		return mBaiduMap;
	}

	public void setmBaiduMap(BaiduMap mBaiduMap) {
		this.mBaiduMap = mBaiduMap;
	}

	public double getmDistance() {
		return mDistance;
	}

	public void setmDistance(double mDistance) {
		this.mDistance = mDistance;
	}

	public List<StoreData> getInfos() {
		return infos;
	}

	public void setInfos(List<StoreData> infos) {
		this.infos = infos;
	}

}
