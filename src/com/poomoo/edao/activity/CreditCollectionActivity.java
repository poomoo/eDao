package com.poomoo.edao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.adapter.ChannelSpinnerAdapter;

/**
 * 
 * @ClassName CreditCollectionActivity
 * @Description TODO 信用收款
 * @author 李苜菲
 * @date 2015-7-30 下午3:30:21
 */
public class CreditCollectionActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_username, textView_phonenum,
			textView_collection_limit, textView_payee_name, textView_channel;
	private EditText editText_bank, editText_bankaccount,
			editText_collection_money, editText_remark;
	private LinearLayout layout_channle;
	private Button button_confirm;

	private PopupWindow popupWindow;
	private View layout, layout_all;
	private ChannelSpinnerAdapter adapter;
	private List<HashMap<String, String>> list;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_credit_collection);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_payee_name = (TextView) findViewById(R.id.credit_collection_textView_payee_name);
		textView_collection_limit = (TextView) findViewById(R.id.credit_collection_textView_collection_limit);
		textView_channel = (TextView) findViewById(R.id.credit_collection_textView_channel);

		editText_bank = (EditText) findViewById(R.id.credit_collection_editText_bank);
		editText_bankaccount = (EditText) findViewById(R.id.credit_collection_editText_bankaccount);
		editText_collection_money = (EditText) findViewById(R.id.credit_collection_editText_collection_money);
		editText_remark = (EditText) findViewById(R.id.credit_collection_editText_remark);

		layout_channle = (LinearLayout) findViewById(R.id.credit_collection_layout_channel);
		layout_all = (LinearLayout) findViewById(R.id.credit_collection_layout);

		button_confirm = (Button) findViewById(R.id.credit_collection_btn_confirm);

		layout_channle.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

		list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> hashMap_sale = new HashMap<String, String>();
		for (int i = 0; i < 5; i++) {
			hashMap_sale = new HashMap<String, String>();
			hashMap_sale.put("name", "意道：费率0.0050");
			hashMap_sale.put("value", "channel");
			list.add(hashMap_sale);
		}
		textView_channel.setText(list.get(0).get("name"));

		adapter = new ChannelSpinnerAdapter(this, list);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.credit_collection_layout_channel:
			showWindow(layout_channle, listView, list, textView_channel,
					adapter);
			break;
		case R.id.credit_collection_btn_confirm:
			break;
		}

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

}
