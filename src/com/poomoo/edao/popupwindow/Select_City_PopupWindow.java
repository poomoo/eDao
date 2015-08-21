package com.poomoo.edao.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.poomoo.edao.R;

public class Select_City_PopupWindow extends PopupWindow {

//	private CityPicker cityPicker;
	private Button button;
	private View mMenuView;

	public Select_City_PopupWindow(Activity context,
			OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_select_city, null);
//		cityPicker = (CityPicker) mMenuView
//				.findViewById(R.id.popup_select_city_picker);
		button = (Button) mMenuView
				.findViewById(R.id.popup_select_city_btn_confirm);

		button.setOnClickListener(itemsOnClick);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height_top = mMenuView.findViewById(
						R.id.popup_select_city_layout).getTop();
				int height_bottom = mMenuView.findViewById(
						R.id.popup_select_city_layout).getBottom();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height_top || y > height_bottom) {
						dismiss();
					}
				}
				return true;
			}
		});
	}
}
