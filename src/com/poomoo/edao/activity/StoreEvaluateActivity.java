package com.poomoo.edao.activity;

import com.poomoo.edao.R;

import android.os.Bundle;

/**
 * 
 * @ClassName StoreEvaluateActivity
 * @Description TODO 店铺评价
 * @author 李苜菲
 * @date 2015-8-3 下午4:02:36
 */
public class StoreEvaluateActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_evaluate);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

	}

}
