@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.utils

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.binzeefox.kt_foxframe.core.FoxCore

/**
 * 资源ID工具
 *
 * @author 狐彻
 * 2020/09/26 20:22
 */
object ResourcesTools {

    // 获取资源文件
    // @author 狐彻 2020/09/26 20:29
    val resources: Resources
        get() = FoxCore.application.resources

    /**
     * 获取String资源
     *
     * @author 狐彻 2020/09/26 20:24
     */
    fun Int.asStringRes(): String =
        resources.getString(this)

    /**
     * 获取数值资源
     *
     * @author 狐彻 2020/09/26 20:25
     */
    fun Int.asInterRes(): Number =
        resources.getDimension(this)

    /**
     * 获取Drawable文件
     *
     * @author 狐彻 2020/09/26 20:30
     */
    fun Int.asDrawableRes(): Drawable? =
        ResourcesCompat.getDrawable(resources, this, null)

    /**
     * 获取Drawable文件
     *
     * @author 狐彻 2020/09/26 20:30
     */
    fun Int.asDrawableRes(theme: Resources.Theme): Drawable? =
        ResourcesCompat.getDrawable(resources, this, theme)
}