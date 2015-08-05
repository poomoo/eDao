package com.poomoo.edao.activity;

import android.os.Bundle;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName StoreInformationActivity
 * @Description TODO 商铺详情
 * @author 李苜菲
 * @date 2015年8月3日 下午9:38:57
 */
public class StoreInformationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_information);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

	}

}
