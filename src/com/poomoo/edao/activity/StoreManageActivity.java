package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.popupwindow.Select_City_PopupWindow;
import com.poomoo.edao.widget.CityPicker;

/**
 * 
 * @ClassName StoreManageActivity
 * @Description TODO 店铺管理
 * @author 李苜菲
 * @date 2015年8月31日 下午10:47:53
 */
public class StoreManageActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout layout_zone;
	private TextView textView_zone;

	private Select_City_PopupWindow select_City_PopupWindow;

	private String curProvince = "", curCity = "", curArea = "";
	private eDaoClientApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_zone = (TextView) findViewById(R.id.store_manage_textView_zone);
		layout_zone = (LinearLayout) findViewById(R.id.store_manage_layout_zone);

		layout_zone.setOnClickListener(this);
		// 取当前定位
		curProvince = application.getCurProvince();
		curCity = application.getCurCity();
		curArea = application.getCurArea();
		textView_zone.setText(curProvince+"-"+curCity+"-"+curArea);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.store_manage_layout_zone:
			select_city();
			break;
		}
	}

	private void select_city() {
		CityPicker.province_name = curProvince;
		CityPicker.city_name = curCity;
		CityPicker.area_name = curArea;
		// 实例化SelectPicPopupWindow
		select_City_PopupWindow = new Select_City_PopupWindow(
				StoreManageActivity.this, itemsOnClick);
		// 显示窗口
		select_City_PopupWindow.showAtLocation(StoreManageActivity.this
				.findViewById(R.id.activity_store_manage_layout),
				Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			if (view.getId() == R.id.popup_select_city_btn_confirm) {
				select_City_PopupWindow.dismiss();
				textView_zone.setText(CityPicker.getZone_string());
			}
		}
	};
}
