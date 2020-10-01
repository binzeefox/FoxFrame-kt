package com.binzeefox.kt_foxframe.tools.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors


// 线程池基础
//
// corePoolSize: 核心线程数，默认情况下会在线程池中一直存活
// maximumPoolSize: 最大线程数，当活跃线程到达该数目时，后续线程进入队列等待
// keepAliveTime: 非核心线程闲置超时，超时后，闲置的非核心线程将被回收
// workQueue: 任务队列，储存线程
//
// FixedThreadPool: 固定线程数，只有核心线程
// CachedThreadPool: 非固定线程数，只有非核心线程
// ScheduledThreadPool: 核心线程数固定，非核心线程数无限制，常用于执行定时任务和又周期性的任务
// SingleThreadPool: 只有一个核心线程，确保所有任务都在统一线程按顺序执行

/**
 * 线程工具
 *
 * 虽然有协程了，线程也不能丢
 * @author 狐彻
 * 2020/09/25 23:00
 */
class ThreadUtil {

    // 双重锁单例
    // @author 狐彻 2020/09/25 9:02
    companion object {
        val instance: ThreadUtil   //单例
                by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED)
                { ThreadUtil() }

        /**
         * 主线程运行
         *
         * @author 狐彻 2020/09/25 23:05
         */
        fun runOnUiThread(work: Runnable) {
            val handler = Handler(Looper.getMainLooper())
            handler.post(work)
        }

        /**
         * 运行方法
         *
         * @author 狐彻 2020/09/25 23:06
         */
        fun execute(work: Runnable) = instance.mExecutor.execute(work)
    }


    // 选用Cached线程池
    // @author 狐彻 2020/09/25 23:02
    private val mExecutor = Executors.newCachedThreadPool()
}