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
 * @ClassName DealerApplyActivity
 * @Description TODO 经销商申请
 * @author 李苜菲
 * @date 2015年7月30日 下午11:45:57
 */
public class DealerApplyActivity extends BaseActivity implements
		OnClickListener {
	private TextView textView_username, textView_phonenum, textView_zone,
			textView_merchant_type;
	private EditText editText_money, editText_secret_key,
			editText_merchant_name;
	private LinearLayout layout_zone, layout_merchant_type;
	private Button button_confirm;

	private PopupWindow popupWindow;
	private View layout;
	private ChannelSpinnerAdapter adapter_zone, adapter_merchant_type;
	private List<HashMap<String, String>> list_zone, list_merchant_type;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		setContentView(R.layout.activity_dealer_apply);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_zone = (TextView) findViewById(R.id.dealer_textView_zone);
		textView_merchant_type = (TextView) findViewById(R.id.dealer_textView_merchant_type);

		editText_money = (EditText) findViewById(R.id.dealer_editText_money);
		editText_secret_key = (EditText) findViewById(R.id.dealer_editText_secret_key);
		editText_merchant_name = (EditText) findViewById(R.id.dealer_editText_merchant_name);

		layout_zone = (LinearLayout) findViewById(R.id.dealer_layout_zone);
		layout_merchant_type = (LinearLayout) findViewById(R.id.dealer_layout_merchant_type);

		button_confirm = (Button) findViewById(R.id.dealer_btn_confirm);

		layout_zone.setOnClickListener(this);
		layout_merchant_type.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

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
		textView_merchant_type.setText(list_merchant_type.get(0).get("name"));
		adapter_merchant_type = new ChannelSpinnerAdapter(this,
				list_merchant_type);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.dealer_layout_zone:
			showWindow(layout_zone, listView, list_zone, textView_zone,
					adapter_zone);
			break;
		case R.id.dealer_layout_merchant_type:
			showWindow(layout_merchant_type, listView, list_merchant_type,
					textView_merchant_type, adapter_merchant_type);
			break;
		case R.id.dealer_btn_confirm:
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
