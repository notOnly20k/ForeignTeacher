package com.xld.foreignteacher.ui.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import com.swifty.dragsquareimage.ActionDialog
import com.swifty.dragsquareimage.ActionDialogClick
import com.xld.foreignteacher.R

/**
 * Created by cz on 3/30/18.
 */
class MyActionDialog : ActionDialog {

    constructor(context: Context) : super(context) {
        init()
    }


    constructor(context: Context, theme: Int) : super(context, theme) {
        init()
    }

    constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener) : super(context, cancelable, cancelListener) {
        init()
    }

    private fun init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_my_action)

        findViewById<View>(com.swifty.dragsquareimage.R.id.pick_image)!!.setOnClickListener { v ->
            if (actionDialogClick != null) actionDialogClick.onPickImageClick(v)
            dismiss()
        }
        findViewById<View>(com.swifty.dragsquareimage.R.id.delete)!!.setOnClickListener { v ->
            if (actionDialogClick != null) actionDialogClick.onDeleteClick(v)
            dismiss()
        }
        findViewById<View>(com.swifty.dragsquareimage.R.id.take_photo)!!.setOnClickListener { v ->
            if (actionDialogClick != null) actionDialogClick.onTakePhotoClick(v)
            dismiss()
        }
    }


    override fun getDeleteButtonView(): View {
        return findViewById<Button>(R.id.delete)!!
    }

    override fun setActionDialogClick(actionDialogClick: ActionDialogClick): ActionDialog {
        this.actionDialogClick = actionDialogClick
        return this
    }

}