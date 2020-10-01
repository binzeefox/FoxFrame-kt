package com.binzeefox.kt_foxframe.tools.perform

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.widget.Toast
import com.binzeefox.kt_foxframe.components.FoxDialogFragment
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.utils.ResourcesTools.asStringRes

/**
 * 通知工具
 *
 * @author 狐彻
 * 2020/09/25 23:24
 */
class InformTool {

    // 静态池
    // @author 狐彻 2020/09/25 23:28
    private companion object {

        // 单例
        // @author 狐彻 2020/09/25 23:28
        val instance: InformTool by lazy { InformTool() }

        /**
         * 显示提示
         *
         * @author 狐彻 2020/09/25 23:31
         */
        fun showToastNow(resourceId: Int) {
            instance.mToast?.cancel()
            instance.mToast =
                Toast.makeText(FoxCore.instance.topActivity, resourceId, Toast.LENGTH_LONG)
                    .also { it.show() }
        }

        /**
         * 显示提示
         *
         * @author 狐彻 2020/09/25 23:31
         */
        fun showToastNow(text: CharSequence) {
            instance.mToast?.cancel()
            instance.mToast =
                Toast.makeText(FoxCore.instance.topActivity, text, Toast.LENGTH_LONG)
                    .also { it.show() }
        }

        ///////////////////////////////////////////////////////////////////////////
        // 弹窗
        ///////////////////////////////////////////////////////////////////////////

        /**
         * 注销弹窗
         *
         * @author 狐彻 2020/09/26 20:01
         */
        fun dismissDialog(){
            instance.mDialog?.dismiss()
        }

        /**
         * 显示弹窗，同一处理
         * 
         * @author 狐彻 2020/09/26 20:10
         */
        fun showDialog(dialog: Dialog){
            showDialog(dialog, dialog.hashCode().toString())
        }

        /**
         * 显示弹窗
         *
         * @author 狐彻 2020/09/26 20:13
         */
        fun showDialog(dialog: Dialog, tag: String){
            instance.getDialog(dialog).show(tag)
        }

        /**
         * 弹窗构造器
         *
         * @author 狐彻 2020/09/26 20:20
         */
        class DialogBuilder{
            var title: CharSequence? = null
            var message: CharSequence? = null
            var cancelText: CharSequence? = null
            var confirmText: CharSequence? = null
            var cancelable: Boolean = true
            var positiveBtn: DialogInterface.OnClickListener? = null
            var negativeBtn: DialogInterface.OnClickListener? = null
            var dismissListener: DialogInterface.OnDismissListener? = null

            fun setTitle(resId: Int): DialogBuilder {
                title = resId.asStringRes()
                return this
            }

            fun setTitle(text: String): DialogBuilder {
                title = text
                return this
            }

            fun setMessage(resId: Int): DialogBuilder {
                message = resId.asStringRes()
                return this
            }

            fun setMessage(text: String): DialogBuilder {
                message = text
                return this
            }

            fun setCancelText(resId: Int): DialogBuilder {
                cancelText = resId.asStringRes()
                return this
            }

            fun setCancelText(text: String): DialogBuilder {
                cancelText = text
                return this
            }

            fun setConfirmText(resId: Int): DialogBuilder {
                confirmText = resId.asStringRes()
                return this
            }

            fun setConfirmText(text: String): DialogBuilder {
                confirmText = text
                return this
            }

            fun create(): Dialog =
                AlertDialog
                    .Builder(FoxCore.instance.topActivity)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(cancelable)
                    .setPositiveButton(confirmText, positiveBtn)
                    .setNegativeButton(cancelText, negativeBtn)
                    .setOnDismissListener(dismissListener)
                    .create()

            fun show(){
                InformTool.showDialog(create())
            }

            fun show(tag: String){
                InformTool.showDialog(create(), tag)
            }
        }
    }

    // Toast实例
    // @author 狐彻 2020/09/25 23:29
    private var mToast: Toast? = null

    // Dialog实例
    // @author 狐彻 2020/09/26 19:59
    private var mDialog: FoxDialogFragment? = null

    /**
     * 获取mDialog实例，确保页面弹窗唯一
     * 
     * @author 狐彻 2020/09/26 20:08
     */
    private fun getDialog(dialog: Dialog): FoxDialogFragment{
        mDialog?.dismiss()
        mDialog = FoxDialogFragment().also { it.innerDialog = dialog }
        return mDialog!!
    } 
}