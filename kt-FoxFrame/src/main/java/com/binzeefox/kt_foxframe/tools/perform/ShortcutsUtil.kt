@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.perform

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import com.binzeefox.kt_foxframe.core.FoxCore

/**
 * 快捷入口工具类
 * 长按图标显示的捷径，动态生成的捷径也可以一直存在
 * 无需静态配置
 *
 * @author 狐彻
 * 2020/09/28 20:31
 */
object ShortcutsUtil {
    private const val TAG = "ShortcutsUtil"

    // 快捷入口管理器
    // @author 狐彻 2020/09/28 20:38
    val shortcutManager: ShortcutManager?
        get() = FoxCore.application.getSystemService(ShortcutManager::class.java)

    // 获取动态快捷方式
    // @author 狐彻 2020/09/28 20:45
    val dynamicShortcuts: MutableList<ShortcutInfo>
        get() = shortcutManager?.dynamicShortcuts ?: ArrayList()

    /**
     * 添加动态快捷方式
     *
     * @return 是否添加成功
     * @author 狐彻 2020/09/28 20:47
     */
    fun addDynamicShortcuts(infoList: List<ShortcutInfo>): Boolean {
        return when {
            dynamicShortcuts.size + infoList.size
                    > (shortcutManager?.maxShortcutCountPerActivity ?: 0) -> false
            else -> shortcutManager?.addDynamicShortcuts(infoList) ?: false
        }
    }

    /**
     * 添加动态快捷方式
     *
     * @return 是否添加成功
     * @author 狐彻 2020/09/28 20:47
     */
    fun setDynamicShortcuts(infoList: List<ShortcutInfo>): Boolean = when {
        dynamicShortcuts.size + infoList.size
                > (shortcutManager?.maxShortcutCountPerActivity ?: 0) -> false
        else -> shortcutManager?.setDynamicShortcuts(infoList) ?: false
    }

    /**
     * 禁用快捷方式
     *
     * @param items   被禁用的快捷方式id
     * @param message 点击被禁用按钮的提示
     * @author 狐彻 2020/09/28 20:57
     */
    fun disableShortcuts(items: List<String>, message: String) {
        shortcutManager?.disableShortcuts(items, message)
    }

    /**
     * 移除快捷方式
     *
     * @param items 被禁用的快捷方式id
     * @author 狐彻 2020/09/28 20:58
     */
    fun removeShortcuts(items: List<String>) {
        shortcutManager?.removeDynamicShortcuts(items)
    }

    /**
     * 更新快捷方式
     *
     * @author 狐彻 2020/09/28 21:00
     */
    fun removeAllShortcuts(items: List<ShortcutInfo>) {
        shortcutManager?.updateShortcuts(items)
    }

    /**
     * 根据ID获取快捷方式
     *
     * @author 狐彻 2020/09/28 21:02
     */
    fun getShortcut(id: String): ShortcutInfo? {
        for (info in dynamicShortcuts)
            if (id == info.id) return info
        return null
    }
}