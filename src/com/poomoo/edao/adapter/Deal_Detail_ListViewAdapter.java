package com.poomoo.edao.adapter;

import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.model.OrderListData;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName Order_List_ListViewAdapter
 * @Description TODO 全部订单列表适配器
 * @author 李苜菲
 * @date 2015-8-3 上午10:37:42
 */
public class Deal_Detail_ListViewAdapter extends BaseAdapter {

	private List<OrderListData> list;
	private LayoutInflater inflater;
	private Activity activity;

	public Deal_Detail_ListViewAdapter(Activity activity, List<OrderListData> list) {
		super();
		this.activity = activity;
		this.list = list;
		this.inflater = LayoutInflater.from(activity);
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
			convertView = inflater.inflate(R.layout.item_listview_order_list, parent, false);
			holder = new ViewHolder();
			holder.textView_order_id = (TextView) convertView.findViewById(R.id.order_list_item_textView_orderid);
			holder.textView_order_money = (TextView) convertView.findViewById(R.id.order_list_item_textView_money);
			holder.textView_order_state = (TextView) convertView.findViewById(R.id.order_list_item_textView_state);
			holder.textView_order_date = (TextView) convertView.findViewById(R.id.order_list_item_textView_date);
			holder.textView_order_remark = (TextView) convertView.findViewById(R.id.order_list_item_textView_remark);
			holder.textView_order_handing_fee = (TextView) convertView
					.findViewById(R.id.order_list_item_textView_handing_fee);
			holder.layout_handing_fee = (LinearLayout) convertView
					.findViewById(R.id.order_list_item_layout_handing_fee);
			holder.button_pay = (Button) convertView.findViewById(R.id.order_list_item_button_pay);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textView_order_id.setText(list.get(position).getOrdersId());
		holder.textView_order_money.setText(list.get(position).getPayFee());
		holder.textView_order_state.setText(list.get(position).getStatus());
		holder.textView_order_remark.setText(list.get(position).getRemark());

		if (!TextUtils.isEmpty(list.get(position).getHandlingFee())) {
			holder.layout_handing_fee.setVisibility(View.VISIBLE);
			holder.textView_order_handing_fee.setText(list.get(position).getHandlingFee());
		}

		holder.textView_order_date.setText(list.get(position).getOrdersDt());

		return convertView;
	}

	private class ViewHolder {
		private TextView textView_order_id, textView_order_money, textView_order_state, textView_order_date,
				textView_order_remark, textView_order_handing_fee;
		private Button button_pay;
		private LinearLayout layout_handing_fee;
	}

}
