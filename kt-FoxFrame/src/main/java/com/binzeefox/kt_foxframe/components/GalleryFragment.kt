package com.binzeefox.kt_foxframe.components

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.binzeefox.kt_foxframe.R
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.core.base.BaseLazyFragment
import com.binzeefox.kt_foxframe.core.base.FoxActivity
import com.binzeefox.kt_foxframe.tools.media.ImageLoader
import com.binzeefox.kt_foxframe.tools.media.picker.MediaPickerParams
import com.binzeefox.kt_foxframe.tools.utils.LogUtil
import com.binzeefox.kt_foxframe.tools.utils.UriTools
import com.binzeefox.kt_foxframe.tools.utils.UriTools.Companion.mimeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

/**
 * 相册Fragment
 *
 * TODO 待测试
 * @author 狐彻
 * 2020/09/29 19:49
 */
class GalleryFragment : BaseLazyFragment() {

    /**
     * 相册事件回调
     *
     * @author 狐彻 2020/09/29 20:01
     */
    interface GalleryCallback {
        fun onPressItem(uri: Uri?)
        fun onAddMedia(uri: Uri?)
    }

    /**
     * 加载布局回调
     *
     * @author 狐彻 2020/09/29 20:02
     */
    interface ItemViewCallback {
        // 创建布局
        // @author 狐彻 2020/09/29 20:03
        fun createAddButton(parent: ViewGroup): View
        fun createImageItem(parent: ViewGroup): View
        fun createVideoItem(parent: ViewGroup): View
        fun createUnknownItem(parent: ViewGroup): View

        // 填充布局内容
        // @author 狐彻 2020/09/29 20:03
        fun onSetUpAddButton(container: ViewGroup)
        fun onSetUpImageItem(uri: Uri, container: ViewGroup)
        fun onSetUpVideoItem(uri: Uri, container: ViewGroup)
        fun onSetUpUnknownItem(uri: Uri, container: ViewGroup)
    }

    /**
     * ViewHolder
     *
     * @author 狐彻 2020/09/29 20:05
     */
    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // 静态池
    // @author 狐彻 2020/09/29 19:51
    companion object {
        const val PARAMS_MEDIA_LIST = "params_media_list"
        const val PARAMS_SHOW_VIDEO = "params_show_video"
        const val PARAMS_SHOW_ADD = "params_show_add"
        const val PARAMS_SHOW_UNKNOWN = "params_show_known"

        private const val TAG = "GalleryFragment"
        private const val TYPE_ADD = 0
        private const val TYPE_IMAGE = 1
        private const val TYPE_VIDEO = 2
        private const val TYPE_UNKNOWN = -1
    }

    // 内部维护的RecyclerView
    // @author 狐彻 2020/09/29 19:57
    private lateinit var mInnerListView: RecyclerView

    // 列表适配器
    // @author 狐彻 2020/09/29 20:55
    private var mListAdapter = ListAdapter()

    // 要显示的内容，通过arguments输入
    // @author 狐彻 2020/09/29 20:55
    private var mediaList: ArrayList<Uri> = ArrayList()
        set(value) {
            field = value
            mListAdapter.notifyDataSetChanged()
        }

    // 过滤后的内容
    // @author 狐彻 2020/10/01 9:02
    private val _mediaList = ArrayList<Uri>()

    // 是否显示视频
    // @author 狐彻 2020/09/29 19:58
    var showVideo = false
        set(value) {
            field = value
            mListAdapter.notifyDataSetChanged()
        }

    // 是否显示无法显示项
    // @author 狐彻 2020/09/29 19:58
    var showUnknown = false
        set(value) {
            field = value
            mListAdapter.notifyDataSetChanged()
        }

    // 是否显示新增按钮
    // @author 狐彻 2020/09/29 19:58
    var showAdd = true
        set(value) {
            field = value
            mListAdapter.notifyDataSetChanged()
        }

    // 相册回调
    // @author 狐彻 2020/09/29 20:06
    var galleryCallback: GalleryCallback? = null

    // 子项布局回调
    // @author 狐彻 2020/09/29 20:49
    var itemViewCallback: ItemViewCallback = object : ItemViewCallback {
        override fun createAddButton(parent: ViewGroup): View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_fragment_add, parent, false)

        override fun createImageItem(parent: ViewGroup): View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_fragment_image, parent, false)

        override fun createVideoItem(parent: ViewGroup): View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_fragment_video, parent, false)

        override fun createUnknownItem(parent: ViewGroup): View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_fragment_unknown, parent, false)

        override fun onSetUpAddButton(container: ViewGroup) {
            container.findViewById<View>(R.id.press_archer)
                .setOnClickListener {
                    onPressAdd(it)
                }
        }

        override fun onSetUpImageItem(uri: Uri, container: ViewGroup) {
            val view = container.findViewById<ImageView>(R.id.item_fragment_gallery_view)
            view.setImageResource(R.drawable.ic_baseline_image_66)
            (FoxCore.instance.topActivity as FoxActivity).launch(Dispatchers.IO) {
                try {
                    val d = ImageLoader.get(uri).decodeDrawable()
                    setImageMain(d, view)
                } catch (e: IOException) {
                    LogUtil.e(TAG, "onSetUpImageItem: 加载图片失败", e);
                    setImageMain(null, view)
                }
                container.findViewById<View>(R.id.press_archer).setOnClickListener {
                    galleryCallback?.onPressItem(uri)
                }
            }
        }

        override fun onSetUpVideoItem(uri: Uri, container: ViewGroup) {
            val view = container.findViewById<ImageView>(R.id.item_fragment_gallery_view)
            view.setImageResource(R.drawable.ic_baseline_image_66)
            (FoxCore.instance.topActivity as FoxActivity).launch(Dispatchers.IO) {
                try {
                    val d = ImageLoader.get(uri).decodeDrawable()
                    setImageMain(d, view)
                } catch (e: IOException) {
                    LogUtil.e(TAG, "onSetUpImageItem: 加载图片失败", e);
                    setImageMain(null, view)
                }
                container.findViewById<View>(R.id.press_archer).setOnClickListener {
                    galleryCallback?.onPressItem(uri)
                }
            }
        }

        override fun onSetUpUnknownItem(uri: Uri, container: ViewGroup) {
            val view = container.findViewById<ImageView>(R.id.item_fragment_gallery_view)
            view.setImageResource(R.drawable.ic_baseline_broken_image_66)
            container.findViewById<View>(R.id.press_archer).setOnClickListener {
                galleryCallback?.onPressItem(uri)
            }
        }

        /**
         * 主协程设置图片
         *
         * @author 狐彻 2020/09/29 20:33
         */
        private fun setImageMain(d: Drawable?, view: ImageView?) {
            (FoxCore.instance.topActivity as FoxActivity).launch(Dispatchers.Main) {
                if (d == null) view?.setImageResource(R.drawable.ic_baseline_broken_image_66)
                else view?.setImageDrawable(d)
            }
        }
    }

    override fun onLoad() {
        //过滤数据源
        //若全部显示，则不过滤
        if (showAdd || showUnknown) return
        _mediaList.clear()
        mediaList.forEach { uri ->
            if (!showVideo && checkType(uri) == TYPE_VIDEO) return
            if (!showUnknown && checkType(uri) == TYPE_UNKNOWN) return
            _mediaList.add(uri)
        }

        mInnerListView.adapter = mListAdapter
    }

    override fun onSetLayoutView(layoutInflater: LayoutInflater, container: ViewGroup?): View? =
        mInnerListView.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

    override fun create(root: View?, savedInstanceState: Bundle?) {
        arguments ?: return
        mediaList = arguments?.getParcelableArrayList(PARAMS_MEDIA_LIST) ?: ArrayList()
        showVideo = arguments?.getBoolean(PARAMS_SHOW_VIDEO) ?: showVideo
        showAdd = arguments?.getBoolean(PARAMS_SHOW_ADD) ?: showAdd
        showUnknown = arguments?.getBoolean(PARAMS_SHOW_UNKNOWN) ?: showUnknown

        mInnerListView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(view!!.context, 3, RecyclerView.VERTICAL, false)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 检查格式
     *
     * @author 狐彻 2020/10/01 9:05
     */
    private fun checkType(uri: Uri): Int {
        //检查格式
        return when {
            uri.mimeType?.startsWith("image") ?: false -> TYPE_IMAGE
            uri.mimeType?.startsWith("video") ?: false -> TYPE_VIDEO
            else -> TYPE_UNKNOWN
        }
    }

    /**
     * 点击增加按钮
     *
     * @author 狐彻 2020/09/29 20:25
     */
    open fun onPressAdd(view: View) {
        MediaPopupMenu.with(this).apply {
            showAdd = true
            showUnknown = true
            showVideo = true
            cameraConfig = MediaPickerParams.CameraConfig().apply {
                requestCode = 10
                resultUri = getCacheUri(TYPE_IMAGE)
                onResult = MediaPickerParams.OnResultListener { mediaUri, _, _, requestCode ->
                    if (requestCode != this.requestCode) return@OnResultListener
                    galleryCallback?.onAddMedia(mediaUri)
                }
            }
            galleryConfig = MediaPickerParams.GalleryConfig().apply {
                requestCode = 11
                onResult = MediaPickerParams.OnResultListener { mediaUri, _, _, requestCode ->
                    if (requestCode != this.requestCode) return@OnResultListener
                    galleryCallback?.onAddMedia(mediaUri)
                }
            }
            videoConfig = MediaPickerParams.VideoConfig().apply {
                requestCode = 12
                resultUri = getCacheUri(TYPE_VIDEO)
                onResult = MediaPickerParams.OnResultListener{ mediaUri, _, _, requestCode ->
                    if (requestCode != this.requestCode) return@OnResultListener
                    galleryCallback?.onAddMedia(mediaUri)
                }
            }
        }
    }

    /**
     * 获取缓存路径
     *
     * @author 狐彻 2020/10/01 12:24
     */
    open fun getCacheUri(type: Int): Uri? {
        val types: String = when(type) {
            TYPE_VIDEO -> "mp4"
            TYPE_IMAGE -> "jpeg"
            else -> return null
        }
        File("${FoxCore.application.cacheDir.path}${File.separator}temp_${System.currentTimeMillis()}.$types")
            .also {
                return UriTools.fileToUri(it, FoxCore.instance.authority)
            }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 内部类
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 列表适配器
     *
     * @author 狐彻 2020/09/29 20:08
     */
    private inner class ListAdapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            when (viewType) {
                TYPE_ADD -> ViewHolder(itemViewCallback.createAddButton(parent))
                TYPE_IMAGE -> ViewHolder(itemViewCallback.createImageItem(parent))
                TYPE_VIDEO -> ViewHolder(itemViewCallback.createVideoItem(parent))
                else -> ViewHolder(itemViewCallback.createUnknownItem(parent))
            }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val pos = when (showAdd) {
                true -> position - 1
                false -> position
            }

            when (holder.itemViewType) {
                TYPE_ADD -> itemViewCallback.onSetUpAddButton(holder.itemView as ViewGroup)
                TYPE_IMAGE -> itemViewCallback.onSetUpImageItem(
                    _mediaList[pos],
                    holder.itemView as ViewGroup
                )
                TYPE_VIDEO -> itemViewCallback.onSetUpVideoItem(
                    _mediaList[pos],
                    holder.itemView as ViewGroup
                )
                else -> itemViewCallback.onSetUpUnknownItem(
                    _mediaList[pos],
                    holder.itemView as ViewGroup
                )
            }
        }

        override fun getItemViewType(position: Int): Int {
            if (showAdd && position == 0) return TYPE_ADD
            val pos = when (showAdd) {
                true -> position - 1
                false -> position
            }
            //检查格式
            return checkType(_mediaList[pos])
        }

        override fun getItemCount(): Int =
            when (showAdd) {
                true -> _mediaList.size + 1
                else -> _mediaList.size
            }
    }
}