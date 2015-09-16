package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * 
 * @ClassName PubActivity
 * @Description TODO 用于展示选项
 * @author 李苜菲
 * @date 2015年9月11日 上午10:12:01
 */
public class PubActivity extends BaseActivity implements OnItemClickListener {
	private ListView listView;
	private TextView textView_title;

	private List<HashMap<String, String>> list_classify;
	private SimpleAdapter adapter_classify;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pub);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (ListView) findViewById(R.id.pub_listView);
		textView_title = (TextView) findViewById(R.id.navigation_textView_title);
		textView_title.setText("选择店铺种类");

		list_classify = new ArrayList<HashMap<String, String>>();
		int length = eDaoClientConfig.store_class.length;
		System.out.println("length:" + length);
		HashMap<String, String> item = null;
		for (int i = 0; i < length; i++) {
			item = new HashMap<String, String>();
			item.put("name", eDaoClientConfig.store_class[i]);
			item.put("id", i + 1 + "");
			list_classify.add(item);
		}
		adapter_classify = new SimpleAdapter(this, list_classify, R.layout.item_listview_pub, new String[] { "name" },
				new int[] { R.id.item_listview_pub_textView });
		listView.setAdapter(adapter_classify);
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		System.out.println("position:" + position);
		String name = list_classify.get(position).get("name");
		String id = list_classify.get(position).get("id");
		Intent intent = getIntent();
		intent.putExtra("name", name);
		intent.putExtra("id", id);
		setResult(1, intent);
		finish();
	}

}
