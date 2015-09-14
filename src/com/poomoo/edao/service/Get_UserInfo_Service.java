package com.poomoo.edao.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.Message;

import com.google.gson.Gson;
import com.poomoo.edao.activity.NavigationActivity;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.UserInfoData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;

public class Get_UserInfo_Service extends Service {
	private Gson gson = new Gson();
	private eDaoClientApplication application;
	private SharedPreferences userInfo = null;
	private Editor editor;

	public void onCreate() {
		super.onCreate();
	};

	public void onStart(Intent intent, int startId) {
		System.out.println("服务启动");
		application = (eDaoClientApplication) getApplication();
		userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		editor = userInfo.edit();
		getUserInfoData();
	};

	private void getUserInfoData() {
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "10000");
		data.put("method", "10013");
		data.put("userId", application.getUserId());
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				System.out.println("getUserInfoData onFinish");
				// TODO 自动生成的方法存根
				if (responseData.getRsCode() == 1) {
					UserInfoData infoData = new UserInfoData();
					infoData = gson.fromJson(responseData.getJsonData(), UserInfoData.class);
					application.setTotalEb(infoData.getTotalEb());
					application.setTotalGold(infoData.getTotalGold());
					application.setTotalIntegral(infoData.getTotalIntegral());
					application.setPayPwdValue(infoData.getPayPwdValue());
					application.setRealNameAuth(infoData.getRealNameAuth());
					application.setRealName(infoData.getRealName());
					application.setType(infoData.getType());
					application.setJoinType(infoData.getJoinType());
					application.setJoinStatus(infoData.getJoinStatus());
					application.setQuickmarkPic(infoData.getQuickmarkPic());
					editor.putString("realName", application.getRealName());
					editor.putString("realNameAuth", application.getRealNameAuth());
					editor.putString("type", application.getType());
					editor.putString("payPwdValue", application.getPayPwdValue());
					editor.putString("joinType", application.getJoinType());
					editor.putString("joinStatus", application.getJoinStatus());
					editor.commit();

					Message msg = new Message();
					msg.what = eDaoClientConfig.freshFlag;
					NavigationActivity.handler.sendMessage(msg);
					System.out.println("返回成功1");
					stopSelf();
				} else
					stopSelf();
				// getUserInfoData();
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				System.out.println("getUserInfoData onError:" + e.getMessage());
				// getUserInfoData();
			}
		});
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		System.out.println("服务销毁");
	}

}
