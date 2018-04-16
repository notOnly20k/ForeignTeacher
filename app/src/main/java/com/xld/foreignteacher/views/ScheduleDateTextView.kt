package com.xld.foreignteacher.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.xld.foreignteacher.R
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/16/18.
 */
class ScheduleDateTextView @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private var tvDiscount: TextView
    private var rbDate: TextView
    private val logger = LoggerFactory.getLogger("ScheduleDateTextView")
    private var editable = false
    private var fill = false
    private var showDiscount = false
    private var onScheduleDateTextViewClickListner: OnScheduleDateTextViewClickListner? = null
    private var isChecked = false


    init {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_schedule_text, this, true)
        rbDate = view.findViewById(R.id.rb_date)
        tvDiscount = view.findViewById(R.id.tv_discount)
        rbDate.setBackgroundResource(R.color.white)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleDateTextView, defStyleAttr, 0)
        editable = typedArray.getBoolean(R.styleable.ScheduleDateTextView_editable, false)
        fill = typedArray.getBoolean(R.styleable.ScheduleDateTextView_fill, false)
        showDiscount = typedArray.getBoolean(R.styleable.ScheduleDateTextView_show_discount, false)
        rbDate.text = typedArray.getText(R.styleable.ScheduleDateTextView_date_text)
        tvDiscount.text = typedArray.getText(R.styleable.ScheduleDateTextView_discount)
        if (showDiscount) {
            tvDiscount.visibility = View.VISIBLE
        } else {
            tvDiscount.visibility = View.GONE
        }
        if (fill) {
            rbDate.setBackgroundResource(R.drawable.bg_schedule_text_fill)
        } else {
            rbDate.setBackgroundResource(R.color.white)
        }
        typedArray.recycle()
        if (editable) {
            rbDate.setOnClickListener { view ->
                isChecked = !isChecked
                onScheduleDateTextViewClickListner?.click(isChecked)
                if (isChecked) {
                    rbDate.setBackgroundResource(R.drawable.bg_schedule_text_edit)
                    tvDiscount.visibility = View.GONE
                } else {
                    if (fill) {
                        tvDiscount.visibility = View.VISIBLE
                        rbDate.setBackgroundResource(R.drawable.bg_schedule_text_fill)
                    } else {
                        tvDiscount.visibility = View.VISIBLE
                        rbDate.setBackgroundResource(R.color.white)
                    }
                }
            }
        }
    }

    fun setOnClickListener(onScheduleDateTextViewClickListner: OnScheduleDateTextViewClickListner) {
        this.onScheduleDateTextViewClickListner = onScheduleDateTextViewClickListner
    }

    interface OnScheduleDateTextViewClickListner {
        fun click(boolean: Boolean)
    }

}
