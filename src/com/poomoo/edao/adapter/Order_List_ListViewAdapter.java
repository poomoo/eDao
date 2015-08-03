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

/**
 * 
 * @ClassName Order_List_ListViewAdapter
 * @Description TODO 全部订单列表适配器
 * @author 李苜菲
 * @date 2015-8-3 上午10:37:42
 */
public class Order_List_ListViewAdapter extends BaseAdapter {

	private List<HashMap<String, String>> list;
	private LayoutInflater inflater;

	public Order_List_ListViewAdapter(Context context,
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
			convertView = inflater.inflate(R.layout.item_listview_order_list,
					parent,false);
			holder = new ViewHolder();
			holder.textView_order_id = (TextView) convertView
					.findViewById(R.id.order_list_item_textView_orderid);
			holder.textView_order_money = (TextView) convertView
					.findViewById(R.id.order_list_item_textView_money);
			holder.textView_order_state = (TextView) convertView
					.findViewById(R.id.order_list_item_textView_state);
			holder.textView_order_date = (TextView) convertView
					.findViewById(R.id.order_list_item_textView_date);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.order_list_item_imageView_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView_order_id.setText(list.get(position).get("id"));
		holder.textView_order_money.setText(list.get(position).get("money"));
		holder.textView_order_state.setText(list.get(position).get("state"));
		holder.textView_order_date.setText(list.get(position).get("date"));

		return convertView;
	}

	private class ViewHolder {
		private TextView textView_order_id, textView_order_money,
				textView_order_state, textView_order_date;
		private ImageView imageView;

	}

}
