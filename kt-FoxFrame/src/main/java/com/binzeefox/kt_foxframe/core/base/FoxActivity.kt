@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.core.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.navigate.Navigator
import com.binzeefox.kt_foxframe.tools.perform.LayoutTool
import com.binzeefox.kt_foxframe.tools.utils.RxUtil.setThreadComputation
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

/**
 * 活动基类
 *
 * @author 狐彻
 * 2020/09/25 9:58
 */
abstract class FoxActivity : AppCompatActivity(), CoroutineScope {

    // Rx回收器，全部用RxJava3
    // @author 狐彻 2020/09/25 10:01
    protected var dContainer = CompositeDisposable()

    // 二次点击判断相关
    // @author 狐彻 2020/09/25 10:10
    private val mTimerQueue = SparseArray<Disposable>()
    private val mFlagQueue = SparseBooleanArray()

    // 上页面的数据
    // @author 狐彻 2020/09/25 11:34
    val dataFromNavigate: Bundle get() = Navigator.getDataFromNavigate(intent)

    // 视图工具
    // @author 狐彻 2020/09/25 13:33
    val layoutTool: LayoutTool get() = object : LayoutTool() {
        override fun <T : View> findView(id: Int): T? = findViewById(id)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 实现协程接口
    ///////////////////////////////////////////////////////////////////////////

    // 协程
    // @author 狐彻 2020/09/25 22:02
    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    ///////////////////////////////////////////////////////////////////////////
    // 抽象方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 接管的onCreate实现方法
     *
     * @author 狐彻 2020/09/25 10:27
     */
    protected abstract fun create(savedInstanceState: Bundle?)

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
    protected open fun onSetLayoutView(): View? = null

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
     * onCreate接管
     *
     * @author 狐彻 2020/09/25 10:15
     */
    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        beforeInflate(savedInstanceState)
        FoxCore.instance.activityStack.push(this)   //注册模拟返回栈

        //加载布局
        if (onSetLayoutView() != null) setContentView(onSetLayoutView())
        else setContentView(onSetLayoutResource())

        create(savedInstanceState)
    }

    /**
     * 生命周期 onResume
     *
     * @author 狐彻 2020/09/25 10:28
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
        FoxCore.instance.activityStack.remove(this) //注销模拟返回栈
        if (!dContainer.isDisposed) {
            dContainer.dispose()
            dContainer.clear()
        }
        job.cancel()
    }

    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 设置全屏
     *
     * @author 狐彻 2020/09/25 10:31
     */
    fun fullScreen() {
        val decorView = window.decorView
        val option: Int =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = option
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

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