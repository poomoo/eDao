package com.poomoo.edao.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplicaiton;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.fragment.Fragment_Home;
import com.poomoo.edao.fragment.Fragment_Personal_Center;
import com.poomoo.edao.fragment.Fragment_Store;
import com.poomoo.edao.widget.SideBar;

public class NavigationActivity extends BaseActivity implements OnClickListener {

	private FrameLayout frameLayout;
	private RadioButton radioButton_home, radioButton_myown;
	public static RadioButton radioButton_shop;
	private Fragment_Home fragment_Home;
	public static Fragment_Store fragment_Store;
	private Fragment_Personal_Center fragment_Personal_Center;
	public static SideBar sideBar;
	private int clo = 0;

	// 侧边栏
	private TextView textView_userName, textView_userTel;
	private eDaoClientApplicaiton applicaiton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		applicaiton=(eDaoClientApplicaiton)getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		frameLayout = (FrameLayout) findViewById(R.id.navigation_frameLayout);
		radioButton_home = (RadioButton) findViewById(R.id.navigation_radioButton_home);
		radioButton_shop = (RadioButton) findViewById(R.id.navigation_radioButton_shop);
		radioButton_myown = (RadioButton) findViewById(R.id.navigation_radioButton_myown);
		sideBar = (SideBar) findViewById(R.id.navigation_sidebar);

		frameLayout.setOnClickListener(this);
		radioButton_home.setOnClickListener(this);
		radioButton_shop.setOnClickListener(this);
		radioButton_myown.setOnClickListener(this);

		setDefaultFragment();

		textView_userName = (TextView) findViewById(R.id.sidebar_textView_userName);
		textView_userTel = (TextView) findViewById(R.id.sidebar_textView_userTel);

		// 是否实名认证
		if (TextUtils.isEmpty(applicaiton.getRealName())) {
			System.out.println("进行实名认证");
			textView_userName.setText(eDaoClientConfig.certificate);
			spark();
		} else
			textView_userName.setText(applicaiton.getRealName());
		textView_userTel.setText(applicaiton.getTel());
		textView_userName.setOnClickListener(this);

	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragment_Home = new Fragment_Home();
		fragmentTransaction.replace(R.id.navigation_frameLayout, fragment_Home);
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		System.out.println("onClick");
		sideBar.closeMenu();
		switch (v.getId()) {
		case R.id.navigation_radioButton_home:
			System.out.println("onClick home");
			if (fragment_Home == null)
				fragment_Home = new Fragment_Home();
			fragmentTransaction.replace(R.id.navigation_frameLayout,
					fragment_Home);
			break;
		case R.id.navigation_radioButton_shop:
			if (fragment_Store == null)
				fragment_Store = new Fragment_Store();
			fragmentTransaction.replace(R.id.navigation_frameLayout,
					fragment_Store);
			break;
		case R.id.navigation_radioButton_myown:
			if (fragment_Personal_Center == null)
				fragment_Personal_Center = new Fragment_Personal_Center();
			fragmentTransaction.replace(R.id.navigation_frameLayout,
					fragment_Personal_Center);
			break;
		case R.id.sidebar_textView_userName:
			System.out.println("点击userName");
			openActivity(CertificationActivity.class);
			break;
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
							textView_userName.setTextColor(Color.WHITE);
						} else {
							clo = 0;
							textView_userName.setTextColor(Color.RED);
						}
					}
				});
			}
		};
		timer.schedule(taskcc, 1, 1000);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (sideBar.isOpen())
				sideBar.closeMenu();
			else
				finish();
		}
		return true;
	}

}
