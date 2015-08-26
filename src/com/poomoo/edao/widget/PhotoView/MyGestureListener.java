package com.poomoo.edao.widget.PhotoView;

import android.content.Context;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.poomoo.edao.R;

public class MyGestureListener implements OnGestureListener {

	private Context context;
	private ViewFlipper vf;
	private TextView textView_indicator;
	private int count = 0;

	public MyGestureListener(Context context, ViewFlipper vf,
			TextView indicator, int count) {
		this.context = context;
		this.vf = vf;
		this.textView_indicator = indicator;
		this.count = count;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		vf.setClickable(true);
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		vf.setClickable(false);
		System.out.println("滑动图片");

		if (e1.getX() - e2.getX() > 0) {
			// 设置View进入和退出的动画效果
			vf.setInAnimation(AnimationUtils.loadAnimation(context,
					R.anim.left_in));
			vf.setOutAnimation(AnimationUtils.loadAnimation(context,
					R.anim.left_out));
			vf.showNext();
			CharSequence text = context.getString(R.string.viewpager_indicator,
					vf.getDisplayedChild() + 1, count);
			textView_indicator.setText(text);
			return true;
		}
		if (e1.getX() - e2.getX() < -0) {
			this.vf.setInAnimation(AnimationUtils.loadAnimation(context,
					R.anim.right_in));
			this.vf.setOutAnimation(AnimationUtils.loadAnimation(context,
					R.anim.right_out));
			vf.showPrevious();

			CharSequence text = context.getString(R.string.viewpager_indicator,
					vf.getDisplayedChild() + 1, count);
			textView_indicator.setText(text);
			return true;
		}
		return false;

	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
