package com.poomoo.edao.wxapi;

import com.poomoo.edao.R;
import com.poomoo.edao.activity.BaseActivity;
import com.poomoo.edao.activity.BuyKeyActivity;
import com.poomoo.edao.activity.PaymentActivity;
import com.poomoo.edao.activity.RechargeActivity;
import com.poomoo.edao.service.Get_UserInfo_Service;
import com.poomoo.edao.weixinpay.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	private IWXAPI api;
	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_result);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		textView = (TextView) findViewById(R.id.pay_result_textView);
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		if (RechargeActivity.instance != null)
			RechargeActivity.instance.finish();
		if (PaymentActivity.instance != null)
			PaymentActivity.instance.finish();
		if (BuyKeyActivity.instance != null)
			BuyKeyActivity.instance.finish();
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
		System.out.println("onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			System.out.println("onResp:" + resp.errCode + ":" + resp.errStr);
			String msg = "";
			if (resp.errCode == 0) {
				msg = "支付成功！";
				Intent intent = new Intent(this, Get_UserInfo_Service.class);
				startService(intent);
			} else if (resp.errCode == -1)
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