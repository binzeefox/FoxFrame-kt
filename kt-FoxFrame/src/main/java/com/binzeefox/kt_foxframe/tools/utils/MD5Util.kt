@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.utils

import okhttp3.internal.and
import okhttp3.internal.toHexString
import java.lang.StringBuilder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * MD5工具类，之后可能回变成一个主司加密的类
 *
 * @author 狐彻
 * 2020/09/25 23:08
 */
object MD5Util {
    private const val TAG = "MD5Util"

    /**
     * MD5 加密
     *
     * @author 狐彻 2020/09/25 23:18
     */
    fun md5(value: String): String {
        if (value.isEmpty()) return ""
        var md5: MessageDigest? = null
        return try {
            md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(value.toByteArray())
            val result = StringBuilder()
            for (b in bytes){
                var temp = (b and 0xff).toHexString()
                if (temp.length == 1) temp = "0$temp"
                result.append(temp)
            }
            result.toString()
        } catch (e: NoSuchAlgorithmException) {
            LogUtil.e(TAG, "md5: MD5失败", e)
            ""
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 拓展函数
    ///////////////////////////////////////////////////////////////////////////

    fun String.toMD5(): String = MD5Util.md5(this)
}