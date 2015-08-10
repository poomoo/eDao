package com.poomoo.edao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.poomoo.edao.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 
 * @ClassName ShareActivity
 * @Description TODO 分享
 * @author 李苜菲
 * @date 2015年8月10日 下午10:00:42
 */
public class ShareActivity extends BaseActivity implements OnClickListener {
	private Button button_share;

	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.navigation_fragment));
		init();
		umeng();
	}

	private void init() {
		// TODO 自动生成的方法存根
		button_share = (Button) findViewById(R.id.share_button);
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.DOUBAN);
		button_share.setOnClickListener(this);
	}

	public void umeng() {
		// 设置分享内容
		mController
				.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(this,
				"http://www.umeng.com/images/pic/banner_module_social.png"));
		// 设置分享图片，参数2为本地图片的资源引用
		// mController.setShareMedia(new UMImage(getActivity(),
		// R.drawable.icon));
		// 设置分享图片，参数2为本地图片的路径(绝对路径)
		// mController.setShareMedia(new UMImage(getActivity(),
		// BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

		// 设置分享音乐
		// UMusic uMusic = new
		// UMusic("http://sns.whalecloud.com/test_music.mp3");
		// uMusic.setAuthor("GuGu");
		// uMusic.setTitle("天籁之音");
		// 设置音乐缩略图
		// uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// mController.setShareMedia(uMusic);

		// 设置分享视频
		// UMVideo umVideo = new UMVideo(
		// "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		// 设置视频缩略图
		// umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		// umVideo.setTitle("友盟社会化分享!");
		// mController.setShareMedia(umVideo);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		// 是否只有已登录用户才能打开分享选择页
		mController.openShare(this, false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

}
