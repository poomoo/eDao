package com.poomoo.edao.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 * @ClassName BaseActivity
 * @Description TODO 基础activity
 * @author 李苜菲
 * @date 2015-7-27 上午9:31:15
 */
public class BaseActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 
	 * 
	 * @Title: openActivity
	 * @Description: TODO 跳转Activity 并含有Bundle数据
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-14上午10:38:18
	 */
	protected void openActivity(Class<?> pClass, Bundle pBundle) {
		Intent intent = new Intent(this, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		startActivity(intent);
	}

	/**
	 * 
	 * 
	 * @Title: openActivity
	 * @Description: TODO 跳转Activity
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-14上午10:39:33
	 */
	protected void openActivity(Class<?> pClass) {
		Intent intent = new Intent(this, pClass);
		startActivity(intent);
	}

	/**
	 * 
	 * 
	 * @Title: openActivityForResult
	 * @Description: TODO
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015年8月24日下午11:28:46
	 */
	protected void openActivityForResult(Class<?> pClass, int requestCode) {
		Intent intent = new Intent(this, pClass);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * 
	 * 
	 * @Title: setImmerseLayout
	 * @Description: TODO 实现沉浸式状态栏效果
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-5上午11:09:52
	 */
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}

	protected void setImmerseLayout() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}

	/**
	 * 
	 * 
	 * @Title: getStatusBarHeight
	 * @Description: TODO 用于获取状态栏的高度。 使用Resource对象获取
	 * @author 李苜菲
	 * @return
	 * @return int 返回状态栏高度的像素值
	 * @throws
	 * @date 2015-8-5上午11:10:00
	 */
	protected int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

}
