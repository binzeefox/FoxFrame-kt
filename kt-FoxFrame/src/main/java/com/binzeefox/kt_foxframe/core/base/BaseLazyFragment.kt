package com.binzeefox.kt_foxframe.core.base

import android.os.Bundle
import android.view.View

/**
 * 懒加载Fragment
 *
 * @author 狐彻
 * 2020/09/25 13:57
 */
abstract class BaseLazyFragment : FoxFragment {

    // 构造器
    // @author 狐彻 2020/09/25 13:59
    constructor(): super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    // 是否完成加载
    // @author 狐彻 2020/09/25 14:00
    var isLoaded = false

    override fun onResume() {
        super.onResume()
        if (!isLoaded) onLoad() //初次加载
        isLoaded = true
    }

    override fun onDestroy() {
        super.onDestroy()
        //若不将其在此置false，onDestroy后可能类没有被回收，而导致再次加载时会失败
        isLoaded = false;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 抽象方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 初次加载
     *
     * @author 狐彻 2020/09/25 14:01
     */
    abstract fun onLoad()
}