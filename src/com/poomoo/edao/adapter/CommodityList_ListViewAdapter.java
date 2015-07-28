package com.poomoo.edao.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.edao.R;

public class CommodityList_ListViewAdapter extends BaseAdapter {

	private List<HashMap<String, String>> list;
	private LayoutInflater inflater;

	public CommodityList_ListViewAdapter(Context context,
			List<HashMap<String, String>> list) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.activity_commoditylist_item, parent, false);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.commoditylist_item_imageView);
			holder.textView_name = (TextView) convertView
					.findViewById(R.id.commoditylist_item_textView_name);
			holder.textView_price = (TextView) convertView
					.findViewById(R.id.commoditylist_item_textView_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView imageView;
		private TextView textView_name, textView_price;

	}

}
