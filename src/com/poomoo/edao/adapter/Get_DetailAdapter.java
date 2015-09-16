package com.poomoo.edao.adapter;

import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.model.DetailData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Get_DetailAdapter extends BaseAdapter {

	private List<DetailData> list;
	private LayoutInflater inflater;

	public Get_DetailAdapter(Context context, List<DetailData> list) {
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
			convertView = inflater.inflate(R.layout.item_listview_get_detail,
					parent, false);
			holder = new ViewHolder();
			holder.textView_expense_time = (TextView) convertView
					.findViewById(R.id.get_detail_item_textView_expense_time);
			holder.textView_get_money = (TextView) convertView
					.findViewById(R.id.get_detail_item_textView_get_money);
			holder.textView_balance = (TextView) convertView
					.findViewById(R.id.get_detail_item_textView_balance);
			holder.textView_get_time = (TextView) convertView
					.findViewById(R.id.get_detail_item_textView_get_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView_expense_time.setText(list.get(position).getPayDt());
		holder.textView_get_money.setText("￥"+list.get(position).getGotNum());
		holder.textView_balance.setText("￥"+list.get(position).getTotalNum());
		holder.textView_get_time.setText(list.get(position).getGotTime());
		return convertView;
	}

	private class ViewHolder {
		private TextView textView_expense_time, textView_get_money,
				textView_balance, textView_get_time;

	}

}
