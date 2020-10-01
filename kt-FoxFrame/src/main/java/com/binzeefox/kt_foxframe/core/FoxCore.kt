@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.core

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import java.lang.RuntimeException
import java.util.*


/**
 * 核心类
 *
 * @author 狐彻
 * 2020/09/25 9:22
 */
class FoxCore {

    // 静态池
    // @author 狐彻 2020/09/25 9:02
    companion object {
        private const val TAG = "FoxCore"

        // 双重锁单例
        // @author 狐彻 2020/09/25 9:02
        val instance: FoxCore   //单例
                by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED)
                { FoxCore() }

        // Application实例
        // @author 狐彻 2020/09/25 9:02
        lateinit var application: Application

        /**
         * 初始化函数
         *
         * @author 狐彻 2020/09/25 8:53
         */
        fun init(application: Application) {
            this.application = application
        }
    }

    // Package信息
    // @author 狐彻 2020/09/25 9:02
    val packageInfo: PackageInfo get() {
            return try {
                val manager = application.packageManager
                manager.getPackageInfo(application.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                throw RuntimeException("获取PackageInfo失败")
            }
        }

    // 版本名
    // @author 狐彻 2020/09/25 9:03
    val versionName: String get() {
            val info = packageInfo
            return info.versionName ?: ""
        }

    // 版本号
    // @author 狐彻 2020/09/25 9:03
    val versionCode: Long get() {
            val info = packageInfo
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                info.longVersionCode
            else (info.versionCode).toLong()
        }

    // 内容提供者Authority，默认为包名加.authority
    // @author 狐彻 2020/09/25 9:49
    var authority: String = "${packageInfo.packageName}.authority"

    ///////////////////////////////////////////////////////////////////////////
    // 返回栈
    ///////////////////////////////////////////////////////////////////////////

    // 返回栈
    // @author 狐彻 2020/09/25 9:05
    val activityStack = ActivityStack()

    // 返回栈顶部
    // @author 狐彻 2020/09/25 9:06
    val topActivity: AppCompatActivity get() = activityStack.peek()

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 返回栈
     *
     * @author 狐彻 2020/09/25 9:13
     */
    class ActivityStack: Stack<AppCompatActivity>() {

        /**
         * 杀死顶部Activity
         *
         * @author 狐彻 2020/09/25 9:14
         */
        fun kill() = pop().finish()

        /**
         * 杀死一定数量的Activity
         *
         * @author 狐彻 2020/09/25 9:19
         */
        fun kill(count: Int) {
            for (i in 0 .. count) kill()
        }

        /**
         * 杀死所有Activity
         *
         * @author 狐彻 2020/09/25 9:20
         */
        fun killAll() = kill(count() - 1)
    }
}