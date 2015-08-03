package com.poomoo.edao.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName EvaluationListActivity
 * @Description TODO 评价列表
 * @author 李苜菲
 * @date 2015年8月3日 下午9:59:05
 */
public class EvaluationListActivity extends BaseActivity {
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluation_list);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (ListView) findViewById(R.id.evaluation_listView);
	}

}
