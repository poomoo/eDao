package com.poomoo.edao.activity;

import com.poomoo.edao.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 
 * @ClassName ShareActivity
 * @Description TODO 分享
 * @author 李苜菲
 * @date 2015年8月10日 下午10:00:42
 */
public class ShareActivity extends BaseActivity implements OnClickListener {
	private ImageView imageView_return;
	private Button button_share;

	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private static final String content = "乐意道。http://www.yidao7.com";
	private static final String website = "http://www.yidao7.com";
	private static final String title = "扫描二维码下载注册乐意道";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		// 实现沉浸式状态栏效果
		setImmerseLayout(findViewById(R.id.share_layout));
		init();
		// 配置需要分享的相关平台
		configPlatforms();
		// 设置分享内容
		shareContent();
	}

	private void init() {
		// TODO 自动生成的方法存根
		imageView_return = (ImageView) findViewById(R.id.share_imageView_return);

		button_share = (Button) findViewById(R.id.share_button);

		imageView_return.setOnClickListener(this);
		button_share.setOnClickListener(this);
	}

	/**
	 * 配置分享平台参数</br>
	 */
	private void configPlatforms() {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// 添加QQ、QZone平台
		addQQQZonePlatform();

		// 添加微信、微信朋友圈平台
		addWXPlatform();

		// 添加短信平台
		addSMS();
	}

	public void shareContent() {
		// 本地图片
		UMImage localImage = new UMImage(this, R.drawable.ic_leyidao);

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

		// QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
		// "100424468", "c7394704798a158208a74ab60104f0ba");

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent.setShareContent(content);
		weixinContent.setTitle(title);
		weixinContent.setTargetUrl(website);
		weixinContent.setShareMedia(localImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(content);
		circleMedia.setTitle(title);
		circleMedia.setShareMedia(localImage);
		circleMedia.setTargetUrl(website);
		mController.setShareMedia(circleMedia);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(content);
		qzone.setTargetUrl(website);
		qzone.setTitle(title);
		qzone.setShareMedia(localImage);
		mController.setShareMedia(qzone);

		// 设置QQ分享内容
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(content);
		qqShareContent.setTitle(title);
		qqShareContent.setShareMedia(localImage);
		qqShareContent.setTargetUrl(website);
		mController.setShareMedia(qqShareContent);

		// 设置短信分享内容
		SmsShareContent sms = new SmsShareContent();
		sms.setShareContent(content);
		sms.setShareImage(localImage);
		mController.setShareMedia(sms);

		// 设置新浪微博分享内容
		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent.setShareContent(content);
		sinaContent.setShareImage(localImage);
		mController.setShareMedia(sinaContent);

	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
		qqSsoHandler.setTargetUrl(website);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId,
				appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx967daebe835fbeac";
		String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * 添加短信平台
	 */
	private void addSMS() {
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mController.getConfig().cleanListeners();
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
		case R.id.share_imageView_return:
			finish();
			break;
		case R.id.share_button:
			// 是否只有已登录用户才能打开分享选择页
			mController.getConfig().setPlatforms(SHARE_MEDIA.QQ,
					SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
					SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
					SHARE_MEDIA.TENCENT, SHARE_MEDIA.SMS);
			mController.openShare(this, false);
			break;
		}
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
