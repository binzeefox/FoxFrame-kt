package com.binzeefox.kt_foxframe.tools.utils

import android.content.Context
import androidx.annotation.StringRes
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.utils.ResourcesTools.asStringRes
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * 本地化数据工具
 *
 * @author 狐彻
 * 2020/09/29 9:26
 */
object SharedPreferenceUtil {
    private const val TAG = "SharedPreferenceUtil"

    // 读写锁
    // @author 狐彻 2020/09/29 11:02
    private val lock = ReentrantReadWriteLock()

    /**
     * 写入器
     *
     * @author 狐彻 2020/09/29 9:37
     */
    class Writer(fileName: String) {

        // sp实例
        // @author 狐彻 2020/09/29 11:02
        private val editor = FoxCore.application
            .getSharedPreferences(fileName, Context.MODE_PRIVATE).edit()
        private val writeLock = lock.writeLock()

        init {
            writeLock.lock()
        }

        /**
         * 写入
         *
         * @author 狐彻 2020/09/29 11:11
         */
        @Synchronized
        fun write(key: String, content: Any): Writer {
            when (content) {
                is Int -> editor.putInt(key, content)
                is Long -> editor.putLong(key, content)
                is Float -> editor.putFloat(key, content)
                is Boolean -> editor.putBoolean(key, content)
                is String -> editor.putString(key, content)
            }
            return this
        }

        /**
         * 确定更变
         *
         * @author 狐彻 2020/09/29 11:22
         */
        fun commit() {
            editor.commit()
            writeLock.unlock()
        }
    }

    /**
     * 读取工具
     *
     * @author 狐彻 2020/09/29 11:16
     */
    class Reader(fileName: String) {

        // 本地化工具
        // @author 狐彻 2020/09/29 11:23
        private val sp = FoxCore.application
            .getSharedPreferences(fileName, Context.MODE_PRIVATE)

        // 读取锁
        // @author 狐彻 2020/09/29 11:24
        private val readLock = lock.readLock()

        init {
            readLock.lock()
        }

        /**
         * 读字符
         *
         * @author 狐彻 2020/09/29 11:34
         */
        fun read(key: String, defaultValue: String): String {
            try {
                return sp.getString(key, defaultValue) ?: ""
            } finally {
                readLock.unlock()
            }
        }

        /**
         * 读数字
         *
         * @author 狐彻 2020/09/29 11:38
         */
        fun read(key: String, defaultValue: Number): Number {
            return try {
                when(defaultValue) {
                    is Int -> sp.getInt(key, defaultValue)
                    is Long -> sp.getLong(key, defaultValue)
                    is Float -> sp.getFloat(key, defaultValue)
                    else -> {
                        LogUtil.w(TAG, "read: 读取失败")
                        defaultValue
                    }
                }
            } finally {
                readLock.unlock()
            }
        }

        /**
         * 读布尔值
         *
         * @author 狐彻 2020/09/29 11:39
         */
        fun read(key: String, defaultValue: Boolean): Boolean {
            return try {
                sp.getBoolean(key, defaultValue)
            } finally {
                readLock.unlock()
            }
        }
    }
}