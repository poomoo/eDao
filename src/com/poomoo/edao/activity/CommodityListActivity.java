package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.CommodityListSpinnerAdapter;

/**
 * 
 * @ClassName CommodityListActivity
 * @Description TODO 商品列表
 * @author 李苜菲
 * @date 2015-7-28 下午4:00:47
 */
public class CommodityListActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout layout_zonghe, layout_sales, layout_prices,
			layout_sort;
	private ListView listView_commodity, listView_sort;
	private PopupWindow popupWindow;
	private LinearLayout layout;
	private CommodityListSpinnerAdapter adapter_sale, adapter_price;
	private List<HashMap<String, String>> list_sale, list_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_list);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		layout_zonghe = (LinearLayout) findViewById(R.id.commoditylist_layout_zonghe);
		layout_sales = (LinearLayout) findViewById(R.id.commoditylist_layout_sales);
		layout_prices = (LinearLayout) findViewById(R.id.commoditylist_layout_prices);
		layout_sort = (LinearLayout) findViewById(R.id.commoditylist_layout_sort);
		listView_commodity = (ListView) findViewById(R.id.commoditylist_listView);

		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.dropdown_commoditylistspinner, null);

		layout_zonghe.setOnClickListener(this);
		layout_sales.setOnClickListener(this);
		layout_prices.setOnClickListener(this);

		list_sale = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap_sale = new HashMap<String, String>();
		hashMap_sale.put("name", "销量从低到高");
		hashMap_sale.put("value", "asc");
		list_sale.add(hashMap_sale);
		hashMap_sale = new HashMap<String, String>();
		hashMap_sale.put("name", "销量从高到低");
		hashMap_sale.put("value", "desc");
		list_sale.add(hashMap_sale);
		adapter_sale = new CommodityListSpinnerAdapter(this, list_sale, -1);

		list_price = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap_price = new HashMap<String, String>();
		hashMap_price.put("name", "价格从低到高");
		hashMap_price.put("value", "asc");
		list_price.add(hashMap_sale);
		hashMap_price = new HashMap<String, String>();
		hashMap_price.put("name", "价格从高到低");
		hashMap_price.put("value", "desc");
		list_price.add(hashMap_price);
		adapter_price = new CommodityListSpinnerAdapter(this, list_price, -1);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.commoditylist_layout_zonghe:
			break;
		case R.id.commoditylist_layout_sales:
			showWindow(layout_sort, listView_sort, list_sale, adapter_sale);
			break;
		case R.id.commoditylist_layout_prices:
			showWindow(layout_sort, listView_sort, list_price, adapter_price);
			break;
		}

	}

	public void showWindow(View spinnerlayout, ListView listView,
			final List<HashMap<String, String>> list,
			final CommodityListSpinnerAdapter adapter) {
		listView = (ListView) layout
				.findViewById(R.id.commoditylistspinner_dropdown_listView);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(spinnerlayout);
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(spinnerlayout.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(spinnerlayout, 0, 0);
		// 弹出动画
		// popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// spinnerlayout
				// .setBackgroundResource(R.drawable.preference_single_item);
			}

		});
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				adapter_sale.refresh(-1);
				adapter_price.refresh(-1);
				adapter.refresh(arg2);
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

}
