package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.OperateManage_ListViewAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.OperateManageData;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.popupwindow.Operate_Manage_PopupWindow;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.MyListView;
import com.poomoo.edao.widget.MyListView.OnRefreshListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Intents.Insert;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 * @ClassName OperateManageActivity
 * @Description TODO 经营管理
 * @author 李苜菲
 * @date 2015年8月31日 下午10:01:30
 */
public class OperateManageActivity extends BaseActivity {
	private MyListView listView;
	private OperateManage_ListViewAdapter adapter;
	private List<OperateManageData> list;

	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int curPage = 1, pageSize = 10;
	private boolean isFirst = true;// 是否第一次加载
	private eDaoClientApplication application = null;

	private String tel = "", realName = "";
	private Operate_Manage_PopupWindow operate_Manage_PopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_operate_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		listView = (MyListView) findViewById(R.id.operate_manage_listView);

		list = new ArrayList<OperateManageData>();
		adapter = new OperateManage_ListViewAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getData();
			}
		});

		showProgressDialog("请稍后...");
		getData();
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "20000");
		data.put("method", "20010");
		data.put("userId", application.getUserId());
		data.put("currPage", curPage);
		data.put("pageSize", pageSize);

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (responseData.getRsCode() == 1 && responseData.getJsonData().length() > 0) {
							try {
								JSONObject result = new JSONObject(responseData.getJsonData().toString());

								JSONArray pager = result.getJSONArray("records");
								int length = pager.length();
								for (int i = 0; i < length; i++) {
									OperateManageData data = new OperateManageData();
									data = gson.fromJson(pager.getJSONObject(i).toString(), OperateManageData.class);
									list.add(data);
								}
								if (isFirst) {
									isFirst = false;
								}
								adapter.notifyDataSetChanged();

								curPage += 1;
						
							} catch (JSONException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						}
						listView.onRefreshComplete();
					}

				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						listView.onRefreshComplete();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}

				});
			}
		});
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
			System.out.println("点击item:" + position);
			tel = list.get(position).getTel();
			realName = list.get(position).getRealName();
			show();
		}

	}

	private void show() {
		// 实例化SelectPicPopupWindow
		operate_Manage_PopupWindow = new Operate_Manage_PopupWindow(OperateManageActivity.this, itemsOnClick, realName,
				tel);
		// 显示窗口
		operate_Manage_PopupWindow.showAtLocation(
				OperateManageActivity.this.findViewById(R.id.activity_operate_manage_layout), Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick = new OnClickListener() {

		@Override
		public void onClick(View view) {
			operate_Manage_PopupWindow.dismiss();
			switch (view.getId()) {
			case R.id.popup_key_manage_textView_save:
				Intent intent_save = new Intent(Intent.ACTION_INSERT);
				intent_save.setType("vnd.android.cursor.dir/person");
				intent_save.setType("vnd.android.cursor.dir/contact");
				intent_save.setType("vnd.android.cursor.dir/raw_contact");
				// 添加姓名
				intent_save.putExtra(Insert.NAME, realName);
				// 添加手机
				intent_save.putExtra(Insert.PHONE_TYPE, Phone.TYPE_MOBILE);
				intent_save.putExtra(Insert.PHONE, tel);
				startActivity(intent_save);
				break;
			case R.id.popup_key_manage_textView_dial:
				Intent intent_dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tel));
				startActivity(intent_dial);
				break;
			}
		}
	};

}
