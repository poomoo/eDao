package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Rebate_ListViewAdapter;

/**
 * 
 * @ClassName RebateActivity
 * @Description TODO 全国返利
 * @author 李苜菲
 * @date 2015-7-29 下午2:14:43
 */
public class RebateActivity extends BaseActivity {
	private ListView listView;
	private Rebate_ListViewAdapter adapter;
	private List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rebate);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (ListView) findViewById(R.id.rebate_listView);
		adapter = new Rebate_ListViewAdapter(this, list);
		listView.setAdapter(adapter);
	}

}
