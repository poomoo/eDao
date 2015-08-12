/**
 * @Title: PostalService
 * @Package com.dvt.common
 * @author 李苜菲
 * @date 2014年4月28日 上午9:15:18
 * @version V1.0
 */
package com.poomoo.edao.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.poomoo.edao.R;

/**
 * 
 * @ClassName MessageBox_YESNO
 * @Description TODO 有确定和取消按钮的对话框
 * @author 李苜菲
 * @date 2015-8-12 上午10:21:49
 */
public class MessageBox_YESNO extends Dialog {
	int dialogResult;
	Handler mHandler;
	private Activity context;
	private Button OkBtn, CanBtn;

	public MessageBox_YESNO(Activity context) {
		super(context);
		this.context = context;
		dialogResult = 0;
		setOwnerActivity(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		onCreate();
	}

	public void onCreate() {
		setContentView(R.layout.messagebox_yesno);
		this.setCanceledOnTouchOutside(false);
	}

	public void setButtonText(String OK, String Cancle) {
		OkBtn = (Button) this.findViewById(R.id.messagebox_yesno_btn_ok);
		CanBtn = (Button) this.findViewById(R.id.messagebox_yesno_btn_cancel);
		OkBtn.setText(OK);
		CanBtn.setText(Cancle);
	}

	public int getDialogResult() {
		return dialogResult;
	}

	public void setDialogResult(int dialogResult) {
		this.dialogResult = dialogResult;
	}

	public void endDialog(int result) {
		dismiss();
		setDialogResult(result);
	}

	public int showDialog(String Msg,
			final DialogResultListener dialogResultListener) {
		TextView Info = (TextView) findViewById(R.id.messagebox_yesno_textView_content);
		Info.setText(Msg);
		super.show();

		findViewById(R.id.messagebox_yesno_btn_cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View paramView) {
						endDialog(0);
						if (dialogResultListener != null)
							dialogResultListener.onFinishDialogResult(0);
					}
				});
		findViewById(R.id.messagebox_yesno_btn_ok).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View paramView) {
						endDialog(1);
						if (dialogResultListener != null)
							dialogResultListener.onFinishDialogResult(1);
					}
				});

		this.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO 自动生成的方法存根
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					endDialog(0);
					if (dialogResultListener != null)
						dialogResultListener.onFinishDialogResult(0);
					return true;
				}
				return false;
			}
		});
		return dialogResult;
	}
}
