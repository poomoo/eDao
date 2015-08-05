package com.poomoo.edao.activity;

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
 * @ClassName MywalletActivity
 * @Description TODO 我的钱包
 * @author 李苜菲
 * @date 2015-7-29 下午4:31:49
 */
public class MywalletActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum, textView_balance,
			textView_hangding_charge, textView_hangding_toplimit,
			textView_bankname, textView_branch_bankname, textView_bankaccount,
			textView_channel;
	private EditText editText_handing_money;
	private LinearLayout layout_channle;
	private Button button_recharge, button_handing;

	private PopupWindow popupWindow;
	private View layout;
	private ChannelSpinnerAdapter adapter;
	private List<HashMap<String, String>> list;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_mywallet);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_balance = (TextView) findViewById(R.id.mywallet_textView_balance);
		textView_hangding_charge = (TextView) findViewById(R.id.mywallet_textView_handing_charge);
		textView_hangding_toplimit = (TextView) findViewById(R.id.mywallet_textView_handing_toplimit);
		textView_bankname = (TextView) findViewById(R.id.mywallet_textView_bankname);
		textView_branch_bankname = (TextView) findViewById(R.id.mywallet_textView_branchbankname);
		textView_bankaccount = (TextView) findViewById(R.id.mywallet_textView_bankaccount);
		textView_channel = (TextView) findViewById(R.id.mywallet_textView_channel);

		editText_handing_money = (EditText) findViewById(R.id.mywallet_editText_handing_money);

		layout_channle = (LinearLayout) findViewById(R.id.mywallet_layout_channel);

		button_recharge = (Button) findViewById(R.id.mywallet_btn_recharge);
		button_handing = (Button) findViewById(R.id.mywallet_btn_handing);

		layout_channle.setOnClickListener(this);
		button_recharge.setOnClickListener(this);
		button_handing.setOnClickListener(this);

		adapter = new ChannelSpinnerAdapter(this, list);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.mywallet_layout_channel:
			showWindow(layout_channle, listView, list, textView_channel,
					adapter);
			break;
		case R.id.mywallet_btn_recharge:
			break;
		case R.id.mywallet_btn_handing:
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
