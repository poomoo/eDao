package com.poomoo.edao.navigation;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.edao.R;

@SuppressLint("NewApi")
public class ReturnNavigation extends Fragment {

	ImageView imageView_return;
	TextView textView_title;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.navigation_return, container, false);

	}

	public void init() {
		imageView_return = (ImageView) getView().findViewById(
				R.id.navigation_imageView_return);
		imageView_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		textView_title = (TextView) getView().findViewById(
				R.id.navigation_textView_title);
		setNavigationTitle();
	}

	public void setNavigationTitle() {
		String[] thisactivity = getActivity().toString().split("@");

		if (thisactivity[0]
				.equals("com.poomoo.edao.activity.common.PassWordManageActivity")) {
			textView_title.setText("密码管理");
		}
		if (thisactivity[0]
				.equals("com.poomoo.edao.activity.common.RegistrationActivity")) {
			textView_title.setText("注册");
		}
		if (thisactivity[0]
				.equals("com.poomoo.edao.activity.common.UploadPicsActivity")) {
			textView_title.setText("上传照片");
		}

	}

}
