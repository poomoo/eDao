package com.poomoo.edao.activity.common;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Main_GridViewAdapter;

public class MainActivity extends BaseActivity {
	private TextView textView_inform, textView_ecoin, textView_goldcoin,
			textView_point;
	private ImageView imageView_user, imageView_position;
	private GridView gridView;
	private Main_GridViewAdapter gridViewAdapter;
	private final String[] list_name = { "全国返利", "", "消费领取", "我的钱包", "交易明细",
			"信用管理", "转账支付", "合作加盟", "爱心基金", "意道商城" };
	private final int[] list_image = { R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher };

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

		gridViewAdapter = new Main_GridViewAdapter(this, list_name, list_image);
//		gridView.setAdapter(gridViewAdapter);

	}
}
