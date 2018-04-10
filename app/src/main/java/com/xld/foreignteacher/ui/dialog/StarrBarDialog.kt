package com.xld.foreignteacher.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.TextView
import com.xld.foreignteacher.R
import com.xld.foreignteacher.views.StarBarView

/**
 * Created by cz on 3/30/18.
 */
class StarrBarDialog : AppCompatDialogFragment() {



    private lateinit var selectStarListener: SelectStarListener
    private var rating= 0F


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view=inflater.inflate(R.layout.dialog_star_bar,container,false)
        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
            dismiss()
        }
        val starBar=view.findViewById<StarBarView>(R.id.sbv_starbar)
        starBar.setStarRating(rating)
        view.findViewById<TextView>(R.id.tv_ok).setOnClickListener {
            selectStarListener.onOkClick( starBar.getSartRating())
            dismiss()
        }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!, R.style.CustomBottomDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_star_bar)
        dialog.setCanceledOnTouchOutside(true)

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        val window = dialog.window
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = wlp

        return dialog
    }


    fun setRating(rating:Float){
        this.rating=rating
    }



    fun setDialogListene(selectStarListener: SelectStarListener) {
        this.selectStarListener=selectStarListener
    }


    interface SelectStarListener{
       fun onOkClick(level:Float)
    }

}