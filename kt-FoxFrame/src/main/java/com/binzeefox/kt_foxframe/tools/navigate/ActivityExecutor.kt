package com.binzeefox.kt_foxframe.tools.navigate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 基于Activity的跳转器
 *
 * @author 狐彻
 * 2020/09/25 10:47
 */
internal class ActivityExecutor(
    private val activity: AppCompatActivity
) : Navigator.Executor {

    private var intent: Intent? = null

    override fun setIntent(intent: Intent) {
        this.intent = intent
    }

    override fun commit() {
        activity.startActivity(intent)
    }

    override fun commit(options: Bundle) {
        activity.startActivity(intent, options)
    }

    override fun commitForResult(requestCode: Int) {
        activity.startActivityForResult(intent, requestCode)
    }

    override fun commitForResult(requestCode: Int, options: Bundle) {
        activity.startActivityForResult(intent, requestCode, options)
    }
}