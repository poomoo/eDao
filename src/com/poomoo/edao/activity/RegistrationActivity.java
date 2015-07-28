package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.poomoo.edao.R;

public class RegistrationActivity extends BaseActivity {
	private EditText editText_openaccountbank, editText_accountnum,
			editText_accountnumagain;
	private Button button_next;
	private Spinner spinner_province, spinner_city, spinner_bank;
	private List<HashMap<String, String>> list;

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
		spinner_province = (Spinner) findViewById(R.id.registration_spinner_province);
		spinner_city = (Spinner) findViewById(R.id.registration_spinner_city);
		spinner_bank = (Spinner) findViewById(R.id.registration_spinner_bank);
		button_next = (Button) findViewById(R.id.registration_btn_confirm);

		SimpleAdapter adapter_spinner_province = new SimpleAdapter(this, list,
				R.layout.spinner_board, new String[] {},
				new int[] { R.id.spinner_content });
		spinner_province.setAdapter(adapter_spinner_province);

	}

}
