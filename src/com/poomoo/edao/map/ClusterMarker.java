package com.poomoo.edao.map;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.util.Log;

public class ClusterMarker {
	private LatLng mCenter;
	private List<Marker> mMarkers;
	private MBound mGridBounds;
	private OverlayOptions overlayOptions = null;

	public ClusterMarker(LatLng ll) {
		this.mCenter = ll;
		mMarkers = new ArrayList<Marker>();
	}

	/**
	 * 计算平均中心点
	 * 
	 * @return
	 */
	private LatLng calAverageCenter() {
		double latitude = 0, longitude = 0;
		int len = mMarkers.size() == 0 ? 1 : mMarkers.size();

		Log.e("calAverageCenter:", "calAverageCenter：------>" + len);

		for (int i = 0; i < len; i++) {
			latitude = latitude + mMarkers.get(i).getPosition().latitude;
			longitude = longitude + mMarkers.get(i).getPosition().longitude;
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
	public void AddMarker(Marker marker, Boolean isAverageCenter) {
		mMarkers.add(marker);

		if (!isAverageCenter) {
			if (mCenter == null)
				this.mCenter = mMarkers.get(0).getPosition();
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

	public List<Marker> getmMarkers() {
		return mMarkers;
	}

	public void setmMarkers(List<Marker> mMarkers, Boolean isAverageCenter) {
		this.mMarkers.addAll(mMarkers);
		if (!isAverageCenter) {
			if (mCenter == null) {
				this.mCenter = mMarkers.get(0).getPosition();
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
