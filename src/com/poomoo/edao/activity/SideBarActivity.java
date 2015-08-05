package com.poomoo.edao.activity;

import android.os.Bundle;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName SideBarActivity
 * @Description TODO 侧边栏
 * @author 李苜菲
 * @date 2015-8-4 上午11:15:03
 */
public class SideBarActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sidebar);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.sidebar_layout));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
	}

}
