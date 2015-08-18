package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.ChannelSpinnerAdapter;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.popupwindow.Select_City_PopupWindow;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.CityPicker;

/**
 * 
 * @ClassName AllianceApplyActivity
 * @Description TODO 加盟商申请
 * @author 李苜菲
 * @date 2015年7月30日 下午11:23:58
 */
public class AllianceApplyActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_username, textView_phonenum, textView_zone,
			textView_money, textView_merchant_name;
	private EditText editText_merchant_num;
	private LinearLayout layout_zone;
	private Button button_confirm;

	private PopupWindow popupWindow;
	private View layout;
	private ChannelSpinnerAdapter adapter_zone, adapter_merchant_type;
	private List<HashMap<String, String>> list_zone, list_merchant_type;
	private ListView listView;
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private String zone = "", merchant_num = "", merchant_name = "",
			referrerUserId = "", referrerName = "", curProvince = "",
			curCity = "";
	private Select_City_PopupWindow select_City_PopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alliance_apply);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		isPartener();
		init();
	}

	private void isPartener() {
		// TODO 自动生成的方法存根
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		if (!sp.getString("type", "1").equals("2")) {
			openActivity(CertificationActivity.class);
		}

	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_zone = (TextView) findViewById(R.id.alliance_textView_zone);
		textView_money = (TextView) findViewById(R.id.alliance_textView_money);
		textView_merchant_name = (TextView) findViewById(R.id.alliance_textView_merchant_name);

		editText_merchant_num = (EditText) findViewById(R.id.alliance_editText_merchant_num);

		layout_zone = (LinearLayout) findViewById(R.id.alliance_layout_zone);

		button_confirm = (Button) findViewById(R.id.alliance_btn_confirm);

		layout_zone.setOnClickListener(this);
		button_confirm.setOnClickListener(this);
		textView_merchant_name.setOnClickListener(this);

		// 取当前定位
		SharedPreferences sp = getSharedPreferences("location",
				Context.MODE_PRIVATE);
		curProvince = sp.getString("province", "");
		curCity = sp.getString("city", "");
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.alliance_layout_zone:
			select_city();
			break;
		case R.id.alliance_textView_merchant_name:
			getMerchantName();
			break;
		case R.id.alliance_btn_confirm:
			if (checkInput()) {
				apply();
			}
			break;
		}

	}

	private void select_city() {
		CityPicker.province_name = curProvince;
		CityPicker.city_name = curCity;
		// 实例化SelectPicPopupWindow
		select_City_PopupWindow = new Select_City_PopupWindow(
				AllianceApplyActivity.this, itemsOnClick);
		// 显示窗口
		select_City_PopupWindow.showAtLocation(AllianceApplyActivity.this
				.findViewById(R.id.activity_alliance_layout), Gravity.CENTER,
				0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			System.out.println("点击事件---" + view.getId());
			if (view.getId() == R.id.popup_select_city_btn_confirm) {
				select_City_PopupWindow.dismiss();
				textView_zone.setText(CityPicker.getZone_string());
				// getMoney();
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
	 * @throws
	 * @date 2015-8-17下午4:54:42
	 */
	private void getMoney() {
		// TODO 自动生成的方法存根
		progressDialog = null;
		showProgressDialog("查询费用......");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20001");
		data.put("joinType", "1");
		data.put("areaId", CityPicker.getArea_id());
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

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
										JSONObject result = new JSONObject(
												responseData.getJsonData()
														.toString());
										textView_money.setText(result
												.getString("price"));
									} catch (JSONException e) {
									}
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
								Utity.showToast(getApplicationContext(),
										eDaoClientConfig.checkNet);
							}
						});
					}
				});
	}

	/**
	 * 
	 * 
	 * @Title: apply
	 * @Description: TODO 提交申请
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-17下午4:53:39
	 */
	private void apply() {
		progressDialog = null;
		showProgressDialog("提交申请中...");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20003");
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		data.put("userId", sp.getString("userId", ""));
		data.put("areaId", CityPicker.getArea_id());
		data.put("joinType", "1");
		data.put("address", "");
		data.put("referrerTel", merchant_num);
		data.put("referrerUserId", referrerUserId);
		data.put("referrerName", referrerName);

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

					@Override
					public void onFinish(final ResponseData responseData) {
						// TODO 自动生成的方法存根
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO 自动生成的方法存根
								closeProgressDialog();
								if (responseData.getRsCode() == 1) {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
								Utity.showToast(getApplicationContext(),
										eDaoClientConfig.checkNet);
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
	 * @throws
	 * @date 2015-8-17下午4:52:44
	 */
	private boolean checkInput() {
		// TODO 自动生成的方法存根
		zone = textView_zone.getText().toString().trim();
		if (TextUtils.isEmpty(zone)) {
			select_city();
			Utity.showToast(getApplicationContext(), "请选择区域");
			return false;
		}
		merchant_num = editText_merchant_num.getText().toString().trim();
		if (TextUtils.isEmpty(merchant_num)) {
			editText_merchant_num.setFocusable(true);
			editText_merchant_num.setFocusableInTouchMode(true);
			editText_merchant_num.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入服务商手机号");
			return false;
		}
		merchant_name = textView_merchant_name.getText().toString().trim();
		if (TextUtils.isEmpty(zone)) {
			Utity.showToast(getApplicationContext(), "请查询服务商户名");
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
	 * @throws
	 * @date 2015-8-17下午4:52:19
	 */
	private void getMerchantName() {
		merchant_num = editText_merchant_num.getText().toString().trim();
		if (merchant_num.length() != 11) {
			Utity.showToast(getApplicationContext(), "手机号长度不对");
			return;
		}
		progressDialog = null;
		showProgressDialog("查询服务商户名");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20002");
		data.put("referrerTel", merchant_num);

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

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
										JSONObject result = new JSONObject(
												responseData.getJsonData()
														.toString());
										referrerUserId = result
												.getString("referrerUserId");
										referrerName = result
												.getString("referrerName");
										textView_merchant_name
												.setText(referrerName);
									} catch (JSONException e) {
									}
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
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
								Utity.showToast(getApplicationContext(),
										eDaoClientConfig.checkNet);
							}
						});
					}
				});
	}

	/**
	 * 
	 * 
	 * @Title: showProgressDialog
	 * @Description: TODO 显示进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-12下午1:23:53
	 */
	private void showProgressDialog(String msg) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage(msg);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * 
	 * 
	 * @Title: closeProgressDialog
	 * @Description: TODO 关闭进度对话框
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws
	 * @date 2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

}
