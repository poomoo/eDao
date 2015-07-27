package com.poomoo.edao.activity.common;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.edao.R;

public class MainActivity extends BaseActivity {
	private TextView textView_inform, textView_ecoin, textView_goldcoin,
			textView_point;
	private ImageView imageView_user, imageView_position;
	private GridView gridView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
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

	}

}
