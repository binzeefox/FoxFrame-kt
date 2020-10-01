package com.binzeefox.kt_foxframe.core.base

import android.app.Application
import com.binzeefox.kt_foxframe.core.FoxCore

/**
 * Application基类
 *
 * @author 狐彻
 * 2020/09/25 9:23
 */
open class FoxApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FoxCore.init(this)
        FoxCore.instance.authority = authority()
    }

    /**
     * 获取provider所需authority，继承类可重写以自定义
     *
     * @author 狐彻 2020/09/25 9:56
     */
    open fun authority(): String{
        return "${packageName}.authority"
    }
}