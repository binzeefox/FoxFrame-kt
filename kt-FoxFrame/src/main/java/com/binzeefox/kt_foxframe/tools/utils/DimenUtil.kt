@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.utils

import com.binzeefox.kt_foxframe.core.FoxCore
import java.util.regex.Pattern

/**
 * 数值工具类，主要为单位转换
 *
 * @author 狐彻
 * 2020/09/25 22:50
 */
object DimenUtil {

    // 数值乘数
    // @author 狐彻 2020/09/25 22:52
    private val scale
        get() = FoxCore.application.resources.displayMetrics.density

    /**
     * dp转px
     *
     * @author 狐彻 2020/09/25 22:56
     */
    fun dipToPx(dpValue: Int): Int =
        (dpValue * scale + 0.5f).toInt()

    fun pxToDpi(pxValue: Int): Int =
        (pxValue / scale + 0.5f).toInt()

    /**
     * 数字是否是奇数
     *
     * @author 狐彻 2020/09/29 17:57
     */
    fun isObb(num: Long): Boolean =
        num % 2 == 1L

    /**
     * 数字是否是奇数
     *
     * @author 狐彻 2020/09/29 17:57
     */
    fun isObb(num: Int): Boolean =
        num % 2 == 1

    /**
     * 字符串是否是奇数
     *
     * @author 狐彻 2020/09/29 18:02
     */
    fun isObb(numStr: String): Boolean = when {
        !TextTools.isInteger(numStr) -> false
        else -> numStr.toLong() % 2 == 1L
    }

    ///////////////////////////////////////////////////////////////////////////
    // 拓展函数
    ///////////////////////////////////////////////////////////////////////////

    fun Int.dpToPx() = DimenUtil.dipToPx(this)

    fun Int.pxToDp() = DimenUtil.pxToDpi(this)
}