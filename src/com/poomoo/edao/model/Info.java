package com.poomoo.edao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poomoo.edao.R;

public class Info implements Serializable {
	private static final long serialVersionUID = -758459502806858414L;
	/**
	 * 精度
	 */
	private double latitude;
	/**
	 * 纬度
	 */
	private double longitude;
	/**
	 * 图片ID，真实项目中可能是图片路径
	 */
	private int imgId;
	/**
	 * 商家名称
	 */
	private String name;
	/**
	 * 距离
	 */
	private String distance;
	/**
	 * 赞数量
	 */
	private int zan;

	public static List<Info> infos = new ArrayList<Info>();

	static {
		infos.add(new Info(26.612562, 106.636244, R.drawable.a01, "龙禧苑1",
				"距离209米", 1456));
		infos.add(new Info(26.612433, 106.635911, R.drawable.a02, "龙禧苑2",
				"距离897米", 456));
		infos.add(new Info(26.612264, 106.637236, R.drawable.a03, "龙禧苑3",
				"距离249米", 1456));

	}

	public Info() {
	}

	public Info(double latitude, double longitude, int imgId, String name,
			String distance, int zan) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.name = name;
		this.distance = distance;
		this.zan = zan;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getName() {
		return name;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getZan() {
		return zan;
	}

	public void setZan(int zan) {
		this.zan = zan;
	}

}
