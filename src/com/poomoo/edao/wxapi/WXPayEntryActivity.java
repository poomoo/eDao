package com.poomoo.edao.wxapi;

import com.poomoo.edao.R;
import com.poomoo.edao.activity.BaseActivity;
import com.poomoo.edao.activity.BuyKeyActivity;
import com.poomoo.edao.activity.PaymentActivity;
import com.poomoo.edao.activity.RechargeActivity;
import com.poomoo.edao.service.Get_UserInfo_Service;
import com.poomoo.edao.weixinpay.Constants;
import com.poomoo.edao.widget.dialog.OptAnimationLoader;
import com.poomoo.edao.widget.dialog.SuccessTickView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	private IWXAPI api;
	private TextView textView;
	private FrameLayout mErrorFrame;
	private FrameLayout mSuccessFrame;
	private SuccessTickView mSuccessTick;
	private ImageView mErrorX;
	private View mSuccessLeftMask;
	private View mSuccessRightMask;
	private AnimationSet mSuccessLayoutAnimSet;
	private Animation mErrorInAnim;
	private AnimationSet mErrorXInAnim;
	private Animation mSuccessBowAnim;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_result);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		textView = (TextView) findViewById(R.id.pay_result_textView);
		mErrorFrame = (FrameLayout) findViewById(R.id.pay_result_error_frame);
		mErrorX = (ImageView) mErrorFrame.findViewById(R.id.pay_result_error_x);
		mSuccessFrame = (FrameLayout) findViewById(R.id.pay_result_success_frame);
		mSuccessTick = (SuccessTickView) mSuccessFrame.findViewById(R.id.pay_result_success_tick);
		mSuccessLeftMask = mSuccessFrame.findViewById(R.id.pay_result_mask_left);
		mSuccessRightMask = mSuccessFrame.findViewById(R.id.pay_result_mask_right);

		mErrorInAnim = OptAnimationLoader.loadAnimation(this, R.anim.error_frame_in);
		mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(this, R.anim.error_x_in);
		mSuccessBowAnim = OptAnimationLoader.loadAnimation(this, R.anim.success_bow_roate);
		mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(this, R.anim.success_mask_layout);

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
				mSuccessFrame.setVisibility(View.VISIBLE);
				// initial rotate layout of success mask
				mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
				mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));

				mSuccessTick.startTickAnim();
				mSuccessRightMask.startAnimation(mSuccessBowAnim);

				msg = "支付成功！";
				Intent intent = new Intent(this, Get_UserInfo_Service.class);
				startService(intent);
			} else if (resp.errCode == -1) {
				mErrorFrame.setVisibility(View.VISIBLE);
				mErrorFrame.startAnimation(mErrorInAnim);
				mErrorX.startAnimation(mErrorXInAnim);
				msg = "支付失败！";
			} else {
				// mErrorFrame.setVisibility(View.VISIBLE);
				// mErrorFrame.startAnimation(mErrorInAnim);
				// mErrorX.startAnimation(mErrorXInAnim);
				mSuccessFrame.setVisibility(View.VISIBLE);
				mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
				mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));

				mSuccessTick.startTickAnim();
				mSuccessRightMask.startAnimation(mSuccessBowAnim);
				msg = "取消支付！";
			}
			textView.setText(msg);
		}

	}
}