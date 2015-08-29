package com.poomoo.edao.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.litepal.LitePalApplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.CitySpinnerAdapter;
import com.poomoo.edao.adapter.ProvinceSpinnerAdapter;
import com.poomoo.edao.adapter.RegistrationSpinnerAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.database.CityInfo;
import com.poomoo.edao.model.database.ProvinceInfo;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.DialogResultListener;
import com.poomoo.edao.widget.MessageBox_YES;

/**
 * 
 * @ClassName CertificationActivity
 * @Description TODO 实名认证
 * @author 李苜菲
 * @date 2015-8-14 下午2:22:02
 */
public class CertificationActivity extends BaseActivity implements
		OnClickListener {
	private EditText editText_realName, editText_idNum, editText_accountnum,
			editText_accountnumagain;
	private Button button_next;
	private PopupWindow popupWindow;
	private LinearLayout layout_province, layout_city, layout_bank, layout;
	private TextView textView_province, textView_city, textView_bank;

	private ProvinceSpinnerAdapter adapter_province;
	private CitySpinnerAdapter adapter_city;
	private RegistrationSpinnerAdapter adapter_bank;

	private ArrayList<ProvinceInfo> list_province;
	private ArrayList<CityInfo> list_city;
	private List<HashMap<String, String>> list_bank;
	private ListView listView;

	private eDaoClientApplication applicaiton = null;
	private SharedPreferences sharedPreferences_certificaitonInfo = null;
	private Editor editor = null;
	private final String[] strbank = new String[] { "中国建设银行", "中国工商银行",
			"中国农业银行", "中国银行", "招商银行" };
	private String province_name = "", province_id = "", city_name = "",
			city_id = "";
	private String realName = "", idNum = "", province = "", city = "",
			bank = "", account1 = "", account2 = "";
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private MessageBox_YES box_YES;
	private boolean isUpload = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_certification);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		applicaiton=(eDaoClientApplication)getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_realName = (EditText) findViewById(R.id.registration_editText_realName);
		editText_idNum = (EditText) findViewById(R.id.registration_editText_idNum);
		editText_accountnum = (EditText) findViewById(R.id.registration_editText_accountnum);
		editText_accountnumagain = (EditText) findViewById(R.id.registration_editText_accountnumagain);
		textView_province = (TextView) findViewById(R.id.registration_textView_province);
		textView_city = (TextView) findViewById(R.id.registration_textView_city);
		textView_bank = (TextView) findViewById(R.id.registration_textView_bank);
		layout_province = (LinearLayout) findViewById(R.id.registration_layout_province);
		layout_city = (LinearLayout) findViewById(R.id.registration_layout_city);
		layout_bank = (LinearLayout) findViewById(R.id.registration_layout_bank);
		button_next = (Button) findViewById(R.id.registration_btn_confirm);

		sharedPreferences_certificaitonInfo = getSharedPreferences(
				"certificaitonInfo", Context.MODE_PRIVATE);
		if (sharedPreferences_certificaitonInfo.getBoolean("uploadStatus",
				false)) {
			province_name = sharedPreferences_certificaitonInfo.getString(
					"bankProvince", "");
			city_name = sharedPreferences_certificaitonInfo.getString(
					"bankCity", "");
			textView_province.setText(province_name);
			textView_city.setText(city_name);
			editText_realName.setText(sharedPreferences_certificaitonInfo
					.getString("realName", ""));
			editText_idNum.setText(sharedPreferences_certificaitonInfo
					.getString("idCardNum", ""));
			textView_bank.setText(sharedPreferences_certificaitonInfo
					.getString("bankName", ""));
			editText_accountnum.setText(sharedPreferences_certificaitonInfo
					.getString("bankCardId", ""));
			editText_accountnumagain
					.setText(sharedPreferences_certificaitonInfo.getString(
							"bankCardId", ""));
			isUpload = true;
		} else {
			province_name = applicaiton.getCurProvince();
			city_name = applicaiton.getCurCity();
			textView_province.setText(province_name);
			textView_city.setText(city_name);
		}
		list_province = Utity.getProvinceList();
		province_id = list_province.get(
				Utity.getProvincePosition(list_province, province_name))
				.getProvince_id();
		list_city = Utity.getCityList(province_id);
		city_id = list_city.get(Utity.getCityPosition(list_city, city_name))
				.getCity_id();

		list_bank = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> bank_data;
		for (int i = 0; i < strbank.length; i++) {
			bank_data = new HashMap<String, String>();
			bank_data.put("name", strbank[i]);
			list_bank.add(bank_data);
		}

		adapter_province = new ProvinceSpinnerAdapter(this, list_province);
		adapter_city = new CitySpinnerAdapter(this, list_city);
		adapter_bank = new RegistrationSpinnerAdapter(this, list_bank);

		layout_province.setOnClickListener(this);
		layout_city.setOnClickListener(this);
		layout_bank.setOnClickListener(this);
		button_next.setOnClickListener(this);

		// 每4位加一个空格
		Utity.setOnTextChanged(editText_accountnum);
		Utity.setOnTextChanged(editText_accountnumagain);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.registration_layout_province:
			showWindow_province(layout_province, listView, list_province,
					textView_province, adapter_province);
			break;
		case R.id.registration_layout_city:
			showWindow_city(layout_city, listView, list_city, textView_city,
					adapter_city);
			break;
		case R.id.registration_layout_bank:
			showWindow_bank(layout_bank, listView, list_bank, textView_bank,
					adapter_bank);
			break;
		case R.id.registration_btn_confirm:
			if (isUpload) {
				openActivity(UploadPicsActivity.class);
				finish();
			} else if (checkInput()) {
				certificate();
			}
			break;
		}
	}

	private void certificate() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10017");
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		data.put("userId", sp.getString("userId", ""));
		data.put("realName", realName);
		data.put("idCardNum", idNum);
		data.put("bankProvince", province_id);
		data.put("bankCity", city_id);
		data.put("bankName", bank);
		data.put("bankCardId", Utity.trimAll(account1));

		showProgressDialog();
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
								if (responseData.getRsCode() != 1) {
									box_YES = new MessageBox_YES(
											CertificationActivity.this);
									box_YES.showDialog(responseData.getMsg(),null);
								} else {
									sharedPreferences_certificaitonInfo = getSharedPreferences(
											"certificaitonInfo",
											Context.MODE_PRIVATE);
									editor = sharedPreferences_certificaitonInfo
											.edit();
									editor.putString("realName", realName);
									editor.putString("idCardNum", idNum);
									editor.putString("bankProvince", province);
									editor.putString("bankCity", city);
									editor.putString("bankName", bank);
									editor.putString("bankCardId", account1);
									editor.putBoolean("uploadStatus", true);
									editor.commit();
									openActivity(UploadPicsActivity.class);
									CertificationActivity.this.finish();
								}
							}
						});

					}

					@Override
					public void onError(final Exception e) {
						// TODO 自动生成的方法存根
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								closeProgressDialog();
								// TODO 自动生成的方法存根
								Utity.showToast(getApplicationContext(),
										eDaoClientConfig.checkNet);
							}

						});
					}
				});
	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		realName = editText_realName.getText().toString().trim();
		if (TextUtils.isEmpty(realName)) {
			editText_realName.setFocusable(true);
			editText_realName.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入姓名");
			return false;
		}
		idNum = editText_idNum.getText().toString().trim();
		if (TextUtils.isEmpty(idNum)) {
			editText_idNum.setFocusable(true);
			editText_idNum.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入身份证号");
			return false;
		}
		province = textView_province.getText().toString().trim();
		if (TextUtils.isEmpty(province)) {
			Utity.showToast(getApplicationContext(), "请选择开户省");
			return false;
		}
		city = textView_city.getText().toString().trim();
		if (TextUtils.isEmpty(city)) {
			Utity.showToast(getApplicationContext(), "请选择开户银行");
			return false;
		}
		bank = textView_bank.getText().toString().trim();
		if (TextUtils.isEmpty(bank)) {
			Utity.showToast(getApplicationContext(), "请选择开户银行");
			return false;
		}
		account1 = editText_accountnum.getText().toString().trim();
		if (TextUtils.isEmpty(account1)) {
			editText_accountnum.setFocusable(true);
			editText_accountnum.requestFocus();
			Utity.showToast(getApplicationContext(), "请输入银行账号");
			return false;
		}
		account2 = editText_accountnumagain.getText().toString().trim();
		if (TextUtils.isEmpty(account2)) {
			editText_accountnumagain.setFocusable(true);
			editText_accountnumagain.requestFocus();
			Utity.showToast(getApplicationContext(), "请确定银行账号");
			return false;
		}
		if (!account1.equals(account2)) {
			Utity.showToast(getApplicationContext(), "两次输入的账号不一样");
			return false;
		}
		return true;
	}

	public void showWindow_province(final LinearLayout spinnerlayout,
			ListView listView, final ArrayList<ProvinceInfo> list,
			final TextView text, ProvinceSpinnerAdapter adapter) {

		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.myspinner_dropdown, null);
		listView = (ListView) layout
				.findViewById(R.id.myspinner_dropdown_listView);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(spinnerlayout);
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(spinnerlayout.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(spinnerlayout, 1, 0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// spinnerlayout
				// .setBackgroundResource(R.drawable.preference_single_item);
			}

		});
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				text.setText(list.get(arg2).getProvince_name());// 设置所选的item作为下拉框的标题
				province_id = list.get(arg2).getProvince_id();
				list_city = Utity.getCityList(province_id);
				textView_city.setText(list_city.get(0).getCity_name());
				adapter_city = new CitySpinnerAdapter(
						CertificationActivity.this, list_city);
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

	public void showWindow_city(final LinearLayout spinnerlayout,
			ListView listView, final ArrayList<CityInfo> list,
			final TextView text, CitySpinnerAdapter adapter) {

		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.myspinner_dropdown, null);
		listView = (ListView) layout
				.findViewById(R.id.myspinner_dropdown_listView);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(spinnerlayout);
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(spinnerlayout.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(spinnerlayout, 1, 0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// spinnerlayout
				// .setBackgroundResource(R.drawable.preference_single_item);
			}

		});
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				text.setText(list.get(arg2).getCity_name());// 设置所选的item作为下拉框的标题
				city_id = list.get(arg2).getCity_id();
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

	public void showWindow_bank(final LinearLayout spinnerlayout,
			ListView listView, final List<HashMap<String, String>> list,
			final TextView text, RegistrationSpinnerAdapter adapter) {

		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.myspinner_dropdown, null);
		listView = (ListView) layout
				.findViewById(R.id.myspinner_dropdown_listView);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(spinnerlayout);
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(spinnerlayout.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(spinnerlayout, 1, 0);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// spinnerlayout
				// .setBackgroundResource(R.drawable.preference_single_item);
			}

		});
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				text.setText(list.get(arg2).get("name"));// 设置所选的item作为下拉框的标题
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
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
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("认证中...");
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
