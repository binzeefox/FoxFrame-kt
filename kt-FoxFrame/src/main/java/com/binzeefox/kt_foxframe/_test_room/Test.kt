package com.binzeefox.kt_foxframe._test_room

import androidx.fragment.app.Fragment
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.core.base.FoxApplication
import com.binzeefox.kt_foxframe.tools.perform.permission.PermissionFragment

/**
 * c测试
 *
 * @author 狐彻
 * 2020/09/25 8:46
 */
class Test: FoxApplication() {

    override fun onCreate() {
        super.onCreate()
        FoxCore.instance.versionCode
    }

    fun test(){
        FoxCore.instance
        val fragment: Fragment = PermissionFragment()
//        PermissionUtil.with(fragment).
    }
}