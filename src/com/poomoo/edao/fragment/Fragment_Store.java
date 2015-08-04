package com.poomoo.edao.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Fragment_Store_GridViewAdapter;

public class Fragment_Store extends Fragment {
	private EditText editText_keywords;
	private ImageView imageView_user, imageView_position;
	private GridView gridView;
	private Fragment_Store_GridViewAdapter gridViewAdapter;
	private final String[] list_name = { "金银首饰", "酒店娱乐", "餐饮美食", "服装鞋类", "生活超市",
			"旅游度假", "美容保健", "宣传广告", "数码电器", "皮具箱包", "酒类服务", "休闲户外", "汽车服务", "教育培训", "农副产品",
			"医药服务", "交通运输", "办公家居", "房产建材", "机械设备" };
	private final int[] list_image = { R.drawable.ic_store_jewelry,
			R.drawable.ic_store_hotel, R.drawable.ic_store_food,
			R.drawable.ic_store_clothing, R.drawable.ic_store_super_market,
			R.drawable.ic_store_travel, R.drawable.ic_store_beauty,
			R.drawable.ic_store_advertisement, R.drawable.ic_store_electronic,
			R.drawable.ic_store_bags, R.drawable.ic_store_wine,
			R.drawable.ic_store_relaxing, R.drawable.ic_store_car,
			R.drawable.ic_store_education, R.drawable.ic_store_agricultural,
			R.drawable.ic_store_medicine, R.drawable.ic_store_traffic,
			R.drawable.ic_store_office, R.drawable.ic_store_housing,
			R.drawable.ic_store_machine };

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_store, container, false);
	}

	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

		imageView_user = (ImageView) getView().findViewById(
				R.id.fragment_home_imageView_user);
		imageView_position = (ImageView) getView().findViewById(
				R.id.fragment_home_imageView_position);
		editText_keywords = (EditText) getView().findViewById(
				R.id.fragment_home_editText_keywords);
		gridView = (GridView) getView().findViewById(
				R.id.fragment_home_gridView);

		gridViewAdapter = new Fragment_Store_GridViewAdapter(getActivity(),
				list_name, list_image, gridView);
		gridView.setAdapter(gridViewAdapter);
	}

}