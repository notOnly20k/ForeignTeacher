package com.xld.foreignteacher.views

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.xld.foreignteacher.R

/**
 * Created by cz on 3/28/18.
 */

class MyDialogProgress(context: Context) : Dialog(context, R.style.DialogStyle) {

    init {
        init()
    }

    //初始化 dialog 的属性
    private fun init() {
        val progressView = LayoutInflater.from(context).inflate(R.layout.progress, null)
        val circle = progressView.findViewById<ImageView>(R.id.progressbar_circle)
        val rotateAnim = AnimationUtils.loadAnimation(context, R.anim.progress)
        val lin = LinearInterpolator()
        rotateAnim.interpolator = lin
        circle.startAnimation(rotateAnim)
        this.setContentView(progressView)
        this.setCanceledOnTouchOutside(false)
        val params = this.window!!
                .attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER
        //alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明，自身不可见。
        params.alpha = 1.0f

        // 这个是设置activity的亮度的dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗。
        params.dimAmount = 0.1f
        // 设置对话框的布局参数为居中
        this.window!!.attributes = params
    }
}
