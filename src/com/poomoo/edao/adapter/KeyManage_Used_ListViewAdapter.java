package com.poomoo.edao.adapter;

import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.activity.KeyManageActivity;
import com.poomoo.edao.activity.KeyManageActivity.MyListener;
import com.poomoo.edao.model.KeyManageData;
import com.poomoo.edao.util.Utity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class KeyManage_Used_ListViewAdapter extends BaseAdapter {

	private List<KeyManageData> list;
	private LayoutInflater inflater;
	private KeyManageActivity keyManageActivity;

	public KeyManage_Used_ListViewAdapter(Context context,
			List<KeyManageData> list) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.keyManageActivity = (KeyManageActivity) context;
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
		MyListener listener = keyManageActivity.new MyListener(position);
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.item_listview_key_manage_used, parent, false);
			holder = new ViewHolder();
			holder.textView_name = (TextView) convertView
					.findViewById(R.id.key_manage_used_item_textView_name);
			holder.textView_tel = (TextView) convertView
					.findViewById(R.id.key_manage_used_item_textView_tel);
			holder.textView_date = (TextView) convertView
					.findViewById(R.id.key_manage_used_item_textView_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView_name.setText(Utity.addStarByName(list.get(position)
				.getRealName()));
		holder.textView_tel.setText(list.get(position).getTel());
		holder.textView_date.setText(list.get(position).getJoinDt());

		holder.textView_tel.setOnClickListener(listener);

		return convertView;
	}

	private class ViewHolder {
		private TextView textView_name, textView_tel, textView_date;

	}

}
