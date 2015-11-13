package com.poomoo.edao.adapter;

import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.activity.MyOrderActivity;
import com.poomoo.edao.activity.MyOrderActivity.MyListener;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.model.OrderListData;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @ClassName MyOrder_ListViewAdapter
 * @Description TODO 我的订单
 * @author 李苜菲
 * @date 2015年9月15日 下午4:00:52
 */
public class MyOrder_ListViewAdapter extends BaseAdapter {

	private List<OrderListData> list;
	private LayoutInflater inflater;
	private MyOrderActivity myOrderActivity;
	private eDaoClientApplication application = null;
	public static String type = "1";// 交易类型 1-待处理 2-已完成
	public static Button currentBtn;

	public MyOrder_ListViewAdapter(MyOrderActivity activity, List<OrderListData> list) {
		super();
		this.myOrderActivity = activity;
		this.list = list;
		this.inflater = LayoutInflater.from(activity);
		this.application = (eDaoClientApplication) activity.getApplication();
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
			holder.textView_lable = (TextView) convertView.findViewById(R.id.order_list_item_textView_store_name_lable);
			holder.textView_order_id = (TextView) convertView.findViewById(R.id.order_list_item_textView_orderid);
			holder.textView_shop_name = (TextView) convertView.findViewById(R.id.order_list_item_textView_store_name);
			holder.textView_order_money = (TextView) convertView.findViewById(R.id.order_list_item_textView_money);
			holder.textView_order_state = (TextView) convertView.findViewById(R.id.order_list_item_textView_state);
			holder.textView_order_date = (TextView) convertView.findViewById(R.id.order_list_item_textView_date);
			holder.textView_order_remark = (TextView) convertView.findViewById(R.id.order_list_item_textView_remark);
			holder.button_pay = (Button) convertView.findViewById(R.id.order_list_item_button_pay);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MyListener listener = myOrderActivity.new MyListener(position, holder.button_pay);

		holder.textView_order_id.setText(list.get(position).getOrdersId());
		holder.textView_shop_name.setText(list.get(position).getShopName());
		holder.textView_order_money.setText(list.get(position).getPayFee());
		holder.textView_order_state.setText(list.get(position).getStatus());
		holder.textView_order_date.setText(list.get(position).getOrdersDt());
		holder.textView_order_remark.setText(list.get(position).getRemark());

		// 待处理
		if (type.equals("1")) {
			// 客户角色
			if (list.get(position).getMyPay().equals("true")) {
				holder.textView_lable.setText("商户名称:　");
				holder.button_pay.setVisibility(View.INVISIBLE);
				System.out.println("商户名称:" + list.get(position).getShopName() + "position:" + position);
			}
			// 商户角色
			else {
				holder.textView_lable.setText("客户姓名:　");
				holder.button_pay.setVisibility(View.VISIBLE);
				holder.button_pay.setText("确认收款");
				holder.button_pay.setOnClickListener(listener);
				holder.button_pay.setTag("confirm");
				System.out.println("客户名称:" + list.get(position).getShopName()+ "position:" + position);
			}
		}
		// 已完成
		if (type.equals("2")) {
			// 客户角色
			if (list.get(position).getMyPay().equals("true")) {
				holder.textView_lable.setText("商户名称:　");
				if (list.get(position).getIsAppraise().equals("0")) {
					holder.button_pay.setVisibility(View.VISIBLE);
					holder.button_pay.setText("去评价");
					holder.button_pay.setBackgroundResource(R.drawable.style_btn_yes_background);
					holder.button_pay.setOnClickListener(listener);
					holder.button_pay.setTag("evaluate");
				} else {
					holder.button_pay.setVisibility(View.VISIBLE);
					holder.button_pay.setText("已评价");
					holder.button_pay.setClickable(false);
					holder.button_pay.setBackgroundResource(R.drawable.style_btn_no_background);
				}
			}
			// 商户角色
			else {
				holder.textView_lable.setText("客户姓名:　");
				holder.button_pay.setVisibility(View.INVISIBLE);
			}
		}
		System.out.println("getview完成"+ "position:" + position);

		return convertView;
	}

	private class ViewHolder {
		private TextView textView_lable, textView_order_id, textView_shop_name, textView_order_money,
				textView_order_state, textView_order_date, textView_order_remark;
		private Button button_pay;
	}

}
