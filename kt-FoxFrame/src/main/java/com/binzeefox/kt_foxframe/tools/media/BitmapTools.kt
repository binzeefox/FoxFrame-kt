@file:Suppress("MemberVisibilityCanBePrivate")

package com.binzeefox.kt_foxframe.tools.media

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.binzeefox.kt_foxframe.core.FoxCore
import com.binzeefox.kt_foxframe.tools.utils.LogUtil
import com.binzeefox.kt_foxframe.tools.utils.ResourcesTools
import okhttp3.internal.format
import java.io.*
import kotlin.math.roundToInt

/**
 * 位图工具
 *
 * @author 狐彻
 * 2020/09/29 11:49
 */
object BitmapTools {
    private const val TAG = "BitmapTools"

    /**
     * 保存至文件
     *
     * @param bitmap 目标位图
     * @param outputStream  存入目标的输出流
     * @param format 输出格式，默认PNG
     * @param quality 质量，默认100
     * @author 狐彻 2020/09/29 11:51
     */
    fun saveToFile(bitmap: Bitmap, outputStream: OutputStream
                   , format: Bitmap.CompressFormat, quality: Int){
        bitmap.compress(format, quality, outputStream)
        outputStream.close()
    }

    fun saveToFile(bitmap: Bitmap, outputStream: OutputStream, format: Bitmap.CompressFormat){
        saveToFile(bitmap, outputStream, format, 100)
    }

    fun saveToFile(bitmap: Bitmap, outputStream: OutputStream, quality: Int){
        saveToFile(bitmap, outputStream, Bitmap.CompressFormat.PNG, quality)
    }

    fun saveToFile(bitmap: Bitmap, outputStream: OutputStream){
        saveToFile(bitmap, outputStream, Bitmap.CompressFormat.PNG, 100)
    }

    /**
     * drawable转Bitmap
     *
     * @author 狐彻 2020/09/29 13:18
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap{
        if (drawable is BitmapDrawable)
            return drawable.bitmap

        //尺寸
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight

        //颜色格式
        val config = Bitmap.Config.ARGB_8888

        //创建Bitmap
        return Bitmap.createBitmap(w, h, config).apply {
            drawable.setBounds(0, 0, w, h)
            drawable.draw(Canvas(this))
        }
    }

    /**
     * bitmap转Drawable
     *
     * @author 狐彻 2020/09/29 13:28
     */
    fun bitmapToDrawable(bitmap: Bitmap): Drawable =
        BitmapDrawable(ResourcesTools.resources, bitmap)

    ///////////////////////////////////////////////////////////////////////////
    // Bitmap操作
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 按百分比缩放位图
     *
     * @author 狐彻 2020/09/29 13:32
     */
    fun scaleByPercent(source: Bitmap, scale: Float): Bitmap {
        val w = source.width
        val h = source.height
        return Bitmap.createScaledBitmap(source, (w*scale).roundToInt()
            , (h*scale).roundToInt(), true)
    }

    /**
     * 旋转位图
     *
     * @author 狐彻 2020/09/29 13:33
     */
    fun rotate(bitmap: Bitmap, degrees: Number): Bitmap {

        // 旋转角度矩阵
        val matrix = Matrix()
        matrix.postRotate(degrees as Float)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /**
     * 位图压缩
     *
     * @author 狐彻 2020/09/29 13:51
     */
    fun compress(bitmap: Bitmap, format: Bitmap.CompressFormat, width: Int, height: Int): Bitmap? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(format, 100, baos)  //不压缩质量
        val bais = ByteArrayInputStream(baos.toByteArray())

        val options = BitmapFactory.Options().apply {
            this.inPreferredConfig = Bitmap.Config.RGB_565
            this.inJustDecodeBounds = true
        }
        BitmapFactory.decodeStream(bais, null, options)

        options.inSampleSize = calculateSampleSize(options, width, height)
        options.inMutable = true
        options.inBitmap = Bitmap.createBitmap(options.outWidth, options.outHeight, Bitmap.Config.RGB_565)
        options.inJustDecodeBounds = false  //改为需要取图

        bais.reset()
        return BitmapFactory.decodeStream(bais, null, options)
    }

    ///////////////////////////////////////////////////////////////////////////
    // 私有方法
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 计算最合适的取样值
     *
     * @author 狐彻 2020/09/29 13:56
     */
    private fun calculateSampleSize(options: BitmapFactory.Options, width: Int, height: Int): Int {
        val w = options.outWidth
        val h = options.outHeight
        var inSampleSize = 1
        if (h > height || w > width){
            val hRatio = (h.toFloat() / height.toFloat()).roundToInt()
            val wRatio = (w.toFloat() / width.toFloat()).roundToInt()
            inSampleSize = Math.min(hRatio, wRatio)
        }
        return inSampleSize
    }
}