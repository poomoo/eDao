package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.poomoo.edao.R;

public class UploadPicsActivity extends BaseActivity implements OnClickListener {
	private Button button_upload;
	private FrameLayout frameLayout_identitycard_front,
			frameLayout_identitycard_back, frameLayout_identitycard_inhand,
			frameLayout_business_license;
	private ImageView imageView_identitycard_front,
			imageView_identitycard_back, imageView_identitycard_inhand,
			imageView_business_license;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadpics);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		frameLayout_identitycard_front = (FrameLayout) findViewById(R.id.uploadpics_layout_identitycard_front);
		frameLayout_identitycard_back = (FrameLayout) findViewById(R.id.uploadpics_layout_identitycard_back);
		frameLayout_identitycard_inhand = (FrameLayout) findViewById(R.id.uploadpics_layout_identitycard_inhand);
		frameLayout_business_license = (FrameLayout) findViewById(R.id.uploadpics_layout_businesss_license);

		imageView_identitycard_front = (ImageView) findViewById(R.id.uploadpics_imageView_identitycard_front);
		imageView_identitycard_back = (ImageView) findViewById(R.id.uploadpics_imageView_identitycard_back);
		imageView_identitycard_inhand = (ImageView) findViewById(R.id.uploadpics_imageView_identitycard_inhand);
		imageView_business_license = (ImageView) findViewById(R.id.uploadpics_imageView_businesss_license);

		button_upload = (Button) findViewById(R.id.uploadpics_btn_upload);

		frameLayout_identitycard_front.setOnClickListener(this);
		frameLayout_identitycard_back.setOnClickListener(this);
		frameLayout_identitycard_inhand.setOnClickListener(this);
		frameLayout_business_license.setOnClickListener(this);
		button_upload.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.uploadpics_layout_identitycard_front:
			break;
		case R.id.uploadpics_layout_identitycard_back:
			break;
		case R.id.uploadpics_layout_identitycard_inhand:
			break;
		case R.id.uploadpics_layout_businesss_license:
			break;
		case R.id.uploadpics_btn_upload:
			break;
		}

	}

}
