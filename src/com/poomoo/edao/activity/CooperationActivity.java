package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.UserInfoData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * @ClassName CooperationActivity
 * @Description TODO 合作加盟
 * @author 李苜菲
 * @date 2015年7月30日 下午11:07:55
 */
public class CooperationActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum;
	private Button button_alliance_apply, button_dealer_apply, button_partner_apply;
	private eDaoClientApplication application = null;
	private Editor editor = null;
	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cooperation);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);

		button_alliance_apply = (Button) findViewById(R.id.cooperation_btn_alliance_apply);
		button_dealer_apply = (Button) findViewById(R.id.cooperation_btn_dealer_apply);
		button_partner_apply = (Button) findViewById(R.id.cooperation_btn_partner_apply);

		button_alliance_apply.setOnClickListener(this);
		button_dealer_apply.setOnClickListener(this);
		button_partner_apply.setOnClickListener(this);

		Utity.setUserAndTel(textView_username, textView_phonenum, application);
		
		if(application.getType().equals("2"))
			checkStatus();
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

	private void checkStatus() {
		showProgressDialog("查看审核状态中...");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10013");
		data.put("userId", application.getUserId());
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						if (responseData.getRsCode() == 1) {
							UserInfoData infoData = new UserInfoData();
							infoData = gson.fromJson(responseData.getJsonData(), UserInfoData.class);
							String type = infoData.getType();
							String joinStatus = infoData.getJoinStatus();
							application.setType(type);
							application.setJoinStatus(joinStatus);

							editor = getSharedPreferences("userInfo", Context.MODE_PRIVATE).edit();
							editor.putString("type", type);
							editor.putString("joinStatus", joinStatus);
							editor.commit();
							if (application.getRealNameAuth().equals("0")) {
								Utity.showToast(getApplicationContext(), "加盟审核中...");
								finish();
							} else if (application.getRealNameAuth().equals("1")) {
								Utity.showToast(getApplicationContext(), "加盟成功!");
								finish();
							} else {
								Utity.showToast(getApplicationContext(), "加盟审核失败,请重新申请!");
							}

						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
							finish();
						}

					}
				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
						finish();
					}

				});
			}
		});
	}
}
