package com.xld.foreignteacher.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.style.ReplacementSpan
import cn.sinata.xldutils.utils.DensityUtil

/**
 * Created by cz on 4/8/18.
 */
class RoundBackgroundColorSpan(private val context: Context, private val bgColor: Int, private val textColor: Int, private val textSize: Int) : ReplacementSpan() {
    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return paint.measureText(text, start, end).toInt() + 20
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val color1 = paint.color
        paint.color = this.bgColor
        canvas.drawRoundRect(RectF(x, (top + 1).toFloat(), x + (paint.measureText(text, start, end).toInt() + 20), (bottom - 1).toFloat()), DensityUtil.dip2px(context, 3f).toFloat(), DensityUtil.dip2px(context, 3f).toFloat(), paint)
        paint.color = this.textColor
        paint.textSize = DensityUtil.sp2px(context, textSize.toFloat()).toFloat()
        canvas.drawText(text, start, end, x + 15, (y-5).toFloat(), paint)
        paint.color = color1
    }
}