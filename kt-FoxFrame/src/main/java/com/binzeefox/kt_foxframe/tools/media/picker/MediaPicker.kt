@file:Suppress("ClassName", "MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.media.picker

import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.perform.request.RequestFragment
import com.binzeefox.kt_foxframe.tools.perform.request.RequestHelper

/**
 * 通过系统工具获取图像视频
 *
 * @author 狐彻
 * 2020/09/30 20:22
 */
class MediaPicker private constructor(private val manager: FragmentManager) {

    // 静态池
    // @author 狐彻 2020/09/30 20:43
    companion object {

        fun with(activity: AppCompatActivity): MediaPicker =
            MediaPicker(activity.supportFragmentManager)

        fun with(fragment: Fragment): MediaPicker =
            MediaPicker(fragment.childFragmentManager)

        fun with(manager: FragmentManager): MediaPicker =
            MediaPicker(manager)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 参数方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 开启相册
     *
     * @author 狐彻 2020/10/01 11:23
     */
    fun openGallery(config: MediaPickerParams.GalleryConfig.() -> Unit) {
        val params = MediaPickerParams.GalleryConfig()
            .apply(config)
        open(params)
    }

    /**
     * 开启相机
     *
     * @author 狐彻 2020/10/01 11:33
     */
    fun openCamera(config: MediaPickerParams.CameraConfig.() -> Unit){
        val params = MediaPickerParams.CameraConfig()
            .apply(config)
        open(params)
    }

    /**
     * 开启录像
     *
     * @author 狐彻 2020/10/01 11:37
     */
    fun openVideo(config: MediaPickerParams.VideoConfig.() -> Unit) {
        val params = MediaPickerParams.VideoConfig()
            .apply(config)
        open(params)
    }

    /**
     * 开启剪裁
     *
     * @author 狐彻 2020/10/01 11:40
     */
    fun openCrop(config: MediaPickerParams.CropConfig.() -> Unit) {
        val params = MediaPickerParams.CropConfig()
            .apply(config)
        open(params)
    }

    /**
     * 开启参数
     *
     * @author 狐彻 2020/10/01 11:54
     */
    fun <T> open(config: T){
        when(config) {
            //相册
            is MediaPickerParams.GalleryConfig -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.resolveActivity(FoxCore.application.packageManager)?.run {
                    RequestHelper.with(manager).request {
                        requestCode = config.requestCode
                        this.intent = intent
                        onResult = RequestFragment.OnResultListener { data, resultCode, requestCode ->
                            config.onResult?.onResult(data?.data, data, resultCode, requestCode)
                        }
                    }
                }
            }
            //相机
            is MediaPickerParams.CameraConfig -> {
                val intent = when (config.secure) {
                    true -> Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
                    else -> Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                }
                RequestHelper.with(manager).request {
                    requestCode = config.requestCode
                    config.resultUri?.run { intent.putExtra(MediaStore.EXTRA_OUTPUT, config.resultUri) }
                    this.intent = intent
                    onResult = RequestFragment.OnResultListener { data, resultCode, requestCode ->
                        config.onResult?.onResult(data?.data, data, resultCode, requestCode)
                    }
                }
            }
            //摄像
            is MediaPickerParams.VideoConfig -> {
                val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                config.apply {
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,quality)
                    resultUri?.also { intent.putExtra(MediaStore.EXTRA_OUTPUT, resultUri)}
                    when {
                        duration != -1L -> intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,duration)
                        maxBytes != -1L -> intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT,maxBytes)
                    }
                }
                RequestHelper.with(manager).request {
                    requestCode = config.requestCode
                    config.resultUri?.run { intent.putExtra(MediaStore.EXTRA_OUTPUT, config.resultUri) }
                    this.intent = intent
                    onResult = RequestFragment.OnResultListener { data, resultCode, requestCode ->
                        config.onResult?.onResult(data?.data, data, resultCode, requestCode)
                    }
                }
            }
            //剪裁
            is MediaPickerParams.CropConfig -> {
                val intent = Intent("com.android.camera.action.CROP")
                config.apply {
                    interceptor?.intercept(intent) ?: run {
                        //默认设置
                        intent.putExtra("crop", "true")
                        intent.putExtra("aspectX", 0) //自由比例

                        intent.putExtra("aspectY", 0) //自由比例

                        intent.putExtra("scale", true)
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
                        intent.putExtra("return-data", false) //不要返回Bitmap

                        intent.putExtra("noFaceDetection", true) //取消面部识别

                        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            resultUri
                        )
                        intent.setDataAndType(inputUri, "image/*")
                    }
                    intent.resolveActivity(FoxCore.application.packageManager)?.run {
                        RequestHelper.with(manager).request {
                            requestCode = config.requestCode
                            config.resultUri?.run { intent.putExtra(MediaStore.EXTRA_OUTPUT, config.resultUri) }
                            this.intent = intent
                            onResult = RequestFragment.OnResultListener { data, resultCode, requestCode ->
                                config.onResult?.onResult(data?.data, data, resultCode, requestCode)
                            }
                        }
                    }
                }
            }
        }
    }
}