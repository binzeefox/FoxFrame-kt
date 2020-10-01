@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.perform.request

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.perform.permission.PermissionFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 请求工具类
 *
 * @author 狐彻
 * 2020/09/29 14:58
 */
class RequestHelper private constructor(private val fragmentManager: FragmentManager) {

    // 静态池
    // @author 狐彻 2020/09/29 15:01
    companion object {
        private const val FRAGMENT_TAG = "request_fragment_tag"

        fun with(activity: AppCompatActivity): RequestHelper =
            RequestHelper(activity.supportFragmentManager)

        fun with(fragment: Fragment): RequestHelper =
            RequestHelper(fragment.childFragmentManager)

        fun with(fragmentManager: FragmentManager) =
            RequestHelper(fragmentManager)
    }

    // 业务搭载Fragment
    // @author 狐彻 2020/09/29 15:34
    private val mFragment: RequestFragment get() {
        var fragment: RequestFragment? =
            fragmentManager.findFragmentByTag(FRAGMENT_TAG) as? RequestFragment
        fragment = fragment ?: RequestFragment().also {
            fragmentManager.beginTransaction()
                .add(it, FRAGMENT_TAG)
                .commitNow()
        }
        return fragment
    }

    ///////////////////////////////////////////////////////////////////////////
    // 参数方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 开始请求
     *
     * @author 狐彻 2020/10/01 12:03
     */
    fun request(params: RequestFragment.Params) {
        mFragment.mParams = params
        mFragment.request()
    }

    /**
     * 开始请求
     *
     * @author 狐彻 2020/09/29 15:30
     */
    fun request(params: RequestFragment.Params.() -> Unit) {
        mFragment.mParams = RequestFragment.Params().apply(params)
        mFragment.request()
    }
}