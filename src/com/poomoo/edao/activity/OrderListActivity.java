package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Order_List_ListViewAdapter;

/**
 * 
 * @ClassName OrderListActivity
 * @Description TODO 全部订单列表
 * @author 李苜菲
 * @date 2015-8-3 上午10:35:50
 */
public class OrderListActivity extends BaseActivity {
	private ListView listView;
	private Order_List_ListViewAdapter adapter;
	private List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (ListView) findViewById(R.id.order_list_listView);

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
	}

}
