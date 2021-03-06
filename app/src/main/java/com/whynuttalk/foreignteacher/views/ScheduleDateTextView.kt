package com.whynuttalk.foreignteacher.views

import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.whynuttalk.foreignteacher.R
import org.slf4j.LoggerFactory

/**
 * Created by cz on 4/16/18.
 */
class ScheduleDateTextView @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    private var tvDiscount: TextView
    private var rbDate: TextView
    private val logger = LoggerFactory.getLogger("ScheduleDateTextView")
    private var editable = false
    private var state = NORMAL
    private var showDiscount = false
    private var onScheduleDateTextViewClickListner: OnScheduleDateTextViewClickListner? = null
    private var isChecked = false
    private var classState: Int = NORMAL


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
            onScheduleDateTextViewClickListner?.click(isChecked)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width=MeasureSpec.getSize(widthMeasureSpec)/3
        val height=MeasureSpec.getSize(heightMeasureSpec)
        val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun changByState(state: Int) {
        when (state) {
            OPEN -> {
                classState = OPEN
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_fill)
            }
            NORMAL -> {
                classState = NORMAL
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.color.white)
            }
            DISABLE -> {
                classState = DISABLE
                tvDiscount.visibility = View.GONE
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_enable)
            }
            EDIT -> {
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_edit)
                tvDiscount.visibility = View.GONE
            }
            OPENLEFT -> {
                classState = OPEN
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_fill_left)
            }
            OPENRIGHT -> {
                classState = OPEN
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_fill_right)
            }
            OPENCENTER -> {
                classState = OPEN
                isShowDiscount(showDiscount)
                rbDate.setBackgroundColor(context.resources.getColor(R.color.yellow_ffcc00_alpha))
            }
            DISABLECENTER -> {
                classState = DISABLE
                isShowDiscount(showDiscount)
                rbDate.setBackgroundColor(context.resources.getColor(R.color.grey_f1f1f1))
            }
            DISABLELEFT -> {
                classState = DISABLE
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_enable_left)
            }
            DISABLERIGHT -> {
                classState = DISABLE
                isShowDiscount(showDiscount)
                rbDate.setBackgroundResource(R.drawable.bg_schedule_text_enable_right)
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
        val count= SpannableString("$string%")
        count.setSpan(RelativeSizeSpan(1.2f),0,string.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tvDiscount.text = count
    }

    fun getState(): Int {
        return state
    }

    fun getClassState(): Int {
        return classState
    }

    fun setButtonCheckState(boolean: Boolean) {
        isChecked = boolean
    }


    fun setOnClickListener(onScheduleDateTextViewClickListner: OnScheduleDateTextViewClickListner) {
        this.onScheduleDateTextViewClickListner = onScheduleDateTextViewClickListner
    }


    interface OnScheduleDateTextViewClickListner {
        fun click(isChecked: Boolean)
    }

    companion object {
        val EDIT = 3
        val OPEN = 2
        val NORMAL = 1
        val DISABLE = 0
        val OPENLEFT = 4
        val OPENRIGHT = 5
        val OPENCENTER = 6
        val DISABLELEFT = 7
        val DISABLERIGHT = 8
        val DISABLECENTER = 9


    }

}
