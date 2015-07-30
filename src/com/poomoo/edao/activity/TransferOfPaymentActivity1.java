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
 * @ClassName TransferOfPaymentActivity1
 * @Description TODO 转账支付1
 * @author 李苜菲
 * @date 2015-7-30 上午11:02:41
 */
public class TransferOfPaymentActivity1 extends BaseActivity implements
		OnClickListener {

	private TextView textView_username, textView_phonenum, textView_balance,
			textView_channel;
	private EditText editText_payee_phonenum;
	private LinearLayout layout_channel, layout_payby_address_book,
			layout_payby_2dimencode;
	private Button button_confirm;

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
		setContentView(R.layout.activity_transfer_of_payment1);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_balance = (TextView) findViewById(R.id.transfer_of_payment1_textView_balance);
		textView_channel = (TextView) findViewById(R.id.transfer_of_payment1_textView_channel);

		editText_payee_phonenum = (EditText) findViewById(R.id.transfer_of_payment1_editText_phonenum);

		layout_channel = (LinearLayout) findViewById(R.id.transfer_of_payment1_layout_channel);
		layout_payby_address_book = (LinearLayout) findViewById(R.id.transfer_of_payment1_layout_payby_addressbook);
		layout_payby_2dimencode = (LinearLayout) findViewById(R.id.transfer_of_payment1_layout_payby_2dimencode);

		button_confirm = (Button) findViewById(R.id.transfer_of_payment1_btn_confirm);

		layout_channel.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

		adapter = new ChannelSpinnerAdapter(this, list);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.transfer_of_payment1_layout_channel:
			showWindow(layout_channel, listView, list, textView_channel,
					adapter);
			break;
		case R.id.transfer_of_payment1_btn_confirm:
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
