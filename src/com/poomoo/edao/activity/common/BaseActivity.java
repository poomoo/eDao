package com.poomoo.edao.activity.common;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
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
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Window window = getWindow();
		window.requestFeature(Window.FEATURE_NO_TITLE);

	}

}
