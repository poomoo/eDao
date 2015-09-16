package com.poomoo.edao.adapter;

import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.model.StoreEvaluationData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class Store_Evaluation_ListViewAdapter extends BaseAdapter {

	private List<StoreEvaluationData> list;
	private LayoutInflater inflater;

	public Store_Evaluation_ListViewAdapter(Context context,
			List<StoreEvaluationData> list) {
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
					R.layout.item_listview_store_evaluate, parent, false);
			holder = new ViewHolder();
			holder.textView_realName = (TextView) convertView
					.findViewById(R.id.item_store_evaluate_textView_name);
			holder.textView_date = (TextView) convertView
					.findViewById(R.id.item_store_evaluate_textView_date);
			holder.textView_info = (TextView) convertView
					.findViewById(R.id.item_store_evaluate_textView_info);
			holder.bar = (RatingBar) convertView
					.findViewById(R.id.item_store_evaluate_ratingBar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView_realName.setText(list.get(position).getRealName());
		holder.textView_date.setText(list.get(position).getAppraiseDt());
		holder.textView_info.setText(list.get(position).getContent());
		holder.bar.setRating(list.get(position).getScore());
		return convertView;
	}

	private class ViewHolder {
		private TextView textView_realName, textView_date, textView_info;
		private RatingBar bar;
	}

}
