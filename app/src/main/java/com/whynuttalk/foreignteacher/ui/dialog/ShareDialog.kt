package com.whynuttalk.foreignteacher.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.umeng.socialize.bean.SHARE_MEDIA
import com.whynuttalk.foreignteacher.R

/**
 * Created by cz on 4/12/18.
 */
class ShareDialog: AppCompatDialogFragment(){
    private lateinit var onShareItemClickListener:OnShareItemClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_share, container, false)
        view.findViewById<ImageView>(R.id.img_close).setOnClickListener { dismiss() }
        view.findViewById<TextView>(R.id.tv_weibo).setOnClickListener { onShareItemClickListener.share(SHARE_MEDIA.SINA)  }
        view.findViewById<TextView>(R.id.tv_wx).setOnClickListener { onShareItemClickListener.share(SHARE_MEDIA.WEIXIN)  }
        view.findViewById<TextView>(R.id.tv_qq).setOnClickListener { onShareItemClickListener.share(SHARE_MEDIA.QQ)  }
        view.findViewById<TextView>(R.id.tv_wx_moment).setOnClickListener { onShareItemClickListener.share(SHARE_MEDIA.WEIXIN_CIRCLE)  }
        view.findViewById<TextView>(R.id.tv_qzone).setOnClickListener { onShareItemClickListener.share(SHARE_MEDIA.QZONE)  }
        view.findViewById<TextView>(R.id.tv_facebook).setOnClickListener { onShareItemClickListener.share(SHARE_MEDIA.FACEBOOK)  }
        view.findViewById<TextView>(R.id.tv_ins).setOnClickListener { onShareItemClickListener.share(SHARE_MEDIA.INSTAGRAM)  }
        return view
    }

    fun setOnShareItemClickListener(listener: OnShareItemClickListener){
        onShareItemClickListener =listener
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

    interface OnShareItemClickListener{
        fun share(type:SHARE_MEDIA)
    }
}