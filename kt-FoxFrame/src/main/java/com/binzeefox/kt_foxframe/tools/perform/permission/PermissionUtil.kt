@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.perform.permission

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.perform.request.RequestFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 请求权限回调
 *
 * TODO 封装过程写成笔记
 * @author 狐彻
 * 2020/09/25 16:17
 */
class PermissionUtil constructor(val fragmentManager: FragmentManager) {

    // 静态池
    // @author 狐彻 2020/09/25 16:19
    companion object {
        private const val TAG = "PermissionUtil"
        private const val FRAGMENT_TAG = "request_permission_fragment_tag"

        /**
         * activity初始化
         *
         * @author 狐彻 2020/09/25 17:20
         */
        fun with(activity: AppCompatActivity): PermissionUtil =
            PermissionUtil(activity.supportFragmentManager)

        /**
         * fragment初始化
         *
         * @author 狐彻 2020/09/25 17:21
         */
        fun with(fragment: Fragment): PermissionUtil =
            PermissionUtil(fragment.childFragmentManager)
    }

    // 功能承担Fragment
    // @author 狐彻 2020/09/25 17:18
    private val mFragment: PermissionFragment get() {
        var fragment: PermissionFragment? =
            fragmentManager.findFragmentByTag(FRAGMENT_TAG) as? PermissionFragment
        fragment = fragment ?: PermissionFragment().also {
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
     * 检查权限
     *
     * @author 狐彻 2020/10/01 12:02
     */
    fun check(params: PermissionFragment.Params) {
        mFragment.mParams = params
        mFragment.check()
    }

    /**
     * 检查权限
     *
     * @author 狐彻 2020/09/25 17:23
     */
    fun check(params: PermissionFragment.Params.() -> Unit) {
        mFragment.mParams = PermissionFragment.Params().apply(params)
        mFragment.check()
    }

    /**
     * 检查并请求权限
     *
     * @author 狐彻 2020/10/01 12:02
     */
    fun checkAndRequest(params: PermissionFragment.Params) {
        mFragment.mParams = params
        mFragment.checkAndRequest()
    }

    /**
     * 检查并请求权限
     *
     * @author 狐彻 2020/09/25 17:23
     */
    fun checkAndRequest(params: PermissionFragment.Params.() -> Unit) {
        mFragment.mParams = PermissionFragment.Params().apply(params)
        mFragment.checkAndRequest()
    }
}