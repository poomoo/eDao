package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.popupwindow.Select_City_PopupWindow;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.CityPicker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @ClassName PartnerApplyActivity
 * @Description TODO 合作商户加盟
 * @author 李苜菲
 * @date 2015年7月30日 下午11:49:23
 */
public class PartnerApplyActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum, textView_zone, textView_money, textView_merchant_name;
	private EditText editText_merchant_phone;
	private LinearLayout layout_zone;
	private Button button_confirm;

	private Select_City_PopupWindow select_City_PopupWindow;
	private eDaoClientApplication application = null;
	private Gson gson = new Gson();
	private String merchant_phone = "", merchant_name = "", referrerUserId = "", referrerName = "", curProvince = "",
			curCity = "", curArea = "", money = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partner_apply);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		getMoney();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_zone = (TextView) findViewById(R.id.partner_textView_zone);
		textView_money = (TextView) findViewById(R.id.partner_textView_money);
		textView_merchant_name = (TextView) findViewById(R.id.partner_textView_merchant_name);

		editText_merchant_phone = (EditText) findViewById(R.id.partner_editText_merchant_phone);

		layout_zone = (LinearLayout) findViewById(R.id.partner_layout_zone);

		button_confirm = (Button) findViewById(R.id.partner_btn_confirm);

		layout_zone.setOnClickListener(this);
		textView_merchant_name.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

		Utity.setUserAndTel(textView_username, textView_phonenum, application);

		// 取当前定位
		curProvince = application.getCurProvince();
		curCity = application.getCurCity();
		curArea = application.getCurArea();

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if (!application.getRealNameAuth().equals("1")) {
			openActivity(CertificationActivity.class);
			finish();
			return;
		}
		switch (v.getId()) {
		case R.id.partner_layout_zone:
			select_city();
			break;
		case R.id.partner_textView_merchant_name:
			getMerchantName();
			break;
		case R.id.partner_btn_confirm:
			if (checkInput()) {
				Bundle pBundle = new Bundle();
				pBundle.putString("money", money);
				pBundle.putString("areaId", CityPicker.getArea_id());
				pBundle.putString("referrerTel", merchant_phone);
				pBundle.putString("referrerUserId", referrerUserId);
				pBundle.putString("referrerName", referrerName);
				pBundle.putString("joinType", "3");
				openActivity(PaymentActivity.class, pBundle);
				finish();
			}
			break;
		}

	}

	private void select_city() {
		CityPicker.province_name = curProvince;
		CityPicker.city_name = curCity;
		CityPicker.area_name = curArea;
		// 实例化SelectPicPopupWindow
		select_City_PopupWindow = new Select_City_PopupWindow(PartnerApplyActivity.this, itemsOnClick);
		// 显示窗口
		select_City_PopupWindow.showAtLocation(PartnerApplyActivity.this.findViewById(R.id.activity_partner_layout),
				Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			System.out.println("点击事件---" + view.getId());
			if (view.getId() == R.id.popup_select_city_btn_confirm) {
				select_City_PopupWindow.dismiss();
				textView_zone.setText(CityPicker.getZone_string());
				getMoney();
			}
		}
	};

	/**
	 * 
	 * 
	 * @Title: getMoney
	 * @Description: TODO 查询加盟费用
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-17下午4:54:42
	 */
	private void getMoney() {
		// TODO 自动生成的方法存根
		showProgressDialog("查询费用......");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20001");
		data.put("joinType", "3");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() == 1) {
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());
								money = result.getString("price");
								textView_money.setText(money);
							} catch (JSONException e) {
							}
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
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
						// TODO 自动生成的方法存根
						closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}

	/**
	 * 
	 * 
	 * @Title: checkInput
	 * @Description: TODO 检查输入项
	 * @author 李苜菲
	 * @return
	 * @return boolean
	 * @throws @date
	 *             2015-8-17下午4:52:44
	 */
	private boolean checkInput() {
		// TODO 自动生成的方法存根
		// zone = textView_zone.getText().toString().trim();
		// if (TextUtils.isEmpty(zone)) {
		// select_city();
		// Utity.showToast(getApplicationContext(), "请选择区域");
		// return false;
		// }
		merchant_phone = editText_merchant_phone.getText().toString().trim();
		if (TextUtils.isEmpty(merchant_phone)) {
			editText_merchant_phone.setFocusable(true);
			editText_merchant_phone.setFocusableInTouchMode(true);
			editText_merchant_phone.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入服务商手机号");
			return false;
		}
		merchant_name = textView_merchant_name.getText().toString().trim();
		if (TextUtils.isEmpty(merchant_name)) {
			Utity.showToast(getApplicationContext(), "请查询服务商户名");
			return false;
		}

		if (TextUtils.isEmpty(money)) {
			Utity.showToast(getApplicationContext(), "请查询费用");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @Title: getMerchantName
	 * @Description: TODO 查询服务商户名
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-17下午4:52:19
	 */
	private void getMerchantName() {
		merchant_phone = editText_merchant_phone.getText().toString().trim();
		if (merchant_phone.length() != 11) {
			Utity.showToast(getApplicationContext(), "手机号长度不对");
			return;
		}
		showProgressDialog("查询服务商户名");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20002");
		data.put("referrerTel", merchant_phone);
		data.put("joinType", "3");

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() == 1) {
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());
								referrerUserId = result.getString("referrerUserId");
								referrerName = result.getString("referrerName");
								textView_merchant_name.setText(referrerName);
							} catch (JSONException e) {
							}
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
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
						// TODO 自动生成的方法存根
						closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}

}
