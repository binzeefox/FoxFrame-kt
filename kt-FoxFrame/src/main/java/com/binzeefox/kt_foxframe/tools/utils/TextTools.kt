@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.utils

import android.icu.util.Calendar
import android.util.Patterns
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.HashMap

/**
 * 字符串工具
 *
 * @author 狐彻
 * 2020/09/29 15:46
 */
object TextTools {
    /**
     * 是否是整数
     *
     * @author 狐彻 2020/09/29 18:02
     */
    fun isInteger(str: String) = when (str) {
        "0" -> true
        "-" -> false
        else -> Pattern.compile("^[-+]?[\\d]*\$").matcher(str).matches()
    }

    /**
     * 是否是汉字
     *
     * @author 狐彻 2020/09/29 18:57
     */
    fun isChinese(c: Char): Boolean =
        c >= 0x4E00.toChar() && c <= 0x9fa5.toChar()

    /**
     * 是否包含中文
     *
     * @author 狐彻 2020/09/29 18:59
     */
    fun hasChinese(str: String): Boolean {
        for (c in str.toCharArray())
            if (isChinese(c)) return true
        return false
    }

    /**
     * 是否是合法网络Uri
     *
     * @author 狐彻 2020/09/29 19:03
     */
    fun isWebUrl(url: String): Boolean =
        Patterns.WEB_URL.matcher(url).matches()


    /**
     * 身份证工具
     *
     * @author 狐彻 2020/09/29 19:04
     */
    class IDCardTools(idStr: String) {

        // 地理代号字典
        // @author 狐彻 2020/09/29 19:05
        private val cCityMap = HashMap<String, String>().apply {
            this["11"] = "北京";
            this["12"] = "天津";
            this["13"] = "河北";
            this["14"] = "山西";
            this["15"] = "内蒙古";
            this["21"] = "辽宁";
            this["22"] = "吉林";
            this["23"] = "黑龙江";
            this["31"] = "上海";
            this["32"] = "江苏";
            this["33"] = "浙江";
            this["34"] = "安徽";
            this["35"] = "福建";
            this["36"] = "江西";
            this["37"] = "山东";
            this["41"] = "河南";
            this["42"] = "湖北";
            this["43"] = "湖南";
            this["44"] = "广东";
            this["45"] = "广西";
            this["46"] = "海南";
            this["50"] = "重庆";
            this["51"] = "四川";
            this["52"] = "贵州";
            this["53"] = "云南";
            this["54"] = "西藏";
            this["61"] = "陕西";
            this["62"] = "甘肃";
            this["63"] = "青海";
            this["64"] = "宁夏";
            this["65"] = "新疆";
            this["71"] = "台湾";
            this["81"] = "香港";
            this["82"] = "澳门";
            this["91"] = "境外";
        }

        // 是否是合法身份证
        // @author 狐彻 2020/09/29 19:09
        val isLegal = Pattern.compile("\\d{15}(\\d{2}[0-9xX])?")
            .matcher(idStr).matches()

        // 获取该身份证城市名称
        // @author 狐彻 2020/09/29 19:10
        val cityName = cCityMap[idStr.substring(0, 2)]

        // 获取该身份证生日信息
        // @author 狐彻 2020/09/29 19:11
        val birthDay: Date =
            Calendar.getInstance(Locale.CHINA).apply {
                val birthday = idStr.substring(6, 14)
                val year = birthday.substring(0, 4).toInt()
                val month = birthday.substring(4, 6).toInt()
                val day = birthday.substring(6).toInt()
                this.set(year, month, day)
            }.time

        // 是否是男性
        // @author 狐彻 2020/09/29 19:21
        val isMale = DimenUtil.isObb(idStr.substring(idStr.length - 2, idStr.length - 1))
    }
}