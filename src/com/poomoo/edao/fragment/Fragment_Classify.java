package com.poomoo.edao.fragment;

import com.poomoo.edao.R;
import com.poomoo.edao.activity.DealDetailActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_Classify extends Fragment implements OnClickListener {
	private TextView textView_transfer, textView_recharge, textView_buy_keys, textView_apply, textView_buy_goods,
			textView_handing;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_classify, container, false);
	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragment_Transfer = new Fragment_Transfer();
		fragmentTransaction.add(R.id.fragment_classify_layout, fragment_Transfer);
		curFragment = fragment_Transfer;
		fragmentTransaction.commit();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_transfer = (TextView) getView().findViewById(R.id.fragment_classify_textView_transfer);
		textView_recharge = (TextView) getView().findViewById(R.id.fragment_classify_textView_recharge);
		textView_buy_keys = (TextView) getView().findViewById(R.id.fragment_classify_textView_buy_keys);
		textView_apply = (TextView) getView().findViewById(R.id.fragment_classify_textView_apply);
		textView_buy_goods = (TextView) getView().findViewById(R.id.fragment_classify_textView_buy_goods);
		textView_handing = (TextView) getView().findViewById(R.id.fragment_classify_textView_handing);

		textView_transfer.setOnClickListener(this);
		textView_recharge.setOnClickListener(this);
		textView_buy_keys.setOnClickListener(this);
		textView_apply.setOnClickListener(this);
		textView_buy_goods.setOnClickListener(this);
		textView_handing.setOnClickListener(this);

		textView_transfer.setSelected(true);
	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(R.id.fragment_classify_layout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		clearSelected();
		v.setSelected(true);
		switch (v.getId()) {
		case R.id.fragment_classify_textView_transfer:
			if (fragment_Transfer == null)
				fragment_Transfer = new Fragment_Transfer();
			switchFragment(fragment_Transfer);
			curFragment = fragment_Transfer;
			DealDetailActivity.instance.button_classify.setText("意币转账");
			break;
		case R.id.fragment_classify_textView_recharge:
			if (fragment_Recharge == null)
				fragment_Recharge = new Fragment_Recharge();
			switchFragment(fragment_Recharge);
			curFragment = fragment_Recharge;
			DealDetailActivity.instance.button_classify.setText("充值");
			break;
		case R.id.fragment_classify_textView_buy_keys:
			if (fragment_Buy_Key == null)
				fragment_Buy_Key = new Fragment_Buy_Key();
			switchFragment(fragment_Buy_Key);
			curFragment = fragment_Buy_Key;
			DealDetailActivity.instance.button_classify.setText("购买秘钥");
			break;
		case R.id.fragment_classify_textView_apply:
			if (fragment_Apply == null)
				fragment_Apply = new Fragment_Apply();
			switchFragment(fragment_Apply);
			curFragment = fragment_Apply;
			DealDetailActivity.instance.button_classify.setText("加盟申请");
			break;
		case R.id.fragment_classify_textView_buy_goods:
			if (fragment_Buy_Goods == null)
				fragment_Buy_Goods = new Fragment_Buy_Goods();
			switchFragment(fragment_Buy_Goods);
			curFragment = fragment_Buy_Goods;
			DealDetailActivity.instance.button_classify.setText("购买商品");
			break;
		case R.id.fragment_classify_textView_handing:
			if (fragment_Handing == null)
				fragment_Handing = new Fragment_Handing();
			switchFragment(fragment_Handing);
			curFragment = fragment_Handing;
			DealDetailActivity.instance.button_classify.setText("提现申请");
			break;
		}
	}

	private void clearSelected() {
		textView_transfer.setSelected(false);
		textView_recharge.setSelected(false);
		textView_buy_keys.setSelected(false);
		textView_apply.setSelected(false);
		textView_buy_goods.setSelected(false);
		textView_handing.setSelected(false);
	}
}
