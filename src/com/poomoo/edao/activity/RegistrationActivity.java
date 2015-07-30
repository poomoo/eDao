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
import com.poomoo.edao.adapter.RegistrationSpinnerAdapter;

public class RegistrationActivity extends BaseActivity implements
		OnClickListener {
	private EditText editText_openaccountbank, editText_accountnum,
			editText_accountnumagain;
	private Button button_next;
	private RegistrationSpinnerAdapter adapter_province, adapter_city, adapter_bank;
	private PopupWindow popupWindow;
	private LinearLayout layout_province, layout_city, layout_bank, layout;
	private TextView textView_province, textView_city, textView_bank;

	private List<HashMap<String, String>> list_province, list_city, list_bank;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_openaccountbank = (EditText) findViewById(R.id.registration_editText_openaccountbank);
		editText_accountnum = (EditText) findViewById(R.id.registration_editText_accountnum);
		editText_accountnumagain = (EditText) findViewById(R.id.registration_editText_accountnumagain);
		textView_province = (TextView) findViewById(R.id.registration_textView_province);
		textView_city = (TextView) findViewById(R.id.registration_textView_city);
		textView_bank = (TextView) findViewById(R.id.registration_textView_bank);
		layout_province = (LinearLayout) findViewById(R.id.registration_layout_province);
		layout_city = (LinearLayout) findViewById(R.id.registration_layout_city);
		layout_bank = (LinearLayout) findViewById(R.id.registration_layout_bank);
		button_next = (Button) findViewById(R.id.registration_btn_confirm);

		list_province = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> item = new HashMap<String, String>();
			item.put("name", "贵州省" + i);
			list_province.add(item);
		}
		textView_province.setText(list_province.get(0).get("name"));
		adapter_province = new RegistrationSpinnerAdapter(this, list_province);
		layout_province.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.registration_layout_province:
			showWindow(layout_province, layout_province, listView,
					list_province, textView_province, adapter_province);
			break;
		case R.id.registration_layout_city:
			break;
		case R.id.registration_layout_bank:
			break;
		case R.id.registration_btn_confirm:
			break;
		}
	}

	public void showWindow(View position, final LinearLayout spinnerlayout,
			ListView listView, final List<HashMap<String, String>> list,
			final TextView text, RegistrationSpinnerAdapter adapter) {

		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.myspinner_dropdown, null);
		listView = (ListView) layout
				.findViewById(R.id.myspinner_dropdown_listView);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(position);
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
		popupWindow.showAsDropDown(position, 1, 0);
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
