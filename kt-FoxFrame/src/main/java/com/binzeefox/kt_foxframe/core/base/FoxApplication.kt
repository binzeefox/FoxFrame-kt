package com.binzeefox.kt_foxframe.core.base

import android.app.Application
import com.binzeefox.kt_foxframe.core.FoxCore

/**
 * 自定义Application
 * @author binze
 * 2020/6/10 11:53
 */
abstract class FoxApplication: Application() {
    //使用的核心实例
    private companion object var core: FoxCore? = null

    open override fun onCreate() {
        super.onCreate()
        core = FoxCore.init(this)
    }
}