package com.poomoo.edao.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Shop_List_ListViewAdapter;

public class Fragment_Home extends Fragment {
	private EditText editText_keywords;
	private ImageView imageView_back;
	private TextView textView_classify;
	private ListView listView;
	private Shop_List_ListViewAdapter adapter;
	private List<HashMap<String, String>> list;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.activity_main, container, false);
	}

	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

		imageView_back = (ImageView) getView().findViewById(
				R.id.fragment_shop_imageView_back);
		textView_classify = (TextView) getView().findViewById(
				R.id.fragment_shop_textView_classify);
		editText_keywords = (EditText) getView().findViewById(
				R.id.fragment_home_editText_keywords);
		listView = (ListView) getView().findViewById(
				R.id.fragment_shop_listView);

		adapter = new Shop_List_ListViewAdapter(getActivity(), list);
		listView.setAdapter(adapter);

	}

}
