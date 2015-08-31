package com.poomoo.edao.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.poomoo.edao.activity.KeyManageActivity;
import com.poomoo.edao.activity.LoginActivity;
import com.poomoo.edao.activity.PassWordManageActivity;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.DialogResultListener;
import com.poomoo.edao.widget.MessageBox_YESNO;

/**
 * 
 * @ClassName Fragment_Personal_Center
 * @Description TODO 个人中心
 * @author 李苜菲
 * @date 2015-8-14 上午11:02:07
 */
public class Fragment_Personal_Center extends Fragment implements
		OnClickListener {
	private TextView textView_username, textView_phonenum;
	private LinearLayout layout_credit, layout_bankaccount,
			layout_accountpassword, layout_paypassword, layout_twodimencode,
			layout_key_manage, layout_operate_manage;
	private Button button_logout;

	private MessageBox_YESNO box_YESNO;
	private SharedPreferences sharedPreferencesUserInfo = null;
	private Editor editor = null;
	private eDaoClientApplication application = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		// 实现沉浸式状态栏效果
		setImmerseLayout(getView().findViewById(
				R.id.fragment_personal_center_layout));
		application = (eDaoClientApplication) getActivity().getApplication();
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_personal_center, container,
				false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) getView().findViewById(
				R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) getView().findViewById(
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
		layout_key_manage = (LinearLayout) getView().findViewById(
				R.id.personalcenter_layout_key_manage);
		layout_operate_manage = (LinearLayout) getView().findViewById(
				R.id.personalcenter_layout_operate_manage);
		button_logout = (Button) getView().findViewById(
				R.id.personalcenter_btn_logout);

		layout_credit.setOnClickListener(this);
		layout_bankaccount.setOnClickListener(this);
		layout_accountpassword.setOnClickListener(this);
		layout_paypassword.setOnClickListener(this);
		layout_twodimencode.setOnClickListener(this);
		layout_key_manage.setOnClickListener(this);
		layout_operate_manage.setOnClickListener(this);
		button_logout.setOnClickListener(this);

		Utity.setUserAndTel(textView_username, textView_phonenum, application);
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
			startActivity(new Intent(getActivity(),
					PassWordManageActivity.class));
			break;
		case R.id.personalcenter_layout_paypassword_manage:
			break;
		case R.id.personalcenter_layout_twodimencode:
			break;
		case R.id.personalcenter_layout_key_manage:
			startActivity(new Intent(getActivity(), KeyManageActivity.class));
			break;
		case R.id.personalcenter_layout_operate_manage:
			break;
		case R.id.personalcenter_btn_logout:
			box_YESNO = new MessageBox_YESNO(getActivity());
			box_YESNO.showDialog("确定退出？", new DialogResultListener() {
				@Override
				public void onFinishDialogResult(int result) {
					// TODO 自动生成的方法存根
					if (result == 1) {
						sharedPreferencesUserInfo = getActivity()
								.getSharedPreferences("userInfo",
										Context.MODE_PRIVATE);
						editor = sharedPreferencesUserInfo.edit();
						editor.putBoolean("isLogin", false);
						editor.commit();
						startActivity(new Intent(getActivity(),
								LoginActivity.class));
						getActivity().finish();
					}

				}
			});
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
