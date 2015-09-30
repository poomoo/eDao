package com.poomoo.edao.activity;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.edao.R;
import com.poomoo.edao.application.eDaoClientApplication;
import com.poomoo.edao.config.eDaoClientConfig;
import com.poomoo.edao.model.ResponseData;
import com.poomoo.edao.model.StoreData;
import com.poomoo.edao.util.HttpCallbackListener;
import com.poomoo.edao.util.HttpUtil;
import com.poomoo.edao.util.Utity;
import com.poomoo.edao.widget.FlexibleRatingBar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * 
 * @ClassName StoreEvaluateActivity
 * @Description TODO 店铺评价
 * @author 李苜菲
 * @date 2015-8-3 下午4:02:36
 */
public class StoreEvaluateActivity extends BaseActivity implements OnClickListener {
	private TextView textView_store_name, textView_total_score, textView_distance, textView_remark, textView_canNum;
	private ImageView imageView_pic;
	private EditText editText_content;
	private RatingBar ratingBar_total_score;
	private FlexibleRatingBar flexibleRatingBar_service, flexibleRatingBar_goods;
	private Button button_confirm;

	private String shopId = "", ordersId = "", content = "";
	private float service = 0, goods = 0;
	private Gson gson = new Gson();
	private ProgressDialog progressDialog = null;
	private int num = 100;
	private eDaoClientApplication application = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_evaluate);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		shopId = getIntent().getStringExtra("shopId");
		ordersId = getIntent().getStringExtra("ordersId");
		application = (eDaoClientApplication) getApplication();
		init();
	}

	private void init() {
		// TODO 自动生成的方法存根
		textView_store_name = (TextView) findViewById(R.id.store_evaluate_textView_store_name);
		textView_total_score = (TextView) findViewById(R.id.store_evaluate_textView_total_score);
		textView_distance = (TextView) findViewById(R.id.store_evaluate_textView_distance);
		textView_remark = (TextView) findViewById(R.id.store_evaluate_textView_remark);
		textView_canNum = (TextView) findViewById(R.id.store_evaluate_textView_canNum);

		editText_content = (EditText) findViewById(R.id.store_evaluate_editText_content);

		imageView_pic = (ImageView) findViewById(R.id.store_evaluate_imageView_pic);

		ratingBar_total_score = (RatingBar) findViewById(R.id.store_evaluate_ratingBar_total_score);

		flexibleRatingBar_service = (FlexibleRatingBar) findViewById(R.id.store_evaluate_ratingBar_service);
		flexibleRatingBar_goods = (FlexibleRatingBar) findViewById(R.id.store_evaluate_ratingBar_goods);

		button_confirm = (Button) findViewById(R.id.store_evaluate_btn_confirm);

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

		showProgressDialog("请稍后...");
		getData();

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
		if(TextUtils.isEmpty(content)){
			Utity.showToast(getApplicationContext(), "请输入评价内容");
			return false;
		}

		service = flexibleRatingBar_service.getRating();
		if (service == 0) {
			Utity.showToast(getApplicationContext(), "请对卖家进行评价");
			return false;
		}
		return true;
	}

	private void getData() {
		// TODO 自动生成的方法存根
		System.out.println("调用getData");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "30000");
		data.put("method", "30004");
		data.put("shopId", shopId);
		HttpUtil.SendPostRequest(gson.toJson(data), eDaoClientConfig.url, new HttpCallbackListener() {

			@Override
			public void onFinish(final ResponseData responseData) {
				// TODO 自动生成的方法存根
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// TODO 自动生成的方法存根
						closeProgressDialog();
						if (responseData.getRsCode() == 1 && responseData.getJsonData().length() > 0) {
							StoreData data = new StoreData();
							data = gson.fromJson(responseData.getJsonData(), StoreData.class);
							setData(data);
						} else
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

	private void confirm() {
		// TODO 自动生成的方法存根
		showProgressDialog("请稍后...");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bizName", "30000");
		data.put("method", "30005");
		data.put("shopId", shopId);
		data.put("score", service);
		data.put("content", content);
		data.put("ordersId", ordersId);
		data.put("userId", application.getUserId());
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
							setResult(1);
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

	private void setData(StoreData data) {
		textView_store_name.setText(data.getShopName());
		textView_total_score.setText(String.valueOf(data.getAvgScore()));
		ratingBar_total_score.setRating(data.getAvgScore());
		textView_distance.setText("");
		textView_remark.setText("");
		ImageLoader.getInstance().displayImage(data.getPictures(), imageView_pic);
	}

}
