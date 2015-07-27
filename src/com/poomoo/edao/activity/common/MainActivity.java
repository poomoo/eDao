package com.poomoo.edao.activity.common;

import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Main_GridViewAdapter;

public class MainActivity extends BaseActivity {
	private TextView textView_inform, textView_ecoin, textView_goldcoin,
			textView_point;
	private ImageView imageView_user, imageView_position;
	private GridView gridView;

	private Main_GridViewAdapter gridViewAdapter;
	private final String[] list_name = { "全国返利", "消费领取", "我的钱包", "交易明细",
			"信用管理", "转账支付", "合作加盟", "爱心基金", "意道商城" };
	private final int[] list_image = { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher };
	private int height = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_inform = (TextView) findViewById(R.id.main_textView_inform);
		textView_ecoin = (TextView) findViewById(R.id.main_textView_ecoin);
		textView_goldcoin = (TextView) findViewById(R.id.main_textView_goldcoin);
		textView_point = (TextView) findViewById(R.id.main_textView_point);
		imageView_user = (ImageView) findViewById(R.id.main_imageView_user);
		imageView_position = (ImageView) findViewById(R.id.main_imageView_position);
		gridView = (GridView) findViewById(R.id.main_gridVeiw);


		height = gridView.getMeasuredHeight();
		gridViewAdapter = new Main_GridViewAdapter(this, list_name, list_image,
				gridView);
		gridView.setAdapter(gridViewAdapter);

		setGridViewHeightBasedOnChildren(gridView);
		gridViewAdapter.notifyDataSetChanged();

	}

	private void setGridViewHeightBasedOnChildren(GridView gridView) {
		// 获取listview的adapter
		if (gridViewAdapter == null) {
			return;
		}
		// 固定列宽，有多少列
		int col = 3;// listView.getNumColumns();
		int totalHeight = 0;
		// i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
		// listAdapter.getCount()小于等于8时计算两次高度相加
		for (int i = 0; i < gridViewAdapter.getCount(); i += col) {
			// 获取listview的每一个item
			View listItem = gridViewAdapter.getView(i, null, gridView);
			listItem.measure(0, 0);
			// 获取item的高度和
			totalHeight += listItem.getMeasuredHeight();
			System.out.println("totalHeight:" + totalHeight);
		}

		// 获取listview的布局参数
		ViewGroup.LayoutParams params = gridView.getLayoutParams();
		// 设置高度
		params.height = totalHeight;
		// 设置margin
		// ((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		// 设置参数
		gridView.setLayoutParams(params);
	}

}
