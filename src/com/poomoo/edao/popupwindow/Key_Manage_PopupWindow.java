package com.poomoo.edao.popupwindow;

import com.poomoo.edao.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class Key_Manage_PopupWindow extends PopupWindow {

	private TextView textView_save, textView_dial;
	private View mMenuView;

	public Key_Manage_PopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_key_manage, null);
		textView_save = (TextView) mMenuView
				.findViewById(R.id.popup_key_manage_textView_save);
		textView_dial = (TextView) mMenuView
				.findViewById(R.id.popup_key_manage_textView_dial);

		textView_save.setOnClickListener(itemsOnClick);
		textView_dial.setOnClickListener(itemsOnClick);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height_top = mMenuView.findViewById(
						R.id.popup_key_manage_layout).getTop();
				int height_bottom = mMenuView.findViewById(
						R.id.popup_key_manage_layout).getBottom();
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
