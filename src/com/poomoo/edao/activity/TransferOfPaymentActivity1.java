package com.poomoo.edao.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplicaiton;
import com.poomoo.edao.util.Utity;

/**
 * 
 * @ClassName TransferOfPaymentActivity1
 * @Description TODO 转账支付1
 * @author 李苜菲
 * @date 2015-7-30 上午11:02:41
 */
public class TransferOfPaymentActivity1 extends BaseActivity implements
		OnClickListener {

	private TextView textView_username, textView_phonenum, textView_balance;
	private ImageView imageView_payby_addressbook;
	private EditText editText_payee_phonenum;
	private LinearLayout layout_payby_phone, layout_payby_2dimencode;
	private Button button_confirm;
	private eDaoClientApplicaiton application = null;
	private static final int READCONTRACT = 1;

	private String name = "", phoneNum = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transfer_of_payment1);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplicaiton) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);
		textView_balance = (TextView) findViewById(R.id.transfer_of_payment1_textView_balance);

		editText_payee_phonenum = (EditText) findViewById(R.id.transfer_of_payment1_editText_phonenum);

		layout_payby_phone = (LinearLayout) findViewById(R.id.transfer_of_payment1_layout_payby_phone);
		layout_payby_2dimencode = (LinearLayout) findViewById(R.id.transfer_of_payment1_layout_payby_2dimencode);

		button_confirm = (Button) findViewById(R.id.transfer_of_payment1_btn_confirm);

		layout_payby_phone.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

		textView_username
				.setText(Utity.addStarByName(application.getRealName()));
		textView_phonenum
				.setText(Utity.addStarByNum(3, 7, application.getTel()));
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.transfer_of_payment1_layout_payby_phone:
			System.out.println("点击图片");

			startActivityForResult(new Intent(Intent.ACTION_PICK,
					ContactsContract.Contacts.CONTENT_URI), READCONTRACT);
			break;
		case R.id.transfer_of_payment1_layout_payby_2dimencode:
			break;
		case R.id.transfer_of_payment1_btn_confirm:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == READCONTRACT && resultCode == Activity.RESULT_OK) {
			// ContentProvider展示数据类似一个单个数据库表
			// ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
			ContentResolver reContentResolverol = getContentResolver();
			// URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
			Uri contactData = data.getData();
			// 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
			Cursor cursor = managedQuery(contactData, null, null, null, null);
			cursor.moveToFirst();
			// 获得DATA表中的名字
			name = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			// 条件为联系人ID
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			// 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
			Cursor phone = reContentResolverol.query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phone.moveToNext()) {
				phoneNum = phone
						.getString(phone
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			}

			editText_payee_phonenum.setText(name + "\t" + phoneNum);
			editText_payee_phonenum.setSelection(editText_payee_phonenum
					.getText().toString().length());
			return;
		}

	}

}
