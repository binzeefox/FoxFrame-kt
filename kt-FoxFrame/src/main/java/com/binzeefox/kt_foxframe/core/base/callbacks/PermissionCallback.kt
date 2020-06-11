package com.binzeefox.kt_foxframe.core.base.callbacks

/**
 * 获取权限 回调
 * @author binze
 * 2020/6/10 12:03
 */
interface PermissionCallback {
    fun callback(failedList: List<String>)
}