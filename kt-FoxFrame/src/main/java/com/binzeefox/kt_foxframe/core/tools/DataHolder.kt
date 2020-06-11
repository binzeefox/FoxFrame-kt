package com.binzeefox.kt_foxframe.core.tools

import android.util.Log
import java.lang.Exception
import java.util.concurrent.locks.ReentrantReadWriteLock

class DataHolder {
    companion object {
        private const val TAG = "DataHolder"

        interface Callback{ //数据变化回调
            fun onCall(key: String, value: Any)
        }
    }

    private val mData: HashMap<String, Any> = HashMap()    //数据容器
    private val mEntry: HashMap<String, Callback> = HashMap()   //回调容器
    private val mDataLock: ReentrantReadWriteLock = ReentrantReadWriteLock();   //数据读写锁
    private val mEntryLock: ReentrantReadWriteLock = ReentrantReadWriteLock();  //回调读写锁

    /**
     * 订阅数据变化
     * @param key   回调id
     * @param callback  回调实例
     * @author binze 2020/6/10 10:57
     */
    fun submit(key: String, callback: Callback){
        mEntryLock.writeLock().lock()
        try {
            if (mData.containsKey(key))
                Log.w(TAG, "submit: 字段冲突，覆盖回调" )
            mEntry[key] = callback
        } finally {
            mEntryLock.writeLock().unlock()
        }
    }

    /**
     * 解除数据变化回调
     * @param key   回调ID
     * @author binze 2020/6/10 11:00
     */
    fun unsubmit(key: String){
        mEntryLock.writeLock().lock()
        try {
            mEntry.remove(key)
        } finally {
            mEntryLock.writeLock().unlock()
        }
    }

    /**
     * 修改数据
     * @author binze 2020/6/10 11:05
     */
    fun set(key: String, value: Any){
        mDataLock.writeLock().lock();
        try {
            mData[key] = value
            mEntry.values.forEach {
                mEntryLock.readLock().lock()
                try {
                    it.onCall(key, value)
                } finally {
                    mEntryLock.readLock().unlock()
                }
            }
        } finally {
            mDataLock.writeLock().unlock()
        }
    }

    /**
     * 读取数据
     * @param key   字段名
     * @param defaultValue  默认值
     * @author binze 2020/6/10 11:14
     */
    fun <T> read(key: String, defaultValue: T): T {
        mDataLock.readLock().lock()
        return try {
            (mData[key]?:defaultValue) as T
        } catch (e: Exception){
            Log.e(TAG, "read: ", e)
            defaultValue
        } finally {
            mDataLock.readLock().unlock()
        }
    }

    /**
     * 移除数据
     * @author binze 2020/6/10 11:18
     */
    fun remove(key: String): Any?{
        mDataLock.writeLock().lock()
        return try {
            mData.remove(key)
        } catch (e: Exception){
            Log.e(TAG, "remove: ", e)
            null
        } finally {
            mDataLock.writeLock().unlock()
        }
    }
}