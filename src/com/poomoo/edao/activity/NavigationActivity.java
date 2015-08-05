package com.poomoo.edao.activity;

import org.litepal.tablemanager.Connector;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.poomoo.edao.R;
import com.poomoo.edao.fragment.Fragment_Home;
import com.poomoo.edao.fragment.Fragment_Store;

public class NavigationActivity extends BaseActivity implements OnClickListener {

	private FrameLayout frameLayout;
	private RadioGroup radioGroup;
	private RadioButton radioButton_home, radioButton_shop, radioButton_myown;
	private Fragment_Home fragment_Home;
	private Fragment_Store fragment_Store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		SQLiteDatabase db=Connector.getDatabase();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		frameLayout = (FrameLayout) findViewById(R.id.navigation_frameLayout);
		radioGroup = (RadioGroup) findViewById(R.id.navigation_radioGroup);
		radioButton_home = (RadioButton) findViewById(R.id.navigation_radioButton_home);
		radioButton_shop = (RadioButton) findViewById(R.id.navigation_radioButton_shop);
		radioButton_myown = (RadioButton) findViewById(R.id.navigation_radioButton_myown);
		
		radioButton_home.setOnClickListener(this);
		radioButton_shop.setOnClickListener(this);
		radioButton_myown.setOnClickListener(this);
		
		setDefaultFragment();

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
			break;
		}
		System.out.println("fragment提交前");
		fragmentTransaction.commit();
		System.out.println("fragment提交后");

	}
}
