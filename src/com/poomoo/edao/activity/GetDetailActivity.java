package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Get_DetailAdapter;

import android.os.Bundle;
import android.widget.ListView;

/**
 * 
 * @ClassName GetDetailActivity
 * @Description TODO 领取明细
 * @author 李苜菲
 * @date 2015-7-29 下午3:36:01
 */
public class GetDetailActivity extends BaseActivity {
	private ListView listView;
	private Get_DetailAdapter adapter;
	private List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_get_detail);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (ListView) findViewById(R.id.get_detail_listView);
//		adapter = new Get_DetailAdapter(this, list);
		listView.setAdapter(adapter);
	}

}
