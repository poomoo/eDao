package com.poomoo.edao.adapter;

import com.poomoo.edao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class Fragment_Home_GridViewAdapter extends BaseAdapter {

	private String[] list_name;
	private int[] list_image;
	private LayoutInflater inflater;
	private GridView gridView;

	public Fragment_Home_GridViewAdapter(Context context, String[] list_name,
			int[] list_image, GridView gridView) {
		super();
		this.list_name = list_name;
		this.list_image = list_image;
		this.gridView = gridView;
		inflater = LayoutInflater.from(context);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		for (int i = 0; i < list_name.length; i++) {

		}
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
			convertView = inflater.inflate(R.layout.item_gridview_home, parent,
					false);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.item_home_imageView);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.item_home_textView);
			viewHolder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.item_home_layout);
			viewHolder.bottomLineView = (View) convertView
					.findViewById(R.id.item_home_bottomline);
			viewHolder.rightLineView = (View) convertView
					.findViewById(R.id.item_home_rightline);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(list_name[position]);
		viewHolder.imageView.setImageResource(list_image[position]);
		// 设置layout大小，以免出现下边框不显示的情况
		viewHolder.linearLayout
				.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT,
						(gridView.getHeight() - 3) / 3));

		// 最右边就隐藏右边线
		if (position == 2 || position == 5 || position == 8) {
			viewHolder.rightLineView.setVisibility(View.GONE);
		}
		// 最下边隐藏下边线
		if (position == 6 || position == 7 || position == 8) {
			viewHolder.bottomLineView.setVisibility(View.GONE);
		}

		// 设置gridview的item的高度,与屏幕适配
		AbsListView.LayoutParams param = new AbsListView.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				gridView.getHeight() / 3);
		convertView.setLayoutParams(param);

		return convertView;
	}

	private class ViewHolder {

		private TextView textView;
		private ImageView imageView;
		private View rightLineView, bottomLineView;
		private LinearLayout linearLayout;
	}

}
