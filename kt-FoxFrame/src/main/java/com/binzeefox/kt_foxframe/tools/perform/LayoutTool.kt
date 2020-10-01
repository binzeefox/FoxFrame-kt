@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.perform

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.utils.RxUtil.setThreadIO
import com.binzeefox.kt_foxframe.tools.utils.SharedPreferenceUtil
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe

/**
 * 视图工具类
 *
 * @author 狐彻
 * 2020/09/25 12:19
 */
abstract class LayoutTool {

    /**
     * 找到View
     *
     * @author 狐彻 2020/09/25 12:27
     */
    abstract fun <T : View> findView(id: Int): T?

    /**
     * 通过ID获取控件文字
     *
     * @author 狐彻 2020/09/25 12:30
     */
    fun getString(id: Int): String = (id.toView(this) as? TextView)?.text.toString()

    /**
     * 设置异常状态
     *
     * @author 狐彻 2020/09/25 12:39
     */
    fun setError(id: Int, error: CharSequence?) {
        (id.toView(this) as? TextView)?.error = error
    }


    /**
     * 设置文字
     *
     * @author 狐彻 2020/09/25 12:49
     */
    fun setText(id: Int, text: CharSequence?) {
        (id.toView(this) as? TextView)?.text = text
    }

    /**
     * 检查可见度
     *
     * @author 狐彻 2020/09/25 12:54
     */
    fun checkVisibility(id: Int) = id.toView(this)?.visibility

    /**
     * 批量设置可见性
     *
     * @author 狐彻 2020/09/25 13:02
     */
    fun setViewsVisibility(visibility: Int, ids: Array<Int>) {
        for (id in ids) id.toView(this)?.visibility = visibility
    }

    /**
     * 批量设置可见性
     *
     * @author 狐彻 2020/09/25 13:06
     */
    fun setViewsVisibility(visibility: Int, views: Array<View>) {
        for (view in views) view.visibility = visibility
    }

    /**
     * 设置可用性
     *
     * @author 狐彻 2020/09/25 13:06
     */
    fun setEnable(id: Int, enable: Boolean) {
        id.toView(this)?.isEnabled = enable
    }

    /**
     * 批量设置可用性
     *
     * @author 狐彻 2020/09/25 13:07
     */
    fun setEnables(enable: Boolean, ids: Array<Int>) {
        for (id in ids) id.toView(this)?.isEnabled = enable
    }

    /**
     * 批量设置可用性
     *
     * @author 狐彻 2020/09/25 13:07
     */
    fun setEnables(enable: Boolean, views: Array<View>) {
        for (view in views) view.isEnabled = enable
    }

    /**
     * 同步加载视图
     *
     * @author 狐彻 2020/09/25 13:10
     */
    fun loadLayoutAsync(id: Int): Observable<View> = Observable.create(
        ObservableOnSubscribe<View> { emitter ->
            val view = LayoutInflater.from(FoxCore.instance.topActivity)
                .inflate(id, null)
            emitter?.onNext(view)
            emitter?.onComplete()
        }).setThreadIO()

    /**
     * 同步加载多视图
     *
     * @author 狐彻 2020/09/25 13:10
     */
    fun loadLayoutAsync(ids: Array<Int>): Observable<List<View>> = Observable.create(
        ObservableOnSubscribe<List<View>> { emitter ->
            val list = ArrayList<View>()
            for (id in ids) {
                val view = LayoutInflater.from(FoxCore.instance.topActivity)
                    .inflate(id, null)
                list.add(view)
            }
            emitter.onNext(list)
            emitter.onComplete()
        }).setThreadIO()

    ///////////////////////////////////////////////////////////////////////////
    // 拓展函数
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 数字转View
     *
     * @author 狐彻 2020/09/25 12:44
     */
    fun Int.toView(tool: LayoutTool): View? = tool.findView(this)
}