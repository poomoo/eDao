package com.poomoo.edao.navigation;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.edao.R;

@SuppressLint("NewApi")
public class ReturnNavigation extends Fragment {

	LinearLayout layout_return;
	TextView textView_title;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.navigation_return, container, false);

	}

	public void init() {
		layout_return = (LinearLayout) getView().findViewById(
				R.id.navigation_layout_return);
		layout_return.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
			}
		});
		textView_title = (TextView) getView().findViewById(
				R.id.navigation_textView_title);
		setNavigationTitle();
	}

	public void setNavigationTitle() {
		String[] thisactivity = getActivity().toString().split("@");
		String[] activity_name = thisactivity[0].split("\\.");
		switch (activity_name[4]) {
		case "RebateActivity":
			textView_title.setText("全国返利");
			break;
		case "PurchaseAndGetDetailActivity":
			textView_title.setText("消费领取");
			break;
		case "MywalletActivity":
			textView_title.setText("我的钱包");
			break;
		case "TransferActivity1":
			textView_title.setText("转账支付");
			break;
		case "TransferActivity2":
			textView_title.setText("转账支付");
			break;
		case "CommodityListActivity":
			textView_title.setText("商品列表");
			break;
		case "CreditManageActivity":
			textView_title.setText("信用管理");
			break;
		case "CreditCollectionActivity":
			textView_title.setText("信用收益");
			break;
		case "CreditPayActivity":
			textView_title.setText("信用支付");
			break;
		case "CreditPayByPhoneActivity":
			textView_title.setText("信用支付");
			break;
		case "CooperationActivity":
			textView_title.setText("合作加盟");
			break;
		case "AllianceApplyActivity":
			textView_title.setText("加盟商申请");
			break;
		case "PartnerApplyActivity":
			textView_title.setText("合作商户申请");
			break;
		case "DealerApplyActivity":
			textView_title.setText("经销商申请");
			break;
		case "AboutUsActivity":
			textView_title.setText("关于我们");
			break;
		case "PersonalCenterActivity":
			textView_title.setText("个人中心");
			break;
		case "CommodityInfomationActivity":
			textView_title.setText("商品详情");
			break;
		case "ConfirmOrderActivity":
			textView_title.setText("确认订单");
			break;
		case "OrderInformationActivity":
			textView_title.setText("订单详情");
			break;
		case "DealDetailActivity":
			textView_title.setText("交易明细");
			break;
		case "StoreInformationActivity":
			textView_title.setText("店铺详情");
			break;
		case "StoreEvaluateActivity":
			textView_title.setText("店铺评价");
			break;
		case "EvaluationListActivity":
			textView_title.setText("评价");
			break;
		case "CertificationActivity":
			textView_title.setText("实名认证");
			break;
		case "UploadPicsActivity":
			textView_title.setText("上传照片");
			break;
		case "GetIdentityCodeActivity":
			textView_title.setText("获取验证码");
			break;
		case "ResetPasswrodActivity":
			textView_title.setText("重置密码");
			break;
		case "RechargeActivity":
			textView_title.setText("充值");
			break;
		case "PaymentActivity":
			textView_title.setText("支付");
			break;
		case "LoveFundActivity":
			textView_title.setText("爱心基金");
			break;
		case "MyOrderActivity":
			textView_title.setText("我的订单");
			break;
		case "PassWordManageActivity":
			textView_title.setText("密码管理");
			break;
		case "WXPayEntryActivity":
			textView_title.setText("支付结果");
			break;
		case "KeyAndOperateActivity":
			textView_title.setText("经营管理");
			break;
		case "KeyManageActivity":
			textView_title.setText("秘钥管理");
			break;
		case "OperateManageActivity":
			textView_title.setText("经营管理");
			break;
		case "StoreManageActivity":
			textView_title.setText("店铺管理");
			break;
		case "BuyKeyActivity":
			textView_title.setText("购买秘钥");
			break;
		}

	}

}
