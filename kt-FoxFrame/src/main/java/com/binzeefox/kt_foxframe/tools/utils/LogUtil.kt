package com.binzeefox.kt_foxframe.tools.utils

import android.util.Log

/**
 * 日志工具类
 *
 *
 * 手动更改[.CURRENT_CLASS] 的值来改变当前的打印等级
 *
 * @author binze
 * 2019/11/19 9:31
 */
object LogUtil {
    const val CLASS_NONE = -1 //CURRENT_CLASS为此时，不打印Log
    const val CLASS_E = 0 //CURRENT_CLASS为此时，只打印E
    const val CLASS_W = 1 //CURRENT_CLASS为此时，只打印E和W
    const val CLASS_D = 2 //CURRENT_CLASS为此时，只打印E,W和D
    const val CLASS_I = 3 //CURRENT_CLASS为此时，只打印E,W,D和I
    const val CLASS_V = 4 //CURRENT_CLASS为此时，全部打印
    var CURRENT_CLASS = CLASS_V //打印指示器

    //V
    fun v(tag: CharSequence, text: CharSequence) {
        if (CURRENT_CLASS >= CLASS_V) Log.v(tag.toString(), text.toString())
    }

    fun v(tag: CharSequence, text: CharSequence, throwable: Throwable?) {
        if (CURRENT_CLASS >= CLASS_V) Log.v(tag.toString(), text.toString(), throwable)
    }

    //D
    fun d(tag: CharSequence, text: CharSequence) {
        if (CURRENT_CLASS >= CLASS_D) Log.d(tag.toString(), text.toString())
    }

    fun d(tag: CharSequence, text: CharSequence, throwable: Throwable?) {
        if (CURRENT_CLASS >= CLASS_D) Log.d(tag.toString(), text.toString(), throwable)
    }

    //I
    fun i(tag: CharSequence, text: CharSequence) {
        if (CURRENT_CLASS >= CLASS_I) Log.i(tag.toString(), text.toString())
    }

    fun i(tag: CharSequence, text: CharSequence, throwable: Throwable?) {
        if (CURRENT_CLASS >= CLASS_I) Log.i(tag.toString(), text.toString(), throwable)
    }

    //W
    fun w(tag: CharSequence, text: CharSequence) {
        if (CURRENT_CLASS >= CLASS_W) Log.w(tag.toString(), text.toString())
    }

    fun w(tag: CharSequence, text: CharSequence, throwable: Throwable?) {
        if (CURRENT_CLASS >= CLASS_W) Log.w(tag.toString(), text.toString(), throwable)
    }

    //E
    fun e(tag: CharSequence, text: CharSequence) {
        if (CURRENT_CLASS >= CLASS_E) Log.e(tag.toString(), text.toString())
    }

    fun e(tag: CharSequence, text: CharSequence, throwable: Throwable?) {
        if (CURRENT_CLASS >= CLASS_E) Log.e(tag.toString(), text.toString(), throwable)
    }
}