package com.poomoo.edao.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.poomoo.edao.R;

public class Store implements Serializable {
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
	 * 评分
	 */
	private float score;
	/**
	 * 信息
	 */
	private String info;

	public static List<Store> infos = new ArrayList<Store>();

	static {
		infos.add(new Store(26.612562, 106.636244, R.drawable.ic_store_pic,
				"乐意道加盟商", "209米", 4.7f, "90起送"));
		infos.add(new Store(26.612433, 106.635911, R.drawable.ic_store_pic,
				"乐意道合作商", "897米", 4.8f, "100起送"));
		infos.add(new Store(26.612264, 106.637236, R.drawable.ic_store_pic,
				"乐意道经销商", "249米", 4.9f, "110起送"));

	}

	public Store(double latitude, double longitude, int imgId, String name,
			String distance, float score, String info) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.imgId = imgId;
		this.name = name;
		this.distance = distance;
		this.score = score;
		this.info = info;
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

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
