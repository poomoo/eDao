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
import android.widget.ImageView;
import android.widget.PopupWindow;

public class Upload_Pics_Example_PopupWindow extends PopupWindow {
	private ImageView imageView;
	private View mMenuView;

	public Upload_Pics_Example_PopupWindow(Activity context, int res, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_uploadpics_example, null);
		imageView = (ImageView) mMenuView.findViewById(R.id.popup_loadpics_imageView);
		imageView.setImageResource(res);

		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.MATCH_PARENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height_top = mMenuView.findViewById(R.id.popup_loadpics_imageView).getTop();
				int height_bottom = mMenuView.findViewById(R.id.popup_loadpics_imageView).getBottom();
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
