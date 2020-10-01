@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.perform.permission

import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.binzeefox.kt_foxframe.core.FoxCore

/**
 * 请求权限专用Fragment
 *
 * @author 狐彻
 * 2020/09/25 16:21
 */
class PermissionFragment : Fragment() {

    // 静态池
    // @author 狐彻 2020/09/25 16:22
    companion object {
        private const val REQUEST_CODE = 0XFF01 //请求码
    }

    fun interface OnResultListener {
        fun onResult(failedList: List<String>, noAskList: List<String>)
    }

    /**
     * 回调参数
     *
     * @author 狐彻 2020/09/25 16:31
     */
    class Params {
        var permissionList: List<String> = ArrayList()
        var onResult: OnResultListener? = null
    }

    /**
     * 参数
     *
     * @author 狐彻 2020/10/01 11:04
     */
    internal lateinit var mParams: Params

    /**
     * 创建
     *
     * @author 狐彻 2020/09/25 16:41
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    /**
     * 检查权限
     *
     * @author 狐彻 2020/09/25 16:45
     */
    internal fun check() {
        val resultArray = checkPermission()
        mParams.onResult?.onResult(resultArray[0], resultArray[1])
    }

    /**
     * 检查并请求权限
     *
     * @author 狐彻 2020/09/25 17:00
     */
    internal fun checkAndRequest() {
        val resultArray = checkPermission()
        if (resultArray[0].isEmpty() && resultArray[1].isEmpty())
            mParams.onResult
        else requestPermissions(resultArray[0].toTypedArray(), REQUEST_CODE)
    }

    /**
     * 权限请求回调
     *
     * @author 狐彻 2020/09/25 17:04
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val ctx = FoxCore.instance.topActivity
        if (requestCode != REQUEST_CODE) return
        val failedList = ArrayList<String>()
        val noAskList = ArrayList<String>()
        for ((i, v) in permissions.withIndex()) {
            if (grantResults[i] != PERMISSION_GRANTED) {
                failedList.add(v)
                if (!ActivityCompat.shouldShowRequestPermissionRationale(ctx, v))
                    noAskList.add(v)
            }
        }

        // 回到主协程
        // @author 狐彻 2020/09/25 22:36
        mParams.onResult?.onResult(failedList, noAskList)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 原生检查权限
     *
     * @author 狐彻 2020/09/25 16:46
     */
    private fun checkPermission(): Array<List<String>> {
        val failedList = ArrayList<String>()
        val noAskList = ArrayList<String>()
        val ctx = FoxCore.instance.topActivity

        mParams.permissionList.forEach {
            val result = ActivityCompat.checkSelfPermission(ctx, it)
            if (result != PERMISSION_GRANTED) {
                failedList.add(it)
                if (!ActivityCompat.shouldShowRequestPermissionRationale(ctx, it))
                    noAskList.add(it)
            }
        }
        return arrayOf(failedList, noAskList)
    }
}