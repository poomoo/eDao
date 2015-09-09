package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName My2DimenCodeActivity
 * @Description TODO 我的二维码
 * @author 李苜菲
 * @date 2015年9月9日 下午8:13:09
 */
public class My2DimenCodeActivity extends BaseActivity implements
		OnClickListener {
	private ImageView imageView_return;
	private Button button_save;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my2dimencode);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.my2dimencode_layout));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		imageView_return = (ImageView) findViewById(R.id.my2dimencode_imageView_return);

		button_save = (Button) findViewById(R.id.my2dimencode_btn_save);

		imageView_return.setOnClickListener(this);
		button_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.my2dimencode_imageView_return:
			finish();
			break;
		case R.id.my2dimencode_btn_save:
			break;
		}
	}

}
