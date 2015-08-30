package com.poomoo.edao.weixinpay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.weixinpay.Constants;
import com.poomoo.edao.widget.MessageBox_YES;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;
	private MessageBox_YES box_YES;
	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_result);
		textView = (TextView) findViewById(R.id.pay_result_textView);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			System.out.println("onResp:" + resp.errCode + ":" + resp.errStr);
			String msg = "";
			if (resp.errCode == 0)
				msg = "支付成功！";
			else if (resp.errCode == -1)
				msg = "支付失败！";
			else
				msg = "取消支付！";
			textView.setText(msg);
			// box_YES = new MessageBox_YES(this);
			// box_YES.showDialog(msg, new DialogResultListener() {
			//
			// @Override
			// public void onFinishDialogResult(int result) {
			// // TODO 自动生成的方法存根
			// finish();
			// }
			// });
		}

	}
}