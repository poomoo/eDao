package com.poomoo.edao.fragment;

import java.util.Calendar;

import com.poomoo.edao.R;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.util.Utity;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class Fragment_Date extends Fragment implements OnClickListener {
	private TextView textView_start_date, textView_end_date;
	private Button button_date_confrim;
	private String startDate = "", endDate = "";

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		System.out.println("进入时间");
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.fragment_date, container, false);
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_start_date = (TextView) getView().findViewById(
				R.id.fragment_date_textView_start_date);
		textView_end_date = (TextView) getView().findViewById(
				R.id.fragment_date_textView_end_date);

		button_date_confrim = (Button) getView().findViewById(
				R.id.fragment_date_btn_date_confirm);

		textView_start_date.setOnClickListener(this);
		textView_end_date.setOnClickListener(this);
		button_date_confrim.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.fragment_date_textView_start_date:
			System.out.println("点击起始日期");
			pickDate(textView_start_date);
			break;
		case R.id.fragment_date_textView_end_date:
			System.out.println("点击结束日期");
			pickDate(textView_end_date);
			break;
		case R.id.fragment_date_btn_date_confirm:
			checkInput();
			break;
		}
	}

	private boolean checkInput() {
		// TODO 自动生成的方法存根
		startDate = textView_start_date.getText().toString();
		endDate = textView_end_date.getText().toString();
		if (!Utity.isSmaller(startDate, endDate)) {
			Utity.showToast(getActivity().getApplication(),
					eDaoClientConfig.timeIllegal);
			return false;
		}

		return true;
	}

	private void pickDate(final TextView textView) {
		Calendar cal = Calendar.getInstance();
		final DatePickerDialog mDialog = new DatePickerDialog(getActivity(),
				null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));

		// 手动设置按钮
		mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
						DatePicker datePicker = mDialog.getDatePicker();
						int year = datePicker.getYear();
						int month = datePicker.getMonth() + 1;
						int day = datePicker.getDayOfMonth();

						textView.setText(year + "-" + month + "-" + day);
					}
				});
		// 取消按钮，如果不需要直接不设置即可
		mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		mDialog.show();
	}
}
