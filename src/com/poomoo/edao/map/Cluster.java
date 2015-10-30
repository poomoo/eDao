package com.poomoo.edao.map;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.edao.R;
import com.poomoo.edao.activity.MapActivity;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.util.Utity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class Cluster {

	private MapActivity context;
	private BaiduMap mBaiduMap;
	private Boolean isAverageCenter;
	// private int mGridSize;
	private double mDistance;
	private BitmapDescriptor bitmap = null;
	private List<ClusterMarker> mClusterMarkers;
	private OverlayOptions overlayOptions = null;
	private List<StoreData> infos = null;
	private LatLng latLng = null;
	private boolean isAlone = false;
	private LatLngBounds bounds = null;

	public Cluster(MapActivity context, BaiduMap mBaiduMap, int minClusterSize, Boolean isAverageCenter, int mGridSize,
			double mDistance) {
		this.context = context;
		this.mBaiduMap = mBaiduMap;
		this.isAverageCenter = isAverageCenter;
		// this.mGridSize = mGridSize;
		this.mDistance = mDistance;

		mClusterMarkers = new ArrayList<ClusterMarker>();
	}

	public void createCluster(List<StoreData> StoreDataList) {
		System.out.println("进入createCluster:" + StoreDataList.size());
		this.mClusterMarkers.clear();

		for (int i = 0; i < StoreDataList.size(); i++) {
			addCluster(StoreDataList.get(i));
		}
		System.out.println("mClusterMarkers:" + mClusterMarkers.size());
		mBaiduMap.clear();

		for (int i = 0; i < mClusterMarkers.size(); i++) {
			ClusterMarker cm = mClusterMarkers.get(i);
			System.out.println("i:" + i + cm.getmStoreDatas().get(0).getPictures());
			setClusterDrawable(cm);
			System.out.println("isAlone:" + isAlone);
			if (!isAlone) {
				if (Utity.isMarkerInBounds(cm.getmCenter(), bounds)) {
					Marker marker = (Marker) mBaiduMap.addOverlay(cm.getOverlayOptions());
					Bundle bundle = new Bundle();
					bundle.putSerializable("isAlone", false);
					marker.setExtraInfo(bundle);
					System.out.println("添加marker");
				}

			}
		}
	}

	private void addCluster(StoreData storeData) {
		latLng = new LatLng(storeData.getLatitude(), storeData.getLongitude());
		// 没有ClusterMarkers
		if (mClusterMarkers.size() == 0) {
			ClusterMarker clusterMarker = new ClusterMarker(latLng);
			clusterMarker.AddInfo(storeData, isAverageCenter);

			// MBound bound = new MBound(latLng, latLng);
			// bound = Utity.getExtendedBounds(mBaiduMap, bound, mGridSize);
			// clusterMarker.setmGridBounds(bound);
			mClusterMarkers.add(clusterMarker);
		} else {
			ClusterMarker clusterContain = null;
			double distance = mDistance;

			for (int i = 0; i < mClusterMarkers.size(); i++) {
				ClusterMarker clusterMarker = mClusterMarkers.get(i);
				LatLng center = clusterMarker.getmCenter();
				double d = DistanceUtil.getDistance(center, latLng);
				System.out.println("相距:" + d + "设定距离:" + distance);
				if (d < distance) {
					// distance = d;
					clusterContain = clusterMarker;
					System.out.println("满足距离条件:" + clusterContain);
					break;
				}

			}
			// 现存的clusterMarker 没有符合条件的
			// if (clusterContain == null || !Utity.isMarkersInCluster(latLng,
			// clusterContain.getmGridBounds())) {
			// 暂时去掉边界判断，如果出现问题再修改
			if (clusterContain == null) {
				System.out.println("没有符合条件的");
				ClusterMarker clusterMarker = new ClusterMarker(latLng);
				clusterMarker.AddInfo(storeData, isAverageCenter);

				// MBound bound = new MBound(latLng, latLng);
				// bound = Utity.getExtendedBounds(mBaiduMap, bound, mGridSize);
				// clusterMarker.setmGridBounds(bound);
				mClusterMarkers.add(clusterMarker);
			} else {
				System.out.println("符合条件");
				clusterContain.AddInfo(storeData, isAverageCenter);
			}
		}
	}

	private void setClusterDrawable(ClusterMarker clusterMarker) {

		View drawableView = LayoutInflater.from(context).inflate(R.layout.drawable_mark, null);
		TextView text = (TextView) drawableView.findViewById(R.id.drawble_mark);
		text.setPadding(3, 3, 3, 3);

		int markNum = clusterMarker.getmStoreDatas().size();
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
			overlayOptions = new MarkerOptions().position(clusterMarker.getmCenter()).icon(bitmap).zIndex(2);
			clusterMarker.setOverlayOptions(overlayOptions);
			isAlone = false;
		} else {
			// 获得marker中的数据
			StoreData info = clusterMarker.getmStoreDatas().get(0);
			LatLng latLngTmp = new LatLng(info.getLatitude(), info.getLongitude());
			System.out.println(info.getPictures() + "LatLng:" + latLngTmp + "bounds:" + bounds);
			if (Utity.isMarkerInBounds(latLngTmp, bounds)) {
				System.out.println("在范围内");
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
								View linlayout = MapActivity.instance.getLayoutInflater()
										.inflate(R.layout.popup_map_inform, null);
								BitmapDescriptor bitmap = BitmapDescriptorFactory
										.fromView(getInfoWindowView(linlayout, info, loadedImage));

								overlayOptions = new MarkerOptions().position(latLng).icon(bitmap).zIndex(1);
								Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
								System.out.println("添加marker:" + marker.toString());
								Bundle bundle = new Bundle();
								bundle.putSerializable("info", info);
								marker.setExtraInfo(bundle);
							}
						}
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						// TODO Auto-generated method stub
						System.out.println("加载图片失败:" + imageUri);
						for (StoreData info : infos) {
							if (info.getPictures().equals(imageUri)) {
								// 位置
								latLng = new LatLng(info.getLatitude(), info.getLongitude());
								// 构建Marker图标
								View linlayout = MapActivity.instance.getLayoutInflater()
										.inflate(R.layout.popup_map_inform, null);
								BitmapDescriptor bitmap = BitmapDescriptorFactory
										.fromView(getInfoWindowView(linlayout, info, null));

								overlayOptions = new MarkerOptions().position(latLng).icon(bitmap).zIndex(1);
								Marker marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
								System.out.println("添加marker:" + marker.toString());
								Bundle bundle = new Bundle();
								bundle.putSerializable("info", info);
								marker.setExtraInfo(bundle);
							}
						}
					}

				});
			}
			isAlone = true;
		}
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
		if (bitmap != null)
			viewHolder.storeImg.setImageBitmap(bitmap);
		else
			ImageLoader.getInstance().displayImage(store.getPictures(), viewHolder.storeImg, options);

		viewHolder.storeRatingBar.setRating(store.getAvgScore());
		viewHolder.storeName.setText(store.getShopName());
		viewHolder.storeOwner.setText(Utity.addStarByName(store.getRealName()));
		return mMarkerLy;
	}

	public BaiduMap getmBaiduMap() {
		return mBaiduMap;
	}

	public void setmBaiduMap(BaiduMap mBaiduMap) {
		this.mBaiduMap = mBaiduMap;
		this.bounds = mBaiduMap.getMapStatus().bound;
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
