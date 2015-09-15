package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.fragment.Fragment_Deleted;
import com.poomoo.edao.fragment.Fragment_Payed;
import com.poomoo.edao.fragment.Fragment_UnPayed;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

/**
 * 
 * @ClassName MyOrderActivity
 * @Description TODO 我的订单
 * @author 李苜菲
 * @date 2015年8月30日 下午5:11:37
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener {
	private RadioButton button_unpay, button_payed, button_delete;

	private Fragment_Payed fragment_Payed;
	private Fragment_UnPayed fragment_UnPayed;
	private Fragment_Deleted fragment_Deleted;
	private Fragment curFragment;

	private Gson gson = new Gson();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_order);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		setDefaultFragment();
		init();
	}

	private void setDefaultFragment() {
		// TODO 自动生成的方法存根
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragment_Payed = new Fragment_Payed();
		curFragment = fragment_Payed;
		fragmentTransaction.add(R.id.my_order_layout, fragment_Payed);
		fragmentTransaction.commit();
	}

	private void init() {
		// TODO 自动生成的方法存根
		button_payed = (RadioButton) findViewById(R.id.my_order_radioButton_payed);
		button_unpay = (RadioButton) findViewById(R.id.my_order_radioButton_nopay);
		button_delete = (RadioButton) findViewById(R.id.my_order_radioButton_delete);
		button_unpay.setOnClickListener(this);
		button_payed.setOnClickListener(this);
		button_delete.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.my_order_radioButton_payed:
			if (fragment_Payed == null)
				fragment_Payed = new Fragment_Payed();
			switchFragment(fragment_Payed);
			curFragment = fragment_Payed;
			break;
		case R.id.my_order_radioButton_nopay:
			if (fragment_UnPayed == null)
				fragment_UnPayed = new Fragment_UnPayed();
			switchFragment(fragment_UnPayed);
			curFragment = fragment_UnPayed;
			break;
		case R.id.my_order_radioButton_delete:
			if (fragment_Deleted == null)
				fragment_Deleted = new Fragment_Deleted();
			switchFragment(fragment_Deleted);
			curFragment = fragment_Deleted;
			break;

		}
	}

	public class MyListener implements OnClickListener {
		private int position = 0;

		public MyListener(int position) {
			super();
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getTag().equals("evaluate")) {

			} else {
				Fragment_UnPayed.instance.showProgressDialog();
				confirm(position);
			}
		}

	}

	public void switchFragment(Fragment to) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (!to.isAdded()) { // 先判断是否被add过
			fragmentTransaction.hide(curFragment).add(R.id.my_order_layout, to); // 隐藏当前的fragment，add下一个到Activity中
		} else {
			fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
		}
		fragmentTransaction.commit();
	}

	public void confirm(int position) {
		// TODO 自动生成的方法存根
		System.out.println("调用confirm");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "50000");
		data.put("method", "50004");
		data.put("ordersId", Fragment_UnPayed.instance.list.get(position).getOrdersId());
		data.put("opType", 1);
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根

						if (responseData.getRsCode() == 1) {
							Fragment_UnPayed.instance.getData(eDaoClientConfig.status);
						} else {
							Fragment_UnPayed.instance.closeProgressDialog();
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
						Fragment_UnPayed.instance.closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}
}
