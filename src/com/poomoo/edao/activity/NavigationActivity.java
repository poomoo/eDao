package com.poomoo.edao.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.poomoo.edao.R;
import com.poomoo.edao.fragment.Fragment_Store;

public class NavigationActivity extends BaseActivity implements OnClickListener {

	private FrameLayout frameLayout;
	private RadioGroup radioGroup;
	private RadioButton radioButton_home, radioButton_shop, radioButton_myown,
			radioButton_more;
	private Fragment_Store fragment_Store;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		frameLayout = (FrameLayout) findViewById(R.id.navigation_frameLayout);
		radioGroup = (RadioGroup) findViewById(R.id.navigation_radioGroup);
		radioButton_home = (RadioButton) findViewById(R.id.navigation_radioButton_home);
		radioButton_shop = (RadioButton) findViewById(R.id.navigation_radioButton_shop);
		radioButton_myown = (RadioButton) findViewById(R.id.navigation_radioButton_myown);
		radioButton_more = (RadioButton) findViewById(R.id.navigation_radioButton_more);

		setDefaultFragment();

	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragment_Store = new Fragment_Store();
		fragmentTransaction
				.replace(R.id.navigation_frameLayout, fragment_Store);
		fragmentTransaction.commit();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		switch (v.getId()) {
		case R.id.navigation_radioButton_home:

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
		fragmentTransaction.commit();

	}
}
