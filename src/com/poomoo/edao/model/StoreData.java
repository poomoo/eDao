package com.poomoo.edao.model;

import java.io.Serializable;

/**
 * 
 * @ClassName StoreData
 * @Description TODO 店铺数据
 * @author 李苜菲
 * @date 2015-8-31 上午9:27:23
 */
public class StoreData implements Serializable {
	// serialVersionUID

	private static final long serialVersionUID = -4761715977345558686L;
	private String shopId = "";
	private String shopName = "";
	private String realName = "";
	private double longitude = 0;
	private double latitude = 0;
	private String address = "";
	private String pictures = "";
	private String typeId = "";
	private float avgScore = 0;
	private String tel = "";
	private String distance = "";

	public String getShopId() {
		return this.shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return this.shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPictures() {
		return this.pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public float getAvgScore() {
		return this.avgScore;
	}

	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDistance() {
		return this.distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

}
