package com.poomoo.edao.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poomoo.edao.R;

public class Pub_GridViewAdapter extends BaseAdapter {

	private List<HashMap<String, String>> list;
	private LayoutInflater inflater;
	public static SparseArray<TextView> textViews = new SparseArray<TextView>();

	public Pub_GridViewAdapter(Context context,
			List<HashMap<String, String>> list) {
		super();
		this.list = list;
		inflater = LayoutInflater.from(context);
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
		System.out.println("getview:" + position);
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_gridview_pub, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.item_pub_textView);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(list.get(position).get("name"));
		textViews.put(position, viewHolder.textView);
		if (position == 0)
			viewHolder.textView.setTextColor(Color.parseColor("#1995EB"));
		return convertView;
	}

	private class ViewHolder {

		public TextView textView;
	}

	public void setTextColor() {
		int length = textViews.size();
		System.out.println("setTextColor:" + length);
		for (int i = 0; i < length; i++) {
			textViews.get(i).setTextColor(Color.parseColor("#808080"));
		}
	}
}
