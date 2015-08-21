package com.poomoo.edao.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.activity.CertificationActivity;
import com.poomoo.edao.activity.CooperationActivity;
import com.poomoo.edao.activity.CreditManageActivity;
import com.poomoo.edao.activity.GetDetailActivity;
import com.poomoo.edao.activity.LoveFundActivity;
import com.poomoo.edao.activity.MapActivity;
import com.poomoo.edao.activity.MywalletActivity;
import com.poomoo.edao.activity.NavigationActivity;
import com.poomoo.edao.activity.PurchaseHistoryActivity;
import com.poomoo.edao.activity.RebateActivity;
import com.poomoo.edao.activity.TransferOfPaymentActivity1;
import com.poomoo.edao.adapter.Fragment_Home_GridViewAdapter;
import com.poomoo.edao.application.eDaoClientApplicaiton;
import com.poomoo.edao.widget.SideBar;

/**
 * 
 * @ClassName Fragment_Home
 * @Description TODO 首页
 * @author 李苜菲
 * @date 2015-8-4 下午3:59:10
 */
public class Fragment_Home extends Fragment implements OnClickListener,
		OnItemClickListener {
	private TextView textView_inform, textView_ecoin, textView_goldcoin,
			textView_point;
	private LinearLayout layout_user, layout_map;
	private RadioButton radioButton_shop;
	private GridView gridView;
	private SideBar sidebar;
	private Fragment_Store fragment_Store;

	private Fragment_Home_GridViewAdapter gridViewAdapter;
	private static final String[] list_name = { "普惠全民", "消费领取", "我的钱包", "交易明细",
			"信用管理", "转账支付", "合作加盟", "爱心基金", "乐意道商城" };
	private static final int[] list_image = { R.drawable.ic_rebate,
			R.drawable.ic_get_detail, R.drawable.ic_my_wallet,
			R.drawable.ic_purchase_history, R.drawable.ic_credit_manage,
			R.drawable.ic_transfer_payment, R.drawable.ic_cooperation,
			R.drawable.ic_love_fund, R.drawable.ic_shop };

	private static final Class[] outIntent = { RebateActivity.class,
			GetDetailActivity.class, MywalletActivity.class,
			PurchaseHistoryActivity.class, CreditManageActivity.class,
			TransferOfPaymentActivity1.class, CooperationActivity.class,
			LoveFundActivity.class, Fragment_Store.class };

	private eDaoClientApplicaiton applicaiton=null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		// 实现沉浸式状态栏效果
		setImmerseLayout(getView().findViewById(R.id.fragment_home_layout));

		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_inform = (TextView) getView().findViewById(
				R.id.main_textView_inform);
		textView_ecoin = (TextView) getView().findViewById(
				R.id.main_textView_ecoin);
		textView_goldcoin = (TextView) getView().findViewById(
				R.id.main_textView_goldcoin);
		textView_point = (TextView) getView().findViewById(
				R.id.main_textView_point);
		layout_user = (LinearLayout) getView().findViewById(
				R.id.main_layout_user);
		layout_map = (LinearLayout) getView()
				.findViewById(R.id.main_layout_map);
		gridView = (GridView) getView().findViewById(R.id.main_gridView);

		gridViewAdapter = new Fragment_Home_GridViewAdapter(getActivity(),
				list_name, list_image, gridView);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(this);

		sidebar = NavigationActivity.sideBar;
		fragment_Store = NavigationActivity.fragment_Store;
		radioButton_shop = (RadioButton) NavigationActivity.radioButton_shop;

		layout_user.setOnClickListener(this);
		layout_map.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.main_layout_user:
			sidebar.toggle();
			break;
		case R.id.main_layout_map:
			startActivity(new Intent(getActivity(), MapActivity.class));
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO 自动生成的方法存根
		showActivity(arg2);
	}

	private void showActivity(int arg2) {
		// TODO 自动生成的方法存根
		if (arg2 == 6 || arg2 == 2) {
			applicaiton = (eDaoClientApplicaiton) getActivity()
					.getApplication();
			// if (!applicaiton.getType().equals("2")) {
			// startActivity(new Intent(getActivity(),
			// CertificationActivity.class));
			// } else
				startActivity(new Intent(getActivity(), outIntent[arg2]));
		} else if (arg2 != 8)
			startActivity(new Intent(getActivity(), outIntent[arg2]));
		else {
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragment_Store == null)
				fragment_Store = new Fragment_Store();
			fragmentTransaction.replace(R.id.navigation_frameLayout,
					fragment_Store);
			fragmentTransaction.commit();
			radioButton_shop.setChecked(true);
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
