package com.poomoo.edao.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplicaiton;

/**
 * 
 * @ClassName CooperationActivity
 * @Description TODO 合作加盟
 * @author 李苜菲
 * @date 2015年7月30日 下午11:07:55
 */
public class CooperationActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_username, textView_tel;
	private Button button_alliance_apply, button_dealer_apply,
			button_partner_apply;
	private eDaoClientApplicaiton applicaiton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cooperation);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		applicaiton = (eDaoClientApplicaiton) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_tel = (TextView) findViewById(R.id.layout_userinfo_textView_tel);

		button_alliance_apply = (Button) findViewById(R.id.cooperation_btn_alliance_apply);
		button_dealer_apply = (Button) findViewById(R.id.cooperation_btn_dealer_apply);
		button_partner_apply = (Button) findViewById(R.id.cooperation_btn_partner_apply);

		button_alliance_apply.setOnClickListener(this);
		button_dealer_apply.setOnClickListener(this);
		button_partner_apply.setOnClickListener(this);

		textView_username.setText(applicaiton.getRealName());
		textView_tel.setText(applicaiton.getTel());
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.cooperation_btn_alliance_apply:
			openActivity(AllianceApplyActivity.class);
			finish();
			break;
		case R.id.cooperation_btn_dealer_apply:
			openActivity(DealerApplyActivity.class);
			finish();
			break;
		case R.id.cooperation_btn_partner_apply:
			openActivity(PartnerApplyActivity.class);
			finish();
			break;
		}
	}

}
