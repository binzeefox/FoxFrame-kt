@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.utils

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * RxJava工具
 *
 * @author 狐彻
 * 2020/09/25 11:46
 */
object RxUtil {

    /**
     * 线程控制
     *
     * IO线程运行，主线程观察
     * @author 狐彻 2020/09/25 12:03
     */
    fun <T> setThreadIO() = ObservableTransformer<T, T> {
        it.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 线程控制
     *
     * 计算线程运行，主线程观察
     * @author 狐彻 2020/09/25 12:03
     */
    fun <T> setThreadComputation() = ObservableTransformer<T, T> {
        it.subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 线程控制
     *
     * 新线程运行，主线程观察
     * @author 狐彻 2020/09/25 12:03
     */
    fun <T> setThreadNew() = ObservableTransformer<T, T> {
        it.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
    }

    ///////////////////////////////////////////////////////////////////////////
    // 拓展
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 拓展
     *
     * @author 狐彻 2020/09/25 13:22
     */
    fun <T> Observable<T>.setThreadIO(): Observable<T> {
        this.compose(RxUtil.setThreadIO())
        return this
    }

    /**
     * 拓展
     *
     * @author 狐彻 2020/09/25 13:22
     */
    fun <T> Observable<T>.setThreadComputation(): Observable<T> {
        this.compose(RxUtil.setThreadComputation())
        return this
    }

    /**
     * 拓展
     *
     * @author 狐彻 2020/09/25 13:22
     */
    fun <T> Observable<T>.setThreadNew(): Observable<T> {
        this.compose(RxUtil.setThreadNew())
        return this
    }
}