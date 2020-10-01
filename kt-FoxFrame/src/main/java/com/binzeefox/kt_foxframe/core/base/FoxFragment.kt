@file:Suppress("ProtectedInFinal", "MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.core.base

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.binzeefox.kt_foxframe.tools.navigate.Navigator
import com.binzeefox.kt_foxframe.tools.perform.LayoutTool
import com.binzeefox.kt_foxframe.tools.utils.RxUtil.setThreadComputation
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 碎片基类
 *
 * @author 狐彻
 * 2020/09/25 13:42
 */
abstract class FoxFragment : Fragment {

    // 构造器
    // @author 狐彻 2020/09/25 13:46
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    // Rx回收器
    // @author 狐彻 2020/09/25 13:46
    protected var dContainer = CompositeDisposable()

    // 二次点击判断相关
    // @author 狐彻 2020/09/25 10:10
    private val mTimerQueue = SparseArray<Disposable>()
    private val mFlagQueue = SparseBooleanArray()

    // 主视图
    // @author 狐彻 2020/09/25 13:50
    var root: View? = null

    // 视图工具
    // @author 狐彻 2020/09/25 13:33
    val layoutTool: LayoutTool
        get() = object : LayoutTool() {
        override fun <T : View> findView(id: Int): T? = view?.findViewById(id)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 抽象方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 接管的onCreateView实现
     *
     * @author 狐彻 2020/09/25 13:48
     */
    protected abstract fun create(root: View?, savedInstanceState: Bundle?)

    ///////////////////////////////////////////////////////////////////////////
    // 空实现继承方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 通过资源ID加载布局，优先级低
     *
     * @author 狐彻 2020/09/25 10:13
     */
    protected open fun onSetLayoutResource(): Int = -1

    /**
     * 通过View直接加载布局，优先级高
     *
     * @author 狐彻 2020/09/25 10:14
     */
    protected open fun onSetLayoutView(layoutInflater: LayoutInflater, container: ViewGroup?): View? = null

    /**
     * onCreate中加载布局之前的回调
     *
     * @author 狐彻 2020/09/25 10:26
     */
    protected open fun beforeInflate(savedInstanceState: Bundle?) {}

    ///////////////////////////////////////////////////////////////////////////
    // 生命周期
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 创建页面
     *
     * @author 狐彻 2020/09/25 13:49
     */
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        beforeInflate(savedInstanceState)
        root = onSetLayoutView(layoutInflater, container)
            ?: inflater.inflate(onSetLayoutResource(), container, false)
        create(root, savedInstanceState)
        return root
    }

    /**
     * 生命周期 onResume
     *
     * @author 狐彻 2020/09/25 13:53
     */
    override fun onResume() {
        super.onResume()
        if (dContainer.isDisposed) dContainer = CompositeDisposable()
    }

    /**
     * 生命周期 onDestroy
     *
     * @author 狐彻 2020/09/25 10:29
     */
    override fun onDestroy() {
        super.onDestroy()
        if (!dContainer.isDisposed) {
            dContainer.dispose()
            dContainer.clear()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 跳转
     *
     * @author 狐彻 2020/09/25 11:30
     */
    fun navigate(target: Class<*>) = Navigator(this).setTarget(target)

    /**
     * 跳转
     *
     * @author 狐彻 2020/09/25 11:31
     */
    fun navigate(intent: Intent) = Navigator(this).setIntent(intent)

    /**
     * 若该方法在timeout内用同一id调用两侧，则返回true。否则返回false
     *
     * @param timeout   判断时限，单位毫秒
     * @author 狐彻 2020/09/25 11:36
     */
    fun checkCallTwice(timeout: Long, id: Int): Boolean {
        var timer: Disposable? = mTimerQueue[id]
        val flag = mFlagQueue[id]

        if (timer != null && !timer.isDisposed && flag) {
            //在倒计时中
            timer.dispose()
            mFlagQueue.delete(id)
            mTimerQueue.delete(id)
            return true
        }

        timer?.dispose()
        mFlagQueue.put(id, true)
        val ob: Observable<Long> = Observable.timer(timeout, TimeUnit.MILLISECONDS)
            .setThreadComputation()
        timer = ob.subscribe { mFlagQueue.delete(id) }
        mTimerQueue.put(id, timer)
        return false
    }

    /**
     * 默认id-1
     *
     * @author 狐彻 2020/09/25 12:16
     */
    fun checkCallTwice(timeout: Long): Boolean {
        return checkCallTwice(timeout, -1)
    }
}