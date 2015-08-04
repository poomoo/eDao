package com.poomoo.edao.activity;

import android.os.Bundle;

import com.poomoo.edao.R;
import com.poomoo.edao.widget.FlexibleRatingBar;

/**
 * 
 * @ClassName CommodityEvaluateActivity
 * @Description TODO 商品评价
 * @author 李苜菲
 * @date 2015-8-3 下午1:40:14
 */
public class CommodityEvaluateActivity extends BaseActivity {
	private FlexibleRatingBar ratingBar_shop, ratingBar_commodity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_evaluate);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		// ratingBar_shop = (FlexibleRatingBar) findViewById(R.id.commodity_e);
	}

}
