package com.poomoo.edao.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName Fragment_Personal_Center
 * @Description TODO 个人中心
 * @author 李苜菲
 * @date 2015-8-14 上午11:02:07
 */
public class Fragment_Personal_Center extends Fragment implements
		OnClickListener {
	private TextView textView_name, textView_tel;
	private LinearLayout layout_credit, layout_bankaccount,
			layout_accountpassword, layout_paypassword, layout_twodimencode;
	private Button button_logout;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_personal_center, container,
				false);
	}

	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		// 实现沉浸式状态栏效果
		setImmerseLayout(getView().findViewById(R.id.personalcenter_layout));

		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_name = (TextView) getView().findViewById(
				R.id.layout_userinfo_textView_username);
		textView_tel = (TextView) getView().findViewById(
				R.id.layout_userinfo_textView_tel);
		layout_credit = (LinearLayout) getView().findViewById(
				R.id.personalcenter_layout_credit_manage);
		layout_bankaccount = (LinearLayout) getView().findViewById(
				R.id.personalcenter_layout_bankaccount_manage);
		layout_accountpassword = (LinearLayout) getView().findViewById(
				R.id.personalcenter_layout_accountpassword_manage);
		layout_paypassword = (LinearLayout) getView().findViewById(
				R.id.personalcenter_layout_paypassword_manage);
		layout_twodimencode = (LinearLayout) getView().findViewById(
				R.id.personalcenter_layout_twodimencode);
		button_logout = (Button) getView().findViewById(
				R.id.personalcenter_btn_logout);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.personalcenter_layout_credit_manage:
			break;
		case R.id.personalcenter_layout_bankaccount_manage:
			break;
		case R.id.personalcenter_layout_accountpassword_manage:
			break;
		case R.id.personalcenter_layout_paypassword_manage:
			break;
		case R.id.personalcenter_layout_twodimencode:
			break;
		case R.id.personalcenter_btn_logout:
			break;
		}

	}

	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getActivity().getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			int statusBarHeight = getStatusBarHeight(getActivity()
					.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}

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
