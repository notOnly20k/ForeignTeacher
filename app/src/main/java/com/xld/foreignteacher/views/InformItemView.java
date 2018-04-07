package com.xld.foreignteacher.views;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.xld.foreignteacher.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lmw on 2018/3/22.
 */

public class InformItemView extends FrameLayout {
    @BindView(R.id.tv_content)
    CheckedTextView tvContent;
    @BindView(R.id.fl_item)
    FrameLayout flItem;
    @BindView(R.id.iv_choose)
    ImageView ivChoose;


    public InformItemView(@NonNull Context context) {
        this(context, null);
    }

    public InformItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InformItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InformItemView);
        String text = typedArray.getString(R.styleable.InformItemView_text);
        boolean checked = typedArray.getBoolean(R.styleable.InformItemView_checked, false);
        typedArray.recycle();
        tvContent.setText(text);
        setChecked(checked);
    }


    public void setText(String string){
        tvContent.setText(string);
    }

    public String getText(){
        return tvContent.getText().toString();
    }

    public FrameLayout getFrameLayout(){
        return flItem;
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_inform_item, this, true);
        ButterKnife.bind(this,view);
    }

    public void setChecked(boolean checked) {
        ivChoose.setVisibility(checked ? VISIBLE : INVISIBLE);
        tvContent.setChecked(checked);
    }
    public boolean getChecked(){
        return tvContent.isChecked();
    }
}
