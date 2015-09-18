package com.poomoo.edao.adapter;

import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.activity.OperateManageActivity;
import com.poomoo.edao.activity.OperateManageActivity.MyListener;
import com.poomoo.edao.model.OperateManageData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OperateManage_ListViewAdapter extends BaseAdapter {

	private List<OperateManageData> list;
	private LayoutInflater inflater;
	private OperateManageActivity operateManageActivity;

	public OperateManage_ListViewAdapter(Context context, List<OperateManageData> list) {
		super();
		this.list = list;
		this.inflater = LayoutInflater.from(context);
		this.operateManageActivity = (OperateManageActivity) context;
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
		MyListener listener = operateManageActivity.new MyListener(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_listview_operate_manage, parent, false);
			holder = new ViewHolder();
			holder.textView_name = (TextView) convertView.findViewById(R.id.operate_manage_item_textView_name);
			holder.textView_tel = (TextView) convertView.findViewById(R.id.operate_manage_item_textView_tel);
			holder.textView_level = (TextView) convertView.findViewById(R.id.operate_manage_item_textView_level);
			holder.textView_date = (TextView) convertView.findViewById(R.id.operate_manage_item_textView_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView_name.setText(list.get(position).getRealName());
		holder.textView_tel.setText(list.get(position).getTel());
		holder.textView_level.setText(list.get(position).getJoinTypeName());
		holder.textView_date.setText(list.get(position).getJoinDt());

		holder.textView_tel.setOnClickListener(listener);
		return convertView;
	}

	private class ViewHolder {
		private TextView textView_name, textView_tel, textView_level, textView_date;
	}

}
