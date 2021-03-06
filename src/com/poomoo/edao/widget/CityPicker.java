package com.poomoo.edao.widget;

import java.util.ArrayList;

import com.poomoo.edao.R;
import com.poomoo.edao.model.database.AreaInfo;
import com.poomoo.edao.model.database.CityInfo;
import com.poomoo.edao.model.database.ProvinceInfo;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.ScrollerNumberPicker.OnSelectListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 
 * @ClassName CityPicker
 * @Description TODO 城市选择器
 * @author 李苜菲
 * @date 2015-8-17 上午11:05:02
 */
public class CityPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker areaPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private int tempCounyIndex = -1;
	private Context context;

	private ArrayList<ProvinceInfo> provinceList = new ArrayList<ProvinceInfo>();
	private ArrayList<CityInfo> cityList = new ArrayList<CityInfo>();
	private ArrayList<AreaInfo> areaList = new ArrayList<AreaInfo>();

	public static String province_name = "", city_name = "", area_name = "",
			province_id = "", city_id = "", area_id = "";
	// private static int province_position = 0, city_position = 0,
	// area_position = 0;
	private static String zone_string = "";

	public CityPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public CityPicker(Context context) {
		super(context);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.widget_city_picker,
				this);
		// 获取控件引用
		provincePicker = (ScrollerNumberPicker) findViewById(R.id.picker_province);
		cityPicker = (ScrollerNumberPicker) findViewById(R.id.picker_city);
		areaPicker = (ScrollerNumberPicker) findViewById(R.id.picker_area);

		provinceList = Utity.getProvinceList();
		provincePicker.setProcinceData(provinceList);
		// 判断当前省份是否存在 是-显示当前省份
		if (TextUtils.isEmpty(province_name)) {
			province_name = provinceList.get(0).getProvince_name();
			province_id = provinceList.get(0).getProvince_id();
			provincePicker.setDefault(0);
		} else {
			int position = Utity.getProvincePosition(provinceList,
					province_name);
			province_id = provinceList.get(position).getProvince_id();
			provincePicker.setDefault(position);
		}

		cityList = Utity.getCityList(province_id);
		cityPicker.setCityData(cityList);
		if (TextUtils.isEmpty(city_name)) {
			city_name = cityList.get(0).getCity_name();
			city_id = cityList.get(0).getCity_id();
			cityPicker.setDefault(0);
		} else {
			int position = Utity.getCityPosition(cityList, city_name);
			city_id = cityList.get(position).getCity_id();
			cityPicker.setDefault(position);
		}

		areaList = Utity.getAreaList(city_id);
		areaPicker.setAreaData(areaList);
		System.out.println("area_name:" + area_name);
		if (TextUtils.isEmpty(area_name)) {
			area_name = areaList.get(0).getArea_name();
			area_id = areaList.get(0).getArea_id();
			areaPicker.setDefault(0);
		} else {
			int position = Utity.getAreaPosition(areaList, area_name);
			areaPicker.setDefault(position);
			area_id = areaList.get(position).getArea_id();
		}

		provincePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(text))
					return;
				province_name = text;
				province_id = provinceList.get(id).getProvince_id();
				if (tempProvinceIndex != id) {
					System.out.println("endselect");
					// String city = cityPicker.getSelectedText();
					// if (TextUtils.isEmpty(city))
					// return;
					// String area = areaPicker.getSelectedText();
					// if (TextUtils.isEmpty(area))
					// return;
					city_id = provincePicker.getItemId(id);
					// 城市数组
					cityList = Utity.getCityList(province_id);
					cityPicker.setCityData(cityList);
					cityPicker.setDefault(0);

					areaList = Utity.getAreaList(cityPicker.getItemId(0));
					areaPicker.setAreaData(areaList);
					areaPicker.setDefault(0);
					System.out.println("cityList大小:" + cityList.size());
					if (cityList.size() > 0) {
						city_name = cityList.get(0).getCity_name();
						city_id = cityList.get(0).getCity_id();
						area_name = areaList.get(0).getArea_name();
						area_id = areaList.get(0).getArea_id();
					} else {
						city_name = "";
						area_name = "";
						city_id = province_id;
						area_id = province_id;
					}
					int lastId = Integer.valueOf(provincePicker.getListSize());
					if (id > lastId) {
						provincePicker.setDefault(lastId - 1);
					}
				}
				tempProvinceIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub
			}
		});
		cityPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(text))
					return;
				city_name = text;
				city_id = cityList.get(id).getCity_id();
				// city_position = id;
				if (temCityIndex != id) {
					String province = provincePicker.getSelectedText();
					if (TextUtils.isEmpty(province))
						return;
					String area = areaPicker.getSelectedText();
					if (TextUtils.isEmpty(area))
						return;
					areaList = Utity.getAreaList(city_id);
					areaPicker.setAreaData(areaList);
					if (areaList.size() > 0) {
						areaPicker.setDefault(0);
						area_name = areaList.get(0).getArea_name();
						area_id = areaList.get(0).getArea_id();
					}

					int lastId = Integer.valueOf(cityPicker.getListSize());
					if (id > lastId) {
						cityPicker.setDefault(lastId - 1);
					}
				}
				temCityIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
		areaPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub

				if (TextUtils.isEmpty(text))
					return;
				area_name = text;
				// area_position = id;
				if (tempCounyIndex != id) {
					String province = provincePicker.getSelectedText();
					if (TextUtils.isEmpty(province))
						return;
					String city = cityPicker.getSelectedText();
					if (TextUtils.isEmpty(city))
						return;
					// 区域id
					area_id = areaPicker.getItemId(id);
					int lastId = Integer.valueOf(areaPicker.getListSize());
					if (id > lastId) {
						areaPicker.setDefault(lastId - 1);
					}
				}
				tempCounyIndex = id;
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
		areaPicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根

			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public static String getProvince_id() {
		return province_id;
	}

	public static String getCity_id() {
		return city_id;
	}

	public static String getArea_id() {
		return area_id;
	}

	public static String getZone_string() {
		if (!TextUtils.isEmpty(city_name) && !TextUtils.isEmpty(area_name))
			zone_string = province_name + "-" + city_name + "-" + area_name;
		else
			zone_string = province_name;
		return zone_string;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
