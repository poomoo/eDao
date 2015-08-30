package com.poomoo.edao.model;

/**
 * 
 * @ClassName OrderListData
 * @Description TODO 订单列表数据
 * @author 李苜菲
 * @date 2015年8月30日 下午4:02:56
 */
public class OrderListData {
	private String id = "";
	private String money = "";
	private String state = "";
	private String date = "";

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMoney() {
		return this.money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
