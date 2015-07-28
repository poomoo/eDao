package com.poomoo.edao.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName CommodityListActivity
 * @Description TODO 商品列表
 * @author 李苜菲
 * @date 2015-7-28 下午4:00:47
 */
public class CommodityListActivity extends BaseActivity {
	private LinearLayout layout_zonghe, layout_sales, layout_prices;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commoditylist);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		layout_zonghe = (LinearLayout) findViewById(R.id.commoditylist_layout_zonghe);
		layout_sales = (LinearLayout) findViewById(R.id.commoditylist_layout_sales);
		layout_prices = (LinearLayout) findViewById(R.id.commoditylist_layout_prices);
		listView = (ListView) findViewById(R.id.commoditylist_listView);
	}

}
