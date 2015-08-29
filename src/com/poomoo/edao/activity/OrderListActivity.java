package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SimpleAdapter;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Fragment_Home_GridViewAdapter;
import com.poomoo.edao.adapter.Order_List_ListViewAdapter;
import com.poomoo.edao.adapter.Pub_GridViewAdapter;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName OrderListActivity
 * @Description TODO 全部订单列表
 * @author 李苜菲
 * @date 2015-8-3 上午10:35:50
 */
public class OrderListActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, OnCheckedChangeListener {
	private RadioButton button_status, button_classify, button_date,
			button_unpay, button_payed, button_delete, button_transfer,
			button_recharge, button_buy_key, button_apply,
			button_buy_commodity, button_handing;
	private RadioGroup group_status, group1, group2;
	private LinearLayout layout_classify;
	private GridView gridView;
	private ListView listView;

	private Order_List_ListViewAdapter adapter;
	private SimpleAdapter simpleAdapter;
	private List<HashMap<String, String>> list;
	private final static String[] items = new String[] { "意币转账", "充值", "购买秘钥",
			"申请加盟", "购买商品", "提现申请" };

	private Pub_GridViewAdapter gridViewAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (ListView) findViewById(R.id.order_list_listView);
		group_status = (RadioGroup) findViewById(R.id.order_list_radioGroup_status);
		/*
		 * layout_classify = (LinearLayout)
		 * findViewById(R.id.order_list_layout_classify); group1 = (RadioGroup)
		 * findViewById(R.id.order_list_radioGroup_classify_1); group2 =
		 * (RadioGroup) findViewById(R.id.order_list_radioGroup_classify_2);
		 */
		gridView = (GridView) findViewById(R.id.order_list_gridView_classify);

		button_status = (RadioButton) findViewById(R.id.order_list_radioButton_status);
		button_classify = (RadioButton) findViewById(R.id.order_list_radioButton_classify);
		button_date = (RadioButton) findViewById(R.id.order_list_radioButton_date);
		button_unpay = (RadioButton) findViewById(R.id.order_list_radioButton_nopay);
		button_payed = (RadioButton) findViewById(R.id.order_list_radioButton_payed);
		button_delete = (RadioButton) findViewById(R.id.order_list_radioButton_delete);
		button_transfer = (RadioButton) findViewById(R.id.order_list_radioButton_ecoin_transfer);
		button_recharge = (RadioButton) findViewById(R.id.order_list_radioButton_recharge);
		button_buy_key = (RadioButton) findViewById(R.id.order_list_radioButton_buy_key);
		button_apply = (RadioButton) findViewById(R.id.order_list_radioButton_apply);
		button_buy_commodity = (RadioButton) findViewById(R.id.order_list_radioButton_buy_commodity);
		button_handing = (RadioButton) findViewById(R.id.order_list_radioButton_handing);

		button_status.setOnClickListener(this);
		button_classify.setOnClickListener(this);
		button_date.setOnClickListener(this);
		button_unpay.setOnClickListener(this);
		button_payed.setOnClickListener(this);
		button_delete.setOnClickListener(this);
		button_transfer.setOnClickListener(this);
		button_recharge.setOnClickListener(this);
		button_buy_key.setOnClickListener(this);
		button_apply.setOnClickListener(this);
		button_buy_commodity.setOnClickListener(this);
		button_handing.setOnClickListener(this);

		// buttons = new ArrayList<RadioButton>();
		// buttons.add(button_transfer);
		// buttons.add(button_recharge);
		// buttons.add(button_buy_key);
		// buttons.add(button_apply);
		// buttons.add(button_buy_commodity);
		// buttons.add(button_handing);

		list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap;
		for (int i = 0; i < 10; i++) {
			hashMap = new HashMap<String, String>();
			hashMap.put("id", "1234567890" + i);
			hashMap.put("money", "￥" + (100 + i + ".00"));
			hashMap.put("state", "已支付");
			hashMap.put("date", "2015-06-06");
			list.add(hashMap);
		}
		adapter = new Order_List_ListViewAdapter(this, list);
		listView.setAdapter(adapter);

		list = new ArrayList<HashMap<String, String>>();
		for (String string : items) {
			hashMap = new HashMap<String, String>();
			hashMap.put("name", string);
			list.add(hashMap);
		}
		// simpleAdapter = new SimpleAdapter(this, list,
		// R.layout.item_gridview_order_list, new String[] { "name" },
		// new int[] { R.id.item_order_list_textView });
		gridViewAdapter = new Pub_GridViewAdapter(this, list);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);

		// group1.setOnCheckedChangeListener(this);
		// group2.setOnCheckedChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.order_list_radioButton_status:
			group_status.setVisibility(View.VISIBLE);
			// layout_classify.setVisibility(View.GONE);
			gridView.setVisibility(View.GONE);
			break;
		case R.id.order_list_radioButton_classify:
			group_status.setVisibility(View.GONE);
			// layout_classify.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.VISIBLE);
			break;
		case R.id.order_list_radioButton_date:
			break;
		case R.id.order_list_radioButton_nopay:
			break;
		case R.id.order_list_radioButton_payed:
			break;
		case R.id.order_list_radioButton_delete:
			break;
		case R.id.order_list_radioButton_ecoin_transfer:
			// setChecked(0);
			break;
		case R.id.order_list_radioButton_recharge:
			// setChecked(1);
			break;
		case R.id.order_list_radioButton_buy_key:
			// setChecked(2);
			break;
		case R.id.order_list_radioButton_apply:
			// setChecked(3);
			break;
		case R.id.order_list_radioButton_buy_commodity:
			// setChecked(4);
			break;
		case R.id.order_list_radioButton_handing:
			// setChecked(5);
			break;

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO 自动生成的方法存根
		gridViewAdapter.setTextColor();
		System.out.println("arg2:" + position + ":"
				+ Pub_GridViewAdapter.textViews.get(position));
		Pub_GridViewAdapter.textViews.get(position).setTextColor(
				Color.parseColor("#1995EB"));
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO 自动生成的方法存根
		System.out.println("group1:" + R.id.order_list_radioGroup_classify_1);
		System.out.println("group2:" + R.id.order_list_radioGroup_classify_2);
		System.out.println("group:" + group.getId() + "checkedId:" + checkedId);
		int curPosition = Utity.getLastNum(checkedId)
				- Utity.getLastNum(group.getId()) - 1;
		System.out.println("plus:" + curPosition);
		// group.check(curPosition);
		// setChecked(checkedId);
		System.out.println("isSelected:"
				+ ((RadioButton) group.getChildAt(curPosition)).isChecked());
		if (group.getId() == R.id.order_list_radioGroup_classify_1
				&& ((RadioButton) group.getChildAt(curPosition)).isChecked()) {
			// group2.clearCheck();
			// button_apply.setChecked(false);
			// button_buy_commodity.setChecked(false);
			// button_handing.setChecked(false);
		}
		if (group.getId() == R.id.order_list_radioGroup_classify_2
				&& ((RadioButton) group.getChildAt(curPosition)).isChecked()) {
			// group1.clearCheck();
			// button_transfer.setChecked(false);
			// button_recharge.setChecked(false);
			// button_buy_key.setChecked(false);
		}
	}

	// private void setChecked(int curId) {
	// int i = 0;
	// for (RadioButton button : buttons) {
	// if (curId == i)
	// button.setChecked(true);
	// else
	// button.setChecked(false);
	// i++;
	// }
	// }
}
