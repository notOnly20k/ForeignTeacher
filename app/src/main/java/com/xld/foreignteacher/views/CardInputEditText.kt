package com.xld.foreignteacher.views

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet

/**
 * Created by cz on 4/27/18.
 */
class CardInputEditText: android.support.v7.widget.AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    fun init(){
        inputType = InputType.TYPE_CLASS_NUMBER
        addTextChangedListener(watcher)
    }

    private val watcher = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            if (s == null) {
                return
            }
            //判断是否是在中间输入，需要重新计算
            val isMiddle = start + count < s.length
            //在末尾输入时，是否需要加入空格
            var isNeedSpace = false
            if (!isMiddle && s.isNotEmpty() && s.length % 5 == 0) {
                isNeedSpace = true
            }
            if (isMiddle || isNeedSpace) {
                var newStr = s.toString()
                newStr = newStr.replace("-", "")
                val sb = StringBuilder()
                var i = 0
                while (i < newStr.length) {
                    if (i > 0) {
                        sb.append("-")
                    }
                    if (i + 4 <= newStr.length) {
                        sb.append(newStr.substring(i, i + 4))
                    } else {
                        sb.append(newStr.substring(i, newStr.length))
                    }
                    i += 4
                }
                removeTextChangedListener(this)
                setText(sb)
                //如果是在末尾的话,或者加入的字符个数大于零的话（输入或者粘贴）
                if (!isMiddle || count > 1) {
                    setSelection(sb.length)
                } else if (isMiddle) {
                    //如果是删除
                    if (count == 0) {
                        //如果删除时，光标停留在空格的前面，光标则要往前移一位
                        if ((start - before + 1) % 5 == 0) {
                            setSelection(if (start - before > 0) start - before else 0)
                        } else {
                            setSelection(if (start - before + 1 > sb.length) sb.length else start - before + 1)
                        }
                    } else {
                        if ((start - before + count) % 5 == 0) {
                            setSelection(if (start + count - before + 1 < sb.length) start + count - before + 1 else sb.length)
                        } else {
                            setSelection(start + count - before)
                        }
                    }//如果是增加
                }
                addTextChangedListener(this)
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    fun getTextWithoutSpace(): String {
        return super.getText().toString().replace("-", "")
    }
}