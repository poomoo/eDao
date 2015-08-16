package com.poomoo.edao.model.database;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @ClassName ProvinceInfo
 * @Description TODO 省份模型
 * @author 李苜菲
 * @date 2015年8月16日 下午10:50:26
 */
public class ProvinceInfo {

	private String province_id = "";
	private String province_name = "";
	private List<CityInfo> cityInfoList = new ArrayList<CityInfo>();

	public String getProvince_id() {
		return this.province_id;
	}

	public void setProvince_id(String province_id) {
		this.province_id = province_id;
	}

	public String getProvince_name() {
		return this.province_name;
	}

	public void setProvince_name(String province_name) {
		this.province_name = province_name;
	}

	public List<CityInfo> getCityInfoList() {
		return this.cityInfoList;
	}

	public void setCityInfoList(List<CityInfo> cityInfoList) {
		this.cityInfoList = cityInfoList;
	}

}
