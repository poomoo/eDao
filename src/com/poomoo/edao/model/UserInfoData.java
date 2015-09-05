package com.poomoo.edao.model;

/**
 * 
 * @ClassName UserInfoData
 * @Description TODO 用户信息
 * @author 李苜菲
 * @date 2015-9-1 上午10:42:20
 */
public class UserInfoData {

	private String userId = "";
	private String realName = "";
	private String tel = "";
	private String type = "";// 用户类型 1普通会员，2：加盟会员
	private String realNameAuth = "";// 0默认未认证，1认证通过，2认证未通过
	private String idCardNum = "";// 身份证编号
	private String quickmarkPic = "";// 二维码
	private String bankProvince = "";// 开户省
	private String bankCity = "";// 开户市
	private String bankName = "";// 开户行名称
	private String bankCardId = "";// 银行卡号
	private String payPwdValue = "";// 是否设置过支付密码
	private String joinType = "";// 加盟类型 1-加盟商 2-经销商 3-合作商
	private String joinStatus = "";// 加盟审核 0,"默认值，未审核" 1,"审核通过" 2,"审核未通过"

	private int totalEb = 0;// 总的意币
	private int totalGold = 0;// 总金币
	private int totalIntegral = 0;// 总的积分

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getRealNameAuth() {
		return this.realNameAuth;
	}

	public void setRealNameAuth(String realNameAuth) {
		this.realNameAuth = realNameAuth;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdCardNum() {
		return this.idCardNum;
	}

	public void setIdCardNum(String idCardNum) {
		this.idCardNum = idCardNum;
	}

	public String getQuickmarkPic() {
		return this.quickmarkPic;
	}

	public void setQuickmarkPic(String quickmarkPic) {
		this.quickmarkPic = quickmarkPic;
	}

	public String getBankProvince() {
		return this.bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return this.bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
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

	public String getPayPwdValue() {
		return this.payPwdValue;
	}

	public void setPayPwdValue(String payPwdValue) {
		this.payPwdValue = payPwdValue;
	}

	public String getJoinType() {
		return this.joinType;
	}

	public void setJoinType(String joinType) {
		this.joinType = joinType;
	}

	public String getJoinStatus() {
		return this.joinStatus;
	}

	public void setJoinStatus(String joinStatus) {
		this.joinStatus = joinStatus;
	}

	public int getTotalEb() {
		return this.totalEb;
	}

	public void setTotalEb(int totalEb) {
		this.totalEb = totalEb;
	}

	public int getTotalGold() {
		return this.totalGold;
	}

	public void setTotalGold(int totalGold) {
		this.totalGold = totalGold;
	}

	public int getTotalIntegral() {
		return this.totalIntegral;
	}

	public void setTotalIntegral(int totalIntegral) {
		this.totalIntegral = totalIntegral;
	}

}
