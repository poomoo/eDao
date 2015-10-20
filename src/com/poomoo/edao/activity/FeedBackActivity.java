package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * @ClassName FeedBackActivity
 * @Description TODO 意见反馈
 * @author 李苜菲
 * @date 2015年10月20日 上午10:10:53
 */
public class FeedBackActivity extends BaseActivity implements OnClickListener {
	private EditText editText_content;
	private TextView textView_canNum;
	private Button button_confirm;

	private String content = "";
	private Gson gson = new Gson();
	private int num = 200;
	private eDaoClientApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		editText_content = (EditText) findViewById(R.id.feed_back_editText_content);
		textView_canNum = (TextView) findViewById(R.id.feed_back_textView_canNum);
		button_confirm = (Button) findViewById(R.id.feed_back_btn_confirm);

		button_confirm.setOnClickListener(this);

		textView_canNum.setText("还能输入" + num + "字");
		editText_content.addTextChangedListener(new TextWatcher() {

			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int number = num - s.length();
				// textView_publication_cannum.setText("" + number);
				textView_canNum.setText("还能输入" + number + "字");

				selectionStart = editText_content.getSelectionStart();
				selectionEnd = editText_content.getSelectionEnd();
				if (temp.length() > num) {
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					editText_content.setText(s);
					editText_content.setSelection(tempSelection);// 设置光标在最后
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (checkInput())
			confirm();
	}

	private boolean checkInput() {
		// TODO Auto-generated method stub
		content = editText_content.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			Utity.showToast(getApplicationContext(), "请提出您的宝贵意见！");
			return false;
		}
		return true;
	}

	private void confirm() {
		// TODO 自动生成的方法存根
		showProgressDialog("请稍后...");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "10000");
		data.put("method", "10010");
		data.put("userId", application.getUserId());
		data.put("content", content);
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
							finish();
						}
						Utity.showToast(getApplicationContext(), responseData.getMsg());
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
