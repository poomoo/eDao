package com.poomoo.edao.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.Fragment_Home_GridViewAdapter;

/**
 * 
 * @ClassName Fragment_Home
 * @Description TODO 首页
 * @author 李苜菲
 * @date 2015-8-4 下午3:59:10
 */
public class Fragment_Home extends Fragment {
	private TextView textView_inform, textView_ecoin, textView_goldcoin,
			textView_point;
	private ImageView imageView_user, imageView_position;
	private GridView gridView;

	private Fragment_Home_GridViewAdapter gridViewAdapter;
	private final String[] list_name = { "全国返利", "消费领取", "我的钱包", "交易明细",
			"信用管理", "转账支付", "合作加盟", "爱心基金", "乐意道商城" };
	private final int[] list_image = { R.drawable.ic_rebate,
			R.drawable.ic_get_detail, R.drawable.ic_my_wallet,
			R.drawable.ic_purchase_history, R.drawable.ic_credit_manage,
			R.drawable.ic_transfer_payment, R.drawable.ic_cooperation,
			R.drawable.ic_love_fund, R.drawable.ic_shop };

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
		imageView_user = (ImageView) getView().findViewById(
				R.id.main_imageView_user);
		imageView_position = (ImageView) getView().findViewById(
				R.id.main_imageView_position);
		gridView = (GridView) getView().findViewById(R.id.main_gridView);

		gridViewAdapter = new Fragment_Home_GridViewAdapter(getActivity(),
				list_name, list_image, gridView);
		gridView.setAdapter(gridViewAdapter);
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
