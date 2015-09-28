package com.poomoo.edao.activity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.RepayData;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * 
 * @ClassName RepayOperateActivity
 * @Description TODO 报酬管理
 * @author 李苜菲
 * @date 2015年9月24日 下午5:10:36
 */
public class RepayManageActivity extends BaseActivity implements OnClickListener {
	private TextView textView_username, textView_phonenum, textView_year, textView_month, textView_day,
			textView_year_money, textView_month_money, textView_day_money;
	private eDaoClientApplication application = null;

	private Calendar cal = Calendar.getInstance();
	private Gson gson = new Gson();
	private String curDate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_repay_manage);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_username = (TextView) findViewById(R.id.layout_userinfo_textView_username);
		textView_phonenum = (TextView) findViewById(R.id.layout_userinfo_textView_tel);

		textView_year = (TextView) findViewById(R.id.repay_textView_year);
		textView_month = (TextView) findViewById(R.id.repay_textView_month);
		textView_day = (TextView) findViewById(R.id.repay_textView_day);
		textView_year_money = (TextView) findViewById(R.id.repay_textView_year_money);
		textView_month_money = (TextView) findViewById(R.id.repay_textView_month_money);
		textView_day_money = (TextView) findViewById(R.id.repay_textView_day_money);

		textView_year.setOnClickListener(this);
		textView_month.setOnClickListener(this);
		textView_day.setOnClickListener(this);

		Utity.setUserAndTel(textView_username, textView_phonenum, application);

		textView_year.setText(cal.get(Calendar.YEAR) + "年");
		textView_month.setText((cal.get(Calendar.MONTH) + 1) + "月");
		textView_day.setText(cal.get(Calendar.DAY_OF_MONTH) + "日");

		curDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH);
		getData(curDate);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		pickDate();
	}

	private void pickDate() {
		final DatePickerDialog mDialog = new DatePickerDialog(this, null, cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		DatePicker dp = mDialog.getDatePicker();
		dp.setMaxDate(cal.getTime().getTime());
		// 手动设置按钮
		mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
				DatePicker datePicker = mDialog.getDatePicker();
				int year = datePicker.getYear();
				int month = datePicker.getMonth() + 1;
				int day = datePicker.getDayOfMonth();

				textView_year.setText(year + "年");
				textView_month.setText(month + "月");
				textView_day.setText(day + "日");

				curDate = year + "-" + month + "-" + day;
				getData(curDate);
			}
		});
		// 取消按钮，如果不需要直接不设置即可
		mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		mDialog.show();
	}

	private void getData(String date) {
		// TODO 自动生成的方法存根
		showProgressDialog("请稍后...");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "70000");
		data.put("method", "70011");
		data.put("userId", application.getUserId());
		data.put("date", date);

		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() == 1) {
							RepayData data = new RepayData();
							data = gson.fromJson(responseData.getJsonData(), RepayData.class);
							textView_year_money.setText("￥" + data.getYearIncome());
							textView_month_money.setText("￥" + data.getMonthIncome());
							textView_day_money.setText("￥" + data.getDayIncome());
						} else {
							Utity.showToast(getApplicationContext(), responseData.getMsg());
						}
					}

				});
			}

			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						Utity.showToast(getApplicationContext(), eDaoClientConfig.checkNet);
					}
				});
			}
		});
	}
}
