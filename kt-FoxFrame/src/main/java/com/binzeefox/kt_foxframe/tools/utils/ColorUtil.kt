package com.binzeefox.kt_foxframe.tools.utils

import android.graphics.Color
import okhttp3.internal.toHexString

/**
 * 颜色工具
 *
 * @author 狐彻
 * 2020/09/29 11:41
 */
object ColorUtil {

    /**
     * 通过rgb获取#ffffff颜色信息
     *
     * @author 狐彻 2020/09/29 11:41
     */
    fun getColorHexStr(r: Int, g: Int, b: Int): String {
        val color = Color.rgb(r, g, b)
        val colorStr = "000000${color.toHexString()}"
        return "#${colorStr.substring(colorStr.length - 6)}"
    }

    /**
     * 通过颜色值获取rgb
     *
     * @author 狐彻 2020/09/29 11:46
     */
    fun getColorRgb(color: Int): Array<Int> {
        val red = color and 0xff0000 shr 16
        val green = color and 0x00ff00 shr 8
        val blue = color and 0x0000ff

        return arrayOf(red, green, blue)
    }
}