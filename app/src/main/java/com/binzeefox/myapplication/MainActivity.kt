package com.binzeefox.myapplication

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.binzeefox.kt_foxframe.core.base.FoxActivity
import com.binzeefox.kt_foxframe.tools.media.picker.MediaPicker
import com.binzeefox.kt_foxframe.tools.media.picker.MediaPickerParams
import com.binzeefox.kt_foxframe.tools.perform.permission.PermissionFragment
import com.binzeefox.kt_foxframe.tools.perform.permission.PermissionUtil
import com.binzeefox.kt_foxframe.tools.perform.request.RequestFragment
import com.binzeefox.kt_foxframe.tools.perform.request.RequestHelper
import com.binzeefox.kt_foxframe.tools.utils.LogUtil

class MainActivity : FoxActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }
    override fun onSetLayoutResource(): Int = R.layout.activity_main

    override fun create(savedInstanceState: Bundle?) {

    }

    fun requestPermissionFun(){
        PermissionUtil.with(this)
            .check{
                permissionList = arrayListOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                onResult = PermissionFragment.OnResultListener { failedList, noAskList ->
                    LogUtil.d(TAG, "requestPermissionFun: failedList= $failedList")
                    LogUtil.d(TAG, "requestPermissionFun: noAskList= $noAskList")
                }
            }
    }

    fun requestFun(){
        RequestHelper.with(this)
            .request {
                requestCode = 0x001
                intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                onResult = RequestFragment.OnResultListener { data, resultCode, requestCode ->
                    LogUtil.d(TAG, "requestFun: data = $data");
                    LogUtil.d(TAG, "requestFun: resultCode = $resultCode");
                    LogUtil.d(TAG, "requestFun: requestCode = $requestCode");
                }
            }
    }

    fun mediaPickFun(){
        MediaPicker.with(this)
            .openGallery {
                requestCode = 2
                onResult = MediaPickerParams.OnResultListener { mediaUri, data, resultCode, requestCode ->
                        LogUtil.d(TAG, "mediaPickFun: mediaUri = $mediaUri");
                        LogUtil.d(TAG, "mediaPickFun: data = $data");
                        LogUtil.d(TAG, "mediaPickFun: resultCode = $resultCode");
                        LogUtil.d(TAG, "mediaPickFun: requestCode = $requestCode");
                    }
            }
    }
}