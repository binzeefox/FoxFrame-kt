@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.utils.StatusInfoUtil

/**
 * 自定义DialogFragment
 *
 * @author 狐彻
 * 2020/09/25 23:35
 */
class FoxDialogFragment : AppCompatDialogFragment() {

    // 弹窗
    // @author 狐彻 2020/09/25 23:39
    var innerDialog: Dialog = AlertDialog.Builder(FoxCore.instance.topActivity).create()


    /**
     * 生命周期
     *
     * @author 狐彻 2020/09/26 19:51
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = innerDialog

    ///////////////////////////////////////////////////////////////////////////
    // 公共方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 使用返回栈顶端的FragmentManager
     *
     * @author 狐彻 2020/09/26 19:56
     */
    fun show(tag: String) {
        show(FoxCore.instance.topActivity.supportFragmentManager, tag)
    }
}