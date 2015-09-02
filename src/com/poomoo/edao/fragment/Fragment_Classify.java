package com.poomoo.edao.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Pub_GridViewAdapter;

public class Fragment_Classify extends Fragment implements OnItemClickListener {
	private Pub_GridViewAdapter gridViewAdapter;
	private GridView gridView;
	private List<HashMap<String, String>> menu_list;
	private final static String[] items = new String[] { "意币转账", "充值", "购买秘钥",
			"申请加盟", "购买商品", "提现申请" };
	private Fragment curFragment;
	private Fragment_Transfer fragment_Transfer;
	private Fragment_Recharge fragment_Recharge;
	private Fragment_Buy_Key fragment_Buy_Key;
	private Fragment_Apply fragment_Apply;
	private Fragment_Buy_Goods fragment_Buy_Goods;
	private Fragment_Handing fragment_Handing;

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
		return inflater.inflate(R.layout.fragment_classify, container, false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		gridView = (GridView) getView().findViewById(
				R.id.fragment_classify_gridView);
		menu_list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap;
		for (String string : items) {
			hashMap = new HashMap<String, String>();
			hashMap.put("name", string);
			menu_list.add(hashMap);
		}
		gridViewAdapter = new Pub_GridViewAdapter(getActivity(), menu_list);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);
	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragment_Transfer = new Fragment_Transfer();
		fragmentTransaction.add(R.id.fragment_status_layout, fragment_Transfer);
		curFragment = fragment_Transfer;
		fragmentTransaction.commit();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO 自动生成的方法存根
		gridViewAdapter.setTextColor();
		Pub_GridViewAdapter.textViews.get(position).setTextColor(
				Color.parseColor("#1995EB"));
		switch (position + 1) {
		case 1:
			if (fragment_Transfer == null)
				fragment_Transfer = new Fragment_Transfer();
			switchFragment(fragment_Transfer);
			curFragment = fragment_Transfer;
			break;
		case 2:
			if (fragment_Recharge == null)
				fragment_Recharge = new Fragment_Recharge();
			switchFragment(fragment_Recharge);
			curFragment = fragment_Recharge;
			break;
		case 3:
			if (fragment_Buy_Key == null)
				fragment_Buy_Key = new Fragment_Buy_Key();
			switchFragment(fragment_Buy_Key);
			curFragment = fragment_Buy_Key;
			break;
		case 4:
			if (fragment_Apply == null)
				fragment_Apply = new Fragment_Apply();
			switchFragment(fragment_Apply);
			curFragment = fragment_Apply;
			break;
		case 5:
			if (fragment_Buy_Goods == null)
				fragment_Buy_Goods = new Fragment_Buy_Goods();
			switchFragment(fragment_Buy_Goods);
			curFragment = fragment_Buy_Goods;
			break;
		case 6:
			if (fragment_Handing == null)
				fragment_Handing = new Fragment_Handing();
			switchFragment(fragment_Handing);
			curFragment = fragment_Handing;
			break;
		}
	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(
					R.id.fragment_classify_layout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}
}
