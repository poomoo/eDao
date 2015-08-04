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
	private final String[] list_name = { "金银珠宝", "酒店", "餐饮", "服装", "生活超市",
			"旅游", "美容养生", "保健品", "家电", "皮具", "酒类", "娱乐", "汽车", "批发类", "农副产品",
			"医药", "交通运输", "办公用品", "房产建材", "机械设备" };
	private final int[] list_image = { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };

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
