package com.poomoo.edao.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.poomoo.edao.R;

public class Purchase_History_ListViewAdapter extends BaseAdapter {

	private List<HashMap<String, String>> list;
	private LayoutInflater inflater;

	public Purchase_History_ListViewAdapter(Context context,
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
					R.layout.item_listview_purchase_history, parent, false);
			holder = new ViewHolder();
			holder.textView_shop = (TextView) convertView
					.findViewById(R.id.purchase_history_item_textView_shop);
			holder.textView_money = (TextView) convertView
					.findViewById(R.id.purchase_history_item_textView_money);
			holder.textView_date = (TextView) convertView
					.findViewById(R.id.purchase_history_item_textView_date);
			holder.textView_state = (TextView) convertView
					.findViewById(R.id.purchase_history_item_textView_state);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	private class ViewHolder {
		private TextView textView_shop, textView_money, textView_date,
				textView_state;

	}

}