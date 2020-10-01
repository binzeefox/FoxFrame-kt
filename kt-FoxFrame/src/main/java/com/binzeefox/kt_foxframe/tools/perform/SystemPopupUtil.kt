package com.binzeefox.kt_foxframe.tools.perform

import android.content.Intent
import android.os.Build
import android.os.Parcelable
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.binzeefox.kt_foxframe.core.FoxCore

/**
 * 弹出系统弹窗工具类
 *
 * @author 狐彻
 * 2020/09/28 21:05
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
object SystemPopupUtil {

    /**
     * 网络设置弹窗
     *
     * @author 狐彻 2020/09/28 21:07
     */
    fun showInternetSetting() {
        FoxCore.application.startActivity(Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY))
    }

    /**
     * NFC设置弹窗
     *
     * @author 狐彻 2020/09/28 21:09
     */
    fun showNFCSetting() {
        FoxCore.application.startActivity(Intent(Settings.Panel.ACTION_NFC))
    }

    /**
     * 音量设置弹窗
     *
     * @author 狐彻 2020/09/28 21:11
     */
    fun showVolumeSetting() {
        FoxCore.application.startActivity(Intent(Settings.Panel.ACTION_VOLUME))
    }

    /**
     * WIFI设置弹窗
     *
     * @author 狐彻 2020/09/28 21:11
     */
    fun showWIFISetting() {
        FoxCore.application.startActivity(Intent(Settings.Panel.ACTION_WIFI))
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

//    /**
//     * 分析工具类
//     *
//     * @author 狐彻 2020/09/28 21:15
//     */
//    class ShareBuilder() {
//
//        // 数据Intent
//        // @author 狐彻 2020/09/28 21:15
//        private val infoIntent = Intent().also { it.action = Intent.ACTION_SEND }
//
//        // 添加数据
//        // @author 狐彻 2020/09/28 21:17
//        fun putExtra(name: String, value: Any): ShareBuilder {
//            when (value) {
//                is Int -> infoIntent.putExtra(name, value)
//                is Long -> infoIntent.putExtra(name, value)
//                is Parcelable -> infoIntent.putExtra(name, value)
//                is String -> infoIntent.putExtra(name, value)
//                is Array<Int> ->
//            }
//            return this
//
//        }
//    }
}