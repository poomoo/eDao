package com.poomoo.edao.util;

import com.poomoo.edao.model.ResponseData;

/**
 * 
 * @ClassName HttpCallbackListener
 * @Description TODO 回调接口
 * @author 李苜菲
 * @date 2015-7-24 下午3:08:02
 */
public interface HttpCallbackListener {
	void onFinish(final ResponseData responseData);

	void onError(final Exception e);
}
