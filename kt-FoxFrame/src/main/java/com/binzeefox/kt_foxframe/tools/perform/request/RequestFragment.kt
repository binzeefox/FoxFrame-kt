@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.perform.request

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 请求碎片
 *
 * @author 狐彻
 * 2020/09/29 15:05
 */
class RequestFragment : Fragment() {

    /**
     * 结果回调
     *
     * @author 狐彻 2020/10/01 10:18
     */
    fun interface OnResultListener {
        fun onResult(data: Intent?, resultCode: Int, requestCode: Int)
    }

    /**
     * 参数
     *
     * @author 狐彻 2020/09/29 15:25
     */
    class Params {
        var requestCode: Int = 0x00030
        var intent: Intent? = null
        var onResult: OnResultListener? = null
    }

    internal lateinit var mParams: Params

    /**
     * 生命周期
     *
     * @author 狐彻 2020/09/29 15:19
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * 请求开始
     *
     * @author 狐彻 2020/09/29 15:20
     */
    internal fun request() {
        startActivityForResult(mParams.intent, mParams.requestCode)
    }

    /**
     * 请求结果
     *
     * @author 狐彻 2020/09/29 15:22
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mParams.onResult?.onResult(data, resultCode, requestCode)
    }
}