package com.poomoo.edao.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName CommdityInfomationActivity
 * @Description TODO 商品详情
 * @author 李苜菲
 * @date 2015-7-31 下午2:39:23
 */
public class CommodityInfomationActivity extends BaseActivity {
	private TextView textView_old_price;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commodity_information);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_old_price=(TextView)findViewById(R.id.commodity_information_textView_old_price);
		textView_old_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);  
	}

}
