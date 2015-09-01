package com.poomoo.edao.model;

/**
 * 
 * @ClassName MywalletData
 * @Description TODO 我的钱包数据
 * @author 李苜菲
 * @date 2015-8-27 下午2:52:45
 */
public class MywalletData {
	private String handlingFee = "";
	private String covertMinFee = "";
	private String covertMaxFee = "";
	private int totalEb = 0;
	private String bankName = "";
	private String bankCardId = "";

	public String getHandlingFee() {
		return this.handlingFee;
	}

	public void setHandlingFee(String handlingFee) {
		this.handlingFee = handlingFee;
	}

	public String getCovertMinFee() {
		return this.covertMinFee;
	}

	public void setCovertMinFee(String covertMinFee) {
		this.covertMinFee = covertMinFee;
	}

	public String getCovertMaxFee() {
		return this.covertMaxFee;
	}

	public void setCovertMaxFee(String corvertMaxFee) {
		this.covertMaxFee = corvertMaxFee;
	}

	public int getTotalEb() {
		return this.totalEb;
	}

	public void setTotalEb(int totalEb) {
		this.totalEb = totalEb;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankCardId() {
		return this.bankCardId;
	}

	public void setBankCardId(String bankCardId) {
		this.bankCardId = bankCardId;
	}

}
