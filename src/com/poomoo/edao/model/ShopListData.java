package com.poomoo.edao.model;

/**
 * 
 * @ClassName ShopList
 * @Description TODO 店铺列表
 * @author 李苜菲
 * @date 2015-8-25 上午10:43:51
 */
public class ShopListData {
	private String distance = "";// 店铺与当前位置的距离
	private String shopId = "";
	private String shopName = "";
	private float avgScore = 0;
	private String pictures = "";
	private String address = "";

	public String getDistance() {
		return this.distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

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

	public float getAvgScore() {
		return this.avgScore;
	}

	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public String getPictures() {
		return this.pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
