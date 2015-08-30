package com.poomoo.edao.activity;

import java.io.StringReader;
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

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.weixinpay.Constants;
import com.poomoo.edao.weixinpay.MD5;
import com.poomoo.edao.weixinpay.Util;
import com.poomoo.edao.widget.DialogResultListener;
import com.poomoo.edao.widget.MessageBox_YES;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 
 * @ClassName RechargeActivity
 * @Description TODO 充值
 * @author 李苜菲
 * @date 2015-8-27 下午2:20:26
 */
public class RechargeActivity extends BaseActivity implements OnClickListener,
		IWXAPIEventHandler {

	private EditText editText_pay_money;
	private Button button_pay;

	private String money = "";
	private eDaoClientApplication application = null;
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private MessageBox_YES box_YES;
	private IWXAPI api;
	private String orderId = "";
	private PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private Map<String, String> resultunifiedorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
		weixin();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_pay_money = (EditText) findViewById(R.id.recharge_editText_pay_money);

		button_pay = (Button) findViewById(R.id.recharge_btn_pay);

		button_pay.setOnClickListener(this);
	}

	private void weixin() {
		// TODO 自动生成的方法存根
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(getIntent(), this);
		req = new PayReq();
		msgApi.registerApp(Constants.APP_ID);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.recharge_btn_pay:
			if (checkInput()) {
				confirm();
			}
			break;
		}

	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		money = editText_pay_money.getText().toString().trim();
		if (TextUtils.isEmpty(money)) {
			Utity.showToast(getApplicationContext(), "请输入金额");
			return false;
		}
		return true;
	}

	private void confirm() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "50000");
		data.put("method", "50009");
		data.put("fromUserId", application.getUserId());
		data.put("payFee", money);

		showProgressDialog("提交中...");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

					@Override
					public void onFinish(final ResponseData responseData) {
						// TODO 自动生成的方法存根
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO 自动生成的方法存根
								if (responseData.getRsCode() != 1) {
									closeProgressDialog();
									box_YES = new MessageBox_YES(
											RechargeActivity.this);
									box_YES.showDialog(responseData.getMsg(),
											null);
								} else {
									try {
										JSONObject result = new JSONObject(
												responseData.getJsonData()
														.toString());
										orderId = result.getString("ordersId");
										GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
										getPrepayId.execute();
									} catch (JSONException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
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
							}
						});
					}
				});
	}

	private void sendPayReq() {
		System.out.println("resultunifiedorder");
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			resultunifiedorder = result;
			System.out.println("onPostExecute resultunifiedorder:"
					+ resultunifiedorder);
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
			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			byte[] buf = Util.httpPost(url, entity);
			String content = new String(buf);
			System.out.println("微信返回:" + content);
			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "leyidao"));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url",
					eDaoClientConfig.wxReUrl));
			packageParams.add(new BasicNameValuePair("out_trade_no", orderId));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", Utity
					.getLocalHostIp()));
			Double fee = Double.parseDouble(money) * 100;
			packageParams.add(new BasicNameValuePair("total_fee", String
					.valueOf(Utity.subZeroAndDot(String.valueOf(fee)))));
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

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
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

		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
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
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
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

	@Override
	public void onReq(BaseReq arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO 自动生成的方法存根
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			System.out.println("onResp:" + resp.errCode + ":" + resp.errStr);
			closeProgressDialog();
			String msg = "";
			if (resp.errCode == 0)
				msg = "支付成功！";
			else if (resp.errCode == -1)
				msg = "支付失败！";
			else
				msg = "取消支付！";
			box_YES = new MessageBox_YES(this);
			box_YES.showDialog(msg, new DialogResultListener() {

				@Override
				public void onFinishDialogResult(int result) {
					// TODO 自动生成的方法存根
					finish();
				}
			});
		}
	}
}
