package com.poomoo.edao.model;

/**
 * 
 * @ClassName Purchase_History
 * @Description TODO 消费记录
 * @author 李苜菲
 * @date 2015-8-27 上午10:10:44
 */
public class Purchase_History {
	private String shopName = "";
	private String payFee = "";
	private String payDt = "";
	private String rank = "";

	public String getShopName() {
		return this.shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getPayFee() {
		return this.payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getPayDt() {
		return this.payDt;
	}

	public void setPayDt(String payDt) {
		this.payDt = payDt;
	}

	public String getRank() {
		return this.rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}
}
