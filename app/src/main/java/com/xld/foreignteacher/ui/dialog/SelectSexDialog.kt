package com.xld.foreignteacher.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.TextView
import com.xld.foreignteacher.R



/**
 * Created by cz on 3/30/18.
 */
class SelectSexDialog : AppCompatDialogFragment() {



    private var selectSexListener: SelectSexListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view=inflater.inflate(R.layout.dialog_select_sex,container,false)
        view.findViewById<TextView>(R.id.tv_female).setOnClickListener {
            selectSexListener?.isFemale((it as TextView).text.toString())
            dismiss()
        }
        view.findViewById<TextView>(R.id.tv_male).setOnClickListener {
            selectSexListener?.isFemale((it as TextView).text.toString())
            dismiss()
        }

        return view
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



    fun setDialogListene(selectSexListener: SelectSexListener) {
        this.selectSexListener=selectSexListener
    }


    interface SelectSexListener{
        fun isFemale(string: String)
    }

}

