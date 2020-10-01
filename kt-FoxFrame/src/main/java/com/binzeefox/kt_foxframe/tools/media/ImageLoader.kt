package com.binzeefox.kt_foxframe.tools.media

import android.annotation.TargetApi
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import com.binzeefox.kt_foxframe.tools.utils.ResourcesTools
import com.binzeefox.kt_foxframe.tools.utils.UriTools
import java.io.File
import java.nio.ByteBuffer

/**
 * 图片加载器
 *
 * 基于
 * @see ImageDecoder
 * @author 狐彻
 * 2020/09/29 14:04
 */
@TargetApi(Build.VERSION_CODES.P)
class ImageLoader private constructor(val source: ImageDecoder.Source) {

    // 静态池
    // @author 狐彻 2020/09/29 14:06
    companion object {

        /**
         * 静态获取
         *
         * @author 狐彻 2020/09/29 14:15
         */
        fun get(source: Any): ImageLoader =
            ImageLoader(makeSource(source))

        ///////////////////////////////////////////////////////////////////////////
        // 私有方法
        ///////////////////////////////////////////////////////////////////////////

        /**
         * 创造来源
         *
         * @author 狐彻 2020/09/29 14:16
         */
        private fun makeSource(source: Any): ImageDecoder.Source =
            when (source) {
                is ImageDecoder.Source -> source
                is Int -> ImageDecoder.createSource(ResourcesTools.resources, source)
                is Uri -> ImageDecoder.createSource(UriTools.resolver, source)
                is ByteBuffer -> ImageDecoder.createSource(source)
                is File -> ImageDecoder.createSource(source)
                else -> throw RuntimeException("加载失败，类型错误")
            }
    }

    // 不完全解码监听器，若出发并返回true，则展示不完整图片，缺损区域是空白
    // @author 狐彻 2020/09/29 14:23
    var partialImageListener: ImageDecoder.OnPartialImageListener? = null

    // 图片预处理者
    // @author 狐彻 2020/09/29 14:24
    var postProcessor: PostProcessor? = null

    // 头监听器数组
    // @author 狐彻 2020/09/29 14:21
    private val decodeListeners = ArrayList<ImageDecoder.OnHeaderDecodedListener>()

    // 储存设置监听器脚标
    // @author 狐彻 2020/09/29 14:26
    private var mSizeIndex = -1

    // 清晰度监听器脚标
    // @author 狐彻 2020/09/29 14:27
    private var mSampleSizeIndex = -1

    // 真正的头监听实现
    // @author 狐彻 2020/09/29 14:31
    private val mHeadListener =
        ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
            postProcessor?.also {
                decoder.postProcessor = it
            }
            partialImageListener?.also {
                decoder.onPartialImageListener = partialImageListener
            }
            for (listener in decodeListeners)
                listener.onHeaderDecoded(decoder, info, source)
        }

    /**
     * 一句话设置圆角，会替换之前设置的PostProcessor
     *
     * @author 狐彻 2020/09/29 14:32
     */
    fun roundCorners(roundX: Float, roundY: Float): ImageLoader {
        postProcessor = PostProcessor { canvas ->
            val path = Path()
            path.fillType = Path.FillType.INVERSE_EVEN_ODD
            val width = canvas.width
            val height = canvas.height
            path.addRoundRect(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                roundX,
                roundY,
                Path.Direction.CW
            )
            val paint = Paint().apply {
                this.isAntiAlias = true
                this.color = Color.TRANSPARENT
                this.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
            }
            canvas.drawPath(path, paint)
            PixelFormat.TRANSLUCENT
        }
        return this
    }

    /**
     * 添加头解码监听器
     *
     * @author 狐彻 2020/09/29 14:38
     */
    fun addHeadDecodeListener(listener: ImageDecoder.OnHeaderDecodedListener): ImageLoader {
        if (!decodeListeners.contains(listener))
            decodeListeners.add(listener)
        return this
    }

    /**
     * 设置局部加载监听器
     *
     * @author 狐彻 2020/09/29 14:40
     */
    fun setPartialImageListener(listener: ImageDecoder.OnPartialImageListener): ImageLoader {
        partialImageListener = listener
        return this
    }

    /**
     * 设置预处理器
     *
     * @author 狐彻 2020/09/29 14:41
     */
    fun setPostProcessor(processor: PostProcessor): ImageLoader {
        postProcessor = processor
        return this
    }

    /**
     * 设置取样率
     *
     * @author 狐彻 2020/09/29 14:42
     */
    fun setSampleSize(sampleSize: Int): ImageLoader {
        if (mSampleSizeIndex != -1)
            decodeListeners.removeAt(mSampleSizeIndex)
        mSampleSizeIndex = decodeListeners.size
        addHeadDecodeListener { decoder, _, _ ->
            decoder.setTargetSampleSize(sampleSize)
        }
        return this
    }

    /**
     * 尺寸设置取样率
     *
     * @author 狐彻 2020/09/29 14:47
     */
    fun setSampleSize(width: Int, height: Int): ImageLoader {
        if (mSizeIndex != -1) decodeListeners.removeAt(mSizeIndex)
        mSizeIndex = decodeListeners.size
        decodeListeners.add(ImageDecoder.OnHeaderDecodedListener { decoder, _, _ ->
            decoder.setTargetSize(
                width,
                height
            )
        })
        return this
    }

    /**
     * 解码为Drawable
     *
     * @author 狐彻 2020/09/29 14:48
     */
    fun decodeDrawable(): Drawable =
        ImageDecoder.decodeDrawable(source, mHeadListener)

    /**
     * 解码为Bitmap
     *
     * @author 狐彻 2020/09/29 14:50
     */
    fun decodeBitmap(): Bitmap =
        ImageDecoder.decodeBitmap(source, mHeadListener)
}