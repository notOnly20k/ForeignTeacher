package com.swifty.dragsquareimage;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by swifty on 9/12/2016.
 */

public abstract class ActionDialog extends AlertDialog implements DialogInterface.OnShowListener {
    protected boolean showDeleteButton;
    protected ActionDialogClick actionDialogClick;
    private Context context;

    protected ActionDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    protected ActionDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.context=context;
    }

    protected ActionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context=context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnShowListener(this);
        DisplayMetrics m = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams p = this.getWindow().getAttributes(); //获取对话框当前的参数值
        p.width = m.widthPixels; //宽度设置为屏幕
        this.getWindow().setAttributes(p); //设置生效
        Window window = this.getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(null);
    }

    @Override
    public void onShow(DialogInterface dialog) {
        if (getDeleteButtonView() == null) return;
        if (showDeleteButton()) {
            getDeleteButtonView().setVisibility(View.VISIBLE);
        } else {
            getDeleteButtonView().setVisibility(View.GONE);
        }
    }

    public abstract View getDeleteButtonView();

    public abstract ActionDialog setActionDialogClick(ActionDialogClick actionDialogClick);

    public boolean showDeleteButton() {
        return showDeleteButton;
    }

    public ActionDialog setShowDeleteButton(boolean showDeleteButton) {
        this.showDeleteButton = showDeleteButton;
        return this;
    }
}
