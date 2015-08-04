package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Shop_List_ListViewAdapter;

/**
 * 
 * @ClassName ShopListActivity
 * @Description TODO 店铺列表
 * @author 李苜菲
 * @date 2015-8-4 下午3:43:49
 */
public class ShopListActivity extends BaseActivity {
	private EditText editText_keywords;
	private ImageView imageView_back;
	private TextView textView_classify;
	private ListView listView;
	private Shop_List_ListViewAdapter adapter;
	private List<HashMap<String, String>> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shop_list);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根

		imageView_back = (ImageView) this
				.findViewById(R.id.fragment_shop_imageView_back);
		textView_classify = (TextView) this
				.findViewById(R.id.fragment_shop_textView_classify);
		editText_keywords = (EditText) this
				.findViewById(R.id.fragment_home_editText_keywords);
		listView = (ListView) this.findViewById(R.id.fragment_shop_listView);

		adapter = new Shop_List_ListViewAdapter(this, list);
		listView.setAdapter(adapter);

	}
}
