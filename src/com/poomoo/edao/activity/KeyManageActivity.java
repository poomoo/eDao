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
import com.poomoo.edao.adapter.KeyManage_Apply_ListViewAdapter;
import com.poomoo.edao.adapter.KeyManage_Used_ListViewAdapter;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.KeyManageData;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * 
 * @ClassName KeyManageActivity
 * @Description TODO 秘钥管理
 * @author 李苜菲
 * @date 2015年8月30日 下午10:24:03
 */
public class KeyManageActivity extends BaseActivity implements OnClickListener {
	private RadioButton button_apply, button_used, button_notUsed;
	private ImageView imageView_return;
	private TextView textView_buy_key, textView_content;
	private MyListView listView;

	private KeyManage_Apply_ListViewAdapter adapter_apply;
	private KeyManage_Used_ListViewAdapter adapter_used;

	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int apply_curPage = 1, apply_pageSize = 10, used_curPage = 1, used_pageSize = 10;
	private boolean apply_isFirst = true, used_isFirst = true;// 是否第一次加载
	private eDaoClientApplication application;
	private static final String apply = "1", used = "2", notUsed = "3";
	private List<KeyManageData> list_apply, list_used;
	private String content = "", tel = "", realName = "";
	private boolean isFresh = false;// 是否刷新标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_key_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		imageView_return = (ImageView) findViewById(R.id.key_manage_imageView_back);
		textView_buy_key = (TextView) findViewById(R.id.key_manage_textView_buy_key);
		textView_content = (TextView) findViewById(R.id.key_manage_textView_content);
		listView = (MyListView) findViewById(R.id.key_manage_listView);

		button_apply = (RadioButton) findViewById(R.id.key_manage_radioButton_apply);
		button_used = (RadioButton) findViewById(R.id.key_manage_radioButton_used);
		button_notUsed = (RadioButton) findViewById(R.id.key_manage_radioButton_notUsed);

		imageView_return.setOnClickListener(this);
		textView_buy_key.setOnClickListener(this);
		button_apply.setOnClickListener(this);
		button_used.setOnClickListener(this);
		button_notUsed.setOnClickListener(this);

		list_apply = new ArrayList<KeyManageData>();
		list_used = new ArrayList<KeyManageData>();
		adapter_apply = new KeyManage_Apply_ListViewAdapter(KeyManageActivity.this, list_apply);
		adapter_used = new KeyManage_Used_ListViewAdapter(KeyManageActivity.this, list_used);
		listView.setAdapter(adapter_apply);
		showProgressDialog();
		getApplyData();
		listView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getApplyData();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.key_manage_imageView_back:
			finish();
			break;
		case R.id.key_manage_textView_buy_key:
			openActivity(BuyKeyActivity.class);
			break;
		case R.id.key_manage_radioButton_apply:
			textView_content.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			if (apply_isFirst) {
				showProgressDialog();
				getApplyData();
			}
			listView.setAdapter(adapter_apply);
			listView.setonRefreshListener(new OnRefreshListener() {
				public void onRefresh() {
					getApplyData();
				}
			});
			break;
		case R.id.key_manage_radioButton_used:
			textView_content.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			if (used_isFirst) {
				showProgressDialog();
				getUsedData();
			}
			listView.setAdapter(adapter_used);
			listView.setonRefreshListener(new OnRefreshListener() {
				public void onRefresh() {
					getUsedData();
				}
			});
			break;
		case R.id.key_manage_radioButton_notUsed:
			textView_content.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			getNotUsedData();
			break;
		}
	}

	private void getApplyData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "20000");
		data.put("method", "20006");
		data.put("userId", application.getUserId());
		data.put("status", apply);
		data.put("currPage", apply_curPage);
		data.put("pageSize", apply_pageSize);

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
									KeyManageData data = new KeyManageData();
									data = gson.fromJson(pager.getJSONObject(i).toString(), KeyManageData.class);
									list_apply.add(data);
								}
								System.out.println("申请秘钥list:" + list_apply.size());
								if (apply_isFirst) {
									apply_isFirst = false;
								}
								adapter_apply.notifyDataSetChanged();
								apply_curPage += 10;
								apply_pageSize += 10;

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

	private void getUsedData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "20000");
		data.put("method", "20006");
		data.put("userId", application.getUserId());
		data.put("status", used);
		data.put("currPage", used_curPage);
		data.put("pageSize", used_pageSize);

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
									KeyManageData data = new KeyManageData();
									data = gson.fromJson(pager.getJSONObject(i).toString(), KeyManageData.class);
									list_used.add(data);
								}
								if (used_isFirst) {
									used_isFirst = false;
								}
								adapter_used.notifyDataSetChanged();
								used_curPage += 10;
								used_pageSize += 10;

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

	private void getNotUsedData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "20000");
		data.put("method", "20006");
		data.put("userId", application.getUserId());
		data.put("status", notUsed);

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
								content = result.getString("content");
								textView_content.setText(content);
							} catch (JSONException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							}

						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						}
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
			switch (v.getId()) {
			case R.id.item_key_manage_apply_button_agree:
				check(position, 1);
				break;
			case R.id.item_key_manage_apply_button_refuse:
				check(position, 0);
				break;

			}
		}

	}

	private void check(int position, int status) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "20000");
		data.put("method", "20004");
		data.put("userId", list_apply.get(position).getUserId());
		data.put("checkUserId", application.getUserId());
		data.put("status", status);

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				closeProgressDialog();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						if (responseData.getRsCode() == 1) {
							showProgressDialog();
							apply_curPage = 1;
							apply_pageSize = 10;
							list_apply.clear();
							getApplyData();
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						}
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
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}

				});
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
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("请稍后...");
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
}
