package com.poomoo.edao.activity;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

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
import com.poomoo.edao.weixinpay.Constants;
import com.poomoo.edao.weixinpay.MD5;
import com.poomoo.edao.weixinpay.Util;
import com.poomoo.edao.widget.DialogResultListener;
import com.poomoo.edao.widget.MessageBox_YES;
import com.poomoo.edao.widget.MessageBox_YESNO;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
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
 * @ClassName PaymentActivity
 * @Description TODO 支付
 * @author 李苜菲
 * @date 2015年8月29日 下午9:02:54
 */
public class PaymentActivity extends BaseActivity implements OnClickListener {

	private TextView textView_balance, textView_channel, textView_isEnough, textView_between1, textView_between2;
	private EditText editText_pay_money, editText_pay_password, editText_remark;
	private LinearLayout layout_channel, layout_balance, layout_pay_password;
	private Button button_pay;

	private PopupWindow popupWindow;
	private View layout;
	private ChannelSpinnerAdapter adapter;
	private List<HashMap<String, String>> list;
	private ListView listView;

	private eDaoClientApplication application = null;
	private String money = "", payType = "1", payPwd = "", remark = "", orderId = "", referrerTel = "",
			referrerUserId = "", referrerName = "", joinType = "", areaId = "";
	private static final String[] channel = new String[] { "意币支付", "微信支付" };
	private boolean needPassword = true, isBalanceEnough = true;
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private MessageBox_YES box_YES;
	private MessageBox_YESNO box_YESNO;
	private PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private Map<String, String> resultunifiedorder;
	public static PaymentActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		getIntentData();
		application = (eDaoClientApplication) getApplication();
		instance = this;
		init();
		weixin();
	}

	private void getIntentData() {
		// TODO 自动生成的方法存根

		money = getIntent().getExtras().getString("money");
		areaId = getIntent().getExtras().getString("areaId");
		referrerTel = getIntent().getExtras().getString("referrerTel");
		referrerUserId = getIntent().getExtras().getString("referrerUserId");
		referrerName = getIntent().getExtras().getString("referrerName");
		joinType = getIntent().getExtras().getString("joinType");
	}

	private void weixin() {
		// TODO 自动生成的方法存根
		req = new PayReq();
		msgApi.registerApp(Constants.APP_ID);
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_balance = (TextView) findViewById(R.id.payment_textView_balance);
		textView_channel = (TextView) findViewById(R.id.payment_textView_channel);
		textView_isEnough = (TextView) findViewById(R.id.payment_textView_isEnough);
		textView_between1 = (TextView) findViewById(R.id.payment_textView_between1);
		textView_between2 = (TextView) findViewById(R.id.payment_textView_between2);

		editText_pay_money = (EditText) findViewById(R.id.payment_editText_pay_money);
		editText_pay_password = (EditText) findViewById(R.id.payment_editText_pay_password);
		editText_remark = (EditText) findViewById(R.id.payment_editText_remark);

		layout_channel = (LinearLayout) findViewById(R.id.payment_layout_channel);
		layout_balance = (LinearLayout) findViewById(R.id.payment_layout_balance);
		layout_pay_password = (LinearLayout) findViewById(R.id.payment_layout_pay_password);

		button_pay = (Button) findViewById(R.id.payment_btn_pay);

		button_pay.setOnClickListener(this);

		list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> data = null;
		int length = channel.length;
		for (int i = 0; i < length; i++) {
			data = new HashMap<String, String>();
			data.put("name", channel[i]);
			data.put("id", i + 1 + "");
			list.add(data);
		}

		textView_channel.setText(channel[0]);
		layout_channel.setOnClickListener(this);
		// 指定意币转账后不能选择支付类型
		// if (TextUtils.isEmpty(payType)) {
		// payType = "1";
		// } else {
		// isSelectedChannle = true;
		// }
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
		case R.id.payment_layout_channel:
			showWindow(layout_channel, listView, list, textView_channel, adapter);
			break;
		case R.id.payment_btn_pay:
			if (checkInput()) {
				apply();
			}
			break;
		}

	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
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
		payPwd = editText_pay_password.getText().toString().trim();
		if (needPassword && TextUtils.isEmpty(payPwd)) {
			Utity.showToast(getApplicationContext(), "请输入支付密码");
			return false;
		}
		remark = editText_remark.getText().toString().trim();
		return true;
	}

	/**
	 * 
	 * 
	 * @Title: apply
	 * @Description: TODO 提交申请
	 * @author 李苜菲
	 * @return
	 * @return void
	 * @throws @date
	 *             2015-8-17下午4:53:39
	 */
	private void apply() {
		progressDialog = null;
		showProgressDialog("提交申请中...");
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20003");
		data.put("userId", application.getUserId());
		data.put("areaId", areaId);
		data.put("joinType", joinType);
		data.put("referrerTel", referrerTel);
		data.put("referrerUserId", referrerUserId);
		data.put("referrerName", referrerName);

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
							confirm();
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

	private void confirm() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "50000");
		data.put("method", "50010");
		data.put("fromUserId", application.getUserId());
		data.put("payFee", money);
		data.put("payType", payType);
		data.put("payPwd", payPwd);
		data.put("remark", remark);
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (responseData.getRsCode() != 1) {
							closeProgressDialog();
							box_YES = new MessageBox_YES(PaymentActivity.this);
							box_YES.showDialog(responseData.getMsg(), null);
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
							if (payType.equals("1")) {
								closeProgressDialog();
								startService(new Intent(PaymentActivity.this, Get_UserInfo_Service.class));
								finish();
							} else {
								try {
									JSONObject result = new JSONObject(responseData.getJsonData().toString());
									orderId = result.getString("ordersId");
									GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
									getPrepayId.execute();
								} catch (JSONException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
							}

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
					layout_pay_password.setVisibility(View.VISIBLE);
					textView_between1.setVisibility(View.VISIBLE);
					textView_between2.setVisibility(View.VISIBLE);

				} else {
					needPassword = false;
					layout_balance.setVisibility(View.GONE);
					layout_pay_password.setVisibility(View.GONE);
					textView_between1.setVisibility(View.GONE);
					textView_between2.setVisibility(View.GONE);
				}
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
	 * @throws @date
	 *             2015-8-12下午1:23:53
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
	 * @throws @date
	 *             2015-8-12下午1:24:43
	 */
	private void closeProgressDialog() {
		if (progressDialog != null)
			progressDialog.dismiss();
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			resultunifiedorder = result;
			System.out.println("onPostExecute resultunifiedorder:" + resultunifiedorder);
			genPayReq();
			sendPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			System.out.println("doInBackground");
			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			byte[] buf = Util.httpPost(url, entity);
			String content = new String(buf);
			System.out.println("微信返回:" + content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	private void sendPayReq() {
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "充值"));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", eDaoClientConfig.wxReUrl));
			packageParams.add(new BasicNameValuePair("out_trade_no", orderId));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", Utity.getLocalHostIp()));
			Double fee = Double.parseDouble(money) * 100;
			packageParams
					.add(new BasicNameValuePair("total_fee", String.valueOf(Utity.subZeroAndDot(String.valueOf(fee)))));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);
			xmlstring = new String(xmlstring.getBytes("UTF-8"), "ISO-8859-1");

			return xmlstring;

		} catch (Exception e) {
			System.out.println("genProductArgs异常" + e.getMessage());
			return null;
		}

	}

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		Log.e("orion", signParams.toString());

	}
}
