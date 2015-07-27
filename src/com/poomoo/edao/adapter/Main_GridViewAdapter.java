package com.poomoo.edao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.edao.R;

public class Main_GridViewAdapter extends BaseAdapter {

	private String[] list_name;
	private int[] list_image;
	private LayoutInflater inflater;

	public Main_GridViewAdapter(Context context, String[] list_name,
			int[] list_image) {
		super();
		this.list_name = list_name;
		this.list_image = list_image;
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
		System.out.println("getview");
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.acticity_main_item, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.item_main_imageView);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.item_main_textView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(list_name[position]);
		viewHolder.imageView.setBackgroundResource(list_image[position]);
		return null;
	}

	private class ViewHolder {
		private TextView textView;
		private ImageView imageView;
	}

}
