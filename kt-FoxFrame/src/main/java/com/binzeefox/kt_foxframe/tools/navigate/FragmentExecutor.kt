package com.binzeefox.kt_foxframe.tools.navigate

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 基于Fragment的跳转器
 *
 * @author 狐彻
 * 2020/09/25 10:58
 */
internal class FragmentExecutor(
    private val fragment: Fragment
): Navigator.Executor {

    private var intent: Intent? = null

    override fun setIntent(intent: Intent) {
        this.intent = intent
    }

    override fun commit() {
        fragment.startActivity(intent)
    }

    override fun commit(options: Bundle) {
        fragment.startActivity(intent, options)
    }

    override fun commitForResult(requestCode: Int) {
        fragment.startActivityForResult(intent, requestCode)
    }

    override fun commitForResult(requestCode: Int, options: Bundle) {
        fragment.startActivityForResult(intent, requestCode, options)
    }
}