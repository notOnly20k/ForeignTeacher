package com.whynuttalk.foreignteacher.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.TextView
import com.whynuttalk.foreignteacher.R

/**
 * Created by cz on 3/30/18.
 */
class CustomDialog : AppCompatDialogFragment() {


    private var customDialogListener: CustomDialogListener? = null
    private lateinit var title: TextView
    private lateinit var button1: TextView
    private lateinit var button2: TextView
    private var titleText: String = ""
    private var isShowTitle = true
    private var isShowButton2 = true
    private var button1Text: String = "Yes"
    private var button2Text: String = "No"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_order, container, false)
        title = view.findViewById(R.id.tv_title)
        if (titleText.isNotEmpty()) {
            title.text = titleText
        }
        if (!isShowTitle) {
            title.visibility = View.GONE
        }
        button1 = view.findViewById(R.id.tv_sure)
        button2 = view.findViewById(R.id.tv_cancel)
        if (!isShowButton2) {
            button2.visibility = View.GONE
        }
        button1.text = button1Text
        button2.text = button2Text
        button1.setOnClickListener {
            customDialogListener?.clickButton1(this)
        }
        button2.setOnClickListener {
            customDialogListener?.clickButton2(this)
        }

        return view
    }

    fun showtitle(boolean: Boolean): CustomDialog {
        isShowTitle = boolean
        return this
    }

    fun showButton2(boolean: Boolean): CustomDialog {
        isShowButton2 = boolean
        return this
    }

    fun setTitle(string: String): CustomDialog {
        titleText = string
        return this
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.CustomBottomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_select_sex)
        dialog.setCanceledOnTouchOutside(true)

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        val window = dialog.window
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = wlp

        return dialog
    }

    fun setButton1Text(string: String): CustomDialog {
        button1Text = string
        return this
    }

    fun setButton2Text(string: String): CustomDialog {
        button2Text = string
        return this
    }


    fun setDialogListene(CustomDialogListener: CustomDialogListener): CustomDialog {
        this.customDialogListener = CustomDialogListener
        return this
    }


    interface CustomDialogListener {
        fun clickButton1(customDialog: CustomDialog)
        fun clickButton2(customDialog: CustomDialog)
    }

    class Builder {
        fun create(): CustomDialog {
            return CustomDialog()
        }
    }

}
