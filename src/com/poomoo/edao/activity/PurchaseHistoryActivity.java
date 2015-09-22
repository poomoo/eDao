package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.List;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Purchase_History_ListViewAdapter;

import android.os.Bundle;
import android.widget.ListView;

/**
 * 
 * @ClassName PurchaseHistoryActivity
 * @Description TODO 消费记录
 * @author 李苜菲
 * @date 2015-7-29 下午2:32:43
 */
public class PurchaseHistoryActivity extends BaseActivity {
	private ListView listView;
	private Purchase_History_ListViewAdapter adapter;
	private List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_purchase_history);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
	}

}
