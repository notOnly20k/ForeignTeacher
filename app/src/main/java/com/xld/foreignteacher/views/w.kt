package com.xld.foreignteacher.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckedTextView
import android.widget.FrameLayout
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.xld.foreignteacher.R

/**
 * Created by cz on 4/1/18.
 */
class w: FrameLayout {
    @BindView(R.id.tv_content)
    lateinit var tvContent: CheckedTextView
    @BindView(R.id.iv_choose)
    lateinit var ivChoose: ImageView
    constructor(context: Context) : super(context)


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InformItemView)
        val text = typedArray.getString(R.styleable.InformItemView_text)
        var checked = typedArray.getBoolean(R.styleable.InformItemView_checked, false)
        typedArray.recycle()
        tvContent.text = text
        this.checked = checked
    }


    var checked: Boolean
        get() = tvContent.isChecked
        set(checked) {
            ivChoose.visibility = if (checked) View.VISIBLE else View.INVISIBLE
            tvContent.isChecked = checked
        }


    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_inform_item, this, true)
        ButterKnife.bind(this, view)

    }
}
