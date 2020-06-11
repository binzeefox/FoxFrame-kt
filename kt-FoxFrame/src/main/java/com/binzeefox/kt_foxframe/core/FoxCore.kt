package com.binzeefox.kt_foxframe.core

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.binzeefox.kt_foxframe.core.tools.DataHolder

/**
 * 框架核心
 * @author binze
 * 2020/6/10 10:36
 */
class FoxCore private constructor() {
    companion object {  //静态池
        private const val TAG = "FoxCore"
        private var instance: FoxCore? = null
            get() {
                if (field == null)
                    field = FoxCore()
                return field
            }
        
        private var mApp: Application? = null   //Application实例

        /**
         * 初始化
         * @author binze 2020/6/10 10:36
         */
        fun init(application: Application): FoxCore{
            this.mApp = application
            return instance!!
        }

        /**
         * 获取实例
         * @author binze 2020/6/10 10:36
         */
        fun get(): FoxCore{
            mApp?:throw IllegalAccessException("should call init(Application) first!!!")
            return instance!!
        }
        
        /**
         * 获取application实例
         * @author binze 2020/6/10 10:51
         */
        val application: Application? get() = mApp!!
    }

    /**
     * Package信息
     * @author binze 2020/6/10 11:28
     */
    val packageInfo: PackageInfo? get() {
        return try {
            val manager = mApp!!.packageManager
            manager.getPackageInfo(mApp!!.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException){
            Log.e(TAG, "getPackageInfo: 获取失败", e)
            null
        }
    }

    /**
     * 版本名
     * @author binze 2020/6/10 11:31
     */
    val versionName: String get() {
        val info = packageInfo
        return info?.versionName?: ""
    }

    /**
     * 版本号
     * @author binze 2020/6/10 11:39
     */
    val versionCode: Long get() {
        val  info = packageInfo
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            info?.longVersionCode?: -1
        else (info?.versionCode?: -1).toLong()
    }


    /**
     * 全局数据
     * @author binze 2020/6/10 11:43
     */
    var globalData: DataHolder? = null
        get() {
            if (field == null) field = DataHolder()
            return field
        }

}