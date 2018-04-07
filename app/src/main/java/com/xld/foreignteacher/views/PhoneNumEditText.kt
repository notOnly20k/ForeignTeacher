package com.xld.foreignteacher.views

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet

/**
 * Created by cz on 3/28/18.
 */
class PhoneNumEditText : android.support.v7.widget.AppCompatEditText {
    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_PHONE
        addTextChangedListener(PhoneNumberTextWatcher())
    }

    private inner class PhoneNumberTextWatcher : TextWatcher {
        internal var lastContentLength = 0
        internal var isDelete = false

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val sb = StringBuffer(s)
            //是否为输入状态
            isDelete = s.length <= lastContentLength

            //输入是第4，第9位，这时需要插入空格
            if (!isDelete && (s.length == 4 || s.length == 9)) {
                if (s.length == 4) {
                    sb.insert(3, "-")
                } else {
                    sb.insert(8, "-")
                }
                setContent(sb)
            }

            //删除的位置到4，9时，剔除空格
            if (isDelete && (s.length == 4 || s.length == 9)) {
                sb.deleteCharAt(sb.length - 1)
                setContent(sb)
            }

            lastContentLength = sb.length
        }

        override fun afterTextChanged(s: Editable) {

        }

        /**
         * 添加或删除空格EditText的设置
         *
         * @param sb
         */
        private fun setContent(sb: StringBuffer) {
            setText(sb.toString())
            //移动光标到最后面
            setSelection(sb.length)
        }
    }

}
