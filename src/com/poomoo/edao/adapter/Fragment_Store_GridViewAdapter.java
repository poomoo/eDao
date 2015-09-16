package com.poomoo.edao.adapter;

import com.poomoo.edao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_Store_GridViewAdapter extends BaseAdapter {

	private String[] list_name;
	private int[] list_image;
	private LayoutInflater inflater;
	private GridView gridView;

	public Fragment_Store_GridViewAdapter(Context context, String[] list_name,
			int[] list_image, GridView gridView) {
		super();
		this.list_name = list_name;
		this.list_image = list_image;
		this.gridView = gridView;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return list_name.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return list_name[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.item_gridview_store, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.item_fragment_store_imageView);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.item_fragment_store_textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(list_name[position]);
		viewHolder.imageView.setImageResource(list_image[position]);

		// 设置gridview的item的高度,与屏幕适配
		// AbsListView.LayoutParams param = new AbsListView.LayoutParams(
		// android.view.ViewGroup.LayoutParams.MATCH_PARENT,
		// gridView.getHeight() / 5);
		// convertView.setLayoutParams(param);

		return convertView;
	}

	private class ViewHolder {
		private TextView textView;
		private ImageView imageView;
	}

}
