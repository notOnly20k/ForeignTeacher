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
    private var state = NORMAL
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
        state = typedArray.getInt(R.styleable.ScheduleDateTextView_state, NORMAL)
        showDiscount = typedArray.getBoolean(R.styleable.ScheduleDateTextView_show_discount, false)
        rbDate.text = typedArray.getText(R.styleable.ScheduleDateTextView_date_text)
        tvDiscount.text = typedArray.getText(R.styleable.ScheduleDateTextView_discount)
        isShowDiscount(showDiscount)
        changByState(state)
        typedArray.recycle()

        rbDate.setOnClickListener { view ->
            isChecked = !isChecked
            onScheduleDateTextViewClickListner!!.click(isChecked)
        }
    }

    fun changByState(state: Int) {
        when (state) {
            OPEN -> {
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_fill)
            }
            NORMAL -> {
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.color.white)
            }
            DISABLE -> {
                tvDiscount.visibility = View.GONE
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_enable)
            }
            EDIT -> {
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_edit)
                tvDiscount.visibility = View.GONE
            }
        }
    }


    fun isShowDiscount(isShow: Boolean) {
        showDiscount = isShow
        if (isShow) {
            tvDiscount.visibility = View.VISIBLE
        } else {
            tvDiscount.visibility = View.GONE
        }
    }

    fun setDisCount(string: String) {
        tvDiscount.text = "$string%"
    }


    fun setOnClickListener(onScheduleDateTextViewClickListner: OnScheduleDateTextViewClickListner) {
        this.onScheduleDateTextViewClickListner = onScheduleDateTextViewClickListner
    }


    interface OnScheduleDateTextViewClickListner {
        fun click(isChecked: Boolean)
    }

    companion object {

        val OPEN = 2
        val NORMAL = 1
        val DISABLE = 0
        val EDIT = 3
    }

}
