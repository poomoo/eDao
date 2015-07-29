package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Get_DetailAdapter;

/**
 * 
 * @ClassName GetDetailActivity
 * @Description TODO 领取明细
 * @author 李苜菲
 * @date 2015-7-29 下午3:36:01
 */
public class GetDetailActivity extends Activity {
	private ListView listView;
	private Get_DetailAdapter adapter;
	private List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_detail);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (ListView) findViewById(R.id.get_detail_listView);
		adapter = new Get_DetailAdapter(this, list);
		listView.setAdapter(adapter);
	}

}
