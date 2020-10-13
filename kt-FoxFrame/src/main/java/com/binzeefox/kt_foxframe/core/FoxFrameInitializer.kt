package com.binzeefox.kt_foxframe.core

import android.content.Context
import androidx.startup.Initializer

/**
 * App Startup 初始化工具
 *
 * @author 狐彻
 * 2020/10/13 8:46
 */
class FoxFrameInitializer: Initializer<Unit> {

    override fun create(context: Context) =
        FoxCore.init(context)

    override fun dependencies(): MutableList<Class<out Initializer<*>>> =
        ArrayList()
}