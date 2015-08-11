package com.poomoo.edao.activity;

import android.os.Bundle;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName LoveFundActivity
 * @Description TODO 爱心基金
 * @author 李苜菲
 * @date 2015-8-11 下午5:36:39
 */
public class LoveFundActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_love_fund);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
	}

}
