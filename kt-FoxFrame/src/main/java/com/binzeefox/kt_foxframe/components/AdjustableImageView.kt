package com.binzeefox.kt_foxframe.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.ceil

/**
 * 自适应图片视图
 *
 * @author 狐彻
 * 2020/09/29 19:26
 */
class AdjustableImageView: AppCompatImageView {

    // 尺寸变化监听器
    // @author 狐彻 2020/09/29 19:29
    interface OnSizeChangedListener {
        fun onSizeChanged(width: Int, height: Int)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    // 尺寸变化监听器
    // @author 狐彻 2020/09/29 19:29
    var sizeChangedListener: OnSizeChangedListener? = null

    /**
     * 测量回调
     *
     * @author 狐彻 2020/09/29 19:32
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val d: Drawable? = drawable    //获取图片
        if (d == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }

        // 尺寸
        // @author 狐彻 2020/09/29 19:31
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        var measures = fitWidth(widthMeasureSpec)


        // height非确定值，则宽自适应
        // @author 狐彻 2020/09/29 19:34
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            width = measures[0] ?: width
            height = measures[1] ?: height
            sizeChangedListener?.onSizeChanged(width, height)
            return
        }

        // 先宽自适应，若长度超标，则长自适应
        // 1.宽自适应
        // @author 狐彻 2020/09/29 19:38
        width = measures[0]
        height = measures[1]

        // 2.长度超标，则长度自适应
        if (height > MeasureSpec.getSize(heightMeasureSpec)) {
            measures = fitHeight(heightMeasureSpec)
            width = measures[0]
            height = measures[1]
        }

        setMeasuredDimension(width, height)
        sizeChangedListener?.onSizeChanged(width, height)
    }

    /**
     * 长自适应
     *
     * @author 狐彻 2020/09/29 19:43
     */
    private fun fitHeight(heightMeasureSpec: Int): Array<Int> {
        var width = 0
        var height = 0
        val d = drawable

        height = MeasureSpec.getSize(heightMeasureSpec)
        width = ceil(
            height.toFloat() * d.intrinsicWidth / d.intrinsicHeight
        ).toInt()

        return arrayOf(width, height)
    }

    /**
     * 宽度自适应
     *
     * @author 狐彻 2020/09/29 19:34
     */
    private fun fitWidth(widthMeasureSpec: Int): Array<Int> {
        var width = 0
        var height = 0
        val d = drawable

        width = MeasureSpec.getSize(widthMeasureSpec)
        height = ceil(
            width.toFloat() * d.intrinsicHeight / d.intrinsicWidth
        ).toInt()

        return arrayOf(width, height)
    }
}