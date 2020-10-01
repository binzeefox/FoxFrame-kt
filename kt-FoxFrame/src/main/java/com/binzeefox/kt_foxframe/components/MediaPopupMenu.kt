@file:Suppress("MemberVisibilityCanBePrivate", "ClassName")

package com.binzeefox.kt_foxframe.components

import android.content.Context
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.binzeefox.kt_foxframe.tools.media.picker.MediaPicker
import com.binzeefox.kt_foxframe.tools.media.picker.MediaPickerParams
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * 调用系统相机相册底部弹窗
 *
 * @author 狐彻
 * 2020/09/30 19:37
 */
class MediaPopupMenu private constructor(private val manager: FragmentManager) {

    // 静态池
    // @author 狐彻 2020/09/30 22:03
    companion object {
        fun with(activity: AppCompatActivity): MediaPopupMenu =
            MediaPopupMenu(activity.supportFragmentManager)

        fun with(fragment: Fragment): MediaPopupMenu =
            MediaPopupMenu(fragment.childFragmentManager)

        fun with(manager: FragmentManager): MediaPopupMenu =
            MediaPopupMenu(manager)
    }

    /**
     * 参数
     *
     * @author 狐彻 2020/10/01 11:53
     */
    var galleryConfig: MediaPickerParams.GalleryConfig? = null
    var cameraConfig: MediaPickerParams.CameraConfig? = null
    var videoConfig: MediaPickerParams.VideoConfig? = null

    ///////////////////////////////////////////////////////////////////////////
    // 方法
    ///////////////////////////////////////////////////////////////////////////

    fun show(context: Context) {
        show(context, false)
    }

    fun show(context: Context, showVideo: Boolean) {
        //容器
        val container = CardView(context)
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).also {
            container.layoutParams = it
        }

        //列表
        val listView = ListView(context)
        ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        ).also {
            listView.layoutParams = it
        }
        val item = ArrayList<String>().also { item ->
            item.add("相册")
            item.add("拍照")
            if (showVideo) item.add("摄像")
        }
        //适配器
        object : ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, item) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                //列表文字居中
                val tv = super.getView(position, convertView, parent) as TextView
                tv.gravity = Gravity.CENTER
                return tv
            }
        }.also {
            listView.adapter = it
        }

        container.addView(listView)

        val dialog = BottomSheetDialog(context)
        dialog.setContentView(container)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> openSystemGallery()
                1 -> openSystemCamera()
                2 -> openSystemVideoRecorder()
            }
        }
        dialog.show()
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    private fun openSystemGallery() {
        MediaPicker.with(manager).open(galleryConfig)
    }

    private fun openSystemCamera() {
        MediaPicker.with(manager).open(cameraConfig)
    }

    private fun openSystemVideoRecorder() {
        MediaPicker.with(manager).open(videoConfig)
    }
}