package com.poomoo.edao.map;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.poomoo.edao.model.StoreData;

import android.util.Log;

public class ClusterMarker {
	private LatLng mCenter;
	private List<StoreData> mStoreDatas;
	private MBound mGridBounds;
	private OverlayOptions overlayOptions = null;

	public ClusterMarker(LatLng ll) {
		this.mCenter = ll;
		mStoreDatas = new ArrayList<StoreData>();
	}

	/**
	 * 计算平均中心点
	 * 
	 * @return
	 */
	private LatLng calAverageCenter() {
		double latitude = 0, longitude = 0;
		int len = mStoreDatas.size() == 0 ? 1 : mStoreDatas.size();

		Log.e("calAverageCenter:", "calAverageCenter：------>" + len);

		for (int i = 0; i < len; i++) {
			latitude = latitude + mStoreDatas.get(i).getLatitude();
			longitude = longitude + mStoreDatas.get(i).getLongitude();
		}

		return new LatLng((int) (latitude / len), (int) (longitude / len));
	}

	/**
	 * 
	 * ClusterMarker 中添加marker
	 * 
	 * @param marker
	 * @param isAverageCenter
	 */
	public void AddInfo(StoreData storeData, Boolean isAverageCenter) {
		mStoreDatas.add(storeData);

		if (!isAverageCenter) {
			if (mCenter == null) {
				LatLng latLng = new LatLng(storeData.getLatitude(), storeData.getLongitude());
				this.mCenter = latLng;
			}
		} else {
			this.mCenter = calAverageCenter();
		}
	}

	public LatLng getmCenter() {
		return this.mCenter;
	}

	public void setmCenter(LatLng mCenter) {
		this.mCenter = mCenter;
	}

	public OverlayOptions getOverlayOptions() {
		return overlayOptions;
	}

	public void setOverlayOptions(OverlayOptions overlayOptions) {
		this.overlayOptions = overlayOptions;
	}

	public List<StoreData> getmStoreDatas() {
		return mStoreDatas;
	}

	public void setmStoreDatas(List<StoreData> mStoreDatas, Boolean isAverageCenter) {
		this.mStoreDatas.addAll(mStoreDatas);
		if (!isAverageCenter) {
			if (mCenter == null) {
				LatLng latLng = new LatLng(mStoreDatas.get(0).getLatitude(), mStoreDatas.get(0).getLongitude());
				this.mCenter = latLng;
			}
		} else
			this.mCenter = calAverageCenter();
	}

	public MBound getmGridBounds() {
		return mGridBounds;
	}

	public void setmGridBounds(MBound mGridBounds) {
		this.mGridBounds = mGridBounds;
	}
}
