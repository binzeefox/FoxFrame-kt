package com.binzeefox.kt_foxframe.core.base

import android.os.Bundle
import android.os.PersistableBundle
import android.util.SparseArray
import android.util.SparseBooleanArray
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.binzeefox.kt_foxframe.core.base.callbacks.PermissionCallback
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

/**
 * Activity基类
 * @author binze
 * 2020/6/10 12:05
 */
abstract class FoxActivity : AppCompatActivity() {
    private companion object

    val TAG = "FoxActivity"
    protected val dContainer = CompositeDisposable()    //Rx回收器

    private val mTimerQueue: SparseArray<Disposable> = SparseArray()
    private val mFlagQueue = SparseBooleanArray()   //二次点击标识

    /**
     * 通过资源ID加载布局，优先级较低
     * @author binze 2020/6/10 12:12
     */
    protected open fun onSetLayoutResource(): Int {
        return -1
    }

    /**
     * 通过View加载布局，优先级较高
     * @author binze 2020/6/10 12:13
     */
    protected open fun onSetLayoutView(): View? {
        return null
    }

    /**
     * 接管onCreate 业务部分
     * @author binze 2020/6/10 12:14
     */
    protected abstract fun create(savedInstanceState: Bundle?, persistentState: PersistableBundle?)

    /**
     * 生命周期 重写接管于create()
     * @author binze 2020/6/10 12:19
     */
    final override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        //设置布局
        if (onSetLayoutView() != null)
            setContentView(onSetLayoutView())
        else if (onSetLayoutResource() != -1)
            setContentView(onSetLayoutResource())

        create(savedInstanceState, persistentState)
    }

    /**
     * 生命周期
     * @author binze 2020/6/10 12:24
     */
    override fun onDestroy() {
        super.onDestroy()
        dContainer.dispose()
        dContainer.clear()
    }

    /**
     * 请求权限
     * @author binze 2020/6/10 12:15
     */
    protected open fun requestPermission(
        permissionList: List<String>,
        callback: PermissionCallback
    ) {
        //TODO 需要完成 PermissionUtil.class
    }

    //TODO 需要完成 Navigator.class
}