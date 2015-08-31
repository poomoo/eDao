package com.poomoo.edao.model;

import java.io.Serializable;

/**
 * 
 * @ClassName StoreEvaluationData
 * @Description TODO 店铺评价数据
 * @author 李苜菲
 * @date 2015-8-31 下午2:17:07
 */
public class StoreEvaluationData implements Serializable {
	// serialVersionUID

	private static final long serialVersionUID = -3517901693623080375L;
	private String shopId = "";
	private String userId = "";
	private String realName = "";
	private String content = "";
	private float score = 0;
	private String appraiseDt = "";

	public String getShopId() {
		return this.shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

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

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public float getScore() {
		return this.score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getAppraiseDt() {
		return this.appraiseDt;
	}

	public void setAppraiseDt(String appraiseDt) {
		this.appraiseDt = appraiseDt;
	}

}
