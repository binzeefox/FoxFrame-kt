package com.binzeefox.kt_foxframe.tools.media.picker

import android.content.Intent
import android.net.Uri

/**
 * 媒体获取器参数类
 *
 * @author 狐彻
 * 2020/10/01 11:10
 */
object MediaPickerParams {

    /**
     * 视频质量枚举
     *
     * @author 狐彻 2020/10/01 11:11
     */
    enum class Quality(val value: Int) {
        QUALITY_LOW(0), QUALITY_HIGH(1)
    }

    /**
     * 请求回调
     *
     * @author 狐彻 2020/10/01 11:11
     */
    fun interface OnResultListener {
        fun onResult(mediaUri: Uri?, data: Intent?, resultCode: Int, requestCode: Int)
    }

    /**
     * 剪裁请求拦截
     *
     * @author 狐彻 2020/10/01 11:11
     */
    fun interface CropOptionInterceptor {
        fun intercept(intent: Intent)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 参数类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 相册参数类
     *
     * @author 狐彻 2020/10/01 11:12
     */
    class GalleryConfig {
        var requestCode: Int = 0x00020
        var onResult: OnResultListener? = null
    }

    /**
     * 相机参数类
     *
     * @author 狐彻 2020/10/01 11:15
     */
    class CameraConfig {
        var requestCode: Int = 0x00021
        var resultUri: Uri? = null
        var secure: Boolean = false
        var onResult: OnResultListener? = null
    }

    /**
     * 摄像参数类
     *
     * @author 狐彻 2020/10/01 11:15
     */
    class VideoConfig {
        var requestCode: Int = 0x00022
        var resultUri: Uri? = null
        var quality: Quality = Quality.QUALITY_HIGH
        var duration: Long = -1
        var maxBytes: Long = -1
        var onResult: OnResultListener? = null
    }

    /**
     * 剪裁参数类
     *
     * @author 狐彻 2020/10/01 11:19
     */
    class CropConfig {
        var requestCode: Int = 0x00023
        var inputUri: Uri? = null   //不能为空
        var resultUri: Uri? = null
        var interceptor: CropOptionInterceptor? = null
        var onResult: OnResultListener? = null
    }
}