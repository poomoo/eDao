package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.adapter.ChannelSpinnerAdapter;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName AllianceApplyActivity
 * @Description TODO 加盟商申请
 * @author 李苜菲
 * @date 2015年7月30日 下午11:23:58
 */
public class AllianceApplyActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener {
	private TextView textView_username, textView_phonenum, textView_zone,
			textView_money, textView_merchant_name;
	private EditText editText_merchant_num;
	private LinearLayout layout_zone;
	private Button button_confirm;

	private PopupWindow popupWindow;
	private View layout;
	private ChannelSpinnerAdapter adapter_zone, adapter_merchant_type;
	private List<HashMap<String, String>> list_zone, list_merchant_type;
	private ListView listView;
	private ProgressDialog progressDialog;
	private Gson gson = new Gson();
	private String merchant_num = "", referrerUserId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alliance_apply);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();

		test();
	}

	private void test() {
		// TODO 自动生成的方法存根
		Map<String, String> data = new HashMap<String, String>();
		data.put("bizName", "20000");
		data.put("method", "20001");
		data.put("joinType", "1");
		data.put("areaId", "");
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
				new HttpCallbackListener() {

					@Override
					public void onFinish(final ResponseData responseData) {
						// TODO 自动生成的方法存根
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO 自动生成的方法存根
								if (responseData.getRsCode() == 1) {
									try {
										JSONObject result = new JSONObject(
												responseData.getJsonData()
														.toString());
										textView_money.setText(result
												.getString("price"));
									} catch (JSONException e) {
									}
								} else {
									Utity.showToast(getApplicationContext(),
											responseData.getMsg());
								}

							}
						});
					}

					@Override
					public void onError(Exception e) {
						// TODO 自动生成的方法存根

					}
				});
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_zone = (TextView) findViewById(R.id.alliance_textView_zone);
		textView_money = (TextView) findViewById(R.id.alliance_textView_money);
		textView_merchant_name = (TextView) findViewById(R.id.alliance_textView_merchant_name);

		editText_merchant_num = (EditText) findViewById(R.id.alliance_editText_merchant_num);

		layout_zone = (LinearLayout) findViewById(R.id.alliance_layout_zone);

		button_confirm = (Button) findViewById(R.id.alliance_btn_confirm);

		layout_zone.setOnClickListener(this);
		button_confirm.setOnClickListener(this);
		editText_merchant_num.setOnFocusChangeListener(this);

		list_zone = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap_sale = new HashMap<String, String>();
		for (int i = 0; i < 5; i++) {
			hashMap_sale = new HashMap<String, String>();
			hashMap_sale.put("name", "贵州" + i);
			hashMap_sale.put("value", "zone");
			list_zone.add(hashMap_sale);
		}
		textView_zone.setText(list_zone.get(0).get("name"));
		adapter_zone = new ChannelSpinnerAdapter(this, list_zone);

		list_merchant_type = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap_sale1 = new HashMap<String, String>();
		for (int i = 0; i < 5; i++) {
			System.out.println("i:" + i);
			hashMap_sale1 = new HashMap<String, String>();
			hashMap_sale1.put("name", "电商" + i);
			hashMap_sale1.put("value", "type");
			list_merchant_type.add(hashMap_sale1);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.alliance_layout_zone:
			showWindow(layout_zone, listView, list_zone, textView_zone,
					adapter_zone);
			break;
		case R.id.alliance_btn_confirm:
			break;
		}

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
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("登录中...");
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

	public void showWindow(View spinnerlayout, ListView listView,
			final List<HashMap<String, String>> list, final TextView text,
			final ChannelSpinnerAdapter adapter) {
		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.myspinner_dropdown, null);
		listView = (ListView) layout
				.findViewById(R.id.myspinner_dropdown_listView);
		listView.setAdapter(adapter);

		popupWindow = new PopupWindow(spinnerlayout);
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(spinnerlayout.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(spinnerlayout, 0, 0);
		// 弹出动画
		// popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// spinnerlayout
				// .setBackgroundResource(R.drawable.preference_single_item);
			}

		});
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				text.setText(list.get(arg2).get("name"));// 设置所选的item作为下拉框的标题
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO 自动生成的方法存根
		merchant_num = editText_merchant_num.getText().toString().trim();
		if (hasFocus) {
			editText_merchant_num.setText("");
		}
		if (!hasFocus && merchant_num.length() != 11) {
			Utity.showToast(getApplicationContext(), "手机号长度不对");
			// v.setFocusable(true);
			// v.setFocusableInTouchMode(true);
			// v.requestFocus();
		}
		if (!hasFocus && merchant_num.length() == 11) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("bizName", "20000");
			data.put("method", "20002");
			data.put("referrerTel", merchant_num);

			HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url,
					new HttpCallbackListener() {

						@Override
						public void onFinish(final ResponseData responseData) {
							// TODO 自动生成的方法存根
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO 自动生成的方法存根
									if (responseData.getRsCode() == 1) {
										try {
											JSONObject result = new JSONObject(
													responseData.getJsonData()
															.toString());
											textView_merchant_name.setText(result
													.getString("referrerName"));
											referrerUserId = result
													.getString("referrerUserId");
										} catch (JSONException e) {
										}
									} else {
										Utity.showToast(
												getApplicationContext(),
												responseData.getMsg());
									}

								}
							});
						}

						@Override
						public void onError(Exception e) {
							// TODO 自动生成的方法存根

						}
					});

		}

	}
}
