package com.poomoo.edao.model;

/**
 * 
 * @ClassName OrderListData
 * @Description TODO 订单列表数据
 * @author 李苜菲
 * @date 2015年8月30日 下午4:02:56
 */
public class OrderListData {
	private String ordersId = "";
	private String tradeName="";
	private String shopId = "";
	private String shopName="";
	private String payFee = "";
	private String status = "";
	private String ordersDt = "";
	private String remark = "";
	private String handlingFee = "";

	public String getOrdersId() {
		return this.ordersId;
	}

	public void setOrdersId(String ordersId) {
		this.ordersId = ordersId;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
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

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrdersDt() {
		return this.ordersDt;
	}

	public void setOrdersDt(String ordersDt) {
		this.ordersDt = ordersDt;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getHandlingFee() {
		return handlingFee;
	}

	public void setHandlingFee(String handlingFee) {
		this.handlingFee = handlingFee;
	}

}
