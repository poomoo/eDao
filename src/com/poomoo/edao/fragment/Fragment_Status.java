package com.poomoo.edao.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.poomoo.edao.R;

public class Fragment_Status extends Fragment implements OnClickListener {
	private ProgressDialog progressDialog = null;
	private RadioButton button_unpay, button_payed, button_delete;

	private Fragment curFragment;
	private Fragment_Payed fragment_Payed;
	private Fragment_UnPayed fragment_UnPayed;
	private Fragment_Deleted fragment_Deleted;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		setDefaultFragment();
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_status, container, false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		button_payed = (RadioButton) getView().findViewById(
				R.id.fragment_status_radioButton_payed);
		button_unpay = (RadioButton) getView().findViewById(
				R.id.fragment_status_radioButton_nopay);
		button_delete = (RadioButton) getView().findViewById(
				R.id.fragment_status_radioButton_delete);
		button_unpay.setOnClickListener(this);
		button_payed.setOnClickListener(this);
		button_delete.setOnClickListener(this);
	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragment_Payed = new Fragment_Payed();
		fragmentTransaction.add(R.id.fragment_status_layout, fragment_Payed);
		curFragment = fragment_Payed;
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.fragment_status_radioButton_payed:
			if (fragment_Payed == null)
				fragment_Payed = new Fragment_Payed();
			switchFragment(fragment_Payed);
			curFragment = fragment_Payed;
			break;
		case R.id.fragment_status_radioButton_nopay:
			if (fragment_UnPayed == null)
				fragment_UnPayed = new Fragment_UnPayed();
			switchFragment(fragment_UnPayed);
			curFragment = fragment_UnPayed;
			break;
		case R.id.fragment_status_radioButton_delete:
			if (fragment_Deleted == null)
				fragment_Deleted = new Fragment_Deleted();
			switchFragment(fragment_Deleted);
			curFragment = fragment_Deleted;
			break;
		}
	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(
					R.id.fragment_status_layout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}

}
