package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.ChannelSpinnerAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.service.Get_UserInfo_Service;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.DialogResultListener;
import com.poomoo.edao.widget.MessageBox_YES;
import com.poomoo.edao.widget.MessageBox_YESNO;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

/**
 * 
 * @ClassName TransferOfPaymentActivity2
 * @Description TODO 转账支付2
 * @author 李苜菲
 * @date 2015-7-30 上午11:02:41
 */
public class TransferActivity2 extends BaseActivity implements OnClickListener {

	private TextView textView_payee_name, textView_payee_phonenum, textView_balance, textView_channel,
			textView_isEnough;
	private EditText editText_pay_money, editText_pay_password, editText_remark;
	private LinearLayout layout_balance, layout_password, layout_channel;
	private Button button_pay;

	private PopupWindow popupWindow;
	private View layout;
	private ChannelSpinnerAdapter adapter;
	private List<HashMap<String, String>> list;
	private ListView listView;

	private eDaoClientApplication application = null;
	private String userId = "", realName = "", tel = "", money = "", payType = "1", payPwd = "", remark = "";
	private static final String[] channel = new String[] { "意币支付", "现金支付" };
	private boolean needPassword = false, isBalanceEnough = true;
	private Gson gson = new Gson();
	private MessageBox_YESNO box_YESNO;
	private MessageBox_YES box_YES;
	private String transferType = "";// transferType:转账类型，1：意币转账，2：购买支付

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer2);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		getIntentData();
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void getIntentData() {
		// TODO 自动生成的方法存根
		userId = getIntent().getExtras().getString("userId");
		realName = getIntent().getExtras().getString("realName");
		tel = getIntent().getExtras().getString("tel");
		money = getIntent().getExtras().getString("money");
		transferType = getIntent().getExtras().getString("transferType");
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_payee_name = (TextView) findViewById(R.id.transfer2_textView_payee_name);
		textView_payee_phonenum = (TextView) findViewById(R.id.transfer2_textView_payee_phonenum);
		textView_balance = (TextView) findViewById(R.id.transfer2_textView_balance);
		textView_channel = (TextView) findViewById(R.id.transfer2_textView_channel);
		textView_isEnough = (TextView) findViewById(R.id.transfer2_textView_isEnough);

		editText_pay_money = (EditText) findViewById(R.id.transfer2_editText_pay_money);
		editText_pay_password = (EditText) findViewById(R.id.transfer2_editText_pay_password);
		editText_remark = (EditText) findViewById(R.id.transfer2_editText_remark);

		layout_balance = (LinearLayout) findViewById(R.id.transfer2_layout_balance);
		layout_password = (LinearLayout) findViewById(R.id.transfer2_layout_password);
		layout_channel = (LinearLayout) findViewById(R.id.transfer2_layout_channel);

		button_pay = (Button) findViewById(R.id.transfer2_btn_pay);

		button_pay.setOnClickListener(this);

		textView_channel.setText(channel[0]);
		needPassword = true;
		// 购买支付
		if (transferType.equals("2")) {
			layout_channel.setOnClickListener(this);
			list = new ArrayList<HashMap<String, String>>();
			HashMap<String, String> data = null;
			int length = channel.length;
			for (int i = 0; i < length; i++) {
				data = new HashMap<String, String>();
				data.put("name", channel[i]);
				data.put("id", i + 1 + "");
		 		list.add(data);
			}
		}

		textView_payee_name.setText(realName);
		textView_payee_phonenum.setText(tel);

		// 指定了金额时不能手动修改
		if (!TextUtils.isEmpty(money)) {
			editText_pay_money.setText(money);
			editText_pay_money.setEnabled(false);
		}

		adapter = new ChannelSpinnerAdapter(this, list);
		textView_balance.setText("￥" + application.getTotalEb());
		editText_pay_money.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO 自动生成的方法存根
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO 自动生成的方法存根
				System.out.println("afterTextChanged");
				String temp = s.toString();
				int posDot = temp.indexOf(".");
				if (posDot > 0)
					if (temp.length() - posDot - 1 > 2)
						s.delete(posDot + 3, posDot + 4);

				System.out.println("s:" + s.toString());
				if (s.length() == 0) {
					textView_isEnough.setVisibility(View.GONE);
					return;
				}
				if (payType.equals("1")) {// 意币支付时不能超过上限
					double money = Double.parseDouble(s.toString().trim());
					if (money > (double) application.getTotalEb()) {
						textView_isEnough.setVisibility(View.VISIBLE);
						textView_isEnough.setText(eDaoClientConfig.balanceIsNotEnough);
						isBalanceEnough = false;
					} else {
						isBalanceEnough = true;
						textView_isEnough.setVisibility(View.GONE);
					}
				} else
					isBalanceEnough = true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.transfer2_layout_channel:
			showWindow(layout_channel, listView, list, textView_channel, adapter);
			break;
		case R.id.transfer2_btn_pay:
			if (checkInput()) {
				confirm();
			}
			break;
		}

	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		// if (!isSelectedChannle) {
		// Utity.showToast(getApplicationContext(), "请选择支付方式");
		// return false;
		// }
		if (TextUtils.isEmpty(application.getPayPwdValue())) {
			box_YESNO = new MessageBox_YESNO(this);
			box_YESNO.showDialog("请设置支付密码", new DialogResultListener() {

				@Override
				public void onFinishDialogResult(int result) {
					// TODO 自动生成的方法存根
					if (result == 1) {
						Bundle pBundle = new Bundle();
						pBundle.putString("type", "2");
						openActivity(PassWordManageActivity.class, pBundle);
					}
				}
			});
			return false;
		}
		if (!isBalanceEnough) {
			Utity.showToast(getApplicationContext(), eDaoClientConfig.balanceIsNotEnough);
			return false;
		}
		money = editText_pay_money.getText().toString().trim();
		if (TextUtils.isEmpty(money)) {
			Utity.showToast(getApplicationContext(), "请输入金额");
			return false;
		}
		// 购买支付时金额不能超过5W
		if (transferType.equals("2")) {
			if (Double.parseDouble(money) > 5000) {
				Utity.showToast(getApplicationContext(), "不能超过5000!");
				return false;
			}
		}
		payPwd = editText_pay_password.getText().toString().trim();
		if (needPassword && TextUtils.isEmpty(payPwd)) {
			Utity.showToast(getApplicationContext(), "请输入支付密码");
			return false;
		}
		remark = editText_remark.getText().toString().trim();
		return true;
	}

	private void confirm() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "50000");
		data.put("fromUserId", application.getUserId());
		data.put("toUserId", userId);
		data.put("payFee", money);
		data.put("payType", payType);
		data.put("payPwd", payPwd);
		data.put("remark", remark);
		// transferType:转账类型，1：意币转账，2：购买支付
		if (transferType.equals("1"))
			data.put("method", "50002");
		else
			data.put("method", "50012");

		showProgressDialog("提交中...");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() != 1) {
							box_YES = new MessageBox_YES(TransferActivity2.this);
							box_YES.showDialog(responseData.getMsg(), null);
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
							if (payType.equals("1"))
								startService(new Intent(TransferActivity2.this, Get_UserInfo_Service.class));
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
						// TODO 自动生成的方法存根
						closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}

	public void showWindow(View spinnerlayout, ListView listView, final List<HashMap<String, String>> list,
			final TextView text, final ChannelSpinnerAdapter adapter) {
		layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.myspinner_dropdown, null);
		listView = (ListView) layout.findViewById(R.id.myspinner_dropdown_listView);
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
		popupWindow.showAsDropDown(spinnerlayout, 0, 0);
		// 弹出动画
		// popupWindow.setAnimationStyle(R.style.popwin_anim_style);
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
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				text.setText(list.get(arg2).get("name"));// 设置所选的item作为下拉框的标题
				payType = list.get(arg2).get("id");
				if (payType.equals("1")) {
					needPassword = true;
					layout_balance.setVisibility(View.VISIBLE);
					layout_password.setVisibility(View.VISIBLE);
				} else {
					needPassword = false;
					layout_balance.setVisibility(View.GONE);
					layout_password.setVisibility(View.GONE);
				}
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

}
