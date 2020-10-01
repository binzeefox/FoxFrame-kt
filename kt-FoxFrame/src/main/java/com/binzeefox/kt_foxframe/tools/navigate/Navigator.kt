package com.binzeefox.kt_foxframe.tools.navigate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.binzeefox.kt_foxframe.core.FoxCore

/**
 * 跳转器
 *
 * @author 狐彻
 * 2020/09/25 10:38
 */
class Navigator {

    /**
     * 执行器接口
     *
     * @author 狐彻 2020/09/25 10:41
     */
    interface Executor {
        fun setIntent(intent: Intent)
        fun commit()
        fun commit(options: Bundle)
        fun commitForResult(requestCode: Int)
        fun commitForResult(requestCode: Int, options: Bundle)
    }

    // 静态池
    // @author 狐彻 2020/09/25 11:01
    companion object {

        // 跳转参数键
        // @author 狐彻 2020/09/25 11:03
        const val PARAMS_BUNDLE = "params_bundle"

        /**
         * 通过Intent获取跳转内容
         *
         * @author 狐彻 2020/09/25 11:05
         */
        fun getDataFromNavigate(intent: Intent): Bundle {
            return intent.getBundleExtra(PARAMS_BUNDLE) ?: Bundle()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有字段
    ///////////////////////////////////////////////////////////////////////////

    // 执行器
    // @author 狐彻 2020/09/25 11:18
    private val executor: Executor

    ///////////////////////////////////////////////////////////////////////////
    // 构造器
    ///////////////////////////////////////////////////////////////////////////

    constructor(fragment: Fragment) {
        executor = FragmentExecutor(fragment)
    }

    constructor(activity: AppCompatActivity) {
        executor = ActivityExecutor(activity)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置目标Intent
     *
     * @author 狐彻 2020/09/25 11:18
     */
    fun setIntent(intent: Intent): Executor = executor.also { it.setIntent(intent) }

    /**
     * 目标跳转
     *
     * @author 狐彻 2020/09/25 11:23
     */
    fun setTarget(target: Class<*>): Executor {
        val intent = Intent(FoxCore.application, target)
        return executor.also { it.setIntent(intent) }
    }
}