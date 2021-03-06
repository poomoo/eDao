package com.poomoo.edao.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.fragment.Fragment_Home;
import com.poomoo.edao.fragment.Fragment_Personal_Center;
import com.poomoo.edao.fragment.Fragment_Store;
import com.poomoo.edao.receiver.NetWorkConnectionChangeReceiver;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.SideBar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class NavigationActivity extends BaseActivity implements OnClickListener {
	private LinearLayout layout_myOrder, layout_shopping_cart, layout_messages, layout_shared, layout_check_update,
			layout_feed_back, layout_help_center, layout_about_us;
	private FrameLayout frameLayout;
	private RadioButton radioButton_home, radioButton_myown;
	public static RadioButton radioButton_shop;
	private Fragment_Home fragment_Home;
	public static Fragment_Store fragment_Store;
	private Fragment_Personal_Center fragment_Personal_Center;
	public static Fragment curFragment;

	// 侧边栏
	public static SideBar sideBar;
	private int clo = 0;
	private TextView textView_username, textView_phonenum, textView_ecoin, textView_gold_coin, textView_point;
	private eDaoClientApplication application = null;
	public static NavigationActivity instance = null;
	public static Handler handler = null;

	private NetWorkConnectionChangeReceiver myReceiver = null;
	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		application = (eDaoClientApplication) getApplication();
		instance = this;
		init();
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO 自动生成的方法存根
				super.handleMessage(msg);
				if (msg.what == eDaoClientConfig.freshFlag) {
					System.out.println("收到返回");
					setUserInfo();
					Message message = new Message();
					message.what = eDaoClientConfig.freshFlag;
					Fragment_Home.handler.sendMessage(message);
				}
			}

		};
		// 注册网络状态监听
		registerReceiver();
	}

	private void init() {
		// TODO 自动生成的方法存根
		frameLayout = (FrameLayout) findViewById(R.id.navigation_frameLayout);
		radioButton_home = (RadioButton) findViewById(R.id.navigation_radioButton_home);
		radioButton_shop = (RadioButton) findViewById(R.id.navigation_radioButton_shop);
		radioButton_myown = (RadioButton) findViewById(R.id.navigation_radioButton_myown);
		sideBar = (SideBar) findViewById(R.id.navigation_sidebar);
		layout_shared = (LinearLayout) findViewById(R.id.sidebar_layout_shared);
		layout_check_update = (LinearLayout) findViewById(R.id.sidebar_layout_check_update);
		layout_feed_back = (LinearLayout) findViewById(R.id.sidebar_layout_feed_back);
		layout_help_center = (LinearLayout) findViewById(R.id.sidebar_layout_help_center);
		layout_about_us = (LinearLayout) findViewById(R.id.sidebar_layout_about_us);
		layout_myOrder = (LinearLayout) findViewById(R.id.sidebar_layout_myorder);
		layout_shopping_cart = (LinearLayout) findViewById(R.id.sidebar_layout_shopping_cart);
		layout_messages = (LinearLayout) findViewById(R.id.sidebar_layout_messges);

		frameLayout.setOnClickListener(this);
		radioButton_home.setOnClickListener(this);
		radioButton_shop.setOnClickListener(this);
		radioButton_myown.setOnClickListener(this);
		layout_shared.setOnClickListener(this);
		layout_check_update.setOnClickListener(this);
		layout_feed_back.setOnClickListener(this);
		layout_help_center.setOnClickListener(this);
		layout_about_us.setOnClickListener(this);
		layout_myOrder.setOnClickListener(this);
		layout_shopping_cart.setOnClickListener(this);
		layout_messages.setOnClickListener(this);

		setDefaultFragment();

		textView_username = (TextView) findViewById(R.id.sidebar_textView_userName);
		textView_phonenum = (TextView) findViewById(R.id.sidebar_textView_userTel);
		textView_ecoin = (TextView) findViewById(R.id.sidebar_textView_ecoin);
		textView_gold_coin = (TextView) findViewById(R.id.sidebar_textView_gold_coin);
		textView_point = (TextView) findViewById(R.id.sidebar_textView_point);

		// 是否实名认证
		if (!application.getRealNameAuth().equals("1")) {
			System.out.println("进行实名认证");
			textView_username.setText(eDaoClientConfig.certificate);
			textView_phonenum.setText("");
			// spark();
		} else
			Utity.setUserAndTel(textView_username, textView_phonenum, application);
		textView_username.setOnClickListener(this);
	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragment_Home = new Fragment_Home();
		curFragment = fragment_Home;
		fragmentTransaction.add(R.id.navigation_frameLayout, fragment_Home);
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.navigation_radioButton_home:
			sideBar.closeMenu();
			if (fragment_Home == null)
				fragment_Home = new Fragment_Home();
			switchFragment(fragment_Home);
			curFragment = fragment_Home;
			break;
		case R.id.navigation_radioButton_shop:
			sideBar.closeMenu();
			if (fragment_Store == null)
				fragment_Store = new Fragment_Store();
			switchFragment(fragment_Store);
			curFragment = fragment_Store;
			break;
		case R.id.navigation_radioButton_myown:
			sideBar.closeMenu();
			if (fragment_Personal_Center == null)
				fragment_Personal_Center = new Fragment_Personal_Center();
			switchFragment(fragment_Personal_Center);
			curFragment = fragment_Personal_Center;
			break;
		case R.id.sidebar_textView_userName:
			sideBar.closeMenu();
			if (!application.getRealNameAuth().equals("1")) {
				openActivity(CertificationActivity.class);
			} else {
				if (fragment_Personal_Center == null)
					fragment_Personal_Center = new Fragment_Personal_Center();
				switchFragment(fragment_Personal_Center);
				curFragment = fragment_Personal_Center;
				radioButton_myown.setChecked(true);
			}
			break;
		case R.id.sidebar_layout_shared:
			openActivity(ShareActivity.class);
			break;
		case R.id.sidebar_layout_check_update:
			// openActivity(OrderListActivity.class);
			break;
		case R.id.sidebar_layout_feed_back:
			openActivity(FeedBackActivity.class);
			break;
		case R.id.sidebar_layout_help_center:
			Bundle pBundle = new Bundle();
			pBundle.putString("from", "help");
			openActivity(WebViewActivity.class, pBundle);
			break;
		case R.id.sidebar_layout_about_us:
			openActivity(AboutUsActivity.class);
			break;
		case R.id.sidebar_layout_myorder:
			openActivity(MyOrderActivity.class);
			break;
		case R.id.sidebar_layout_shopping_cart:
			Utity.showToast(getApplicationContext(), eDaoClientConfig.notDevelop);
			break;
		case R.id.sidebar_layout_messges:
			Utity.showToast(getApplicationContext(), eDaoClientConfig.notDevelop);
			break;
		}
	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(R.id.navigation_frameLayout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}

	public void spark() {
		Timer timer = new Timer();
		TimerTask taskcc = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {

					public void run() {
						if (clo == 0) {
							clo = 1;
							textView_username.setTextColor(Color.WHITE);
						} else {
							clo = 0;
							textView_username.setTextColor(Color.RED);
						}
					}
				});
			}
		};
		timer.schedule(taskcc, 1, 1000);
	}

	@Override
	protected void onRestart() {
		// TODO 自动生成的方法存根
		super.onRestart();
		setUserInfo();
	}

	private void setUserInfo() {
		// TODO 自动生成的方法存根
		// 是否实名认证
		if (!application.getRealNameAuth().equals("1")) {
			System.out.println("进行实名认证");
			textView_username.setText(eDaoClientConfig.certificate);
			textView_phonenum.setText(Utity.addStarByNum(3, 7, application.getTel()));
		} else
			Utity.setUserAndTel(textView_username, textView_phonenum, application);
		textView_ecoin.setText("" + application.getTotalEb());
		textView_gold_coin.setText("" + application.getTotalGold());
		textView_point.setText("" + application.getTotalIntegral());
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		myReceiver = new NetWorkConnectionChangeReceiver();
		this.registerReceiver(myReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(myReceiver);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (sideBar.isOpen())
				sideBar.closeMenu();
			else
				exitApp();
		}
		return true;
	}

	private void exitApp() {
		// 判断2次点击事件时间
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Utity.showToast(getApplicationContext(), "再按一次退出程序");
			exitTime = System.currentTimeMillis();
		} else {
			finish();
		}
	}
}
