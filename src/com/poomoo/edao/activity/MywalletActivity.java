package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.WindowManager;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName MywalletActivity
 * @Description TODO 我的钱包
 * @author 李苜菲
 * @date 2015-7-29 下午4:31:49
 */
public class MywalletActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_mywallet);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
	}

}
