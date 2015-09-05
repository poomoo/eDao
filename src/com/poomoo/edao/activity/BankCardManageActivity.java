package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.poomoo.edao.adapter.ChannelSpinnerAdapter;
import com.poomoo.edao.adapter.CitySpinnerAdapter;
import com.poomoo.edao.adapter.ProvinceSpinnerAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.database.CityInfo;
import com.poomoo.edao.model.database.ProvinceInfo;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MessageBox_YES;

/**
 * 
 * @ClassName BankCardManageActivity
 * @Description TODO 银行卡管理
 * @author 李苜菲
 * @date 2015-9-5 上午10:57:43
 */
public class BankCardManageActivity extends BaseActivity implements
		OnClickListener {
	private EditText editText_accountnum, editText_accountnumagain;
	private Button button_next;
	private PopupWindow popupWindow;
	private LinearLayout layout_bank, layout;
	private TextView textView_bank;

	private ChannelSpinnerAdapter adapter_bank;

	private List<HashMap<String, String>> list_bank;
	private ListView listView;

	private eDaoClientApplication applicaiton = null;
	private final String[] strbank = new String[] { "中国建设银行", "中国工商银行",
			"中国农业银行", "中国银行", "招商银行" };
	private String province_name = "", province_id = "", city_name = "",
			city_id = "";
	private String bank = "", account1 = "", account2 = "";
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private MessageBox_YES box_YES;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bank_card_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		applicaiton = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_accountnum = (EditText) findViewById(R.id.bank_card_manage_editText_accountnum);
		editText_accountnumagain = (EditText) findViewById(R.id.bank_card_manage_editText_accountnumagain);
		textView_bank = (TextView) findViewById(R.id.bank_card_manage_textView_bank);
		layout_bank = (LinearLayout) findViewById(R.id.bank_card_manage_layout_bank);
		button_next = (Button) findViewById(R.id.bank_card_manage_btn_confirm);

		list_bank = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> bank_data;
		for (int i = 0; i < strbank.length; i++) {
			bank_data = new HashMap<String, String>();
			bank_data.put("name", strbank[i]);
			list_bank.add(bank_data);
		}

		adapter_bank = new ChannelSpinnerAdapter(this, list_bank);

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
		case R.id.bank_card_manage_layout_bank:
			showWindow_bank(layout_bank, listView, list_bank, textView_bank,
					adapter_bank);
			break;
		case R.id.bank_card_manage_btn_confirm:

			break;
		}
	}

	private void certificate() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10008");
		SharedPreferences sp = getSharedPreferences("userInfo",
				Context.MODE_PRIVATE);
		data.put("userId", sp.getString("userId", ""));
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
											BankCardManageActivity.this);
									box_YES.showDialog(responseData.getMsg(),
											null);
								} else {
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

	public void showWindow_bank(final LinearLayout spinnerlayout,
			ListView listView, final List<HashMap<String, String>> list,
			final TextView text, ChannelSpinnerAdapter adapter) {

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
