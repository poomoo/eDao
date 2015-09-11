package com.poomoo.edao.receiver;

import com.poomoo.edao.fragment.Fragment_Home;
import com.poomoo.edao.fragment.Fragment_Store;
import com.poomoo.edao.service.Get_UserInfo_Service;
import com.poomoo.edao.util.Utity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * @ClassName NetWorkConnectionChangeReceiver
 * @Description TODO 网络状态广播接收器
 * @author 李苜菲
 * @date 2015年9月11日 下午3:00:32
 */
public class NetWorkConnectionChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		System.out.println("网络状态改变");
		if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
			Utity.showToast(context, "网络不可以用");
		} else {
			context.startService(new Intent(context, Get_UserInfo_Service.class));
			if (Fragment_Home.instance != null) {
				if (!Fragment_Home.instance.inform)
					Fragment_Home.instance.getInformData();// 加载首页通知
				if (!Fragment_Home.instance.hasPics)
					Fragment_Home.instance.getAdvData();// 加载首页广告
			}
			if (Fragment_Store.instance != null) {
				if (!Fragment_Store.instance.hasPics)
					Fragment_Store.instance.getAdvData();// 加载商城广告
			}
		}
	}

}
